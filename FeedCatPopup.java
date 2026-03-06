import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class FeedCatPopup {
    private GameFrame frame;
    private User currentUser;
    private OwnedCat catToFeed;

    public FeedCatPopup(GameFrame frame, User currentUser, OwnedCat catToFeed) {
        this.frame = frame;
        this.currentUser = currentUser;
        this.catToFeed = catToFeed;
        showFeedConfirm();
    }

    public void showFeedConfirm() {
        JFrame frame = new JFrame();
        frame.setSize(300, 150);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        frame.getContentPane().setBackground(new Color(255, 255, 200));

        JLabel label = new JLabel("Feed " + catToFeed.getCat().getName() + "?", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        frame.add(label);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 255, 200));

        JButton yesButton = new JButton("Yes");
        yesButton.setBackground(new Color(153, 76, 0));
        yesButton.setForeground(Color.WHITE);
        yesButton.setFocusPainted(false);
        yesButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        yesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        yesButton.addActionListener(e -> {
            frame.dispose();
            showFoodSelection();
        });

        JButton noButton = new JButton("No");
        noButton.setBackground(new Color(102, 51, 0));
        noButton.setForeground(Color.WHITE);
        noButton.setFocusPainted(false);
        noButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        noButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        noButton.addActionListener(e -> frame.dispose());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
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
}
