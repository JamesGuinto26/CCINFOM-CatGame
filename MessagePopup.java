import javax.swing.*;
import java.awt.*;

public class MessagePopup extends JDialog {

    public MessagePopup(JFrame parent, String message) {
        super(parent, true);
        setTitle("Message");

        JPanel mainPanel = new JPanel(new BorderLayout(10, 3));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        mainPanel.setBackground(new Color(255, 255, 200));

        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setOpaque(false);

        JButton okButton = new JButton("OK");
        okButton.setForeground(Color.WHITE);
        okButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        okButton.setBackground(new Color(102, 51, 0));
        okButton.setPreferredSize(new Dimension(80, 30));
        okButton.setFocusPainted(false);
        okButton.setBorderPainted(false);
        okButton.setContentAreaFilled(true);
        okButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        okButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 200));
        buttonPanel.add(okButton);

        mainPanel.add(label, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setSize(300, 150);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setResizable(false);
    }
}
