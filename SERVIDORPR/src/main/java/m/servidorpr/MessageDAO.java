package m.servidorpr;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public static void saveMessage(String origen, String contenido) {
        String sql = "INSERT INTO mensajes (origen, contenido) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, origen);
            ps.setString(2, contenido);
            ps.executeUpdate();
        } catch (Exception e) { System.out.println("[DB] save error: "+e.getMessage()); }
    }

    public static List<String> getAllMessages() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT id, origen, contenido, fecha FROM mensajes ORDER BY fecha DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add("[" + rs.getTimestamp("fecha") + "] " + rs.getString("origen") + ": " + rs.getString("contenido"));
            }
        } catch (Exception e) { System.out.println("[DB] read error: "+e.getMessage()); }
        return list;
    }

    public static boolean deleteMessage(int id) {
        String sql = "DELETE FROM mensajes WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { System.out.println("[DB] delete error: "+e.getMessage()); return false; }
    }
}
