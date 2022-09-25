package org.glagan.Core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    protected Connection connection;

    public Database() {
        this.connection = null;
    }

    public void connect(String host, int port, String user, String password, String name) {
        // Validate for null and empty values
        if (host == null || host.equals("")) {
            System.err.println("\u001B[31mInvalid database hostname\u001B[0m");
            return;
        }
        if (port < 0 || port > 65535) {
            System.err.println("\u001B[31mInvalid database port\u001B[0m");
            return;
        }
        if (user == null || user.equals("")) {
            System.err.println("\u001B[31mInvalid database user\u001B[0m");
            return;
        }
        if (password == null || password.equals("")) {
            System.err.println("\u001B[31mInvalid database password\u001B[0m");
            return;
        }
        if (name == null || name.equals("")) {
            System.err.println("\u001B[31mInvalid database name\u001B[0m");
            return;
        }

        // Connect and ignore errors
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + name;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("\u001B[36mConnected to database \u001B[0m" + host + ":" + port + "@" + name);
        } catch (SQLException e) {
            System.err.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            System.err.println("\u001B[31mSaves will not be saved in the database\u001B[0m");
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("\u001B[31mFailed to close database\u001B[0m");
            }
            connection = null;
        }
    }
}