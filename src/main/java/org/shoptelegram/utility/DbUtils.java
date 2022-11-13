package org.shoptelegram.utility;

import org.shoptelegram.models.DisposableEcig;
import org.shoptelegram.models.Headphones;
import org.shoptelegram.models.LoyalCard;
import org.shoptelegram.services.LoyalCardServices;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class DbUtils {
    public static final Connection connection = getDBConnection();

    public static Connection getDBConnection() {
        Connection dbConnection = null;
        try{
            Class.forName("org.postgresql.Driver");
            dbConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","kikiki","dashka0310");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Opened database successfully");
        return dbConnection;
    }
    public static List<Headphones> readAllHeadphonesDB() throws SQLException{

        PreparedStatement headphonesStatement = connection.prepareStatement(
                "SELECT * FROM headphones ORDER BY serial");
        ResultSet headphonesSet = headphonesStatement.executeQuery();
        List<Headphones> headphonesList = new ArrayList<>();
        while (headphonesSet.next()){
            Headphones headphones = new Headphones(headphonesSet.getString(1),
                    headphonesSet.getString(2), headphonesSet.getString(3),
                    headphonesSet.getInt(4), headphonesSet.getString(5),
                    headphonesSet.getString(6));
            System.out.println(headphones);
            if (headphonesSet.getInt(6)> 0)
                headphonesList.add(headphones);
        }
        return headphonesList;
    }
    public static List<DisposableEcig> realAllDisp() throws SQLException{
        PreparedStatement dispStatement = connection.prepareStatement
                ("SELECT * FROM disposables ORDER BY serial");
        ResultSet disposablesSet = dispStatement.executeQuery();
        List<DisposableEcig> disposableEcigList = new ArrayList<>();
        while (disposablesSet.next()){
            DisposableEcig disposableEcig = new DisposableEcig(disposablesSet.getString(1),
                    disposablesSet.getString(2),disposablesSet.getInt(3),
                    disposablesSet.getString(4),disposablesSet.getInt(5));
            System.out.println(disposableEcig);
            if(disposablesSet.getInt(6)>0)
                disposableEcigList.add(disposableEcig);
        }
        return disposableEcigList;
    }

    public static List<String> readAllHeadphonesPhotoDB() throws SQLException{
        PreparedStatement headphonesStatement = connection.prepareStatement
                ("SELECT * FROM headphones ORDER BY serial");
        ResultSet photosSet = headphonesStatement.executeQuery();
        List<String> photos = new ArrayList<>();
        while (photosSet.next()){
            String s = photosSet.getString(6);
            photos.add(s);
        }
        return photos;
    }
    public static HashMap<String, LoyalCard> readAllLoyalCardDB() {
        PreparedStatement cardStatement = null;
        try {
            cardStatement = connection.prepareStatement(
                    "SELECT * FROM loyal_card ORDER BY phone");

            ResultSet cardSet = cardStatement.executeQuery();
            List<LoyalCard> cards = new ArrayList<>();
            while (cardSet.next()) {
                LoyalCard loyalCard = new LoyalCard(cardSet.getString(1),
                        cardSet.getInt(2), cardSet.getInt(3));
                cards.add(loyalCard);
            }
            HashMap<String,LoyalCard> hashMap = IntStream.range(0,cards.size())
                    .collect(
                            HashMap::new,
                            (m,i) -> m.put(cards.get(i).getPhone(),cards.get(i)),
                    Map::putAll);
            return hashMap;
        } catch (SQLException e) {
            throw new RuntimeException(e);
         }

    }
    public static LoyalCard readLoyalCardDB(String phone) {
        String resultString = LoyalCardServices.formatPhone(phone);
        PreparedStatement cardStatement = null;
        try {
            cardStatement = connection.prepareStatement(
                    "SELECT FROM loyal_card WHERE phone = " + resultString);
            ResultSet cardSet = cardStatement.executeQuery();
            LoyalCard loyalCard = new LoyalCard(cardSet.getString(1),
                    cardSet.getInt(2),cardSet.getInt(3));
            return loyalCard;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean saveLoyalCardDB(LoyalCard loyalCard){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO loyal_card(phone,points,purchaseCount)" +
                            "VALUES (?,?,?);");
            preparedStatement.setString(1,loyalCard.getPhone());
            preparedStatement.setInt(2,loyalCard.getPoints());
            preparedStatement.setInt(3,loyalCard.getPurchaseCount());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
