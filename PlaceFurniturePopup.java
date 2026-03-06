import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlaceFurniturePopup {
    private GameFrame gameFrame;
    private User currentUser;
    private JPanel grassPanel;

    public PlaceFurniturePopup(GameFrame gameFrame, User currentUser, JPanel grassPanel) {
        this.gameFrame = gameFrame;
        this.currentUser = currentUser;
        this.grassPanel = grassPanel;
    }

    public void addPlusButton(JButton plusButton, int positionIndex) {
        plusButton.addActionListener(e -> showFurniturePopup(plusButton, positionIndex));
    }

    public void showFurniturePopup(JButton plusButton, int positionIndex) {
        JFrame popup = new JFrame();
        popup.setTitle("Place Furniture");
        popup.setSize(500, 450);
        popup.setLocationRelativeTo(null);
        popup.setLayout(new BorderLayout());
        popup.setResizable(false);
        popup.setUndecorated(true);

        JPanel contentPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        contentPanel.setBackground(new Color(255, 255, 200));
        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        popup.add(scroll, BorderLayout.CENTER);

        ArrayList<OwnedFurniture> ownedList = currentUser.getFurnitureInventory();

        if (ownedList == null || ownedList.isEmpty()) {
            JOptionPane.showMessageDialog(gameFrame, "You don't have any furniture.", "No Furniture", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        FurnitureSystem sys = new FurnitureSystem();
        ArrayList<Furniture> allFurnitures = sys.getAllFurnitures();

        boolean anyShown = false;

        for (OwnedFurniture of : ownedList) {

            if (of == null) continue;
            if (of.getQuantity() <= 0) continue; // skip zero-quantity items

            Furniture f = findFurnitureByName(allFurnitures, of.getName());

            if (f == null) continue;

            anyShown = true;
            JPanel itemPanel = createItemPanel(f.getName(), f.getImage());

            final Furniture selected = f;
            itemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    placeFurnitureAtButton(selected, plusButton, positionIndex);
                    popup.dispose();
                }
            });

            contentPanel.add(itemPanel);
        }

        if (!anyShown) {
            JOptionPane.showMessageDialog(gameFrame, "No furniture available to place (all quantities are zero).", "No Furniture", JOptionPane.INFORMATION_MESSAGE);
            popup.dispose();
            return;
        }
        popup.setVisible(true);
    }

    public Furniture findFurnitureByName(ArrayList<Furniture> list, String name) {
        if (list == null || name == null) return null;
        for (Furniture f : list) {
            if (f != null && name.equals(f.getName())) {
                return f;
            }
        }
        return null;
    }

    public void placeFurnitureAtButton(Furniture f, JButton plusButton, int positionIndex) {
        int x = 0;
        int y = 0;

        switch (positionIndex) {
            case 0:
                x = 60;
                y = 10;
                break;
            case 1:
                x = 530;
                y = 10;
                break;
            case 2:
                x = 290;
                y = 130;
                break;
            case 3:
                x = 60;
                y = 290;
                break;
            case 4:
                x = 530;
                y = 290;
                break;
        }

        // create furniture JLabel
        ImageIcon originalIcon = f.getImage();
        Image img = originalIcon.getImage();
        Image scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel furnitureLabel = new JLabel(new ImageIcon(scaledImg));
        furnitureLabel.setBounds(x, y, 200, 200);
        furnitureLabel.setOpaque(false);

        grassPanel.add(furnitureLabel);
        grassPanel.repaint();

        plusButton.setVisible(false);

        PlacedFurniture newPF = new PlacedFurniture(0, positionIndex, currentUser.getIdNumber(), f);
        PlacedFurniture[] currentPlaced = currentUser.getPlacedFurniture();
        int size = (currentPlaced != null) ? currentPlaced.length : 0;
        PlacedFurniture[] updatedPlaced = new PlacedFurniture[size + 1];

        if (size > 0) {
            System.arraycopy(currentPlaced, 0, updatedPlaced, 0, size);
        }
        updatedPlaced[size] = newPF;
        currentUser.setPlacedFurniture(updatedPlaced);

        PlacedFurnitureSQL sql = new PlacedFurnitureSQL();
        sql.insertPlacedFurniture(positionIndex + 1, currentUser.getIdNumber(), f.getName());
        sql.decreaseOwnedFurniture(currentUser.getIdNumber(), f.getName());
        sql.deleteIfZero(currentUser.getIdNumber(), f.getName());

        // update user's inventory
        ArrayList<OwnedFurniture> owned = currentUser.getFurnitureInventory();

        for (int i = 0; i < owned.size(); i++) {
            OwnedFurniture ownedFurniture = owned.get(i);
            if (ownedFurniture.getName().equals(f.getName())) {
                int newQty = ownedFurniture.getQuantity() - 1;

                if (newQty <= 0) {
                    owned.remove(i);
                } else {
                    ownedFurniture.setQuantity(newQty);
                }
                break;
            }
        }
        RemoveFurnitureListener(furnitureLabel, plusButton, newPF);
    }

    public void RemoveFurnitureListener(JLabel furnitureLabel, JButton plusButton, PlacedFurniture placedFurniture) {
        furnitureLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                int result = JOptionPane.showConfirmDialog(
                        gameFrame,
                        "Remove this furniture?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );

                if (result == JOptionPane.YES_OPTION) {
                    grassPanel.remove(furnitureLabel);
                    if (plusButton != null) plusButton.setVisible(true);

                    // Remove from placed furniture array
                    PlacedFurniture[] placed = currentUser.getPlacedFurniture();
                    ArrayList<PlacedFurniture> tempList = new ArrayList<>();

                    if (placed != null) {
                        for (PlacedFurniture p : placed) {
                            if (p != null && p != placedFurniture) {
                                tempList.add(p);
                            }
                        }
                    }

                    currentUser.setPlacedFurniture(tempList.toArray(new PlacedFurniture[0]));

                    // Update inventory
                    PlacedFurnitureSQL sql = new PlacedFurnitureSQL();
                    sql.increaseOwnedFurniture(placedFurniture.getPlayerId(), placedFurniture.getFurniture().getName());
                    sql.archiveRecord(placedFurniture);
                    sql.deletePlacedFurniture(placedFurniture.getPositionPlaced(), placedFurniture.getPlayerId());

                    ArrayList<OwnedFurniture> owned = currentUser.getFurnitureInventory();
                    boolean found = false;

                    for (OwnedFurniture of : owned) {

                        if (of.getName().equals(placedFurniture.getFurniture().getName())) {
                            of.setQuantity(of.getQuantity() + 1);
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        owned.add(new OwnedFurniture(placedFurniture.getFurniture().getName(), 1));
                    }

                    currentUser.setFurnitureInventory(owned);
                    grassPanel.revalidate();
                    grassPanel.repaint();
                }
            }
        });
    }

    public JPanel createItemPanel(String name, ImageIcon image) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setBackground(Color.WHITE);

        JLabel imgLabel = new JLabel(new ImageIcon(image.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setOpaque(true);
        nameLabel.setBackground(new Color(102, 51, 0));
        nameLabel.setForeground(Color.WHITE);

        panel.add(imgLabel, BorderLayout.CENTER);
        panel.add(nameLabel, BorderLayout.SOUTH);

        return panel;
    }
}
