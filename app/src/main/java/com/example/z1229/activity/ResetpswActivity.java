package com.example.z1229.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.summerclient.R;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MaiBenBen on 2019/4/9.
 */

public class ResetpswActivity extends Activity implements View.OnClickListener {
    private EditText edit1;
    private EditText edit2;
    private EditText edit3;
    private Button btn;
    private ImageView discover;
    boolean visible=false;
    private String passStr=null;
    private String passCon=null;
    private TextView text;
    Intent i;
    Gson gson = new Gson();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpsw);
        initView();

        final Intent intent=new Intent(ResetpswActivity.this,ResetSucceed.class);
        intent.putExtra("type",i.getStringExtra("type"));


    }
    private void initView(){
        edit1=(EditText)findViewById(R.id.pass3_phone);
        i=getIntent();
        text=(TextView)findViewById(R.id.pass3_txt);
        text.setText(i.getStringExtra("type"));
        edit1.setText(i.getStringExtra("phone"));
        edit2=(EditText)findViewById(R.id.pass3_pass);
        edit3=(EditText)findViewById(R.id.pass3_passconfirm);
        btn=(Button)findViewById(R.id.pass3_nextstep);
        discover=(ImageView)findViewById(R.id.pass3_image);
        discover.setOnClickListener(this);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.pass3_image:
                if(!visible){
                    visible=true;
                    edit2.setInputType(128);//设置为显示密码
                }else{
                    visible=false;
                    edit2.setInputType(129);//设置为隐藏密码
                }
                break;
            case R.id.pass3_nextstep:
                passStr = edit2.getText().toString().trim();
                passCon=edit3.getText().toString().trim();
                Log.e("codeStr", passStr);
                if (null ==passStr || TextUtils.isEmpty(passStr)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    String REGEX_PASSWORD_SIMPLE =  "^[a-zA-Z0-9_]{8,16}$";//把正则表达式的规则编译成模板
                    Pattern pattern = Pattern.compile(REGEX_PASSWORD_SIMPLE);//把需要匹配的字符给模板匹配，获得匹配器
                    Matcher matcher = pattern.matcher(passStr);// 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
                    if(!matcher.matches())
                    {
                        Toast.makeText(this, "密码不符合格式", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (null ==passCon || TextUtils.isEmpty(passCon)) {
                    Toast.makeText(this, "请重新确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passStr!=null&&passCon!=null&&!(passStr.equals(passCon))){
                    Toast.makeText(this,"两次输入密码不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
            default:
                break;
        }
    }
}
