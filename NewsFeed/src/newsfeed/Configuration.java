/**
 * 
 */
package newsfeed;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

/**
 * @author Florian
 *
 */
public class Configuration {

	private static String dbServerHostName;
	private static String dbServerPort;
	private static String dbServerSchemaName;
	private static String dbServerUserName;
	private static String dbServerPassword;
	private static String dbSqlDateFormat;
	
	private static int networkConnectionTimeout;
	private static int networkReadTimeout;
	
	private static boolean serviceInfiniteLoop;
	private static int serviceCyclicDelay;
	
	private static boolean generalBeVerbose;
	private static String generalOutputDateFormat;
	
	
	public static void loadFromFile(String configFile) {
		Properties prop = new Properties();
		try (InputStream in = new FileInputStream(configFile)) {
			prop.load(new FileInputStream(configFile));
		} catch (Exception e) {
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			System.err.print(sdf.format(cal.getTime()) + " : ");
			System.err.println("Failed to read config File at " + configFile);
			System.err.println("Exception message: " + e.getMessage());
			System.err.println("Using default configuration instead. ");
			System.err.println();
		}
		
		dbServerHostName = prop.getProperty("dbServerHostName", "127.0.0.1");
		dbServerPort = prop.getProperty("dbServerPort", "3306");
		dbServerSchemaName = prop.getProperty("dbServerSchemaName", "Newsfeed");
		dbServerUserName = prop.getProperty("dbServerUserName", "root");
		dbServerPassword = prop.getProperty("dbServerPassword", "");
		dbSqlDateFormat = prop.getProperty("dbSqlDateFormat", "yyyy-MM-dd HH:mm:ss");
		
		networkConnectionTimeout = Integer.parseUnsignedInt(prop.getProperty("networkConnectionTimeout", "10000"));
		networkReadTimeout = Integer.parseUnsignedInt(prop.getProperty("networkReadTimeout", "10000"));
		
		serviceInfiniteLoop = Boolean.parseBoolean(prop.getProperty("serviceInfiniteLoop", "False"));
		serviceCyclicDelay = Integer.parseUnsignedInt(prop.getProperty("serviceCyclicDelay", "1000"));
		
		generalBeVerbose = Boolean.parseBoolean(prop.getProperty("generalBeVerbose", "False"));		
		generalOutputDateFormat = prop.getProperty("generalOutputDateFormat", "dd.MM.yyyy HH:mm:ss");
	}


	public static String getDbServerHostName() {
		return dbServerHostName;
	}


	public static String getDbServerPort() {
		return dbServerPort;
	}


	public static String getDbServerSchemaName() {
		return dbServerSchemaName;
	}


	public static String getDbServerUserName() {
		return dbServerUserName;
	}


	public static String getDbServerPassword() {
		return dbServerPassword;
	}

	
	public static String getDbSqlDateFormat() {
		return dbSqlDateFormat;
	}
	

	public static int getNetworkConnectionTimeout() {
		return networkConnectionTimeout;
	}


	public static int getNetworkReadTimeout() {
		return networkReadTimeout;
	}


	public static boolean getServiceInfiniteLoop() {
		return serviceInfiniteLoop;
	}


	public static int getServiceCyclicDelay() {
		return serviceCyclicDelay;
	}


	public static boolean getGeneralBeVerbose() {
		return generalBeVerbose;
	}
	
	
	public static String getGeneralOutputDateFormat() {
		return generalOutputDateFormat;
	}
	
}
