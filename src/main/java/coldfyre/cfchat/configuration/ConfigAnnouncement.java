package coldfyre.cfchat.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.coldfyre.api.AbstractConfig;
import com.coldfyre.api.utilities.Util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ConfigAnnouncement extends AbstractConfig implements Runnable {
	
	private Map<String, AnnouncementData> announcements;

	public ConfigAnnouncement(File ymlFile) {
		super(ymlFile);
		announcements = new HashMap<String, AnnouncementData>();
		
		for(String data : bukkitConfig[0].getConfigurationSection("").getKeys(false))
			announcements.put(data, new AnnouncementData(data + "."));
	}
	
	public Set<String> getAnnouncementKeys() { return announcements.keySet(); }
	
	public String getLocation(String announcement) { return announcements.get(announcement).location; }
	public long getDefaultTime(String announcement) { return announcements.get(announcement).defaultTime; }
	public long getTimeCounter(String announcement) { return announcements.get(announcement).nextShowTimeCounter; }
	public void resetTimeCounter(String announcement) { announcements.get(announcement).nextShowTimeCounter = announcements.get(announcement).defaultTime; }
	public List<String> getAnnouncement(String announcement) { return announcements.get(announcement).message; }
	public List<String> getExemptPermissions(String announcement) { return announcements.get(announcement).exemptPermissions; }
	
	@Override
	public void run() {
		for(AnnouncementData ad : announcements.values()) {
			ad.nextShowTimeCounter--;
			
			if(ad.nextShowTimeCounter <= 0) {
				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
					boolean found = false;
					
					if(ad.exemptPermissions != null) {
						for(String perm : ad.exemptPermissions) {
							if(p.hasPermission(perm)) {
								found = true;
								break;
							}
						}
					}
					
					if(!found) {
						TextComponent send = new TextComponent();
							
						for(String line : ad.message)
							send.addExtra(line.replace('&', '§') + "\n");
						
						send.setText(send.getText().substring(0,send.getText().length() - 2));
						
						p.spigot().sendMessage(ChatMessageType.valueOf(ad.location.toUpperCase()), send);
					}
				}
			}
		}
	}
	
	private class AnnouncementData {
		private String location;
		private long defaultTime;
		private long nextShowTimeCounter;
		private List<String> message;
		private List<String> exemptPermissions;
		
		@SuppressWarnings("unchecked")
		public AnnouncementData(String data) {
			location = bukkitConfig[0].getString(data + "loc");
			defaultTime = Util.toTime(bukkitConfig[0].getString(data + "time"));
			nextShowTimeCounter = defaultTime;
			message = getMessageList(data);
			ChatMessageType.valueOf("CHAT");
			
			exemptPermissions = bukkitConfig[0].contains(data + "exempt") ? (List<String>) bukkitConfig[0].getList(data + "exempt") : null;
		}
		
		private List<String> getMessageList(String data) {
			if(bukkitConfig[0].isList(data + "message"))
				return bukkitConfig[0].getStringList(data + "message");
			else {
				List<String> collect = new ArrayList<String>();
				collect.add(bukkitConfig[0].getString(data + "message"));
				
				return collect;
			}
		}
	}

}
