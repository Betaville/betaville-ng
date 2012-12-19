package net.betaville.ng;

/**
 * Kiosk configuration options
 * @author Skye Book
 *
 */
public class KioskMode {
	
	public static final String KIOSK_PASSWORD_HASH = "betaville.kiosk.password";
	public static final String KIOSK_PASSWORD_REQUIRED = "betaville.kiosk.requirepass";
	public static final String KIOSK_REFRESH_RATE = "betaville.kiosk.refresh";
	public static final String KIOSK_MODE_ENABLED = "betaville.kiosk.enabled";
	
	public static boolean kioskPasswordIsSetAndEnabled(){
		return KioskMode.isInKioskMode() && KioskMode.isExitPasswordRequired() &&
				KioskMode.getKioskPasswordHash()!=null && KioskMode.getKioskPasswordHash().length()==40;
	}

	public static String getKioskPasswordHash(){
		return System.getProperty(KIOSK_PASSWORD_HASH);
	}

	public static boolean isExitPasswordRequired(){
		return SettingsPreferences.readTrueFalseValue(KIOSK_PASSWORD_REQUIRED);
	}

	public static boolean isInKioskMode(){
		return SettingsPreferences.readTrueFalseValue(KIOSK_MODE_ENABLED);
	}
	
	public static int getRefreshRate(){
		Integer rate = Integer.parseInt(System.getProperty(KIOSK_REFRESH_RATE));
		if(rate==null){
			return 0;
		}
		else return rate;
	}

}
