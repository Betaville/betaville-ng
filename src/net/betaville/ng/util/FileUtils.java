package net.betaville.ng.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Skye Book
 *
 */
public class FileUtils {
	
	private static final int BUFFER_SIZE = 4096;
	
	/**
	 * 
	 * @param sourceFile
	 * @param destinationFile
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(File sourceFile, File destinationFile) throws IOException{
		FileInputStream fis = new FileInputStream(sourceFile);
		FileOutputStream fos = new FileOutputStream(destinationFile);
		
		int bufferSizeForThisUse = BUFFER_SIZE;
		
		byte[] readBuffer = new byte[bufferSizeForThisUse];
		int n=-1;
		while ((n = fis.read(readBuffer, 0, bufferSizeForThisUse)) != -1){
			fos.write(readBuffer, 0, n);
		}
		fis.close();
		fos.close();
		return true;
	}
	
	/**
	 * 
	 * @param sourceFile
	 * @param destinationFile
	 * @return
	 * @throws IOException
	 */
	public static boolean moveFile(File sourceFile, File destinationFile) throws IOException{
		if(copyFile(sourceFile, destinationFile)){
			return sourceFile.delete();
		}
		else return false;
	}
}
