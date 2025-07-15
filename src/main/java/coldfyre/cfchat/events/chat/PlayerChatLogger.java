package coldfyre.cfchat.events.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import coldfyre.cfchat.configuration.ConfigMain;
import coldfyre.cfchat.manager.CFManager;
import coldfyre.cfchat.manager.ConfigManager;
import coldfyre.cfchat.players.PlayerLog.LogType;

public class PlayerChatLogger implements Listener {
	
	private CFManager manager;
	private ConfigMain main = manager.getConfigManager().getConfig(ConfigManager.CONFIG_MAIN);
	
	public PlayerChatLogger(CFManager manager) {
		this.manager = manager;
		manager.getPlugin().getServer().getPluginManager().registerEvents(this, manager.getPlugin());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if(main.isLoggerEnabled() && main.isLoggingMessages())
			manager.getPlayerManager().getPlayerConfig(event.getPlayer()).getPlayerLog().addLog(LogType.MESSAGES, event.getMessage());
	}

}
