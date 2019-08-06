package com.example.z1229.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.summerclient.R;
import com.google.gson.Gson;

/**
 * Created by MaiBenBen on 2019/4/7.
 */

public class LoadActivity extends Activity {

    private ImageButton mButton01=null;
    private TextView register =null;
    private TextView resetpsw =null;
    private EditText mEditText01=null;
    private EditText mEditText02=null;
    private ImageView image=null;
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
                Intent intent = new Intent(LoadActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(LoadActivity.this,"点击登陆按钮成功",Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){//注册
                Intent intent=new Intent(LoadActivity.this,SmsActivity.class);
                intent.putExtra("type","用户注册");
                startActivity(intent);
                finish();
            }
        });

        resetpsw.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){//找回密码
                Intent intent=new Intent(LoadActivity.this, SmsActivity.class);
                intent.putExtra("type","找回密码");
                startActivity(intent);
                finish();
            }
        });
    }
}
