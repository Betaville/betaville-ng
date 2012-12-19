package net.betaville.ng.net.codes;

/**
 * Command codes that remote controls can send to the client
 * @author Skye Book
 *
 */
public class RemoteCodes {
	
	public static final byte FORWARD = 0x00;
	public static final byte BACKWARD = 0x01;
	public static final byte STRAFE_LEFT = 0x02;
	public static final byte STRAFE_RIGHT = 0x03;
	public static final byte ROTATE_UP = 0x04;
	public static final byte ROTATE_DOWN = 0x05;
	public static final byte ROTATE_LEFT = 0x06;
	public static final byte ROTATE_RIGHT = 0x07;
	public static final byte ALTITUDE_UP = 0x08;
	public static final byte ALTITUDE_DOWN = 0x09;
	public static final byte GO_TO = 0x0A;
	public static final byte ROTATE_TO = 0x0B;
	
	public static final byte VOLUME_MUTE = 0x10;
	public static final byte VOLUME_UP = 0x11;
	public static final byte VOLUME_DOWN = 0x12;
	
	public static final byte REQUEST_CONTROL = 0x20;
	public static final byte ABDICATE_CONTROL = 0x21;
	
}
