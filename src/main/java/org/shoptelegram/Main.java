package org.shoptelegram;

import org.shoptelegram.services.LoyalCardServices;

public class Main {
    private static final String BOT_TOKEN = "5785341845:AAGdA1c8Ayoi6hXb8VW4UzKkUmfA43Z95T4" ;

    public static void main(String[] args) {
        TelegramBotApplication application = TelegramBotApplication.builder().botToken(BOT_TOKEN).build();

        application.run();
    }
    /*public static void main(String[] args) {
        System.out.println(LoyalCardServices.checkIsPhone("+79525410223"));
    }*/
}