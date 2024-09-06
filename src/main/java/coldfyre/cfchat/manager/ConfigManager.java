package coldfyre.cfchat.manager;

import java.util.HashMap;
import java.util.Map;

import com.coldfyre.api.AbstractConfig;
import com.coldfyre.api.manager.FilesManager;

import coldfyre.cfchat.configuration.ConfigAlternateFilter;
import coldfyre.cfchat.configuration.ConfigAnnouncement;
import coldfyre.cfchat.configuration.ConfigData;
import coldfyre.cfchat.configuration.ConfigFilter;
import coldfyre.cfchat.configuration.ConfigMain;

/**
 * Class for storing all the Configurations of the plugin. Note that this does NOT contain
 * the Configurations of Players or Groups. To edit Player/Group configs, use their
 * respective Config Manager objects.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class ConfigManager {
	
	public static final Class<? extends ConfigMain> CONFIG_MAIN = ConfigMain.class;
	public static final Class<? extends ConfigAnnouncement> CONFIG_ANNOUNCEMENT = ConfigAnnouncement.class;
	public static final Class<? extends ConfigFilter> CONFIG_FILTER = ConfigFilter.class;
	public static final Class<? extends ConfigAlternateFilter> CONFIG_ALTERNATE = ConfigAlternateFilter.class;
	public static final Class<? extends ConfigData> CONFIG_DATA = ConfigData.class;
	
	private Map<Class<? extends AbstractConfig>, AbstractConfig> confs;
	
	/**
	 * Creates a new object that collects and gets all the Configs of the plugin.
	 * @param manager - Main manager of plugin
	 */
	public ConfigManager(CFManager manager) {
		reload(manager.getFilesManager());
	}
	
	/**
	 * Removes the plugin configs, getting the new data FROM the file. No data changed within this, or their respective
	 * Configs will be saved to the files. These only act as getters for the data rather than using I/O repetitively.
	 * 
	 * @param fManager - FilesManager of plugin
	 */
	public void reload(FilesManager fManager) {
		confs = new HashMap<Class<? extends AbstractConfig>, AbstractConfig>();
		
		confs.put(CONFIG_MAIN, new ConfigMain(fManager.getFile("config_file")));
		confs.put(CONFIG_ANNOUNCEMENT, new ConfigAnnouncement(fManager.getFile("announcement_file")));
		confs.put(CONFIG_FILTER, new ConfigFilter(fManager.getFile("blacklist_file"), fManager.getFile("whitelist_file")));
		confs.put(CONFIG_ALTERNATE, new ConfigAlternateFilter(fManager.getFile("alternate_file")));
		confs.put(CONFIG_DATA, new ConfigData(fManager.getFile("data_file")));
	}
	
	/**
	 * Gets a map of all the Configurations for this plugin.
	 * 
	 * @return Map of Configs
	 */
	public Map<Class<? extends AbstractConfig>, AbstractConfig> getRawData() { return confs; }
	
	/**
	 * Gets the Config based on the config type. Each config is represented by their class object. A set of Enum-Type
	 * values are within this class for use of getting the correct Config.
	 * 
	 * @param <T> - Class of Super-level config
	 * @param clazz - Class of config to obtain
	 * @return Config of given class
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractConfig> T getConfig(Class<T> clazz) {
		return (T) confs.get(clazz);
	}
}
