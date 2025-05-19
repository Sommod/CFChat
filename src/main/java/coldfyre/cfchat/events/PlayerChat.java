package coldfyre.cfchat.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import coldfyre.cfchat.configuration.ConfigMain;
import coldfyre.cfchat.manager.CFManager;
import coldfyre.cfchat.manager.ConfigManager;
import coldfyre.cfchat.players.PlayerLog.LogType;

public class PlayerChat implements Listener {
	
	private CFManager manager;
	private ConfigMain main = manager.getConfigManager().getConfig(ConfigManager.CONFIG_MAIN);
	
	public PlayerChat(CFManager manager) {
		this.manager = manager;
		manager.getPlugin().getServer().getPluginManager().registerEvents(this, manager.getPlugin());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		// TODO: Create Temporary variables about who, what and where player chat event was fired.
		String playerChat = manager.getPlayerManager().getPlayerChat(event.getPlayer());
		boolean inMainChat = playerChat.equalsIgnoreCase("Main");
		
		if(main.isLoggerEnabled() && main.isLoggingMessages())
			manager.getPlayerManager().getPlayerConfig(event.getPlayer()).getPlayerLog().addLog(LogType.MESSAGES, event.getMessage());
		
		// Event is already set to be cancelled, no need to perform any action.
		if(event.isCancelled())
			return;
		
		//TODO: Check if filter is active
		//TODO: Perform filter on player if needed
		//TODO: Perform chat limiting on who sees message
	}
}
