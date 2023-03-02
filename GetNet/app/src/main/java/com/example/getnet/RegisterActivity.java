package com.example.getnet;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    @SuppressLint("WrongViewCast")
    EditText user, pwo, rpw, uemail,pcode;
    Button buttonR, buttonB;
    RadioGroup sexq;
    CheckBox checkBox3;
    String sex="男";
    Button buttonP;
    private String mUrl = "http://192.168.43.101:5000/auth/register";
    private  String dUrl="http://192.168.43.101:5000/auth/captcha/email?";
    private Handler mHandler =  new Handler();


    @SuppressLint("WrongViewCast")

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        {
            buttonB = findViewById(R.id.buttonB);
            buttonR = findViewById(R.id.buttonR);
            uemail = findViewById(R.id.uemail);
            pwo = findViewById(R.id.pwo);
            rpw = findViewById(R.id.rpw);
            user = findViewById(R.id.user);
            sexq = findViewById(R.id.sexq);
            checkBox3 = findViewById(R.id.checkBox3);
            buttonP=findViewById(R.id.tvnump);
            pcode=findViewById(R.id.uprove);
            buttonR.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               if (checkBox3.isChecked())
                                               {
                                                   if (rpw.getText().toString().trim().equals("") || uemail.getText().toString().trim().equals("") || pwo.getText().toString().trim().equals("") || user.getText().toString().trim().equals(""))
                                                   {
                                                       Toast.makeText(RegisterActivity.this, "请填完所有信息", Toast.LENGTH_LONG).show();
                                                       return;
                                                   }
                                                   if (!rpw.getText().toString().trim().equals(pwo.getText().toString().trim()))
                                                   {
                                                       Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_LONG).show();
                                                       return;
                                                   }
                                                   Thread thread2=new Thread(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           final ByteArrayOutputStream bos;
                                                           try {
                                                               String name = user.getText().toString();
                                                               String password = pwo.getText().toString();
                                                               String email=uemail.getText().toString();
                                                               String cpw=rpw.getText().toString();
                                                               String code=pcode.getText().toString();
                                                               String data = "email="+email + "&adminname=" + name+ "&password=" + password
                                                                       + "&password_confirm=" + cpw+"&captcha="+code;;
                                                               //Toast.makeText(RegisterActivity.this,"111111", Toast.LENGTH_LONG).show();
                                                               HttpURLConnection conn = (HttpURLConnection) new URL(mUrl).openConnection();
                                                               //Toast.makeText(RegisterActivity.this,"hahaha", Toast.LENGTH_LONG).show();
                                                               conn.setReadTimeout(5000);
                                                               conn.setRequestMethod("POST");
                                                               conn.setDoOutput(true);
                                                               conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                                               //conn.setRequestProperty("Content-Length", data.length() + "");
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
                                                                               //tvC.setText(new String(bos.toByteArray(),"utf-8"));
                                                                               String res=new String(bos.toByteArray(),"utf-8");
                                                                               Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();

                                                                           } catch (UnsupportedEncodingException e) {
                                                                               e.printStackTrace();
                                                                           }
                                                                       }
                                                                   });
                                                               }
                                                           } catch (Exception e) {
                                                               e.printStackTrace();
                                                           }
                                                       }
                                                   });
                                                   thread2.start();
                                               }
                                               else
                                               {
                                                   Toast.makeText(RegisterActivity.this, "请勾选已阅读会员协议", Toast.LENGTH_LONG).show();
                                               }

                                           }
                                       }
            );
            buttonP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name=user.getText().toString();
                    String pow=pwo.getText().toString();
                    Thread thread1=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final ByteArrayOutputStream bos;
                            try {
                                String email = uemail.getText().toString();
                                String data = "email="+email ;
                                HttpURLConnection conn = (HttpURLConnection) new URL(dUrl+data).openConnection();
                                conn.setReadTimeout(5000);
                                conn.setRequestMethod("GET");
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
                                                //tvC.setText(new String(bos.toByteArray(),"utf-8"));
                                                String mes=new String(bos.toByteArray(),"utf-8");
                                                //Toast.makeText(RegisterActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread1.start();

                }
            });
            sexq.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i)
                {
                    switch (radioGroup.getCheckedRadioButtonId())
                    {
                        case R.id.ButtonM:
                            sex = "男";
                            break;
                        case R.id.ButtonW:
                            sex = "女";
                            break;
                    }
                }
            });

            //点击返回跳转登录界面

            buttonB.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    finish();
                }
            });
        }

    }

}


