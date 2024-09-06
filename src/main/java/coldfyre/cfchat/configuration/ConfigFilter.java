package coldfyre.cfchat.configuration;

import java.io.File;
import java.util.List;

import com.coldfyre.api.AbstractConfig;

public class ConfigFilter extends AbstractConfig {

	private List<String> blacklist, whitelist;
	
	public ConfigFilter(File... ymlFiles) {
		super(ymlFiles);
		
		blacklist = bukkitConfig[0].getStringList("blacklist");
		whitelist = bukkitConfig[1].getStringList("whitelist");
	}
	
	public List<String> getBlacklist() { return blacklist; }
	public List<String> getWhitelist() { return whitelist; }
	
	public boolean contains(int which, String word) { return which == 0 ? blacklist.contains(word) : whitelist.contains(word); }
}
