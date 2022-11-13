package org.shoptelegram.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisposableEcig {
    private String serial;
    private String model;
    private int puffs;
    private String taste;
    private int cost;


    public String toString(){
        String temp = Integer.toString(puffs);
        if(this.model.contains(serial) && this.model.contains(temp)){
            return this.model + " " + this.taste + "\n" +
                    "\n" + "Цена -- " + this.cost;

        }
        if (this.model.contains(serial) && !this.model.contains(temp)){
            return this.model + " " + this.puffs + "\n"
                    + this.taste + "\n" + "\n" + "Цена - " + this.cost;
        }
        if (!this.model.contains(serial) && this.model.contains(temp)) {
            return this.serial + " " + this.model + "\n"
                    + this.taste + "\n" + "\n" + "Цена - " + this.cost;

        }

        return this.serial + " " + this.model + " " + this.puffs
               + "\n"   + this.taste + "\n" + "\n" + "Цена - " + this.cost;

    }

}
