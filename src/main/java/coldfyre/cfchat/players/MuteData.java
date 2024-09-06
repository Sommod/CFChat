package coldfyre.cfchat.players;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Contains the data of a player mute. This class does not pertain to a particular player; it's the responsibility
 * of the programmer to attach this data to an entity. This only provides methods to stored information about a mute.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class MuteData {

	private boolean muted;
	private long time, timeStamp;
	private OfflinePlayer muter;
	
	/**
	 * Creates a new MuteData object. All values are NULL by default.
	 */
	public MuteData() { }
	
	/**
	 * Loads the MuteData from the config file. If the time of mute is past the set mute release time, then
	 * all the data is cleared as though an unmute had occurred.
	 * 
	 * @param config - Player config file
	 * @return MuteData object
	 */
	public static MuteData loadMuteData(YamlConfiguration config) {
		MuteData d = new MuteData();
		
		d.time = Long.parseLong(config.getStringList("chat.mute.time").get(0));
		
		if(d.time <= System.currentTimeMillis()) {
			d.time = 0L;
			d.muted = false;
			d.timeStamp = 0L;
			d.muter = null;
			
			return d;
		}
		
		d.muted = config.getBoolean("chat.mute.on");
		d.timeStamp = Long.parseLong(config.getStringList("chat.mute.at").get(0));
		d.muter = config.getStringList("chat.mute.by").get(0).equalsIgnoreCase("console") ? null :
			Bukkit.getServer().getOfflinePlayer(UUID.fromString(config.getStringList("chat.mute.by").get(0)));
		
		return d;
	}
	
	/**
	 * Sets the data for the mute based on the muter and time. If the muter is console, then set the muter value
	 * to <strong>null</strong>. The Time Stamp is set during this method call.
	 * 
	 * @param muter - Entity that muted
	 * @param time - Release time of mute
	 */
	public void mute(OfflinePlayer muter, long time) {
		muted = true;
		this.time = time;
		timeStamp = System.currentTimeMillis();
		this.muter = muter;
	}
	
	/**
	 * Removes or clears the mute data from this object.
	 */
	public void unmute() {
		muted = false;
		time = 0L;
		timeStamp = 0L;
		muter = null;
	}
	
	/**
	 * Checks if the data for this object is muted.
	 * 
	 * @return True - if muted
	 */
	public boolean isMuted() { return muted; }
	
	/**
	 * Gets the time of release for the mute.
	 * 
	 * @return Long - Release mute time
	 */
	public long getMuteTime() { return time; }
	
	/**
	 * Time of the when the mute occurred.
	 * 
	 * @return Long - Time Stamp of muting time
	 */
	public long getMuteTimeStamp() { return timeStamp; }
	
	/**
	 * Gets the mute relase time in a more user-friendly display. Format will be:</br>
	 * Day Month Year Hour:Min:Sec
	 * 
	 * @return String - User-Friendly formatted time
	 */
	public String getMuteTimeString() { return convertTime(time); }
	
	/**
	 * Gets the Mute Time Stamp in a more user-friendly display. Format will be:</br>
	 * Day Month Year Hour:Min:Sec
	 * 
	 * @return String - User-Friendly formatted time
	 */
	public String getMuteTimeStampString() { return convertTime(timeStamp); }
	
	/**
	 * Gets the entity that muted. Note that if this is NULL, then the muter was Console; not a player.
	 * 
	 * @return OfflinePlayer - Entity that muted
	 */
	public OfflinePlayer getMuter() { return muter; }
	
	// converts the long value into a user-friendly string representation
	private String convertTime(long time)  { return new SimpleDateFormat("dd MMM YYYY kk:mm:ss").format(new Date(time)); }
}
