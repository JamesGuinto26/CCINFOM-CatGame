import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;

public class Cat {
    int id;
    String name;
    String gender;
    String lifeStage;
    String breed;
    String color;
    String personality;
    String description;
    ArrayList<Food> preferredFood;
    ArrayList<Furniture> preferredFurniture;
    private ImageIcon image;

    public Cat(
            int id,
            String name,
            String gender,
            String lifeStage,
            String breed,
            String color,
            String personality,
            String description
    ) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.lifeStage = lifeStage;
        this.breed = breed;
        this.color = color;
        this.personality = personality;
        this.description = description;
        this.preferredFood = loadPreferredFood(breed);
        this.preferredFurniture = loadPreferredFurniture(breed);

        String key = breed.ToLowerCase() + "-" + color.ToLowerCase();
        
        switch(key) {
            case "british shorthair-blue":
                image = new ImageIcon("images/Cat_Designs/BS_B.png");
                break;
            case "british shorthair-calico":
                image = new ImageIcon("images/Cat_Designs/BS_C.png");
                break;
            case "british shorthair-gray":
                image = new ImageIcon("images/Cat_Designs/BS_G.png");
                break;
            case "bengal-marble pattern":
                image = new ImageIcon("images/Cat_Designs/B_MP.png");
                break;
            case "bengal-orange":
                image = new ImageIcon("images/Cat_Designs/B_O.png");
                break;
            case "bengal-spotted rosette":
                image = new ImageIcon("images/Cat_Designs/B_SR.png");
                break;
            case "domestic shorthair-black":
                image = new ImageIcon("images/Cat_Designs/DC_BL.png");
                break;
            case "domestic shorthair-tortoiseshell":
                image = new ImageIcon("images/Cat_Designs/DC_T.png");
                break;
            case "main coon-black smoke":
                image = new ImageIcon("images/Cat_Designs/MC_BS.png");
                break;
            case "main coon-brown tabby":
                image = new ImageIcon("images/Cat_Designs/MC_BT.png");
                break;
            case "main coon-pinkish gray":
                image = new ImageIcon("images/Cat_Designs/MC_PG.png");
                break;
            case "persian-silver":
                image = new ImageIcon("images/Cat_Designs/P_MAF.png");
                break;
            case "persian-white":
                image = new ImageIcon("images/Cat_Designs/P_MAM.png");
                break;
            case "persian-sable brown":
                image = new ImageIcon("images/Cat_Designs/P_S.png");
                break;
            case "ragdoll-blue mitted":
                image = new ImageIcon("images/Cat_Designs/R_BM.png");
                break;
            case "ragdoll-mitted":
                image = new ImageIcon("images/Cat_Designs/R_M.png");
                break;
            case "ragdoll-seal bicolor":
                image = new ImageIcon("images/Cat_Designs/R_SB.png");
                break;
            case "siamese-chocolate-point":
                image = new ImageIcon("images/Cat_Designs/S_CPA.png");
                break;
            case "siamese-chocolate point":
                image = new ImageIcon("images/Cat_Designs/S_CPK.png");
                break;
            case "siamese-cream with spots":
                image = new ImageIcon("images/Cat_Designs/S_CS.png");
                break;
    
        }
    }

    public int getId() {
         return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getGender() {
        return this.gender;
    }

    public String getLifeStage() {
        return this.lifeStage;
    }

    public String getBreed() {
        return this.breed;
    }

    public String getColor() {
        return this.color;
    }

    public String getPersonality() {
        return this.personality;
    }

    public String getDescription() {
        return this.description;
    }

    public ArrayList<Food> getPreferredFood() {
        return this.preferredFood;
    }

    public ArrayList<Furniture> getPreferredFurniture() {
        return this.preferredFurniture;
    }

    public static ArrayList<Food> loadPreferredFood(String breed) {
        String query = "SELECT * FROM Food_Preference fp " +
                "    JOIN Food f" +
                "       on fp.food_name = f.name" +
                "WHERE cat_breed = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, breed);
            ResultSet rs = ps.executeQuery();

            ArrayList<Food> list = new ArrayList<>();
            while(rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int saturationValue = rs.getInt("saturation_value");
                String description = rs.getString("description");

                list.add(new Food(name, price, description, saturationValue));
            }

            return list;
        } catch(SQLException e) {
            e.printStackTrace();
            return new ArrayList<Food>();
        }
    }

    public static ArrayList<Furniture> loadPreferredFurniture(String breed) {
        String query = "SELECT * FROM Furniture_Preference fp " +
                "    JOIN Furniture f" +
                "       on fp.furniture_name = f.name" +
                "WHERE cat_breed = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, breed);
            ResultSet rs = ps.executeQuery();

            ArrayList<Furniture> list = new ArrayList<>();
            while(rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");
                double price = rs.getDouble("price");
                String description = rs.getString("description");

                list.add(new Furniture(name, type, price, description));
            }

            return list;
        } catch(SQLException e) {
            e.printStackTrace();
            return new ArrayList<Furniture>();
        }
    }
}

