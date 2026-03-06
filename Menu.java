import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Menu extends JPanel {

    private User currentUser;
    private GameFrame gameFrame;

    public Menu(User currentUser, GameFrame gameFrame) {

        this.currentUser = currentUser;
        this.gameFrame = gameFrame;

        this.setBackground(new Color(255, 255, 200));
        this.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 2),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                )
        );

        this.setLayout(new GridLayout(3, 3, 20, 20));
        this.setPreferredSize(new Dimension(400, 400));

        Color[] buttonColors = {
                new Color(153, 204, 255),
                new Color(204, 255, 153),
                new Color(255, 153, 153),
                new Color(255, 204, 153),
                new Color(204, 204, 255),
                new Color(255, 255, 153),
                new Color(255, 204, 255),
                new Color(204, 255, 229),
                new Color(204, 255, 204)
        };

        String[] menuImages = {
                "images/shop.png",
                "images/cats.png",
                "images/inventory.png",
                "images/place.png",
                "images/viewStats.png",
                "images/logout.png",
                null,
                null,
                null
        };

        for (int i = 0; i < 9; i++) {
            JButton button = new JButton();
            button.setBackground(buttonColors[i]);
            button.setBorder(new LineBorder(Color.BLACK, 1));

            ImageIcon icon = new ImageIcon(menuImages[i]);
            Image img = icon.getImage();
            Image scaledMenuIcon = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledMenuIcon));

            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            int index = i;
            button.addActionListener(e -> handleMenuButton(index));

            this.add(button);
        }
    }

    public void handleMenuButton(int index) {
        switch (index) {
            case 0:
                Shop shop = new Shop();
                shop.openShop(currentUser, gameFrame);
                this.setVisible(false);
                break;
            case 1:
                SelectCat cat = new SelectCat();
                cat.Selection(currentUser);
                this.setVisible(false);
                // System.out.println(currentUser.getCurrentMoney());
                break;
            case 2:
                currentUser.setFurnitureInventory(PlayerInventory.loadFurnitureInventory(currentUser)); // load from DB
                new InventoryFrame(currentUser);
                break;
            case 3:
                this.setVisible(false); // hide menu
                gameFrame.showAddButtons(); // show the plus signs
                break;
            case 4:
                Report reports = new Report();
                reports.showReports(currentUser);
                this.setVisible(false);
                break;
            case 5:
                new Logout(this, gameFrame);
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
        }
    }


}
