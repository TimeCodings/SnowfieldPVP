package dev.timecoding.snowfieldpvp.config;

import dev.timecoding.snowfieldpvp.SnowfieldPVP;
import dev.timecoding.snowfieldpvp.database.file.FileManager;
import dev.timecoding.snowfieldpvp.enums.SnowFiles;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {

    private SnowfieldPVP plugin;

    public ConfigHandler(SnowfieldPVP plugin){
        this.plugin = plugin;
    }

    private File f = null;
    public YamlConfiguration cfg = null;

    public void init(){
        plugin.saveDefaultConfig();
        f = new File(plugin.getDataFolder(), "config.yml");
        cfg = YamlConfiguration.loadConfiguration(f);
        cfg.options().copyDefaults(true);
    }

    public String getPluginVersion(){
        return plugin.getDescription().getVersion();
    }



    public void save(){
        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload(){
        cfg = YamlConfiguration.loadConfiguration(f);
    }

    public YamlConfiguration getConfig(){
        return cfg;
    }

    public void setString(String key, String value){
        cfg.set(key, value);
        save();
    }

    public Integer getInteger(String key){
        if(keyExists(key)){
            return cfg.getInt(key);
        }
        return null;
    }

    public String getString(String key){
        if(keyExists(key)){
            return cfg.getString(key);
        }
        return null;
    }

    public Boolean getBoolean(String key){
        if(keyExists(key)){
            return cfg.getBoolean(key);
        }
        return null;
    }

    public boolean keyExists(String key){
        if(cfg.get(key) != null){
            return true;
        }
        return false;
    }
}
