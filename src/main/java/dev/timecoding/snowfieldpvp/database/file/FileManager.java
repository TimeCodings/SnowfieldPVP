package dev.timecoding.snowfieldpvp.database.file;

import dev.timecoding.snowfieldpvp.SnowfieldPVP;
import dev.timecoding.snowfieldpvp.config.ConfigHandler;
import dev.timecoding.snowfieldpvp.enums.SnowFiles;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {

    private SnowfieldPVP plugin;
    private SnowFiles type = null;
    private ConfigHandler cfg;
    private Logger logger;

    public FileManager(SnowfieldPVP plugin){
        this.plugin = plugin;
        this.cfg = this.plugin.getCustomConfig();
        this.logger = this.plugin.getLogger();
        setupFolder();
    }

    public FileManager selectFileType(SnowFiles type){
        this.type = type;
        if(!getGeneratedFile().exists()){
            setupFileorFiles();
        }
        return this;
    }

    public FileManager setupFolder(){
            File f = new File(plugin.getDataFolder()+"//"+subfolderName());
            if(!subfolderEnabled() || subfolderEnabled() && !f.exists()){
                boolean created = f.mkdirs();
                if(!created){
                    logger.warning("Error while creating "+f.getName()+"-Folder!");
                }else{
                    logger.info(f.getName()+"-Folder created!");
                }
            }
        return this;
    }

    public boolean folderExists(){
        if(this.type != null){
            File f = new File(plugin.getDataFolder()+"//"+subfolderName());
            if(!subfolderEnabled() || subfolderEnabled() && f.exists()){
                return true;
            }
        }else{
            logger.warning("Error while making folderrequest, because no FileType was selected!");
        }
        return false;
    }

    public YamlConfiguration toYAMLDatas(){
        if(this.type != null){
            File f = getGeneratedFile();
            if(!f.exists()){
                setupFolder();
                setupFileorFiles();
            }
            return YamlConfiguration.loadConfiguration(f);
        }else{
            logger.warning("Error while converting to YAMLDatas, because no FileType was selected!");
        }
        return null;
    }

    public void saveToYAML(){
        if(this.type != null){
            File f = getGeneratedFile();
            try {
                if(!f.exists()){
                    setupFolder();
                    setupFileorFiles();
                }
                YamlConfiguration.loadConfiguration(f).save(f);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error while creating new File:", e);
            }
        }else{
            for(SnowFiles f : SnowFiles.values()){
                String s = f.name().toLowerCase();
                File gen = new File(plugin.getDataFolder().toString(), s+".yml");
                try {
                    YamlConfiguration.loadConfiguration(gen).save(gen);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error while creating new File:", e);
                }
            }
        }
    }

    public boolean subfolderEnabled(){
        return cfg.getBoolean("Databases.Files.Subfolder.Enabled");
    }

    public String subfolderName(){
        return cfg.getString("Databases.Files.Subfolder.Name");
    }

    public FileManager setupFileorFiles(){
        if(this.type != null){
            File gen = new File(plugin.getDataFolder().toString(), getTypeString()+".yml");
            if(subfolderEnabled()){
                gen = new File(plugin.getDataFolder()+"//"+subfolderName(), getTypeString()+".yml");
            }
            if(!gen.exists()){
                try {
                    gen.createNewFile();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error while creating new File:", e);
                }
            }
        }else{
            for(SnowFiles f : SnowFiles.values()){
                String s = f.name().toLowerCase();
                File gen = new File(plugin.getDataFolder().toString(), s+".yml");
                if(subfolderEnabled()){
                    gen = new File(plugin.getDataFolder()+"//"+subfolderName(), s+".yml");
                }
                if(!gen.exists()){
                    try {
                        gen.createNewFile();
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Error while creating new File:", e);
                    }
                }
            }
        }
        return this;
    }

    private File getGeneratedFile(){
        return new File(plugin.getDataFolder().toString(), getTypeString()+".yml");
    }

    public SnowFiles getType(){
        return this.type;
    }

    public String getTypeString(){
        return this.type.toString().toLowerCase();
    }

}
