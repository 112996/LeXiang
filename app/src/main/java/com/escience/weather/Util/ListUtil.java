package com.escience.weather.Util;

import com.escience.weather.bean.BaseItem;
import com.escience.weather.bean.ItemMood;

import java.util.ArrayList;

/**
 * Created by Stark on 2017/4/21.
 */
public class ListUtil {
    public static ArrayList<BaseItem> UpadteState(ArrayList<BaseItem> mArrays,String msgCode,String newCode){
        for(int i=mArrays.size()-1;i>=0;i--){
            ItemMood temp=(ItemMood)mArrays.get(i);
            if(temp.getMsgCode2()!=null&&temp.getMsgCode2().equals(msgCode)){
                temp.setMsgCode(newCode);
                mArrays.set(i,temp);
            }
        }
        return mArrays;
    }
}
