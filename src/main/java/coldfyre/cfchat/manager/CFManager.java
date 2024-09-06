package coldfyre.cfchat.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.coldfyre.api.manager.FilesManager;
import com.coldfyre.api.manager.PluginManager;

import coldfyre.cfchat.CFChat;

/**
 * Main manager class for the plugin. All <i>(most)</i> files, or rather manager-type files are
 * initialized here. Any core, or base-type data files will be created/loaded when this constructor
 * is called. From this class, all other classes can be reached; as such, it'll be noticeable that
 * the other classes will have this object passed as a parameter to gain access to the rest of the
 * plugin. This object is not to be stored within the other classes, alone from Main Plugin Java class.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class CFManager extends PluginManager<CFChat> {
	
	private FilesManager filesManager;
	private ConfigManager configManager;
	private PlayerManager playerManager;

	/**
	 * Creates a new Manager-type object that can be used to store, create and otherwise connect the rest
	 * of the plugin. When using this object, the super class of this object attaches to a JavaPlugin class,
	 * ensuring that any data related to the plugin remains within their respective collection as well as their
	 * plugin Data folder where plugin data is stored.
	 * 
	 * @param plugin - JavaPlugin to attach to.
	 */
	public CFManager(CFChat plugin) {
		super(plugin);
		reload();
	}
	
	/**
	 * Reloads the plugin and all the data within.
	 */
	public void reload() {
		if(filesManager == null)
			filesManager = new FilesManager(this);
		
		filesManager.clear();
		filesManager.addFolder("config_data", "Config Data");
		filesManager.addFolder("player_data", "Player Data");
		filesManager.addFolder("group_data", "Group Data");
		
		loadDataFiles(filesManager.addFile("config_file", "Config.yml"), filesManager.getFile("config_file"), getPlugin().getClass().getResourceAsStream("default_config.yml"));
		loadDataFiles(filesManager.addFile("announcement_file", "Config Data/Announcement.yml"), filesManager.getFile("announcement_file"), getPlugin().getClass().getResourceAsStream("default_announcement.yml"));
		loadDataFiles(filesManager.addFile("blacklist_file", "Config Data/Blacklist.yml"), filesManager.getFile("blacklist_file"), getPlugin().getClass().getResourceAsStream("default_blacklist.yml"));
		loadDataFiles(filesManager.addFile("whitelist_file", "Config Data/Whitelist.yml"), filesManager.getFile("whitelist_file"), getPlugin().getClass().getResourceAsStream("default_whitelist.yml"));
		loadDataFiles(filesManager.addFile("data_file", "Config Data/Data.yml"), filesManager.getFile("data_file"), getPlugin().getClass().getResourceAsStream("default_data.yml"));
		loadDataFiles(filesManager.addFile("alternate_file", "Config Data/Alternate Filter Words.yml"), filesManager.getFile("alternate_file"), getPlugin().getClass().getResourceAsStream("default_words.yml"));
		loadDataFiles(filesManager.addFile("help_file", "Help.txt"), filesManager.getFile("help_file"), getPlugin().getClass().getResourceAsStream("default_help.txt"));
		
		if(configManager == null)
			configManager = new ConfigManager(this);
		else
			configManager.reload(filesManager);
		
		if(playerManager == null)
			playerManager = new PlayerManager(this);
		else
			playerManager.reload(this);
		
		//TODO: Load Group
	}
	
	// Loads the data file with the correct corresponding information from the InputStream
	private void loadDataFiles(boolean needsLoaded, File file, InputStream is) {
		if(!needsLoaded)
			return;
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(is)); BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			String line;
			
			while((line = reader.readLine()) != null)
				writer.write(line);
		} catch (IOException e) {
			FilesManager.LogException(getPlugin(), e);
		}
	}
	
	/**
	 * Gets this Plugin's Filing Manager.
	 * @return {@link FilesManager}
	 */
	public FilesManager getFilesManager() { return filesManager; }
	
	/**
	 * Gets the Manager of this plugin's Configuration Manager. Note that this
	 * does not include Group or Player Configs, each of those contain their own
	 * Manager class.
	 * 
	 * @return {@link ConfigManager}
	 */
	public ConfigManager getConfigManager() { return configManager;}

	/**
	 * Disable method for shutting down the plugin normally. This is so that
	 * any data that needs to be saved will be, no loss of data.
	 */
	public void disable() {
	}

}
