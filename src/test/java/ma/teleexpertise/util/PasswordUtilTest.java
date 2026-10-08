package ma.teleexpertise.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    @DisplayName("BCrypt password hashing and verification should succeed with correct password")
    void testHashAndCheckPasswordSuccess() {
        String plainPassword = "SecurePassword123!";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$") || hashedPassword.startsWith("$2b$") || hashedPassword.startsWith("$2y$"));
        assertTrue(PasswordUtil.checkPassword(plainPassword, hashedPassword));
    }

    @Test
    @DisplayName("BCrypt check should fail with incorrect password")
    void testCheckPasswordFailure() {
        String plainPassword = "SecurePassword123!";
        String wrongPassword = "WrongPassword123!";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        assertFalse(PasswordUtil.checkPassword(wrongPassword, hashedPassword));
    }

    @Test
    @DisplayName("BCrypt hashing should reject null or empty password")
    void testEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword(""));
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword(null));
    }
}
