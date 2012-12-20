package net.betaville.ng.net;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;

import net.betaville.ng.ResourceLoader;
import net.betaville.ng.SettingsPreferences;

import org.apache.log4j.Logger;

/**
 * @author Skye Book
 *
 */
public class SSLClientManager extends SecureClientManager{
	private static Logger logger = Logger.getLogger(SSLClientManager.class);

	/**
	 * Constant <PORT_SERVER> - Port of the server
	 */
	private final int PORT_SERVER = 14501;

	private char[] keyStorePass = "123456".toCharArray();
	private char[] trustStorePass = keyStorePass;

	/**
	 * Constructor - Creation of the client manager
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public SSLClientManager() throws UnknownHostException, IOException{
		super(false);
		try{
			KeyStore keyStore = KeyStore.getInstance("JKS");
			KeyStore trustStore = KeyStore.getInstance("JKS");

			keyStore.load(ResourceLoader.loadResource("/data/certs/client.keystore").openStream(), keyStorePass);
			trustStore.load(ResourceLoader.loadResource("/data/certs/client.truststore").openStream(), trustStorePass);

			KeyManagerFactory keyManager = KeyManagerFactory.getInstance("SunX509");
			keyManager.init(keyStore, keyStorePass);
			TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
			trustManager.init(trustStore);
			
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(keyManager.getKeyManagers(), trustManager.getTrustManagers(), null);
			//context.init(keyManager.getKeyManagers(), trustAll, null);

			SSLSocketFactory sslFactory = context.getSocketFactory();
			clientSocket = (SSLSocket)sslFactory.createSocket();

			clientSocket.connect(new InetSocketAddress(SettingsPreferences.getServerIP(), PORT_SERVER));
			progressOutput = new ProgressOutputStream(clientSocket.getOutputStream());
			output = new ObjectOutputStream(progressOutput);
			
			progressInput = new ProgressInputStream(clientSocket.getInputStream());
			input = new ObjectInputStream(progressInput);
		}catch(KeyStoreException e){
			logger.fatal("Java KeyStore Issue", e);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			logger.fatal("Could not connect to server at "+SettingsPreferences.getServerIP(), e);
			JOptionPane.showMessageDialog(null, "Could not connect to server at "+SettingsPreferences.getServerIP());
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	public static TrustManager[] createTrustAll() {
		// Create a trust manager that does not validate certificate chains
		return new TrustManager[] { new X509TrustManager() {

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				return;
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				return;
			}

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
		}};

	}


}
