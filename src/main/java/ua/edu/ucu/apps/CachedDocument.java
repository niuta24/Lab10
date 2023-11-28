package ua.edu.ucu.apps;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CachedDocument extends SmartDocument {
    private static final String DB_URL = "jdbc:sqlite:path_to_your_database.db";

    public CachedDocument(String gcsPath) {
        super(gcsPath);
    }

    @Override
    public String parse() {
        try (Connection CONN = DriverManager.getConnection(DB_URL)) {
            String cachedText = getCachedText(CONN);
            if (cachedText != null) {
                return cachedText;
            } else {
                String parsedText = super.parse();
                cacheText(CONN, parsedText);
                return parsedText;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return super.parse();
        }
    }

    private String getCachedText(Connection conn) throws SQLException {
        String sql = "SELECT content FROM cache WHERE gcsPath = ?";
        try (PreparedStatement PSTMT = conn.prepareStatement(sql)) {
            PSTMT.setString(1, this.getGcsPath());
            ResultSet rs = PSTMT.executeQuery();
            if (rs.next()) {
                return rs.getString("content");
            }
        }
        return null;
    }

    private void cacheText(Connection conn, String text) throws SQLException {
        String sql = "INSERT INTO cache (gcsPath, content) VALUES (?, ?)";
        try (PreparedStatement PSTMT = conn.prepareStatement(sql)) {
            PSTMT.setString(1, this.getGcsPath());
            PSTMT.setString(2, text);
            PSTMT.executeUpdate();
        }
    }
}
