package com.hotelreservation.template.support;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import org.flywaydb.core.Flyway;

public final class MigrationTestDatabase {

  private static final String USERNAME = "sa";
  private static final String PASSWORD = "";

  private final String url = "jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1";

  public void migrateTo(String version) {
    Flyway.configure().dataSource(url, USERNAME, PASSWORD).target(version).load().migrate();
  }

  public void migrateToLatest() {
    Flyway.configure().dataSource(url, USERNAME, PASSWORD).load().migrate();
  }

  public void execute(String sql) throws SQLException {
    try (Connection connection = connection();
        Statement statement = connection.createStatement()) {
      statement.executeUpdate(sql);
    }
  }

  public BigDecimal queryBigDecimal(String sql, String column) throws SQLException {
    try (Connection connection = connection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql)) {
      if (!result.next()) {
        throw new IllegalStateException("Query returned no rows: " + sql);
      }
      return result.getBigDecimal(column);
    }
  }

  private Connection connection() throws SQLException {
    return DriverManager.getConnection(url, USERNAME, PASSWORD);
  }
}
