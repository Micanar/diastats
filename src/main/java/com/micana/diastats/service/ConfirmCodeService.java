package com.micana.diastats.service;

import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class ConfirmCodeService {
    public String generateConfirmationCode() {
        int codeLength = 5; // Длина кода подтверждения
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Допустимые символы для кода

        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(index);
            code.append(randomChar);
        }

        return code.toString();
    }
}
