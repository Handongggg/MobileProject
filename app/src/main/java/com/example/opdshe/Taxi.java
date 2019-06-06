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
import com.kakao.auth.Session;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.kakaotalk.api.KakaoTalkMessageRequest;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Taxi extends AppCompatActivity {
    private DatabaseReference mPostReference;
    static SimpleDateFormat format2 = new SimpleDateFormat("HH시mm분");
    static Date time = new Date();
    Toolbar toolbar;
    Button posting;
    static String temp_time = format2.format(time);

    static String PASSWORD;
    static String TITLE;
    static String SOURCE;
    static String DEST;
    static String PERSONNEL;
    static String CURRENT_PERSONNEL;
    static String TIME = temp_time;
    static String EDITOR_ID;

    static String  USER_ID;
    static ArrayList<String> POST_LIST =new ArrayList<>();



    String ID;
    String name;
    long age;
    String gender = "";
    String sort = "id";

    //ArrayAdapter<String> arrayAdapter;
    ListViewAdapter adapter;


    static ArrayList<String> arrayIndex = new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Taxi");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new ListViewAdapter();
        //

        //
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onClickListener);
        listView.setOnItemLongClickListener(longClickListener);

        getFirebaseDatabase();


        //
        posting = findViewById(R.id.btn_posting);

        posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaxiPosting.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_taxi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_refresh:
                getFirebaseDatabase();
                return true;
            case R.id.menu_mypage:
                Toast.makeText(getApplicationContext(), " press My page", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(Taxi.this, Taxi_MyPage.class);
                startActivity(intent);
                return true;
            case R.id.menu_logout:
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent =new Intent(Taxi.this, Login.class);
                        startActivity(intent);
                    }
                });
                Toast.makeText(getApplicationContext(), " press logout", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //


    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Num of listviewList", "number = " + adapter.listViewItemList.size());
            final String[] nowData = arrayData.get(position).split("/");
            AlertDialog.Builder dialog = new AlertDialog.Builder(Taxi.this);
            dialog.setTitle("동승 신청")
                    .setMessage("해당 게시글에 신청하시겠습니까?")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CURRENT_PERSONNEL=nowData[5];
                            PERSONNEL=nowData[4];
                            Integer p_temp=Integer.parseInt(""+PERSONNEL.charAt(0));
                            Integer c_temp=Integer.parseInt(CURRENT_PERSONNEL);
                            if(c_temp>=p_temp){
                                Toast.makeText(Taxi.this, "인원 초과입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String temp = nowData[5];
                            Integer int_temp = Integer.parseInt(temp);
                            int_temp++;
                            TITLE = nowData[0];
                            SOURCE = nowData[1];
                            DEST = nowData[2];
                            TIME = nowData[3];
                            PERSONNEL = nowData[4];
                            CURRENT_PERSONNEL = Integer.toString(int_temp);
                            PASSWORD = nowData[6];
                            EDITOR_ID=nowData[7];
                            POST_LIST.add(TITLE);
                            postDataList(true);
                            postFirebaseDatabase(true);
                            getFirebaseDatabase();
                            requestSendMemo();

                        }

                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Taxi.this, "신청을 취소했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create()
                    .show();
        }
    };

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Long Click", "position = " + position);
            final String[] nowData = arrayData.get(position).split("/");
            PASSWORD = nowData[6];
            //String viewData = nowData[0] + ", " + nowData[1] + ", " + nowData[2] + ", " + nowData[3]+ ", " + nowData[4]+ ", " + nowData[5];
            final EditText edittext = new EditText(getApplicationContext());
            edittext.setText(PASSWORD);
            AlertDialog.Builder dialog = new AlertDialog.Builder(Taxi.this);
            dialog.setTitle("데이터 삭제")
                    .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n" + "삭제를 원하시면 비밀번호 입력 후\n '네'버튼을 눌러주세요")
                    .setView(edittext)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (edittext.getText().toString().equals(PASSWORD)) {
                                postFirebaseDatabase(false);
                                getFirebaseDatabase();
                                Toast.makeText(Taxi.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                            } else
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
            return true;
        }
    };

    public boolean IsExistID() {
        boolean IsExist = arrayIndex.contains(PASSWORD);
        return IsExist;
    }

    public void postFirebaseDatabase(boolean add) {

        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if (add) {
            Post post = new Post(TITLE, SOURCE, DEST, TIME, PERSONNEL, CURRENT_PERSONNEL, PASSWORD, EDITOR_ID);
            postValues = post.toMap();
        }
        childUpdates.put("/POST/" + PASSWORD, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void postDataList(boolean add) {

        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if (add) {
            User user = new User(USER_ID,POST_LIST);
            postValues = user.toMap();
        }
        childUpdates.put("/LIST/" + USER_ID, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void getFirebaseDatabase() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
                arrayData.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Post get = postSnapshot.getValue(Post.class);
                    String[] info = {get.title, get.source, get.dest, get.time, get.personnel, get.current_personnel, get.password, get.editor_id};
                    String Result = info[0] + "/" + info[1] + "/" + info[2] + "/" + info[3] + "/" + info[4] + "/" + info[5] + "/" + info[6]+ "/" + info[7];
                    arrayData.add(Result);
                    arrayIndex.add(key);
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3] + info[4] + info[5] + info[6] +info[7]);
                }
                adapter.clear();
                adapter.addAll(arrayData);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("POST").orderByChild(sort);
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    public String setTextLength(String text, int length) {
        if (text.length() < length) {
            int gap = length - text.length();
            for (int i = 0; i < gap; i++) {
                text = text + " ";
            }
        }
        return text;
    }

    public void requestSendMemo() {
        KakaoTalkMessageBuilder builder = new KakaoTalkMessageBuilder();
        builder.addParam("TITLE", "제목: "+TITLE+"\n");
        builder.addParam("SOURCE", "출발지: "+SOURCE+"\n");
        builder.addParam("DEST", "목적지: "+DEST+"\n");
        builder.addParam("TIME", "시간: "+TIME+"\n");
        builder.addParam("EDITOR_ID", "작성자 ID: "+EDITOR_ID+"\n");
        KakaoTalkService.getInstance().requestSendMemo(new KakaoTalkResponseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Logger.d("send message to my chatroom : " + result);
                Toast.makeText(Taxi.this,"나에게 보내기",Toast.LENGTH_SHORT).show();
            }
                                                       }
                , "16632" // templateId
                , builder.build());
    }

    //카카오톡 메시지 내용 맵
    public class KakaoTalkMessageBuilder {
        public Map<String, String> messageParams = new HashMap<String, String>();

        public KakaoTalkMessageBuilder addParam(String key, String value) {
            messageParams.put("${" + key + "}", value);
            return this;
        }

        public Map<String, String> build() {
            return messageParams;
        }
    }
    //카카오톡 콜백 클래스
    private abstract class KakaoTalkResponseCallback<T> extends TalkResponseCallback<T> {
        @Override
        public void onNotKakaoTalkUser() {
            Logger.w("not a KakaoTalk user");
        }

        @Override
        public void onFailure(ErrorResult errorResult) {
            Toast.makeText(getApplicationContext(),"전송 실패",Toast.LENGTH_LONG).show();
            Logger.e("failure : " + errorResult);
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            //redirectLoginActivity();
//재로그인!
        }

        @Override
        public void onNotSignedUp() {
            //redirectSignupActivity();
//재로그인!
        }
    }



}
