package kr.ac.sungkyul.mysite.web.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.ac.sungkyul.mysite.dao.BoardDao;
import kr.ac.sungkyul.mysite.vo.BoardVo;
import kr.ac.sungkyul.mysite.vo.UserVo;
import kr.ac.sungkyul.web.Action;

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		BoardVo vo = new BoardVo();

		String title = request.getParameter("title");
		String content = request.getParameter("content");

		vo.setTitle(title);
		vo.setContent(content);
		vo.setUserNo(authUser.getNo());
		
		if (null != request.getParameter("groupNo")) {
			Long groupNo = Long.parseLong(request.getParameter("groupNo"));
			Long orderNo = Long.parseLong(request.getParameter("orderNo"));
			Long depth = Long.parseLong(request.getParameter("depth"));
			
			vo.setGroupNo(groupNo);
			vo.setOrderNo(orderNo);
			vo.setDepth(depth);
		}

		BoardDao dao = new BoardDao();
		dao.insert(vo);

		response.sendRedirect("/mysite/board");

	}

}
