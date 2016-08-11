package kr.ac.sungkyul.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kr.ac.sungkyul.mysite.vo.BoardVo;

public class BoardDao {

	private Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return conn;
	}

	public void update(BoardVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();

			Long no = vo.getNo();
			String title = vo.getTitle();
			String content = vo.getContent();

			String sql = null;

			sql = "UPDATE BOARD set title = ?, content = ? WHERE no = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setLong(3, no);
			
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public BoardVo getContent(Long selectedNo) {
		BoardVo vo = new BoardVo();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt1 = null;

		try {
			conn = getConnection();

			String sql = "SELECT no, title, content, group_no, order_no, depth, user_no FROM BOARD WHERE no = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, selectedNo);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String content = rs.getString(3);
				Long groupNo = rs.getLong(4);
				Long orderNo = rs.getLong(5);
				Long depth = rs.getLong(6);
				Long userNo = rs.getLong(7);

				vo.setNo(no);
				vo.setTitle(title);
				vo.setContent(content);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setUserNo(userNo);

				// 글 조회 성공 시 조회수 증가
				sql = "UPDATE BOARD set view_count = view_count + 1 WHERE no=?";
				pstmt1 = conn.prepareStatement(sql);
				pstmt1.setLong(1, no);
				pstmt1.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return vo;

	}

	public void updateOno(Long groupNo, Long orderNo) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();

			String sql = null;

			sql = "UPDATE BOARD set order_no = order_no + 1 WHERE group_no = ? AND ? < order_no ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, groupNo);
			pstmt.setLong(2, orderNo);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void insert(BoardVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConnection();
			if (vo.getGroupNo() == null) {
				String sql = "INSERT into BOARD values(seq_board.nextval, ?, ?, sysdate, 0, nvl((select max(group_no) from board), 0) + 1, 1, 0, ?)";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, vo.getTitle());
				pstmt.setString(2, vo.getContent());
				pstmt.setLong(3, vo.getUserNo());

				pstmt.executeUpdate();
			} else {
				// order_no 증가
				updateOno(vo.getGroupNo(), vo.getOrderNo());

				String sql = "INSERT into BOARD values(seq_board.nextval, ?, ?, sysdate, 0, ?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, vo.getTitle());
				pstmt.setString(2, vo.getContent());
				pstmt.setLong(3, vo.getGroupNo());				
				pstmt.setLong(4, vo.getOrderNo() + 1L);
				pstmt.setLong(5, vo.getDepth() + 1L);
				pstmt.setLong(6, vo.getUserNo());
				
				pstmt.executeUpdate();
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void delete(Long no) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {

			conn = getConnection();

			String sql = "DELETE FROM BOARD WHERE no = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, no);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("에러 : " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("에러 : " + e);
			}
		}

	}

	public int getTotalRecords(String key, int currentPage) {
		int totalRecords = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String cntQuery = "";

		try {
			conn = getConnection();
			cntQuery = "SELECT count(no) FROM BOARD WHERE 1=1 ";

			if (!"".equals(key)) {
				cntQuery += "AND title LIKE '%" + key + "%' AND content LIKE '%" + key + "%'";
			}

			pstmt = conn.prepareStatement(cntQuery);
			rs = pstmt.executeQuery();

			// 전체 레코드 수 추출
			if (rs.next()) {
				totalRecords = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return totalRecords;
	}

	public List<BoardVo> getList(String key, int currentPage, int PAGE_RECORDS) {

		List<BoardVo> list = new ArrayList<BoardVo>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String listQuery = "";
		try {
			conn = getConnection();

			listQuery = "SELECT * FROM (SELECT a.*,rownum rnum FROM ( SELECT a.no, a.title, a.content, to_char(a.reg_date, 'yyyy-mm-dd am hh:mi:ss' ), a.view_count, a.group_no, a.order_no, a.depth, a.user_no, b.name FROM BOARD a, USERS b WHERE a.USER_NO = b.no";

			if (!"".equals(key)) {
				listQuery += " AND title LIKE '%" + key + "%' AND content LIKE '%" + key + "%'";
			}

			listQuery += " ORDER BY a.group_no DESC, a.order_no ASC ) a ) where (" + currentPage + " -1)* "
					+ PAGE_RECORDS + "+1<= rnum and rnum <= " + currentPage + " * " + PAGE_RECORDS;

			pstmt = conn.prepareStatement(listQuery);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String content = rs.getString(3);
				String regDate = rs.getString(4);
				Long viewCount = rs.getLong(5);
				Long groupNo = rs.getLong(6);
				Long orderNo = rs.getLong(7);
				Long depth = rs.getLong(8);
				Long userNo = rs.getLong(9);
				String userName = rs.getString(10);

				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContent(content);
				vo.setRegDate(regDate);
				vo.setViewCount(viewCount);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setUserNo(userNo);
				vo.setUserName(userName);

				list.add(vo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}

				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
