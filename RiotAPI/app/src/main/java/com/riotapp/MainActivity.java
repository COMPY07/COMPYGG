package com.riotapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.riotapp.RiotAPI.PlayerData;
import com.riotapp.RiotAPI.RiotAPI;
import com.riotapp.databinding.ActivityMainBinding;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    RiotAPI api;
    private ActivityMainBinding binding;


    ArrayList<PlayerData> datas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        datas = new ArrayList<>();
        EditText input = findViewById(binding.editText.getId());
        Button findBtn = findViewById(binding.button.getId());
        findBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(isContain(input.getText().toString())){
                    NextAct(input.getText().toString());
                }

                boolean isRealPlayer = getPlayerData(input.getText().toString());


                if(isRealPlayer){


                    PlayerData data = api.get();
                    datas.add(data);
                    NextAct(input.getText().toString());
                    // setContentView(R.layout.activity_sub);
                }else{ Snackbar.make(view, "없는 플레이어 입니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show(); }
            }
        });
    }
    void NextAct(String name){
        Intent intent = new Intent(getApplicationContext(), SubActivity.class);//getApplicationContext(), SubActivity.class);
        PlayerData data = datas.get(0);
        for(PlayerData d : datas){
            if(!Objects.equals(d.getName(), name)) continue;
            data = d;
            break;
        }
        System.out.println(Color.argb(0, 255, 0, 0)+ name);
        intent.putExtra("name", data.getName());
        intent.putExtra("rank",  data.getRank());
        intent.putExtra("tier", data.getTier());
        intent.putExtra("level", data.getLevel());
        intent.putExtra("win", data.getWins());
        intent.putExtra("loss", data.getLosses());
        intent.putExtra("icon", data.getIcon());

        startActivity(intent);

    }
    public boolean isContain(String playerData){
        for(PlayerData d : datas){
            if(d.getName() == playerData) return true;
        }
        return false;
    }
    public boolean getPlayerData(String PlayerName){
        api = new RiotAPI(PlayerName);

        try{
            api.start();
            api.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return api.success;
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getPriority(String str){

        for(PlayerData data : datas){ data.setCost(similarity(data.getName(), str)); }


        for(PlayerData data : datas){
            System.out.println(data);
        }
        datas.sort(new Comparator<PlayerData>() {
            @Override
            public int compare(PlayerData playerData, PlayerData t1) {
                return (int) (playerData.getCost() - t1.getCost());
            }
        });
    }
    private double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;

        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }

        int longerLength = longer.length();
        if (longerLength == 0) return 1.0;
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }
    private int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) { costs[j] = j; }
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];

                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) { newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1; }

                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }

            if (i > 0) costs[s2.length()] = lastValue;
        }

        return costs[s2.length()];
    }

}