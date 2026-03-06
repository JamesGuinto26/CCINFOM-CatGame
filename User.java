import java.sql.Connection;
import java.sql.*;
import java.util.*;

public class User
{
    private String idNumber;
    private String username;
    private String dateOfBirth;
    private char gender;
    private double currentMoney;
    private ArrayList<OwnedFurniture> furnitureInventory;
    private ArrayList<OwnedFood> foodInventory;
    private PlacedFurniture[] placedFurnitures;
    private ArrayList<OwnedCat> catsOnLawn;

    public User(String idNumber, String username, String dateOfBirth, char gender) {
        this.idNumber = idNumber;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.furnitureInventory = PlayerInventory.loadFurnitureInventory(this);
        this.foodInventory = PlayerInventory.loadFoodInventory(this);
        assignMissingOwnedCats();
        assignDailyFurnitureOccupancy();
        catsOnLawn = loadCats();
        this.placedFurnitures = PlacedFurnitureSQL.loadPlacedFurniture(this);
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public String getUsername() {
        return this.username;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public char getGender() {
        return this.gender;
    }

    public double getCurrentMoney() {
        return this.currentMoney;
    }

    public void setCurrentMoney(double money) {
        this.currentMoney = money;
    }


    public ArrayList<OwnedFurniture> getFurnitureInventory() {
        return this.furnitureInventory;
    }

    public void setFurnitureInventory(ArrayList<OwnedFurniture> furnitureInventory) {
        this.furnitureInventory = furnitureInventory;
    }

    public ArrayList<OwnedFood> getFoodInventory() {
        return this.foodInventory;
    }

    public void setFoodInventory(ArrayList<OwnedFood> foodInventory) {
        this.foodInventory = foodInventory;
    }

    public PlacedFurniture[] getPlacedFurniture() {
        return this.placedFurnitures;
    }

    public void setPlacedFurniture(PlacedFurniture[] placedFurniture) {
        this.placedFurnitures = placedFurniture;
    }

    public void assignMissingOwnedCats() {

        String query =
                "INSERT INTO Owned_Cat (player_id, cat_id) " +
                "SELECT " +
                "    pf.player_id, " +
                "    c.id AS cat_id " +
                "FROM Placed_Furniture pf " +
                "JOIN Furniture_Preference fp " +
                "    ON pf.furniture_name = fp.furniture_name " +
                "JOIN Cat c " +
                "    ON c.breed = fp.cat_breed " +
                "LEFT JOIN Furniture_Occupancy fo " +
                "    ON fo.placed_furniture_id = pf.id " +
                "   AND fo.time_occupied = CURDATE() " +
                "LEFT JOIN Owned_Cat oc " +
                "    ON oc.player_id = pf.player_id " +
                "   AND oc.cat_id = c.id " +
                "WHERE fo.placed_furniture_id IS NULL " +
                "  AND oc.cat_id IS NULL";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void assignDailyFurnitureOccupancy() {
        String sql =
    "INSERT INTO Furniture_Occupancy (placed_furniture_id, owned_cat_id, time_occupied) " +
            "SELECT " +
            "    x.placed_furniture_id, " +
            "    x.owned_cat_id, " +
            "    CURDATE() " +
            "FROM ( " +
            "    SELECT " +
            "        pf.id AS placed_furniture_id, " +
            "        oc.id AS owned_cat_id, " +
            "        ROW_NUMBER() OVER (PARTITION BY pf.id ORDER BY RAND()) AS rn " +
            "    FROM Placed_Furniture pf " +
            "    JOIN Furniture_Preference fp " +
            "        ON pf.furniture_name = fp.furniture_name " +
            "    JOIN Cat c " +
            "        ON c.breed = fp.cat_breed " +
            "    JOIN Owned_Cat oc " +
            "        ON oc.cat_id = c.id " +
            "       AND oc.player_id = pf.player_id " +
            "    LEFT JOIN Furniture_Occupancy fo " +
            "        ON fo.placed_furniture_id = pf.id " +
            "       AND fo.time_occupied = CURDATE() " +
            "    WHERE fo.placed_furniture_id IS NULL " +
            ") x " +
            "WHERE x.rn = 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<OwnedCat> loadCats() {
        int playerId = Integer.parseInt(this.idNumber);
        ArrayList<OwnedCat> cats = new ArrayList<>();

        String sql =
                "SELECT oc.id AS owned_cat_id, oc.date_arrived, oc.affection_points, oc.hunger, " +
                        "       oc.player_id, oc.cat_id " +
                        "FROM Furniture_Occupancy fo " +
                        "JOIN Owned_Cat oc ON fo.owned_cat_id = oc.id " +
                        "JOIN Placed_Furniture pf ON pf.id = fo.placed_furniture_id " +
                        "WHERE fo.time_occupied = CURDATE() " +
                        "  AND pf.player_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OwnedCat cat = new OwnedCat(
                        rs.getInt("owned_cat_id"),
                        rs.getString("date_arrived"),
                        rs.getInt("affection_points"),
                        rs.getInt("hunger"),
                        rs.getInt("player_id"),
                        rs.getInt("cat_id")
                );
                cats.add(cat);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cats;
    }

    public ArrayList<OwnedCat> getCatsOnLawn() {
        return catsOnLawn;
    }
}


