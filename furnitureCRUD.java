import javax.swing.*;
import java.sql.*;

public class furnitureCRUD {

    // CREATE
    public boolean addFurniture(String name, String type, double price, String description) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getConnection();

            String checkFurnitures = "SELECT name FROM Furniture WHERE name = ?";
            preparedStatement = connection.prepareStatement(checkFurnitures);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Furniture with name \"" + name + "\" already exists!", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            preparedStatement.close();
            resultSet.close();

            String addFurnitures = "INSERT INTO Furniture (name, type, price, description) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(addFurnitures);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            preparedStatement.setDouble(3, price);
            preparedStatement.setString(4, description);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Furniture \"" + name + "\" added successfully!", "Furniture Successfully Added", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error:\n" + e.getMessage(),"Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            DBConnection.closeConnection(resultSet, preparedStatement, connection);
        }
    }

    // UPDATE
    public boolean updateFurniture(String oldName, String newName, String newType, double newPrice, String newDescription) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getConnection();

            String checkFurniture = "SELECT name FROM Furniture WHERE name = ?";
            preparedStatement = connection.prepareStatement(checkFurniture);
            preparedStatement.setString(1, newName);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Cannot rename furniture. \"" + newName + "\" already exists.", "Duplicate Name", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            DBConnection.closeConnection(resultSet, preparedStatement, null);
            preparedStatement = null;
            resultSet  = null;

            String updateFurniture = "UPDATE Furniture SET name = ?, type = ?, price = ?, description = ? WHERE name = ?";
            preparedStatement = connection.prepareStatement(updateFurniture);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newType);
            preparedStatement.setDouble(3, newPrice);
            preparedStatement.setString(4, newDescription);
            preparedStatement.executeUpdate();

            DBConnection.closeConnection(null,preparedStatement, null);
            preparedStatement = null; // will still be used for Owned_Furniture

            if (!oldName.equals(newName)) {

                String updateOwned = "UPDATE Owned_Furniture SET furniture_name = ? WHERE furniture_name = ?";
                preparedStatement = connection.prepareStatement(updateOwned);
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, oldName);
                preparedStatement.executeUpdate();
                DBConnection.closeConnection(null, preparedStatement, null);
                preparedStatement = null;

                String updatePreference = "UPDATE Furniture_Preference SET furniture_name = ? WHERE furniture_name = ?";
                preparedStatement = connection.prepareStatement(updatePreference);
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, oldName);
                preparedStatement.executeUpdate();
                DBConnection.closeConnection(null, preparedStatement, null);
                preparedStatement = null;

                String updatePlaced = "UPDATE Placed_Furniture SET furniture_name = ? WHERE furniture_name = ?";
                preparedStatement = connection.prepareStatement(updatePlaced);
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, oldName);
                preparedStatement.executeUpdate();
                DBConnection.closeConnection(null, preparedStatement, null);
                preparedStatement = null;
            }

            JOptionPane.showMessageDialog(null, "Furniture \"" + oldName + "\" updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            DBConnection.closeConnection(resultSet, preparedStatement, connection);
        }
    }

    // DELETE
    public boolean deleteFurniture(String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String checkSQL = "SELECT name FROM Furniture WHERE name = ?";
            preparedStatement = connection.prepareStatement(checkSQL);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Furniture \"" + name + "\" does not exist.", "Not Found", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            DBConnection.closeConnection(resultSet, preparedStatement, null);
            preparedStatement = null;
            resultSet = null;

            // check if furniture is owned by a player
            String checkOwned = "SELECT 1 FROM Owned_Furniture WHERE furniture_name = ?";
            preparedStatement = connection.prepareStatement(checkOwned);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Cannot delete. Furniture is owned by a player.", "Deletion Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            DBConnection.closeConnection(resultSet, preparedStatement, null);
            preparedStatement = null;
            resultSet = null;

            // check if furniture is placed
            String checkPlaced = "SELECT 1 FROM Placed_Furniture WHERE furniture_name = ?";
            preparedStatement = connection.prepareStatement(checkPlaced);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Cannot delete. Furniture is currently placed.", "Deletion Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            DBConnection.closeConnection(resultSet, preparedStatement, null);
            preparedStatement = null;
            resultSet = null;

            // check if furniture is included in preferred furniture
            String checkPreference = "SELECT 1 FROM Furniture_Preference WHERE furniture_name = ?";
            preparedStatement = connection.prepareStatement(checkPreference);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Cannot delete. Furniture is included in a cat's preference.", "Deletion Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            DBConnection.closeConnection(resultSet, preparedStatement, null);
            preparedStatement = null;
            resultSet = null;

            // delete furniture
            String deleteFurniture = "DELETE FROM Furniture WHERE name = ?";
            preparedStatement = connection.prepareStatement(deleteFurniture);
            preparedStatement.setString(1, deleteFurniture);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Furniture \"" + deleteFurniture + "\" deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Database Error:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;

        } finally {
            DBConnection.closeConnection(resultSet, preparedStatement, connection);
        }
    }
}
