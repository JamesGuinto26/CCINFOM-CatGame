import java.sql.*;
import java.util.ArrayList;

public class OwnedCat{
    int id;
    String dateArrived;
    int affectionPoints;
    int hunger;
    int playerId;
    int catId;

    public OwnedCat(
            int id,
            String dateArrived,
            int affectionPoints,
            int hunger,
            int playerId,
            int catId
    ) {
        this.id = id;
        this.dateArrived = dateArrived;
        this.affectionPoints = affectionPoints;
        this.hunger = hunger;
        this.playerId = playerId;
        this.catId = catId;
    }

    public int getId() {
        return this.id;
    }

    public String getDateArrived() {
        return this.getDateArrived();
    }

    public int getAffectionPoints() {
        return this.getAffectionPoints();
    }

    public int getHunger() {
        return this.getHunger();
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public int getCatId() {
        return this.catId;
    }

    public void setAffectionPoints(int affectionPoints) {
        this.affectionPoints = affectionPoints;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public static ArrayList<OwnedCat> loadOwnedCats(User currentUser) {
        String playerId = currentUser.getIdNumber();

        String query = "SELECT * FROM Owned_Cat " +
                "WHERE player_id = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, playerId);
            ResultSet rs = ps.executeQuery();

            ArrayList<OwnedCat> cats = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String dateArrived = rs.getDate("date_arrived").toString();
                int affectionPoints = rs.getInt("affection_points");
                int hunger = rs.getInt("hunger");
                int catId = rs.getInt("cat_id");

                cats.add(new OwnedCat(id, dateArrived, affectionPoints, hunger, Integer.parseInt(playerId), catId));
            }

            return cats;

        } catch(SQLException e) {
            e.printStackTrace();
            return new ArrayList<OwnedCat>();
        }

    }

    public Cat getCat() {
        String query = "SELECT * FROM Cat WHERE id = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, this.catId);
            ResultSet rs = ps.executeQuery();

            int id = rs.getInt("id");
            String name = rs.getString("name");
            String gender = rs.getString("gender");
            String life_stage = rs.getString("life_stage");
            String breed = rs.getString("breed");
            String color = rs.getString("color");
            String personality = rs.getString("personality");
            String description = rs.getString("description");

            return new Cat(id, name, gender, life_stage, breed, color, personality, description);

        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
