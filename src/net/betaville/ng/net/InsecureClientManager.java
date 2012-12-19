package net.betaville.ng.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.betaville.ng.SettingsPreferences;

import org.apache.log4j.Logger;

/**
 * Class <ClientManager> - Manage the connection and requests of the client to
 * the server
 * 
 * @author Caroline Bouchat
 * @author Skye Book
 */
public class InsecureClientManager extends ClientManager{
	private static Logger logger = Logger.getLogger(InsecureClientManager.class);

	/**
	 * Constant <PORT_SERVER> - Port of the server
	 */
	private final int PORT_SERVER = 14500;
	
	/**
	 * Constructor - Creation of the client manager
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public InsecureClientManager() throws UnknownHostException, IOException{
		this(SettingsPreferences.getServerIP());
	}
	
	public InsecureClientManager(String serverIP) throws UnknownHostException, IOException{
			clientSocket = new Socket(serverIP, PORT_SERVER);
			logger.info("Client application : "+ clientSocket.toString());
			progressOutput = new ProgressOutputStream(clientSocket.getOutputStream());
			output = new ObjectOutputStream(progressOutput);
			
			progressInput = new ProgressInputStream(clientSocket.getInputStream());
			input = new ObjectInputStream(progressInput);
	}
}