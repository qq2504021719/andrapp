package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    /*
    *
    * 创建CrimeAdapter,然后设置给RecyclerView
    *
    * */
    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder{

        public TextView mTitleTextView;

        public CrimeHolder(View itemView){
            super(itemView);
            mTitleTextView = (TextView)itemView;
        }

    }
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){

            mCrimes = crimes;

        }
        /*
        * RecycleView需要新的View视图来显示列表项时,会调用此方法,在这个方法内部,我创建View视图，然后封装到ViewHolder中。此时，RecyclerView并不要求
        * 封装视图装载数据
        *
        * 为得到View视图，我实例化了Android标准库中名为simple_list_item_1的布局。改布局定义了美观的TextView视图，主要用于列表项的展示。
        *
        * */
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent,int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            return new CrimeHolder(view);
        }


        /*
        *
        * 该方法会把ViewHolder的View视图和模型层数据绑定起来。收到ViewHolder和列表项咋数据集中的索引位置后，我们通过索引位置找到要显示的
        * 数据进行绑定。绑定完毕，刷新显示View视图。
        * 所谓索引位置，实际上就是数组中Crime的位置。取出目标数据后，通过发送crime标题给ViewHolder的TextView视图，我们就完成了Crime数据和View视图的绑定。
        *
        * */
        @Override
        public void onBindViewHolder(CrimeHolder holder,int position){
            Crime crime = mCrimes.get(position);
            holder.mTitleTextView.setText(crime.getTitle());
        }

        @Override
        public int getItemCount(){
            return mCrimes.size();
        }

    }



}
