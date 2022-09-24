package dev.timecoding.snowfieldpvp.database;

import dev.timecoding.snowfieldpvp.SnowfieldPVP;
import dev.timecoding.snowfieldpvp.config.ConfigHandler;
import dev.timecoding.snowfieldpvp.database.file.FileManager;
import dev.timecoding.snowfieldpvp.database.mysql.MySQLConnector;
import dev.timecoding.snowfieldpvp.database.sqlite.SQLiteConnector;
import dev.timecoding.snowfieldpvp.enums.SnowDatabaseType;
import dev.timecoding.snowfieldpvp.enums.SnowFiles;
import dev.timecoding.snowfieldpvp.enums.SnowLanguage;
import dev.timecoding.snowfieldpvp.object.DatabaseObject;

public class DatabaseService {

    private SnowfieldPVP plugin;
    private ConfigHandler handler;

    private DatabaseObject object;
    private boolean enabled = false;

    public DatabaseService(SnowfieldPVP plugin, boolean autoenable){
        this.plugin = plugin;
        this.handler = plugin.getCustomConfig();
        if(autoenable){
            enable();
        }
    }

    public void enable(){
        switch (getSelected().toLowerCase()){
            case "sqlite":
                //Register SQLite Connector
                SQLiteConnector sqLiteConnector = new SQLiteConnector(plugin);
                sqLiteConnector.setupFileorFiles();
                boolean sucs = sqLiteConnector.connectAll();
                break;
            case "mysql":
                //Register MySQL Connector
                MySQLConnector connector = new MySQLConnector(plugin, getConfigMySQLString("Host"), getConfigMySQLString("Database"), getConfigMySQLString("User"), getConfigMySQLString("Password"), handler.getInteger("Databases.MySQL.Port").toString());
                object = new DatabaseObject(connector);
                boolean suc = connector.connect();
                break;
            default:
                //Register FileManager
                FileManager fm = new FileManager(plugin);
                object = new DatabaseObject(fm);
                //Setup Files
                fm.setupFileorFiles();
                break;
        }
    }

    public void disable(){
        switch (getSelected().toLowerCase()){
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

    public boolean isEnabled() {
        return enabled;
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
