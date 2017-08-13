package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/9.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;

    private CrimeAdapter mAdapter;
    // 切换菜单项标题状态
    private boolean mSubtitleVisible;

    // 保存菜单项标题状态
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // 让FragmentManager知道CrimeListFragment需接收选项菜单方法回调
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        // 接收菜单项标题状态, 显示犯罪 隐藏犯罪
        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

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
    * activity状态改变是保存值
    * */
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    /*
    *
    * 实例化fragment_crime_list.xml中定义的菜单。将布局文件中定义的菜单项目填充到Menu实例中
    * */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

        // 设置菜单项对应id显示的文字
        MenuItem subtitleItem  = menu.findItem(R.id.menu_itme_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    /*
    *
    * 用户点击菜单中的菜单项目时,fragment会收到onOptionsItemSelected(MenuItem)方法的回调请求。
    * 传入该方法的参数是一个描述用户选择的MenuItem实例
    *
    * 当前菜单仅有一个菜单项,但菜单通常包含多个菜单项。通过检查菜单项ID,可确定被选中的是哪个菜单项,
    * 然后做出相应的响应。这个ID实际就是在菜单定义文件中赋予菜单项的资源ID。
    *
    * 响应菜单项的选择事件。在该方法中,创建新的Crime实例，并将其添加到CrimeLab，然后启动CrimePagerActivity实例，
    * 让用户可以编辑创建的Crime记录
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(),crime.getId());
                startActivity(intent);
                return true;
            case R.id.menu_itme_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                // 更新犯罪数量
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    *
    * 更新犯罪数量
    * */
    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        String crimeCount = String.valueOf(crimeLab.getCrimes().size());
        String subtitle = getString(R.string.subtitle_format,crimeCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
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

        // 更新犯罪数量
        updateSubtitle();
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
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            mDateTextView.setText(sDateFormat.format(mCrime.getDate()));
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
        * 该方法会把ViewHolder的View视图和模型层数据绑定起来。收到ViewHolder和列表项数据集中的索引位置后，我们通过索引位置找到要显示的
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
