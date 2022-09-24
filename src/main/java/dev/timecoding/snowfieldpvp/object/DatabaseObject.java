package dev.timecoding.snowfieldpvp.object;

import dev.timecoding.snowfieldpvp.database.file.FileManager;
import dev.timecoding.snowfieldpvp.database.mysql.MySQLConnector;
import dev.timecoding.snowfieldpvp.database.sqlite.SQLiteConnector;
import dev.timecoding.snowfieldpvp.enums.SnowDatabaseType;

public class DatabaseObject {

    private SQLiteConnector c1 = null;
    private MySQLConnector c2 = null;
    private FileManager c3 = null;

    public DatabaseObject(SQLiteConnector sqLiteConnector){
        this.c1 = sqLiteConnector;
    }

    public DatabaseObject(MySQLConnector mySQLConnector){
        this.c2 = mySQLConnector;
    }

    public DatabaseObject(FileManager fileManager){
        this.c3 = fileManager;
    }

    public SnowDatabaseType getType(){
        if(c1 != null){
            return SnowDatabaseType.SQLITE;
        }else if(c2 != null){
            return SnowDatabaseType.MYSQL;
        }else if(c3 != null){
            return SnowDatabaseType.FILES;
        }
        return SnowDatabaseType.FILES;
    }

    public Object get(){
        if(c1 != null){
            return c1;
        }else if(c2 != null){
            return c2;
        }else if(c3 != null){
            return c3;
        }
        return null;
    }

}
