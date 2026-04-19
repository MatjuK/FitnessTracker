package pl.wsb.fitnesstracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class verifies the existence of specific tables and their columns
 * in the database schema using JPA and an in-memory database for testing.
 * Class should be under test/java/pl/wsb/fitnesstracker sources
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class DatabaseSchemaTest {

    @Autowired
    private DataSource dataSource;

    // Testy istnienia tabel

    @Test
    void shouldHaveUsersTable() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            assertThat(tableExists(conn, "users")).isTrue();
        }
    }

    @Test
    void shouldHaveTrainingsTable() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            assertThat(tableExists(conn, "trainings")).isTrue();
        }
    }

    @Test
    void shouldHaveStatisticsTable() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            assertThat(tableExists(conn, "statistics")).isTrue();
        }
    }

    @Test
    void shouldHaveHealthMetricsTable() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            assertThat(tableExists(conn, "health_metrics")).isTrue();
        }
    }

    // Testy kolumn: Users

    @Test
    void usersTableHasExpectedColumns() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            Set<String> cols = tableColumns(conn, "users");
            // Kolumny zgodne ze schematem: id, firstName, lastName, birthday, email
            assertThat(cols).contains("id", "email", "first_name", "last_name", "birthdate");
        }
    }

    // Testy kolumn: Trainings

    @Test
    void trainingsTableHasExpectedColumns() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            Set<String> cols = tableColumns(conn, "trainings");
            // Relacja ManyToOne do Users przez user_id
            // Pozostałe kolumny zgodnie ze schematem
            assertThat(cols).contains(
                    "id",
                    "user_id",
                    "start_time",
                    "end_time",
                    "activity_type",
                    "distance",
                    "average_speed"
            );
        }
    }


    // Testy kolumn: Statistics

    @Test
    void statisticsTableHasExpectedColumns() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            Set<String> cols = tableColumns(conn, "statistics");
            // Relacja OneToOne do Users przez user_id
            // totalTrainings, totalDistance, totalCaloriesBurned zgodnie ze schematem
            assertThat(cols).contains("id", "user_id");
        }
    }

    @Test
    void statisticsTableHasTotalColumns() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            Set<String> cols = tableColumns(conn, "statistics");
            // Sprawdzamy że kolumny ze statystykami istnieją
            assertThat(cols.stream().anyMatch(c -> c.toLowerCase().contains("totaltraining")
                    || c.toLowerCase().contains("total_training"))).isTrue();
            assertThat(cols.stream().anyMatch(c -> c.toLowerCase().contains("totaldistance")
                    || c.toLowerCase().contains("total_distance"))).isTrue();
            assertThat(cols.stream().anyMatch(c -> c.toLowerCase().contains("totalcalories")
                    || c.toLowerCase().contains("total_calories"))).isTrue();
        }
    }


    // Testy kolumn: Health_Metrics

    @Test
    void healthMetricsTableHasExpectedColumns() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            Set<String> cols = tableColumns(conn, "health_metrics");
            // Relacja ManyToOne do Users przez user_id
            // Pozostałe kolumny zgodnie ze schematem
            assertThat(cols).contains("id", "user_id");
        }
    }

    @Test
    void healthMetricsTableHasMeasurementColumns() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            Set<String> cols = tableColumns(conn, "health_metrics");
            assertThat(cols).contains("date", "weight", "height");
            // heartRate
            assertThat(cols.stream().anyMatch(c -> c.toLowerCase().contains("heartrate")
                    || c.toLowerCase().contains("heart_rate"))).isTrue();
        }
    }


    // Metody pomocnicze
    private boolean tableExists(Connection conn, String expectedName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                String schema = rs.getString("TABLE_SCHEM");
                if (schema == null) continue;
                if (!"PUBLIC".equalsIgnoreCase(schema)) continue;
                String name = rs.getString("TABLE_NAME");
                if (expectedName.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Set<String> tableColumns(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        Set<String> cols = new HashSet<>();
        try (ResultSet rs = meta.getColumns(conn.getCatalog(), null, "%", "%")) {
            while (rs.next()) {
                String schema = rs.getString("TABLE_SCHEM");
                if (schema == null) continue;
                if (!"PUBLIC".equalsIgnoreCase(schema)) continue;
                String tbl = rs.getString("TABLE_NAME");
                if (!tableName.equalsIgnoreCase(tbl)) continue;
                String col = rs.getString("COLUMN_NAME");
                if (col != null) {
                    cols.add(col.toLowerCase());
                }
            }
        }
        return cols;
    }
}