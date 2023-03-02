package com.example.getnet;


import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity
{
    @SuppressLint("WrongViewCast")
    EditText user,pwo;
    Button button5,buttonR;
    TextView tvC;
    private String mUrl = "http://192.168.43.101:5000/auth/login";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
        button5.setOnClickListener(new MyButtonListener());
    }
    @SuppressLint("WrongViewCast")
    private void initView ()
    {
        user = findViewById(R.id.user);
        pwo = findViewById(R.id.pwo);
        tvC = findViewById(R.id.tvRC);
        button5 = findViewById(R.id.button5);
        //点击注册跳转
        buttonR = findViewById(R.id.buttonR);
        buttonR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    //显示登录信息
    public void Login(View view)
    {

    }

    class MyButtonListener implements View.OnClickListener
    {
        public void onClick(View view)
        {
            String name=user.getText().toString();
            String pow=pwo.getText().toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final ByteArrayOutputStream bos;
                    try {
                        String name = user.getText().toString();
                        String password = pwo.getText().toString();
                        String data = "username="+name + "&password=" + password;
                        HttpURLConnection conn = (HttpURLConnection) new URL(mUrl).openConnection();
                        conn.setReadTimeout(5000);
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        conn.setRequestProperty("Content-Length", data.length() + "");
                        conn.setConnectTimeout(5000);
                        OutputStream out = conn.getOutputStream();
                        out.write(data.getBytes());
                        if (conn.getResponseCode() == 200) {
                            InputStream inputStream = conn.getInputStream();
                            bos = new ByteArrayOutputStream();
                            int len = 0;
                            byte[] buffer = new byte[1024];
                            while((len = inputStream.read(buffer ))!=-1){
                                bos.write(buffer, 0, len);
                            }
                            bos.flush();
                            inputStream.close();
                            bos.close();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        String data=new String(bos.toByteArray(),"utf-8");
                                        JSONObject jsonObject=new JSONObject(data);
                                        if(jsonObject.optString("isLogin").equals("0")){
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_LONG).show();
                                        }

                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

}
