package net.betaville.ng.net;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ConcurrentModificationException;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import net.betaville.ng.SettingsPreferences;
import net.betaville.ng.periodicupdater.AbstractUpdater;

import org.apache.log4j.Logger;

/** Maintains the network connections throughout the application so that
 * unnecessary connections aren't created.
 * @author Skye Book
 *
 */
public class NetPool extends AbstractUpdater {
	private static Logger logger = Logger.getLogger(NetPool.class);
	private static NetPool netPool = new NetPool(90000);

	private Vector<ClientManager> managers;

	private boolean autoCleanupEnabled=true;

	private AtomicBoolean stopAcceptingRequests = new AtomicBoolean(false);

	private boolean isInUpdate = false;

	/**
	 * 
	 */
	private NetPool(long interval){
		super(interval);
		managers = new Vector<ClientManager>();
	}

	public InsecureClientManager getConnection() throws UnknownHostException, IOException{

		if(stopAcceptingRequests.get()) return null;

		// If there are no managers, create one and return it
		if(managers.isEmpty()){
			logger.info("No client found. Creating " + InsecureClientManager.class.getName());
			InsecureClientManager icm = new InsecureClientManager();
			managers.add(icm);
			icm.busy.set(true);
			return icm;
		}

		// Where there is a list of managers, find an idle manager and return it
		for(ClientManager m : managers){
			if(!m.isBusy() && m instanceof InsecureClientManager){
				((InsecureClientManager)m).busy.set(true);
				return (InsecureClientManager)m;
			}
		}

		// If no idle managers were found, then we add a new manager to the pool and return that
		logger.info("No idle client found. Creating " + InsecureClientManager.class.getName());
		InsecureClientManager icm = new InsecureClientManager();
		managers.add(icm);
		icm.busy.set(true);
		return icm;
	}

	public SecureClientManager getSecureConnection() throws UnknownHostException, IOException{

		if(stopAcceptingRequests.get()) return null;

		// If there are no managers, create one and return it
		if(managers.isEmpty()){
			logger.info("No client found. Creating " + SecureClientManager.class.getName());
			SecureClientManager scm;
			if(SettingsPreferences.useSSL()) scm = new SSLClientManager();
			else scm = new SecureClientManager(true);
			managers.add(scm);
			scm.busy.set(true);
			return scm;
		}

		// Where there is a list of managers, find an idle manager and return it
		for(UnprotectedManager m : managers){
			if(!m.isBusy() && m instanceof SecureClientManager){
				((SecureClientManager)m).busy.set(true);
				return (SecureClientManager)m;
			}
		}

		// If no idle managers were found, then we add a new manager to the pool and return that
		logger.info("No idle client found. Creating " + SecureClientManager.class.getName());
		SecureClientManager scm;
		if(SettingsPreferences.useSSL()) scm = new SSLClientManager();
		else scm = new SecureClientManager(true);
		managers.add(scm);
		scm.busy.set(true);
		return scm;
	}

	public synchronized void cleanAll(){
		try{
			for(UnprotectedManager manager : managers){
				while(manager.isBusy()){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				manager.close();
			}
			managers.clear();
			if(managers.size()==0) logger.info("All Network Connections Closed");
		} catch(ConcurrentModificationException e){
			logger.warn("ConcurrentModificationException");
		}
	}

	public static NetPool getPool(){
		return netPool;
	}

	public void doUpdate(){
		isInUpdate=true;
		logger.info("Performing network connection cleanup ("+managers.size()+" currently)");
		int startingTotal = managers.size();
		// This is reserved for the first idle connection we come across, it will be saved
		int firstIdle=-1;

		for(int i=0; i<managers.size(); i++){
			if(!managers.get(i).isBusy()){
				// If this is the first idle connection we encounter, save it
				if(firstIdle==-1){
					firstIdle=i;
				}
				else{
					// Remove extra idle managers
					logger.info("Closing " + managers.get(i).getClass().getName());
					managers.get(i).close();
					managers.remove(i);
					if(i==managers.size()-1){
						break;
					}
					else{
						i--;
					}
				}
			}
		}

		logger.info("Network cleanup complete: " + (startingTotal-managers.size()) + " connections dropped");
		isInUpdate=false;
	}

	public boolean isUpdateRequired() {
		return autoCleanupEnabled;
	}

	@Override
	public boolean isInUpdate() {
		return isInUpdate;
	}

	/**
	 * @return the autoCleanupEnabled
	 */
	public boolean isAutoCleanupEnabled() {
		return autoCleanupEnabled;
	}

	/**
	 * @param autoCleanupEnabled the autoCleanupEnabled to set
	 */
	public void setAutoCleanupEnabled(boolean autoCleanupEnabled) {
		this.autoCleanupEnabled = autoCleanupEnabled;
	}
}
