package dev.timecoding.snowfieldpvp.database;

import dev.timecoding.snowfieldpvp.SnowfieldPVP;
import dev.timecoding.snowfieldpvp.config.ConfigHandler;
import dev.timecoding.snowfieldpvp.database.file.FileManager;
import dev.timecoding.snowfieldpvp.database.mysql.MySQLConnector;
import dev.timecoding.snowfieldpvp.database.sqlite.SQLiteConnector;
import dev.timecoding.snowfieldpvp.enums.SnowDatabaseType;
import dev.timecoding.snowfieldpvp.object.DatabaseObject;

import java.util.logging.Logger;

public class DatabaseService {

    private SnowfieldPVP plugin;
    private ConfigHandler handler;

    private DatabaseObject object;
    private Logger logger;
    private boolean enabled = false;

    public DatabaseService(SnowfieldPVP plugin, boolean autoenable){
        this.plugin = plugin;
        this.handler = plugin.getCustomConfig();
        this.logger = plugin.getLogger();
        if(autoenable){
            enable();
        }
    }

    public void enable(){
        switch (getSelected().toLowerCase()){
            case "sqlite":
                //Register SQLite Connector
                SQLiteConnector sqLiteConnector = new SQLiteConnector(plugin);
                sqLiteConnector.setupFolder();
                sqLiteConnector.setupFileorFiles();
                object = new DatabaseObject(sqLiteConnector);
                boolean sucs = sqLiteConnector.connectAll();
                if(!sucs){
                    //Register FileManager
                    FileManager fm = new FileManager(plugin);
                    object = new DatabaseObject(fm);
                    //Setup Files
                    fm.setupFolder();
                    fm.setupFileorFiles();
                    logger.info("The FileSystem was loaded, because the SQLite-Database is down!");
                }
                break;
            case "mysql":
                //Register MySQL Connector
                MySQLConnector connector = new MySQLConnector(plugin, getConfigMySQLString("Host"), getConfigMySQLString("Database"), getConfigMySQLString("User"), getConfigMySQLString("Password"), handler.getInteger("Databases.MySQL.Port").toString());
                object = new DatabaseObject(connector);
                boolean suc = connector.connect();
                if(!suc){
                    //Register FileManager
                    FileManager fm = new FileManager(plugin);
                    object = new DatabaseObject(fm);
                    //Setup Files
                    fm.setupFolder();
                    fm.setupFileorFiles();
                    logger.info("The FileSystem was loaded, because the MySQL-Database is down!");
                }
                break;
            default:
                //Register FileManager
                FileManager fm = new FileManager(plugin);
                object = new DatabaseObject(fm);
                //Setup Files
                fm.setupFolder();
                fm.setupFileorFiles();
                break;
        }
    }

    public void reset(){
        object = null;
    }

    public void disable() {
        if (object != null && object.get() != null) {
            switch (getSelected().toLowerCase()) {
                case "sqlite":
                    //Close SQLite Connections
                    SQLiteConnector con = (SQLiteConnector) object.get();
                    con.closeAll();
                    object = null;
                    break;
                case "mysql":
                    //Close MySQL Connections
                    MySQLConnector mySQLConnector = (MySQLConnector) object.get();
                    mySQLConnector.close();
                    object = null;
                    break;
                default:
                    //Close Files
                    FileManager files = (FileManager) object.get();
                    object = null;
                    break;
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setSelected(SnowDatabaseType type){
        this.handler.setString("Databases.Selected", type.name().toString());
        this.handler.save();
    }

    public SnowDatabaseType getDBType(){
        return object.getType();
    }

    private String getSelected(){
        return this.handler.getString("Databases.Selected");
    }

    private String getConfigMySQLString(String key){
        return handler.getString("Databases.MySQL."+key);
    }

    public DatabaseObject getObject() {
        return object;
    }
}
