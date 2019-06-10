package com.example.opdshe;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Button temp= findViewById(R.id.btn_temp);
        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("The Way To Go Home");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitleMarginStart(250);
        toolbar.setTitleMarginTop(150);
        Button Imagebutton_taxi =  findViewById(R.id.image_taxi);
        Button Imagebutton_walking =  findViewById(R.id.image_walking);


        Imagebutton_taxi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Taxi.class);
                startActivity(intent);
            }
        });

        Imagebutton_walking.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Walking.class);
                startActivity(intent);
            }
        });
        /*temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });*/



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }
}
