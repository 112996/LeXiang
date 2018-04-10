package com.escience.weather.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.escience.weather.Listener.CityDialogListener;
import com.escience.weather.R;

import java.lang.reflect.Field;

/**
 * Created by Stark on 2017/11/27.
 */
public class CityDialog extends Dialog {

    String P;
    String C;
    ListView listView2;
    private boolean isAllowClose = true;
    private CityDialogListener Listener;
    public CityDialog(Context context,CityDialogListener  listener) {
        this(context, R.style.NoFrameNoDim_Dialog_Dark, listener);
    }
    public CityDialog(final Context context, int theme, final CityDialogListener listener) {
        super(context, theme);
        Listener=listener;
        setContentView(R.layout.dialog_listview);
        final String[] data = context.getResources().getStringArray(R.array.省份);
        String[] data1=context.getResources().getStringArray(getResource(data[0]));
        P=data[0];
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, data);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, data1);
        ListView listView = (ListView) findViewById(R.id.list1);
        listView2 = (ListView) findViewById(R.id.list2);
        listView.setAdapter(adapter);
        listView2.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                P = data[position];
                listView2.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(getResource(P))));
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                C = context.getResources().getStringArray(getResource(P))[position];
                listener.finish(P,C);
                CityDialog.this.cancel();
            }
        });
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity( Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width= WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
    }
    @Override
    public void cancel() {
        super.cancel();
        Listener.cancel();
    }
    public CityDialog setOnKeyDownClose(boolean isAllowClose) {
        this.isAllowClose = isAllowClose;
        return this;
    }
    public CityDialog setOnTouchClose(boolean temp)
    {
        setCanceledOnTouchOutside(temp);
        return this;
    }
    public int  getResource(String imageName){
        Class array = R.array.class;
        try {
            Field field = array.getField(imageName);
            int resId = field.getInt(imageName);
            return resId;
        } catch (NoSuchFieldException e) {//如果没有在"mipmap"下找到imageName,将会返回0
            return 0;
        } catch (IllegalAccessException e) {
            return 0;
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isAllowClose) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
