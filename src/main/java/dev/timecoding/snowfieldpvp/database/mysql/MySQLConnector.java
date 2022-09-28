package dev.timecoding.snowfieldpvp.database.mysql;

import dev.timecoding.snowfieldpvp.SnowfieldPVP;
import dev.timecoding.snowfieldpvp.database.DatabaseService;
import dev.timecoding.snowfieldpvp.enums.SnowDatabaseType;
import dev.timecoding.snowfieldpvp.enums.SnowFiles;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.logging.Logger;

public class MySQLConnector {

    private String HOST = "";
    private String DATABASE = "";
    private String USER = "";
    private String PASSWORD = "";
    private String PORT = "";

    private SnowfieldPVP plugin;
    private Logger logger;

    private Connection connection;

    public MySQLConnector(SnowfieldPVP plugin, String host, String database, String user, String password, String port) {
        this.HOST = host;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;
        this.PORT = port;

        this.plugin = plugin;
        this.logger = plugin.getLogger();
        boolean b = connect();
    }

    public boolean connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin", USER, PASSWORD);
            logger.info("Successfully connected to your MySQL-Database!");
            return true;
        }catch (SQLException e) {
            DatabaseService service = plugin.getDatabaseService();
            service.reset();
            service.setSelected(SnowDatabaseType.FILES);
            logger.info("I changed the Database-Type to Files, because I couldnt connect to MySQL!");
            service.enable();
        }
        return false;
    }

    public boolean close() {
        try {
            connection.close();
            logger.info("Successfully closed the connection to your MySQL-Database!");
            return true;
        }catch (SQLException e) {
            logger.warning("Error while closing connection to your MySQL-Database:");
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(String qry) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(qry);
            statement.close();
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            if(!connect()){
                DatabaseService service = plugin.getDatabaseService();
                service.reset();
                service.setSelected(SnowDatabaseType.FILES);
                logger.info("I changed the Database-Type to Files, because I couldnt connect to MySQL!");
                service.enable();
            }else{
                logger.warning("Error while updating your MySQL-Database, but you got reconnected!");
            }
        }
        return false;
    }

    public ResultSet query(String qry) {
        ResultSet rs = null;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(qry);
        }catch (SQLException e) {
            if(!connect()){
                DatabaseService service = plugin.getDatabaseService();
                service.reset();
                service.setSelected(SnowDatabaseType.FILES);
                logger.info("I changed the Database-Type to Files, because I couldnt connect to MySQL! (If such problems persist and you don't know what to do, join my Discord: discord.timecoding.de)");
                service.enable();
            }else{
                logger.warning("Error while querying your MySQL-Database, but you got reconnected!");
            }
        }
        return rs;
    }


}
