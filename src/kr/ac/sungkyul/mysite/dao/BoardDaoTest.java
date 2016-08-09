package kr.ac.sungkyul.mysite.dao;

import java.util.List;

import kr.ac.sungkyul.mysite.vo.BoardVo;

public class BoardDaoTest {

	public static void main(String[] args) {
		BoardDao dao = new BoardDao();
		
		Long no = 2L;
		BoardVo vo = dao.getContent(no);
		System.out.println(vo);
		

	}

}
