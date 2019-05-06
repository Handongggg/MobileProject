package com.example.opdshe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton Imagebutton_texi = (ImageButton) findViewById(R.id.texi);
        ImageButton Imagebutton_bus = (ImageButton) findViewById(R.id.bus);
        ImageButton Imagebutton_working = (ImageButton) findViewById(R.id.working);

        Imagebutton_bus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Bus.class);
                startActivity(intent);
            }
        });

        Imagebutton_texi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Taxi.class);
                startActivity(intent);
            }
        });
    }
}
