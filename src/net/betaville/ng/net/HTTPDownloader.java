package net.betaville.ng.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Downloads a file from an HTTP server
 * @author Skye Book
 *
 */
public class HTTPDownloader {
	
	// The size of the buffer to read data into before being saved to disk
	private static final int BUFFER_SIZE = 4096;
	
	// How many bytes to read before an update
	private static int updateInterval = 4096;

	
	public static void download(URL url, File fileSaveLocation, ProgressCallback progressCallback, DownloadCompleteCallback downloadCompleteCallback) throws IOException{
		
		long startTime = System.currentTimeMillis();
		
		URLConnection connection = url.openConnection();
		
		String length = connection.getHeaderField("content-length");
		int size = -1;
		try{
			size = Integer.parseInt(length);
		}catch(NumberFormatException e){
			// content-length header invalid or not supplied
			size = -1;
		}
		
		
		FileOutputStream outputStream = new FileOutputStream(fileSaveLocation);
		InputStream is = connection.getInputStream();
		
		int bufferSizeForThisUse = BUFFER_SIZE;
		
		byte[] readBuffer = new byte[bufferSizeForThisUse];
		int lastUpdate = 0;
		int totalRead = 0;
		int n=-1;
		while ((n = is.read(readBuffer, 0, bufferSizeForThisUse)) != -1){
			totalRead+=n;
			if(totalRead-lastUpdate>updateInterval){
				// update the progress callback
				if(progressCallback!=null) progressCallback.update(totalRead, size);
				lastUpdate=totalRead;
			}
			outputStream.write(readBuffer, 0, n);
		}
		is.close();
		outputStream.close();
		
		if(downloadCompleteCallback!=null) downloadCompleteCallback.downloadComplete(url, fileSaveLocation, (System.currentTimeMillis()-startTime));
	}
	
	
	
	public interface ProgressCallback{
		/**
		 * Called when the download reaches the specified interval
		 * @param bytesRead The number of bytes read so far.
		 * @param totalBytes The total size of the file in bytes. -1 if the content-length
		 * was not reported
		 */
		public void update(int bytesRead, int totalBytes);
	}
	
	public interface DownloadCompleteCallback{
		/**
		 * Called when a download is completed
		 * @param originalURL The original URL of the file
		 * @param fileLocation The location of the saved file
		 * @param timeToDownload The time it took to download the file, in milliseconds
		 */
		public void downloadComplete(URL originalURL, File fileLocation, long timeToDownload);
	}
}