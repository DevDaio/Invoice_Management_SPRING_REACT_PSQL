package com.example.Invoice_Management_SPRING_REACT_PSQL.Security;
import org.junit.jupiter.api.Test;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.User;
import static org.junit.jupiter.api.Assertions.*;

class CryptingTest {

    User user = new User("test@mail.de", "meinPasswort", "USER");

    @Test
    void testPasswordIsHashed() {
        assertNotEquals("meinPasswort", user.getPassword());
    }

    @Test
    void testCheckPasswordCorrect() {
        assertTrue(Crypting.checkPassword(user, "meinPasswort"));
    }

    @Test
    void testCheckPasswordWrong() {
        assertFalse(Crypting.checkPassword(user, "falschesPasswort"));
    }
}
