package dev.timecoding.snowfieldpvp.database;

import dev.timecoding.snowfieldpvp.SnowfieldPVP;
import dev.timecoding.snowfieldpvp.database.file.FileManager;
import dev.timecoding.snowfieldpvp.database.mysql.MySQLConnector;
import dev.timecoding.snowfieldpvp.database.sqlite.SQLiteConnector;
import dev.timecoding.snowfieldpvp.enums.SnowDatabaseType;
import dev.timecoding.snowfieldpvp.enums.SnowFiles;
import dev.timecoding.snowfieldpvp.object.DatabaseObject;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataHandler {

    private SnowfieldPVP plugin;
    private DatabaseService service;
    private Logger logger;

    public DataHandler(SnowfieldPVP plugin){
        this.plugin = plugin;
        this.service = plugin.getDatabaseService();
        this.logger = plugin.getLogger();
        setupTables();
    }

    //TODO CREATE FUNCTION

    public boolean set(@NotNull SnowFiles type, @NotNull String key, String dbkey, @NotNull Object value, String dbwhere, String dbwhereanswer){
        SnowDatabaseType dbtype = service.getDBType();
        DatabaseObject obj = service.getObject();
        addTableColumn(type, dbkey.toUpperCase());
        switch(dbtype){
            case FILES:
                //Cast to FileManager
                FileManager fm = (FileManager) obj.get();
                //Set and safe Files
                fm.selectFileType(type).toYAMLDatas().set(key, value);
                fm.selectFileType(type).saveToYAML();
                return true;
            case MYSQL:
                //Cast to MYSQL
                MySQLConnector mysql = (MySQLConnector) obj.get();
                //Update
                boolean success = mysql.update("UPDATE "+type.name().toUpperCase()+" SET "+dbkey.toUpperCase()+"= '" + value + "' WHERE "+dbwhere+"= '" + dbwhereanswer + "';");
                return success;
            case SQLITE:
                //Cast to SQLITE
                SQLiteConnector sqlite = (SQLiteConnector) obj.get();
                //Update
                boolean suc = sqlite.update(type,"UPDATE "+type.name().toUpperCase()+" SET "+dbkey.toUpperCase()+"= '" + value + "' WHERE "+dbwhere+"= '" + dbwhereanswer + "';");
                return suc;
        }
        return false;
    }

    public Object get(@NotNull SnowFiles type, @NotNull String key, String dbkey, String dbwhere, String dbwhereanswer){
        SnowDatabaseType dbtype = service.getDBType();
        DatabaseObject obj = service.getObject();
        addTableColumn(type, dbkey.toUpperCase());
        switch(dbtype){
            case FILES:
                //Cast to FileManager
                FileManager fm = (FileManager) obj.get();
                //Get files
                Object o = fm.selectFileType(type).toYAMLDatas().get(key);
                return o;
            case MYSQL:
                //Cast to MYSQL
                MySQLConnector mysql = (MySQLConnector) obj.get();
                //Query + Get datas
                Object ob = null;
                ResultSet rs = mysql.query("SELECT * FROM "+type.name().toUpperCase()+" WHERE "+dbwhere+"= '" + dbwhereanswer + "';");
                try {
                    if(!(rs.next()) || (rs.getString(dbkey)) == null);
                    ob = rs.getObject(dbkey);
                } catch (SQLException e) {
                    if(!mysql.connect()){
                        DatabaseService service = plugin.getDatabaseService();
                        service.reset();
                        service.setSelected(SnowDatabaseType.FILES);
                        logger.info("I changed the Database-Type to Files, because I couldnt connect to MySQL!");
                        service.enable();
                    }else{
                        logger.warning("Error while updating your MySQL-Database, but you got reconnected!");
                    }
                }finally {
                    if(rs != null){
                        try {
                            rs.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return ob;
            case SQLITE:
                //Cast to SQLITE
                SQLiteConnector sqlite = (SQLiteConnector) obj.get();
                //Query + Get datas
                Object obje = null;
                ResultSet rssqlite = sqlite.query(type, "SELECT * FROM "+type.name().toUpperCase()+" WHERE "+dbwhere+"= '" + dbwhereanswer + "';");
                try {
                    if(!(rssqlite.next()) || (rssqlite.getString(dbkey)) == null);
                    obje = rssqlite.getObject(dbkey);
                } catch (SQLException e) {
                    if(!sqlite.connect(type)){
                        DatabaseService service = plugin.getDatabaseService();
                        service.reset();
                        service.setSelected(SnowDatabaseType.FILES);
                        logger.info("I changed the Database-Type to Files, because I couldnt connect to SQLite!");
                        service.enable();
                    }else{
                        logger.warning("Error while updating your SQLite-Database, but you got reconnected!");
                    }
                }finally {
                    if(rssqlite != null){
                        try {
                            rssqlite.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return obje;
        }
        return null;
    }

    public boolean create(@NotNull SnowFiles type, String dbwhere, String dbwhereanswer){
        SnowDatabaseType dbtype = service.getDBType();
        DatabaseObject obj = service.getObject();
        addTableColumn(type, dbwhere);
        switch(dbtype){
            case MYSQL:
                //Cast to MYSQL
                MySQLConnector mysql = (MySQLConnector) obj.get();
                //Update
                boolean success = mysql.update("INSERT INTO "+type.name().toUpperCase()+" ("+dbwhere+") VALUES ('"+dbwhereanswer+"');");
                return success;
            case SQLITE:
                //Cast to SQLITE
                SQLiteConnector sqlite = (SQLiteConnector) obj.get();
                //Update
                boolean suc = sqlite.update(type,"INSERT INTO "+type.name().toUpperCase()+" ("+dbwhere+") VALUES ('"+dbwhereanswer+"');");
                return suc;
        }
        return false;
    }

    public boolean setupTables(){
        SnowDatabaseType dbtype = service.getDBType();
        DatabaseObject obj = service.getObject();
        switch (dbtype){
            case SQLITE:
                //Cast to SQLITE
                SQLiteConnector sqlite = (SQLiteConnector) obj.get();
                //Execute update statement
                boolean suc = false;
                for(SnowFiles types : SnowFiles.values()){
                    boolean suc2 = sqlite.update(types,"CREATE TABLE IF NOT EXISTS "+types.name().toUpperCase()+" ('UUID' varchar(200))");
                    if(!suc && suc2){
                        suc = true;
                    }
                }
                return suc;
            case MYSQL:
                //Cast to MYSQL
                MySQLConnector mysql = (MySQLConnector) obj.get();
                //Execute update statement
                boolean suc3 = false;
                for(SnowFiles types : SnowFiles.values()){
                    boolean suc4 = mysql.update("CREATE TABLE IF NOT EXISTS " + types.name().toUpperCase() + " (UUID varchar(200));");
                    if(!suc3 && suc4){
                        suc = true;
                    }
                }
                return suc3;
        }
        return false;
    }

    private boolean columnExistsSQLite(SnowFiles type, SQLiteConnector connector, String name){
        ResultSet rs = connector.query(type, "PRAGMA table_info("+type.name().toUpperCase()+");");
        try {
            while(rs.next()){
                if(rs.getString("name") != null){
                    if(rs.getString("name").equalsIgnoreCase(name)){
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while getting Column-Status:", e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    private boolean columnExistsMySQL(SnowFiles type, MySQLConnector connector, String name){
        ResultSet rs = connector.query("PRAGMA table_info("+type.name().toUpperCase()+");");
        try {
            while(rs.next()){
                if(rs.getString("name") != null){
                    if(rs.getString("name").equalsIgnoreCase(name)){
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while getting Column-Status:", e);
        }finally {
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    public boolean addTableColumn(@NotNull SnowFiles type, String name){
        SnowDatabaseType dbtype = service.getDBType();
        DatabaseObject obj = service.getObject();
        switch (dbtype){
            case SQLITE:
                //Cast to SQLITE
                SQLiteConnector sqlite = (SQLiteConnector) obj.get();
                //Execute update statement
                boolean b = true;
                if(!columnExistsSQLite(type, sqlite, name.toUpperCase())){
                    b = sqlite.update(type,"ALTER TABLE "+type.name().toUpperCase()+" ADD COLUMN "+name.toUpperCase()+" varchar(500);");
                }
                return b;
            case MYSQL:
                //Cast to MYSQL
                MySQLConnector mysql = (MySQLConnector) obj.get();
                //Execute update statement
                boolean bm = mysql.update("ALTER TABLE "+type.name().toUpperCase()+" ADD COLUMN IF NOT EXISTS "+name.toUpperCase()+" varchar(500);");
                return bm;
        }
        return false;
    }

}
