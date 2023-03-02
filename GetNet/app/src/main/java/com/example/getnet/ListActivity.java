package com.example.getnet;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.icu.util.Measure;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getnet.util.NetUtil;
import com.example.getnet.util.NetUtil1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Context context;
    CityAdapter cityAdapter;
    List<City> cities=new ArrayList<City>();
    ListView listView;
    Button btn;
    TextView tv;
    private String delurl="http://192.168.1.102:5000/delete/";
    private int curPos;
    long ins;
    private Handler mHandler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                String strData=(String) msg.obj;
                //显示在文本上
                tv.setText(analysisData(strData));
                listView.setAdapter(cityAdapter);
                //Toast.makeText(ListActivity.this,"主线程收到消息了",Toast.LENGTH_SHORT).show();
            }
        }
    };

    private String analysisData(String strData) {
        String res="";
        try {
            JSONObject set = new JSONObject(strData);
            //set = jsonObject.optJSONArray("distance_x_y");
            //canvasTemp.drawText("你的位置："+startx1+","+starty1, startx1, starty1, p);//原点
            JSONArray arr=set.optJSONArray("data");
            for(int i=0;i<arr.length();i++){
                JSONObject obj=(JSONObject) arr.getJSONObject(i);
                City city=new City(obj.optInt("id"),obj.optString("name"),obj.optString("num"),
                        obj.optInt("x"),obj.optInt("y"));
                cities.add(city);
            }
            cityAdapter=new CityAdapter(ListActivity.this,R.layout.items,cities);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
  //      dbHelper.onUpgrade(db,3,4);
        listView=findViewById(R.id.lv);
        tv=findViewById(R.id.tv);
        btn=findViewById(R.id.del);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickid=cities.get(curPos).getId();
                Thread thread1=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ByteArrayOutputStream bos;
                        try {
                            String data = ""+clickid ;
                            HttpURLConnection conn = (HttpURLConnection) new URL(delurl+clickid).openConnection();
                            conn.setReadTimeout(5000);
                            conn.setRequestMethod("DELETE");
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
                                            cityAdapter.notifyDataSetChanged();
                                            listView.setAdapter(cityAdapter);
                                            Intent intent=new Intent(ListActivity.this,MainActivity.class);
                                            startActivity(intent);
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

    }

    public void cast(View view){
        //    String strFromNet=getStringFromNet();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String strFromNet=getStringFromNet();
                Message message=new Message();
                message.what=0;
                message.obj=strFromNet;
                mHandler.sendMessage(message);
                //    tvContent.setText(strFromNet);
            }
        }).start();
        //      tvContent.setText(strFromNet);
        //Toast.makeText(this,"开启子线程请求网络！",Toast.LENGTH_SHORT).show();
    }

    private String getStringFromNet() {
        return NetUtil1.doGet();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        curPos=position;
        view.setBackgroundColor(Color.YELLOW);
        cityAdapter.setCurrentPos(position);
        cityAdapter.notifyDataSetChanged();

    }
}
