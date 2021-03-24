package eu.iteije.clanwar.databases.tables;

import com.zaxxer.hikari.HikariDataSource;
import eu.iteije.clanwar.databases.tables.interfaces.DatabaseTableImplementation;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@AllArgsConstructor
public class ClansTable implements DatabaseTableImplementation {

    private final HikariDataSource dataSource;

    @Override
    public void create() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS clans(" +
                    "id int PRIMARY NOT NULL AUTO_INCREMENT, " +
                    "name varchar(64) NOT NULL)"
            );
            statement.execute();

            statement.closeOnCompletion();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
