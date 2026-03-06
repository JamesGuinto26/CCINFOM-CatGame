import java.sql.*;

public class PlacedFurnitureSQL {

    public void insertPlacedFurniture(int position, String playerId, String furnitureName) {
        String query = "INSERT INTO Placed_Furniture (position_placed, player_id, furniture_name) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, position);
            ps.setString(2, playerId);
            ps.setString(3, furnitureName);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void decreaseOwnedFurniture(String playerId, String furnitureName) {
        String update = "UPDATE Owned_Furniture SET quantity = quantity - 1 " +
                "WHERE player_id = ? AND furniture_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(update)) {

            ps.setString(1, playerId);
            ps.setString(2, furnitureName);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void archiveRecord(PlacedFurniture placedFurniture) {
        String insert = 
                "INSERT INTO furniture_occupancy_archive " +
                "SELECT oc.player_id, pf.furniture_name, fuo.owned_cat_id " +
                "FROM Furniture_Occupancy fuo " +
                "    JOIN owned_cat oc" +
                "        ON fuo.owned_cat_id = oc.id" +
                "    JOIN placed_furniture pf" +
                "        ON fuo.placed_furniture_id = pf.id" +
                "WHERE placed_furniture_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(insert)) {

            ps.setInt(1, placedFurniture.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteIfZero(String playerId, String furnitureName) {
        String delete = "DELETE FROM Owned_Furniture WHERE player_id = ? AND furniture_name = ? AND quantity <= 0";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(delete)) {

            ps.setString(1, playerId);
            ps.setString(2, furnitureName);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void increaseOwnedFurniture(String playerId, String furnitureName) {
        String query = "INSERT INTO Owned_Furniture (player_id, furniture_name, quantity) " +
                "VALUES (?, ?, 1) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, playerId);
            ps.setString(2, furnitureName);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlacedFurniture(int positionPlaced, String playerId) {
        String delete = "DELETE FROM Placed_Furniture WHERE position_placed = ? AND player_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(delete)) {

            ps.setInt(1, positionPlaced);
            ps.setString(2, playerId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PlacedFurniture[] loadPlacedFurniture(User currentUser) {
        String query = "SELECT * " +
                "FROM Placed_Furniture pf " +
                "JOIN Furniture f " +
                "ON pf.furniture_name = f.name " +
                "WHERE pf.player_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            String playerId = currentUser.getIdNumber();
            ps.setString(1, playerId);

            ResultSet rs = ps.executeQuery();

            PlacedFurniture[] list = new PlacedFurniture[5];

            while (rs.next()) {
                int id = rs.getInt("id");
                int position = rs.getInt("position_placed");
                String name = rs.getString("furniture_name");
                String type = rs.getString("type");
                double price = rs.getDouble("price");
                String description = rs.getString("description");

                Furniture furniture = new Furniture(name, type, price, description);

                if (position >= 1 && position <= 5) {
                    list[position - 1] = new PlacedFurniture(id, position, playerId, furniture);
                }
            }

            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return new PlacedFurniture[5];
        }
    }

}
