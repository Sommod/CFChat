package coldfyre.cfchat.configuration;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.coldfyre.api.AbstractConfig;

public class ConfigData extends AbstractConfig {

	public ConfigData(File ymlFile) {
		super(ymlFile);
	}
	
	public YamlConfiguration getData() { return bukkitConfig[0]; }
}
