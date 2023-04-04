package pl.javastart.bookshop.MySql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MySql {
    private final String host;
    private final int port;
    private final String dbName;
    private final String user;
    private final String password;
    private final Executor executor;
    private final int pool;
    @Getter
    private HikariDataSource hikari;

    public MySql(String host, int port, String dbName, String user, String password, int pool, int threadpool) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.user = user;
        this.password = password;
        this.pool = pool;
        this.executor = Executors.newScheduledThreadPool(threadpool);
        this.connect();
    }

    private void connect() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.dbName);
        hikariConfig.setUsername(this.user);
        hikariConfig.setPassword(this.password);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.setMaximumPoolSize(this.pool);
        this.hikari = new HikariDataSource(hikariConfig);
        try {
            if (this.getHikari().getConnection() != null) {
                Logger.warning("Połączono z mysql!");
            }
        } catch (SQLException e) {
            Logger.warning("Nie udało połaczyć się z mysql!");
        }
    }

    public void update(String u) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.getHikari().getConnection();
            statement = connection.prepareStatement(u);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(connection);
            close(statement);
        }
    }

    public void executeQuery(String query) throws SQLException {
        Connection connection = getHikari().getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(Connection connection) {
        if (connection == null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Błąd podczas zamykania połączenia z bazą danych!");
            }
        }
    }

    public void close(Statement statement) {
        if (statement == null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("Błąd podczas zamykania statementa!");
            }
        }
    }

    public void updateAsync(String u) {
        this.executor.execute(() -> MySql.this.update(u));
    }

    public void CREATETABLE(String table, String values) {
        updateAsync("CREATE TABLE IF NOT EXISTS " + table + " (id int(11) AUTO_INCREMENT PRIMARY KEY, " + values + ");");
    }
}