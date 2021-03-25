package eu.iteije.clanwar.databases;

import com.google.common.base.Throwables;
import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.iteije.clanwar.ClanWar;
import eu.iteije.clanwar.databases.tables.ClansTable;
import eu.iteije.clanwar.databases.tables.PlayersTable;
import eu.iteije.clanwar.resources.PluginFile;
import org.bukkit.configuration.file.FileConfiguration;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class DatabaseModule {

    private final ClanWar instance;

    private HikariDataSource dataSource;
    private final ExecutorService service;

    private final PluginFile configFile;

    public DatabaseModule(ClanWar instance, PluginFile configFile) {
        this.instance = instance;
        this.service = Executors.newCachedThreadPool();
        this.configFile = configFile;

        this.start();
    }

    public synchronized void start() {
        if (this.configFile != null) {
            FileConfiguration config = configFile.getConfiguration();

            Object ip = config.get("database.ip");
            Object port = config.get("database.port");
            Object database = config.get("database.database");
            Object user = config.get("database.user");
            Object password = config.get("database.password");

            if (ip != null && port != null && database != null && user != null && password != null) {
                HikariConfig hikariConfig = new HikariConfig();

                hikariConfig.setLeakDetectionThreshold(10000);
                hikariConfig.setMaximumPoolSize(4);
                hikariConfig.setConnectionTimeout(2000);

                hikariConfig.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
                hikariConfig.addDataSourceProperty("serverName", ip);
                hikariConfig.addDataSourceProperty("port", port);
                hikariConfig.addDataSourceProperty("databaseName", database);
                hikariConfig.addDataSourceProperty("user", user);
                hikariConfig.addDataSourceProperty("password", password);

                this.dataSource = new HikariDataSource(hikariConfig);
            } else {
                instance.getLogger().severe("Database connection could not be established!");
                instance.getServer().getPluginManager().disablePlugin(instance);
            }

        } else {
            instance.getLogger().severe("Failed to load config file -> Could not fetch database information!");
            instance.getServer().getPluginManager().disablePlugin(instance);
        }
    }

    public void createTables() {
        PlayersTable playersTable = new PlayersTable(this.dataSource);
        playersTable.create();

        ClansTable clansTable = new ClansTable(this.dataSource);
        clansTable.create();
    }

    private CompletableFuture<CachedRowSet> query(String query, Object... vars) {
        return makeFuture(() -> {
            try (Connection connection = getConnection()) {
                try (PreparedStatement statement = prepareStatement(query, connection, vars)) {
                    ResultSet resultSet = statement.executeQuery();

                    CachedRowSet cachedRowSet = new CachedRowSetImpl();
                    cachedRowSet.populate(resultSet);

                    return cachedRowSet;
                }
            }
        });
    }

    public void fetch(Consumer<CachedRowSet> consumer, String query, Object... vars) {
        CompletableFuture<CachedRowSet> future = this.query(query, vars);

        future.thenAccept(cachedRowSet -> {
            instance.getServer().getScheduler().runTask(instance, () -> consumer.accept(cachedRowSet));
        });
    }

    public PreparedStatement prepareStatement(String query, Connection connection, Object... vars) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            if (query.contains("?") && vars.length != 0) {
                int index = 0;

                for (Object var : vars) {
                    index++;

                    statement.setObject(index, var);
                }
            }

            return statement;
        } catch (SQLException exception) {
            exception.printStackTrace();
            instance.getLogger().warning("Couldn't prepare statement.");
        }

        return null;
    }

    private CompletableFuture<CachedRowSet> makeFuture(Callable<CachedRowSet> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.call();
            } catch (Exception exception) {
                exception.printStackTrace();

                Throwables.throwIfUnchecked(exception);
                throw new CompletionException(exception);
            }
        }, service);
    }

    public void execute(String query, Object... vars) {
        this.execute((cachedRowSet) -> {}, query, vars);
    }

    public void execute(Consumer<CachedRowSet> onComplete, String query, Object... vars) {
        makeFuture(() -> {
            try (Connection connection = getConnection()) {
                try (PreparedStatement statement = prepareStatement(query, connection, vars)) {
                    statement.execute();
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }
            }

            return null;
        }).thenAccept(onComplete);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
            instance.getLogger().warning("Couldn't get connection.");
        }
        return null;
    }

    public void close() {
        if (this.dataSource != null) {
            this.dataSource.close();
        }
    }

}
