package com.example.z1229.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.bean.Message;
import com.example.z1229.bean.UserBean;
import com.example.z1229.service.SocketService;
import com.example.z1229.summerclient.R;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MaiBenBen on 2019/4/9.
 */

public class ResetpswActivity extends BaseActivity implements View.OnClickListener {
    private EditText edit1;
    private EditText edit2;
    private EditText edit3;
    private Button btn;
    private ImageView discover;
    boolean visible=false;
    private String passStr=null;
    private String passCon=null;
    private TextView text;
    Intent i,intent;
    Gson gson = new Gson();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpsw);

        i=getIntent();
        initView();
        intent=new Intent(ResetpswActivity.this,LoadActivity.class);
        intent.putExtra("type",i.getStringExtra("type"));
    }
    private void initView(){
        edit1=(EditText)findViewById(R.id.pass3_phone);
        text=(TextView)findViewById(R.id.pass3_txt);
        text.setText(i.getStringExtra("type"));
        edit1.setText(i.getStringExtra("phone"));
        edit1.setFocusable(false);
        edit1.setFocusableInTouchMode(false);
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
                    edit3.setInputType(128);
                }else{
                    visible=false;
                    edit2.setInputType(129);//设置为隐藏密码
                    edit3.setInputType(129);
                }
                break;
            case R.id.pass3_nextstep:
                passStr = edit2.getText().toString().trim();
                passCon=edit3.getText().toString().trim();
                if (TextUtils.isEmpty(passStr)) {
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
                    Toast.makeText(this, "请确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passStr!=null&&passCon!=null&&!(passStr.equals(passCon))){
                    Toast.makeText(this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                }else {
                    UserBean userBean = new UserBean();
                    userBean.setPhonenum(i.getStringExtra("phone"));
                    userBean.setType(i.getStringExtra("type"));
                    userBean.setPassWord(passStr);
                    Message message = new Message("UserBean",gson.toJson(userBean));
                    Intent intent  = new Intent(ResetpswActivity.this,SocketService.class);
                    intent.putExtra("message",gson.toJson(message));
                    startService(intent);
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void doAction(String content) {
        Message message = gson.fromJson(content,Message.class);
        if(message.getType().equals("注册")){
            if(message.getContent().equals("true")){
                Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(ResetpswActivity.this,message.getContent(),Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        }else if(message.getType().equals("重置密码")){
            if(message.getContent().equals("true")){
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(ResetpswActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                edit3.setText("");
                edit2.setText("");
            }
        }
    }
}
