package org.shoptelegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.shoptelegram.models.LoyalCard;
import org.shoptelegram.services.LoyalCardServices;
import org.shoptelegram.utility.DbUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

public class TelegramBotApplication extends TelegramBot {

    private HashMap<String, LoyalCard> loyalCards = DbUtils.readAllLoyalCardDB();
    @lombok.Builder
    public TelegramBotApplication(String botToken){
        super(botToken);
    }

    public void run(){
        this.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        Message message = update.message();
        if (message != null){
            String text = message.text();
            Optional.ofNullable(text).ifPresent(commandName -> {
                try {
                    this.serveCommand(commandName,message.chat().id());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void serveCommand(String commandName, Long chatId) throws SQLException {
        switch (commandName) {
            case "/start": {
                SendMessage response = new SendMessage(chatId, "Привет, это бот лояльности магазина ToFroShop \n"  +
                        "Здесь ты можешь проверить баланс своей карты или зарегистрировать ее \n")
                        .replyMarkup(new ReplyKeyboardMarkup(new String[][]{
                        {"У меня уже есть карта"}, {"Зарегистрировать карту"}}));
                this.execute(response);
                break;
            }
            case "У меня уже есть карта":
            case "Зарегистрировать карту": {
                SendMessage response = new SendMessage(chatId,"Введите номер телефона");
                this.execute(response);
                break;
            }
            default: {
                if (LoyalCardServices.checkIsPhone(LoyalCardServices.formatPhone(commandName))){
                    commandName = LoyalCardServices.formatPhone(commandName);
                    boolean b = loyalCards.containsKey(commandName);
                    LoyalCard loyalCard;
                    if(b){
                        loyalCard = loyalCards.get(commandName);
                        SendMessage response = new SendMessage(chatId,"Ваша карта лояльности! \n \n" + loyalCard.toString());
                        this.execute(response);
                        break;
                    }
                    else {
                        loyalCard = new LoyalCard(commandName, 0, 0);
                        DbUtils.saveLoyalCardDB(loyalCard);
                        SendMessage response = new SendMessage(chatId,"Карта лояльности успешно зарегистрирована! " +
                                "Совершайте покупки, копите баллы, покупайте одноразовые по выгодной цене! \n \n" +
                                loyalCard.toString());
                        this.execute(response);
                        break;
                    }
                }
                else{
                    SendMessage response = new SendMessage(chatId,"Введите верный запрос");
                    this.execute(response);
                }
                break;

            }
        }
    }
}
