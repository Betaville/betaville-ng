package net.betaville.ng.net;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Skye Book
 *
 */
public class ProgressInputStream extends FilterInputStream {
	
	// how often to dispatch an update
	private int granularity = 4096;
	private int granularityCount = 0;

	private int bytesRead = 0;

	private ProgressInputListener listener = null;

	/**
	 * @param inputStream
	 */
	public ProgressInputStream(InputStream inputStream) {
		super(inputStream);
	}

	public void resetCounter(){
		bytesRead=0;
		listener = null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.FilterInputStream#read()
	 */
	@Override
	public int read() throws IOException{
		int bytesRead = in.read();
		updateBytesRead(bytesRead);
		return bytesRead;
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.FilterInputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException{
		int bytesRead = in.read(b);
		updateBytesRead(bytesRead);
		return bytesRead;
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException{
		int bytesRead = in.read(b, off, len);
		updateBytesRead(bytesRead);
		return bytesRead;
	}

	public int getBytesRead(){
		return bytesRead;
	}

	private void updateBytesRead(int incrementValue){
		bytesRead+=incrementValue;
		granularityCount+=incrementValue;
		if(granularityCount>granularity){
			if(listener!=null) listener.readProgressUpdate(bytesRead);
			while(granularityCount>granularity){
				granularityCount = granularityCount-granularity;
			}
		}
	}
	
	public void setListener(ProgressInputListener listener){
		this.listener = listener;
	}

	public interface ProgressInputListener{
		public void readProgressUpdate(int bytesRead);
	}

}
