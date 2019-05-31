package com.example.opdshe;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Taxi extends AppCompatActivity  {
    private DatabaseReference mPostReference;
    static SimpleDateFormat format2 = new SimpleDateFormat ( "HH시mm분");
    static Date time = new Date();
    Toolbar toolbar;
    Button posting;
    static String temp_time=format2.format(time);

    static String PASSWORD;
    static String TITLE;
    static String SOURCE;
    static String DEST;
    static String PERSONNEL;
    static String TIME=temp_time;




    String ID;
    String name;
    long age;
    String gender = "";
    String sort = "id";

    //ArrayAdapter<String> arrayAdapter;
    ListViewAdapter adapter;


    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi);

        adapter= new ListViewAdapter();
        //

       //
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onClickListener);
        listView.setOnItemLongClickListener(longClickListener);

        getFirebaseDatabase();


        //
        posting=findViewById(R.id.btn_posting);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Taxi");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar ab = getSupportActionBar() ;
        posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),TaxiPosting.class);
                startActivity(intent);
            }
        });

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
            case R.id.menu_account:
                Toast.makeText(getApplicationContext()," press account",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_logout:
                Toast.makeText(getApplicationContext()," press logout",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //


    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Num of listviewList", "number = " + adapter.listViewItemList.size());

        }
    };

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Long Click", "position = " + position);
            final String[] nowData = arrayData.get(position).split("/");
            PASSWORD = nowData[5];
            //String viewData = nowData[0] + ", " + nowData[1] + ", " + nowData[2] + ", " + nowData[3]+ ", " + nowData[4]+ ", " + nowData[5];
            final EditText edittext = new EditText(getApplicationContext());
            edittext.setText(PASSWORD);
            AlertDialog.Builder dialog = new AlertDialog.Builder(Taxi.this);
            dialog.setTitle("데이터 삭제")
                    .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n"+"삭제를 원하시면 비밀번호 입력 후\n '네'버튼을 눌러주세요" )
                    .setView(edittext)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(edittext.getText().toString().equals(PASSWORD)){
                                postFirebaseDatabase(false);
                                getFirebaseDatabase();
                                Toast.makeText(Taxi.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(Taxi.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Taxi.this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    };

    public boolean IsExistID(){
        boolean IsExist = arrayIndex.contains(PASSWORD);
        return IsExist;
    }

    public void postFirebaseDatabase(boolean add){

        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            Post post = new Post(TITLE, SOURCE, DEST, TIME, PERSONNEL, PASSWORD);
            postValues = post.toMap();
        }
        childUpdates.put("/POST/" +PASSWORD, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void getFirebaseDatabase(){

                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
                        arrayData.clear();
                        arrayIndex.clear();
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            String key = postSnapshot.getKey();
                            Post get = postSnapshot.getValue(Post.class);
                            String[] info = {get.title, get.source, get.dest, get.time, get.personnel, get.password};
                            String Result = info[0]+"/"+info[1]+"/"+info[2]+"/"+info[3]+"/"+info[4]+"/"+info[5];
                            arrayData.add(Result);
                            arrayIndex.add(key);
                            Log.d("getFirebaseDatabase", "key: " + key);
                            Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]+ info[4]+ info[5]);
                        }
                        adapter.clear();
                        adapter.addAll(arrayData);
                        adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("POST").orderByChild(sort);
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }


}
