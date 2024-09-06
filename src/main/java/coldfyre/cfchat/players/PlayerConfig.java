package coldfyre.cfchat.players;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import coldfyre.cfchat.players.PlayerLog.LogType;
import coldfyre.cfchat.warnings.Warning;

/**
 * This config contains all the data of the player. Most details about a player can be
 * found here within this object. Note that some times have their own class that handle
 * their specific data while others are handled within this class.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class PlayerConfig {
	
	private OfflinePlayer player;
	private UUID pid;
	private Map<Integer, Mail> mail;
	private PlayerLog playerLog;
	private List<OfflinePlayer> ignoreList;
	private List<UUID> groups;
	private MuteData muteData;
	private List<Warning> warnings;
	
	/**
	 * Creates a new PLayerConfig file for the player with their file. The file is
	 * then skimmed through, collecting and converting the information into memory
	 * for faster and easier access.
	 * 
	 * @param player - Player to get config of
	 * @param config - Their Config file
	 */
	public PlayerConfig(OfflinePlayer player, YamlConfiguration config) {
		this.player = player;
		pid = player.getUniqueId();
		ignoreList = loadIgnoreList(config.getStringList("chat.ignore"));
		warnings = loadWarningList(config.getStringList("warnings"));
		groups = loadGroups(config.getStringList("groups"));
		muteData = MuteData.loadMuteData(config);
		playerLog = PlayerLog.loadFromConfig(config);
		mail = loadMail(config);
	}
	
	/**
	 * Loads the Mail Data into the Map object of Mail. The actual creation
	 * and collecting of the mail from the config is done within the Mail object,
	 * this only creates and stores said collected data.
	 * 
	 * @param config - Player Config
	 * @return Map of Mail Data
	 */
	private Map<Integer, Mail> loadMail(YamlConfiguration config) {
		Map<Integer, Mail> ret = new HashMap<Integer, Mail>();
		
		for(String mid : config.getConfigurationSection("mail").getKeys(false))
			ret.put(Integer.parseInt(mid), Mail.loadFromConfig(Integer.parseInt(mid), config, "mail"));
		
		return ret;
	}
	
	/**
	 * Loads the list of group UUID's into a list.
	 * 
	 * @param ids - String values of UUID's
	 * @return List of UUID's
	 */
	private List<UUID> loadGroups(List<String> ids) {
		List<UUID> ret = new ArrayList<UUID>();
		
		for(String s : ids)
			ret.add(UUID.fromString(s.split(":")[0]));
		
		return ret;
	}
	
	/**
	 * Loads the ignore list of player UUID's into the ignore list
	 * 
	 * @param ids - String values of UUID's
	 * @return List of players to ignore
	 */
	private List<OfflinePlayer> loadIgnoreList(List<String> ids) {
		List<OfflinePlayer> ret = new ArrayList<OfflinePlayer>();
		
		for(String s : ids)
			ret.add(Bukkit.getServer().getOfflinePlayer(UUID.fromString(s.split(":")[0])));
		
		return ret;
	}
	
	/**
	 * Loads the Warnings the player have received.
	 * 
	 * @param warnings - List of Warning String Data
	 * @return List of Warnings
	 */
	private List<Warning> loadWarningList(List<String> warnings) {
		List<Warning> ret = new ArrayList<Warning>();
		
		for(String s : warnings)
			ret.add(Warning.loadFromConfig(s));
		
		return ret;
	}
	
	/**
	 * Gets the Player Object that is attached to this PlayerConfig file.
	 * 
	 * @return OfflinePlayer
	 */
	public OfflinePlayer getPlayer() { return player; }
	
	/**
	 * Gets the UUID of the player.
	 * 
	 * @return UUID of Player
	 */
	public UUID getPlayerID() { return pid; }
	
	/**
	 * Gets All the mail of the player.
	 * 
	 * @return Map of player mail
	 */
	public Map<Integer, Mail> getMail() { return mail; }
	
	/**
	 * Gets the specific Mail object based on it's ID.
	 * 
	 * @param id - ID of mail object
	 * @return Mail
	 */
	public Mail getMail(int id) { return mail.get(id); }
	
	/**
	 * Gets a Map of all mail that is unread by the player. If the player
	 * has read all mail, then this will return an empty Map object.
	 * 
	 * @return Map of Unread mail
	 */
	public Map<Integer, Mail> getUnreadMail() {
		Map<Integer, Mail> ret = new HashMap<Integer, Mail>();
		
		for(Map.Entry<Integer, Mail> entry : mail.entrySet()) {
			if(entry.getValue().isUnread())
				ret.put(entry.getKey(), entry.getValue());
		}
		
		return ret;
	}
	
	/**
	 * Gets the Logger of the player.
	 * 
	 * @return PlayerLog
	 */
	public PlayerLog getPlayerLog() { return playerLog; }
	
	/**
	 * Gets the Mute Data of the player.
	 * 
	 * @return MuteData
	 */
	public MuteData getMuteData() { return muteData; }
	
	/**
	 * Gets the list of players this player is ignoring.
	 * 
	 * @return List of ignored players
	 */
	public List<OfflinePlayer> getIgnoreList() { return ignoreList; }
	
	/**
	 * Gets the group ID's the player is apart of.
	 * 
	 * @return List of Group ID's
	 */
	public List<UUID> getGroupIds() { return groups; }
	
	/**
	 * Gets the list of Warnings this player has.
	 * 
	 * @return List of Warnings
	 */
	public List<Warning> getWarnings() { return warnings; }
	
	/**
	 * Gets a list of Warnings between the specified dates.
	 * 
	 * @param first - Lower bound of Warnings
	 * @param end - Upper bound of Warnings
	 * @return List of Warnings in the specified time range
	 */
	public List<Warning> getWarnings(int first, int end) {
		List<Warning> ret = new ArrayList<Warning>();
		
		if(first < end) {
			first = first < 0 ? 0 : first;
			end = end >= warnings.size() ? warnings.size() - 1 : end;
		} else {
			int temp = first;
			first = end;
			end = temp;
			
			first = first < 0 ? 0 : first;
			end = end == -1 ? warnings.size() - 1 : end >= warnings.size() ? warnings.size() - 1 : end;
		}
		
		for(; first <= end; first++)
			ret.add(warnings.get(first));
		
		return ret;
	}
	
	/**
	 * Adds a Warning to the player. If the entity that warned the player is the console, then
	 * set the OfflinePlayer object to NULL.
	 * 
	 * @param warner - Player who performed the warning
	 * @param reason - Reason for Warning
	 */
	public void addWarning(OfflinePlayer warner, String reason) { addWarning(new Warning(warner, reason)); }
	
	/**
	 * Adds the Warning object to this players config.
	 * 
	 * @param warning - Warning to add
	 */
	public void addWarning(Warning warning) { warnings.add(warning); }
	
	/**
	 * Removes the Warning from this players config. Each warning has their own ID, but that does NOT
	 * specify which order they were added in. After a warning has been removed, their ID can be used
	 * again for later warnings. As such, using the ID's as to keep an order of first-last is not advised.
	 * 
	 * @param id - ID of warning
	 */
	public void removeWarning(int id) { warnings.remove(id); }
	
	/**
	 * Removes the Warning from this players config. Each warning has their own ID, but that does NOT
	 * specify which order they were added in. After a warning has been removed, their ID can be used
	 * again for later warnings. As such, using the ID's as to keep an order of first-last is not advised.
	 * 
	 * @param warning - Warning to remove
	 */
	public void removeWarning(Warning warning) { warnings.remove(warning); }
	
	/**
	 * Adds a player to the list of Ignores.
	 * 
	 * @param player - Player to ignore
	 */
	public void addIgnore(OfflinePlayer player) {
		if(!ignoreList.contains(player))
			ignoreList.add(player);
	}
	
	/**
	 * Removes a player from the ignore list.
	 * 
	 * @param player - Player to remove
	 */
	public void removeIgnore(OfflinePlayer player) { ignoreList.remove(player); }
	
	/**
	 * Adds a group to the player's list of groups.
	 * 
	 * @param id - ID of group
	 */
	public void addGroup(UUID id) {
		if(!groups.contains(id))
			groups.add(id);
	}
	
