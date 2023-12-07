package com.java.www.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.www.service.IdCheckService;
import com.java.www.service.Service;

@WebServlet("/IdCheck")
public class IdCheck extends HttpServlet {
	
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doAction");
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
		
		System.out.println("IdCheck id : "+id);
		
		//service접근
		IdCheckService idCheckService = new IdCheckService();
		//리턴 - 사용가능, 사용불가능
		String result = idCheckService.idCheck(id);

		
		//ajacx 전송
		response.setCharacterEncoding("utf-8");
		//response.setContentType("text/html; charset=utf-8");//위 CharacterEncoding을쓸경우 이건 안 씀. 이걸 쓰게되면 아래 writer.println("<html>")을 쓴다.
		PrintWriter writer = response.getWriter();
		writer.print(result); //아이디가 존재하지않음, 사용가능
		//writer.print("fail");  //아이디가 존재함, 사용불가
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
