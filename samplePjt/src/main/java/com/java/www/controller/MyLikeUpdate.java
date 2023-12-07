package com.java.www.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.java.www.dao.BoardDao;
import com.java.www.service.IdCheckService;

@WebServlet("/MyLikeUpdate")
public class MyLikeUpdate extends HttpServlet {
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doAction");
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("session_id");
		int bno = Integer.parseInt(request.getParameter("bno"));
		int like_status = Integer.parseInt(request.getParameter("like_status"));
		
		System.out.println("MyLikeUpdate bno : "+bno);
		System.out.println("MyLikeUpdate like_status : "+like_status);
		
		
		//service접근
		//이렇게 만들어도 됨
		//MyLikeUpdateService MyLikeUpdateService = new MyLikeUpdateService();
		
		//dao접근(이렇게 사용하면 안 되는데 지금은 사용) - 좋아요 상태 수정
		BoardDao bdao = new BoardDao();
		int all_like_count = bdao.myLikeUpdate(id,bno,like_status);
		System.out.println("controller all_like_count : "+all_like_count);
		
		//ajacx 전송
		JSONObject json = new JSONObject();
		json.put("all_like_count", all_like_count); // 앞-key, 뒤-value
		
		response.setContentType("application/x-json; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print(json);
		writer.close();
		
		
		
		
	}//doAction
	
	
	//---------------------------
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet");
		doAction(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost");
		doAction(request, response);
	}

}
