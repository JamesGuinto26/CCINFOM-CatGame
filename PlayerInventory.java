import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class PlayerInventory
{

    public static void loadMoney(User currentUser) {
        String statement = "SELECT money FROM Player_Inventory WHERE player_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, currentUser.getIdNumber());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double money = resultSet.getDouble("money");
                currentUser.setCurrentMoney(money);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(resultSet, preparedStatement, connection);
        }
    }

    public static void updateMoney(User currentUser) {
        String statement = "UPDATE Player_Inventory SET money = ? WHERE player_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setDouble(1, currentUser.getCurrentMoney());
            preparedStatement.setString(2, currentUser.getIdNumber());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(null, preparedStatement, connection);
        }
    }

    // for furniture inventory
    public static void addFurniture(User currentUser, Furniture furniture, int quantity) {
        Connection connection = null;
        PreparedStatement updateStatement = null;
        PreparedStatement insertStatement = null;

        String updateQuery = "UPDATE Owned_Furniture " +
                "SET quantity = quantity + ? " +
                "WHERE Player_id = ? AND furniture_name = ?";

        String insertQuery = "INSERT INTO Owned_Furniture(Player_id, furniture_name, quantity) " +
                "SELECT ?, ?, ? WHERE NOT EXISTS " +
                "(SELECT Player_id, furniture_name " +
                "FROM Owned_Furniture WHERE Player_id = ? AND furniture_name = ?)";

        try {
            connection = DBConnection.getConnection();

            // check if furniture is already owned (increment by 1)
            updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, quantity);
            updateStatement.setString(2, currentUser.getIdNumber());
            updateStatement.setString(3, furniture.getName());

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected == 0) {
                insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, currentUser.getIdNumber());
                insertStatement.setString(2, furniture.getName());
                insertStatement.setInt(3, quantity);
                insertStatement.setString(4, currentUser.getIdNumber());
                insertStatement.setString(5, furniture.getName());
                insertStatement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(null, updateStatement, null);
            DBConnection.closeConnection(null, insertStatement, connection);
        }
    }

    // for food inventory
    public static void addFood(User currentUser, Food food, int quantity) {
        Connection connection = null;
        PreparedStatement updateStatement = null;
        PreparedStatement insertStatement = null;

        String updateQuery = "UPDATE Owned_Food " +
                "SET quantity = quantity + ? " +
                "WHERE Player_id = ? AND food_name = ?";

        String insertQuery = "INSERT INTO Owned_Food(Player_id, food_name, quantity) " +
                "SELECT ?, ?, ? WHERE NOT EXISTS " +
                "(SELECT Player_id, food_name " +
                "FROM Owned_Food WHERE Player_id = ? AND food_name = ?)";

        try {
            connection = DBConnection.getConnection();

            // check if furniture is already owned (increment by 1)
            updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, quantity);
            updateStatement.setString(2, currentUser.getIdNumber());
            updateStatement.setString(3, food.getName());

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected == 0) {
                insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, currentUser.getIdNumber());
                insertStatement.setString(2, food.getName());
                insertStatement.setInt(3, quantity);
                insertStatement.setString(4, currentUser.getIdNumber());
                insertStatement.setString(5, food.getName());
                insertStatement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(null, updateStatement, null);
            DBConnection.closeConnection(null, insertStatement, connection);
        }
    }

    public static ArrayList<OwnedFurniture> loadFurnitureInventory(User currentUser) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<OwnedFurniture> inventory = new ArrayList<>();

        String query = "SELECT furniture_name, quantity FROM Owned_Furniture WHERE Player_id = ?";

        try {
            connection = DBConnection.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, currentUser.getIdNumber());
            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("furniture_name");
                int quantity = rs.getInt("quantity");

                inventory.add(new OwnedFurniture(name, quantity)); // lets just loop through furnitures whenever we need to access yung info
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(rs, ps, connection);
        }

        return inventory;
    }

    public static ArrayList<OwnedFood> loadFoodInventory(User currentUser) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<OwnedFood> inventory = new ArrayList<>();

        String query = "SELECT food_name, quantity FROM Owned_Food WHERE Player_id = ?";

        try {
            connection = DBConnection.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, currentUser.getIdNumber());
            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("food_name");
                int quantity = rs.getInt("quantity");

                inventory.add(new OwnedFood(name, quantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(rs, ps, connection);
        }

        return inventory;
    }
}