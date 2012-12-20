package net.betaville.ng.net;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Skye Book
 *
 */
public class ProgressOutputStream extends FilterOutputStream {

	// how often to dispatch an update
	private int granularity = 4096;
	private int granularityCount = 0;

	private int bytesWritten = 0;
	
	private ProgressOutputListener listener = null;

	/**
	 * @param outputStream
	 */
	public ProgressOutputStream(OutputStream outputStream) {
		super(outputStream);
	}

	public void resetCounter(){
		bytesWritten=0;
		listener = null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException{
		out.write(b);
		updateBytesWritten(b.length);
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException{
		out.write(b, off, len);
		updateBytesWritten(len);
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException{
		out.write(b);
		updateBytesWritten(1);
	}
	
	public int getBytesWritten(){
		return bytesWritten;
	}

	private void updateBytesWritten(int incrementValue){
		bytesWritten+=incrementValue;
		granularityCount+=incrementValue;
		if(granularityCount>granularity){
			if(listener!=null) listener.writeProgressUpdate(bytesWritten);
			while(granularityCount>granularity){
				granularityCount = granularityCount-granularity;
			}
		}
	}
	
	public void setListener(ProgressOutputListener listener){
		this.listener = listener;
	}
	
	public interface ProgressOutputListener{
		public void writeProgressUpdate(int bytesWritten);
	}

}
