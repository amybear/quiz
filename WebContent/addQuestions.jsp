<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ page import = "java.util.*, java.text.*, quiz.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Add Questions for 
		<% Quiz quizCreated = (Quiz)request.getSession().getAttribute("quizCreated");
			out.print(quizCreated.getQuizName());
		%>
	</title>
	
	<style type="text/css">
      body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #000000;
      }
      
      .form-quiz {
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
    </style>
	
	<link href="../assets/css/bootstrap-responsive.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<link href='http://fonts.googleapis.com/css?family=Fugaz+One' rel='stylesheet' type='text/css'>
</head>

<body style="background-color:#f5f5f5">

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
	  </div><!-- /.container-fluid -->
	</nav>
	
	<div class="form-quiz" style="width:1000px">
	
	<h4><i>Choose a question type from the drop down menu and fill out the necessary fields. Press "Add Question" to add another
question or press "Done" if you are complete. Press "Add Answers" to add another possible answer to the question. Press "Remove Answers" to remove the last answer.
Please enter one answer per box and do not leave any boxes unfilled.<b>Please do not use "\n" in your inputs.</b></i></h4>

<script type="text/javascript">

//displays the corresponding question type and its fields.
function displayQuestion()
{
	var selected = document.getElementById("optionsBox").selectedIndex;
	var options = document.getElementById("optionsBox").options;
	if (options[selected].value == "QuestionResponse")
	{
		var display = "<h3>Question Response</h3> ";
		display+= "<input type=\"hidden\" name=\"questionType\" value =1>";
		display+= "<input type=\"hidden\" name=\"numAnswers\" value= 1>";
		display+= "<p>Question:<input type = \"text\" name=\"question\"></p>";
		display+= "<p id=\"answers\">Answers:<input type = \"text\" name=\"answer1\"></p>";
		document.getElementById("displayArea").innerHTML = display;
		var choiceButton = "";
		document.getElementById("choicesButton").innerHTML = choiceButton;
		var instructions = "<b>An example of a Question Response question is \"Who was the first president of the United States?\"</b>";
		document.getElementById("specialInstructions").innerHTML = instructions;
	}
	if (options[selected].value == "FillInTheBlank")
	{
		var display = "<h3>Fill in the Blank</h3> ";
		display+= "<input type=\"hidden\" name=\"questionType\" value =2>";
		display+= "<input type=\"hidden\" name=\"numAnswers\" value= 1>";
		display+= "<p>Question:<input type = \"text\" name=\"question\"></p>";
		display+= "<p id=\"answers\">Answers:<input type = \"text\" name=\"answer1\"></p>";
		document.getElementById("displayArea").innerHTML = display;
		var choiceButton = "";
		document.getElementById("choicesButton").innerHTML = choiceButton;
		var instructions = "<b>An example of a Fill in the Blank question is \"______ was the first president of the United States.\"</b>";
		document.getElementById("specialInstructions").innerHTML = instructions;
	}
	if (options[selected].value == "MultipleChoice")
	{
		var display = "<h3>Multiple Choice</h3> ";
		display+= "<input type=\"hidden\" name=\"questionType\" value =3>";
		display+= "<input type=\"hidden\" name=\"numAnswers\" value= 1>";
		display+= "<input type=\"hidden\" name=\"numChoices\" value= 1>";
		display+= "<p>Question:<input type = \"text\" name=\"question\"></p>";
		display+= "<p id=\"answers\">Answers:<input type = \"text\" name=\"answer1\"></p>";
		display+= "<p id= \"choices\">Choices:<input type = \"text\" name=\"choice1\"></p>";
		document.getElementById("displayArea").innerHTML = display;
		var choiceButton = "<input type=\"button\" value=\"Add Choice\" onclick=\"addChoiceBox()\">";
		choiceButton+= "<input type= \"button\" value = \"Remove Choice\" onclick=\"removeChoiceBox()\">";
		document.getElementById("choicesButton").innerHTML = choiceButton;
		var instructions = "<b>An example of a Multiple Choice question is \"Who was the first president of the United States?\"<br>";
		instructions+= "Press \"Add Choices\" to add another choice for the question. Press \"Remove Answers\" to remove the last answer. Please enter only one choice per box and do not leave any Choices boxes unfilled.</b>";
		document.getElementById("specialInstructions").innerHTML = instructions;
	}
	if (options[selected].value == "PictureResponse")
	{
		var display = "<h3>Picture Response</h3> ";
		display+= "<input type=\"hidden\" name=\"questionType\" value =4>";
		display+= "<input type=\"hidden\" name=\"numAnswers\" value= 1>";
		display+= "<p>Question:<input type = \"text\" name=\"question\"></p>";
		display+= "<p id=\"answers\">Answers:<input type = \"text\" name=\"answer1\"></p>";
		display+= "<p>Image URL:<input type = \"text\" name=\"imageURL\"></p>";
		document.getElementById("displayArea").innerHTML = display;
		var choiceButton = "";
		document.getElementById("choicesButton").innerHTML = choiceButton;
		var instructions = "<b>An example of a Picture Response question is a picture of George Washinton with the accompanying question \"Who is this President of the United States?\"<br>";
		instructions+= "The question field may be left blank for this type of question and only a picture will be shown. The URL for the image should be the exact URL to find the image at.</b>";
		document.getElementById("specialInstructions").innerHTML = instructions;
	}
	if (options[selected].value == "MultiAnswerMultipleChoice")
	{
		var display = "<h3>Multi-Answer Multiple Choice</h3> ";
		display+= "<input type=\"hidden\" name=\"questionType\" value =5>";
		display+= "<input type=\"hidden\" name=\"numAnswers\" value= 1>";
		display+= "<input type=\"hidden\" name=\"numChoices\" value= 1>";
		display+= "<p>Question:<input type = \"text\" name=\"question\"></p>";
		display+= "<p id=\"answers\">Answers:<input type = \"text\" name=\"answer1\"></p>";
		display+= "<p id= \"choices\">Choices:<input type = \"text\" name=\"choice1\"></p>";
		document.getElementById("displayArea").innerHTML = display;
		var choiceButton = "<input type=\"button\" value=\"Add Choice\" onclick=\"addChoiceBox()\">";
		choiceButton+= "<input type= \"button\" value = \"Remove Choice\" onclick=\"removeChoiceBox()\">";
		document.getElementById("choicesButton").innerHTML = choiceButton;
		var instructions = "<b>An example of a Multi-Answer Multiple Choice question is \"Select all that are true:\"<br> \"George Washington was the first president.\"<br>";
		instructions+="\"George Washington was the General of the Continental Army.\"<br>\"George Washinton was an English soldier in the Revolutionary War.\"<br>";
		instructions+="The right answers would be \"George Washington was the first president.\" and \"George Washington was the General of the Continental Army.\"<br>";
		instructions+= "Press \"Add Choices\" to add another choice for the question. Press \"Remove Answers\" to remove the last answer. Please enter only one choice per box and do not leave any Choices boxes unfilled.</b>";
		document.getElementById("specialInstructions").innerHTML = instructions;
	}
}


function addAnswerBox()
{
	document.frm.numAnswers.value = parseInt(document.frm.numAnswers.value)+1;
	var answers = document.getElementById("answers");
	answers.appendChild(document.createElement('div')).innerHTML = "<input type =\"text\" name =\"answer"+document.frm.numAnswers.value+ "\">";
}

function removeAnswerBox()
{
	var numAnswers = parseInt(document.frm.numAnswers.value);
	if(numAnswers > 1)
	{
		var answers = document.getElementById("answers");
		answers.removeChild(answers.lastChild);
		document.frm.numAnswers.value = (numAnswers - 1);
	}
}

function addChoiceBox()
{
	document.frm.numChoices.value = parseInt(document.frm.numChoices.value)+1;
	var choices = document.getElementById("choices");
	choices.appendChild(document.createElement('div')).innerHTML = "<input type =\"text\" name =\"choice"+document.frm.numChoices.value+ "\" />";
}

function removeChoiceBox()
{
	var numChoices = parseInt(document.frm.numChoices.value);
	if(numChoices > 1)
	{
		var choices = document.getElementById("choices");
		choices.removeChild(choices.lastChild);
		document.frm.numChoices.value = (numChoices - 1);
	}
}

function checkForm()//error checking to make sure fields are filled out properly
{
	var selected = document.getElementById("optionsBox").selectedIndex;
	var options = document.getElementById("optionsBox").options;
	var numAnswers = document.frm.numAnswers.value;
	var answerLength = 0;
	for(var i = 1; i <=  numAnswers; i++)
	{
		var array = document.getElementsByName("answer"+i);
		var length = array[0].value.length;
		answerLength += length;
		answerLength++;
	}
	if(document.frm.question.value == "")
	{
		alert("Please enter a Question.");
	}
	else if(document.frm.question.value.length > 5000)
	{
		alert("Question is too long. It must be less than 5000 characters.");
	}
	else if(document.frm.answer1.value == "")
	{
		alert("Please enter an Answer.");
	}
	else if(answerLength > 5000)
	{
		alert("Answers are too long. The answers combined should be less than 5000 characters.");
	}
	else if (options[selected].value == "MultipleChoice")
	{
		var numChoices = document.frm.numChoices.value;
		var choiceLength = 0;
		for(var i = 1; i <=  numChoices; i++)
		{
			var array = document.getElementsByName("choice"+i);
			var length = array[0].value.length;
			choiceLength += length;
			choiceLength++;
		}
		if(document.frm.choice1.value == "")
		{
			alert("Please enter a Choice.");
		}
		else if(choiceLength > 5000)
		{
			alert("Choices are too long. The choices combined should be less than 5000 characters.");
		}
		else
		{
			document.frm.submit();
		}
	}
	else if (options[selected].value == "PictureResponse")
	{
		if(document.frm.imageURL.value == "")
		{
			alert("Please enter an Image URL.");
		}else if (document.frm.imageURL.value.length > 5000){
			alert("This URL is too long. Please enter a URL shorter than 5000 characters.");
		}
		else
		{
			document.frm.submit();
		}
	}
	else if (options[selected].value == "MultiAnswerMultipleChoice")
	{
		var numChoices = document.frm.numChoices.value;
		var choiceLength = 0;
		for(var i = 1; i <=  numChoices; i++)
		{
			var array = document.getElementsByName("choice"+i);
			var length = array[0].value.length;
			choiceLength += length;
			choiceLength++;
		}
		if(document.frm.choice1.value == "")
		{
			alert("Please enter a Choice.");
		}
		else if(choiceLength > 5000)
		{
			alert("Choices are too long. The choices combined should be less than 5000 characters.");
		}
		else
		{
			document.frm.submit();
		}
	}
	else
	{
		document.frm.submit();
	}
}

function checkNumQuestions()
{
	var numQuestions = parseInt(document.doneFrm.numQuestions.value);
	if(numQuestions > 0)
	{
		document.doneFrm.submit();
	}
	else
	{
		alert("Quiz needs at least one question.");
	}
}
</script>



<div>
	<select id = "optionsBox" onchange="displayQuestion()">
		<option value = "QuestionResponse"> Question Response</option>
		<option value = "FillInTheBlank"> Fill in the Blank</option>
		<option value = "MultipleChoice"> Multiple Choice</option>
		<option value = "PictureResponse"> Picture Response</option>
		<option value = "MultiAnswerMultipleChoice"> Multi-Answer Multiple Choice</option>
	</select>
</div>

<div>
	<form name ="frm" action ="AddQuestionsServlet" method = "post">
		<div id ="displayArea">
			<h3>Question Response</h3>
			<input type="hidden" name="questionType" value =1>
			<input type="hidden" name="numAnswers" value= 1>
			<p>Question:<input type = "text" name="question"></p>
			<p id="answers">Answers:<input type = "text" name="answer1" ></p>
		</div>
		
		<input type ="button" value = "Add Answer" onclick ="addAnswerBox()">
		<input type= "button" value = "Remove Answer" onclick="removeAnswerBox()">
		<p id= "choicesButton"></p>
		<input type = "button" value = "Add Question" onclick="checkForm()" > 
	</form>
	
	<form name="doneFrm" action="QuizCreationCompletedServlet" method = "post">
		<input type= "hidden" name="numQuestions" value = <%out.print(quizCreated.getQuestions().size());%>>
		<input type = "button" value = "Done" onclick="checkNumQuestions()" >
	</form>
	
</div>

<hr noshade size=4>
<p id = "specialInstructions"><b>An example of a Question Response question is "Who was the first president of the United States?"</b></p>

</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

</body>
</html>