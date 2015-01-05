package org.fides.components;

/**
 * Actions that are used by the protocol
 */
public class Actions {

	/**
	 * action
	 */
	public static final String ACTION = "action";

	/**
	 * The action for createUser
	 */
	public static final String CREATE_USER = "createUser";

	/**
	 * The action for getKeyFile
	 */
	public static final String GET_KEY_FILE = "getKeyFile";

	/**
	 * The action for updateKeyFile
	 */
	public static final String UPDATE_KEY_FILE = "updateKeyFile";

	/**
	 * The action for getFile
	 */
	public static final String GET_FILE = "getFile";

	/**
	 * The action for updateFile
	 */
	public static final String UPDATE_FILE = "updateFile";

	/**
	 * The action for uploadFile
	 */
	public static final String UPLOAD_FILE = "uploadFile";

	/**
	 * The action for removeFile
	 */
	public static final String REMOVE_FILE = "removeFile";

	/**
	 * The action for login
	 */
	public static final String LOGIN = "login";

	/**
	 * The action for disconnect
	 */
	public static final String DISCONNECT = "disconnect";

	/**
	 * Properties that are used by the communication
	 */
	public class Properties {
		/**
		 * The property for usernameHash
		 */
		public static final String USERNAME_HASH = "username";

		/**
		 * The property for passwordHash
		 */
		public static final String PASSWORD_HASH = "passwordHash";

		/**
		 * The property for location
		 */
		public static final String LOCATION = "location";
	}

}
