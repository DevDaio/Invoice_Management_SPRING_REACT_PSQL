package com.example.Invoice_Management_SPRING_REACT_PSQL.Utility;
import org.springframework.security.crypto.bcrypt.BCrypt;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.User;

public final class Crypting {

	public static boolean checkPassword(User user, String password) {
		return BCrypt.checkpw(password, user.getPassword());
	}

   public static String encryptPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

}
