<%@ page import="java.util.*, quiz.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">

<head>
	<meta content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Question Response</title>
	<link href="css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

<%
Quiz quiz = (Quiz)request.getSession().getAttribute("quiz");
int questionNum = quiz.getCurrentQuestionNum();
Question thisQuestion = quiz.getQuestions().get(questionNum);
%>

<h3><%= quiz.getQuizName() %> by <%= quiz.getCreatorName() %></h3>
<h4>Question <%= quiz.getCurrentQuestionNum()+1 %>: <%= thisQuestion.getQuestion() %></h4>
<img src="<%= ((PictureResponse)thisQuestion).getImageURL() %>" height="250" width="250"><br>

<form action="" method="post">
	Your Answer: <input type="text" class="span2" name="answer" />
</form>

<h5>Sorry, your answer was incorrect.</h5>
<h5>Correct Answer: <%= thisQuestion.getAnswer().get(0) %></h5>

<%
if (questionNum == quiz.getQuestions().size()-1){
	out.println("<form action=\"DoneWithQuizServlet\" method=\"post\">");
	out.println("<input type=\"submit\" class=\"btn btn-primary\" value=\"Done\">");
}else{
	out.println("<form action=\"NextQuestionServlet\" method=\"post\">");
	out.println("<input type=\"submit\" class=\"btn btn-primary\" value=\"Next Question\">");
}
%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

</body>
</html>