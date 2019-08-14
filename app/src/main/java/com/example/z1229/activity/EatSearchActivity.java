package com.example.z1229.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.z1229.adapter.EatAdapter;
import com.example.z1229.base.ItemInfo;
import com.example.z1229.summerclient.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EatSearchActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageButton back;
    @BindView(R.id.eat_select_s)
    Button search;
    @BindView(R.id.select_list)
    RecyclerView selectList;
    @BindView(R.id.search_edit)
    EditText searchEdit;

    private ArrayList<ArrayList<ItemInfo>> arrayLists;
    private ArrayList<Integer[]> xylist = new ArrayList<>();
    EatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_search);
        ButterKnife.bind(this);
        initList(new ArrayList<ItemInfo>());
        arrayLists = (ArrayList<ArrayList<ItemInfo>>)getIntent().getSerializableExtra("array");
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = searchEdit.getText().toString().trim();
                if (string.length()>0){
                    searchinfo(string);
                }else {
                    adapter.clear();
                }
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.eat_select_s})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.eat_select_s:
                String string = searchEdit.getText().toString().trim();
                if (string.length()>0){
                    searchinfo(string);
                }else {
                    adapter.clear();
                }
                break;
        }
    }
    private void initList(ArrayList<ItemInfo> arrayList) {
        adapter = new EatAdapter(arrayList);
        selectList.setLayoutManager(new LinearLayoutManager(this));
        selectList.setAdapter(adapter);
        adapter.setOnItemClickListener(new EatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(EatSearchActivity.this,EatSelectActivity.class);
                intent.putExtra("xy",(Serializable)xylist.get(position));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    private ArrayList<ItemInfo> searchinfo(String string){
        ArrayList<ItemInfo> arrayList = new ArrayList<>();
        int x= 0,y=0;
        xylist.clear();
        for (ArrayList<ItemInfo> arrayList1:arrayLists){
            y=0;
            for (ItemInfo itemInfo:arrayList1){
                String name = itemInfo.getName();
                if(name.contains(string)){
                    Integer[] xy = new Integer[2];
                    xy[0] = x;
                    xy[1] = y;
                    xylist.add(xy);
                    itemInfo.setKe(-1);
                    adapter.addData(itemInfo,string);
                }
                y++;
            }
            x++;
        }
        return arrayList;
    }
}
