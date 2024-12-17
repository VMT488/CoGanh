package game;

import javax.swing.*;
import java.awt.*;

public class MainUI {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Cờ Gánh");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 500);
		frame.setLayout(new BorderLayout());
		
		JPanel panelBanCo = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Panel chua cac thong tin
		JPanel huongDanChoi =new JPanel(new GridLayout(3,1));
		JLabel quanDo = new JLabel("Quân đỏ: Máy tính");
		JLabel quanXanh = new JLabel("Quân xanh: Người chơi");
		JLabel huongDanDiChuyen = new JLabel("Cách di chuyển: Kéo thả chuột");
		
		BanCo board = new BanCo();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		panelBanCo.add(board, gbc);
		huongDanChoi.add(quanDo);
		huongDanChoi.add(huongDanDiChuyen);
		huongDanChoi.add(quanXanh);

		// Them vao JFrame
		frame.add(panelBanCo, BorderLayout.CENTER);
		frame.add(huongDanChoi, BorderLayout.EAST);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}