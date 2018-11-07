package util;

import javafx.util.Pair;

import java.sql.*;

public class SQLiteJDBC {

    public static void addUser(String username, String password, String salt) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:printauthentication.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "INSERT INTO users (username, password, salt) " +
                    "VALUES ('" + username + "','" + password + "','" + salt + "');";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    public static String getPassword(String username) {
        Connection c = null;
        Statement stmt = null;
        String password = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:printauthentication.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "SELECT password " +
                    "WHERE username='" + username + "';";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                password = rs.getString("password");
            }

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
        return password;
    }

    public static String getSalt(String username) {
        Connection c = null;
        Statement stmt = null;
        String salt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:printauthentication.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "SELECT salt " +
                    "WHERE username='" + username + "';";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                salt = rs.getString("password");
            }

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
        return salt;
    }
}