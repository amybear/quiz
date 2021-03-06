package quiz;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class User {
	
	/* Instance variables*/
	public boolean isAdministrator;
	private String loginName;
	private String passwordHash;
	private boolean hasNewMessages;
	
	private ArrayList<String> friends;
	private ArrayList<HistoryObject> historyList;
	public boolean[] achievements;
	private ArrayList<Message> messages;
	private int recentAchievement;
	private String recentlyCreatedQuiz;
	private String recentlyTakenQuiz;
	
	private ArrayList<String> recentlyTakenQuizzes;
	private ArrayList<String> recentlyCreatedQuizzes; 
	private ArrayList<FriendRecentActivity> friendsRecentActivity;
	
	private DAL dal; //Handles connection to Database
	
	/* Helper method to set all achievements to false */
	public void initAchievementsArray() {
		for (int i = 0; i < achievements.length; i++) {
			achievements[i] = false;
		}
	}
	
	/* Private helper method. Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array. */
	private String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/* Private helper method to convert the user's password from clear text
	 * to a hash, and to store the hash as an instance variable */
	private void hashPassword(String password) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA"); //Initialize the MessageDigest
			md.reset();
			byte[] buffer = password.getBytes(); //Converts password to array of bytes
			md.update(buffer);
			byte[] digest = md.digest();
			String result = hexToString(digest);
			this.passwordHash = result;
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace(); //TODO: How should we handle this exception?
		}
	}
	
	private ArrayList<HistoryObject> initializeHistoryList() {
		return dal.getHistoryListForUser(this.loginName);
	}
	
	private ArrayList<String> initializeFriends() {
		return dal.getFriendListForUser(this.loginName);
	}
	
	private ArrayList<FriendRecentActivity> initializeFriendsRecentActivity(ArrayList<String> friends)
	{
		return dal.getFriendsRecentActivity(friends);
	}
	
	private ArrayList<Message> initializeMessages() {
		return dal.getUserMessages(this);
	}
	
	
	/* Constructor */
	public User(String loginName, String password, DAL dal) {
		this.dal = dal;
		this.loginName = loginName;
		hashPassword(password);
		this.isAdministrator = false; //By default, a user is not an administrator
		this.hasNewMessages = false;
		achievements = new boolean[Achievements.NUM_ACHIEVEMENTS];
		initAchievementsArray();
		dal.insertUser(loginName, isAdministrator, passwordHash, achievements, null);
		
		friends = new ArrayList<String>();
		friends = initializeFriends();
		//friendsRecentActivity = initializeFriendsRecentActivity(friends);
		
		historyList = initializeHistoryList();
		messages = new ArrayList<Message>();
		messages = initializeMessages();
		
		recentlyTakenQuizzes = new ArrayList<String>();
		recentlyCreatedQuizzes = new ArrayList<String>();
	}
	
	/* Alternate constructor for not inserting User in database */
	public User(String loginName) {
		this.loginName = loginName;
		achievements = new boolean[Achievements.NUM_ACHIEVEMENTS];
		initAchievementsArray();
	}
	
	/* Getter methods */
	
	public String getLoginName() {
		return this.loginName;
	}
	
	public String getPasswordHash() {
		return this.passwordHash;
	}
	
	public boolean getIsAdministrator() {
		return this.isAdministrator;
	}
	
	public ArrayList<String> getFriends() {
		return this.friends;
	}
	
	public ArrayList<HistoryObject> getHistory() {
		return this.historyList;
	}
	
	public boolean[] getAchievements() {
		return this.achievements;
	}
	
	public ArrayList<Message> getMessages() {
		return this.messages;
	}
	
	public ArrayList<FriendRecentActivity> getListOfFriendsRecentActivities() {
		return this.friendsRecentActivity;
	}
	
	public ArrayList<String> getRecentlyTakenQuizzes() {
		return this.recentlyTakenQuizzes;
	}
	
	public ArrayList<String> getRecentlyCreatedQuizzes() {
		return this.recentlyCreatedQuizzes;
	}
	
	public boolean getNewMessages() {
		return hasNewMessages;
	}
	 
	////////////////////////////////////////////////////////////////
	/* Setter methods */
	
	public void setNewPassword(String newPasswordHash) {
		this.passwordHash = newPasswordHash;
	}
	
	public void setHasNewMessagesFalse() {
		hasNewMessages = false;
	}
	
	public void takeQuiz(HistoryObject ho) {
		dal.addToHistoryListForUser(ho.getUserName(), ho.getQuizName(), ho.getNumQuestionsCorrect(), ho.getElapsedTime(), ho.getDate());
		this.recentlyTakenQuizzes = dal.getUserRecentlyTakenQuizzes(ho.getUserName());
		//Update recently taken quizzes, in the database, and the instance variable
		//Updates recentActivity field for user in database
		recentlyTakenQuiz = ho.getQuizName();
		updateRecentActivity(recentAchievement, recentlyTakenQuiz, recentlyCreatedQuiz);
	}
	
	public void createQuiz(Quiz quiz) {
		dal.insertQuiz(quiz);
		this.recentlyCreatedQuizzes = dal.getUserRecentlyCreatedQuizzes(loginName);
		//Update recently created quizzes, in the database, and the instance variable 
		//Updates recentActivity field for user in database
		recentlyCreatedQuiz = quiz.getQuizName();
		updateRecentActivity(recentAchievement, recentlyTakenQuiz, recentlyCreatedQuiz);
	}
	
	public void addFriendPair(String friendName) { 
		friends.add(friendName);
		dal.addFriendPair(loginName, friendName);
	}	
	
	public void removeFriendPair(String friendName) { //TODO Database?
		friends.remove(friendName);
		dal.removeFriendPair(loginName, friendName);
	}
	 
	public void addMessage(User toUser, String type, String message, Quiz quiz) { 
		if (type.equals(Message.NOTE_MESSAGE)) {
			NoteMessage nm = new NoteMessage(this.loginName, toUser.getLoginName(), message, dal);
			messages.add(nm);
			dal.addMessageForUser(this.loginName, toUser.getLoginName(), Message.NOTE_MESSAGE, message, null, -1, new Date());
		} else if (type.equals(Message.FRIEND_REQUEST_MESSAGE)) {
			FriendRequestMessage frm = new FriendRequestMessage(this.loginName, toUser.getLoginName(), dal);
			messages.add(frm);
			dal.addMessageForUser(this.loginName, toUser.getLoginName(), Message.FRIEND_REQUEST_MESSAGE, message, null, -1, new Date());
		} else if (type.equals(Message.CHALLENGE_MESSAGE)) {
			ChallengeMessage cm = new ChallengeMessage(this.loginName, toUser.getLoginName(), quiz, dal);
			messages.add(cm);
			dal.addMessageForUser(this.loginName, toUser.getLoginName(), Message.CHALLENGE_MESSAGE, message, quiz.getQuizName(), cm.challengingUserBestScore(this.loginName), new Date());
		}
		hasNewMessages = true;
	}
	
	public void removeMessage(Message message) { //TODO Database?
		messages.remove(message);
		dal.removeMessageForUser(loginName);
	}
	
	public void addAchievement(int index) {
		achievements[index] = true; //Updates the instance variable 
		String achievementsString = "";
		for (int i = 0; i < Achievements.NUM_ACHIEVEMENTS; i++) {
			if (i == index) {
				achievementsString.concat("1"); //Sets this achievement to be true
			} else {
				achievementsString.concat(achievements[i] + ""); //Leaves the achievement as before
			}
		}
		dal.updateUserAchievements(this.loginName, achievementsString);
		//updates recentActivity field for user in database
		recentAchievement = index;
		updateRecentActivity(recentAchievement, recentlyTakenQuiz, recentlyCreatedQuiz);
	}
	
	public void deleteAchievement(int index){
		achievements[index] = false; //Updates the instance variable
		String achievementsString = "";
		for (int i = 0; i < Achievements.NUM_ACHIEVEMENTS; i++) {
			if (i == index) {
				achievementsString.concat("1"); //Sets this achievement to be true
			} else {
				achievementsString.concat(achievements[i] + ""); //Leaves the achievement as before
			}
		}
		dal.updateUserAchievements(this.loginName, achievementsString);	
	}
	
	public void updateRecentActivity(int achievement, String recentQuizTaken, String recentQuizCreated)
	{
		String recentActivity = ""+achievement+"\n"+
			recentQuizTaken+"\n"+recentQuizCreated;
		//dal method to update recentActivity
		dal.updateRecentUserActivity(loginName, recentActivity);
	}
	
	////////////////////////////////////////////////////////////////
	/* Administrative methods */
	
	/**
	 * Only users who are administrators can create announcements to display on the homepage
	 */
	public void createAnnouncement(String announcement) {
		if (isAdministrator) {
			dal.createAnnouncement(announcement);
		}
	}
	
	/**
	 * Only users who are administrators can remove user accounts
	 */
	public void removeUserAccount(String username) {
		dal.removeUser(username);
	}
	
	/**
	 * Only users who are administrators can remove quizzes
	 */
	public void removeQuiz(String quizName) {
		dal.removeQuiz(quizName);
	}
	
	/* Private helper method: can't be called outside of the class */
	private void updateAdminInstanceVariable(boolean value) {
		this.isAdministrator = value; //Updates the admin instance variable
	}

	
	/**
	 * Only users who are administrators can promote user accounts to administration accounts
	 */
	public void setAsAdministrator(User user) {
		user.updateAdminInstanceVariable(true); //Updates the instance variable
		dal.changeIsAdministrator(user.getLoginName(), true); //Changes it in the database
	}
	
	/**
	 * Only users who are administrators can promote user accounts to administration accounts
	 */
	public void setAsNotAdministrator(User user) {
		user.updateAdminInstanceVariable(false); //Updates the instance variable
		dal.changeIsAdministrator(user.getLoginName(), false); //Changes it in the database
	}
	
	/**
	 * Only users who are administrators can clear all history information for a particular quiz
	 */
	public void clearAllHistoryForQuiz(String quizName) {
		dal.clearAllHistoryForQuiz(quizName);
	}
	
	/**
	 * Administrative site statistics getter: number of users
	 */
	public int getNumberOfUsers() {
		return dal.getNumberOfUsers();
	}
	
	/**
	 * Administrative site statistics getter: number of quizzes taken
	 */
	public int getNumberOfQuizzesTaken() {
		return dal.getNumberOfQuizzesTaken();
	}

	/**
	 * Administrative site statistics getter: number of quizzes created
	 */
	public int getNumberOfQuizzesCreated() {
		return dal.getNumberOfQuizzesCreated();
	}
	
	
}





