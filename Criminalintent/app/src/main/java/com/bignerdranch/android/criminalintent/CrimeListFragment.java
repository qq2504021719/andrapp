package com.bignerdranch.android.criminalintent;

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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/9.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;

    private CrimeAdapter mAdapter;

    private UUID onClickMcrimId;

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
    * 托管Activity恢复运行后,操作系统会发出调用onResume()生命周期方法的指令。activity接到指令后
    * 它的FragmentManager会调用当前被Activity托管的fragment的onResume()方法
    *
    * 一般来说,要保证fragment视图得到刷新,在onResume()方法内更新代码是最安全的选择
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
    * 创建CrimeAdapter,然后设置给RecyclerView
    *
    * 如果已经配置好了CrimeAdapter,就会调用notifyDataSetChanged()方法来修改updateUI()方法
    * */
    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if(mAdapter == null){
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
//            Toast.makeText(getActivity(),"点击了id"+onClickMcrimId,Toast.LENGTH_SHORT).show();
            mAdapter.notifyDataSetChanged();
//            mAdapter.notifyItemChanged(onClickMcrimId);
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        private Crime mCrime;


        /*
        * 绑定每行的数据
        * */
        public void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        public CrimeHolder(View itemView){
            super(itemView);
            // 绑定点击事件
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        /*
        * 实现点击事件方法
        * */
        @Override
        public void onClick(View v){
            //  启动CrimeActivity
//            Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getId());
            Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent);
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
            View view = layoutInflater.inflate(R.layout.list_item_crime,parent,false);
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
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount(){
            return mCrimes.size();
        }

    }



}
