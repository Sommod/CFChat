package coldfyre.cfchat.players;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * This object contains all the information about the logged details of the player. The format of the logger information
 * is based on the type of log as well as where the player detail was used. To clarify, if the <strong>messages</strong>
 * logger is enabled, then the messages will contain a format for both <strong>Regular, Group and Staff</strong> chat
 * messages. To view the format of the logger, view the 'Player Config' file for more information.
 * 
 * @author Sommod
 * @version 1.0
 *
 */
public class PlayerLog {
	
	/**
	 * Simple Enum for classifying the different loggers.
	 * 
	 * @author Sommod
	 * @version 1.0
	 *
	 */
	public enum LogType {
		MESSAGES, MAIL, COMMANDS;
	}
	
	private Map<LogType, List<String>> logger;
	
	/**
	 * Creates a new object that stores Lists of Strings with formatted data. Note that this does
	 * not pertain to any player, rather this simply contains data. It's the programmers responsibility
	 * to attach this to a given player (or entity) as well as manage any changing data with said entity.
	 */
	public PlayerLog() { logger = new HashMap<PlayerLog.LogType, List<String>>(); }
	
	/**
	 * Loads a PlayerLog Object from a players Data Config File.
	 * 
	 * @param playerConfig - Players YamlConfiguration object
	 * @return PlayerLog
	 */
	public static PlayerLog loadFromConfig(YamlConfiguration playerConfig) {
		PlayerLog log = new PlayerLog();
		
		for(String logData : playerConfig.getConfigurationSection("chat.logger").getKeys(false))
			log.logger.put(LogType.valueOf(logData.toUpperCase()), playerConfig.getStringList(logData));
		
		return log;
	}
	
	/**
	 * This will get the raw and formatted for this object string. Note that while this does contain the 
	 * data that was stored, it may not be in user-friendly viewing (meaning some data may have changed).
	 * 
	 * @param type - Logger type to get
	 * @param value - value from list
	 * @return String - Raw String value
	 */
	public String getRawString(LogType type, int value) { return logger.get(type).get(value); }
	
	/**
	 * Gets the entire list of data values from a given logger, but with all the object formatting in place.
	 * 
	 * @param type - Logger type to get
	 * @return List - Logger list
	 */
	public List<String> getRawList(LogType type) { return logger.get(type); }
	
	/**
	 * Gets the size of the logger. This is a helpful method used in tangent with {@link #getRawString(LogType, int)}.
	 * 
	 * @param type - Logger type to get
	 * @return int - size of logger
	 */
	public int getLoggerSize(LogType type) { return logger.get(type).size(); }
	
	/**
	 * Gets a list of logger data based on the log type and the dates to get from. If the given log info is within
	 * the given dates, then this will return it in a list format. The format of the string contains both the time
	 * stamp and the logger information. The format is as follows: </br>
	 * <strong>[01 Jan 1970 23:59:59] LOGGER_VALUE</strong>
	 * 
	 * @param type - Logger type
	 * @param from - Start Date
	 * @param to - End Date
	 * @return List of Logger Data
	 */
	public List<String> getListByDates(LogType type, final Date from, final Date to) {
		boolean first = from.before(to);
		List<String> values = new ArrayList<String>();
		Date use = new Date();

		for(String s : logger.get(type)) {
			use.setTime(Long.parseLong(s.split("§")[1]));
			
			if(use.after(first ? from : to) && use.before(first ? to : from))
				values.add("[" + s.split("§")[1] + "] " + s.split("§")[2].replaceAll("_CFUNIQUE_", "§"));
		}
		
		return values;
	}
	
	/**
	 * Gets the entire list of Logger Data based on the Logger type.
	 * 
	 * @param type - Logger type
	 * @return List of Logger data
	 */
	public List<String> getList(LogType type) {
		return getListByDates(type, new Date(0L), new Date(System.currentTimeMillis()));
	}
	
	/**
	 * Adds a new log to the given Logger type. Formatting is reserved within this method rather than
	 * in-taking the formatted string. The Date and time is calculated during this method call and
	 * placed within the string details. Addtionally, if any symbols that could result in an error
	 * are found, they are replaced with unique values for later reformatting during call of said log data.
	 * 
	 * @param type - Logger type
	 * @param value - Log to add
	 */
	public void addLog(LogType type, String value) {
		value = value.contains("§") ? value.replaceAll("§", "_CFUNIQUE_") : value;
		
		List<String> log = logger.get(type);
		long time = System.currentTimeMillis();
		
		log.add(time + "§" + new SimpleDateFormat("dd MMM YYYY kk:mm:ss").format(new Date(time)) + "§" + value);
		logger.put(type, log);
	}
	
	/**
	 * Clears the Logger type of all data.
	 * 
	 * @param type - Logger type
	 */
	public void clear(LogType type) { logger.put(type, new ArrayList<String>()); }
	
	/**
	 * Clears ALL logger data from all logger types. It is suggested to only use this when
	 * the player's data has been corrupted or otherwise needs to be replaced.
	 */
	public void clear() {
		for(LogType t : LogType.values())
			clear(t);
	}
}
