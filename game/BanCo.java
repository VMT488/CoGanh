package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BanCo extends JPanel {
	private static final int BOARD_SIZE = 400; // Kich thuoc ban co
	private static final int SQUARES_PER_ROW = 5; // Kich thuoc moi o co
	private Point quanCoDuocChon = null;
	private QuanCo[][] quanCo; // Mang 2D chua cac quan co
	private LogicTinhDiem logic;

	public BanCo() {
		setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
		setBackground(Color.WHITE);
		quanCo = new QuanCo[SQUARES_PER_ROW][SQUARES_PER_ROW]; // Mang cac quan co 5x5
		logic = new LogicTinhDiem(); // Khoi tao lop logic
		initializePieces();
		// Di chuyen quan co bang cach keo tha chuot
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int col = e.getX() / (BOARD_SIZE / SQUARES_PER_ROW);
				int row = e.getY() / (BOARD_SIZE / SQUARES_PER_ROW);
				if (quanCo[row][col] != null && quanCo[row][col].getColor() == Color.BLUE) {
					quanCoDuocChon = new Point(col, row);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (quanCoDuocChon != null) {
					int col = e.getX() / (BOARD_SIZE / SQUARES_PER_ROW);
					int row = e.getY() / (BOARD_SIZE / SQUARES_PER_ROW);
					Point target = new Point(col, row);

					if (logic.nuocDiHopLe(quanCoDuocChon, target)) {
						diChuyenQuanCo(quanCoDuocChon, target);
						kiemTraGanh(target);
						repaint();

						// Sau khi nguoi choi di chuyen thi den luot AI
						nuocDiCuaAI();
					}
					quanCoDuocChon = null;
				}
			}
		});
	}

	// Khoi tao quan co va vi tri quan co
	public void initializePieces() {
		// Tao cac quan co mau do
		quanCo[0][0] = new QuanCo(Color.RED);
		quanCo[0][1] = new QuanCo(Color.RED);
		quanCo[0][2] = new QuanCo(Color.RED);
		quanCo[0][3] = new QuanCo(Color.RED);
		quanCo[0][4] = new QuanCo(Color.RED);
		quanCo[1][0] = new QuanCo(Color.RED);
		quanCo[1][4] = new QuanCo(Color.RED);
		quanCo[2][4] = new QuanCo(Color.RED);
		// Tao cac quan co mau xanh
		quanCo[3][4] = new QuanCo(Color.BLUE);
		quanCo[3][0] = new QuanCo(Color.BLUE);
		quanCo[2][0] = new QuanCo(Color.BLUE);
		quanCo[4][0] = new QuanCo(Color.BLUE);
		quanCo[4][1] = new QuanCo(Color.BLUE);
		quanCo[4][2] = new QuanCo(Color.BLUE);
		quanCo[4][3] = new QuanCo(Color.BLUE);
		quanCo[4][4] = new QuanCo(Color.BLUE);
	}

	// Ve ban co
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int squareSize = BOARD_SIZE / SQUARES_PER_ROW;

		for (int row = 0; row < SQUARES_PER_ROW; row++) {
			for (int col = 0; col < SQUARES_PER_ROW; col++) {
				g.setColor(Color.WHITE);
				g.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
				veCacDuongDocNgang(g, col * squareSize, row * squareSize, squareSize);
			}
		}

		veHinhThoi(g);
		veDuongCheo(g);

		for (int row = 0; row < SQUARES_PER_ROW; row++) {
			for (int col = 0; col < SQUARES_PER_ROW; col++) {
				veQuanCo(g, quanCo[row][col], col * squareSize, row * squareSize, squareSize);
			}
		}
	}

	// Phuong thuc ve quan co
	public void veQuanCo(Graphics g, QuanCo piece, int x, int y, int squareSize) {
		if (piece != null) {
			g.setColor(piece.getColor());
			int pawnSize = squareSize / 2;
			g.fillOval(x + (squareSize - pawnSize) / 2, y + (squareSize - pawnSize) / 2, pawnSize, pawnSize);
		}
	}

	// Phuong thuc ve cac duong ke
	public void veCacDuongDocNgang(Graphics g, int x, int y, int squareSize) {
		g.setColor(Color.BLACK);
		int startX = x;
		int startY = y + squareSize / 2;
		int endX = x + squareSize;
		int endY = y + squareSize / 2;
		g.drawLine(startX, startY, endX, endY);
		startX = x + squareSize / 2;
		startY = y;
		endX = x + squareSize / 2;
		endY = y + squareSize;
		g.drawLine(startX, startY, endX, endY);
	}

	// Phuong thuc ve cac duong cheo tao thanh hinh thoi
	public void veHinhThoi(Graphics g) {
		g.setColor(Color.BLACK);
		int centerX = BOARD_SIZE / 2;
		int centerY = BOARD_SIZE / 2;
		int halfSize = 160;
		int[] xPoints = { centerX, centerX + halfSize, centerX, centerX - halfSize };
		int[] yPoints = { centerY - halfSize, centerY, centerY + halfSize, centerY };
		g.drawPolygon(xPoints, yPoints, 4);
	}

	// Phuong thuc ve cac duong cheo con lai
	public void veDuongCheo(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(0, 0, BOARD_SIZE, BOARD_SIZE);
		g.drawLine(BOARD_SIZE, 0, 0, BOARD_SIZE);
	}

	public void diChuyenQuanCo(Point from, Point to) {
		quanCo[to.y][to.x] = quanCo[from.y][from.x];
		quanCo[from.y][from.x] = null;
	}

	public void nuocDiCuaAI() {
		new Thread(() -> {
			try {
				// Do tre 1 giay
				Thread.sleep(1000);

				Move nuocDiTotNhat = null;
				int giaTriTotNhat = Integer.MIN_VALUE;
	            
				for (Move move : logic.taoNuocDiChoAI(quanCo, Color.RED)) {
					logic.applyMove(quanCo, move);
					int giaTriLonHon = logic.minimax(quanCo, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
					logic.undoMove(quanCo, move);

					if (giaTriLonHon > giaTriTotNhat) {
						giaTriTotNhat = giaTriLonHon;
						nuocDiTotNhat = move;
					}
				}

				if (nuocDiTotNhat != null) {
					System.out.println("Nuoc di co gia tri: " + giaTriTotNhat);
					logic.applyMove(quanCo, nuocDiTotNhat);

					// Kiem tra ganh sau nuoc di
					kiemTraGanh(nuocDiTotNhat.getTo());

					// Cap nhat lai giao dien
					SwingUtilities.invokeLater(this::repaint);
				} else {
					System.out.println("Khong co nuoc di");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void kiemTraGanh(Point movedTo) {
		int row = movedTo.y;
		int col = movedTo.x;

		// Mau cua quan co vua di chuyen
		Color mauQuanCo = quanCo[row][col].getColor();

		// Kiem tra ganh theo chieu ngang
		if (col > 0 && col < SQUARES_PER_ROW - 1) {
			if (quanCo[row][col - 1] != null && quanCo[row][col + 1] != null
					&& !quanCo[row][col - 1].getColor().equals(mauQuanCo)
					&& !quanCo[row][col + 1].getColor().equals(mauQuanCo)) {
				quanCo[row][col - 1].changeColor(mauQuanCo);
				quanCo[row][col + 1].changeColor(mauQuanCo);
			}
		}

		// Kiem tra ganh theo chieu doc
		if (row > 0 && row < SQUARES_PER_ROW - 1) {
			if (quanCo[row - 1][col] != null && quanCo[row + 1][col] != null
					&& !quanCo[row - 1][col].getColor().equals(mauQuanCo)
					&& !quanCo[row + 1][col].getColor().equals(mauQuanCo)) {
				quanCo[row - 1][col].changeColor(mauQuanCo);
				quanCo[row + 1][col].changeColor(mauQuanCo);
			}
		}

		// Kiem tra ganh theo duong cheo (trai tren - phai duoi)
		if (row > 0 && col > 0 && row < SQUARES_PER_ROW - 1 && col < SQUARES_PER_ROW - 1) {
			if (quanCo[row - 1][col - 1] != null && quanCo[row + 1][col + 1] != null
					&& !quanCo[row - 1][col - 1].getColor().equals(mauQuanCo)
					&& !quanCo[row + 1][col + 1].getColor().equals(mauQuanCo)) {
				quanCo[row - 1][col - 1].changeColor(mauQuanCo);
				quanCo[row + 1][col + 1].changeColor(mauQuanCo);
			}
		}

		// Kiem tra ganh theo duong cheo (trai duoi - phai tren)
		if (row < SQUARES_PER_ROW - 1 && col > 0 && row > 0 && col < SQUARES_PER_ROW - 1) {
			if (quanCo[row + 1][col - 1] != null && quanCo[row - 1][col + 1] != null
					&& !quanCo[row + 1][col - 1].getColor().equals(mauQuanCo)
					&& !quanCo[row - 1][col + 1].getColor().equals(mauQuanCo)) {
				quanCo[row + 1][col - 1].changeColor(mauQuanCo);
				quanCo[row - 1][col + 1].changeColor(mauQuanCo);
			}
		}
	}
}
