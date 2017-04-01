package program;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

/**
 * static configuration class
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
	
	/**
	 * loads configuration from a configuration file
	 * @param configFile specifies the filepath for the configuration file
	 */
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

	/**
	 * returns the DB server's hostname specified by the configuration file 
	 * @return the DB server's hostname specified by the configuration file 
	 */
	public static String getDbServerHostName() {
		return dbServerHostName;
	}

	/**
	 * returns the DB server's port specified by the configuration file 
	 * @return the DB server's port specified by the configuration file 
	 */
	public static String getDbServerPort() {
		return dbServerPort;
	}

	/**
	 * returns the DB schema specified by the configuration file 
	 * @return the DB schema specified by the configuration file 
	 */
	public static String getDbServerSchemaName() {
		return dbServerSchemaName;
	}

	/**
	 * returns the DB server's username specified by the configuration file 
	 * @return the DB server's username specified by the configuration file 
	 */
	public static String getDbServerUserName() {
		return dbServerUserName;
	}

	/**
	 * returns the DB server's password specified by the configuration file 
	 * @return the DB server's password specified by the configuration file 
	 */
	public static String getDbServerPassword() {
		return dbServerPassword;
	}

	/**
	 * returns the DB server's date format specified by the configuration file 
	 * @return the DB server's date format specified by the configuration file 
	 */
	public static String getDbSqlDateFormat() {
		return dbSqlDateFormat;
	}
	
	/**
	 * returns the application's network connection timeout specified by the configuration file 
	 * @return the application's network connection timeout specified by the configuration file 
	 */
	public static int getNetworkConnectionTimeout() {
		return networkConnectionTimeout;
	}

	/**
	 * returns the application's network read timeout specified by the configuration file 
	 * @return the application's network read timeout specified by the configuration file 
	 */
	public static int getNetworkReadTimeout() {
		return networkReadTimeout;
	}

	/**
	 * indicates if the crawler update is runned once or in an infinite loop; specified by the configuration file 
	 * @return true, if the crawler update is runned in an infinite loop; specified by the configuration file 
	 */
	public static boolean getServiceInfiniteLoop() {
		return serviceInfiniteLoop;
	}

	/**
	 * returns the application's cyclic delay between reading out two feeds; specified by the configuration file 
	 * @return the application's cyclic delay between reading out two feeds; specified by the configuration file 
	 */
	public static int getServiceCyclicDelay() {
		return serviceCyclicDelay;
	}

	/**
	 * indicates if the application generates additional outputs; specified by the configuration file 
	 * @return true, if the application generates additional outputs; specified by the configuration file 
	 */
	public static boolean getGeneralBeVerbose() {
		return generalBeVerbose;
	}
	
	/**
	 * returns the application's output date format specified by the configuration file 
	 * @return the application's output date format specified by the configuration file 
	 */
	public static String getGeneralOutputDateFormat() {
		return generalOutputDateFormat;
	}
	
}
