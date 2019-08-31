package com.example.z1229.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1229.adapter.RViewHolder;
import com.example.z1229.adapter.RecycleViewAdapter;
import com.example.z1229.summerclient.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by MaiBenBen on 2019/8/25.
 */

public class UserSpaceFollow extends Fragment {

    @BindView(R.id.fragment_user_tab_recycler)
    RecyclerView rm;
    LinearLayoutManager lm;
    private View view;
    private RecycleViewAdapter ra;
    private List<String> follow;
    private Handler mHandler;
    private boolean hasmore;
    private int res;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_user_tab, container, false);
        return view;
    }

    public void initData(){
        follow=new ArrayList<>();
        //从数据库查询关注

        for(int i=0;i<30;i++){
            String username="用户"+i;
            follow.add(username);
        }
    }

    public void initRecyclerView(){
        lm=new LinearLayoutManager(getActivity());
        rm.setLayoutManager(lm);
        ra=new RecycleViewAdapter<String>(getActivity(),follow) {
            @Override
            public int setItemLayoutId(int position) {
                return R.layout.item_fragment_user_tab1;
            }
            @Override
            public void bindView(final RViewHolder holder, int position) {
                if(position<follow.size()-1){
                    //holder.setImageResource(R.id.item_fragment_user_tab1_head,R.mipmap.ic_home_social_man);
                    holder.setText(R.id.item_fragment_user_tab1_username,follow.get(position));

                    holder.getImageView(R.id.item_fragment_user_tab1_head).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(),"点击用户头像",Toast.LENGTH_SHORT).show();
                        }
                    });

                    holder.getImageView(R.id.item_fragment_user_tab1_follow).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "点击关注", Toast.LENGTH_SHORT).show();
                            holder.setImageResource(R.id.item_fragment_user_tab1_follow,R.mipmap.item_fragment_user_followed);
                        }
                    });
                }
                rm.setOnScrollListener(new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if(newState==RecyclerView.SCROLL_STATE_DRAGGING||newState==RecyclerView.SCROLL_STATE_SETTLING){
                            //Glide.with(getContext()).pauseRequests();
                        }else if(newState==RecyclerView.SCROLL_STATE_IDLE){
                            if(lm.findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1
                                    && recyclerView.getChildCount()> 0){
                                res=follow.size();
                                updateRecycler();
                                if(hasmore){
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            lm.findViewByPosition(res-1).setVisibility(View.GONE);
                                        }
                                    },350);
                                    ra.notifyDataSetChanged();
                                }else {
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            LinearLayout layout=(LinearLayout)lm.findViewByPosition(res-1);
                                            layout.findViewById(R.id.recycler_view_footer_iv).setVisibility(View.GONE);
                                            TextView tv=(TextView)layout.findViewById(R.id.recycler_view_footer_tv);
                                            tv.setText("到达尽头...");
                                        }
                                    },350);
                                }
                            }
                            //Glide.with(getContext()).resumeRequests();
                        }
                    }
                });
                rm.setAdapter(ra);
                rm.setItemAnimator(new DefaultItemAnimator());
            }
        };
    }

    private void updateRecycler(){
        List<String> res=follow;
        //查询数据库follow是否更新
        hasmore = res != follow;//如果相等hasmore=false，否则=true
    }

    @Override
    public void onDestroy(){
        //activity销毁时移除handler防止内存泄漏
        if(mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
