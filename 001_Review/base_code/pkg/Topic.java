package pkg;
import java.util.*;
import java.io.*;

public class Topic extends Message {

	// Default Constructor
	int code = 0;
	public Topic() {

	}

	// Parameterized constructor
	public Topic(String auth, String subj, String bod, int i) {
		super(auth, subj, bod, i);
		code = i;
	}

	// Returns if it's a reply (false)
	public boolean isReply(){
		return false;
	}
}