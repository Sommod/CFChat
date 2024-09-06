package coldfyre.cfchat.players;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * This object contains the information of a player (or console) sending another player mail. Similar
 * to sending a message within chat, this stores the message for long-term use as well as the ability
 * to send 'messages' to players that are not online.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class PlayerMail {

	private int id;
	private long timeStamp;
	private boolean unread;
	private OfflinePlayer sender;
	private UUID senderID;
	private String dateTime;
	private String message;
	
	// Used just for the Static Method 'loadFromConfig'
	private PlayerMail() { }
	
	/**
	 * This creates a new mail object from the given parameters. Several of the other details (such as the time stamp) are
	 * initialized and stored during this constructor call rather than taking a parameter from the user. This ensures the data
	 * is not altered in such a way to crash or otherwise corrupt the object.
	 * 
	 * @param id
	 * @param sender
	 * @param message
	 */
	public PlayerMail(int id, OfflinePlayer sender, String message) {
		this.id = id;
		this.sender = sender;
		senderID = senderID == null ? null : sender.getUniqueId();
		timeStamp = System.currentTimeMillis();
		dateTime = new SimpleDateFormat("dd MMM YYYY kk:mm:ss").format(new Date(timeStamp));
		unread = true;
		this.message = message;
	}
	
	/**
	 * This takes the players 'Config' and grabs all the stored data within the mail object. If the formatting for the mail
	 * object is not followed (as stated within the player config file), then this will break and throw an exception. This
	 * method assumes that the data within the config is correctly stored and does not actually check if the data is there.
	 * 
	 * @param id - ID of the mail
	 * @param config - YamlConfiguration of the player data file
	 * @param path - path of the mail data
	 * @return {@link PlayerMail} object of the mail data
	 */
	public static PlayerMail loadFromConfig(int id, YamlConfiguration config, String path) {
		PlayerMail m = new PlayerMail();
		
		m.id = id;
		m.senderID = config.getStringList(PATH(path, id) + ".name").get(0).equalsIgnoreCase("console") ? null :
			UUID.fromString(config.getStringList(PATH(path, id) + ".name").get(0));
		m.sender = m.senderID == null ? null : Bukkit.getServer().getOfflinePlayer(m.senderID);
		m.timeStamp = Long.parseLong(config.getStringList(PATH(path, id) + ".time").get(0));
		m.dateTime = config.getStringList(PATH(path, id) + ".time").get(1);
		m.unread = config.getBoolean(PATH(path, id) + ".unread");
		m.message = config.getString(PATH(path, id) + ".message");
		
		return m;
	}
	
	/**
	 * Simple method to concatenate the correct strings and id together to get the corresponding path to the
	 * mail data.
	 * 
	 * @param give - path from {@link #loadFromConfig(int, YamlConfiguration, String)}
	 * @param id - id from {@link #loadFromConfig(int, YamlConfiguration, String)}
	 * @return String - complete path of mail location
	 */
	private static String PATH(String give, int id) { return give.endsWith(".") ? give.concat("" + id) : give.concat("." + id); }
	
	
	/**
	 * Gets the 'ID' or rather the unique number that represents this mail object within the config. This is used for both
	 * listing of mail as well as saving mail to the config. Each Mail MUST be a unique number and cannot overlap. If an
	 * overlapping of ID's occur, the last mail with the same id will be saved with the others being overwritten. While a
	 * {@link UUID} would solve this problem, using extra methods and objects to define a simple number is unneeded.
	 * 
	 * @return the id of this mail
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the {@link OfflinePlayer} object of the sender. If the sender is NULL, then the sender was CONSOLE.
	 * 
	 * @return the sender of the mail
	 */
	public OfflinePlayer getSender() {
		return sender;
	}

	/**
	 * Gets the UUID of the sender. If this object is NULL, then this represents that the console sent the message.
	 * 
	 * @return the UUID of the sender
	 */
	public UUID getSenderID() {
		return senderID;
	}

	/**
	 * Gets the time stamp of the mail. This is when the Mail object was created and sent to the player. This is a programmer
	 * value rather than a user-friendly value. To get the visual representation of this number, use the {@link #getDateTime()}
	 * method, which gives a String representation of this object.
	 * 
	 * @return the time stamp - Long value
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * This is just a user-friendly representation of the <strong>time stamp</strong>. The format of this String is given
	 * in the pattern of <strong>dd MMM YYYY kk:mm:ss</strong>. This is used within the {@link SimpleDateFormat} object.
	 * @return the date and time of the mail creation
	 */
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * This returns whether or not this mail has been read. This only applies to which this mail was sent to, if another
	 * person (who has permission) views someone else's mail via commands or logs, then this will remain unread.
	 * 
	 * @return True - if player has not read this mail object
	 */
	public boolean isUnread() {
		return unread;
	}

	/**
	 * The message that the player (or console) sent. Note that this can contain special characters (such as & or §). These
	 * characters are NOT translated within this string, but rather translated when displaying to the given user when viewing.
	 * 
	 * @return the message stored within this mail object
	 */
	public String getMessage() {
		return message;
	}

}
