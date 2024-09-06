package coldfyre.cfchat.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.coldfyre.api.AbstractConfig;
import com.coldfyre.api.manager.FilesManager;

import coldfyre.cfchat.CFChat;

public class ConfigAlternateFilter extends AbstractConfig {

	private List<String> pub, select, reject, allWords, blockedWords;
	
	public ConfigAlternateFilter(File ymlFile) {
		super(ymlFile);
		
		pub = bukkitConfig[0].getStringList("public");
		select = bukkitConfig[0].getStringList("selective");
		reject = bukkitConfig[0].getStringList("reject");
		
		loadDefaultWords();
	}
	
	private void loadDefaultWords() {
		CFChat plugin = CFChat.getPlugin(CFChat.class);

		blockedWords = readFile(plugin.getClass().getResourceAsStream("blocked_words.txt"));
		
		allWords = readFile(plugin.getClass().getResourceAsStream("all_words.txt"));
		allWords.removeAll(blockedWords);
	}
	
	private List<String> readFile(InputStream is) {
		List<String> ret = new ArrayList<String>();
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			String line;
			
			while((line = reader.readLine()) != null)
				allWords.add(line);
			
		} catch (IOException e) {
			FilesManager.LogException(e);
		}
		
		return ret;
	}
	
	public List<String> getWords(Words word) {
		return switch (word) {
			case PUBLIC:
				yield pub;
			
			case SELECTIVE:
				yield select;
			
			case REJECT:
				yield reject;
		
			default:
				throw new IllegalArgumentException("Unexpected value: " + word);
			};
	}
	
	public List<String> getDefaultAllowedWords() { return allWords; }
	
	public List<String> getDefaultBlockedWords() { return blockedWords; }
	
	public enum Words {
		PUBLIC, SELECTIVE, REJECT;
	}
}
