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
	public static final String CREATEUSER = "createUser";

	/**
	 * The action for getKeyFile
	 */
	public static final String GETKEYFILE = "getKeyFile";

	/**
	 * The action for updateKeyFile
	 */
	public static final String UPDATEKEYFILE = "updateKeyFile";

	/**
	 * The action for getFile
	 */
	public static final String GETFILE = "getFile";

	/**
	 * The action for updateFile
	 */
	public static final String UPDATEFILE = "updateFile";

	/**
	 * The action for uploadFile
	 */
	public static final String UPLOADFILE = "uploadFile";

	/**
	 * The action for removeFile
	 */
	public static final String REMOVEFILE = "removeFile";

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
		 * The property for username
		 */
		public static final String USERNAME = "username";

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
