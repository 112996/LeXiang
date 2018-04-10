package com.escience.weather;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.escience.weather.Dialog.CityDialog;
import com.escience.weather.Listener.CityDialogListener;
import com.escience.weather.Network.JsonConvert;
import com.escience.weather.Network.Net;
import com.escience.weather.Util.Status;
import com.escience.weather.bean.WResult;
import com.escience.weather.bean.WSK;
import com.escience.weather.bean.WToday;
import com.escience.weather.ratio.DynamicAvatarView;
import com.escience.weather.ratio.RatioLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TestActivity extends Activity {

    private DynamicAvatarView mDynamic;
    private RatioLayout mRatio;
    private TextView template;
    private String city;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Status.setTranslucentStatus(getWindow());
        final Button place=(Button)findViewById(R.id.title_place);
        mRatio = (RatioLayout) findViewById(R.id.ratio);
        mDynamic = (DynamicAvatarView) findViewById(R.id.dynamic);
        template=(TextView)findViewById(R.id.template);
        sharedPreferences=getSharedPreferences("action",MODE_PRIVATE);
        city=sharedPreferences.getString("city", "成都");
        place.setText(city);
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CityDialog(TestActivity.this,new CityDialogListener(){
                    @Override
                    public void finish(String p,String c) {
                        city=c;
                        sharedPreferences.edit().putString("province",p).putString("city",c).putString("json",null).apply();
                        place.setText(city);
                        new MsgAsyncTask().execute();
                    }
                    public void cancel(){
                    }
                }).show();
            }
        });
        String[] texts = {"风向:南风","风速:2级", "湿度:25%", "紫外线:弱","最低温:14°","最高温:25°"};
        //final boolean[] bgs = { false, true, false, true, false, true, false, true,false};
        //mRatio.changeTextBackground(bgs);
        //mRatio.setBubbleEnterFinishListener();
        mRatio.addText(texts);
        Button down=(Button)findViewById(R.id.down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRatio.exitBubble();
                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
                }
        });
        mDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRatio.exitBubble();
                Intent intent =new Intent(TestActivity.this,TempActivity.class);
                startActivity(intent);
            }
        });
        //        mRatio.setInnerCenterListener(new RatioLayout.InnerCenterListener() {
//            @Override
//            public void innerCenterHominged(int position, String text) {
//                texts[position] = addNumber(texts[position]);
//                mRatio.changeText(texts);
//            }
//            @Override
//            public void innerCenter(int position, String text) {
//                if(position % 2 == 0){
//                    mRatio.setPlayLoveXin(true);
//                }else{
//                    mRatio.setPlayLoveXin(false);
//                }
//            }
//        });
        new MsgAsyncTask().execute();
    }
    class MsgAsyncTask extends AsyncTask<Void, Integer, Void> {
        WResult result;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //String json="{\"resultcode\":\"200\",\"reason\":\"successed!\",\"result\":{\"sk\":{\"temp\":\"23\",\"wind_direction\":\"南风\",\"wind_strength\":\"0级\",\"humidity\":\"71%\",\"time\":\"18:11\"},\"today\":{\"temperature\":\"18℃~24℃\",\"weather\":\"多云转阴\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"02\"},\"wind\":\"微风\",\"week\":\"星期二\",\"city\":\"广州\",\"date_y\":\"2017年11月28日\",\"dressing_index\":\"舒适\",\"dressing_advice\":\"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。\",\"uv_index\":\"最弱\",\"comfort_index\":\"\",\"wash_index\":\"较适宜\",\"travel_index\":\"较适宜\",\"exercise_index\":\"较适宜\",\"drying_index\":\"\"},\"future\":{\"day_20171128\":{\"temperature\":\"18℃~24℃\",\"weather\":\"多云转阴\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"02\"},\"wind\":\"微风\",\"week\":\"星期二\",\"date\":\"20171128\"},\"day_20171129\":{\"temperature\":\"17℃~25℃\",\"weather\":\"阴转小雨\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"07\"},\"wind\":\"微风\",\"week\":\"星期三\",\"date\":\"20171129\"},\"day_20171130\":{\"temperature\":\"14℃~21℃\",\"weather\":\"小雨转阴\",\"weather_id\":{\"fa\":\"07\",\"fb\":\"02\"},\"wind\":\"微风\",\"week\":\"星期四\",\"date\":\"20171130\"},\"day_20171201\":{\"temperature\":\"13℃~18℃\",\"weather\":\"阴转多云\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"01\"},\"wind\":\"微风\",\"week\":\"星期五\",\"date\":\"20171201\"},\"day_20171202\":{\"temperature\":\"13℃~19℃\",\"weather\":\"多云\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"01\"},\"wind\":\"微风\",\"week\":\"星期六\",\"date\":\"20171202\"},\"day_20171203\":{\"temperature\":\"17℃~25℃\",\"weather\":\"阴转小雨\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"07\"},\"wind\":\"微风\",\"week\":\"星期日\",\"date\":\"20171203\"},\"day_20171204\":{\"temperature\":\"13℃~18℃\",\"weather\":\"阴转多云\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"01\"},\"wind\":\"微风\",\"week\":\"星期一\",\"date\":\"20171204\"}}},\"error_code\":0}";
                String json=sharedPreferences.getString("json",null);
                String time=sharedPreferences.getString("time", null);
                String timenow=new SimpleDateFormat("yyyyMMddhhmm", Locale.CHINA).format(new Date());
                if(json==null||time==null||Long.parseLong(timenow)-Long.parseLong(time)>30){
                    json= Net.request("http://v.juhe.cn/weather/index?cityname=" + city + "&key=a682cda3ab62054c65707dc210f04c53");
                    sharedPreferences.edit().putString("json",json).putString("time",timenow).apply();
                }
                result=(WResult) JsonConvert.DeserializeObject(json, new WResult());
                publishProgress(0);
           }catch (Exception e){
                Log.e("code",e.toString());
                publishProgress(-1);
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]==0){
                try {
                    WSK wsk=result.getSK();
                    WToday wToday=result.getToday();
                    template.setText(wsk.temp+"°");
                    String[] texts = {"风向:"+wsk.wind_direction,"风速:"+wsk.wind_strength, "湿度:"+wsk.humidity, "紫外线:"+wToday.uv_index,"最低温:"+wToday.getLow()+"°","最高温:"+wToday.getHigh()+"°"};
                    mRatio.changeText(texts);
                }catch (Exception e){

                }
            }else{
                Toast.makeText(TestActivity.this, "网络无连接", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String addNumber(String text) {
        int start = text.indexOf("(");
        int end = text.indexOf(")");
        int number = Integer.parseInt(text.substring(start + 1, end));
        number++;
        return text.substring(0, start) + "(" + number + ")";
    }

    public void exit(View view) {
        //mDynamic.exitAnim();
        mRatio.exitBubble();
    }

    public void enter(View view) {
        //mDynamic.enterAnim();
        mRatio.enterBubble();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("-------------------onResume:");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("-------------------onStart:");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("-------------------onPause:");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("-------------------onStop:");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mRatio.enterBubble();
        System.out.println("-------------------onRestart:");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRatio.destry();
        System.out.println("-------------------onDestroy:");
    }
}
