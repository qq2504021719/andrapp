package com.bignerdranch.android.beatbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */

public class BeatBoxFragment extends Fragment{

    private BeatBox mBeatBox;

    public static BeatBoxFragment newInstance(){
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // 默认为false,这表明不会被保留,设备旋转时fragment会随托管activity一起销毁并重建
        //为true方法可保留fragment,已保留的fragment不会随activity一起被销毁,相反,他会
        //一直保留并在需要时原封不动地传递给新的activity
        setRetainInstance(true);
        mBeatBox = new BeatBox(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_beat_box,container,false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.fragment_beat_box_recycler_view);
        // 已网格形式布局,数字3标识网格的每行有3列
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));
        return view;
    }

    /**
     * Activity销毁时调用
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        // 释放音频
        mBeatBox.release();
    }

    /**
     * 创建一个使用list_item_sound.xml布局的ViewHolder
     */
    private class SoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Button mButton;
        private Sound mSound;

        public SoundHolder(LayoutInflater inflater,ViewGroup container){
            super(inflater.inflate(R.layout.list_item_sound,container,false));
            mButton = (Button)itemView.findViewById(R.id.list_item_sound_button);
            mButton.setOnClickListener(this);
        }

        /**
         * 按钮名称设置为文件名称
         * @param sound
         */
        public void bindSound(Sound sound){
            mSound = sound;
            mButton.setText(mSound.getName());
        }

        /**
         * Button点击事件
         * @param v
         */
        @Override
        public void onClick(View v){
            mBeatBox.play(mSound);
        }
    }

    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{
        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds){
            mSounds = sounds;
        }

        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SoundHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(SoundHolder holder, int position) {
            Sound sound = mSounds.get(position);
            holder.bindSound(sound);
        }

        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }
}
