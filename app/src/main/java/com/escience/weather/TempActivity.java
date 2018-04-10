package com.escience.weather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.escience.weather.Dialog.CityDialog;
import com.escience.weather.Listener.CityDialogListener;
import com.escience.weather.Network.JsonConvert;
import com.escience.weather.Network.Net;
import com.escience.weather.Util.Status;
import com.escience.weather.bean.WF;
import com.escience.weather.bean.WResult;
import com.escience.weather.bean.WSK;
import com.escience.weather.bean.WToday;

public class TempActivity extends Activity {
    private SharedPreferences sharedPreferences;
    private String city;
    TextView bt;
    TextView btt;
    TextView yi;
    TextView xin;
    TextView xin1;
    TextView xin2;
    TextView xin3;
    TextView low;
    TextView low1;
    TextView low2;
    TextView low3;
    TextView high;
    TextView high1;
    TextView high2;
    TextView high3;
    TextView zhiwai;
    TextView shidu;
    TextView tigan;
    TextView feng;
    ImageView tianqi;
    ImageView tianqi1;
    ImageView tianqi2;
    ImageView tianqi3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        Status.setTranslucentStatus(getWindow());
        final Button place=(Button)findViewById(R.id.title_place_temp);
        Button button=(Button)findViewById(R.id.title_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         bt=(TextView)findViewById(R.id.template_big);
         btt=(TextView)findViewById(R.id.template_big_text);
         yi=(TextView)findViewById(R.id.pm);
         xin=(TextView)findViewById(R.id.xinqi);
         xin1=(TextView)findViewById(R.id.xinqi1);
         xin2=(TextView)findViewById(R.id.xinqi2);
         xin3=(TextView)findViewById(R.id.xinqi3);
         low=(TextView)findViewById(R.id.template_low);
         low1=(TextView)findViewById(R.id.template_low1);
         low2=(TextView)findViewById(R.id.template_low2);
         low3=(TextView)findViewById(R.id.template_low3);
         high=(TextView)findViewById(R.id.template_high);
         high1=(TextView)findViewById(R.id.template_high1);
         high2=(TextView)findViewById(R.id.template_high2);
         high3=(TextView)findViewById(R.id.template_high3);
        tianqi=(ImageView)findViewById(R.id.tianqi);
        tianqi1=(ImageView)findViewById(R.id.tianqi1);
        tianqi2=(ImageView)findViewById(R.id.tianqi2);
        tianqi3=(ImageView)findViewById(R.id.tianqi3);
        zhiwai=(TextView)findViewById(R.id.ziwaixian_text);
        shidu=(TextView)findViewById(R.id.shidu_text);
        tigan=(TextView)findViewById(R.id.tigan_text);
        feng=(TextView)findViewById(R.id.fengxiang_text);
        sharedPreferences=getSharedPreferences("action", MODE_PRIVATE);
        city=sharedPreferences.getString("city", "成都");
        place.setText(city);
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CityDialog(TempActivity.this, new CityDialogListener() {
                    @Override
                    public void finish(String p, String c) {
                        city=c;
                        sharedPreferences.edit().putString("province", p).putString("city", c).putString("json",null).apply();
                        place.setText(city);
                        new MsgAsyncTask().execute();
                    }
                    public void cancel() {
                    }
                }).show();
            }
        });
        new MsgAsyncTask().execute();
    }
    class MsgAsyncTask extends AsyncTask<Void, Integer, Void> {
        WResult result;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //String json="{\"resultcode\":\"200\",\"reason\":\"successed!\",\"result\":{\"sk\":{\"temp\":\"23\",\"wind_direction\":\"南风\",\"wind_strength\":\"0级\",\"humidity\":\"71%\",\"time\":\"18:11\"},\"today\":{\"temperature\":\"18℃~24℃\",\"weather\":\"多云转阴\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"02\"},\"wind\":\"微风\",\"week\":\"星期二\",\"city\":\"广州\",\"date_y\":\"2017年11月28日\",\"dressing_index\":\"舒适\",\"dressing_advice\":\"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。\",\"uv_index\":\"最弱\",\"comfort_index\":\"\",\"wash_index\":\"较适宜\",\"travel_index\":\"较适宜\",\"exercise_index\":\"较适宜\",\"drying_index\":\"\"},\"future\":{\"day_20171128\":{\"temperature\":\"18℃~24℃\",\"weather\":\"多云转阴\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"02\"},\"wind\":\"微风\",\"week\":\"星期二\",\"date\":\"20171128\"},\"day_20171129\":{\"temperature\":\"17℃~25℃\",\"weather\":\"阴转小雨\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"07\"},\"wind\":\"微风\",\"week\":\"星期三\",\"date\":\"20171129\"},\"day_20171130\":{\"temperature\":\"14℃~21℃\",\"weather\":\"小雨转阴\",\"weather_id\":{\"fa\":\"07\",\"fb\":\"02\"},\"wind\":\"微风\",\"week\":\"星期四\",\"date\":\"20171130\"},\"day_20171201\":{\"temperature\":\"13℃~18℃\",\"weather\":\"阴转多云\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"01\"},\"wind\":\"微风\",\"week\":\"星期五\",\"date\":\"20171201\"},\"day_20171202\":{\"temperature\":\"13℃~19℃\",\"weather\":\"多云\",\"weather_id\":{\"fa\":\"01\",\"fb\":\"01\"},\"wind\":\"微风\",\"week\":\"星期六\",\"date\":\"20171202\"},\"day_20171203\":{\"temperature\":\"17℃~25℃\",\"weather\":\"阴转小雨\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"07\"},\"wind\":\"微风\",\"week\":\"星期日\",\"date\":\"20171203\"},\"day_20171204\":{\"temperature\":\"13℃~18℃\",\"weather\":\"阴转多云\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"01\"},\"wind\":\"微风\",\"week\":\"星期一\",\"date\":\"20171204\"}}},\"error_code\":0}";
                //String json=Net.request("http://v.juhe.cn/weather/index?cityname="+city+"&key=a682cda3ab62054c65707dc210f04c53");
                String json=getSharedPreferences("action", MODE_PRIVATE).getString("json",null);
                if(json==null){
                    json= Net.request("http://v.juhe.cn/weather/index?cityname=" + city + "&key=a682cda3ab62054c65707dc210f04c53");
                    getSharedPreferences("action",MODE_PRIVATE).edit().putString("json",json);
                }
                result=(WResult)JsonConvert.DeserializeObject(json, new WResult());
                publishProgress(0);
            }catch (Exception e){
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
                    WF wf=result.getFuture(wToday.date_y,0);
                    WF wf1=result.getFuture(wToday.date_y,1);
                    WF wf2=result.getFuture(wToday.date_y,2);
                    WF wf3=result.getFuture(wToday.date_y,3);
                    bt.setText(wsk.temp+"°");
                    btt.setText(wToday.weather);
                    yi.setText(wToday.dressing_index);
                    xin.setText(wf.week);
                    low.setText(wf.getLow());
                    high.setText(wf.getHigh());
                    xin1.setText(wf1.week);
                    low1.setText(wf1.getLow());
                    high1.setText(wf1.getHigh());
                    xin2.setText(wf2.week);
                    low2.setText(wf2.getLow());
                    high2.setText(wf2.getHigh());
                    xin3.setText(wf3.week);
                    low3.setText(wf3.getLow());
                    high3.setText(wf3.getHigh());
                    zhiwai.setText("紫外线"+wToday.uv_index);
                    shidu.setText("湿度"+wsk.humidity);
                    tigan.setText("体感温度"+wToday.getMid());
                    feng.setText(wsk.wind_direction+wsk.wind_strength);
                    tianqi.setBackgroundResource(wf.getR());
                    tianqi1.setBackgroundResource(wf1.getR());
                    tianqi2.setBackgroundResource(wf2.getR());
                    tianqi3.setBackgroundResource(wf3.getR());
                }catch (Exception e){
                    Toast.makeText(TempActivity.this,"数据解析异常",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(TempActivity.this,"网络无连接",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
