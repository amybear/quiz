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
	String loginName = (String)session.getAttribute("loginName");
	User user = (User)session.getAttribute("user");
	DAL dal = (DAL)getServletContext().getAttribute("DAL");
	String name = request.getParameter("usernameToSearch");
	boolean hasNewMessages = dal.userHasNewMessages(loginName);
	ArrayList<Message> messagesNotifications = dal.getSevenMostRecentUserMessages(user);
	%>  
	
	<title> Profile Not Found </title>
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
	        <li class="active"><a href="friends.jsp" style="margin:0; padding:0"><img src="friends-icon.jpg" height="50px" height="50px"/></a></li>
	        <li class="active"><a href="messages.jsp" style="margin:0; margin-left:10px; padding:0"><img src="messages.jpg" height="50px" height="50px"/></a></li>
	        
	        <%if (hasNewMessages) {
	        	%> 
				<li class="navbar-form">
				<div class="dropdown">
				  <button class="btn dropdown-toggle btn-danger" type="button" id="dropdownMenu1" data-toggle="dropdown"> Notifications <span class="caret"></span> </button>
				  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
				  	
				  	<% for (int i = 0; i < messagesNotifications.size(); i++) {
				  		%><li role="presentation">
				  			<a role="menuitem" tabindex="-1" href="messages.jsp">
				  				<%
				  				Message message = messagesNotifications.get(i);
				  				String type = message.getMessageType();
				  				String fromUser = message.getFromUser();%>
				  				<%=type%> from <%=fromUser%> 
				  			</a>
				  		</li>
				  	<%}%>
				    <li role="presentation" class="divider"></li>
				    <li role="presentation"><a role="menuitem" tabindex="-1" href="messages.jsp">See All</a></li>
				  </ul>
				</div>
			</li>
				<% 
	        } else {%>
	        	<li class="navbar-form">
				<div class="dropdown">
				  <button class="btn dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown"> Notifications <span class="caret"></span> </button>
				  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
				  	
				  	<% for (int i = 0; i < messagesNotifications.size(); i++) {
				  		%><li role="presentation">
				  			<a role="menuitem" tabindex="-1" href="messages.jsp">
				  				<%
				  				Message message = messagesNotifications.get(i);
				  				String type = message.getMessageType();
				  				String fromUser = message.getFromUser();%>
				  				<%=type%> from <%=fromUser%> 
				  			</a>
				  		</li>
				  	<%}%>
				    <li role="presentation" class="divider"></li>
				    <li role="presentation"><a role="menuitem" tabindex="-1" href="messages.jsp">See All</a></li>
				  </ul>
				</div>
			</li>
			<%}%>
			
			<%if (user.getIsAdministrator()) {
				%> <li class="active"><a href="administratorHomepage.jsp">Home</a></li><%
			} else {
				%> <li class="active"><a href="userHomepage.jsp">Home</a></li>
			<%}%>
			
	        <li class="active"><a href="friendProfile.jsp?friendName=<%=loginName%>"><%=loginName%></a></li>
	        <li style="padding-right:10px">
	        	<form class="navbar-form navbar-right" name="logOut" action="LogOutServlet" method="post">
					<button type="submit" class="btn btn-default">Log Out</button>
				</form>
	        </li>
	      </ul>
	  </div><!-- /.container-fluid -->
	</nav>

	<p> Sorry, we couldn't find <b><%=name%>'s</b> profile :( </p>	
	

</body>
</html>






