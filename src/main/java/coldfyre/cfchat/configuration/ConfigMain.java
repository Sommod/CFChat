package coldfyre.cfchat.configuration;

import java.io.File;

import com.coldfyre.api.AbstractConfig;

public class ConfigMain extends AbstractConfig {

	public enum ItemDisplayType {
		NAME, PLAYER, ITEM, CUSTOM;
		
		public static ItemDisplayType getType(String value) {
			for(ItemDisplayType idt : values()) {
				if(idt.name().equalsIgnoreCase(value))
					return idt;
			}
			
			return CUSTOM;
		}
	}
	
	public ConfigMain(File ymlFile) {
		super(ymlFile);
	}
	
	public boolean isAnnoucementsEnabled() { return bukkitConfig[0].getBoolean("chat.announcements.enabled"); }
	public boolean isItemDisplayingEnabled() { return bukkitConfig[0].getBoolean("chat.item.enabled"); }
	public ItemDisplayType getItemDisplayType() { return ItemDisplayType.getType(bukkitConfig[0].getString("chat.item.display")); }
	public String getItemDisplayFormat() { return bukkitConfig[0].getString("chat.item.display"); }
	public String getItemKeyword() { return bukkitConfig[0].getString("chat.item.showitem_keyword"); }
	public boolean isBlacklistEnabled() { return bukkitConfig[0].getBoolean("chat.blacklist.enabled"); }
	public boolean isGroupFilterEnabled() { return bukkitConfig[0].getBoolean("chat.blacklist.group_filter"); }
	public boolean isStaffFilterEnabled() { return bukkitConfig[0].getBoolean("chat.blacklist.staff_filter"); }
	public boolean isUsingAlternateFilter() { return bukkitConfig[0].getBoolean("chat.blacklist.use_alternative_filter"); }
	public double getFilterPercent() { return bukkitConfig[0].getDouble("chat.blacklist.percent"); }
	public boolean isFilterWordOnly() { return bukkitConfig[0].getString("chat.blacklist.type").equalsIgnoreCase("word"); }
	public boolean isFilterReponseEnabled() { return bukkitConfig[0].getBoolean("chat.blacklist.respond"); }
	public String getFilterReponse() { return bukkitConfig[0].getString("chat.blacklist.message"); }
	public boolean isSpamFilterEnabled() { return bukkitConfig[0].getBoolean("chat.spam.enabled"); }
	public int getSpamMessageInterval() { return bukkitConfig[0].getInt("chat.spam.message_interval"); }
	public boolean isSpamResetTimer() { return bukkitConfig[0].getBoolean("chat.spam.reset_timer"); }
	public String getSpamMessage() { return bukkitConfig[0].getString("chat.spam.message"); }
	public boolean isLoggerEnabled() { return bukkitConfig[0].getBoolean("chat.logger.enabled"); }
	public boolean isLoggingMessages() { return bukkitConfig[0].getBoolean("chat.logger.log_messages"); }
	public boolean isLoggingMail() { return bukkitConfig[0].getBoolean("chat.logger.log_mail"); }
	public boolean isLoggingCommands() { return bukkitConfig[0].getBoolean("chat.logger.log_commands"); }
	
	public boolean isGroupEnabled() { return bukkitConfig[0].getBoolean("group.enabled"); }
	public boolean isGroupOptionPublic() { return bukkitConfig[0].getBoolean("group.options.public"); }
	public int getGroupOptionPlayers() { return bukkitConfig[0].getInt("group.options.players.default"); }
	public int getGroupOptionMaxPlayers() { return bukkitConfig[0].getInt("group.options.players.max_players"); }
	public boolean isGroupOptionIgnore() { return bukkitConfig[0].getBoolean("group.options.ignore.default"); }
	public boolean isGroupOptionIngoreEditable() { return bukkitConfig[0].getBoolean("group.options.ignore.allow_edit"); }
	public String getGroupOptionFormat() { return bukkitConfig[0].getString("group.options.format"); }
	public boolean isWarnSystemEnabled() { return bukkitConfig[0].getBoolean("warn.enabled"); }
	public boolean isAutoWarnEnabled() { return bukkitConfig[0].getBoolean("warn.enable_auto"); }
	public boolean isWarnDecayEnabled() { return bukkitConfig[0].getBoolean("warn.decay.enabled"); }
	public String getRawDecayTime() { return bukkitConfig[0].getString("warn.decay.time"); }
}
