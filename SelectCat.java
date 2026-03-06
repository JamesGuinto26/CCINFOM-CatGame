import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SelectCat extends JFrame {

    // Private Methods
    private JButton catSelection;
    private JButton catOwned;
    private JButton exit;
    private JButton forward;
    private JButton back;
    private JFrame frame;
    private JPanel selection;
    private JPanel main;
    private CatData catData;


    public void Selection(User curUser) {
        JFrame catFrame = new JFrame("Cat menu");
        catFrame.setUndecorated(true);
        catFrame.setResizable(false);
        catFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        catFrame.setLayout(new BorderLayout(20, 20));

        JPanel backgroundPanel = new JPanel(new BorderLayout(20, 20));
        backgroundPanel.setBackground(new Color(255, 255, 200));
        backgroundPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2), BorderFactory.createEmptyBorder(25, 25, 10, 25)));

        JLabel label = new JLabel("Select what to buy:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 19));
        label.setForeground(Color.BLACK);
        backgroundPanel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        buttonPanel.setOpaque(false);

        this.catSelection = styleButton("Cat Types");
        catSelection.setBackground(new Color(102, 51, 0));

        this.catOwned = styleButton("Cats owned");
        catOwned.setBackground(new Color(51, 25, 0));

        this.exit = styleButton("Cancel");
        exit.setBackground(new Color(119, 73, 27));

        buttonPanel.add(catSelection);
        buttonPanel.add(catOwned);
        buttonPanel.add(exit);

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        catFrame.add(backgroundPanel);

        catSelection.addActionListener(e -> {
            cats(curUser, catData);
            catFrame.dispose();
        });
        catOwned.addActionListener(e -> {
            catsOnProperty(curUser, catData);
            catFrame.dispose();
        });
        exit.addActionListener(e -> catFrame.dispose());


        catFrame.setSize(450, 180);
        catFrame.setLocationRelativeTo(null);
        catFrame.setVisible(true);

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

    // Show cats available in the db
    public void cats(User user, CatData catData) {
        // Image list of Cats
        String[] catImages = {
                "images/Cat_Designs/BS_G.png", "images/Cat_Designs/B_O.png", "images/Cat_Designs/S_CS.png",
                "images/Cat_Designs/P_MAM.png", "images/Cat_Designs/MC_BT.png", "images/Cat_Designs/R_M.png",
                "images/Cat_Designs/DC_BL.png", "images/Cat_Designs/R_SB.png", "images/Cat_Designs/B_SR.png",
                "images/Cat_Designs/P_S.png", "images/Cat_Designs/MC_BS.png", "images/Cat_Designs/S_CPK.png",
                "images/Cat_Designs/BS_B.png", "images/Cat_Designs/MC_PG.png", "images/Cat_Designs/BS_C.png",
                "images/Cat_Designs/S_CPA.png", "images/Cat_Designs/DC_T.png", "images/Cat_Designs/B_MP.png",
                "images/Cat_Designs/P_MAF.png", "images/Cat_Designs/R_BM.png" };

        // Fetch all cats from database
        String query = "SELECT * FROM Cat ORDER BY id";
        java.util.List<java.util.Map<String, Object>> catList = new java.util.ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                java.util.Map<String, Object> cat = new java.util.HashMap<>();
                cat.put("id", rs.getInt("id"));
                cat.put("name", rs.getString("name"));
                cat.put("gender", rs.getString("gender"));
                cat.put("life_stage", rs.getString("life_stage"));
                cat.put("breed", rs.getString("breed"));
                cat.put("color", rs.getString("color"));
                cat.put("personality", rs.getString("personality"));
                cat.put("description", rs.getString("description"));
                catList.add(cat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading cats: " + e.getMessage());
            return;
        }

        // Check if there are cats in database
        if (catList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No cats found in database!");
            return;
        }

        // Track current cat index
        final int[] currentIndex = {0};

        // Create the JFrame
        this.frame = new JFrame("Cat's Slideshow");
        frame.setResizable(false);
        frame.setUndecorated(true);

        this.main = new JPanel(new BorderLayout());
        main.setBackground(new Color(255, 255, 200));
        main.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel format = new JPanel(new GridBagLayout());
        format.setBackground(new Color(255, 255, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3,3,3,3);
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;

        // Filler Spaces
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel Spaces = new JLabel("               ");
        format.add(Spaces,gbc);

        // Image of Cat
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Use image based on current index (cycle through available images)
        int imageIndex = currentIndex[0] % catImages.length;
        ImageIcon catImage = resizeImages(catImages[imageIndex]);
        JLabel image = new JLabel(catImage);
        format.add(image, gbc);

        // Text information about cat
        gbc.gridx = 0;
        gbc.gridy = 1;

        JPanel general = new JPanel();
        general.setBackground(new Color(255, 255, 200));
        general.setBounds(100,100,200,1000);
        general.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JLabel catName = new JLabel("Name: " + catList.get(currentIndex[0]).get("name"));
        catName.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel id = new JLabel("ID: " + catList.get(currentIndex[0]).get("id"));
        id.setFont(new Font("Arial", Font.BOLD, 20));

        general.add(catName);
        general.add(id);

        format.add(general, gbc);

        JPanel info = new JPanel(new GridBagLayout());
        info.setBackground(new Color(255, 255, 200));
        GridBagConstraints dos = new GridBagConstraints();
        dos.insets = new Insets(10,10,10,10);
        dos.anchor = GridBagConstraints.BASELINE_LEADING;

        dos.gridx = 0;
        dos.gridy = 0;

        JLabel gender = new JLabel("Gender: " + catList.get(currentIndex[0]).get("gender"));
        gender.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(gender, dos);

        dos.gridy = 1;
        JLabel life_stage = new JLabel("Life Stage: " + catList.get(currentIndex[0]).get("life_stage"));
        life_stage.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(life_stage, dos);

        dos.gridy = 2;
        JLabel breed = new JLabel("Breed: " + catList.get(currentIndex[0]).get("breed"));
        breed.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(breed,dos);

        dos.gridy = 3;
        JLabel color = new JLabel("Color: " + catList.get(currentIndex[0]).get("color"));
        color.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(color,dos);

        dos.gridy = 4;
        JLabel personality = new JLabel("Personality: " + catList.get(currentIndex[0]).get("personality"));
        personality.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(personality,dos);

        dos.gridy = 5;
        JLabel desc = new JLabel("Description: " + catList.get(currentIndex[0]).get("description"));
        desc.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(desc,dos);

        gbc.gridx = 2;
        gbc.gridy = 0;
        format.add(info, gbc);

        // Buttons for slideshow
        this.selection = new JPanel();
        selection.setBackground(new Color(255, 255, 200));
        selection.setBounds(200,200,200,1000);
        selection.setLayout(new FlowLayout(FlowLayout.CENTER, 120, 10));

        this.back = new JButton(" < ");
        back.setSize(200,200);
        back.setForeground(Color.WHITE);
        back.setBackground(new Color(102, 51, 0));
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.setFocusable(false);
        // Back button action listener
        back.addActionListener(e -> {
            currentIndex[0] = (currentIndex[0] - 1 + catList.size()) % catList.size();

            // Update image (cycle through cat images)
            int imgIndex = currentIndex[0] % catImages.length;
            image.setIcon(resizeImages(catImages[imgIndex]));

            // Update cat information from database
            java.util.Map<String, Object> currentCat = catList.get(currentIndex[0]);
            catName.setText("Name: " + currentCat.get("name"));
            id.setText("ID: " + currentCat.get("id"));
            gender.setText("Gender: " + currentCat.get("gender"));
            life_stage.setText("Life Stage: " + currentCat.get("life_stage"));
            breed.setText("Breed: " + currentCat.get("breed"));
            color.setText("Color: " + currentCat.get("color"));
            personality.setText("Personality: " + currentCat.get("personality"));
            desc.setText("Description: " + currentCat.get("description"));

            frame.revalidate();
            frame.repaint();
        });

        this.exit = new JButton("Cancel");
        exit.setSize(200,200);
        exit.setBackground(new Color(51, 25, 0));
        exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exit.setForeground(Color.WHITE);
        exit.setFocusable(false);
        exit.addActionListener(e -> frame.dispose());

        this.forward = new JButton(" > ");
        forward.setSize(200,200);
        forward.setBackground(new Color(119, 73, 27));
        forward.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forward.setForeground(Color.WHITE);
        forward.setFocusable(false);
        // Forward button action listener
        forward.addActionListener(e -> {
            currentIndex[0] = (currentIndex[0] + 1) % catList.size();

            // Update image (cycle through cat images)
            int imgIndex = currentIndex[0] % catImages.length;
            image.setIcon(resizeImages(catImages[imgIndex]));

            // Update cat information from database
            java.util.Map<String, Object> currentCat = catList.get(currentIndex[0]);
            catName.setText("Name: " + currentCat.get("name"));
            id.setText("ID: " + currentCat.get("id"));
            gender.setText("Gender: " + currentCat.get("gender"));
            life_stage.setText("Life Stage: " + currentCat.get("life_stage"));
            breed.setText("Breed: " + currentCat.get("breed"));
            color.setText("Color: " + currentCat.get("color"));
            personality.setText("Personality: " + currentCat.get("personality"));
            desc.setText("Description: " + currentCat.get("description"));

            frame.revalidate();
            frame.repaint();
        });

        selection.add(back);
        selection.add(exit);
        selection.add(forward);

        main.add(format, BorderLayout.WEST);
        main.add(selection, BorderLayout.SOUTH);

        this.frame.add(main);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(true);
        this.frame.setSize(550,450);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    private ImageIcon resizeImages(String imagePath) {
        ImageIcon cats = new ImageIcon(imagePath);
        int h = cats.getIconHeight() / 8;
        int w = cats.getIconWidth() / 6;
        Image scaled = cats.getImage().getScaledInstance(h, w, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }


    // TO IMPLEMENT: Show cats present on the cats property
    public void catsOnProperty (User user, CatData catData)
    {
        // Image list of Cats
        String[] catImages = {
                "images/Cat_Designs/BS_G.png", "images/Cat_Designs/B_O.png", "images/Cat_Designs/S_CS.png",
                "images/Cat_Designs/P_MAM.png", "images/Cat_Designs/MC_BT.png", "images/Cat_Designs/R_M.png",
                "images/Cat_Designs/DC_BL.png", "images/Cat_Designs/R_SB.png", "images/Cat_Designs/B_SR.png",
                "images/Cat_Designs/P_S.png", "images/Cat_Designs/MC_BS.png", "images/Cat_Designs/S_CPK.png",
                "images/Cat_Designs/BS_B.png", "images/Cat_Designs/MC_PG.png", "images/Cat_Designs/BS_C.png",
                "images/Cat_Designs/S_CPA.png", "images/Cat_Designs/DC_T.png", "images/Cat_Designs/B_MP.png",
                "images/Cat_Designs/P_MAF.png", "images/Cat_Designs/R_BM.png" };

        // Fetch all cats from database
        String query = "SELECT * FROM Cat ORDER BY id";
        java.util.List<java.util.Map<String, Object>> catList = new java.util.ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                java.util.Map<String, Object> cat = new java.util.HashMap<>();
                cat.put("id", rs.getInt("id"));
                cat.put("name", rs.getString("name"));
                cat.put("gender", rs.getString("gender"));
                cat.put("life_stage", rs.getString("life_stage"));
                cat.put("breed", rs.getString("breed"));
                cat.put("color", rs.getString("color"));
                cat.put("personality", rs.getString("personality"));
                cat.put("description", rs.getString("description"));
                catList.add(cat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading cats: " + e.getMessage());
            return;
        }

        // Check if there are cats in database
        if (catList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No cats found in database!");
            return;
        }

        // Track current cat index
        final int[] currentIndex = {0};

        // Create the JFrame
        this.frame = new JFrame("Cat's Slideshow");
        frame.setResizable(false);
        frame.setUndecorated(true);

        this.main = new JPanel(new BorderLayout());
        main.setBackground(new Color(255, 255, 200));
        main.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel format = new JPanel(new GridBagLayout());
        format.setBackground(new Color(255, 255, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3,3,3,3);
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;

        // Filler Spaces
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel Spaces = new JLabel("               ");
        format.add(Spaces,gbc);

        // Image of Cat
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Use image based on current index (cycle through available images)
        int imageIndex = currentIndex[0] % catImages.length;
        ImageIcon catImage = resizeImages(catImages[imageIndex]);
        JLabel image = new JLabel(catImage);
        format.add(image, gbc);

        // Text information about cat
        gbc.gridx = 0;
        gbc.gridy = 1;

        JPanel general = new JPanel();
        general.setBackground(new Color(255, 255, 200));
        general.setBounds(100,100,200,1000);
        general.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JLabel catName = new JLabel("Name: " + catList.get(currentIndex[0]).get("name"));
        catName.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel id = new JLabel("ID: " + catList.get(currentIndex[0]).get("id"));
        id.setFont(new Font("Arial", Font.BOLD, 20));

        general.add(catName);
        general.add(id);

        format.add(general, gbc);

        JPanel info = new JPanel(new GridBagLayout());
        info.setBackground(new Color(255, 255, 200));
        GridBagConstraints dos = new GridBagConstraints();
        dos.insets = new Insets(10,10,10,10);
        dos.anchor = GridBagConstraints.BASELINE_LEADING;

        dos.gridx = 0;
        dos.gridy = 0;

        JLabel gender = new JLabel("Gender: " + catList.get(currentIndex[0]).get("gender"));
        gender.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(gender, dos);

        dos.gridy = 1;
        JLabel life_stage = new JLabel("Life Stage: " + catList.get(currentIndex[0]).get("life_stage"));
        life_stage.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(life_stage, dos);

        dos.gridy = 2;
        JLabel breed = new JLabel("Breed: " + catList.get(currentIndex[0]).get("breed"));
        breed.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(breed,dos);

        dos.gridy = 3;
        JLabel color = new JLabel("Color: " + catList.get(currentIndex[0]).get("color"));
        color.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(color,dos);

        dos.gridy = 4;
        JLabel personality = new JLabel("Personality: " + catList.get(currentIndex[0]).get("personality"));
        personality.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(personality,dos);

        dos.gridy = 5;
        JLabel desc = new JLabel("Description: " + catList.get(currentIndex[0]).get("description"));
        desc.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(desc,dos);

        dos.gridy = 6;
        // Check if player owns this cat
        boolean isOwned = checkCatOwnership(user, (Integer) catList.get(currentIndex[0]).get("id"));
        JLabel catPresent = new JLabel();
        if (isOwned) {
            catPresent.setText("Cat Present: Yes");
            catPresent.setForeground(new Color(0, 128, 0));
        } else {
            catPresent.setText("Cat Present: No");
            catPresent.setForeground(new Color(178, 34, 34));
        }
        catPresent.setFont(new Font("Arial", Font.BOLD, 15));
        info.add(catPresent, dos);

        gbc.gridx = 2;
        gbc.gridy = 0;
        format.add(info, gbc);

        // Buttons for slideshow
        this.selection = new JPanel();
        selection.setBackground(new Color(255, 255, 200));
        selection.setBounds(200,200,200,1000);
        selection.setLayout(new FlowLayout(FlowLayout.CENTER, 120, 10));

        this.back = new JButton(" < ");
        back.setSize(200,200);
        back.setForeground(Color.WHITE);
        back.setBackground(new Color(102, 51, 0));
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.setFocusable(false);
        // Back button action listener
        back.addActionListener(e -> {
            currentIndex[0] = (currentIndex[0] - 1 + catList.size()) % catList.size();

            // Update image (cycle through cat images)
            int imgIndex = currentIndex[0] % catImages.length;
            image.setIcon(resizeImages(catImages[imgIndex]));

            // Update cat information from database
            java.util.Map<String, Object> currentCat = catList.get(currentIndex[0]);
            catName.setText("Name: " + currentCat.get("name"));
            id.setText("ID: " + currentCat.get("id"));
            gender.setText("Gender: " + currentCat.get("gender"));
            life_stage.setText("Life Stage: " + currentCat.get("life_stage"));
            breed.setText("Breed: " + currentCat.get("breed"));
            color.setText("Color: " + currentCat.get("color"));
            personality.setText("Personality: " + currentCat.get("personality"));
            desc.setText("Description: " + currentCat.get("description"));

            // Update ownership status
            boolean owned = checkCatOwnership(user, (Integer) currentCat.get("id"));
            if (owned) {
                catPresent.setText("Cat Present: Yes");
                catPresent.setForeground(new Color(0, 128, 0));
            } else {
                catPresent.setText("Cat Present: No");
                catPresent.setForeground(new Color(178, 34, 34));
            }

            frame.revalidate();
            frame.repaint();
        });

        this.exit = new JButton("Cancel");
        exit.setSize(200,200);
        exit.setBackground(new Color(51, 25, 0));
        exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exit.setForeground(Color.WHITE);
        exit.setFocusable(false);
        exit.addActionListener(e -> frame.dispose());

        this.forward = new JButton(" > ");
        forward.setSize(200,200);
        forward.setBackground(new Color(119, 73, 27));
        forward.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forward.setForeground(Color.WHITE);
        forward.setFocusable(false);
        // Forward button action listener
        forward.addActionListener(e -> {
            currentIndex[0] = (currentIndex[0] + 1) % catList.size();

            // Update image (cycle through cat images)
            int imgIndex = currentIndex[0] % catImages.length;
            image.setIcon(resizeImages(catImages[imgIndex]));

            // Update cat information from database
            java.util.Map<String, Object> currentCat = catList.get(currentIndex[0]);
            catName.setText("Name: " + currentCat.get("name"));
            id.setText("ID: " + currentCat.get("id"));
            gender.setText("Gender: " + currentCat.get("gender"));
            life_stage.setText("Life Stage: " + currentCat.get("life_stage"));
            breed.setText("Breed: " + currentCat.get("breed"));
            color.setText("Color: " + currentCat.get("color"));
            personality.setText("Personality: " + currentCat.get("personality"));
            desc.setText("Description: " + currentCat.get("description"));

            // Update ownership status
            boolean owned = checkCatOwnership(user, (Integer) currentCat.get("id"));
            if (owned) {
                catPresent.setText("Cat Present: Yes");
                catPresent.setForeground(new Color(0, 128, 0));
            } else {
                catPresent.setText("Cat Present: No");
                catPresent.setForeground(new Color(178, 34, 34));
            }

            frame.revalidate();
            frame.repaint();
        });

        selection.add(back);
        selection.add(exit);
        selection.add(forward);

        main.add(format, BorderLayout.WEST);
        main.add(selection, BorderLayout.SOUTH);

        this.frame.add(main);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(true);
        this.frame.setSize(550,450);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    private boolean checkCatOwnership(User user, int catId) {
        String query = "SELECT COUNT(*) FROM owned_cat WHERE player_id = ? AND cat_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            int user_id = Integer.parseInt(user.getIdNumber());

            ps.setInt(1, user_id);
            ps.setInt(2, catId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking cat ownership: " + e.getMessage());
        }

        return false;
    }

 }


