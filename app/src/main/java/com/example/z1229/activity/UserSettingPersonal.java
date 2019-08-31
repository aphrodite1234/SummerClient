package com.example.z1229.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.base.ActionSheetDialog;
import com.example.z1229.bean.UserBean;
import com.example.z1229.summerclient.R;
import com.example.z1229.utils.ACache;
import com.example.z1229.utils.PermissionActivity;
import com.example.z1229.utils.PhotoUtils;
import com.example.z1229.utils.PictureUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;


/**
 * Created by MaiBenBen on 2019/8/28.
 */

public class UserSettingPersonal extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.user_setting_personal_info_head)
    LinearLayout Linear_head;
    @BindView(R.id.user_setting_personal_info_head_iv)
    RoundedImageView iv_head;
    @BindView(R.id.user_setting_personal_info_username)
    LinearLayout linear_username;
    @BindView(R.id.user_setting_personal_info_username_tv)
    TextView tv_username;
    @BindView(R.id.user_setting_personal_info_sign)
    LinearLayout linear_sign;
    @BindView(R.id.user_setting_personal_info_sign_tv)
    TextView tv_sign;
    @BindView(R.id.user_setting_personal_info_address)
    LinearLayout linear_address;
    @BindView(R.id.user_setting_personal_info_address_tv)
    TextView tv_address;
    @BindView(R.id.user_setting_personal_info_phonenum)
    LinearLayout linear_phonenum;
    @BindView(R.id.user_setting_personal_info_phonenum_tv)
    TextView tv_phonenum;
    @BindView(R.id.user_setting_personal_info_back)
    ImageView iv_back;

    private static int REQUEST_CODE_CAMERA = 2060;
    private static int REQUEST_CODE_ALBUM = 1030;
    private File tempFile,tempFile1,tempSavedHead;
    private ACache mACache;
    private File fileDir;
    //private String PhotoUri= String.format("%s123.png", getContext().getExternalCacheDir().getPath());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_setting_personal);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void takePhoto(){
        PhotoUtils.showCamera(this, tempFile, REQUEST_CODE_CAMERA);
    }
    public void initView(){
        Linear_head.setOnClickListener(this);
        linear_sign.setOnClickListener(this);
        linear_username.setOnClickListener(this);
        linear_address.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_phonenum.setText("");
    }

    public void initData(){
        //在6.0的华为手机上，使用 getFilesDir() 铁定失败
        //在8.0的小米6上，使用Environment.getExternalStorageDirectory()也同样失败
        fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
        fileDir = new File(fileDir, "photo");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        tempFile = new File(fileDir, "head");
        tempFile1 = new File(fileDir, "headCrop");
        tempSavedHead=new File(fileDir,"saved_head");

        mACache = ACache.get(this);
        UserBean userBean=(UserBean)mACache.getAsObject("local_userBean");
        if(userBean.getPhoto()==null) iv_head.setImageResource(R.mipmap.ic_home_social_man);
        else iv_head.setImageURI(Uri.parse(PictureUtils.byte2file(userBean.getPhoto(),PictureUtils.getRealFilePath(this,Uri.fromFile(tempSavedHead)))));
        tv_username.setText(userBean.getUserName());
        tv_phonenum.setText(String.valueOf(userBean.getPhonenum()));
        tv_address.setText(userBean.getLocation());
        tv_sign.setText(userBean.getSignature());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_setting_personal_info_head:
                new ActionSheetDialog(UserSettingPersonal.this)
                        .builder()
                        .setTitle("选择修改头像")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("拍照修改头像", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener()
                                {
                                    @Override
                                    public void onClick(int which)
                                    {
                                        //PhotoUtils.takePicture(this, Uri.fromFile(new File(PhotoUri)),1);
                                        //检测是否有相机权限，读写权限
                                        if (ContextCompat.checkSelfPermission(UserSettingPersonal.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                                ContextCompat.checkSelfPermission(UserSettingPersonal.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                                ContextCompat.checkSelfPermission(UserSettingPersonal.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            startActivityForResult(new Intent(UserSettingPersonal.this, PermissionActivity.class).putExtra(PermissionActivity.KEY_PERMISSIONS_ARRAY,
                                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}), PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE);
                                        } else
                                            takePhoto();
                                    }
                                })
                        .addSheetItem("相册修改头像", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener()
                                {
                                    @Override
                                    public void onClick(int which)
                                    {
                                        if (ContextCompat.checkSelfPermission(UserSettingPersonal.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                                ContextCompat.checkSelfPermission(UserSettingPersonal.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            startActivityForResult(new Intent(UserSettingPersonal.this, PermissionActivity.class).putExtra(PermissionActivity.KEY_PERMISSIONS_ARRAY,
                                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}), PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE);
                                        }else
                                            PhotoUtils.openPic(UserSettingPersonal.this,REQUEST_CODE_ALBUM);
                                    }
                                }).show();
                break;
            case R.id.user_setting_personal_info_username:
                username_edit();
                break;
            case R.id.user_setting_personal_info_sign:
                sign_edit();
                break;
            case R.id.user_setting_personal_info_address:
                onAddressPicker();
                break;
            case R.id.user_setting_personal_info_back:
                finish();
                break;
        }
    }

    public void username_edit(){
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入用户名")
                //.setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        //Toast.makeText(getApplicationContext(), et.getText().toString(),Toast.LENGTH_LONG).show();
                        tv_username.setText(et.getText());
                    }
                }).setNegativeButton("取消",null).show();
    }
    public void sign_edit(){
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入签名")
                //.setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        //Toast.makeText(getApplicationContext(), et.getText().toString(),Toast.LENGTH_LONG).show();
                        tv_sign.setText(et.getText());
                    }
                }).setNegativeButton("取消",null).show();
    }

    public void onAddressPicker() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                Toast.makeText(UserSettingPersonal.this, "数据初始化失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    //Toast.makeText(UserSettingPersonal.this, province.getAreaName() + city.getAreaName(), Toast.LENGTH_SHORT).show();
                    tv_address.setText(String.format("%s%s", province.getAreaName(), city.getAreaName()));
                } else {
                    //Toast.makeText(UserSettingPersonal.this, province.getAreaName() + city.getAreaName() + county.getAreaName(), Toast.LENGTH_SHORT).show();
                    tv_address.setText(String.format("%s%s%s", province.getAreaName(), city.getAreaName(), county.getAreaName()));
                }
            }
        });
        task.execute("河南", "开封", "金明");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE){
            switch (resultCode){
                case PermissionActivity.CALL_BACK_RESULT_CODE_SUCCESS:
                    Toast.makeText(this,"权限申请成功！",Toast.LENGTH_SHORT).show();
                    takePhoto();
                    break;
                case PermissionActivity.CALL_BACK_RESULE_CODE_FAILURE:
                    Toast.makeText(this,"权限申请失败！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Log.i("head", tempFile.getAbsolutePath());
            //Toast.makeText(this, String.format("保存路径为：%s",tempFile.getAbsolutePath()), Toast.LENGTH_SHORT).show();
            //PhotoUtils.cropImageUri(UserSettingPersonal.this,tempFile,tempFile1, 1, 1, 400, 400, 10086);
            //iv_head.setImageBitmap(PhotoUtils.getBitmapFromUri(Uri.fromFile(tempFile),UserSettingPersonal.this));
            Uri uri=Uri.fromFile(tempFile);
            FileOutputStream out = null;
            try {
                iv_head.setImageBitmap(getBitmapFormUri(uri));
                out = new FileOutputStream(tempFile1);
                getBitmapFormUri(uri).compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert out != null;
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(UserSettingPersonal.this, "保存已经至" + Uri.fromFile(tempFile1), Toast.LENGTH_SHORT).show();
        }
        if(requestCode==10086&&resultCode==Activity.RESULT_OK){
            Log.i("headCrop","headCrop裁剪成功.可是实际上该路径并没有裁剪图像");
        }
        if(requestCode == REQUEST_CODE_ALBUM && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            FileOutputStream out = null;
            try {
                iv_head.setImageBitmap(getBitmapFormUri(uri));
                out = new FileOutputStream(tempFile1);
                getBitmapFormUri(uri).compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert out != null;
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(UserSettingPersonal.this, "保存已经至" + Uri.fromFile(tempFile1), Toast.LENGTH_SHORT).show();
        }
    }
    public Bitmap getBitmapFormUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);

        //这一段代码是不加载文件到内存中也得到bitmap的真是宽高，主要是设置inJustDecodeBounds为true
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;//不加载到内存
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        assert input != null;
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;

        //图片分辨率以480x800为标准
        float hh = 400f;//这里设置高度为800f
        float ww = 400f;//这里设置宽度为480f
        //缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        input = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        assert input != null;
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }
    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options<=0)
                break;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        UserBean userBean=(UserBean) mACache.getAsObject("local_userBean");
        userBean.setPhoto(PictureUtils.file2byte(PictureUtils.getRealFilePath(this,Uri.fromFile(tempFile1))));
        Log.d("photo", Arrays.toString(userBean.getPhoto()));
        userBean.setUserName(tv_username.getText().toString());
        userBean.setLocation(tv_address.getText().toString());
        userBean.setSignature(tv_sign.getText().toString());
        mACache.put("local_userBean",userBean);
    }
}
