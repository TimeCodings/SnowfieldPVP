package dev.timecoding.snowfieldpvp.database.mysql;

import dev.timecoding.snowfieldpvp.SnowfieldPVP;

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
        if(b){
            //Create Tables (MySQL)
            update("CREATE TABLE IF NOT EXISTS `test` (\r\n"
                    + "	`VALUE` VARCHAR(100),\r\n"
                    + "	`VALUE2` LONGTEXT,\r\n"
                    + "	`VALUE3` LONGTEXT,\r\n"
                    + "	`VALUE4` LONGTEXT,\r\n"
                    + "	`VALUE5` VARCHAR(100),\r\n"
                    + "	`VALUE6` VARCHAR(100),\r\n"
                    + "	`VALUE7` VARCHAR(100),\r\n"
                    + "	`VALUE8` VARCHAR(200)\r\n"
                    + ");");
        }
    }

    public boolean connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin", USER, PASSWORD);
            logger.info("Successfully connected to your MySQL-Database!");
            return true;
        }catch (SQLException e) {
            logger.warning("Error while connecting to your MySQL-Database:");
            e.printStackTrace();
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
            connect();
            logger.warning("Error while updating your MySQL-Database:");
        }
        return false;
    }

    public ResultSet query(String qry) {
        ResultSet rs = null;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(qry);
        }catch (SQLException e) {
            connect();
            logger.warning("Error while requesting your MySQL-Database:");
        }
        return rs;
    }


}
