package com.java.www.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.java.www.dao.BoardDao;
import com.java.www.dto.BoardDto;

public class N_SelectOneService implements Service {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		//id
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("session_id");
		//String id ="aaa"; /임시적으로 만들어줄때
		
		//dao접근
		int page = Integer.parseInt(request.getParameter("page"));
		int bno = Integer.parseInt(request.getParameter("bno"));
		String category = request.getParameter("category");
		String sword = request.getParameter("sword");
		BoardDao bdao = new BoardDao();
		BoardDto bdto = bdao.selectOne(bno);
		
		//--------------view일때
		//파일이름 추출(이전글 다음글)
		String uri = request.getRequestURI();
		String cPath = request.getContextPath();
		String fileName = uri.substring(cPath.length());
		BoardDto preDto = null;
		BoardDto nextDto = null;
		if(fileName.equals("/n_view.do")) {
			//pre +1
			preDto = bdao.preSelectOne(bno);
			//next -1
			nextDto = bdao.nextSelectOne(bno);
		}
		//---------------------------
		//좋아요: 내가 좋아요 누른상태, 전체좋아요 개수 - id, bno
		//
		int my_like_count = bdao.myLikeSelect(id,bno);
		int all_like_count = bdao.allLikeSelect(bno);
		
		
		
		
		
		
		
		
		
		//request추가
		request.setAttribute("bdto", bdto);
		request.setAttribute("preDto", preDto);
		request.setAttribute("nextDto", nextDto);
		request.setAttribute("page", page);
		request.setAttribute("category", category);
		request.setAttribute("sword", sword);
		request.setAttribute("my_like_count", my_like_count);
		request.setAttribute("all_like_count", all_like_count);

	}

}
