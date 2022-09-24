package dev.timecoding.snowfieldpvp.database.sqlite;

import dev.timecoding.snowfieldpvp.SnowfieldPVP;
import dev.timecoding.snowfieldpvp.config.ConfigHandler;
import dev.timecoding.snowfieldpvp.enums.SnowFiles;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteConnector {

    private SnowfieldPVP plugin;
    private ConfigHandler cfg;
    private Logger logger;

    private SnowFiles type = null;

    public SQLiteConnector(SnowfieldPVP plugin){
        this.plugin = plugin;
        this.cfg = plugin.getCustomConfig();
        this.logger = plugin.getLogger();
    }

    public void init(){
        setupFolder();
    }

    public SQLiteConnector setupFolder(){
        if(folderExists()){
            File f = new File(plugin.getDataFolder()+"//"+subfolderName());
            if(!folderExists() && subfolderEnabled()){
                boolean created = f.mkdirs();
                if(!created){
                    logger.warning("Error while creating "+f.getName()+"-Folder! This could happen because the plugin do not have enough permissions!");
                }else{
                    logger.info(f.getName()+"-Folder created!");
                }
            }
        }
        return this;
    }


    public SQLiteConnector selectFileType(SnowFiles type){
        this.type = type;
        if(!getGeneratedFile().exists()){
            setupFileorFiles();
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
            logger.warning("Error while making folderrequest, because no SQLiteType was selected! (API-Request)");
        }
        return false;
    }

    public SQLiteConnector setupFileorFiles(){
        if(this.type != null){
            File gen = new File(plugin.getDataFolder().toString(), getTypeString()+".db");
            if(subfolderEnabled()){
                gen = new File(plugin.getDataFolder()+"//"+subfolderName(), getTypeString()+".db");
            }
            if(!gen.exists()){
                try {
                    gen.createNewFile();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error while creating new File", e);
                }
            }
        }else{
            for(SnowFiles f : SnowFiles.values()){
                String s = f.name().toLowerCase();
                File gen = new File(plugin.getDataFolder().toString(), s+".db");
                if(subfolderEnabled()){
                    gen = new File(plugin.getDataFolder()+"//"+subfolderName(), s+".db");
                }
                if(!gen.exists()){
                    try {
                        gen.createNewFile();
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Error while creating new File", e);
                    }
                }
            }
        }
        return this;
    }

    private File getGeneratedFile(){
        return new File(plugin.getDataFolder().toString(), getTypeString()+".db");
    }

    private File getGeneratedFileWithEnum(SnowFiles type){
        return new File(plugin.getDataFolder().toString(), type.name().toString().toLowerCase()+".db");
    }

    private boolean fileExists(){
        return new File(plugin.getDataFolder().toString(), getTypeString()+".db").exists();
    }

    private boolean fileExistsWithEnum(SnowFiles type){
        return new File(plugin.getDataFolder().toString(), type.name().toString().toLowerCase()+".db").exists();
    }

    public SnowFiles getType(){
        return this.type;
    }

    public String getTypeString(){
        return this.type.toString().toLowerCase();
    }

    public boolean subfolderEnabled(){
        return cfg.getBoolean("Databases.SQLite.Subfolder.Enabled");
    }

    public String subfolderName(){
        return cfg.getString("Databases.SQLite.Subfolder.Name");
    }


    //SQLITE CONNECTION AREA

    private HashMap<SnowFiles, Connection> connection = new HashMap<>();

    public Connection getSQLiteConnection(SnowFiles type) throws ClassNotFoundException, SQLException {
            Class.forName("org.sqlite.JDBC");
            if(fileExistsWithEnum(type)){
                if(!connection.containsKey(type) && !connection.get(type).isClosed()){
                    Connection con = DriverManager.getConnection("jdbc:sqlite:" + getGeneratedFileWithEnum(type).getPath());
                    connection.put(type, con);
                    return con;
                }else if(connection.containsKey(type) && connection.get(type).isClosed()){
                    connection.remove(type);
                }
            }
        return connection.get(type);
    }

    public boolean connect(SnowFiles type){
        try{
        Connection c = getSQLiteConnection(type);
        logger.info("Successfully connected to your SQLite-Database! ("+getGeneratedFileWithEnum(type).getName()+")");
        return true;
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error while getting Connection ("+getGeneratedFileWithEnum(type).getName()+")", e);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while getting Connection ("+getGeneratedFileWithEnum(type).getName()+")", e);
        }
        return false;
    }

    public boolean connectAll(){
        boolean b = false;
        for(SnowFiles types : SnowFiles.values()){
            boolean c = connect(types);
            if(!c && !b){
                b = true;
            }
        }
        return b;
    }

    public boolean close(SnowFiles type){
        try {
            if(connection.containsKey(type) || (!connection.get(type).isClosed())){
                Connection con = connection.get(type);
                //Close Connection
                con.close();
                //Remove from Hashmap
                connection.remove(type);
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while closing Connection ("+getGeneratedFileWithEnum(type)+")", e);
        }
        return false;
    }

    public boolean closeAll(){
        boolean b = false;
        for(SnowFiles types : SnowFiles.values()){
            boolean c = close(types);
            if(!c && !b){
                b = true;
            }
        }
        return b;
    }




}
