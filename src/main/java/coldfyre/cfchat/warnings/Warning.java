package coldfyre.cfchat.warnings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * Contains information about a warning that an administrator issued. Note that this does not apply to an individual;
 * as such it is the responsibility of the programmer to attach this to an entity that it represents.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class Warning {
	
	private String reason;
	private String timeStampString;
	private long timeStamp;
	private OfflinePlayer warner;
	
	// Constructor for Static Method
	// Format of Warning String:
	// long_String§String§UUID_String§String§Value_String
	private Warning() { }
	
	/**
	 * Creates a new Warning with the issuer and reason. The Time Stamp of the warning
	 * is created during the initialization of this object.
	 * 
	 * @param warner - Issuer of warning
	 * @param reason - Reason of warning
	 */
	public Warning(OfflinePlayer warner, String reason) {
		this.reason = reason.replaceAll("§", "_CFUNIQUE_");
		this.warner = warner;
		timeStamp = System.currentTimeMillis();
		timeStampString = new SimpleDateFormat("dd MMM YYYY kk:mm:ss").format(new Date(timeStamp));
	}
	
	/**
	 * Used to load a warning from the Player Config file. The data stored should be given
	 * as a string (in the format that's stored as). The string is then split and collected
	 * for the data within.
	 * 
	 * @param value - Warning data
	 * @return Warning
	 */
	public static Warning loadFromConfig(String value) {
		Warning w = new Warning();
		String[] total = value.split("§");
		
		w.timeStamp = Long.parseLong(total[0]);
		w.timeStampString = total[1];
		w.warner = total[2].equalsIgnoreCase("console") ? null : Bukkit.getServer().getOfflinePlayer(UUID.fromString(total[2]));
		w.reason = total[4].replaceAll("_CFUNIQUE", "§");
		
		return w;
	}
	
	/**
	 * Gets the reason for the warning.
	 * 
	 * @return String - Warning Reason
	 */
	public String getReason() { return reason.replaceAll("_CFUNIQUE_", "§"); }
	
	/**
	 * Gets the Time Stamp of the warning in a user-friendly display/format.
	 * 
	 * @return String - user-friendly formatted
	 */
	public String getTimeStampString() { return timeStampString; }
	
	/**
	 * Gets the Time Stamp of the warning.
	 * 
	 * @return long - warning time stamp
	 */
	public long getTimeStamp() { return timeStamp; }
	
	/**
	 * Gets the warner for this warning. If the Warner is NULL, then the console is the issuer.
	 * 
	 * @return Offlineplayer - Issuer of warning
	 */
	public OfflinePlayer getWarner() { return warner; }
}
