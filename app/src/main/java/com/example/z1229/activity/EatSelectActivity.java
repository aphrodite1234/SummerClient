package com.example.z1229.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.adapter.EatAdapter;
import com.example.z1229.base.EatBase;
import com.example.z1229.base.ItemInfo;
import com.example.z1229.summerclient.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EatSelectActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    @BindView(R.id.btn_back)
    ImageButton back;
    @BindView(R.id.eat_select_save)
    ImageButton save;
    @BindView(R.id.eat_select_tab)
    TabLayout tab;
    @BindView(R.id.select_list)
    RecyclerView selectList;
    @BindView(R.id.eat_select_search)
    ImageButton search;

    TextView textCal;
    NumberPicker pickerK;
    NumberPicker pickerB;
    NumberPicker pickerS;
    NumberPicker pickerG;
    Button btnCancel;
    Button btnOk;

    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog;
    private View picker;
    private EatAdapter adapter;
    private ArrayList<ArrayList<ItemInfo>> arrayLists = new ArrayList<>
            (Arrays.asList((new EatBase()).ZS, (new EatBase()).RDN, (new EatBase()).SC,
                    (new EatBase()).SG, (new EatBase()).ZL, (new EatBase()).YLLS));
    private ArrayList<Integer[]> all = new ArrayList<>();//已选的食物
    private ItemInfo itemInfo;//正在选择的食物
    private int ke = 0;//选择的食物重量
    private int itemposition;

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_select);

        ButterKnife.bind(this);
        initDialog();
        initTab();
        initList(arrayLists.get(0));
    }

    private void initTab() {
        for (int i = 0; i < EatBase.TAB.length; i++) {
            tab.addTab(tab.newTab().setText(EatBase.TAB[i]));
        }
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        initList(arrayLists.get(0));
                        break;
                    case 1:
                        initList(arrayLists.get(1));
                        break;
                    case 2:
                        initList(arrayLists.get(2));
                        break;
                    case 3:
                        initList(arrayLists.get(3));
                        break;
                    case 4:
                        initList(arrayLists.get(4));
                        break;
                    case 5:
                        initList(arrayLists.get(5));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initList(ArrayList<ItemInfo> arrayList) {
        adapter = new EatAdapter(arrayList);
        selectList.setLayoutManager(layoutManager);
        selectList.setAdapter(adapter);
        adapter.setOnItemClickListener(new EatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                textCal.setText("0");
                pickerK.setValue(0);
                pickerB.setValue(0);
                pickerS.setValue(0);
                pickerG.setValue(0);
                itemposition = position;
                itemInfo = arrayLists.get(tab.getSelectedTabPosition()).get(position);
                alertDialog.show();
            }
        });
    }

    private void initDialog() {
        final LayoutInflater inflater = EatSelectActivity.this.getLayoutInflater();
        builder = new AlertDialog.Builder(EatSelectActivity.this);
        picker = inflater.inflate(R.layout.dialog_eat_select, null, false);
        textCal = picker.findViewById(R.id.eat_select_dialog_cal);
        TextView point = picker.findViewById(R.id.dialog_eat_point);
        point.setVisibility(View.GONE);
        pickerG = picker.findViewById(R.id.eat_select_g);
        pickerS = picker.findViewById(R.id.eat_select_s);
        pickerB = picker.findViewById(R.id.eat_select_b);
        pickerK = picker.findViewById(R.id.eat_select_k);
        btnCancel = picker.findViewById(R.id.eat_select_dialog_cancel);
        btnOk = picker.findViewById(R.id.eat_select_dialog_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemInfo.setKe(ke);
                adapter.updateItem(itemposition, itemInfo);
                if(ke > 0){
                    Integer[] xy = new Integer[3];
                    xy[0] = tab.getSelectedTabPosition();
                    xy[1] = itemposition;
                    xy[2] = ke;
                    all.add(xy);
                }else {
                    int i=0;
                    for (Integer[] x:all){
                        if(x[0] == tab.getSelectedTabPosition() && x[1] == itemposition){
                            all.remove(i);
                            break;
                        }else {
                            i++;
                        }
                    }
                }
                ke=0;
                alertDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        builder.setView(picker);
        builder.setCancelable(false);
        alertDialog = builder.create();
        initPicker(pickerK);
        initPicker(pickerB);
        initPicker(pickerS);
        initPicker(pickerG);
    }

    private void initPicker(NumberPicker numberPicker) {
        numberPicker.setOnValueChangedListener(this);
        numberPicker.setMaxValue(9);
        numberPicker.setMinValue(0);
    }

    @OnClick({R.id.btn_back, R.id.eat_select_save, R.id.eat_select_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.eat_select_save:
                Intent intent1 = new Intent(this, EatAddActivity.class);
                intent1.putExtra("all", (Serializable) all);
                setResult(RESULT_OK,intent1);
                finish();
                break;
            case R.id.eat_select_search:
                Intent intent = new Intent(this, EatSearchActivity.class);
                intent.putExtra("array", (Serializable) arrayLists);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Integer[] xy = (Integer[]) data.getSerializableExtra("xy");
            tab.getTabAt(xy[0]).select();
            layoutManager.scrollToPosition(xy[1]);
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        ke = pickerK.getValue()*1000+pickerB.getValue()*100+pickerS.getValue()*10+pickerG.getValue();
        textCal.setText(ke*itemInfo.getCal()/100+"");
    }
}
