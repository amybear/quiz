package quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAL {

	/* Private instance variables */
	private DBConnection db;
	private Statement stmt;
	
	/* Constructor */
	public DAL() {
		db = new DBConnection();
		stmt = db.getStatement();
	}
	
	/* Getters */
	
	public boolean accountExists(String loginName) {
		String query = "SELECT * FROM users WHERE loginName = \"" + loginName + "\";";
		try {
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) { //If there exists a record with this loginName in the database return true
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isPasswordForAccount(String loginName, String passwordClear, String hashOfAttemptedPassword) {
		String query = "SELECT * FROM users WHERE loginName = \"" + loginName + "\";";
		try {
			ResultSet rs = stmt.executeQuery(query);
			String databasePasswordHash = rs.getString("password"); //Retrieves the stored hash from the Database for this User
			if (hashOfAttemptedPassword.equals(databasePasswordHash)) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<HistoryObject> getHistoryListForUser(String userName) {
		ArrayList<HistoryObject> historyList = new ArrayList<HistoryObject>();
		try {
			String query = "SELECT * FROM histories WHERE loginName = \"" + userName + "\";";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				String loginName = rs.getString("loginName");
				String quizName = rs.getString("quizName");
				double score = rs.getDouble("score");
				long timeElapsed = rs.getLong("timeElapsed");
				String dateString = rs.getString("date");
				historyList.add(new HistoryObject(loginName, quizName, score, timeElapsed, dateString, this));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return historyList;
	}
	
	public ArrayList<String> getFriendListForUser(String loginName) {
		ArrayList<String> friendList = new ArrayList<String>();
		try {
			String query1 = "SELECT * FROM friends WHERE user1 = \"" + loginName + "\";";
			ResultSet rs1 = stmt.executeQuery(query1);
			while(rs1.next()) {
				String user2 = rs1.getString(2);
				String query2 = "SELECT * FROM users WHERE loginName = \"" + user2 + "\";";
				ResultSet rs2 = stmt.executeQuery(query2);
				while (rs2.next()) {
					//add user to result (should friends just be an ArrayList<String>?)
					friendList.add(rs2.getString(1));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friendList;
	}
	
	public ArrayList<HistoryObject> getAllHistoryLists() {
		ArrayList<HistoryObject> result = new ArrayList<HistoryObject>();

		try {
			String query = "SELECT * FROM histories;";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				String loginName = rs.getString("loginName");
				String quizName = rs.getString("quizName");
				double score = rs.getDouble("score");
				long timeElapsed = rs.getLong("timeElapsed");
				String dateString = rs.getString("date");
				result.add(new HistoryObject(loginName, quizName, score, timeElapsed, dateString, this));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<TopScorer> getAllTopScorersForQuiz(String quizName) {
		ArrayList<TopScorer> topScorers = new ArrayList<TopScorer>();
		try {
			//Query database to get TopScorer's
			ResultSet topScorerResultSet = stmt.executeQuery("SELECT * FROM topscorers WHERE quizName = \"" + quizName + "\";");
			if (topScorerResultSet != null){
				topScorerResultSet.beforeFirst(); 
				
				while (topScorerResultSet.next()){
					String loginName = (String)topScorerResultSet.getObject("loginName");
					int numCorrectQuestions = (Integer)topScorerResultSet.getObject("numCorrectQuestions");
					double timeTaken = (Double)topScorerResultSet.getObject("timeTaken");
					
					//Add top scorer to TopScorer's array
					topScorers.add(new TopScorer(loginName, numCorrectQuestions, timeTaken, this));
				}

			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return topScorers;
	}
	
	////////////////////////
	
	/* Setters */
	
	public void insertUser(String loginName, boolean isAdministrator, String passwordHash, boolean[] achievements) {
		String achievementsString = "000000"; //Initialized to all 0's for all "false"
		try {
			String update = "INSERT INTO users VALUES(\"" + loginName + " \",\" " + isAdministrator + " \",\" " + passwordHash + " \",\" " + achievementsString + ");";
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	public void updateUserAchievements(String loginName, String achievementsString) {
		String update = "UPDATE users SET achievements = \"" + achievementsString + "WHERE loginName = " + loginName + "\";";
		try {
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void changeIsAdministrator(String loginName, boolean isAdmin) {
		String update = "UPDATE users SET isAdministrator = \"" + isAdmin + "WHERE loginName = " + loginName + "\";";
		try {
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addToHistoryListForUser(String loginName, String quizName, double score, long timeElapsed, String dateString) {
		try {
			String update = "INSERT INTO histories VALUES(\"" + loginName + "\",\"" + quizName + "\"," + score + "," + timeElapsed + ",\"" + dateString + "\");";
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addFriendPair(String user1, String user2) {
		try {
			String update = "INSERT INTO friends VALUES(\"" + user1 + "\",\"" + user2 + "\") , "
													+ "(\"" + user2 + "\",\"" + user1 + "\");";
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeFriendPair(String user1, String user2) { //Do we need?
		
	}
	
	public void addMessageForUser(String fromUser, String toUser, String type, String message, String quizName, double bestScore) {
		String update;
		if (type.equals(Message.NOTE_MESSAGE)) {
			update = "INSERT INTO users values(\"" + fromUser + "\", \"" + toUser + "\", \"" + type + "\", \"" + message + "\");";
		} else if (type.equals(Message.FRIEND_REQUEST_MESSAGE)) {
			update = "INSERT INTO users values(\"" + fromUser + "\", \"" + toUser + "\", \"" + type + "\", \"" + message + "\");";
		} else if (type.equals(Message.CHALLENGE_MESSAGE)) {
			update = "INSERT INTO users values(\"" + fromUser + "\", \"" + toUser + "\", \"" + type + "\", \"" + message + "\", \"" + quizName + "\", " + bestScore + ");";
		} else {
			update = ""; //Execution should never get here; just for initialization purposes
		}
		
		try {
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeMessageForUser(String loginName) { //Do we need?
		
	}
	
	public void addTopScorer(TopScorer topScorer, String quizName) {
		try {
			String update = "INSERT INTO topscorers VALUES(\""+quizName+"\",\""+topScorer.getLoginName()+"\","+ 
				topScorer.getNumCorrectQuestions()+","+topScorer.getTimeTaken()+");";
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	public void removeTopScorer(String loginName, String quizName) {
		try {
			String update = "DELETE FROM topscorers WHERE quizName = \"" + quizName + "\" AND loginName = \"" + loginName + "\";";
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	public void insertQuiz(String quizName, String descriptionOfQuiz, boolean isRandom, boolean isMultiplePage, boolean isImmediateCorrection, boolean canBeTakenInPracticeMode, String creatorName) {
		try {
			String update = "INSERT INTO quizzes VALUES(\""+quizName+"\",\""+descriptionOfQuiz+"\","+ isRandom+","+isMultiplePage+","+isImmediateCorrection+","+canBeTakenInPracticeMode+",\"" + creatorName + "\");";
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	
	
	public void populateQuiz(Quiz quiz, String givenQuizName) {
	
	}
	
	public void addQuestion(String quizName, Question question) {
		try {
			String update = "";
			if(question.getQuestionType() == Question.QUESTION_RESPONSE)
			{
				update = "INSERT INTO questionResponse VALUES(\""+quizName+"\",\""+question.getQuestion()
				+"\",\""+question.createAnswerString()+"\","+question.getQuestionType()+","
				+question.getQuestionNumber()+");";
			}
			if(question.getQuestionType() == Question.FILL_IN_THE_BLANK)
			{
				update = "INSERT INTO fillInTheBlank VALUES(\""+quizName+"\",\""+question.getQuestion()
				+"\",\""+question.createAnswerString()+"\","+question.getQuestionType()+","
				+question.getQuestionNumber()+");";
			}
			if(question.getQuestionType() == Question.MULTIPLE_CHOICE)
			{
				update = "INSERT INTO multipleChoice VALUES(\""+quizName+"\",\""+question.getQuestion()
				+"\",\""+question.createAnswerString()+"\","+question.getQuestionType()+","
				+question.getQuestionNumber()+",\""+((MultipleChoice)question).createChoicesString()+"\");";
			}
			if(question.getQuestionType() == Question.PICTURE_RESPONSE)
			{
				update = "INSERT INTO pictureResponse VALUES(\""+quizName+"\",\""+question.getQuestion()
				+"\",\""+question.createAnswerString()+"\","+question.getQuestionType()+","
				+question.getQuestionNumber()+",\""+((PictureResponse)question).getImageURL()+"\");";
			}
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace(); //TODO How do we want to handle this?
		}
	}
	
	public void removeQuestion(String quizName, Question question){
		try {
			String update = "";
			if(question.getQuestionType() == Question.QUESTION_RESPONSE)
			{
				update = "DELETE FROM questionResponse WHERE quizName = \"" + quizName + "\" AND question = \"" + question.getQuestion() + "\";";
			}
			if(question.getQuestionType() == Question.FILL_IN_THE_BLANK)
			{
				update = "DELETE FROM fillInTheBlank WHERE quizName = \"" + quizName + "\" AND question = \"" + question.getQuestion() + "\";";
			}
			if(question.getQuestionType() == Question.MULTIPLE_CHOICE)
			{
				update = "DELETE FROM multipleChoice WHERE quizName = \"" + quizName + "\" AND question = \"" + question.getQuestion() + "\";";
			}
			if(question.getQuestionType() == Question.PICTURE_RESPONSE)
			{
				update = "DELETE FROM pictureResponse WHERE quizName = \"" + quizName + "\" AND question = \"" + question.getQuestion() + "\";";
			}
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace(); //TODO How do we want to handle this?
		}
	}
	
	
	public ArrayList<Question> getQuestionsFromDB(String quizName){
		ArrayList<Question> questions = new ArrayList<Question>();
		try{
			//for question response
			ResultSet qrs = stmt.executeQuery("SELECT * FROM questionResponse WHERE quizName = \"" 
				+ quizName + "\";");
			if (qrs!=null){
				qrs.beforeFirst();
				while(qrs.next())
				{
					questions.add(new QuestionResponse(qrs.getString(2),Question.createArray(qrs.getString(3)),
							qrs.getInt(5)));
				}
			}
			
			//for fill-in-the-blank
			qrs = stmt.executeQuery("SELECT * FROM fillInTheBlank WHERE quizName = \"" 
				+ quizName + "\";");
			if (qrs!=null){
				qrs.beforeFirst();
				while(qrs.next())
				{
					questions.add(new FillInTheBlank(qrs.getString(2),Question.createArray(qrs.getString(3)),
							qrs.getInt(5)));
				}
			}
			
			//for multiple choice
			qrs = stmt.executeQuery("SELECT * FROM multipleChoice WHERE quizName = \"" 
				+ quizName + "\";");
			if (qrs!=null){
				qrs.beforeFirst();
				while(qrs.next())
				{
					questions.add(new MultipleChoice(qrs.getString(2),Question.createArray(qrs.getString(3)),
							qrs.getInt(5), Question.createArray(qrs.getString(6))));
				}
			}
			
			//for picture response
			qrs = stmt.executeQuery("SELECT * FROM pictureResponse WHERE quizName = \"" 
				+ quizName + "\";");
			if (qrs!=null){
				qrs.beforeFirst();
				while(qrs.next())
				{
					questions.add(new PictureResponse(qrs.getString(2),Question.createArray(qrs.getString(3)),
							qrs.getInt(5), qrs.getString(6)));
				}
			}
		} catch (SQLException e){
			e.printStackTrace(); //TODO How do we want to handle this?
		}
		return questions;
	}
	
	public String getNameOfQuiz(String givenQuizName){
		try{
			ResultSet quizResultSet = stmt.executeQuery("SELECT * FROM quizzes WHERE quizName = \"" + givenQuizName + "\";");
			if (quizResultSet!=null){
				quizResultSet.first();
				return (String)quizResultSet.getObject("quizName");
			}
		} catch (SQLException e){
			e.printStackTrace(); //TODO How do we want to handle this?
		}
		return "";
	}
	
	public String getDescriptionOfQuiz(String givenQuizName){
		try{
			ResultSet quizResultSet = stmt.executeQuery("SELECT * FROM quizzes WHERE quizName = \"" + givenQuizName + "\";");
			if (quizResultSet!=null){
				quizResultSet.first();
				return (String)quizResultSet.getObject("description");
			}
		} catch (SQLException e){
			e.printStackTrace(); //TODO How do we want to handle this?
		}
		return "";
	}
	
	public boolean getIsRandomOfQuiz(String givenQuizName){
		try{
			ResultSet quizResultSet = stmt.executeQuery("SELECT * FROM quizzes WHERE quizName = \"" + givenQuizName + "\";");
			if (quizResultSet!=null){
				quizResultSet.first();
				return (Boolean) quizResultSet.getObject("isRandom");
			}
		} catch (SQLException e){
			e.printStackTrace(); //TODO How do we want to handle this?
		}
		return false;
	}
	
	public boolean getMultiplePageOfQuiz(String givenQuizName){
		try{
			ResultSet quizResultSet = stmt.executeQuery("SELECT * FROM quizzes WHERE quizName = \"" + givenQuizName + "\";");
			if (quizResultSet!=null){
				quizResultSet.first();
				return (Boolean) quizResultSet.getObject("isMultiplePage");
			}
		} catch (SQLException e){
			e.printStackTrace(); //TODO How do we want to handle this?
		}
		return false;
	}
	
	public boolean getIsImmediateCorrectionOfQuiz(String givenQuizName){
		try{
			ResultSet quizResultSet = stmt.executeQuery("SELECT * FROM quizzes WHERE quizName = \"" + givenQuizName + "\";");
			if (quizResultSet!=null){
				quizResultSet.first();
				return (Boolean) quizResultSet.getObject("isImmediateCorrection");
			}
		} catch (SQLException e){
			e.printStackTrace(); //TODO How do we want to handle this?
		}
		return false;
	}
	
	public boolean getCanBeTakenInPracticeModeOfQuiz(String givenQuizName){
		try{
			ResultSet quizResultSet = stmt.executeQuery("SELECT * FROM quizzes WHERE quizName = \"" + givenQuizName + "\";");
			if (quizResultSet!=null){
				quizResultSet.first();
				return (Boolean) quizResultSet.getObject("canBeTakenInPracticeMode");
			}
		} catch (SQLException e){
			e.printStackTrace(); //TODO How do we want to handle this?
		}
		return false;
	}
	
	public String getCreatorName(String givenQuizName){
		try{
			ResultSet quizResultSet = stmt.executeQuery("SELECT * FROM quizzes WHERE quizName = \"" + givenQuizName + "\";");
			if (quizResultSet!=null){
				quizResultSet.first();
				return (String) quizResultSet.getObject("creatorName");
			}
		} catch (SQLException e){
			e.printStackTrace(); //TODO How do we want to handle this?
		}
		return "";
	}

	public void createAnnouncement(String announcement) {
		try {
			String update = "INSERT INTO announcements VALUES(\"" + announcement + "\");";
			stmt.executeUpdate(update);
		} catch (SQLException e){
			e.printStackTrace(); 
		}
	}
	
	public ArrayList<String> getAllAnnouncements() {
		ArrayList<String> announcements = new ArrayList<String>();
		String query = "SELECT * FROM announcements;";
		try {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				announcements.add(rs.getString("announcement"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcements;
	}
	
}






