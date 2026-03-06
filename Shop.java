import com.sun.source.tree.NewArrayTree;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
// TO DO: ACTION LISTENERS
public class Shop
{
    public void openShop(User currentUser, GameFrame gameFrame) {
        JFrame shopFrame = new JFrame("Shop Menu");
        shopFrame.setUndecorated(true);
        shopFrame.setResizable(false);
        shopFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        shopFrame.setLayout(new BorderLayout(20, 20));

        JPanel backgroundPanel = new JPanel(new BorderLayout(20, 20));
        backgroundPanel.setBackground(new Color(255, 255, 200));
        backgroundPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2), BorderFactory.createEmptyBorder(25, 25, 10, 25)));

        JLabel label = new JLabel("Select what to buy:", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 19));
        label.setForeground(Color.BLACK);
        backgroundPanel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        buttonPanel.setOpaque(false);

        JButton foodButton = styleButton("Food");
        foodButton.setBackground(new Color(102, 51, 0));

        JButton furnitureButton = styleButton("Furniture");
        furnitureButton.setBackground(new Color(51, 25, 0));

        JButton cancelButton = styleButton("Cancel");
        cancelButton.setBackground(new Color(119, 73, 27));

        buttonPanel.add(foodButton);
        buttonPanel.add(furnitureButton);
        buttonPanel.add(cancelButton);

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        shopFrame.add(backgroundPanel);

        foodButton.addActionListener(e -> shopFoods(currentUser, gameFrame));
        furnitureButton.addActionListener(e -> shopFurnitures(currentUser, gameFrame));
        cancelButton.addActionListener(e -> shopFrame.dispose());

        shopFrame.setSize(450, 180);
        shopFrame.setLocationRelativeTo(null);
        shopFrame.setVisible(true);
    }

    public JButton styleButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 35));
        button.setMargin(new Insets(15, 20, 15, 20));
        button.setFocusPainted(false);

        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public void shopFurnitures(User currentUser, GameFrame gameFrame) {
        FurnitureSystem furnitureSys = new FurnitureSystem();
        ArrayList<Furniture> furnitures = furnitureSys.getAllFurnitures();

        JFrame furnitureFrame = new JFrame();
        furnitureFrame.setUndecorated(true);
        furnitureFrame.setResizable(false);
        furnitureFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        furnitureFrame.setSize(600, 400);
        furnitureFrame.setLocationRelativeTo(null);
        furnitureFrame.setLayout(new BorderLayout(0, 0));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(255, 255, 200));
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));

        // THIS PART DONE
        JButton exitButton = new JButton("X");
        exitButton.setPreferredSize(new Dimension(30, 30));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(153, 76, 0));
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        exitButton.addActionListener(e -> furnitureFrame.dispose());
        topPanel.add(exitButton);

        furnitureFrame.add(topPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        contentPanel.setBackground(new Color(255, 255, 200));

        furnitureFrame.add(contentPanel, BorderLayout.CENTER);

        // Navigation panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        navPanel.setBackground(new Color(255, 255, 200));

        JButton prevButton = new JButton("<");
        prevButton.setBackground(new Color(153, 76, 0));
        prevButton.setPreferredSize(new Dimension(40, 25));
        prevButton.setForeground(Color.WHITE);
        prevButton.setFont(new Font("Arial", Font.BOLD, 14));
        prevButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        prevButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton nextButton = new JButton(">");
        nextButton.setBackground(new Color(153, 76, 0));
        nextButton.setPreferredSize(new Dimension(40, 25));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        navPanel.add(prevButton);
        navPanel.add(nextButton);
        furnitureFrame.add(navPanel, BorderLayout.SOUTH);

        final int[] currentPage = {0}; // each page shows 2 items

        Runnable showPage = () -> {
            contentPanel.removeAll();
            int start = currentPage[0] * 2;
            for (int i = start; i < start + 2 && i < furnitures.size(); i++) {
                contentPanel.add(createFurniturePanel(furnitures.get(i), currentUser, gameFrame));
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        };

        prevButton.addActionListener(e -> {
            if (currentPage[0] > 0) {
                currentPage[0]--;
                showPage.run();
            }
        });

        nextButton.addActionListener(e -> {
            if ((currentPage[0] + 1) * 2 < furnitures.size()) {
                currentPage[0]++;
                showPage.run();
            }
        });

        showPage.run();
        furnitureFrame.setVisible(true);
    }

    private JPanel createFurniturePanel(Furniture furniture, User currentUser, GameFrame gameFrame) {
        JPanel panel = new JPanel(new BorderLayout());
        Color bg = new Color(255, 255, 200);

        panel.setBackground(bg);
        panel.setOpaque(true);

        panel.setBorder(BorderFactory.createCompoundBorder( BorderFactory.createLineBorder(Color.BLACK, 2), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // for name
        JLabel nameLabel = new JLabel(furniture.getName().toUpperCase(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0)); // margin above & below
        nameLabel.setBackground(new Color(102, 51, 0));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setOpaque(true);
        panel.add(nameLabel, BorderLayout.NORTH);

        // for actual image
        ImageIcon icon = furniture.getImage();
        Image scaledImage = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // for image bg (since images are transparent)
        JPanel imageBG = new JPanel(new BorderLayout());
        imageBG.setBackground(Color.WHITE);
        imageBG.setOpaque(true);
        imageBG.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        imageBG.setPreferredSize(new Dimension(200, 160));
        imageBG.add(imageLabel, BorderLayout.CENTER);
        panel.add(imageBG, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(bg);
        bottomPanel.setOpaque(true);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // spacing

        // for type and price
        JPanel rowPanel = new JPanel(new GridLayout(1, 2));
        rowPanel.setBackground(bg);
        rowPanel.setOpaque(true);

        JLabel typeLabel = new JLabel("Type: " + furniture.getType());
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        typeLabel.setForeground(Color.BLACK);

        JLabel priceLabel = new JLabel("Price: $" + furniture.getPrice());
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setForeground(Color.BLACK);

        rowPanel.add(typeLabel);
        rowPanel.add(priceLabel);

        // for description
        JTextArea descriptionArea = new JTextArea(furniture.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(bg);
        descriptionArea.setOpaque(true);
        descriptionArea.setFont(new Font("SansSerif", Font.BOLD, 12));
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        // BUY BUTTON
        JButton buyButton = new JButton("Buy Item");
        buyButton.setFocusPainted(false);
        buyButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        buyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        buyButton.setForeground(Color.WHITE);
        buyButton.setBackground(new Color(102, 51, 0));
        buyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buyButton.addActionListener(e -> {
            purchaseFurniture(furniture, currentUser, gameFrame);
        });

        // Add to bottom panel
        bottomPanel.add(rowPanel, BorderLayout.NORTH);
        bottomPanel.add(descriptionArea, BorderLayout.CENTER);
        bottomPanel.add(buyButton, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void purchaseFurniture(Furniture furniture, User currentUser, GameFrame gameFrame) {
        JFrame frame = new JFrame();
        frame.setSize(280, 150);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(new Color(255, 255, 200));
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel label = new JLabel("Buy " + furniture.getName() + " for " + furniture.getPrice() + "?", SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        frame.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(255, 255, 200));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton yesButton = new JButton("Yes");
        yesButton.setPreferredSize(new Dimension(70, 30));
        yesButton.setBackground(new Color(102, 51, 0));
        yesButton.setForeground(Color.WHITE);
        yesButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        yesButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        yesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(70, 30));
        cancelButton.setBackground(new Color(153, 76, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        yesButton.setFocusPainted(false);
        cancelButton.setFocusPainted(false);
        buttonPanel.add(yesButton);
        buttonPanel.add(cancelButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        yesButton.addActionListener(e -> {
            boolean checkMoney = buyItemFurniture(furniture, furniture.getPrice(), currentUser, gameFrame);

            if (checkMoney) {
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame,"You don't have enough money!", "Not Enough Money", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    public boolean buyItemFurniture(Furniture furniture, double itemPrice, User currentUser, GameFrame gameFrame) {
        double currentMoney = currentUser.getCurrentMoney();
        if (itemPrice <= currentMoney) {
            currentMoney -= itemPrice;
            currentUser.setCurrentMoney(currentMoney); // handle updating of money inside
            PlayerInventory.updateMoney(currentUser);
            gameFrame.refreshMoney();
            PlayerInventory.addFurniture(currentUser, furniture, 1);
            return true;
        }
        return false;
    }

    public void shopFoods(User currentUser, GameFrame gameFrame) {
        FoodSystem foodSys = new FoodSystem();
        ArrayList<Food> foods = foodSys.getAllFoods();

        JFrame foodFrame = new JFrame();
        foodFrame.setUndecorated(true);
        foodFrame.setResizable(false);
        foodFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        foodFrame.setSize(600, 400);
        foodFrame.setLocationRelativeTo(null);
        foodFrame.setLayout(new BorderLayout(0, 0));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(255, 255, 200));
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));

        JButton exitButton = new JButton("X");
        exitButton.setPreferredSize(new Dimension(30, 30));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(153, 76, 0));
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        exitButton.addActionListener(e -> foodFrame.dispose());
        topPanel.add(exitButton);

        foodFrame.add(topPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        contentPanel.setBackground(new Color(255, 255, 200));

        foodFrame.add(contentPanel, BorderLayout.CENTER);

        // Navigation panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        navPanel.setBackground(new Color(255, 255, 200));

        JButton prevButton = new JButton("<");
        prevButton.setBackground(new Color(153, 76, 0));
        prevButton.setPreferredSize(new Dimension(40, 25));
        prevButton.setForeground(Color.WHITE);
        prevButton.setFont(new Font("Arial", Font.BOLD, 14));
        prevButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        prevButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton nextButton = new JButton(">");
        nextButton.setBackground(new Color(153, 76, 0));
        nextButton.setPreferredSize(new Dimension(40, 25));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        prevButton.setFocusPainted(false);
        nextButton.setFocusPainted(false);

        navPanel.add(prevButton);
        navPanel.add(nextButton);
        foodFrame.add(navPanel, BorderLayout.SOUTH);

        final int[] currentPage = {0}; // each page shows 2 items

        Runnable showPage = () -> {
            contentPanel.removeAll();
            int start = currentPage[0] * 2;
            for (int i = start; i < start + 2 && i < foods.size(); i++) {
                contentPanel.add(createFoodPanel(foods.get(i), currentUser, gameFrame));
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        };

        prevButton.addActionListener(e -> {
            if (currentPage[0] > 0) {
                currentPage[0]--;
                showPage.run();
            }
        });

        nextButton.addActionListener(e -> {
            if ((currentPage[0] + 1) * 2 < foods.size()) {
                currentPage[0]++;
                showPage.run();
            }
        });

        showPage.run();
        foodFrame.setVisible(true);
    }

    private JPanel createFoodPanel(Food food, User currentUser, GameFrame gameFrame) {
        JPanel panel = new JPanel(new BorderLayout());
        Color bg = new Color(255, 255, 200);

        panel.setBackground(bg);
        panel.setOpaque(true);

        panel.setBorder(BorderFactory.createCompoundBorder( BorderFactory.createLineBorder(Color.BLACK, 2), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // for name
        JLabel nameLabel = new JLabel(food.getName().toUpperCase(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0)); // margin above & below
        nameLabel.setBackground(new Color(102, 51, 0));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setOpaque(true);
        panel.add(nameLabel, BorderLayout.NORTH);

        // for actual image
        ImageIcon icon = food.getImage();
        Image scaledImage = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // for image bg (since images are transparent)
        JPanel imageBG = new JPanel(new BorderLayout());
        imageBG.setBackground(Color.WHITE);
        imageBG.setOpaque(true);
        imageBG.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        imageBG.setPreferredSize(new Dimension(200, 160));
        imageBG.add(imageLabel, BorderLayout.CENTER);
        panel.add(imageBG, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(bg);
        bottomPanel.setOpaque(true);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // spacing

        // for type and price
        JPanel rowPanel = new JPanel(new GridLayout(1, 2));
        rowPanel.setBackground(bg);
        rowPanel.setOpaque(true);

        JLabel priceLabel = new JLabel("Price: $" + food.getPrice());
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setForeground(Color.BLACK);

        JLabel saturationLabel = new JLabel("Saturation Value: " + food.getSaturationValue());
        saturationLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        saturationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        saturationLabel.setForeground(Color.BLACK);

        rowPanel.add(priceLabel);
        rowPanel.add(saturationLabel);

        // for description
        JTextArea descriptionArea = new JTextArea(food.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(bg);
        descriptionArea.setOpaque(true);
        descriptionArea.setFont(new Font("SansSerif", Font.BOLD, 12));
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        // BUY BUTTON
        JButton buyButton = new JButton("Buy Item");
        buyButton.setFocusPainted(false);
        buyButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        buyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        buyButton.setForeground(Color.WHITE);
        buyButton.setBackground(new Color(102, 51, 0));
        buyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buyButton.addActionListener(e -> {
            purchaseFood(food, currentUser, gameFrame);
        });

        bottomPanel.add(rowPanel, BorderLayout.NORTH);
        bottomPanel.add(descriptionArea, BorderLayout.CENTER);
        bottomPanel.add(buyButton, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void purchaseFood(Food food, User currentUser, GameFrame gameFrame) {
        JFrame frame = new JFrame();
        frame.setSize(280, 150);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(new Color(255, 255, 200));
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel label = new JLabel("Buy " + food.getName() + " for " + food.getPrice() + "?", SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        frame.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(255, 255, 200));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton yesButton = new JButton("Yes");
        yesButton.setPreferredSize(new Dimension(70, 30));
        yesButton.setBackground(new Color(102, 51, 0));
        yesButton.setForeground(Color.WHITE);
        yesButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        yesButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        yesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(70, 30));
        cancelButton.setBackground(new Color(153, 76, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        yesButton.setFocusPainted(false);
        cancelButton.setFocusPainted(false);
        buttonPanel.add(yesButton);
        buttonPanel.add(cancelButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        yesButton.addActionListener(e -> {
            boolean checkMoney = buyItemFood(food, food.getPrice(), currentUser, gameFrame);

            if (checkMoney) {
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame,"You don't have enough money!", "Not Enough Money", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    public boolean buyItemFood(Food food, double itemPrice, User currentUser, GameFrame gameFrame) {
        double currentMoney = currentUser.getCurrentMoney();
        if (itemPrice <= currentMoney) {
            currentMoney -= itemPrice;
            currentUser.setCurrentMoney(currentMoney); // handle updating of money inside
            PlayerInventory.updateMoney(currentUser);
            gameFrame.refreshMoney();
            PlayerInventory.addFood(currentUser, food, 1);
            return true;
        }
        return false;
    }
}
