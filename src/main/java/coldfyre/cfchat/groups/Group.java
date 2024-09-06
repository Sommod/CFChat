package coldfyre.cfchat.groups;

import java.util.Map;

import org.bukkit.OfflinePlayer;

/**
 * Class that represents a group within the server.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class Group {

	public enum GroupRank {
		MEMBER, ADMIN, OWNER, SPY;
	}
	
	private Map<GroupRank, OfflinePlayer> players;
	
	
}
