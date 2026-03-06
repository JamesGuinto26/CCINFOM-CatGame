/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Laptop
 */
public class PlacedFurniture {
    private int id;
    private int positionPlaced;
    private String playerId;
    private Furniture furniture;

    public PlacedFurniture(
            int id,
            int positionPlaced,
            String playerId,
            Furniture furniture
    ) {
        this.id = id;
        this.positionPlaced = positionPlaced;
        this.playerId = playerId;
        this.furniture = furniture;
    }

    public int getId() {
        return this.id;
    }

    public int getPositionPlaced() {
        return this.positionPlaced;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public Furniture getFurniture() {
        return this.furniture;
    }

    public boolean hasOccupyingCat(int playerId) {
        String query = "SELECT COUNT(*) AS count " +
                "    FROM furniture_occupancy fuo" +
                "    JOIN placed_furniture pf " +
                "      ON fuo.placed_furniture_id = pf.id" +
                "    WHERE pf.player_id = ? " +
                "AND time_occupied = CURDATE()";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();

            return rs.getInt("total") > 0;

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public OwnedCat getOccupyingCat(int playerId) {
        String query = "SELECT * " +
                "    FROM furniture_occupancy fuo" +
                "    JOIN placed_furniture pf" +
                "      ON fuo.placed_furniture_id = pf.id " +
                "    JOIN Owned_Cat oc " +
                "      ON fuo.owned_cat_id = oc.id" +
                "    WHERE pf.player_id = ? " +
                "AND time_occupied = CURDATE()";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();

            OwnedCat cat = null;

            while(rs.next()) {
                int id = rs.getInt("id");
                String dateArrived = rs.getString("date_arrived");
                int affectionPoints = rs.getInt("affection_points");
                int hunger = rs.getInt("hunger");
                int catId = rs.getInt("cat_id");

                cat = new OwnedCat(id, dateArrived, affectionPoints, hunger, playerId, catId);
            }

            return cat;

        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
