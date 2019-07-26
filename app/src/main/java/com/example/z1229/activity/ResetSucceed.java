package com.example.z1229.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.z1229.summerclient.R;

/**
 * Created by MaiBenBen on 2019/4/12.
 */

public class ResetSucceed extends Activity {
    private ImageView image;
    private Button button;
    private TextView txt;
    private Intent i;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetsucceed);
        image=(ImageView)findViewById(R.id.pass4_image);
        image.setImageResource(R.drawable.ic_duihao);
        button=(Button)findViewById(R.id.pass4_back);
        i=getIntent();
        txt=(TextView)findViewById(R.id.pass4_txt1);
        txt.setText(i.getStringExtra("type")+"成功");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ResetSucceed.this, LoadActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
