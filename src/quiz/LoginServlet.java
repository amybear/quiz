package quiz;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet.
 * On a post request, checks whether entered username exists and the password
 * matches the username.  If it does, displays the welcome user page.  If it 
 * does not, displays the try again page.
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AccountManager manager;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * Gets username and password fields entered by the user and displays appropriate
	 * page depending on whether or not the username inputed exists and the password
	 * matches the username.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		manager = (AccountManager)getServletContext().getAttribute("manager");
		String username = (String)request.getParameter("username");
		String password = (String)request.getParameter("password");
		
		if (manager.accountExists(username) && manager.isPasswordForAccount(username, password)){
			
			//display user homepage
			RequestDispatcher dispatch = request.getRequestDispatcher("userHomepage.jsp");
			dispatch.forward(request,response);
			
			//Display "Welcome User" page
			/*response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE html>");
			out.println("<head>");
			out.println("<meta charset=\"UTF-8\" />");
			out.println("<title>Welcome " + username + "</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Welcome " + username + "</h1>");
			out.println("</body>");
			out.println("</html>");*/
		
		}else{
		
			//display try again page
			RequestDispatcher dispatch = request.getRequestDispatcher("tryAgainPage.html");
			dispatch.forward(request,response);
		}
	}
}
