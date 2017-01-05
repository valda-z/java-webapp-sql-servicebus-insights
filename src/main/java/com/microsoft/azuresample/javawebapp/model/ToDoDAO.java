package com.microsoft.azuresample.javawebapp.model;

import com.microsoft.azuresample.javawebapp.Utils.SqlServerHelper;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vazvadsk on 2016-12-05.
 */
@Component
public class ToDoDAO {

    @PostConstruct
    public void init(){
        System.out.println("### INIT of ToDoDAO called.");

        try {
            SQLServerConnection conn = SqlServerHelper.GetConnection();
            try (PreparedStatement stmt = conn.prepareStatement(
                    "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ToDo' and xtype='U')\n" +
                            "CREATE TABLE [dbo].[ToDo](\n" +
                            " [Id] [int] IDENTITY(1,1) NOT NULL,\n" +
                            " [GId] [uniqueidentifier] NOT NULL DEFAULT NEWID(),\n" +
                            " [Category] [nvarchar](50) NULL,\n" +
                            " [Note] [nvarchar](500) NULL,\n" +
                            " [Created] [datetime] NOT NULL DEFAULT GETDATE(),\n" +
                            " CONSTRAINT [PK_dbo.ToDo] PRIMARY KEY CLUSTERED \n" +
                            "(\n" +
                            " [Id] ASC\n" +
                            ") )"))
            {
                stmt.executeUpdate();
            }finally {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("ERROR: cannot connect to SQL Server.");
            e.printStackTrace();
        }
    }

    public List<ToDo> query(){
        List<ToDo> ret = new ArrayList<ToDo>();
        try {
            SQLServerConnection conn = SqlServerHelper.GetConnection();
            try (PreparedStatement selectStatement = conn.prepareStatement(
                    "SELECT Id, Note, Category, GId FROM ToDo"))
            {
                ResultSet rs = selectStatement.executeQuery();
                while(rs.next()) {
                    ret.add(new ToDo(
                            rs.getInt("Id"),
                            rs.getString("Note"),
                            rs.getString("Category"),
                            rs.getString("GId")
                    ));
                }
                rs.close();
            }finally {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("ERROR: cannot connect to SQL Server.");
            e.printStackTrace();
        }
        return ret;
    }

    public ToDo create(ToDo item){

        try {
            SQLServerConnection conn = SqlServerHelper.GetConnection();
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO ToDo(Note, Category) VALUES(?,?)",
                    Statement.RETURN_GENERATED_KEYS))
            {
                stmt.setString(1, item.getNote());
                stmt.setString(2, item.getCategory());
                System.out.println("INSERT: before insert call.");
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                int primaryKey = rs.getInt(1);
                rs.close();

                System.out.println("INSERT: geting details.");
                try {
                    try (PreparedStatement selectStatement = conn.prepareStatement(
                            "SELECT Id, Note, Category, GId FROM ToDo WHERE Id=" + primaryKey))
                    {
                        ResultSet rs2 = selectStatement.executeQuery();
                        while(rs2.next()) {
                            item.setId(rs2.getInt("Id"));
                            item.setGid(rs2.getString("GId"));
                            item.setNote(rs2.getString("Note"));
                            item.setCategory(rs2.getString("Category"));

                            System.out.println("Inserted record: Id:" + item.getId() + ", GId:"
                                    + item.getGid() + ", Note:"
                                    + item.getNote() + ", Category:"
                                    + item.getCategory()
                            );
                        }
                        rs2.close();
                    }finally {
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.out.println("ERROR: cannot connect to SQL Server.");
                    e.printStackTrace();
                }

            }finally {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("ERROR: cannot connect to SQL Server.");
            e.printStackTrace();
        }

        return item;
    }

    public String test(){
        String ret = "";
        try {
            SQLServerConnection conn = SqlServerHelper.GetConnection();
            try (PreparedStatement selectStatement = conn.prepareStatement(
                    "SELECT Id, Note, Category FROM ToDo"))
            {
                ResultSet rs = selectStatement.executeQuery();
                while(rs.next()) {
                    ret += rs.getString("Note") + ", " + rs.getString("Category")+", ";
                }
                rs.close();
            }finally {
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("ERROR: cannot connect to SQL Server.");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            ret = sw.toString();
        }
        return ret;
    }
}
