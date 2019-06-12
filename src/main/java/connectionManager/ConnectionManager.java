package connectionManager;

import org.jsoup.select.Evaluator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private Connection connection;
    private String url;
    private String user;
    private String password;

    public ConnectionManager(String url, String user, String password) {
        this.url=url;
        this.user=user;
        this.password=password;
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection=DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;

    }

    public void closeConnection() throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }


    }
}
