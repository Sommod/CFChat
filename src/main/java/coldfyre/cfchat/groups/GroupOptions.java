package coldfyre.cfchat.groups;

import org.bukkit.configuration.file.YamlConfiguration;

import coldfyre.cfchat.configuration.ConfigMain;

/**
 * Configuration options that affect the group.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class GroupOptions {
	private String groupName, format;
	private int maxPlayers;
	private boolean ignore, open;
	
	// Basic Constructor for static constructor
	private GroupOptions() { }
	
	public GroupOptions(String groupName, ConfigMain config) {
		format = config.getGroupOptionFormat();
		maxPlayers = config.getGroupOptionPlayers();
		ignore = config.isGroupOptionIgnore();
		open = config.isGroupOptionPublic();
		this.groupName = groupName.replace('&', '§');
	}
	
	public static GroupOptions loadFromConfig(YamlConfiguration groupConfig) {
		GroupOptions g = new GroupOptions();
		
		g.format = groupConfig.getString("options.format").replace('&', '§');
		g.open = groupConfig.getBoolean("options.public");
		g.groupName = groupConfig.getString("options.name").replace('&', '§');
		g.maxPlayers = groupConfig.getInt("options.max");
		g.ignore = groupConfig.getBoolean("options.ignore");
		
		return g;
	}

	/**
	 * This gets the name of the group. Note that the name can have color formatting in it.
	 * 
	 * @return The name of the group
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @return the maxPlayers
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * @return the ignore
	 */
	public boolean isIgnore() {
		return ignore;
	}

	/**
	 * @return the open
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @param maxPlayers the maxPlayers to set
	 */
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	/**
	 * @param ignore the ignore to set
	 */
	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}
}
