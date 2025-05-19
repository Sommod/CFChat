package coldfyre.cfchat.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;

import coldfyre.cfchat.warnings.Warning;

/**
 * Manager for handling the warnings of server.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class WarnManager {
	private Map<OfflinePlayer, List<Warning>> warnings;
	
	public WarnManager(CFManager manager) {
		warnings = new HashMap<OfflinePlayer, List<Warning>>();
	}
	
	public void reload(CFManager manager) {
		
	}
}
