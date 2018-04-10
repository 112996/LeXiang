package com.escience.weather;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.escience.weather.Network.MD5;
import com.escience.weather.Network.Net;
import com.escience.weather.Network.NetBuilder;
import com.escience.weather.Util.FileType;
import com.escience.weather.Util.FileUtil;
import com.escience.weather.Util.ImgStorage;
import com.escience.weather.Util.Status;
import com.escience.weather.View.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;

public class UserActivity extends Activity {

    private CircleImageView head;
    private Button back;
    private Button ensure;
    private ImageButton boy;
    private ImageButton girl;
    private EditText name;
    private String sex;
    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    private static int CROP_REQUEST_CODE = 3;
    private static int MY_PERMISSIONS_REQUEST_CAMERA_GALLERY = 4;
    private static int GOTO_APPSETTING = 5;
    private String outPath;
    private String creamPath;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Status.setTranslucentStatus(getWindow());
        back=(Button)findViewById(R.id.dialog_title_back);
        head=(CircleImageView)findViewById(R.id.dialog_head);
        boy=(ImageButton)findViewById(R.id.dialog_boy);
        girl=(ImageButton)findViewById(R.id.dialog_girl);
        name=(EditText)findViewById(R.id.dialog_name);
        ensure=(Button)findViewById(R.id.dialog_ensure);
        outPath=FileUtil.getPath(FileType.ImgTemp) + "/poly.png";
        creamPath=FileUtil.getPath(FileType.ImgTemp) + "/crm.png";
        sp=getSharedPreferences("action", Context.MODE_PRIVATE);
        sex=sp.getString("sex", "1");
        name.setText(sp.getString("nick",sp.getString("city","成都")+"用户"));
        name.setSelection(name.getText().length());
        setSex();
        getDefaultImageLoader(UserActivity.this).displayImage("http://kwall.cn/weather/head/"+sp.getString("head",null),head, getSimpleOptions());
        head.setOnClickListener(click);
        back.setOnClickListener(click);
        boy.setOnClickListener(click);
        girl.setOnClickListener(click);
        ensure.setOnClickListener(click);
    }
    View.OnClickListener click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.dialog_title_back:
                    finish();
                    break;
                case R.id.dialog_boy:
                    sex="1";
                    setSex();
                    break;
                case R.id.dialog_girl:
                    sex="0";
                    setSex();
                    break;
                case R.id.dialog_ensure:
                    sp.edit().putString("nick",name.getText()==null?null:name.getText().toString()).putString("sex", sex + "").apply();
                    new UserAsyncTask().execute();
                    break;
                case R.id.dialog_head:
                    showChoosePicDialog();
                    break;
            }
        }
    };
    void setSex(){
        if(sex.equals("1")) {
            boy.setImageResource(R.drawable.image_boy_click);
            girl.setImageResource(R.drawable.image_girl);
        }
        else{
            boy.setImageResource(R.drawable.image_boy);
            girl.setImageResource(R.drawable.image_girl_click);
        }
    }
    public void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"拍照", "选择本地照片"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Log.e("拍照", "选择本地照片");
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&& ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_GALLERY);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.PathToUri(creamPath));
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        }
                        break;
                    case 1:
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA_GALLERY);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, GALLERY_REQUEST_CODE);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Uri in = Uri.fromFile(new File(creamPath));
                Uri out = Uri.fromFile(new File(outPath));
                startActivityForResult(ImgStorage.getCropIntent(in, out), CROP_REQUEST_CODE);
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                startActivityForResult(ImgStorage.getCropIntent(Uri.fromFile(new File(FileUtil.getPhotoPathFromContentUri(this, data.getData()))), Uri.fromFile(new File(outPath))), CROP_REQUEST_CODE);
            } else if (requestCode == CROP_REQUEST_CODE) {
                FileAsyncTask fileAsyncTask = new FileAsyncTask();
                fileAsyncTask.execute(outPath,FileUtil.getPath(FileType.Head));

            } else if (requestCode == GOTO_APPSETTING) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        PermissionRequest();
                    } else {
                        Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    private void PermissionRequest() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("权限不可用")
                .setMessage("请在-应用设置-权限中，获取权限。")
                .setPositiveButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, GOTO_APPSETTING);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setCancelable(false).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, java.lang.String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA_GALLERY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        PermissionRequest();
                    } else {
                        finish();
                    }
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    class FileAsyncTask extends AsyncTask<String, Integer, Void> {
        String md5;
        @Override
        protected java.lang.Void doInBackground(final String... values) {
            File file=new File(values[0]);
            md5= MD5.get(file);
            try{
                if(Net.UpLoad("http://kwall.cn/UploadFile.php",file)){
                    publishProgress(0);
                }else{
                    publishProgress(1);
                }
            }catch (Exception e){
                publishProgress(-1);
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(java.lang.Integer... values) {
            super.onProgressUpdate(values);
            String temp=null;
            switch (values[0]){
                case -1:
                    temp="网络无连接";
                    break;
                case 0:
                    sp.edit().putString("head",md5).apply();
                    getDefaultImageLoader(UserActivity.this).displayImage("http://kwall.cn/weather/head/"+md5,head, getSimpleOptions());
                    temp="上传成功";
                    break;
                case 1:
                    temp="上传失败";
                    break;
            }
            Toast.makeText(UserActivity.this,temp,Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileType(String fileName) {
        // TODO Auto-generated method stub
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }
    class UserAsyncTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String result;
            NetBuilder N=new NetBuilder();
            SharedPreferences sp=getSharedPreferences("action",MODE_PRIVATE);
            N.put("id", sp.getString("id",null)).put("head",sp.getString("head",null)).put("nick",sp.getString("nick",sp.getString("city","成都")+"用户")).put("sex", sp.getString("sex", "0"));
            try {
                result= Net.request("http://kwall.cn/moodChange.php", N.build());
                Log.e("re",N.build());
                N=new NetBuilder(result);
                if (N.getBool("flag",false)){
                    publishProgress(0);
                }else{
                    publishProgress(1);
                }
            }catch (Exception e){
                Log.e("e",e.toString());
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            finish();
        }
    }
}
