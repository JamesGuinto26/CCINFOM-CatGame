import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CatPopup {
    private GameFrame frame;
    private User currentUser;
    private OwnedCat catToFeed;

    public CatPopup(GameFrame frame, User currentUser, OwnedCat catToFeed) {
        this.frame = frame;
        this.currentUser = currentUser;
        this.catToFeed = catToFeed;
        showPopup();
    }

    public void showPopup() {
        JFrame frame = new JFrame("Cat Popup Menu");
        frame.setSize(300, 300);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        frame.getContentPane().setBackground(new Color(255, 255, 200));

        // Label
        JLabel label = new JLabel("Cat Popup Menu", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(Color.BLACK);
        frame.add(label, BorderLayout.NORTH);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));

        // Button panel with vertical layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 200));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Feed Cat button
        JButton feedButton = new JButton("Feed Cat");
        feedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedButton.setBackground(new Color(102, 51, 0));
        feedButton.setForeground(Color.WHITE);
        feedButton.setFocusPainted(false);
        feedButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        feedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        feedButton.setMaximumSize(new Dimension(200, 40));
        feedButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        feedButton.addActionListener(e-> {
            frame.dispose();
            showFoodSelection();
        });

        // Play with Cat button
        JButton playButton = new JButton("Play with Cat");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setBackground(new Color(155, 84, 14));
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playButton.setMaximumSize(new Dimension(200, 40));
        playButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        playButton.addActionListener(e -> {
            showPlayWithCatPopup(catToFeed);
            frame.dispose();
        });

        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setBackground(new Color(204, 102, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setMaximumSize(new Dimension(200, 40));
        cancelButton.addActionListener(e -> frame.dispose());
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cancelButton.addActionListener(e -> frame.dispose());

        // Add buttons to panel with spacing
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(feedButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(playButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createVerticalStrut(15));

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void showFoodSelection() {
        JFrame feedPopup = new JFrame("Select Food");
        feedPopup.setSize(500, 450);
        feedPopup.setLocationRelativeTo(frame);
        feedPopup.setLayout(new BorderLayout());
        feedPopup.setResizable(true);
        feedPopup.setUndecorated(true);

        JPanel contentPanel = new JPanel(new GridLayout(0, 3, 10, 3));
        contentPanel.setBackground(new Color(255, 255, 200));

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        feedPopup.add(scroll, BorderLayout.CENTER);

        ArrayList<OwnedFood> ownedFoods = currentUser.getFoodInventory();

        if (ownedFoods == null || ownedFoods.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "You don't have any food.", "No Food", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        FoodSystem sys = new FoodSystem();
        ArrayList<Food> allFoods = sys.getAllFoods();

        boolean foodShown = false;

        for (OwnedFood owned : ownedFoods) {
            if (owned == null || owned.getQuantity() <= 0) continue;

            Food food = findFoodByName(allFoods, owned.getName());

            if (food == null) continue;

            foodShown = true;
            JPanel itemPanel = createItemPanel(food.getName(), food.getImage(), owned.getQuantity());

            itemPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    feedCatMethod(food);
                    feedPopup.dispose();
                }
            });

            contentPanel.add(itemPanel);
        }

        if (!foodShown) {
            JOptionPane.showMessageDialog(frame, "No food available to feed (all quantities are zero).", "No Food", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        feedPopup.setVisible(true);
    }

    public Food findFoodByName(ArrayList<Food> list, String name) {
        if (list == null || name == null) return null;
        for (Food f : list) {
            if (f != null && name.equals(f.getName())) {
                return f;
            }
        }
        return null;
    }

    public JPanel createItemPanel(String name, ImageIcon image, int quantity) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.BLACK));
        panel.setBackground(Color.WHITE);

        JLabel imgLabel = new JLabel(new ImageIcon(image.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel(name + " x" + quantity, SwingConstants.CENTER);
        nameLabel.setOpaque(true);
        nameLabel.setBackground(new Color(102, 51, 0));
        nameLabel.setForeground(Color.WHITE);

        panel.add(imgLabel, BorderLayout.CENTER);
        panel.add(nameLabel, BorderLayout.SOUTH);

        return panel;
    }

    public void feedCatMethod(Food food) {

        if(catIsHungry()) {
            feedCat(catToFeed, food);
            updateCatSQL(catToFeed);
        } else {
            JOptionPane.showMessageDialog(frame, "The cat isn't hungry yet.", "Not Hungry", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ArrayList<OwnedFood> owned = currentUser.getFoodInventory();

        for (int i = 0; i < owned.size(); i++) {
            OwnedFood ownedF = owned.get(i);
            if (ownedF.getName().equals(food.getName())) {
                int newQty = ownedF.getQuantity() - 1;

                if (newQty <= 0) {
                    owned.remove(i);
                } else {
                    ownedF.setQuantity(newQty); // decrease food quantity (JAVA only)
                }
                break;
            }
        }
    }

    public boolean catIsHungry() {
        return catToFeed.getHunger() >=  25;
    }

    public void feedCat(OwnedCat cat, Food food) {
        int hungerResult = cat.getHunger() - food.getSaturationValue();

        if(hungerResult < 0) {
            cat.setHunger(0);
        } else {
            cat.setHunger(hungerResult);
        }

        int affectionResult = cat.getAffectionPoints() + 10;

        if(affectionResult > 100) {
            cat.setAffectionPoints(100);
        } else {
            cat.setAffectionPoints(affectionResult);
        }
    }

    public void updateCatSQL(OwnedCat fedCat) {
        String query = "UPDATE Owned_Cat SET affection_points = ?, hunger = ?, "
                + "WHERE id = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, fedCat.getAffectionPoints());
            ps.setInt(2, fedCat.getHunger());
            ps.setInt(3, fedCat.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showPlayWithCatPopup(OwnedCat cat) {
        JFrame frame = new JFrame("Play with Cat");
        frame.setSize(300, 200);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        frame.getContentPane().setBackground(new Color(255, 255, 200));

        int affectionResult = cat.getAffectionPoints() + 10;

        if (affectionResult > 100) {
            affectionResult = 100;
        }

        cat.setAffectionPoints(affectionResult);
        updateAffectionSQL(cat);

        // Main message
        JLabel messageLabel = new JLabel("You played with " + cat.getCat().getName() + "!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 0, 10));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // +10 points label (RED)
        JLabel plusPointsLabel = new JLabel("+10 Affection Points!", SwingConstants.CENTER);
        plusPointsLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        plusPointsLabel.setForeground(new Color(200, 20, 0)); // red
        plusPointsLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        plusPointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Affection value
        JLabel affectionLabel = new JLabel("Affection: " + cat.getAffectionPoints() + " / 100", SwingConstants.CENTER);
        affectionLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        affectionLabel.setForeground(Color.BLACK);
        affectionLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));
        affectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBackground(new Color(255, 255, 200));
        labelPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        labelPanel.add(messageLabel);
        labelPanel.add(Box.createVerticalStrut(8));
        labelPanel.add(plusPointsLabel);
        labelPanel.add(Box.createVerticalStrut(8));
        labelPanel.add(affectionLabel);

        frame.add(labelPanel, BorderLayout.CENTER);

        // OK button
        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(102, 51, 0));
        okButton.setPreferredSize(new Dimension(120, 40));
        okButton.setForeground(Color.WHITE);
        okButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> frame.dispose());
        okButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 200));
        buttonPanel.add(okButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void updateAffectionSQL(OwnedCat catPlaymate) {
        String query = "UPDATE Owned_Cat SET affection_points = ?, hunger = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, catPlaymate.getAffectionPoints());
            ps.setInt(2, catPlaymate.getHunger());
            ps.setInt(3, catPlaymate.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}