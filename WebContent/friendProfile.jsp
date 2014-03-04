<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*, quiz.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<%
	String name = request.getParameter("friendName");
	String userName = (String)session.getAttribute("loginName");
	
	DAL dal = (DAL) request.getServletContext().getAttribute("DAL");
	ArrayList<String> userRecentlyCreatedQuizzes = dal.getUserRecentlyCreatedQuizzes(name);
	ArrayList<String> userRecentlyTakenQuizzes = dal.getUserRecentlyTakenQuizzes(name);
	String achievements = dal.getUserAchievements(name);
	ArrayList<String> friends = dal.getFriendListForUser(name);
	%>  
	
	<title> <%= name %>'s Profile </title>
	<link href="css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

	<h2><%= name %>'s Profile</h2>
	
	<%
	if(dal.getFriendListForUser(userName).contains(name)) {%> 
		<form name="removeFriend" action ="RemoveFriendServlet" method="post">
			<input type="hidden" name="user2" value="<%=name%>">
			<input type="submit" value="Remove friend">
		</form>
	<%} else {%>
		<form name="addFriend" action ="AddFriendServlet" method="post">
			<input type="hidden" name="user2" value="<%=name%>">
			<input type="submit" value="Add friend!">
		</form>
	<%}%>

	<h2> Achievements: </h2>
	<ul>
		<% 
			for (int i = 0; i < achievements.length(); i++) {
				if (achievements.charAt(i) == '1') {
					%> <li> <%= Achievements.achievements[i] %></li> <% ;	
				}
			}
			%> 
	</ul>
	
	<h2> <%=name%>'s Recently Created Quizzes: </h2>
	<ul>
		<%
			for (int i = 0; i < userRecentlyCreatedQuizzes.size(); i++) {
				%> <li> Quiz: <%= userRecentlyCreatedQuizzes.get(i) %></li> <% 
			}
		%>
	</ul>
	
	<h2> <%=name%>'s Recently Taken Quizzes: </h2>
	<ul>
		<%
			for (int i = 0; i < userRecentlyTakenQuizzes.size(); i++) {
				%> <li> Quiz: <%= userRecentlyTakenQuizzes.get(i) %></li> <% 
			}
		%>
	</ul>
	
	<h2> <%=name%>'s Friends: </h2>
	<ul>
		<%
			for (int i = 0; i < friends.size(); i++) {
				String friend = friends.get(i);
				%> <li> <a href="friendProfile.jsp?friendName=<%=friend%>"> <%=friend%> </a> </li> <%
			}
		%>
	</ul>
	
	<%
	if (dal.isUserAdmin(name)) {
		%> Go back <a href="administratorHomepage.jsp">home</a><% 
	} else {
		%> Go back <a href="userHomepage.jsp">home</a><% 
	}%>

</body>
</html>






