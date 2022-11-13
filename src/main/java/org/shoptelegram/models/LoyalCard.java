package org.shoptelegram.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.Name;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoyalCard {
    private String phone;
    private int points;
    private int purchaseCount;

    public String toString(){
        return "Номер телефона : " + phone + "\n" + "Количество баллов : " + Integer.toString(points) + "\n" +
                "Количество покупок : " + purchaseCount;
    }
}
