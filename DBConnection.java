import java.sql.*;

// NOTE: feel free change the url, username, and password

public class DBConnection
{
    public static Connection getConnection()
    {
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/kitty", "root", "p@ssword");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database connection error:");
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection)
    {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
