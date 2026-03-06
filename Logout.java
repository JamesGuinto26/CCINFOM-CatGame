import javax.swing.*;
import java.awt.*;

public class Logout extends JFrame {

    public Logout(Menu menuPanel, GameFrame gameFrame) {
        setUndecorated(true);
        setSize(320, 180);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel bg = new JPanel();
        bg.setBackground(new Color(255, 255, 200));
        bg.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        bg.setLayout(new BoxLayout(bg, BoxLayout.Y_AXIS));
        setContentPane(bg);

        bg.add(Box.createVerticalStrut(40));

        JLabel label = new JLabel("Logout from your account?", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        bg.add(label);

        bg.add(Box.createVerticalStrut(30));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonPanel.setOpaque(false);

        JButton yesButton = new JButton("Logout");
        yesButton.setPreferredSize(new Dimension(100, 40));
        yesButton.setBackground(new Color(153, 76, 0));
        yesButton.setForeground(Color.WHITE);
        yesButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        yesButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        yesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton noButton = new JButton("Cancel");
        noButton.setPreferredSize(new Dimension(100, 40));
        noButton.setBackground(new Color(102, 51, 0));
        noButton.setForeground(Color.WHITE);
        noButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        noButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        noButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        bg.add(buttonPanel);

        bg.add(Box.createVerticalStrut(20));

        yesButton.setFocusPainted(false);
        noButton.setFocusPainted(false);

        yesButton.addActionListener(e -> {
            gameFrame.getContentPane().remove(menuPanel);
            gameFrame.dispose();
            this.dispose();
            new Login().setVisible(true);
        });

        noButton.addActionListener(e -> dispose());

        setVisible(true);
    }
}
