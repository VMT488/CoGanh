package game;

import javax.swing.*;
import java.awt.*;

public class MainUI {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Cờ Gánh");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setLayout(new BorderLayout());

		// Panel de can giua ban co
		JPanel panelBanCo = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Panel chua cac thong tin
		JPanel panelThongTin =new JPanel(new FlowLayout());
		JLabel quanDo = new JLabel("Quân đỏ: AI");
		JLabel quanXanh = new JLabel("Quân xanh: Người chơi");
		JLabel diChuyen = new JLabel("Cách di chuyển: Kéo thả chuột");
		
		BanCo board = new BanCo();

		// Can giua ban co
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		panelBanCo.add(board, gbc);
		panelThongTin.add(quanDo);
		panelThongTin.add(quanXanh);
		panelThongTin.add(diChuyen);
		
		// Them vao JFrame
		frame.add(panelBanCo, BorderLayout.CENTER);
		frame.add(panelThongTin, BorderLayout.SOUTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}