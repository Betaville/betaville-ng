package net.betaville.ng.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author Skye Book
 *
 */
public class StringZipper {

	public static byte[] compress(String string) throws UnsupportedEncodingException, IOException{
		byte[] input = string.getBytes("UTF-8");
		Deflater deflator = new Deflater();
		deflator.setInput(input);

		ByteArrayOutputStream bo = new ByteArrayOutputStream(input.length);
		deflator.finish();
		byte[] buffer = new byte[1024];
		while(!deflator.finished())
		{
			int count = deflator.deflate(buffer);
			bo.write(buffer, 0, count);
		}
		bo.close();
		byte[] output = bo.toByteArray();
		return output;
	}

	public static String uncompress(byte[] bytes) throws UnsupportedEncodingException, IOException, DataFormatException{
		Inflater inflator = new Inflater();
		inflator.setInput(bytes);

		ByteArrayOutputStream bo = new ByteArrayOutputStream(bytes.length);
		byte[] buffer = new byte[1024];
		while(!inflator.finished())
		{
			int count = inflator.inflate(buffer);
			bo.write(buffer, 0, count);
		}
		bo.close();
		byte[] output = bo.toByteArray();
		return new String(output, "UTF-8");
	}
}
