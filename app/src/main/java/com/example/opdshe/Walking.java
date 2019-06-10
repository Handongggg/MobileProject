package com.example.opdshe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Walking extends AppCompatActivity {
    Toolbar toolbar;
    Button button;
    Button present;
    EditText start;
    EditText desti;
    int i = 1;
    double a = 0;
    double a1 = 0;
    double b = 0;
    double b1 = 0;
    double lan_d = 0;
    double lon_d = 0;
    ArrayList lan_list = new ArrayList();
    ArrayList lon_list = new ArrayList();
    CheckBox safe;
    Button check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        present = (Button) findViewById(R.id.present_button);
        safe = (CheckBox)findViewById(R.id.safe_walking);
        button = (Button) findViewById(R.id.button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        start = (EditText) findViewById(R.id.star);
        desti = (EditText) findViewById(R.id.des);
        check = (Button)findViewById(R.id.check);
        toolbar.setTitle("Walking");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Geocoder geocoder = new Geocoder(this);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("start");
        start.setText(name);
        name = intent.getExtras().getString("desti");
        desti.setText(name);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Address> list = null;
                String start1 = null, desti1 = null;
                String str = start.getText().toString();
                try {
                    list = geocoder.getFromLocationName
                            (str, // 지역 이름
                                    10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                }
                if (list != null) {
                    // 해당되는 주소로 인텐트 날리기
                    Address addr = list.get(0);
                    double lat = addr.getLatitude();
                    double lon = addr.getLongitude();
                    String sss = String.format("%f,%f", lat, lon);
                    start1 = sss;
                }

                str = desti.getText().toString();
                try {
                    list = geocoder.getFromLocationName
                            (str, // 지역 이름
                                    10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                }
                if (list != null) {
                    // 해당되는 주소로 인텐트 날리기
                    Address addrs = list.get(0);
                    double lat_desti = addrs.getLatitude();
                    double lon_desti = addrs.getLongitude();
                    String sss1 = String.format("%f,%f", lat_desti, lon_desti);
                    desti1 = sss1;
                }

                //여기까지 주소를 위도,경도로 변환

                String url =
                        "daummaps://route?sp=" + start1 + "&ep=" + desti1 + "&by=FOOT";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(safe.isChecked() == true) {

                    for (i = 1; i < 34; i++) {
                        new GetXMLTask().execute("https://openapi.gg.go.kr/CCTV?KEY=6734cecd49a14e4bb647e6515d8ce56a&pSize=1000&pIndex=" + i);
                    }
                }
            }
        });

        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Address> list = null;

                String str = start.getText().toString();
                try {
                    list = geocoder.getFromLocationName
                            (str, // 지역 이름
                                    10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                }
                if (list != null) {
                    // 해당되는 주소로 인텐트 날리기
                    Address addr = list.get(0);
                    double lat = addr.getLatitude();
                    double lon = addr.getLongitude();
                    a = lat;
                    a1 = lon;
                }

                str = desti.getText().toString();
                try {
                    list = geocoder.getFromLocationName
                            (str, // 지역 이름
                                    10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                }
                if (list != null) {
                    // 해당되는 주소로 인텐트 날리기
                    Address addrs = list.get(0);
                    double lat_desti = addrs.getLatitude();
                    double lon_desti = addrs.getLongitude();
                    b = lat_desti;
                    b1 = lon_desti;
                }

                ArrayList<Double> lan_list1 = new ArrayList();
                ArrayList<Double> lon_list1 = new ArrayList();

                if(a>b) {
                    lan_d = a - b;
                }
                else {
                    lan_d = b - a;
                }
                if(a1>b1) {
                    lon_d = a1 - b1;
                }
                else {
                    lon_d = b1 - a1;
                }
                double x,y;
                lan_list1.add(a);
                lon_list1.add(a1);
                lan_list1.add(b);
                lon_list1.add(b1);
                for (int i=0; i < lan_list.size(); i++) {
                    x = (double) lan_list.get(i);
                    y = (double) lon_list.get(i);
                    if(a>b) {
                        if (Double.compare(b-0.005,x)==-1 && Double.compare(a+0.005,x)==1){
                            if(a1>b1){
                                if(Double.compare(b1-0.005,y)==-1 && Double.compare(a1+0.005,y)==1  ){
                                    lan_list1.add(x);
                                    lon_list1.add(y);
                                }
                            }
                            else if(b1>a1){
                                if(Double.compare(b1+0.005,y)==1 && Double.compare(a1-0.005,y)==-1  ){
                                    lan_list1.add(x);
                                    lon_list1.add(y);
                                }
                            }
                        }
                    }

                    if(a<b) {
                        if (Double.compare(b+0.005,x)==1 && Double.compare(a-0.005,x)==-1){
                            if(a1>b1){
                                if(Double.compare(b1-0.005,y)==-1 && Double.compare(a1+0.005,y)==1  ){
                                    lan_list1.add(x);
                                    lon_list1.add(y);
                                }
                            }
                            else if(b1>a1){
                                if(Double.compare(b1+0.005,y)==1 && Double.compare(a1-0.005,y)==-1  ){
                                    lan_list1.add(x);
                                    lon_list1.add(y);
                                }
                            }
                        }
                    }
                }

                Intent intent1 = new Intent(Walking.this, Road.class);
                Bundle b = new Bundle();
                intent1.putExtra("bundle",b);
                intent1.putExtra("lon",lon_list1);
                intent1.putExtra("lan",lan_list1);
                startActivityForResult(intent1, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_search:
                Toast.makeText(getApplicationContext(), " press search", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_account:
                Toast.makeText(getApplicationContext(), " press account", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_logout:
                Toast.makeText(getApplicationContext(), " press logout", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class GetXMLTask extends AsyncTask<String, Void, Document> {
        ProgressDialog asyncDialog = new ProgressDialog(
                Walking.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            Document doc = null;

            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            NodeList nodeList = doc.getElementsByTagName("row");
            for(int i = 0; i< nodeList.getLength(); i++){

                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;
                String name = fstElmnt.getElementsByTagName("SIGUN_NM").item(0).getChildNodes().item(0).getNodeValue().toString();

                if(name.equals("수원시")) {
                    NodeList WGS84_LOGT = fstElmnt.getElementsByTagName("WGS84_LOGT");
                    String s = WGS84_LOGT.item(0).getChildNodes().item(0).getNodeValue();
                    lon_list.add(Double.parseDouble(s));
                    NodeList WGS84_LAT = fstElmnt.getElementsByTagName("WGS84_LAT");
                    s = WGS84_LAT.item(0).getChildNodes().item(0).getNodeValue();
                    lan_list.add(Double.parseDouble(s));
                }
            }
            asyncDialog.dismiss();
            super.onPostExecute(doc);
        }
    }
}