package dev.timecoding.snowfieldpvp.config.object;

import dev.timecoding.snowfieldpvp.config.LanguageHandler;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageData {

    private YamlConfiguration cfg;
    private LanguageHandler handler;

    public LanguageData(LanguageHandler handler, YamlConfiguration cfg){
        this.cfg = cfg;
        this.handler = handler;
    }

    public void setString(String key, String value){
        cfg.set(key, value);
        save();
    }

    public void setInteger(String key, Integer value){
        cfg.set(key, value);
        save();
    }

    public void setBoolean(String key, Boolean value){
        cfg.set(key, value);
        save();
    }

    public String getString(String key){
        if(keyExists(key)){
            return ChatColor.translateAlternateColorCodes('&', cfg.getString(key));
        }else{
            setString(key, key.toUpperCase());
        }
        return "";
    }

    public Integer getInteger(String key){
        if(keyExists(key)){
            return cfg.getInt(key);
        }else{
            setInteger(key, 0);
        }
        return 0;
    }

    public boolean getBoolean(String key){
        if(keyExists(key)){
            return cfg.getBoolean(key);
        }else{
            setBoolean(key, false);
        }
        return false;
    }

    public boolean keyExists(String key){
        return (cfg.get(key) != null);
    }

    public void save(){
        handler.saveSelectedLanguageConfig();
    }

}
