package utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBContext {

    private static final String hostname = "localhost";
    private static final int serverPort = 1433;
    private static final String dbName = "e-exam";
    private static final String username = "sa";
    private static final String password = "Abc@12345678";
    private static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String connectionStr = "jdbc:sqlserver://" + hostname + ":" + serverPort + ";databaseName=" + dbName;

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl(connectionStr);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
