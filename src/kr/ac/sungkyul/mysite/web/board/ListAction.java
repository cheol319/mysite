package kr.ac.sungkyul.mysite.web.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.ac.sungkyul.mysite.dao.BoardDao;
import kr.ac.sungkyul.mysite.vo.BoardVo;
import kr.ac.sungkyul.web.Action;
import kr.ac.sungkyul.web.WebUtil;

public class ListAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 페이징 변수
		int totalRecords = 0;
		int currentPage = 0;
		int number = 0;
		int totalPages = 0;
		int totalPageSets = 0;
		int currentPageSet = 0;

		// 페이지의 크기와 페이지 집합의 크기 지정
		final int PAGE_RECORDS = 10;
		final int PAGE_SETS = 5;

		// 현재 페이지번호 지정
		if (request.getParameter("cp") == null) {
			currentPage = 1;
		} else {
			currentPage = Integer.parseInt(request.getParameter("cp"));
		}

		// 검색 데이터 추출
		String key = request.getParameter("key");
//		String kwd = request.getParameter("kwd");
		if (key == null) {
			key = "";
		} 
//		if (kwd != null) {
//			key = kwd;
//		}

		BoardDao dao = new BoardDao();
		totalRecords = dao.getTotalRecords(key, currentPage);
		List<BoardVo> list = dao.getList(key, currentPage, PAGE_RECORDS);

		// 전체 페이지 수, 전체 페이지집합의 수, 현재 페이지집합 번호 추출
		totalPages = (int) Math.ceil((double) totalRecords / PAGE_RECORDS);
		totalPageSets = (int) Math.ceil((double) totalPages / PAGE_SETS);
		currentPageSet = (int) Math.ceil((double) currentPage / PAGE_SETS);

		// 이전 페이지 집합으로의 링크
		String beforePageSetSrc = "";
		if (currentPageSet > 1) {
			int beforePageSet = PAGE_SETS * (currentPageSet - 1);
			beforePageSetSrc = "/mysite/board?a=list&key=" + key + "&cp=" + beforePageSet;
		}
		
		// 이전 페이지로의 링크
		String beforePageSrc = "";
		if (currentPage > 1) {
			int beforePage = currentPage - 1;
			beforePageSrc = "/mysite/board?a=list&key=" + key + "&cp=" + beforePage;
		}

		// 현재 페이지 집합에서 출력할 첫 페이지 번호와 끝 페이지번호 추출
		int firstPage = PAGE_SETS * (currentPageSet - 1);
		int lastPage = PAGE_SETS * currentPageSet;

		if (currentPageSet == totalPageSets) {
			lastPage = totalPages;
		}

		// 다음 페이지로의 링크 작성
		String nextPageSrc = "";
		if (totalPages > currentPage) {
			int nextPage = currentPage + 1;
			nextPageSrc = "/mysite/board?a=list&key=" + key + "&cp=" + nextPage;

		} 

		// 다음 페이지 집합으로의 링크 작성
		String nextPageSetSrc = "";
		if (totalPageSets > currentPageSet) {
			int nextPageSet = PAGE_SETS * currentPageSet + 1;
			nextPageSetSrc = "/mysite/board?a=list&key=" + key + "&cp=" + nextPageSet;
		} 
		
		// 페이지별 가상 시작번호 생성
		number = totalRecords - (currentPage-1)*PAGE_RECORDS;		

		// db 데이터
		request.setAttribute("totalRecords", totalRecords);
		request.setAttribute("list", list);

		// 페이징 데이터
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("totalPageSets", totalPageSets);
		request.setAttribute("currentPageSet", currentPageSet);
		request.setAttribute("beforePageSetSrc", beforePageSetSrc);
		request.setAttribute("beforePageSrc", beforePageSrc);
		request.setAttribute("firstPage", firstPage);
		request.setAttribute("lastPage", lastPage);
		request.setAttribute("nextPageSrc", nextPageSrc);
		request.setAttribute("nextPageSetSrc", nextPageSetSrc);
		request.setAttribute("number", number);
		    
		WebUtil.forward("/WEB-INF/views/board/list.jsp?key=" + key + "&cp=" + currentPage, request, response);

	}

}
