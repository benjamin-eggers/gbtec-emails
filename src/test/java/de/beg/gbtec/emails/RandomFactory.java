package de.beg.gbtec.emails;

import net.datafaker.Faker;

public class RandomFactory {

    private static final Faker FAKER = new Faker();

    public static String randomEmail() {
        return FAKER.internet().safeEmailAddress();
    }

    public static String randomQuote() {
        return FAKER.elderScrolls().quote();
    }

    public static String randomText() {
        return FAKER.text().text();
    }
}
