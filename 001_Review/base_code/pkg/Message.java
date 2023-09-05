package pkg;
import java.util.*;
import java.io.*;

public class Message {

	String person = new String("");
	String topic = new String("");
	String words = new String("");
	int code;
	ArrayList<Message> everything = new ArrayList<Message>();
	
	// Default Constructor
	public Message() {
		
	}
	
	// Parameterized Constructor
	public Message(String auth, String subj, String bod, int i) {
		person = auth;
		topic = subj;
		words = bod;
		code = i;
		
	}

	// This function is responsbile for printing the Message
	// (whether Topic or Reply), and all of the Message's "subtree" recursively:

	// After printing the Message with indentation n and appropriate format (see output details),
	// it will invoke itself recursively on all of the Replies inside its childList, 
	// incrementing the indentation value at each new level.

	// Note: Each indentation increment represents 2 spaces. e.g. if indentation ==  1, the reply should be indented 2 spaces, 
	// if it's 2, indent by 4 spaces, etc. 
	public void print(int indentation){
		// System.out.print(everything.size());
		if(!(everything.size() == 0)){
			indent(indentation);
		
			for(int i = 0; i < everything.size(); i++){
				System.out.println("Message #" + (everything.get(i)).getId() + ": Re: " + '"' + (everything.get(i)).getSubject() + '"');
				System.out.println("From " + (everything.get(i)).getUsername() + ":" + '"' + (everything.get(i)).getBody() + '"');
			}
			// print(indentation + 1);
		}
	}
	
	public void indent(int indentation){
		for(int i = 0; i < indentation * 2; i++){
			System.out.print(" ");
		}
	}

	// Default function for inheritance
	public boolean isReply(){
		return false;
	}
	
	public String getUsername(){
		return person;
	}

	// Returns the subject String
	public String getSubject(){
		return topic;
	} 

	// Returns the ID
	public int getId(){
		return code;
	}
	
	public String getBody(){
		return words;
	}

	// Adds a child pointer to the parent's childList.
	public void addChild(Message child){
		everything.add(child);
	}

}