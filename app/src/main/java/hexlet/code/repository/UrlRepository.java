package hexlet.code.repository;

import hexlet.code.dto.urls.UrlDTO;
import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public static boolean save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, url.getCreatedAt());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }

            return true;
        }
    }

    public static Optional<Url> find(long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlModel = new Url(name, createdAt);
                urlModel.setId(id);
                return Optional.of(urlModel);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findByUrl(String urlString) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, urlString);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlModel = new Url(name, createdAt);
                urlModel.setId(id);
                return Optional.of(urlModel);
            }
            return Optional.empty();
        }
    }

    public static List<Url> getAll() throws SQLException {
        var sql = "SELECT * FROM urls";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<Url>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlModel = new Url(name, createdAt);
                urlModel.setId(id);
                result.add(urlModel);
            }
            return result;
        }
    }

    public static List<UrlDTO> getAllWithLastCheck() throws SQLException {
        var sql = """
            SELECT
                u.id,
                u.name,
                uc.status_code AS last_check_status,
                uc.created_at AS last_check_time
            FROM urls u
            LEFT JOIN url_checks uc ON u.id = uc.url_id
            LEFT JOIN url_checks uc2 ON u.id = uc2.url_id
                AND (uc.created_at < uc2.created_at OR uc.created_at = uc2.created_at AND uc.id < uc2.id)
            WHERE uc2.url_id IS NULL;
            """;
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlDTO>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var lastCheckStatus = resultSet.getInt("last_check_status");
                var lastCheckTime = resultSet.getTimestamp("last_check_time");
                var urlModel = new UrlDTO(id, name, lastCheckStatus, lastCheckTime);
                result.add(urlModel);
            }
            return result;
        }
    }

    public static void removeAll() throws SQLException {
        var sql = "delete from urls";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }
    }
}
