package com.xuena.supplier.infrastructure.util;

import org.junit.jupiter.api.Test;

class GeneratePasswordTest {

    @Test
    void generateAdminPassword() {
        PasswordUtil passwordUtil = new PasswordUtil();
        String hash = passwordUtil.encrypt("admin123");
        System.out.println("admin123 BCrypt hash: " + hash);
    }

    @Test
    void generateUser1Password() {
        PasswordUtil passwordUtil = new PasswordUtil();
        String hash = passwordUtil.encrypt("user123");
        System.out.println("user123 BCrypt hash: " + hash);
    }

    @Test
    void verifyAdminPassword() {
        PasswordUtil passwordUtil = new PasswordUtil();
        String storedHash = "$2a$10$.D/1FzKoZGAV.qv7dT.98OwxKDTqM5abFpREzvAluI/Hbjgu2dW42";
        boolean matches = passwordUtil.matches("admin123", storedHash);
        System.out.println("admin123 matches: " + matches);
    }
}
