package program;

/**
 * job for loading configuration from a file
 */
public class ConfigLoad implements IApplicationJob {
	private String path;

	/**
	 * standard constructor
	 * @param path path to the configuration file
	 */
	public ConfigLoad(String path) {
		this.path = path;
	}

	/**
	 * loads the configuration
	 */
	@Override
	public void run() {
		Configuration.loadFromFile(this.path);
	}

	@Override
	public boolean needsDB() {
		return false;
	}
	
	@Override
	public String toString() {
		return "ConfigLoad";
	}

}
