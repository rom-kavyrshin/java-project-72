package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class UrlCheckRepository extends BaseRepository {

    private UrlCheckRepository() {
    }

    public static boolean save(UrlCheck urlCheck) throws SQLException {
        String sql = """
                INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at)
                VALUES (?, ?, ?, ?, ?, ?)""";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, urlCheck.getUrlId());
            preparedStatement.setInt(2, urlCheck.getStatusCode());
            preparedStatement.setString(3, urlCheck.getH1());
            preparedStatement.setString(4, urlCheck.getTitle());
            preparedStatement.setString(5, urlCheck.getDescription());
            preparedStatement.setTimestamp(6, urlCheck.getCreatedAt());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }

            return true;
        }
    }

    public static List<UrlCheck> getAllWhereUrlId(long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");

                var status = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");

                var urlCheck = new UrlCheck(status, title, h1, description, urlId, createdAt);
                urlCheck.setId(id);
                result.add(urlCheck);
            }
            return result;
        }
    }

    public static void removeAll() throws SQLException {
        var sql = "delete from url_checks";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }
    }
}
