import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CatReport {


    public void catStats(User curUser, String catName)
    {
        JFrame ps = new JFrame("Player's Cat Report");
        ps.setResizable(false);
        ps.setUndecorated(true);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 255, 200));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(255, 255, 200));

        JLabel desc = new JLabel("Cat Report: " + catName);
        desc.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(desc);

        // Data panel
        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setBackground(new Color(255, 255, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;

        // Initialize data variables
        String breed = "N/A";
        String uniqueFoodsEaten = "0";
        String totalFoodConsumptions = "0";
        String distinctFurnitureUsed = "0";
        String totalFurnitureUses = "0";

        // Query the database for cat statistics
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            if (conn != null) {
                String query = "SELECT cat_name, breed, " +
                        "SUM(distinct_foods_eaten) as unique_foods_eaten, " +
                        "SUM(total_food_consumptions) as total_food_consumptions, " +
                        "SUM(distinct_furniture_used) as distinct_furniture_used, " +
                        "SUM(total_furniture_uses) as total_furniture_uses " +
                        "FROM Cat_Report WHERE cat_name = ? " +
                        "GROUP BY cat_name, breed";

                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, catName);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    breed = rs.getString("breed");
                    uniqueFoodsEaten = String.valueOf(rs.getInt("unique_foods_eaten"));
                    totalFoodConsumptions = String.valueOf(rs.getInt("total_food_consumptions"));
                    distinctFurnitureUsed = String.valueOf(rs.getInt("distinct_furniture_used"));
                    totalFurnitureUses = String.valueOf(rs.getInt("total_furniture_uses"));
                }
            } else {
                JOptionPane.showMessageDialog(ps,
                        "Unable to connect to database",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ps,
                    "Error loading cat statistics: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(rs, pstmt, conn);
        }

        // Cat Name row
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Cat Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        dataPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JLabel nameValue = new JLabel(catName);
        nameValue.setFont(new Font("Arial", Font.PLAIN, 15));
        dataPanel.add(nameValue, gbc);

        // Breed row
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel breedLabel = new JLabel("Breed:");
        breedLabel.setFont(new Font("Arial", Font.BOLD, 15));
        dataPanel.add(breedLabel, gbc);

        gbc.gridx = 1;
        JLabel breedValue = new JLabel(breed);
        breedValue.setFont(new Font("Arial", Font.PLAIN, 15));
        dataPanel.add(breedValue, gbc);

        // Unique Foods Eaten row
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel uniqueFoodLabel = new JLabel("Unique Foods Eaten:");
        uniqueFoodLabel.setFont(new Font("Arial", Font.BOLD, 15));
        dataPanel.add(uniqueFoodLabel, gbc);

        gbc.gridx = 1;
        JLabel uniqueFoodValue = new JLabel(uniqueFoodsEaten);
        uniqueFoodValue.setFont(new Font("Arial", Font.PLAIN, 15));
        dataPanel.add(uniqueFoodValue, gbc);

        // Total Food Consumptions row
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel foodConsumptionLabel = new JLabel("Total Food Consumptions:");
        foodConsumptionLabel.setFont(new Font("Arial", Font.BOLD, 15));
        dataPanel.add(foodConsumptionLabel, gbc);

        gbc.gridx = 1;
        JLabel foodConsumptionValue = new JLabel(totalFoodConsumptions);
        foodConsumptionValue.setFont(new Font("Arial", Font.PLAIN, 15));
        dataPanel.add(foodConsumptionValue, gbc);

        // Distinct Furniture Used row
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel distinctFurnitureLabel = new JLabel("Distinct Furniture Used:");
        distinctFurnitureLabel.setFont(new Font("Arial", Font.BOLD, 15));
        dataPanel.add(distinctFurnitureLabel, gbc);

        gbc.gridx = 1;
        JLabel distinctFurnitureValue = new JLabel(distinctFurnitureUsed);
        distinctFurnitureValue.setFont(new Font("Arial", Font.PLAIN, 15));
        dataPanel.add(distinctFurnitureValue, gbc);

        // Total Furniture Uses row
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel furnitureUsesLabel = new JLabel("Total Furniture Uses:");
        furnitureUsesLabel.setFont(new Font("Arial", Font.BOLD, 15));
        dataPanel.add(furnitureUsesLabel, gbc);

        gbc.gridx = 1;
        JLabel furnitureUsesValue = new JLabel(totalFurnitureUses);
        furnitureUsesValue.setFont(new Font("Arial", Font.PLAIN, 15));
        dataPanel.add(furnitureUsesValue, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 255, 200));

        JButton fold = new JButton("Return");
        fold.setForeground(Color.WHITE);
        fold.setFont(new Font("Arial", Font.BOLD, 15));
        fold.setBackground(new Color(102, 51, 0));
        fold.setPreferredSize(new Dimension(120, 40));
        fold.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        fold.setFocusable(false);

        fold.addActionListener(e -> ps.dispose());

        buttonPanel.add(fold);

        // Add all panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        ps.add(mainPanel);
        ps.pack();
        ps.setLocationRelativeTo(null);
        ps.setSize(500, 400);
        ps.setVisible(true);
    }
    /*
        Frame could be resized/ reformated as needed.
    */
    public void foodStats(User curUser, String foodName)
    {
            JFrame ps = new JFrame("Food Statistics");
            ps.setResizable(false);
            ps.setUndecorated(true);

            // Main panel
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(255, 255, 200));
            mainPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 2),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)));

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setBackground(new Color(255, 255, 200));

            JLabel desc = new JLabel("Food Report: " + foodName);
            desc.setFont(new Font("Arial", Font.BOLD, 24));
            titlePanel.add(desc);

            // Data panel
            JPanel dataPanel = new JPanel(new GridBagLayout());
            dataPanel.setBackground(new Color(255, 255, 200));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.BASELINE_LEADING;

            // Initialize data variables
            String price = "N/A";
            String description = "N/A";
            String totalOwned = "0";
            String timesEaten = "0";

            // Query the database for food statistics
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DBConnection.getConnection();

                if (conn != null) {
                    String query = "SELECT food_name, price, description, total_owned, total_times_eaten " +
                            "FROM Food_Report WHERE food_name = ?";

                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, foodName);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        price = String.format("%.2f", rs.getDouble("price"));
                        description = rs.getString("description");
                        totalOwned = String.valueOf(rs.getInt("total_owned"));
                        timesEaten = String.valueOf(rs.getInt("total_times_eaten"));
                    }
                }
                else {
                    JOptionPane.showMessageDialog(ps,
                            "Unable to connect to database",
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(ps,
                        "Error loading food statistics: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                DBConnection.closeConnection(rs, pstmt, conn);
            }

            // Food Name row
            gbc.gridx = 0;
            gbc.gridy = 0;
            JLabel nameLabel = new JLabel("Food Name:");
            nameLabel.setFont(new Font("Arial", Font.BOLD, 15));
            dataPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            JLabel nameValue = new JLabel(foodName);
            nameValue.setFont(new Font("Arial", Font.PLAIN, 15));
            dataPanel.add(nameValue, gbc);

            // Price row
            gbc.gridx = 0;
            gbc.gridy = 1;
            JLabel priceLabel = new JLabel("Price:");
            priceLabel.setFont(new Font("Arial", Font.BOLD, 15));
            dataPanel.add(priceLabel, gbc);

            gbc.gridx = 1;
            JLabel priceValue = new JLabel( price);
            priceValue.setFont(new Font("Arial", Font.PLAIN, 15));
            dataPanel.add(priceValue, gbc);

            // Description row
            gbc.gridx = 0;
            gbc.gridy = 2;
            JLabel descLabel = new JLabel("Description:");
            descLabel.setFont(new Font("Arial", Font.BOLD, 15));
            dataPanel.add(descLabel, gbc);

            gbc.gridx = 1;
            JLabel descValue = new JLabel(description);
            descValue.setFont(new Font("Arial", Font.PLAIN, 15));
            dataPanel.add(descValue, gbc);

            // Total Owned row
            gbc.gridx = 0;
            gbc.gridy = 3;
            JLabel ownedLabel = new JLabel("Total Owned (All Players):");
            ownedLabel.setFont(new Font("Arial", Font.BOLD, 15));
            dataPanel.add(ownedLabel, gbc);

            gbc.gridx = 1;
            JLabel ownedValue = new JLabel(totalOwned);
            ownedValue.setFont(new Font("Arial", Font.PLAIN, 15));
            dataPanel.add(ownedValue, gbc);

            // Times Eaten row
            gbc.gridx = 0;
            gbc.gridy = 4;
            JLabel eatenLabel = new JLabel("Total Times Eaten:");
            eatenLabel.setFont(new Font("Arial", Font.BOLD, 15));
            dataPanel.add(eatenLabel, gbc);

            gbc.gridx = 1;
            JLabel eatenValue = new JLabel(timesEaten);
            eatenValue.setFont(new Font("Arial", Font.PLAIN, 15));
            dataPanel.add(eatenValue, gbc);

            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(new Color(255, 255, 200));

            JButton fold = new JButton("Return");
            fold.setForeground(Color.WHITE);
            fold.setFont(new Font("Arial", Font.BOLD, 15));
            fold.setBackground(new Color(102, 51, 0));
            fold.setPreferredSize(new Dimension(120, 40));
            fold.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            fold.setFocusable(false);

            fold.addActionListener(e -> ps.dispose());

            buttonPanel.add(fold);

            // Add all panels to main panel
            mainPanel.add(titlePanel, BorderLayout.NORTH);
            mainPanel.add(dataPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            ps.add(mainPanel);
            ps.pack();
            ps.setLocationRelativeTo(null);
            ps.setSize(450, 350);
            ps.setVisible(true);
        }

        private JButton styleButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(100, 35));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setFocusable(false);
        return button;
       }

    /*
        Frame could be resized/ reformated as needed.
    */
    public void furnitureStats(User curUser, String furnitureName)
    {
        JFrame ps = new JFrame("Furniture Statistics");
        ps.setResizable(false);
        ps.setUndecorated(true);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 255, 200));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(255, 255, 200));

        JLabel desc = new JLabel("Furniture Report: " + furnitureName);
        desc.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(desc);

        // Data panel
        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setBackground(new Color(255, 255, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;

        // Initialize data variables
        String description = "N/A";
        String totalOwned = "0";
        String timesPlaced = "0";
        String catOccupancy = "0";

        // Query the database for furniture statistics
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            if (conn != null) {
                String query = "SELECT furniture_name, description, total_owned, total_times_placed, total_cat_occupancy " +
                        "FROM Furniture_Report WHERE furniture_name = ?";

                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, furnitureName);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    description = rs.getString("description");
                    totalOwned = String.valueOf(rs.getInt("total_owned"));
                    timesPlaced = String.valueOf(rs.getInt("total_times_placed"));
                    catOccupancy = String.valueOf(rs.getInt("total_cat_occupancy"));
                }
            } else {
                JOptionPane.showMessageDialog(ps,
                        "Unable to connect to database",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ps,
                    "Error loading furniture statistics: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnection.closeConnection(rs, pstmt, conn);
        }

        // Furniture Name row
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Furniture Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dataPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JLabel nameValue = new JLabel(furnitureName);
        nameValue.setFont(new Font("Arial", Font.PLAIN, 12));
        dataPanel.add(nameValue, gbc);

        // Description row
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dataPanel.add(descLabel, gbc);

        gbc.gridx = 1;
        JLabel descValue = new JLabel(description);
        descValue.setFont(new Font("Arial", Font.PLAIN, 12));
        dataPanel.add(descValue, gbc);

        // Total Owned row
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel ownedLabel = new JLabel("Total Owned: ");
        ownedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dataPanel.add(ownedLabel, gbc);

        gbc.gridx = 1;
        JLabel ownedValue = new JLabel(totalOwned);
        ownedValue.setFont(new Font("Arial", Font.PLAIN, 12));
        dataPanel.add(ownedValue, gbc);

        // Times Placed row
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel placedLabel = new JLabel("Total Times Placed:");
        placedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dataPanel.add(placedLabel, gbc);

        gbc.gridx = 1;
        JLabel placedValue = new JLabel(timesPlaced);
        placedValue.setFont(new Font("Arial", Font.PLAIN, 12));
        dataPanel.add(placedValue, gbc);

        // Cat Occupancy row
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel occupancyLabel = new JLabel("Total Cat Occupancies:");
        occupancyLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dataPanel.add(occupancyLabel, gbc);

        gbc.gridx = 1;
        JLabel occupancyValue = new JLabel(catOccupancy);
        occupancyValue.setFont(new Font("Arial", Font.PLAIN, 12));
        dataPanel.add(occupancyValue, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 255, 200));

        JButton fold = new JButton("Return");
        fold.setForeground(Color.WHITE);
        fold.setFont(new Font("Arial", Font.BOLD, 15));
        fold.setBackground(new Color(102, 51, 0));
        fold.setPreferredSize(new Dimension(120, 40));
        fold.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        fold.setFocusable(false);

        fold.addActionListener(e -> ps.dispose());

        buttonPanel.add(fold);

        // Add all panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        ps.add(mainPanel);
        ps.pack();
        ps.setLocationRelativeTo(null);
        ps.setSize(450, 350);
        ps.setVisible(true);
    }
}
