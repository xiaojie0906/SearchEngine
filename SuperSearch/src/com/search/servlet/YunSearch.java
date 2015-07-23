package com.search.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class YunSearch
 */
@WebServlet("/search")
public class YunSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public String keyword = "2";
	public int pageno = 0;
	public int j = -1;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public YunSearch() {
		
		super();
		
		 System.out.print("addddddddddddd\n");
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException { 
		request.setCharacterEncoding("UTF-8");
		this.keyword = request.getParameter("search");
		this.pageno = Integer.parseInt(request.getParameter("pageno"));

		// ArrayList<String[]> urlList= new ArrayList<String[]>();
		if (this.keyword == "") {
			response.setContentType("text/html; charset=UTF-8");
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);

		} else {
			// urlList = this.Querry(this.keyword);
			// size = urlList.size();
			response.setContentType("text/html; charset=UTF-8");
			RequestDispatcher rd = request.getRequestDispatcher("result.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		this.keyword = request.getParameter("search");
		this.pageno = Integer.parseInt(request.getParameter("pageno"));

		// ArrayList<String[]> urlList= new ArrayList<String[]>();
		if (this.keyword == "") {
			response.setContentType("text/html; charset=UTF-8");
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);

		} else {
			// urlList = this.Querry(this.keyword);
			// size = urlList.size();
			response.setContentType("text/html; charset=UTF-8");
			RequestDispatcher rd = request.getRequestDispatcher("result.jsp");
			rd.forward(request, response);
		}
		
		
	}

}
