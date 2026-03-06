import java.sql.*;
import java.util.ArrayList;

public class FurnitureSystem
{
    public ArrayList<Furniture> getAllFurnitures()
    {
        ArrayList<Furniture> furnitures = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM Furniture";

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("description");

                Furniture newFurniture = new Furniture(name, type, price, description);
                furnitures.add(newFurniture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(resultSet, preparedStatement, connection);
        }

        return furnitures;
    }
}
