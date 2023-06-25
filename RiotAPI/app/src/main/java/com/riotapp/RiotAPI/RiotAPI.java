package com.riotapp.RiotAPI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class RiotAPI extends Thread{

    String PlayerName;

    final String TOKEN = "RGAPI-b95828ac-16b9-4d82-bb93-c36fd05ec8a7";

    PlayerData data;
    public boolean success;

    // region PlayerData

    String Name, PlayerTier, PlayerRank;

    int PlayerLevel, PlayerIcon, wins, losses;

    Bitmap bitmap;

    HashMap<String, String> PlayerData;

    // endregion PlayerData


    public RiotAPI(String PlayerName){
        this.Name = PlayerName;
        this.success = true;
        this.PlayerData = new HashMap<>();
        data = new PlayerData();
    }

    @Override
    public void run(){

        String SummonerName = this.Name.replaceAll(" ", "%20");
        System.out.println(SummonerName);
        String requestURL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + SummonerName + "?api_key=" + TOKEN;
        JSONObject json = getData(requestURL);

        success = json != null;
        if(!success) return;

        try {
            PlayerName = (String) json.get("id");
            PlayerLevel = Integer.parseInt(String.valueOf(json.get("summonerLevel")));
            PlayerIcon =  Integer.parseInt(String.valueOf(json.get("profileIconId")));
            bitmap = getImageFromURL("https://ddragon-webp.lolmath.net/latest/img/profileicon/"+PlayerIcon+".webp");
            getInfo();

        }catch(JSONException e){}
    }

    // region GetData from RiotAPI
    public JSONObject getData(String url_){
        try{
            URL url = new URL(url_);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestMethod("GET");
            con.setDoOutput(false);

            if(con.getResponseCode() != HttpURLConnection.HTTP_OK) System.out.printf("wrong response: ", con.getResponseMessage());

            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), StandardCharsets.UTF_8));
            String line = reader.readLine();

            while((line != null)){
                builder.append(line).append("\n");
                line = reader.readLine();
            }

            reader.close();

            String[] json = builder.toString().split(",");

            json[0] = json[0].substring(1);
            json[json.length-1] = json[json.length-1].substring(0, json[json.length-1].length()-2);


            JSONObject result = new JSONObject();
            for(String i : json){
                String[] tv;
                tv= i.split(":");
                System.out.println(i);
                for(int j = 0; j < 2; j++){
                    if(tv[j].contains("\"")) tv[j] = tv[j].substring(1, tv[j].length()-1);
                }
                result.put(tv[0], tv[1]);
            }
            return result;

        } catch (IOException | JSONException e) { e.printStackTrace(); }

        return null;
    }


    public Bitmap getImageFromURL(String imageURL){
        Bitmap imgBitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;

        try
        {
            URL url = new URL(imageURL);
            conn = (HttpURLConnection)url.openConnection();
            conn.connect();

            int nSize = conn.getContentLength();
            bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e){
            e.printStackTrace();
        } finally{
            if(bis != null) {
                try {bis.close();} catch (IOException e) {}
            }
            if(conn != null ) {
                conn.disconnect();
            }
        }

        return imgBitmap;
    }

    public void getInfo(){
        String requestURL = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/"+PlayerName+"?api_key="+TOKEN;
        JSONObject json = getData(requestURL);
        // System.out.println(json);


        try {
            // System.out.println(json.getString("tier"));
            PlayerTier = (String) json.getString("tier");
            PlayerRank = (String) json.getString("rank");
            wins = (int) json.getInt("wins");
            losses = (int) json.getInt("losses");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // endregion GetData from RiotAPI

    // region Getter Method

    public String getPlayerName(){
        return Name;
    }
    public String getPlayerLevel(){
        return String.valueOf(PlayerLevel);
    }
    public String getPlayerTier(){
        return PlayerTier;
    }
    public String getPlayerRank(){
        return PlayerRank;
    }
    public int getWins(){
        return wins;
    }

    public int getLoses(){
        return losses;
    }

    public Bitmap getPlayerIcon(){
        return bitmap;
    }
    public int totalGamePlay(){
        return wins+losses;
    }

    private void updateData(){
        PlayerData.put("name", Name);
        PlayerData.put("tier", PlayerTier);
        PlayerData.put("rank", PlayerRank);
        PlayerData.put("level", String.valueOf(PlayerLevel));
        PlayerData.put("win", String.valueOf(wins));
        PlayerData.put("loss", String.valueOf(losses));

        data.setLevel(PlayerLevel);
        data.setName(Name);
        data.setRank(PlayerRank);
        data.setTier(PlayerTier);
        data.setLosses(losses);
        data.setWins(wins);
        data.setIcon(bitmap);

    }
    public PlayerData get(){
        updateData();
        return data;
    }
    // endregion Getter Method
}
