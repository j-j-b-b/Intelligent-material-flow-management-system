package com.example.getnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getnet.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<City> cityList=new ArrayList<>();
    private ListView lv;
    //默认屏幕长宽，横屏状态
    int w=1500;
    int h=2000;
    //默认坐标原点
    float startx1=100;
    float starty1=900;
    //默认x y轴终点，以默认屏幕长宽为标准
    float endx1=1500;
    float endy1=40;
    int allX=15;//默认x轴计数个数
    int allY=15;//默认y轴等分个数
    float fullx=800;
    float fully=800;//默认最大y值
    //默认x y轴间隔值，以上面数据为基础得出
    float xinterval=(endx1-startx1)/allX;
    float yinterval=(starty1-endy1)/allY;
    //绘制点数据所需数据
    int all=0;//当前绘制个数,初始为0
    int citynum=0;
    float thispoint;
    float [] mypoint=new float[ allX];; //存储点数据
    float x1,x2,y1,y2;
    float cury=starty1;
    float curx=startx1;
    float rulex=(startx1-endx1);
    float ruley=(starty1-endy1);
    float min=0;
    float max=999999;
    float x3,y3;
    //int rule2=(starty1-endy1)/fully;
    private ImageView imageView;
    private Bitmap newb;
    private Canvas canvasTemp;
    private Paint p;
    private Paint p2;
    private Paint p3;

    Button btn;
    //////

    private TextView tvContent;
//    private String strFromNet;
    private Handler mHandler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                String strData=(String) msg.obj;
                //显示在文本上
                analysisJsonData(strData);
                Toast.makeText(MainActivity.this,"主线程收到消息了",Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //lv=findViewById(R.id.lv);
        btn=findViewById(R.id.mes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);
            }
        });
        chart();
    }

    public void chart(){
        //将imageView绑定到布局界面中id为image的IMageView
        imageView=(ImageView)findViewById(R.id.image);
        //以下内容的实现可以看我的另一个博客的详细解释，上有链接
        newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(newb);
        //Canvas canvasTemp2=new Canvas(newb);
        canvasTemp.drawColor(Color.TRANSPARENT);
        p = new Paint();
        //防锯齿
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);//STROKE,FILL
        p.setStrokeWidth(5);
        p.setColor(Color.LTGRAY);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(40);
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);//STROKE,FILL
        p.setStrokeWidth(2);
        p2=p;
        imageView.setImageBitmap(newb);
    }
    public void start(View view){
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
        Toast.makeText(this,"开启子线程请求网络！",Toast.LENGTH_SHORT).show();
    }
    public void analysisJsonData(String jsonData) {
        JSONArray set =new JSONArray();
        String res="";
        canvasTemp.drawColor(0, PorterDuff.Mode.CLEAR);
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            set = jsonObject.optJSONArray("distance_x_y");
            JSONObject jsonObject1=(JSONObject) set.get(0);
            curx=jsonObject1.optInt("x");
            cury=jsonObject1.optInt("y");
            x1= startx1 - curx/ fullx * rulex; //x轴按间隔递增就好（单单计数）
            y1= starty1 - cury/ fully * ruley;
            //canvasTemp.drawText("你的位置："+startx1+","+starty1, startx1, starty1, p);//原点
            p.setStrokeWidth(20);
            p.setColor(Color.BLUE);
            canvasTemp.drawPoint(x1, y1, p);
            citynum=set.length();
            p.setColor(Color.BLACK);
            for(int i=1;i<set.length();i++){
                JSONObject jsonObject2=(JSONObject) set.get(i);
                curx=jsonObject2.optInt("x");
                cury=jsonObject2.optInt("y");
                x2 = startx1 - curx/ fullx * rulex; //x轴按间隔递增就好（单单计数）
                y2 = starty1 - cury/ fully * ruley;
                p.setStrokeWidth(10);
                canvasTemp.drawText("("+curx+","+cury+")", x2, y2, p);
                canvasTemp.drawPoint(x2, y2, p);
                p.setStrokeWidth(5);
                canvasTemp.drawLine(x1, y1, x2, y2, p);
                x1=x2;
                y1=y2;
            }
            p.setColor(Color.RED);
            p.setStrokeWidth(20);
            canvasTemp.drawPoint(x2, y2, p);
            //canvasTemp.drawText("终点", x2, y2, p);//原点
            p.setColor(Color.BLACK);
            p.setStrokeWidth(5);
            canvasTemp.drawLine(startx1, starty1, x2, y2, p);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private String getStringFromNet() {
        String Jstr=NetUtil.doGet();
       return NetUtil.doGet();
    }
}