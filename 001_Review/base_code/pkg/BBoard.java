package pkg;
import java.util.*;
import java.io.*;

public class BBoard {		// This is your main file that connects all classes.
	// Think about what your global variables need to be.
	ArrayList<User> users = new ArrayList<User>();
	ArrayList<Message> messages = new ArrayList<Message>();
	User currentUser;
	int messId = 1;
	String title;
	// Default constructor that creates a board with a default title, empty user and message lists,
	// and no current user
	
	public BBoard() {
		title = "";
	}

	// Same as the default constructor except it sets the title of the board
	public BBoard(String ttl) {	
		title = new String(ttl);
	}

	// Gets a filename of a file that stores the user info in a given format (users.txt)
	// Opens and reads the file of all authorized users and passwords
	// Constructs a User object from each name/password pair, and populates the userList ArrayList.
	public void loadUsers(String inputFile) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(inputFile));
		while(sc.hasNextLine()){
			String x = sc.nextLine();
			String[] split = x.split(" ");
			for(int i = 0; i < split.length; i++){
				split[i].trim();
				User temp = new User(split[0], split[1]);
				users.add(temp);
			}
		}
	}

	// Asks for and validates a user/password. 
	// This function asks for a username and a password, then checks the userList ArrayList for a matching User.
	// If a match is found, it sets currentUser to the identified User from the list
	// If not, it will keep asking until a match is found or the user types 'q' or 'Q' as username to quit
	// When the users chooses to quit, sayu "Bye!" and return from the login function
	public boolean login(){
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter your username ('Q' or 'q' to quit): ");
		String inputUser = sc.nextLine();
		if((inputUser.toLowerCase()).equals("q")){
			System.out.println("Bye!");
			return false;
		}
		System.out.print("Enter your password: ");
		String inputPass = sc.nextLine();
		System.out.println("");
		for(int i = 0; i < users.size(); i++){
			// System.out.println(inputUser + " + " + inputPass);
			if(users.get(i).check(inputUser, inputPass)){
				currentUser = users.get(i);
				return true;
			}
		}
		System.out.println("Invalid User");
		login();
		return true;
	}
	
	// Contains main loop of Bulletin Board
	// IF and ONLY IF there is a valid currentUser, enter main loop, displaying menu items
	// --- Display Messages ('D' or 'd')
	// --- Add New Topic ('N' or 'n')
	// --- Add Reply ('R' or 'r')
	// --- Change Password ('P' or 'p')
	// --- Quit ('Q' or 'q')
	// With any wrong input, user is asked to try again
	// Q/q should reset the currentUser to 0 and then end return
	// Note: if login() did not set a valid currentUser, function must immediately return without showing menu
	public void run(){
		System.out.println(title);
		if(login()){
			System.out.println("Welcome back " + currentUser.getUsername() + "!\n");
			while(true){
			Scanner sc = new Scanner(System.in);
			System.out.println("Menu");
			System.out.print("  - Display Messages ('D' or 'd')\n  - Add New Topic ('N' or 'n')\n  - Add Reply ('R' or 'r')\n  - Change Password ('P' or 'p')\n  - Quit ('Q' or 'q')\n");
			System.out.print("Choose an action: ");
			String newTask = sc.nextLine();
			System.out.println();
			
			if(newTask.toLowerCase().equals("q")){
				currentUser = new User();
				System.out.println("Bye!");
				return;
			}
			else if(newTask.toLowerCase().equals("n")){
				addTopic();
			}
			else if(newTask.toLowerCase().equals("p")){
				
				setPassword();
			}
			else if(newTask.toLowerCase().equals("d")){
				display();
			}
			else if(newTask.toLowerCase().equals("r")){
				addReply();
			}
			else{
				System.out.println("Wrong Input - Please enter another.");
			}
			}
		}
	}

	// Traverse the BBoard's message list, and invote the print function on Topic objects ONLY
	// It will then be the responsibility of the Topic object to invoke the print function recursively on its own replies
	// The BBoard display function will ignore all reply objects in its message list
	private void display(){
		for(int i = 0; i < messages.size(); i++){
			System.out.println("--------------------------------------------");
			System.out.println("Message #" + (messages.get(i)).getId() + ": " + '"' + (messages.get(i)).getSubject() + '"');
			System.out.println("From " + currentUser.getUsername() + ":" + '"' + (messages.get(i)).getBody() + '"');
			(messages.get(i)).print(1);
			System.out.println("--------------------------------------------");
		}
	}


	// This function asks the user to create a new Topic (i.e. the first message of a new discussion "thread")
	// Every Topic includes a subject (single line), and body (single line)

	/* 
	Subject: "Thanks"
	Body: "I love this bulletin board that you made!"
	*/

	// Each Topic also stores the username of currentUser; and message ID, which is (index of its Message + 1)

	// For example, the first message on the board will be a Topic who's index will be stored at 0 in the messageList ArrayList,
	// so its message ID will be (0+1) = 1
	// Once the Topic has been constructed, add it to the messageList
	// This should invoke your inheritance of Topic to Message
	private void addTopic(){
		Scanner sc = new Scanner(System.in);
		System.out.print("Subject: ");
		String input = sc.nextLine();
		System.out.print("Body: ");
		String input2 = sc.nextLine();
		System.out.println();
		Topic incon = new Topic(currentUser.getUsername(), input, input2, messId);
		messId++;
		messages.add(incon);
	}

	// This function asks the user to enter a reply to a given Message (which may be either a Topic or a Reply, so we can handle nested replies).
	//		The addReply function first asks the user for the ID of the Message to which they are replying;
	//		if the number provided is greater than the size of messageList, it should output and error message and loop back,
	// 		continuing to ask for a valid Message ID number until the user enters it or -1.
	// 		(-1 returns to menu, any other negative number asks again for a valid ID number)
	
	// If the ID is valid, then the function asks for the body of the new message, 
	// and constructs the Reply, pushing back the Reply on to the messageList.
	// The subject of the Reply is a copy of the parent Topic's subject with the "Re: " prefix.
	// e.g., suppose the subject of message #9 was "Thanks", the user is replying to that message:


	/*
			Enter Message ID (-1 for Menu): 9
			Body: It was a pleasure implementing this!
	*/

	// Note: As before, the body ends when the user enters an empty line.
	// The above dialog will generate a reply that has "Re: Thanks" as its subject
	// and "It was a pleasure implementing this!" as its body.

	// How will we know what Topic this is a reply to?
	// In addition to keeping a pointer to all the Message objects in BBoard's messageList ArrayList
	// Every Message (wheather Topic or Reply) will also store an ArrayList of pointers to all of its Replies.
	// So whenever we build a Reply, we must immediately store this Message in the parent Message's list. 
	// The Reply's constructor should set the Reply's subject to "Re: " + its parent's subject.
	// Call the addChild function on the parent Message to push back the new Message (to the new Reply) to the parent's childList ArrayList.
	// Finally, push back the Message created to the BBoard's messageList. 
	// Note: When the user chooses to return to the menu, do not call run() again - just return fro mthis addReply function. 
	private void addReply(){
		Scanner sc = new Scanner(System.in);
		boolean check = true;
		while(check){
			System.out.print("Enter Message ID (-1 for Menu): ");
			int idMessage = sc.nextInt();
			System.out.println();
			if(idMessage == -1){
				// System.out.print("what");
				return;
			}
			else if(idMessage < 1 || idMessage >= messId){
				System.out.println("Invalid Message ID!");
			}
			else{
				check = false;
				// System.out.print("test");
			}
		}
		System.out.print("Body: ");
		String tray = sc.nextLine();
		System.out.println();
		Reply incon2 = new Reply(currentUser.getUsername(), (messages.get(messId - 2)).getSubject(), tray, messId);
		messages.get(messId - 2).addChild(incon2);
		messId++;
	}

	// This function allows the user to change their current password.
	// The user is asked to provide the old password of the currentUser.
	// 		If the received password matches the currentUser password, then the user will be prompted to enter a new password.
	// 		If the received password doesn't match the currentUser password, then the user will be prompted to re-enter the password. 
	// 		The user is welcome to enter 'c' or 'C' to cancel the setting of a password and return to the menu.
	// Any password is allowed except 'c' or 'C' for allowing the user to quit out to the menu. 
	// Once entered, the user will be told "Password Accepted." and returned to the menu.
	private void setPassword(){
		Scanner sc = new Scanner(System.in);
		System.out.print("Old Password ('c' or 'C' for Menu): ");
		String cancel = sc.nextLine();
		System.out.println();
		if((cancel.toLowerCase()).equals("c")){
			System.out.print("testing");
			return;
		}
			if(cancel.equals(currentUser.getPassword())){
				System.out.print("Please enter your new password: ");
				String newPassword = sc.nextLine();
				if(currentUser.setPassword(cancel, newPassword)){
					System.out.println("Password Accepted.");
					System.out.println();
					return;
				}
			}
			System.out.println("Invalid Password, please re-enter.");
			System.out.println();
			setPassword();
	}

}
