package com.example.z1229.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.base.DBOpenHelper;
import com.example.z1229.bean.Message;
import com.example.z1229.service.SocketService;
import com.example.z1229.summerclient.R;
import com.example.z1229.utils.SPUtils;
import com.google.gson.Gson;

/**
 * Created by MaiBenBen on 2019/4/7.
 */

public class LoadActivity extends BaseActivity {

    private ImageButton mButton01=null;
    private TextView register =null;
    private TextView resetpsw =null;
    private EditText mEditText01=null;
    private EditText mEditText02=null;
    private ImageView image=null;
    private String islogin;
    Gson gson = new Gson();
    boolean visible=false;
    private Intent service;
    private DBOpenHelper dbOpenHelper;
    public static SocketService.MyBinder SOCKET_BINDER;
    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SOCKET_BINDER =(SocketService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        service = new Intent(LoadActivity.this,SocketService.class);
        startService(service);

        //绑定socketservice,获取binder对象
        bindService(service,serviceConnection,BIND_AUTO_CREATE);
        initView();
    }

    private void initView(){
        mButton01=(ImageButton)findViewById(R.id.Button01);
        register =(TextView) findViewById(R.id.Button02);
        resetpsw =(TextView) findViewById(R.id.Button03);
        mEditText01=(EditText)findViewById(R.id.EditText01);
        mEditText02=(EditText)findViewById(R.id.EditText02);
        image = (ImageView)findViewById(R.id.pass3_image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!visible){
                    visible=true;
                    mEditText02.setInputType(128);//设置为显示密码
                }else{
                    visible=false;
                    mEditText02.setInputType(129);//设置为隐藏密码
                }
            }
        });

        //登录
        mButton01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String phone = mEditText01.getText().toString().trim();
//                String password = mEditText02.getText().toString().trim();
//                UserBean userBean = new UserBean();
//                if(TextUtils.isEmpty(phone)){
//                    Toast.makeText(LoadActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
//                }else if(TextUtils.isEmpty(password)){
//                    Toast.makeText(LoadActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
//                }else {
//                    userBean.setType("登录");
//                    userBean.setPhonenum(phone);
//                    userBean.setPassWord(password);
//                    Message message = new Message("UserBean",gson.toJson(userBean));
//                    service.putExtra("message",gson.toJson(message));
//                    startService(service);
//                }
                startActivity(new Intent(LoadActivity.this,MainActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){//注册
                Intent intent=new Intent(LoadActivity.this,SmsActivity.class);
                intent.putExtra("type","注册");
                startActivity(intent);
                finish();
            }
        });

        resetpsw.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){//找回密码
                Intent intent=new Intent(LoadActivity.this, SmsActivity.class);
                intent.putExtra("type","重置密码");
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(SPUtils.contains(this,"phone")){
            mEditText01.setText(SPUtils.get(this,"phone"));
        }
        if(SPUtils.contains(this,"phone")) {
            mEditText02.setText(SPUtils.get(this, "password"));
        }
        if (SPUtils.contains(this,"islogin")){
            islogin=SPUtils.get(this,"islogin");
            if(islogin.equals("true")){
                Intent intent = new Intent(LoadActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    protected void doAction(String content) {
        Message message = gson.fromJson(content,Message.class);
        if(message.getType().equals("登录")&&message.getContent().equals("true")){
            SPUtils.put(this,"phone",mEditText01.getText().toString());
            SPUtils.put(this,"password",mEditText02.getText().toString());
            SPUtils.put(this,"islogin","true");
            Intent intent = new Intent(LoadActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(LoadActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
        }
    }
}
