package com.example.opdshe;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.User;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity  {
    //google
    SignInButton Google_Login;
    private static final int RC_SIGN_IN = 1000;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    //kakao
    private static final String TAG = "";
    SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getHashKey();
        //kakao
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        requestMe();



    }






    ////////////kakao
    private void getHashKey(){
        try {                                                        // 패키지이름을 입력해줍니다.
            PackageInfo info = getPackageManager().getPackageInfo("com.example.opdshe", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("temp","key_hash="+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            UserManagement user= UserManagement.getInstance();
            user.requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        //에러로 인한 로그인 실패
                        // finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {

                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.

                     //Log.e("UserProfile", userProfile.toString());
                     //Log.e("UserProfile", userProfile.getId() + "");


                    Log.e("UserProfile", userProfile.toString());
                    Log.e("UserProfile", userProfile.getId() + "");
                    Taxi.USER_ID=userProfile.getId()+"";
                    Taxi_MyPage.MYPAGE_ID=userProfile.getId()+"";
                    Taxi_MyPage.MYPAGE_NICKNAME=userProfile.getNickname();
                    Taxi_MyPage.MYPAGE_IMAGE=userProfile.getThumbnailImagePath();
                    Taxi_MyPage.MYPAGE_EMAIL=userProfile.getEmail();
                    Intent intent= new Intent(Login.this, MainActivity.class);
                    startActivity(intent);


                }
            });

        }
        // 세션 실패시
        @Override
        public void onSessionOpenFailed(KakaoException exception) {

        }
    }
    public void requestMe() {
        //유저의 정보를 받아오는 함수
        UserManagement user=UserManagement.getInstance();
        user.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e(TAG, "error message=" + errorResult);
//                super.onFailure(errorResult);
            }
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d(TAG, "onSessionClosed1 =" + errorResult);
            }
            @Override
            public void onNotSignedUp() {
                //카카오톡 회원이 아닐시
                Log.d(TAG, "onNotSignedUp ");
            }
            @Override
            public void onSuccess(UserProfile result) {
                Log.e("UserProfile", result.toString());
                Log.e("UserProfile", result.getId() + "");
                Taxi.USER_ID=result.getId()+"";
                Taxi_MyPage.MYPAGE_ID=result.getId()+"";
                Taxi_MyPage.MYPAGE_NICKNAME=result.getNickname();
                Taxi_MyPage.MYPAGE_IMAGE=result.getThumbnailImagePath();
                Taxi_MyPage.MYPAGE_EMAIL=result.getEmail();
                //Toast.makeText(Login.this, "로그인 성공", Toast.LENGTH_SHORT).show();

            }
        });
    }

}