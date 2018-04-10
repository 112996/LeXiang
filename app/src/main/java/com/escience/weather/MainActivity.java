package com.escience.weather;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.escience.weather.Callback.CardItemTouchHelperCallback;
import com.escience.weather.Config.CardConfig;
import com.escience.weather.DataBase.DBHandler;
import com.escience.weather.Dialog.InputDialog;
import com.escience.weather.Listener.InputDialogListener;
import com.escience.weather.Listener.OnSwipeListener;
import com.escience.weather.Network.Net;
import com.escience.weather.Network.NetBuilder;
import com.escience.weather.Util.ListUtil;
import com.escience.weather.Util.Status;
import com.escience.weather.View.CardLayoutManager;
import com.escience.weather.View.CircleImageView;
import com.escience.weather.bean.BaseItem;
import com.escience.weather.bean.ItemMood;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    private ArrayList<BaseItem> mArrays;
    private MyRAdapter adapter;
    private String sponsor = null;
    private SharedPreferences sp;
    private int start = 0;
    private float sy;
    private float sx;
    private boolean IsDown;
    private boolean LockDown;
    private Button add;
    private Button rsh;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Status.setTranslucentStatus(getWindow());
        Button button=(Button)findViewById(R.id.button);
        add=(Button)findViewById(R.id.mode_add_btn);
        rsh=(Button)findViewById(R.id.mode_rsh_btn);
        Button more=(Button)findViewById(R.id.title_more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      finish();
                  }
              }
        );
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setVisibility(View.GONE);
                rsh.setVisibility(View.GONE);
                InputDialog inputDialog= new InputDialog(MainActivity.this,Listener);
                inputDialog.show();
            }
        });
        rsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start=0;
                initData();
            }
        });
        sp = getSharedPreferences("action", MODE_PRIVATE);
        sponsor = sp.getString("id", null);
        if(sponsor==null){
            sponsor=new NetBuilder().getMsgcode();
            sp.edit().putString("id",sponsor).apply();
        }
        initView();
        initData();
    }
        InputDialogListener Listener=new InputDialogListener() {
        @Override
        public void finish(String msg) {
            new MsgSendAsyncTask().execute(msg);
        }
        public void cancel(){
            add.setVisibility(View.VISIBLE);
            rsh.setVisibility(View.VISIBLE);
        }
    };
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mArrays = new ArrayList<BaseItem>();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new MyRAdapter();
        recyclerView.setAdapter(adapter);
        CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(recyclerView.getAdapter(), mArrays);
        cardCallback.setOnSwipedListener(new OnSwipeListener<Object>() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                MyRAdapter.MyViewHolder myHolder = (MyRAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.05f);
                if (direction == CardConfig.SWIPING_LEFT) {
                    myHolder.dislike.setAlpha(Math.abs(ratio));
                } else if (direction == CardConfig.SWIPING_RIGHT) {
                    myHolder.like.setAlpha(Math.abs(ratio));
                } else {
                    myHolder.dislike.setAlpha(0f);
                    myHolder.like.setAlpha(0f);
                }
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, Object o, int direction) {
                MyRAdapter.MyViewHolder myHolder = (MyRAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                myHolder.dislike.setAlpha(0f);
                myHolder.like.setAlpha(0f);
                ItemMood msg=(ItemMood)o;
                String dir;
                if(direction==CardConfig.SWIPED_LEFT){
                    dir="1";
                }else{
                    dir="0";
                }
                new LikeAsyncTask().execute(msg.getSponsor(), msg.getMsgCode(), dir);
            }
            @Override
            public void onSwipedClear() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                },0L);
            }
        });
        final ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        final CardLayoutManager cardLayoutManager = new CardLayoutManager(recyclerView, touchHelper);
        recyclerView.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void initData() {
        new MsgGetAsyncTask().execute();
    }
    private DisplayImageOptions getSimpleOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_photo_pesonal)
                .showImageForEmptyUri(R.drawable.image_photo_pesonal)
                .showImageOnFail(R.drawable.image_photo_pesonal)
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(false)// 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.NONE)// 设置图片以如何的编码方式显示
                .considerExifParams(true).bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
                .build();// 构建完成
    }
    public ImageLoader getDefaultImageLoader(Context context){
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader imageLoader=ImageLoader.getInstance();
        imageLoader.init(configuration);
        return imageLoader;
    }
    private class MyRAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            return new MyViewHolder(view);
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemMood msg=(ItemMood)mArrays.get(position);
            MyViewHolder myHolder=(MyViewHolder) holder;
            getDefaultImageLoader(MainActivity.this).displayImage(msg.getHead(), myHolder.head, getSimpleOptions());
            myHolder.nick.setText(msg.getNick());
            myHolder.msg.setText(msg.getMsg());
            myHolder.sex.setImageResource(msg.getSex());
            myHolder.time.setText(msg.getTime());
            myHolder.count.setText(msg.getAnum());
            myHolder.place.setText(msg.getPlace());
        }
        @Override
        public int getItemCount() {
            return mArrays.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView skin;
            ImageView like;
            ImageView dislike;
            CircleImageView head;
            TextView nick;
            TextView count;
            ImageView sex;
            TextView msg;
            TextView time;
            TextView place;
            MyViewHolder(View itemView) {
                super(itemView);
                head=(CircleImageView)itemView.findViewById(R.id.head);
                nick=(TextView)itemView.findViewById(R.id.nick);
                count=(TextView)itemView.findViewById(R.id.like_count);
                msg=(TextView)itemView.findViewById(R.id.msg);
                time=(TextView)itemView.findViewById(R.id.time);
                place=(TextView)itemView.findViewById(R.id.place);
                sex=(ImageView) itemView.findViewById(R.id.sex);
                skin = (ImageView) itemView.findViewById(R.id.bkg);
                like = (ImageView) itemView.findViewById(R.id.iv_like);
                dislike = (ImageView) itemView.findViewById(R.id.iv_dislike);
            }
        }
    }


    class MsgSendAsyncTask extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... msg) {
            SQLiteDatabase db=new DBHandler(MainActivity.this).getWritableDatabase();
            NetBuilder N=new NetBuilder();
            N.put("sponsor",sponsor).put("place",sp.getString("place","成都")).put("msg",msg[0]).put("msgcode2", N.getMsgcode());
            try {
                String Json=N.build();
                N.put("head",sp.getString("head",null)).put("sex",sp.getString("sex",null)).put("anum", "0").put("nick",sp.getString("nick",null));
                mArrays.add(0, new ItemMood(0, (HashMap) N.Package));
                Log.e("da",Json);
                String result= Net.request("http://kwall.cn/moodSend.php", Json);
                Log.e("e",result);
                NetBuilder Net=new NetBuilder(result);
                if(Net.getBool("flag",false)){
                    ListUtil.UpadteState(mArrays, Net.get("msgcode2"), Net.get("msgcode"));
                }
            }catch (Exception e){
                Log.e("error",e.toString());
            }
            publishProgress(0);
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }
    }
    class LikeAsyncTask extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            NetBuilder N=new NetBuilder();
            N.put("sponsor", sponsor).put("receiver",params[0]).put("msgcode",params[1]).put("agree", params[2]);
            try {
                Log.e("das",N.build());
                Net.request("http://kwall.cn/moodLike.php", N.build());
            }catch (Exception e){
                Log.e("e",e.toString());
            }
            return null;
        }
    }
    class MsgGetAsyncTask extends AsyncTask<Void, Integer, Void> {
        int SelectionTemp = 0;
        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase db=new DBHandler(MainActivity.this).getWritableDatabase();
            String result;
            NetBuilder N=new NetBuilder();
            N.add("sponsor", sponsor).add("sort","msgcode DESC").add("start",start);
            try {
                result= Net.request("http://kwall.cn/moodRsh.php", N.build());
                N=new NetBuilder(result);
                if(start==0&&N.ListSize()>0) {
                    mArrays.clear();
                    db.delete("mood", "1", null);
                }
                for (int i = 0; i < N.ListSize(); i++) {
                    if(start==0){
                        ContentValues cv=DBHandler.MapToContentValues((HashMap) N.getList(i));
                        db.insert("mood", null,cv);
                    }
                    mArrays.add(new ItemMood(0,(HashMap)N.getList(i)));
                }
            }catch (Exception e){
                Log.e("e",e.toString());
                publishProgress(-1);
            }
            SelectionTemp=N.ListSize();
            publishProgress(0);
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]==-1){
                Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
            }
            if(SelectionTemp==0){
                start=0;
            }else {
                start += SelectionTemp;
            }
            adapter.notifyDataSetChanged();
        }
    }
    @Override
         protected void onRestart() {
        super.onRestart();
        recyclerView.getAdapter().notifyDataSetChanged();
        System.out.println("-------------------onRestart:");
    }
}
