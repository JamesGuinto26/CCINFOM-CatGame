import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.*;

public class Report extends JFrame{

    // Private Methods
    private JLabel title;
    private JLabel desc;
    private JPanel panel;
    private JFrame reportList;
    private JButton playerPerformance;
    private JButton catReport;
    private JButton eatReport;
    private JButton furnitureReport;
    private JButton fold;
    private CatReport report;

    public Report()
    {
        this.report = new CatReport();
    }


    private void pStats(User curUser)
    {
        JFrame ps = new JFrame("Player Data");
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

        this.desc = new JLabel("Player Statistics");
        desc.setFont(new Font("Arial", Font.BOLD, 30));
        titlePanel.add(desc);

        // Data panel
        JPanel dataPanel = new JPanel(new GridBagLayout());
        dataPanel.setBackground(new Color(255, 255, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;

        // Get user data
        String name = curUser.getUsername();
        String id = curUser.getIdNumber();
        String dob = curUser.getDateOfBirth();
        String gender = "" + curUser.getGender();

        // Initialize statistics variables
        String totalCats = "0";
        String totalFood = "0";
        String totalFurniture = "0";

        // Query the database for player statistics
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            if (conn != null) {
                String query = "SELECT total_cats, total_food_items, total_furniture_items " +
                        "FROM Player_Report WHERE id = ?";

                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, id);

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    totalCats = String.valueOf(rs.getInt("total_cats"));
                    totalFood = String.valueOf(rs.getInt("total_food_items"));
                    totalFurniture = String.valueOf(rs.getInt("total_furniture_items"));
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
                    "Error loading player statistics: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        finally {
            DBConnection.closeConnection(rs, pstmt, conn);
        }

        // Name row
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        dataPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JLabel nameValue = new JLabel(name);
        nameValue.setFont(new Font("Arial", Font.PLAIN, 15));
        dataPanel.add(nameValue, gbc);

        // ID row
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 15));
        dataPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        JLabel idValue = new JLabel(id);
        idValue.setFont(new Font("Arial", Font.PLAIN, 15));
        dataPanel.add(idValue, gbc);

        // Total Cats
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel totalFed = new JLabel("Total Cats: ");
        totalFed.setFont(new Font("Arial", Font.BOLD, 15));
        dataPanel.add(totalFed, gbc);

        gbc.gridx = 1;
        JLabel countFed = new JLabel(totalCats);
        countFed.setFont(new Font("Arial", Font.PLAIN, 15));
        dataPanel.add(countFed, gbc);

        // Fed preference food count
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel prefLabel = new JLabel("Total Food Items: ");
        prefLabel.setFont(new Font("Arial", Font.BOLD, 15));
        dataPanel.add(prefLabel, gbc);

        gbc.gridx = 1;
        JLabel prefCount = new JLabel(totalFood);
        prefCount.setFont(new Font("Arial", Font.PLAIN, 15));
        dataPanel.add(prefCount, gbc);

        // Total Furnitures collected
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel catsLabel = new JLabel("Total Furniture Items: ");
        catsLabel.setFont(new Font("Arial", Font.BOLD, 15));
        dataPanel.add(catsLabel, gbc);

