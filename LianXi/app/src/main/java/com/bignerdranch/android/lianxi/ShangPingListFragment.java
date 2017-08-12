package com.bignerdranch.android.lianxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/8/12.
 */

public class ShangPingListFragment extends Fragment {

    private RecyclerView mShangPingRecyclerView;

    private ShangPingAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_shangping_list,container,false);

        mShangPingRecyclerView = (RecyclerView) view.findViewById(R.id.shangping_recycler_view);
        mShangPingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }


    /*
    *
    * 托管Activity恢复运行后,操作系统会发出调用onResume(()生命周期方法的指令。activity接到指令后
    * 它的FragmentManager会调用当前被Activity托管的fragment的onResume()方法
    *
    * 一般来说,要保证fragment视图得到刷新,在onResume()方法内更新代码是最安全的
    *
    * */
    @Override
    public void onResume(){
        super.onResume();
        // 刷新数据
        updateUI();
    }

    /*
    *
    * 创建ShangpingAdapter,然后设置给RecyclerView
    *
    * 如果已经配置好了ShangpingAdapter,就会调用notifyDataSetChanged()方法来修改updateUI()方法
    *
    *  */
    private void updateUI(){
        ShangPingS shangpings = ShangPingS.get(getActivity());
        List<ShangPing> sshangpings = shangpings.getShangpingS();
        if(mAdapter == null){
            mAdapter = new ShangPingAdapter(sshangpings);
            mShangPingRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }

    }

    private class ShangPingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mAddTimeTextView;
        private CheckBox mIsXiHuanCheckBox;

        private ShangPing mShangPing;

        /*
        *
        * 绑定每行的数据
        *
        * */
        public void bindShangPing(ShangPing shangping){
            mShangPing = shangping;
            mTitleTextView.setText(mShangPing.getTitle());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            mAddTimeTextView.setText(sDateFormat.format(mShangPing.getAddTime()));
            mIsXiHuanCheckBox.setChecked(mShangPing.isXiHuan());
        }


        /*
        *
        * 绑定点击事件,绑定视图
        *
        * */
        public ShangPingHolder(View itemView){
            super(itemView);
            // 绑定点击事件
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_shangping_title_text_view);
            mAddTimeTextView = (TextView)itemView.findViewById(R.id.list_item_shangping_addtime_text_view);
            mIsXiHuanCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_shangping_isxihuan_check_box);
        }

        /*
        *
        * 实现点击事件方法
        *
        * */
        @Override
        public void onClick(View v){
            // 启动 ShangPingPagerActivity
//            Intent intent =
        }

    }

    private class ShangPingAdapter extends RecyclerView.Adapter<ShangPingHolder>{

        private List<ShangPing> mShangpingS;

        public ShangPingAdapter(List<ShangPing> shangpings){
            mShangpingS = shangpings;
        }

        @Override
        public ShangPingHolder onCreateViewHolder(ViewGroup parent,int ViewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_shangping,parent,false);
            return new ShangPingHolder(view);
        }

        @Override
        public void onBindViewHolder(ShangPingHolder holder,int position){
            ShangPing shangping = mShangpingS.get(position);
            holder.bindShangPing(shangping);
        }

        @Override
        public int getItemCount(){
            return mShangpingS.size();
        }

    }

}
