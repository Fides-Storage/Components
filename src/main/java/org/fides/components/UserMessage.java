package org.fides.components;

public class UserMessage {

	public UserMessage(String message, boolean error) {
		this.message = message;
		this.error = error;
	}

	public String message;

	public boolean error;
}
