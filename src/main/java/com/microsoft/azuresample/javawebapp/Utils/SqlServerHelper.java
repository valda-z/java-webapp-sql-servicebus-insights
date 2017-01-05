package com.microsoft.azuresample.javawebapp.Utils;

import com.microsoft.sqlserver.jdbc.*;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

@Component
public class SqlServerHelper {

    public static String sqlurl;

    public static SQLServerConnection GetConnection() throws SQLException {
        if(sqlurl==null){
            new SqlServerHelper().Init();
        }

        SQLServerConnection connection = (SQLServerConnection) DriverManager.getConnection(sqlurl);
        return connection;
    }

    public void Init(){
        Map<String, String> env = System.getenv();
        sqlurl = env.get("SQLSERVER_URL");

        System.out.println("### INIT of SqlServerHelper called.");
    }
}
