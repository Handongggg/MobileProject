package com.example.opdshe;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Walking extends AppCompatActivity {
    Toolbar toolbar;
    Button sour_button;
    Button desti_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        sour_button = (Button)findViewById(R.id.source);
        desti_button = (Button)findViewById(R.id.destination);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Walking");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sour_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent3 = new Intent(Walking.this, DaumWebViewActivity.class);
                startActivityForResult(intent3,1);
            }
        });

        desti_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent4 = new Intent(Walking.this, DaumWebViewActivity.class);
                startActivityForResult(intent4,1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_search:
                Toast.makeText(getApplicationContext()," press search",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_account:
                Toast.makeText(getApplicationContext()," press account",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_logout:
                Toast.makeText(getApplicationContext()," press logout",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
