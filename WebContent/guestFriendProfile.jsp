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
	String friendName = request.getParameter("friendName");
	DAL dal = (DAL) request.getServletContext().getAttribute("DAL");
	ArrayList<String> userRecentlyCreatedQuizzes = dal.getUserRecentlyCreatedQuizzes(friendName);
	ArrayList<String> userRecentlyTakenQuizzes = dal.getUserRecentlyTakenQuizzes(friendName);
	String achievements = dal.getUserAchievements(friendName);
	ArrayList<String> friends = dal.getFriendListForUser(friendName);
	%>  
	
	<title> <%=friendName%>'s Profile </title>
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link href='http://fonts.googleapis.com/css?family=Fugaz+One' rel='stylesheet' type='text/css'>
</head>

<body>

	<nav class="navbar navbar-default navbar-fixed-top navbar-inverse" role="navigation">
	  <div class="container-fluid">
	    <!-- Brand and toggle get grouped for better mobile display -->
	    <div class="navbar-header">
	      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
	        <span class="sr-only">Toggle navigation</span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	      </button>
	      <div><a class="navbar-brand" style="color:white; font-family:'Fugaz One', cursive; font-size:40px">Quizzler</a></div>
	    </div>
	
		<form class="navbar-form navbar-left" role="search" name="searchForUser" action="SearchForUserServlet" method="post">
			<div class="form-group">
				<input type="text" class="form-control" placeholder="Search for friends" name="usernameToSearch">
				<button type="submit" class="btn btn-default">Submit</button>
			</div>	
		</form>
		
	      <ul class="nav navbar-nav navbar-right">
	      	<li class="active"><a href="guestHomepage.jsp">Home</a></li>
	      	<li class="active"><a href="index.html">Log In</a>
	      	<li class="active"><a href="createAccount.html">Create Account</a>
	      </ul>
	  </div><!-- /.container-fluid -->
	</nav>
	
	<div class="row">
		<div class="col-md-2 col-md-offset-1">
			<h3><a href="guestFriendProfile.jsp?friendName=<%=friendName%>"><%= friendName %></a></h3>
		</div>
		
		<div class="col-md-6 well">
			<h3> Achievements: </h3>
			<%for (int i = 0; i < achievements.length(); i++) {
				if (achievements.charAt(i) == '1') {%>
					<div style="padding-left:10px">
						<img src="<%=Achievements.achievementsImgs[i]%>" height="30px" width="30px" />
						<%=Achievements.achievements[i]%>	
					</div> <% ;
				}
			}%> 
			
			<h3> <%=friendName%>'s Friends: </h3>
			<ul>
				<%
					for (int i = 0; i < friends.size(); i++) {
						String friend = friends.get(i);
						%> <li> <a href="guestFriendProfile.jsp?friendName=<%=friend%>"> <%=friend%> </a> </li> <%
					}
				%>
			</ul>
			
			<h3> <%=friendName%>'s Recently Created Quizzes: </h3> 
				<%for (int i = 0; i < userRecentlyCreatedQuizzes.size(); i++) {
					String quizName = userRecentlyCreatedQuizzes.get(i);
					%> <div><a href="guestQuizSummary.jsp?quizName=<%=quizName%>"> <%=quizName%> </a> </div> <% 
				}%>
				
			<h3> <%=friendName%>'s Recently Taken Quizzes: </h3>
				<%for (int i = 0; i < userRecentlyTakenQuizzes.size(); i++) {
						String quizName = userRecentlyTakenQuizzes.get(i);
						%> <div><a href="guestQuizSummary.jsp?quizName=<%=quizName%>"> <%=quizName%> </a></div> <% 
				}%>

		</div>
	
	</div>

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script> 
	<script type="text/javascript" src="http://twitter.github.com/bootstrap/assets/js/bootstrap-dropdown.js"></script>
	<script src="js/bootstrap.min.js"></script>

</body>
</html>






