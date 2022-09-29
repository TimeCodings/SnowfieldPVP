package dev.timecoding.snowfieldpvp.config;

import dev.timecoding.snowfieldpvp.SnowfieldPVP;
import dev.timecoding.snowfieldpvp.config.object.LanguageData;
import dev.timecoding.snowfieldpvp.enums.SnowLanguage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class LanguageHandler {

    private SnowfieldPVP plugin;
    private ConfigHandler configHandler;
    private @NotNull Logger logger;
    private YamlConfiguration config;
    private File selconfigfile;

    public LanguageHandler(SnowfieldPVP plugin){
        this.plugin = plugin;
        this.configHandler = plugin.getCustomConfig();
        this.logger = plugin.getLogger();
    }

    public SnowLanguage getLanguage(){
        String language = configHandler.getString("Language");
        if(language.equalsIgnoreCase("German") || language.equalsIgnoreCase("DE")){
            return SnowLanguage.DE;
        }else{
            return SnowLanguage.EN;
        }
    }

    public void setLanguage(SnowLanguage lang){
        configHandler.setString("Language", lang.name());
    }

    public LanguageData getSelectedLanguageConfig(){
        return new LanguageData(this, config);
    }

    public void saveSelectedLanguageConfig(){
        try {
            this.config.save(this.selconfigfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setup(){
        File folder = new File(plugin.getDataFolder()+"//languages");
        File file = new File(plugin.getDataFolder()+"//languages", getFileNameByLang(getLanguage()));
        if(!folder.exists()){
            logger.info("Language-Folder got generated!");
            folder.mkdirs();
        }
        if(!file.exists()){
            logger.info("Language-File '"+getLanguage().name()+".yml' in folder languages got generated!");
            plugin.saveResource(getFileNameByLang(getLanguage()), false);
            File gen = new File(plugin.getDataFolder(), getFileNameByLang(getLanguage()));
            gen.renameTo(new File(plugin.getDataFolder()+"//languages", getFileNameByLang(getLanguage())));
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        this.selconfigfile = file;
    }

    public String getFileNameByLang(SnowLanguage lang){
        if(lang.name().equalsIgnoreCase("DE")){
            return "germanlang.yml";
        }else{
            return "englishlang.yml";
        }
    }

}
