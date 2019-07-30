package com.example.z1229.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.bean.Message;
import com.example.z1229.bean.UserBean;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        mButton01=(ImageButton)findViewById(R.id.Button01);
        register =(TextView) findViewById(R.id.Button02);
        resetpsw =(TextView) findViewById(R.id.Button03);
        mEditText01=(EditText)findViewById(R.id.EditText01);
        mEditText02=(EditText)findViewById(R.id.EditText02);
        image = (ImageView)findViewById(R.id.pass3_image);


//        DBOpenHelper dbOpenHelper=new DBOpenHelper(LoadActivity.this);
//        final SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        final Intent intent = new Intent(this,SocketService.class);
        startService(intent);


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

        mButton01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//登录
                UserBean userBean = new UserBean();
                userBean.setType("登录");
                userBean.setPhonenum(Long.valueOf(mEditText01.getText().toString().trim()));
                userBean.setPassWord(mEditText02.getText().toString().trim());
                Message message = new Message("UserBean",gson.toJson(userBean));

                Intent intent = new Intent(LoadActivity.this,SocketService.class);
                intent.putExtra("message",gson.toJson(message));
                startService(intent);
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
