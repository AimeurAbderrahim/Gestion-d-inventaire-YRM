package stateMachin;

import testpackage.model.core.Compte;

public class Session {
	private static Compte currentUser;

	public static void login(Compte user) {
		currentUser = user;
	}

	public static Compte getCurrentUser() {
		return currentUser;
	}

	public static void logout() {
		currentUser = null;
	}
}
