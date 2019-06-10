package com.example.opdshe;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class DaumWebViewActivity extends AppCompatActivity {

    private WebView daum_webView;
    private TextView daum_result;
    private Handler handler;
    Button button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daum_web_view);
        button = (Button)findViewById(R.id.adress);
        daum_result = (TextView) findViewById(R.id.daum_result);

        Toast.makeText(this,"출발지를 검색하여 확인버튼을 누르세요",Toast.LENGTH_SHORT).show();
        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }

    public void init_webView() {
        // WebView 설정
        daum_webView = (WebView) findViewById(R.id.daumwebview);
        // JavaScript 허용
        daum_webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        daum_webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        daum_webView.setWebChromeClient(new WebChromeClient());
        // webview url load. php 파일 주소
        daum_webView.loadUrl("http://hdy0409.cafe24.com/Daum.php");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    daum_result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    // WebView를 초기화 하지않으면 재사용할 수 없음

                    init_webView();
                }
            });
        }
    }

    int i = 0;
    String temp, start = null, desti;
    public void onClick(View v){
        Toast.makeText(this,"도착지를 검색하여 확인버튼을 누르세요",Toast.LENGTH_SHORT).show();
        temp = (String) daum_result.getText();
        if(i ==0) {
            start = (String) daum_result.getText();
            button.setText("도착지 확인");
            i++;
        }
        else{
            if(start != null) {
                temp = start;
            }
            i =0;
            desti = (String) daum_result.getText();
            Intent intent = new Intent(this, Walking.class);
            intent.putExtra("start",temp);
            intent.putExtra("desti",desti);
            start = null;
            temp = null;
            desti = null;
            startActivityForResult(intent,1);
        }

    }

}