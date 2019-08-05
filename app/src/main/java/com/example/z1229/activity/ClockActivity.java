package com.example.z1229.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.z1229.adapter.ClockAdapter;
import com.example.z1229.summerclient.R;

import java.util.LinkedList;

public class ClockActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    private ImageButton back,plus;
    private ListView listView;
    private static final int REQUEST_CLOCK_ADD = 1;
    private static final int REQUEST_CLOCK_ITEM = 2;
    private static final int INTENT_CLOCK_ADD = 1;
    private static final int INTENT_CLOCK_ITEM = 2;
    private static final int INTENT_CLOCK_DELETE = 3;
    private ClockAdapter clockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        initview();
    }

    private void initview(){
        back = (ImageButton)findViewById(R.id.btn_back);
        plus = (ImageButton)findViewById(R.id.btn_clock_plus);
        listView = (ListView)findViewById(R.id.clock_list);
        back.setOnClickListener(this);
        plus.setOnClickListener(this);
        LinkedList<Bundle> linkedList = new LinkedList<>();
        clockAdapter = new ClockAdapter(ClockActivity.this,linkedList);
        listView.setAdapter(clockAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_clock_plus:
//                Intent intent = new Intent(ClockActivity.this,ClockAddActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("type",INTENT_CLOCK_ADD);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, REQUEST_CLOCK_ADD);
                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_SKIP_UI, false);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent,REQUEST_CLOCK_ADD);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ClockActivity.this,ClockAddActivity.class);
        Bundle bundle = clockAdapter.getdata(position);
        bundle.putInt("type",INTENT_CLOCK_ITEM);
        bundle.putInt("position",position);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CLOCK_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode){
            switch (requestCode){
                case REQUEST_CLOCK_ADD:
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("h",data.getIntExtra(AlarmClock.EXTRA_HOUR,-1));
                    bundle1.putInt("m",data.getIntExtra(AlarmClock.EXTRA_MINUTES,-1));
                    bundle1.putString("remark",data.getStringExtra(AlarmClock.EXTRA_MESSAGE));
                    bundle1.putString("repeat",data.getStringExtra("123456"));
                    clockAdapter.add(bundle1);
                    break;
                case REQUEST_CLOCK_ITEM:
                    Bundle bundle = data.getExtras();
                    int type = bundle.getInt("type");
                    int position = bundle.getInt("position");
                    if(type == INTENT_CLOCK_DELETE){
                        clockAdapter.delete(position);
                    }else {
                        clockAdapter.update(position,bundle);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
