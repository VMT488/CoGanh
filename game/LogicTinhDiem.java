package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LogicTinhDiem {
	
	private static final List<Point[]> DANH_SACH_CAM = List.of(new Point[] { new Point(0, 1), new Point(1, 2) },
			new Point[] { new Point(1, 2), new Point(0, 3) }, new Point[] { new Point(0, 3), new Point(1, 4) },
			new Point[] { new Point(3, 0), new Point(4, 1) }, new Point[] { new Point(0, 1), new Point(1, 0) },
			new Point[] { new Point(2, 1), new Point(1, 0) }, new Point[] { new Point(2, 1), new Point(1, 2) },
			new Point[] { new Point(2, 3), new Point(1, 2) }, new Point[] { new Point(2, 3), new Point(1, 4) },
			new Point[] { new Point(3, 0), new Point(2, 1) }, new Point[] { new Point(2, 1), new Point(3, 2) },
			new Point[] { new Point(3, 2), new Point(2, 3) }, new Point[] { new Point(3, 2), new Point(2, 4) },
			new Point[] { new Point(3, 2), new Point(4, 1) }, new Point[] { new Point(3, 2), new Point(4, 3) },
			new Point[] { new Point(4, 3), new Point(3, 4) });
	private Stack<NuocDi> lichSuNuocDi = new Stack<>();

	// Ham tinh diem
	public int heuristic(QuanCo[][] banCo, boolean minimax) {
		int quanDo = 0, quanXanh = 0;
		int kichThuocBanCo = banCo.length;

		// Duyet qua tung vi tri trong mang
		for (int row = 0; row < banCo.length; row++) {
			for (int col = 0; col < banCo[row].length; col++) {
				if (banCo[row][col] != null) {
					if (banCo[row][col].getColor().equals(Color.RED)) {
						quanDo++; // Mot quan co mau do tuong duong voi 1 diem
						quanDo += tinhDiemKhiCoQuanDongMinhLienKe(banCo, row, col, Color.RED);
						quanDo += tinhDiemKhiGanTrungTam(row, col, kichThuocBanCo);
					} else if (banCo[row][col].getColor().equals(Color.BLUE)) {
						quanXanh++; // Mot quan co mau xanh tuong duong voi 1 diem
						quanXanh += tinhDiemKhiCoQuanDongMinhLienKe(banCo, row, col, Color.BLUE);
						quanXanh += tinhDiemKhiGanTrungTam(row, col, kichThuocBanCo);
					}
				}
			}
		}

		// Tra ve so diem cua AI(quan co do) - diem cua nguoi choi(quan co xanh)
		return minimax ? (quanDo - quanXanh) : (quanXanh - quanDo);
	}

	// Ham tinh diem dua tren so luong quan dong minh o gan
	public int tinhDiemKhiCoQuanDongMinhLienKe(QuanCo[][] banCo, int row, int col, Color mauSacQuanCoTrongLuot) {
		int tongDiem = 0;
		// Duyet qua cac o xung quanh quan co dang di
		for (int dr = -1; dr <= 1; dr++) {
			for (int dc = -1; dc <= 1; dc++) {
				int newRow = row + dr;
				int newCol = col + dc;
				// Dam bao cac o lien ke van nam trong ban co
				if (newRow >= 0 && newRow < banCo.length && newCol >= 0 && newCol < banCo[0].length) {
					QuanCo quanDongMinhLienKe = banCo[newRow][newCol]; // Neu van nam trong thi lay ra kiem tra
					// Kiem tra co phai la quan co dong minh
					if (quanDongMinhLienKe != null && quanDongMinhLienKe.getColor().equals(mauSacQuanCoTrongLuot)) {
						tongDiem += 1; // Cong diem cho moi dong minh o gan
					}
				}
			}
		}
		return tongDiem;
	}

	// Ham minimax
	public int minimax(QuanCo[][] state, int depth, int alpha, int beta, boolean minmax) {
		if (depth == 0 || gameOver(state)) {
			return heuristic(state, minmax);
		}

		List<NuocDi> danhSachNuocDi = taoNuocDiChoAI(state, minmax ? Color.RED : Color.BLUE);

		if (danhSachNuocDi.isEmpty())
			return heuristic(state, minmax);

		if (minmax) {
			int temp = Integer.MIN_VALUE;
			for (NuocDi move : danhSachNuocDi) {
				thucHienDiChuyen(state, move); // tao ban co moi 
				int tinhDiem = minimax(state, depth - 1, alpha, beta, false); // Tinh dien cho state moi
				hoanTac(state, move);
				temp = Math.max(temp, tinhDiem);
				alpha = Math.max(alpha, tinhDiem); // Cap nhat gia tri lon nhat
				if (beta <= alpha)
					break; // Cat bo nhanh khong can thiet
			}
			return temp;
		} else {
			int temp = Integer.MAX_VALUE;
			for (NuocDi move : danhSachNuocDi) {
				thucHienDiChuyen(state, move);
				int tinhDiem = minimax(state, depth - 1, alpha, beta, true);
				hoanTac(state, move);
				temp = Math.min(temp, tinhDiem);
				beta = Math.min(beta, tinhDiem); // Cap nhat gia tri lon nhat
				if (beta <= alpha)
					break; // Cat bo nhanh khong can thiet
			}
			return temp;
		}
	}

	// Kiem tra dieu kien ket thuc
	public boolean gameOver(QuanCo[][] banCo) {
		boolean quanDo = false, quanXanh = false;

		for (QuanCo[] row : banCo) {
			for (QuanCo piece : row) {
				if (piece != null) {
					if (piece.getColor().equals(Color.RED))
						quanDo = true;
					if (piece.getColor().equals(Color.BLUE))
						quanXanh = true;
				}
			}
		}

		return !(quanDo && quanXanh);
	}

	// Ham tao nuoc di cho AI
	public List<NuocDi> taoNuocDiChoAI(QuanCo[][] banCo, Color mauQuanCoCuaAI) {
		List<NuocDi> danhSachNuocDi = new ArrayList<>(); // Luu tru danh sach nuoc di
		// Duyet tung vi tri trong ban co
		for (int row = 0; row < banCo.length; row++) {
			for (int col = 0; col < banCo[row].length; col++) {
				// Dieu kien o nay co quan co va trung voi mau quan co AI
				if (banCo[row][col] != null && banCo[row][col].getColor().equals(mauQuanCoCuaAI)) {
					// Kiem tra cac vi tri xung quanh
					for (int dr = -1; dr <= 1; dr++) {
						for (int dc = -1; dc <= 1; dc++) {
							int newRow = row + dr;
							int newCol = col + dc;
							// Vi tri sap di phai nam trong ban co va dang trong
							if (newRow >= 0 && newRow < banCo.length && newCol >= 0 && newCol < banCo[row].length
									&& banCo[newRow][newCol] == null) {
								NuocDi move = new NuocDi(new Point(col, row), new Point(newCol, newRow));
								if (nuocDiHopLe(new Point(col, row), new Point(newCol, newRow))) {
									danhSachNuocDi.add(move);
								}
							}
						}
					}
				}
			}
		}
		// Sap xep theo xem nuoc di nao duoc nhieu diem hon
		danhSachNuocDi.sort((cach1, cach2) -> {
			int diemCach1 = danhGiaQuanDongMinhLienKe(banCo, cach1);
			int diemCach2 = danhGiaQuanDongMinhLienKe(banCo, cach2);
			return Integer.compare(diemCach2, diemCach1); // Lay nuoc di nhieu diem
		});
		return danhSachNuocDi;
	}

	// Ham tinh diem khi quan co o gan trung tam
	public int tinhDiemKhiGanTrungTam(int row, int col, int kichThuocBanCo) {
		// Tinh toa do trung tam
		double trungTamX = (kichThuocBanCo - 1) / 2.0;
		double trungTamY = (kichThuocBanCo - 1) / 2.0;

		// Tinh khoang cach den trung tam
		double khoangCach = Math.abs(row - trungTamX) + Math.abs(col - trungTamY);

		// Diem la 10 o trung tam giam dan khi ra xa
		int diem = (int) Math.round(1 - khoangCach);

		// Dam bao diem khong am
		return Math.max(diem, 0);
	}

	// Ham danh gia muc do ho tro nuoc di
	public int danhGiaQuanDongMinhLienKe(QuanCo[][] banCo, NuocDi nuocDi) {
		thucHienDiChuyen(banCo, nuocDi); // Di chuyen thu de kiem tra diem
		int diem = tinhDiemKhiCoQuanDongMinhLienKe(banCo, nuocDi.to.y, nuocDi.to.x, Color.BLUE);
		hoanTac(banCo, nuocDi); // Hoan tac de giu nguyen ban co truoc do
		return diem;
	}
	
	// Thuc hien nuoc di
	public void thucHienDiChuyen(QuanCo[][] banCo, NuocDi nuocDi) {
		lichSuNuocDi.add(nuocDi);
		banCo[nuocDi.to.y][nuocDi.to.x] = banCo[nuocDi.from.y][nuocDi.from.x];
		banCo[nuocDi.from.y][nuocDi.from.x] = null;
	}

	// Hoan tac nuoc di
	public void hoanTac(QuanCo[][] banCo, NuocDi nuocDi) {
		banCo[nuocDi.from.y][nuocDi.from.x] = banCo[nuocDi.to.y][nuocDi.to.x];
		banCo[nuocDi.to.y][nuocDi.to.x] = null;
	}

	public boolean nuocDiHopLe(Point from, Point to) {
		for (Point[] cam : DANH_SACH_CAM) {
			if ((cam[0].equals(from) && cam[1].equals(to)) || (cam[1].equals(from) && cam[0].equals(to))) {
				return false; // Nước đi bị cấm
			}
		}
		return true; // Nước đi hợp lệ
	}
}