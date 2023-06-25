package com.riotapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.riotapp.databinding.ActivityMainBinding;


public class SubActivity extends AppCompatActivity {

    ActivityMainBinding binding;



    String Name, Level, Rank, Tier, win, loss;
    Bitmap Icon;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity_main);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setting();
        System.out.println(Name+" "+ Level+" "+Rank+" "+Tier +" "+Icon);

        ImageView image = findViewById(R.id.circle_center);
        TextView name = findViewById(R.id.name);
        TextView TierText = findViewById(R.id.tier_text);
        ImageView TierImage = findViewById(R.id.TierImage);
        TextView tier = findViewById(R.id.tier);
        TextView winRate = findViewById(R.id.win);
        ImageButton btn = findViewById(R.id.backButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tier.setText(Tier+" "+Rank);
        winRate.setText("총 게임 수 : "+(Integer.parseInt(win)+Integer.parseInt(loss))+"\n"
                +"승리 : "+win+"        패배 : "+loss+"\n"+"승률 : "+((int)(Double.parseDouble(win) / (Integer.parseInt(win)+Integer.parseInt(loss)) * 100))+"%");

        name.setText(Name+"\n레벨 : "+Level+" ");
        image.setImageBitmap(Icon);
        TierText.setText(ChangeText(TierImage));

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private String ChangeText(ImageView view){
        switch (Tier){
            case "IRON":
                view.setImageDrawable(getResources().getDrawable(R.drawable.iron));
                return "쓰읍... 혹시 이게 맞는걸까요?";
            case "BRONZE":
                view.setImageDrawable(getResources().getDrawable(R.drawable.bronze));
                return "브론즈 쉑";
            case "SILVER":
                view.setImageDrawable(getResources().getDrawable(R.drawable.silver));
                return "음... 우리나라 평균!";
            case "GOLD":
                view.setImageDrawable(getResources().getDrawable(R.drawable.gold));
                return "골딱이는 들어가시구요";
            case "PLATINUM":
                view.setImageDrawable(getResources().getDrawable(R.drawable.platinum));
                return "브실골플";
            case "DIAMOND":
                view.setImageDrawable(getResources().getDrawable(R.drawable.diamond));
                return "예티들이 말이야 롤창이랬어!";
            case "MASTER":
                view.setImageDrawable(getResources().getDrawable(R.drawable.master));
                return "우리나라 위에 있어...?!??!";
            case "GRANDMASTER":
                view.setImageDrawable(getResources().getDrawable(R.drawable.grandmaster));
                return "와... 당신이 정말 협곡의 지배자군용";
            case "CHALLENGER":
                view.setImageDrawable(getResources().getDrawable(R.drawable.challanger));
                return "HI The Shy";
        }
        return "잠재적 챌린저 지렸다";
    }

    @Override
    public void onPause() {

        super.onPause();
        finish();
    }
    public void setting(){

        //  intent.putExtra("name", data.getName());
        //        intent.putExtra("rank",  data.getRank());
        //        intent.putExtra("tier", data.getTier());
        //        intent.putExtra("level", data.getLevel());
        //        intent.putExtra("win", data.getWins());
        //        intent.putExtra("loss", data.getLosses());
        //        intent.putExtra("icon", data.getIcon());
        Intent intent = getIntent();
        Name = intent.getStringExtra("name");
        Level = String.valueOf(intent.getIntExtra("level", 0));
        Icon = intent.getParcelableExtra("icon");
        Rank = intent.getStringExtra("rank");
        Tier = intent.getStringExtra("tier");
        loss = String.valueOf(intent.getIntExtra("loss", 0));
        win= String.valueOf(intent.getIntExtra("win", 0));
    }

}