//	/**
//	 * This adds the group to the this players list of groups.
//	 *
//	 * @param group - Group to add
//	 */
//	public void addGroup(Group group) {
//		if(!groups.contains(group.getID()))
//			groups.add(group.getID());
//	}
	
	/**
	 * Removes the Group from this players config.
	 * 
	 * @param id - ID of group
	 */
	public void removeGroup(UUID id) { groups.remove(id); }
//	/**
//	 * Removes the group from the player config.
//	 * 
//	 * @param group - Group to remove
//	 */
//	public void removeGroup(Group group) { groups.remove(group.getID()); }
	
	/**
	 * Checks if this player is apart of the group based on the ID of the group.
	 * 
	 * @param id - ID of group
	 * @return True - if player is apart of the group
	 */
	public boolean isInGroup(UUID id) { return groups.contains(id); }
	
//	/**
//	 * Checks if this player is apart of the group.
//	 * 
//	 * @param group - Group to check
//	 * @return True - If player is apart of the group
//	 */
//	public boolean isInGroup(Group group) { return groups.contains(group.getID()); }
	
	/**
	 * Saves this data into the given file. Note that this data does not pertain to
	 * any player. As such, it's the responsibility of the programmer to ensure this
	 * data is attached to an entity.
	 * 
	 * @param file - File to save data to
	 */
	public void save(File file) {
		YamlConfiguration pConfig = YamlConfiguration.loadConfiguration(file);
		
		pConfig.set("mail", null);
		pConfig.set("warnings", null);
		
		pConfig.createSection("mail");
		pConfig.createSection("warnings");
		
		List<String> groupList = new ArrayList<String>();
		List<String> ignoreList = new ArrayList<String>();
		List<String> warnings = new ArrayList<String>();
		
		for(UUID id : groups)
			groupList.add(id.toString());
		
		for(OfflinePlayer player : this.ignoreList)
			ignoreList.add(player.getUniqueId().toString());

		for(Warning w : this.warnings)
			warnings.add(w.getTimeStamp() + "§"
					+ w.getTimeStampString() + "§"
					+ (w.getWarner() == null ? "Console" : w.getWarner().getUniqueId().toString()) + "§"
					+ (w.getWarner() == null ? "Console" : w.getWarner().getName()) + "§"
					+ w.getReason().replaceAll("§", "_CFUNIQUE_"));
		
		pConfig.set("warnings", warnings);
		pConfig.set("groups", groupList);
		pConfig.set("chat.ignore", ignoreList);
		pConfig.set("chat.logger.messages", playerLog.getRawList(LogType.MESSAGES));
		pConfig.set("chat.logger.commands", playerLog.getRawList(LogType.COMMANDS));
		pConfig.set("chat.logger.mail", playerLog.getRawList(LogType.MAIL));
		
		pConfig.set("chat.mute.on", muteData.isMuted());
		pConfig.set("chat.mute.time", Arrays.asList("" + (muteData.getMuteTime() > 0 ? muteData.getMuteTime() : ""), muteData.getMuteTime() > 0 ? muteData.getMuteTimeString() : ""));
		pConfig.set("chat.mute.by", Arrays.asList(muteData.getMuter() == null ? "Console" : muteData.getMuter().getUniqueId().toString(), muteData.getMuter() == null ? "Console" : muteData.getMuter().getName()));
		pConfig.set("chat.mute.at", Arrays.asList("" + (muteData.getMuteTimeStamp() > 0 ? muteData.getMuteTimeStamp() : ""), muteData.getMuteTimeStamp() > 0 ? muteData.getMuteTimeStampString() : ""));
		
		for(Map.Entry<Integer, Mail> mailEntry : mail.entrySet()) {
			String path = "mail." + mailEntry.getKey() + ".";
			
			pConfig.set(path + "name", Arrays.asList(mailEntry.getValue().getSenderID().toString(), mailEntry.getValue().getSender().getName()));
			pConfig.set(path + "time", Arrays.asList(mailEntry.getValue().getTimeStamp() + "", mailEntry.getValue().getDateTime()));
			pConfig.set(path + "unread", mailEntry.getValue().isUnread());
			pConfig.set(path + "message", mailEntry.getValue().getMessage());
			
		}
	}
}
