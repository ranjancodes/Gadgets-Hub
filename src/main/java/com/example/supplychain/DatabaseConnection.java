package com.example.supplychain;
import java.sql.*;

public class DatabaseConnection {
    private static final String databaseURL = "jdbc:mysql://localhost:3306/supply_chain";
    private static final String userName = "root";
    private static final String password = "NdSk@123a11";

    public Statement getStatement(){
        Statement statement = null;
        Connection conn;
        try {
            conn = DriverManager.getConnection(databaseURL, userName, password);
            statement = conn.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }
        return statement;
    }
    public ResultSet getQueryTable(String query){
        Statement statement = getStatement();
        try {
            return statement.executeQuery(query);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int executeUpdateQuery(String query){
        Statement statement = getStatement();
        try {
            return statement.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

//    public static void main(String[] args) throws Exception {
//        DatabaseConnection databaseConnection = new DatabaseConnection();
//        ResultSet rs = databaseConnection.getQueryTable("SELECT * FROM CUSTOMER");
//        try {
//            while(rs.next()) {
//                System.out.println(rs.getString("password")+" "+rs.getString("first_name"));
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
