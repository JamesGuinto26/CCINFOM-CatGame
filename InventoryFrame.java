import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class InventoryFrame extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private JScrollPane scroll;

    public InventoryFrame(User currentUser) {
        this.currentUser = currentUser;

        setTitle("Inventory");
        setSize(500, 450); // smaller frame
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); // no title bar / X button
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(new Color(255, 255, 200));
        contentPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columns, auto rows

        scroll = new JScrollPane(contentPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // for gaps
        buttonPanel.setBackground(new Color(102, 51, 0));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        buttonPanel.setPreferredSize(new Dimension(0, 50));

        JButton foodButton = new JButton("Food");
        foodButton.setBackground(new Color(232, 142, 52));
        foodButton.setForeground(Color.BLACK);
        foodButton.setFont(new Font("Arial", Font.BOLD, 14));
        foodButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        foodButton.setPreferredSize(new Dimension(60, 30));
        foodButton.setFocusPainted(false);
        foodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton furnitureButton = new JButton("Furniture");
        furnitureButton.setBackground(new Color(232, 142, 52));
        furnitureButton.setForeground(Color.BLACK);
        furnitureButton.setFont(new Font("Arial", Font.BOLD, 14));
        furnitureButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        furnitureButton.setPreferredSize(new Dimension(80, 30));
        furnitureButton.setFocusPainted(false);
        furnitureButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(232, 142, 52));
        exitButton.setForeground(Color.BLACK);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        exitButton.setPreferredSize(new Dimension(60, 30));
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(foodButton);
        buttonPanel.add(furnitureButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        foodButton.addActionListener(e -> displayOwnedFood());
        furnitureButton.addActionListener(e -> displayOwnedFurniture());
        exitButton.addActionListener(e -> dispose());

        // show furniture by default
        displayOwnedFurniture();

        setVisible(true);
    }


    public void displayOwnedFurniture() {
        contentPanel.removeAll();
        FurnitureSystem sys = new FurnitureSystem();

        ArrayList<OwnedFurniture> ownedFurnitures = PlayerInventory.loadFurnitureInventory(currentUser);
        ArrayList<Furniture> allFurnitures = sys.getAllFurnitures();

        for (OwnedFurniture owned : ownedFurnitures) {
            Furniture furniture = null;

            for (Furniture f : allFurnitures) {
                if (f.getName().equals(owned.getName())) {
                    furniture = f;
                    break;
                }
            }

            JPanel itemPanel = createItemPanel(furniture.getName(), owned.getQuantity(), furniture.getImage());
            contentPanel.add(itemPanel);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void displayOwnedFood() {
        contentPanel.removeAll();
        FoodSystem sys = new FoodSystem();

        ArrayList<OwnedFood> ownedFoods = PlayerInventory.loadFoodInventory(currentUser);
        ArrayList<Food> allFoods = sys.getAllFoods();

        for (OwnedFood owned : ownedFoods) {
            Food food = null;

            for (Food f : allFoods) {
                if (f.getName().equals(owned.getName())) {
                    food = f;
                    break;
                }
            }

            JPanel itemPanel = createItemPanel(food.getName(), owned.getQuantity(), food.getImage());
            contentPanel.add(itemPanel);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createItemPanel(String name, int quantity, ImageIcon image) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setPreferredSize(new Dimension(150, 180));
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        itemPanel.setBackground(Color.WHITE);

        Image scaledImage = image.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(scaledImage));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setOpaque(true);
        imgLabel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(name + " x" + quantity, SwingConstants.CENTER);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        nameLabel.setOpaque(true);
        nameLabel.setBackground(new Color(102, 51, 0));
        nameLabel.setForeground(Color.WHITE);

        itemPanel.add(imgLabel, BorderLayout.CENTER);
        itemPanel.add(nameLabel, BorderLayout.SOUTH);

        return itemPanel;
    }
}
