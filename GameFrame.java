import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private Menu menu;
    private User currentUser;
    private JLabel moneyLabel;
    private JPanel grassPanel;

    public GameFrame(User user) {

        this.currentUser = user;
        ImageIcon icon = new ImageIcon("images/catIcon.png");
        this.setIconImage(icon.getImage());

        this.setTitle("Kitty Collector Game");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 800, 800);
        this.add(layeredPane);

        ImageIcon suburbs = new ImageIcon("images/suburbs.png");
        Image bgImage = suburbs.getImage();
        Image scaledBG = bgImage.getScaledInstance(800, 260, Image.SCALE_SMOOTH);
        JLabel topBG = new JLabel(new ImageIcon(scaledBG));
        topBG.setBounds(0, 0, 800, 260);
        layeredPane.add(topBG, JLayeredPane.DEFAULT_LAYER);

        grassPanel = new JPanel();
        grassPanel.setLayout(null); // so we can freely place plus signs
        grassPanel.setBackground(new Color(132, 232, 132));
        grassPanel.setBounds(0, 260, 800, 510);
        grassPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        layeredPane.add(grassPanel, JLayeredPane.DEFAULT_LAYER);

        ImageIcon menuImg = new ImageIcon("images/menu.png");
        Image img = menuImg.getImage();
        Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledMenuImg = new ImageIcon(scaledImg);

        JButton menuButton = new JButton(scaledMenuImg);
        menuButton.setBackground(new Color(255, 255, 200));
        menuButton.setBorder(new LineBorder(Color.BLACK, 1));
        menuButton.setBounds(10, 10, 50, 50);
        menuButton.setFocusPainted(false);
        menuButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        layeredPane.add(menuButton, JLayeredPane.PALETTE_LAYER);

        moneyLabel = new JLabel("MONEY:  " + currentUser.getCurrentMoney());
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moneyLabel.setForeground(Color.BLACK);
        moneyLabel.setOpaque(true);
        moneyLabel.setBackground(new Color(255, 255, 200));
        moneyLabel.setBounds(620, 10, 150, 40);
        moneyLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        moneyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        moneyLabel.setVerticalAlignment(SwingConstants.CENTER);
        layeredPane.add(moneyLabel, JLayeredPane.PALETTE_LAYER);

        menu = new Menu(currentUser, this);
        menu.setSize(300, 300);
        menu.setVisible(false);
        layeredPane.add(menu, JLayeredPane.MODAL_LAYER); // always on top

        menuButton.addActionListener(e -> {
            int frameW = this.getWidth();
            int frameH = this.getHeight();
            int menuW = menu.getWidth();
            int menuH = menu.getHeight();

            menu.setLocation((frameW - menuW) / 2, (frameH - menuH) / 2);
            menu.setVisible(!menu.isVisible());
        });

        this.setVisible(true);
        loadPlacedFurnitureOnGrass();
    }

    public void refreshMoney() {
        moneyLabel.setText("Money: " + currentUser.getCurrentMoney());
    }


    public void showAddButtons() {
        grassPanel.setLayout(null);

        JButton topLeft = createPlusButton();
        topLeft.setBounds(130, 70, 50, 50);
        int posTopLeft = 0;

        JButton topRight = createPlusButton();
        topRight.setBounds(620, 70, 50, 50);
        int posTopRight = 1;

        JButton middle = createPlusButton();
        middle.setBounds(375, 200, 50, 50);
        int posMiddle = 2;

        JButton bottomLeft = createPlusButton();
        bottomLeft.setBounds(130, 380, 50, 50);
        int posBottomLeft = 3;

        JButton bottomRight = createPlusButton();
        bottomRight.setBounds(620, 380, 50, 50);
        int posBottomRight = 4;

        JButton[] plusButtons = {topLeft, topRight, middle, bottomLeft, bottomRight};
        int[] positions = {posTopLeft, posTopRight, posMiddle, posBottomLeft, posBottomRight};

        PlaceFurniturePopup placeFurniture = new PlaceFurniturePopup(this, currentUser, grassPanel);

        PlacedFurniture[] placed = currentUser.getPlacedFurniture();

        for (int i = 0; i < plusButtons.length; i++) {
            JButton btn = plusButtons[i];
            int pos = positions[i];
            boolean occupied = false;

            if (placed != null) {
                for (PlacedFurniture placedFurniture : placed) {
                    if (placedFurniture != null && placedFurniture.getPositionPlaced() == pos) {
                        occupied = true;
                        break;
                    }
                }
            }

            // if already occupied, hide button
            if (occupied) {
                btn.setVisible(false);
            }
            grassPanel.add(btn);

            // do button action if not occupied
            if (!occupied) {
                btn.addActionListener(e -> placeFurniture.showFurniturePopup(btn, pos));
            }
        }

        JButton exitButton = new JButton("Cancel");
        exitButton.setBounds(360, 10, 80, 40); // top-right corner
        exitButton.setFont(new Font("Arial", Font.BOLD, 17));
        exitButton.setBackground(new Color(243, 43, 43));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        exitButton.addActionListener(e -> {
            for (JButton btn : plusButtons) {
                grassPanel.remove(btn);
            }
            grassPanel.remove(exitButton);
            grassPanel.revalidate();
            grassPanel.repaint();
        });

        grassPanel.add(exitButton);

        grassPanel.revalidate();
        grassPanel.repaint();
    }

    // DONE
    private JButton createPlusButton() {
        JButton plus = new JButton("+");
        plus.setFont(new Font("Arial", Font.BOLD, 24));
        plus.setFocusPainted(false);
        plus.setBackground(new Color(255, 255, 200));
        plus.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        plus.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        return plus;
    }

    public void loadPlacedFurnitureOnGrass() {
        PlacedFurniture[] placed = currentUser.getPlacedFurniture();
        if (placed == null) return;

        for (PlacedFurniture pf : placed) {
            if (pf == null) continue;

            int pos = pf.getPositionPlaced() - 1;

            int x = 0, y = 0;
            switch (pos) {
                case 0: x = 60;  y = 10;  break;
                case 1: x = 530; y = 10;  break;
                case 2: x = 290; y = 130; break;
                case 3: x = 60;  y = 290; break;
                case 4: x = 530; y = 290; break;
            }

            ImageIcon original = pf.getFurniture().getImage();
            Image scaled = original.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaled));
            label.setBounds(x, y, 200, 200);

            PlaceFurniturePopup popup = new PlaceFurniturePopup(this, currentUser, grassPanel);
            popup.RemoveFurnitureListener(label, null, pf);

            grassPanel.add(label);
        }

        grassPanel.repaint();
    }

}