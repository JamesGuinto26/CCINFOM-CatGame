import java.sql.*;
import java.util.ArrayList;

public class FoodSystem
{
    public ArrayList<Food> getAllFoods()
    {
        ArrayList<Food> foods = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM Food";

        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("description");
                int saturationValue = resultSet.getInt("saturation_value");

                Food newFood = new Food(name, price, description, saturationValue);
                foods.add(newFood);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(resultSet, preparedStatement, connection);
        }

        return foods;
    }
}