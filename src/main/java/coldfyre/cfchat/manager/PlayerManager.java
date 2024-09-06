package coldfyre.cfchat.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import com.coldfyre.api.manager.FilesManager;

import coldfyre.cfchat.players.PlayerConfig;

/**
 * Manager class that contains all the player data as well as containing the methods that
 * both save and reload player data. This object doesn't change any player data, such
 * actions are left to the individual player data objects.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class PlayerManager {
	
	private Map<UUID, PlayerConfig> playerData;
	
	/**
	 * Creates a new manager for the Player Data.
	 * 
	 * @param manager - Main Plugin Manager
	 */
	public PlayerManager(CFManager manager) {
		reload(manager);
	}
	
	// Used to load the data from the JAR default files into the
	// systems file.
	private void loadFirstTimeData(File file, InputStream is) {
		String line;
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(is)); BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			while((line = reader.readLine()) != null)
				writer.write(line);
		} catch (IOException e) {
			FilesManager.LogException(e);
		}
	}
	
	/**
	 * Gets the Players Data Configuration file. Note that any changes done will see an effect in-game, but
	 * are not necessarily saved the moment of any change. To reduce the number of I/O operations, the saving
	 * of player data is reversed on a timer.
	 * 
	 * @param player - Player to get Data from
	 * @return player's Config Data
	 */
	public PlayerConfig getPlayerConfig(OfflinePlayer player) { return getPlayerConfig(player.getUniqueId()); }
	
	/**
	 * Gets the Players Data Configuration file. Note that any changes done will see an effect in-game, but
	 * are not necessarily saved the moment of any change. To reduce the number of I/O operations, the saving
	 * of player data is reversed on a timer.
	 * 
	 * @param id - UUID of player
	 * @return player's Config data
	 */
	public PlayerConfig getPlayerConfig(UUID id) { return playerData.get(id); }
	
	/**
	 * Saves ALL player data from within this plugin into their respective files
	 * within the system.
	 * 
	 * @param manager - Main Plugin Manager
	 */
	public void save(CFManager manager) {
		for(UUID id : playerData.keySet())
			save(manager, id);
	}
	
	/**
	 * Saves the given player's (based on their UUID) data into their config file. This can be used
	 * if special or manual saving is recommended to ensure no lose of data.
	 * 
	 * @param manager - Main Plugin Manager
	 * @param id - UUID of player
	 */
	public void save(CFManager manager, UUID id) {
		playerData.get(id).save(manager.getFilesManager().getFile("player_" + id.toString()));
	}
	
	/**
	 * Reloads this object. Note that this will NOT save any changed data prior to reloading. As such, if any
	 * data is to be saved before reloading, then calling the {@link #save(CFManager)} method is recommended
	 * prior to calling this method.
	 * 
	 * @param manager - Main Plugin Manager
	 */
	public void reload(CFManager manager) {
		if(playerData == null)
			playerData = new HashMap<UUID, PlayerConfig>();
		else
			playerData.clear();
		
		for(OfflinePlayer allPlayers : manager.getPlugin().getServer().getOfflinePlayers()) {
			String fileName = allPlayers.getUniqueId().toString() + ".yml";
			String filesManagerName = "player_" + allPlayers.getUniqueId().toString();
			YamlConfiguration pConfig;
			
			if(manager.getFilesManager().addFile(filesManagerName, "Player Data/" + fileName)) {
				loadFirstTimeData(manager.getFilesManager().getFile(filesManagerName), manager.getPlugin().getClass().getResourceAsStream("default_player.yml"));
				
				pConfig = YamlConfiguration.loadConfiguration(manager.getFilesManager().getFile(filesManagerName)); 
				pConfig.set("name", allPlayers.getName());
				
				try {
					pConfig.save(manager.getFilesManager().getFile(filesManagerName));
				} catch (IOException e) {
					FilesManager.LogException(manager, e);
				}
			} else
				pConfig = YamlConfiguration.loadConfiguration(manager.getFilesManager().getFile(filesManagerName));
			
			playerData.put(allPlayers.getUniqueId(), new PlayerConfig(allPlayers, pConfig));
		}
	}
	
	/**
	 * Reloads a given players Data file. Note that this will not save their data (if any changes have occurred) to their file
	 * prior to reloading. Any changes will be lost unless the method {@link #save(CFManager, UUID)} is called, ensuring that
	 * any changes to their config is saved.
	 * 
	 * @param player - Player to reload
	 * @param manager - Main Plugin Manager
	 */
	public void reloadPlayer(OfflinePlayer player, CFManager manager) { reloadPlayer(player.getUniqueId(), manager); }
	
	/**
	 * Reloads a given players Data file. Note that this will not save their data (if any changes have occurred) to their file
	 * prior to reloading. Any changes will be lost unless the method {@link #save(CFManager, UUID)} is called, ensuring that
	 * any changes to their config is saved.
	 * 
	 * @param playerID - UUID of Player
	 * @param manager - Main Pluing Manager
	 */
	public void reloadPlayer(UUID playerID, CFManager manager) {
		YamlConfiguration pConfig;
		
		if(manager.getFilesManager().addFile("player_" + playerID.toString(), "Player Data/" + playerID.toString() + ".yml")) {
			loadFirstTimeData(manager.getFilesManager().getFile("player_" + playerID.toString()), manager.getPlugin().getClass().getResourceAsStream("default_player.yml"));
			
			pConfig = YamlConfiguration.loadConfiguration(manager.getFilesManager().getFile("player_" + playerID.toString())); 
			pConfig.set("name", manager.getPlugin().getServer().getOfflinePlayer(playerID).getName());
			
			try {
				pConfig.save(manager.getFilesManager().getFile("player_" + playerID.toString()));
			} catch (IOException e) {
				FilesManager.LogException(manager, e);
			}
		} else
			pConfig = YamlConfiguration.loadConfiguration(manager.getFilesManager().getFile("player_" + playerID.toString()));
		
		playerData.put(playerID, new PlayerConfig(manager.getPlugin().getServer().getOfflinePlayer(playerID), pConfig));
	}
}
