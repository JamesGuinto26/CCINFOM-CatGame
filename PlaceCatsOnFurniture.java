import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlaceCatsOnFurniture {
    private GameFrame gameFrame;
    private User currentUser;
    private JPanel grassPanel;

    public PlaceCatsOnFurniture(GameFrame gameFrame, User currentUser, JPanel grassPanel) {
        this.gameFrame = gameFrame;
        this.currentUser = currentUser;
        this.grassPanel = grassPanel;
    }

    public void spawnCatsOnFurniture() {
        PlacedFurniture[] furnitures = currentUser.getPlacedFurniture();
        ArrayList<OwnedCat> catsOnLawn = currentUser.getCatsOnLawn();

        if (furnitures == null || catsOnLawn == null) {
            return;
        }

        for (OwnedCat cat : catsOnLawn) {
            if (cat == null) {
                continue;
            }

            // find the first furniture without a cat
            for (PlacedFurniture pf : furnitures) {
                if (pf != null && !pf.hasOccupyingCat(Integer.parseInt(currentUser.getIdNumber()))) {

                    int x = 0, y = 0;
                    int index = pf.getPositionPlaced(); // get position of furniture where it was placed
                    switch (index) {
                        case 0: x = 60;  y = 10;  break;
                        case 1: x = 530; y = 10;  break;
                        case 2: x = 290; y = 130; break; // then place the cat there
                        case 3: x = 60;  y = 290; break;
                        case 4: x = 530; y = 290; break;
                    }

                    // offset so cat appears beside the furniture (not yet tested so not sure how it will look)
                    int catX = x + 50;
                    int catY = y + 50;

                    ImageIcon catImg = cat.getImage();
                    Image scaledCat = catImg.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    JLabel catLabel = new JLabel(new ImageIcon(scaledCat));
                    catLabel.setBounds(catX, catY, 100, 100);
                    catLabel.setOpaque(false);

                    addCatClickListener(catLabel, cat); // make cat clickable
                    grassPanel.add(catLabel, JLayeredPane.PALETTE_LAYER);

                    break;
                }
            }
        }

        grassPanel.revalidate();
        grassPanel.repaint();
    }

    private void addCatClickListener(JLabel catLabel, OwnedCat cat) {
        catLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // call the feed popup class here
            }
        });
    }
}
