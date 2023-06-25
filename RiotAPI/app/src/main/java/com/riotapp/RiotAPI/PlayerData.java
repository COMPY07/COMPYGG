package com.riotapp.RiotAPI;

import android.graphics.Bitmap;

import java.util.HashMap;

public class PlayerData implements Comparable<PlayerData>{

    Bitmap icon;
    String Name, PlayerTier, PlayerRank;

    int PlayerLevel, PlayerIcon, wins, losses;

    HashMap<String, String> PlayerData;
    double cost;

    public PlayerData(){
        cost = 0;
    }


    public void setIcon(Bitmap bit){
        icon = bit;
    }
    public Bitmap getIcon(){
        return icon;
    }

    public void setWins(int wins){
        this.wins = wins;
    }
    public int getWins(){
        return wins;
    }

    public void setLosses(int loss){
        losses = loss;
    }

    public int getLosses(){
        return losses;
    }

    public void setName(String name){
        Name = name;
    }
    public String getName(){
        return Name;
    }

    public void setTier(String Tier){
        PlayerTier = Tier;
    }

    public String getTier(){
        return PlayerTier;
    }
    public void setRank(String rank){
        PlayerRank = rank;
    }
    public String getRank(){
        return PlayerRank;
    }
    public int getLevel(){
        return PlayerLevel;
    }
    public void setLevel(int level){
        PlayerLevel = level;
    }

    public void setCost(double cost){
        this.cost = cost;
    }
    public double getCost(){
        return cost;
    }
    @Override
    public int compareTo(PlayerData o) {
        return (int) (o.cost - this.cost);

    }
    @Override
    public String toString(){
        return "이름 : "+Name+"\n"+"레벨 : "+PlayerLevel+"\n"+"티어 : "+PlayerTier;
    }

}
