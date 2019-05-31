package com.example.opdshe;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaxiPosting extends Taxi {
    SimpleDateFormat format2 = new SimpleDateFormat ( "HH시mm분");

    Date time = new Date();
    Toolbar toolbar;
    Button send;
    EditText title;
    EditText source;
    EditText dest;
    Spinner personnel;
    EditText password;
    TimePicker timepicker;
    String temp;
    String temp_time=format2.format(time);

    /*String PASSWORD;
    String TITLE;
    String SOURCE;
    String DEST;
    String PERSONNEL;
    String TIME=temp_time;*/
    DatabaseReference mPostReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_posting);
        mPostReference=FirebaseDatabase.getInstance().getReference();
        title=findViewById(R.id.edit_p_title);
        source=findViewById(R.id.edit_p_source);
        dest=findViewById(R.id.edit_p_dest);
        personnel=findViewById(R.id.spn_personnel);
        password=findViewById(R.id.edit_password);
        timepicker=findViewById(R.id.timepicker);
        send=findViewById(R.id.btn_post);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("글쓰기");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar() ;
        personnel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                temp=personnel.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }
        );
        timepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay<12){
                    temp_time="오전 "+hourOfDay+"시"+minute+"분";
                }
                else
                    temp_time="오후 "+(hourOfDay-12)+"시"+minute+"분";
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Taxi.TITLE=title.getText().toString();
                Taxi.SOURCE=source.getText().toString();
                Taxi.DEST=dest.getText().toString();
                Taxi.PASSWORD=password.getText().toString();
                Taxi.PERSONNEL=temp;
                Taxi.TIME=temp_time;
                writePost(true);
                getFirebaseDatabase();
                finish();
            }
        });

    }
    public void writePost(boolean add){

        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            Post post = new Post(TITLE, SOURCE, DEST, TIME, PERSONNEL, PASSWORD);
            postValues = post.toMap();
        }
        childUpdates.put("/POST/" + PASSWORD, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_taxi, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_refresh:
                getFirebaseDatabase();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
