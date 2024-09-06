package coldfyre.cfchat;

import org.bukkit.plugin.java.JavaPlugin;

import coldfyre.cfchat.manager.CFManager;

/**
 * Main class for this Plugin.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class CFChat extends JavaPlugin {
	private CFManager manager;
	
	@Override
	public void onEnable() { manager = new CFManager(this); }
	
	@Override
	public void onDisable() {
		manager.disable();
	}
}
