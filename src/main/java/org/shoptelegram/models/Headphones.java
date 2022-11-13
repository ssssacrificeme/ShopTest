package org.shoptelegram.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Headphones {
    private String serial;
    private String model;
    private String color;
    private int cost;
    private String description;

    private String photo;


    public String toString(){
        return this.serial + " " + this.model + " " + this.color
                + "\n"  + this.description + "\n" + "\n" + "Цена -- " + this.cost;
    }
}
