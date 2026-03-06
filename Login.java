import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame
{
    private JTextField username;
    private JTextField idNumber;
    private JButton loginButton;
    private JButton exitButton;

    public Login()
    {
        ImageIcon frameIcon = new ImageIcon("images/catIcon.png");

        this.setIconImage(frameIcon.getImage());
        this.setTitle("Login Page");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setUndecorated(true);

        ImageIcon icon = new ImageIcon("images/kittyCo.png");
        Image scaledImage = icon.getImage().getScaledInstance(350, 400, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

        JLayeredPane imageLayer = new JLayeredPane();
        imageLayer.setPreferredSize(new Dimension(350, 400));

        JLabel imageLabel = new JLabel(scaledImageIcon);
        imageLabel.setBounds(0, 0, 350, 400);
        imageLayer.add(imageLabel, JLayeredPane.DEFAULT_LAYER);

        ImageIcon dbIcon = new ImageIcon("images/setting.png");
        Image btnScaled = dbIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon btnFinal = new ImageIcon(btnScaled);

        JButton dbButton = new JButton(btnFinal);
        dbButton.setBounds(10, 310, 40, 40);
        dbButton.setBackground(new Color(255, 255, 200));
        dbButton.setOpaque(true);
        dbButton.setContentAreaFilled(true);
        dbButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        dbButton.addActionListener(e -> dbOptionPopUp());

        dbButton.setBorder(new LineBorder(Color.BLACK, 1));
        dbButton.setFocusPainted(false);

        imageLayer.add(dbButton, JLayeredPane.PALETTE_LAYER);
        this.add(imageLayer, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(new Color(255, 255, 200));

        GridBagConstraints alignFormat = new GridBagConstraints();
        alignFormat.insets = new Insets(15, 10, 15, 10);
        alignFormat.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(new Color(51, 25, 0));
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        alignFormat.gridx = 0;
        alignFormat.gridy = 0;
        rightPanel.add(usernameLabel, alignFormat);

        username = new JTextField(15);
        username.setBorder(new LineBorder(Color.BLACK, 2));
        username.setFont(new Font("SansSerif", Font.PLAIN, 13));
        username.setPreferredSize(new Dimension(200, 30));
        alignFormat.gridx = 1;
        alignFormat.gridy = 0;
        rightPanel.add(username, alignFormat);

        JLabel idLabel = new JLabel("ID Number:");
        idLabel.setForeground(new Color(51, 25, 0));
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        alignFormat.gridx = 0;
        alignFormat.gridy = 1; // below username
        rightPanel.add(idLabel, alignFormat);

        idNumber = new JTextField(15);
        idNumber.setBorder(new LineBorder(Color.BLACK, 2));
        idNumber.setFont(new Font("SansSerif", Font.PLAIN, 13));
        idNumber.setPreferredSize(new Dimension(200, 30));
        alignFormat.gridx = 1;
        alignFormat.gridy = 1;
        rightPanel.add(idNumber, alignFormat);

        // for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(153, 76, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setBorder(new LineBorder(Color.BLACK, 2));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(102, 51, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        exitButton.setBorder(new LineBorder(Color.BLACK, 2));
        exitButton.setPreferredSize(new Dimension(120, 40));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.setFocusPainted(false);
        exitButton.setFocusPainted(false);

        buttonPanel.setBackground(new Color(255, 255, 200));
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        alignFormat.gridx = 0;
        alignFormat.gridy = 2;
        alignFormat.gridwidth = 2;
        rightPanel.add(buttonPanel, alignFormat);

        this.add(rightPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> loginUser());

        exitButton.addActionListener(e -> this.dispose());

        setVisible(true);
    }

    public void loginUser()
    {
        String user = username.getText().trim();
        String id = idNumber.getText().trim();
        String prompt = "";
        User currentUser;

        if (user.isEmpty() || id.isEmpty()) {
            prompt = "Enter a username and id number!";
        } else {
            currentUser = checkIfUserExist(user, id);

            if (currentUser != null) {
                prompt = "Login was successful!";

                PlacedFurnitureSQL sql = new PlacedFurnitureSQL();
                PlacedFurniture[] placed = PlacedFurnitureSQL.loadPlacedFurniture(currentUser);
                currentUser.setPlacedFurniture(placed);

                GameFrame frame = new GameFrame(currentUser);
                this.dispose();
            } else {
                prompt = "Username or ID number not found";
            }
        }
        new MessagePopup(this, prompt).setVisible(true);
        //JOptionPane.showMessageDialog(this, prompt);
    }

    public User checkIfUserExist(String username, String idNumber)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User currentUser = null;
        String statement;

        try {
            statement = "SELECT * FROM Player WHERE username = ? AND id = ?";
            connection = DBConnection.getConnection();

            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, idNumber);

            resultSet = preparedStatement.executeQuery();

            // Not sure if correct column/attribute names
            if(resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("username");
                String dob = resultSet.getString("date_of_birth");
                char gender = resultSet.getString("gender").charAt(0);

                currentUser = new User(id, name, dob, gender);
                PlayerInventory.loadMoney(currentUser);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(resultSet, preparedStatement, connection);
        }

        return currentUser;
    }

    public void dbOptionPopUp() {
        JFrame frame = new JFrame();
        frame.setSize(350, 300);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(new Color(255, 255, 200));

        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        frame.setLayout(null);

        JButton closeButton = new JButton("X");
        closeButton.setBounds(310, 10, 30, 30);
        closeButton.setFocusPainted(false);
        closeButton.setBackground(new Color(51, 25, 0));
        closeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> frame.dispose());

        frame.add(closeButton);

        JButton user = new JButton("User");
        user.setBounds(100, 70, 150, 40);
        user.setFocusPainted(false);
        user.setBackground(new Color(102, 51, 0));
        user.setFont(new Font("SansSerif", Font.BOLD, 14));
        user.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        user.setForeground(Color.WHITE);
        user.setCursor(new Cursor(Cursor.HAND_CURSOR));

        user.addActionListener(e-> {
            this.dispose();
            frame.dispose();
            CRUDPanelHolder(new PlayerData(), "Player Data", 600, 600);
        });
        frame.add(user);

        JButton cats = new JButton("Cats");
        cats.setBounds(100, 120, 150, 40);
        cats.setFocusPainted(false);
        cats.setBackground(new Color(155, 84, 14));
        cats.setFont(new Font("SansSerif", Font.BOLD, 14));
        cats.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cats.setForeground(Color.WHITE);
        cats.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cats.addActionListener(e-> {
            this.dispose();
            frame.dispose();
            CRUDPanelHolder(new CatData(), "Cat Data", 600, 600);
        });
        frame.add(cats);

        JButton foods = new JButton("Foods");
        foods.setBounds(100, 170, 150, 40);
        foods.setFocusPainted(false);
        foods.setBackground(new Color(153, 76, 0));
        foods.setFont(new Font("SansSerif", Font.BOLD, 14));
        foods.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        foods.setForeground(Color.WHITE);
        foods.setCursor(new Cursor(Cursor.HAND_CURSOR));

        foods.addActionListener(e-> {
            this.dispose();
            frame.dispose();
            CRUDPanelHolder(new FoodData(), "Food Data", 533, 488);
        });
        frame.add(foods);

        JButton furniture = new JButton("Furniture");
        furniture.setBounds(100, 220, 150, 40);
        furniture.setFocusPainted(false);
        furniture.setBackground(new Color(204, 102, 0));
        furniture.setFont(new Font("SansSerif", Font.BOLD, 14));
        furniture.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        furniture.setForeground(Color.WHITE);
        furniture.setCursor(new Cursor(Cursor.HAND_CURSOR));

        /*
        furniture.addActionListener(e-> {
            this.dispose();
            frame.dispose();
            CRUDPanelHolder(new FurnitureData(), "Furniture Data", 533, 488);
        });

         */

        frame.add(foods);
        frame.add(furniture);

        frame.setVisible(true);
    }

    // Reusable method to show any JPanel in a JFrame
    private void CRUDPanelHolder(JPanel panel, String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon frameIcon = new ImageIcon("images/catIcon.png");
        frame.setIconImage(frameIcon.getImage());
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setContentPane(panel);
        frame.setVisible(true);

        JButton closeButton = new JButton("X");
        closeButton.setBounds(width - 40, 10, 30, 30);
        closeButton.setFocusPainted(false);
        closeButton.setBackground(new Color(51, 25, 0));
        closeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        closeButton.addActionListener(e -> {
            frame.dispose();
            new Login().setVisible(true);
        });
        frame.add(closeButton);

        panel.setBounds(0, 0, width, height);
        frame.add(panel);

        frame.setVisible(true);
    }
}


