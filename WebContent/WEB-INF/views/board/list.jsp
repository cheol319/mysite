<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 

<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/mysite/assets/css/board.css" rel="stylesheet"	type="text/css">
</head>
<body>
	<div id="container"> 
		<c:import url='/WEB-INF/views/include/header.jsp'/>
		<div id="content">
			<div id="board">
				<form id="search_form" action="/mysite/board?a=list" method="get">
					<input type="text" id=key name="key" value="">
					<input type="hidden" id=cp name="cp" value="1">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>				
										
					<c:set var='countList' value='${fn:length(list)}'/>
					<c:forEach var='vo' items='${list }' varStatus='status'>
						
						<tr>
							<td>[${number - status.index }]</td>							
							<td style="text-align:left; padding-left:${vo.depth * 10}px"><a href="/mysite/board?a=view&no=${vo.no }"> 
								<c:if test='${vo.depth > 0 }'> 
									<img src="/mysite/assets/images/re.gif"/s>
								</c:if>  
								${vo.title }  </a></td>
							<td>${vo.userName }</td>
							<td>${vo.viewCount }</td>
							<td>${vo.regDate }</td>
							<td>
								<c:if test='${not empty authUser && vo.userNo == authUser.no }'>
									<a href="/mysite/board?a=delete&no=${vo.no }" class="del">삭제</a> 
								</c:if> 
							</td>
						</tr>
					</c:forEach>				
				</table>				
				<!-- begin:paging -->
				<div class="pager">
					<ul>
						<c:if test='${beforePageSetSrc != "" }'>
							<li><a href=${beforePageSetSrc }> << </a></li>
						</c:if>
						
						<c:if test='${beforePageSrc != "" }'>
							<li><a href=${beforePageSrc }> ◀ </a></li>
						</c:if>
						
						<c:forEach begin='${firstPage + 1 }' end='${lastPage }' var='i' step='1'>  
							<c:choose>
								<c:when test='${param.cp == i }'>
									<li class="selected">${i }</li>								
								</c:when>
								<c:otherwise>
									<li><a href="/mysite/board?a=list&key=${param.key }&cp=${i }">${i }</a></li>									
								</c:otherwise>
							</c:choose>							
						</c:forEach>
						
						<c:if test='${nextPageSrc != "" }'>
							<li><a href=${nextPageSrc }> ▶ </a></li>
						</c:if>
						
						<c:if test='${nextPageSetSrc != "" }'>
							<li><a href=${nextPageSetSrc }> >> </a></li>
						</c:if>
						
					</ul>
				</div>
				<!-- end:paging -->

				<div class="bottom">
				<c:if test='${not empty authUser }'>
					<a href="/mysite/board?a=wf" id="new-book">글쓰기</a>
				</c:if>
				</div>
			</div>
		</div>
		<c:import url='/WEB-INF/views/include/navi.jsp'/>
		<c:import url='/WEB-INF/views/include/footer.jsp'/>
	</div>
</body>
</html>