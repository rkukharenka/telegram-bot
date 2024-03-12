package com.rkukharenka.telegrambot.instaboxbot.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PhoneNumberUtilsTest {

    @Test
    void testPhoneNumberRemoveNonNumeric() {
        String input = "+375(29)368-66-34";
        String expectedResult = "375293686634";

        String actualResult = PhoneNumberUtils.removeNonNumeric(input);

        Assertions.assertEquals(expectedResult, actualResult);
    }
}