        gbc.gridx = 1;
        JLabel countCollected = new JLabel(totalFurniture);
        countCollected.setFont(new Font("Arial", Font.PLAIN, 15));
        dataPanel.add(countCollected, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 255, 200));

        this.fold = new JButton("Return");
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
        ps.setSize(400, 350);
        ps.setVisible(true);
    }

    private void cStats(User curUser)
    {
        JFrame searchFrame = new JFrame("Cat menu");
        searchFrame.setUndecorated(true);
        searchFrame.setResizable(false);
        searchFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        searchFrame.setLayout(new BorderLayout(20, 20));

        JPanel backgroundPanel = new JPanel(new BorderLayout(20, 20));
        backgroundPanel.setBackground(new Color(255, 255, 200));
        backgroundPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(25, 25, 10, 25)));

        JLabel label = new JLabel("Enter which cat data to load: ", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 19));
        label.setForeground(Color.BLACK);
        backgroundPanel.add(label, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        searchPanel.setOpaque(false);

        JTextField find = new JTextField();
        find.setBorder(new LineBorder(Color.BLACK, 2));
        find.setFont(new Font("Arial", Font.PLAIN, 13));
        find.setPreferredSize(new Dimension(200, 30));

        JButton enter = styleButton("Enter");
        enter.setBackground(new Color(51, 25, 0));
        enter.setSize(100, 100);

        JButton exit = styleButton("Cancel");
        exit.setBackground(new Color(119, 73, 27));
        exit.setSize(100, 100);

        searchPanel.add(find);
        searchPanel.add(enter);
        searchPanel.add(exit);

        backgroundPanel.add(searchPanel, BorderLayout.CENTER);
        searchFrame.add(backgroundPanel);

        // Validate cat entry
        enter.addActionListener(e -> {
            String catName = find.getText().trim();

            if (catName.isEmpty()) {
                JOptionPane.showMessageDialog(searchFrame,
                        "Please enter a cat name!",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validate if cat exists in database
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DBConnection.getConnection();

                if (conn != null) {
                    String query = "SELECT COUNT(*) FROM Cat WHERE name = ?";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, catName);
                    rs = pstmt.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {
                        // Cat exists, open report
                        searchFrame.dispose();
                        report.catStats(curUser, catName);
                    } else {
                        JOptionPane.showMessageDialog(searchFrame,
                                "Cat '" + catName + "' not found in database!",
                                "Cat Not Found",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(searchFrame,
                        "Error validating cat: " + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                DBConnection.closeConnection(rs, pstmt, conn);
            }
        });

        exit.addActionListener(e -> searchFrame.dispose());

        searchFrame.setSize(550, 180);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setVisible(true);
    }


    private void eStats(User curUser)
    {
        JFrame searchFrame = new JFrame("Food Report");
        searchFrame.setUndecorated(true);
        searchFrame.setResizable(false);
        searchFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        searchFrame.setLayout(new BorderLayout(20, 20));

        JPanel backgroundPanel = new JPanel(new BorderLayout(20, 20));
        backgroundPanel.setBackground(new Color(255, 255, 200));
        backgroundPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(25, 25, 10, 25)));

        JLabel label = new JLabel("Enter which food data to load: ", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 19));
        label.setForeground(Color.BLACK);
        backgroundPanel.add(label, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        searchPanel.setOpaque(false);

        JTextField find = new JTextField();
        find.setBorder(new LineBorder(Color.BLACK, 2));
        find.setFont(new Font("Arial", Font.PLAIN, 13));
        find.setPreferredSize(new Dimension(200, 30));

        JButton enter = styleButton("Enter");
        enter.setBackground(new Color(51, 25, 0));
        enter.setSize(100, 100);

        JButton exit = styleButton("Cancel");
        exit.setBackground(new Color(119, 73, 27));
        exit.setSize(100, 100);

        searchPanel.add(find);
        searchPanel.add(enter);
        searchPanel.add(exit);

        backgroundPanel.add(searchPanel, BorderLayout.CENTER);
        searchFrame.add(backgroundPanel);

        // Validate food entry
        enter.addActionListener(e -> {
            String foodName = find.getText().trim();

            if (foodName.isEmpty()) {
                JOptionPane.showMessageDialog(searchFrame,
                        "Please enter a food name!",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validate if food exists in database
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DBConnection.getConnection();

                if (conn != null) {
                    String query = "SELECT COUNT(*) FROM food WHERE name = ?";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, foodName);
                    rs = pstmt.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {
                        // Food exists, open report
                        searchFrame.dispose();
                        report.foodStats(curUser, foodName);
                    } else {
                        JOptionPane.showMessageDialog(searchFrame,
                                "Food '" + foodName + "' not found in database!",
                                "Food Not Found",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(searchFrame,
                        "Error validating food: " + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                DBConnection.closeConnection(rs, pstmt, conn);
            }
        });

        exit.addActionListener(e -> searchFrame.dispose());

        searchFrame.setSize(550, 180);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setVisible(true);
    }

    private void fStats(User curUser)
    {
        JFrame searchFrame = new JFrame("Furniture Report");
        searchFrame.setUndecorated(true);
        searchFrame.setResizable(false);
        searchFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        searchFrame.setLayout(new BorderLayout(20, 20));

        JPanel backgroundPanel = new JPanel(new BorderLayout(20, 20));
        backgroundPanel.setBackground(new Color(255, 255, 200));
        backgroundPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(25, 25, 10, 25)));

        JLabel label = new JLabel("Enter which furniture data to load: ", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 19));
        label.setForeground(Color.BLACK);
        backgroundPanel.add(label, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        searchPanel.setOpaque(false);

        JTextField find = new JTextField();
        find.setBorder(new LineBorder(Color.BLACK, 2));
        find.setFont(new Font("Arial", Font.PLAIN, 13));
        find.setPreferredSize(new Dimension(200, 30));

        JButton enter = styleButton("Enter");
        enter.setBackground(new Color(51, 25, 0));
        enter.setSize(100, 100);

        JButton exit = styleButton("Cancel");
        exit.setBackground(new Color(119, 73, 27));
        exit.setSize(100, 100);

        searchPanel.add(find);
        searchPanel.add(enter);
        searchPanel.add(exit);

        backgroundPanel.add(searchPanel, BorderLayout.CENTER);
        searchFrame.add(backgroundPanel);

        // Validate furniture entry
        enter.addActionListener(e -> {
            String furnitureName = find.getText().trim();

            if (furnitureName.isEmpty()) {
                JOptionPane.showMessageDialog(searchFrame,
                        "Please enter a furniture name!",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validate if furniture exists in database
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                conn = DBConnection.getConnection();

                if (conn != null) {
                    String query = "SELECT COUNT(*) FROM furniture WHERE name = ?";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, furnitureName);
                    rs = pstmt.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {
                        // Furniture exists, open report
                        searchFrame.dispose();
                        report.furnitureStats(curUser, furnitureName);
                    } else {
                        JOptionPane.showMessageDialog(searchFrame,
                                "Furniture '" + furnitureName + "' not found in database!",
                                "Furniture Not Found",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(searchFrame,
                        "Error validating furniture: " + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                DBConnection.closeConnection(rs, pstmt, conn);
            }
        });

        exit.addActionListener(e -> searchFrame.dispose());

        searchFrame.setSize(550, 180);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setVisible(true);
    }

    public JButton styleButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 35));
        button.setMargin(new Insets(15, 20, 15, 20));
        button.setFocusPainted(false);

        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public void showReports(User curUser)
    {
        this.reportList = new JFrame("Reports");
        reportList.setUndecorated(true);
        reportList.setResizable(false);

        //  reportList.setFocusable(false);

        this.panel = new JPanel();
        panel.setBackground(new Color(255, 255, 200));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2), BorderFactory.createEmptyBorder(20,20,20,20)));

        this.title = new JLabel("           Choose Report:            "); // I'll try to fix this, seems sumama ung laki ng frame w/ this text
        title.setFont(new Font("Arial", Font.BOLD, 30));

        this.playerPerformance  = new JButton("Player Performance >");
        playerPerformance.setForeground(Color.WHITE);
        playerPerformance.setFont(new Font("Arial", Font.BOLD, 15));
        playerPerformance.setBackground(new Color(102, 51, 0));
        playerPerformance.setSize(500,500);
        playerPerformance.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playerPerformance.setFocusable(false);

        this.catReport = new JButton("Cat Report >");
        catReport.setForeground(Color.WHITE);
        catReport.setFont(new Font("Arial", Font.BOLD, 15));
        catReport.setBackground(new Color(51, 25, 0));
        catReport.setSize(500,500);
        catReport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        catReport.setFocusable(false);

        this.eatReport = new JButton("Food Report >");
        eatReport.setForeground(Color.WHITE);
        eatReport.setFont(new Font("Arial", Font.BOLD, 15));
        eatReport.setBackground(new Color(119, 73, 27));
        eatReport.setSize(500,500);
        eatReport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eatReport.setFocusable(false);

        this.furnitureReport = new JButton("Furniture Report >");
        furnitureReport.setForeground(Color.WHITE);
        furnitureReport.setFont(new Font("Arial", Font.BOLD, 15));
        furnitureReport.setBackground(new Color(216,205,102));
        furnitureReport.setSize(500,500);
        furnitureReport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        furnitureReport.setFocusable(false);

        this.fold = new JButton("Return X");
        fold.setForeground(Color.WHITE);
        fold.setFont(new Font("Arial", Font.BOLD, 15));
        fold.setBackground(new Color(206,131,73));
        fold.setSize(500,500);

        panel.add(title);

        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(playerPerformance);
        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(catReport);
        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(eatReport);
        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(furnitureReport);
        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(fold);

        reportList.add(panel);

        reportList.setSize(400,250);
        reportList.setLocationRelativeTo(null);
        reportList.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        reportList.pack();
        reportList.setVisible(true);


        // Allow buttons to process reports

        playerPerformance.addActionListener(e -> {

            pStats(curUser);
            reportList.setVisible(false);
        });

        catReport.addActionListener(e -> {
            cStats(curUser);
            reportList.setVisible(false);
        });
        eatReport.addActionListener(e -> {
            eStats(curUser);
            reportList.setVisible(false);
        });
        furnitureReport.addActionListener(e ->{
            fStats(curUser);
            reportList.setVisible(false);

        } );

        fold.addActionListener(e -> reportList.dispose());

        fold.setFocusable(false);



    }

}
