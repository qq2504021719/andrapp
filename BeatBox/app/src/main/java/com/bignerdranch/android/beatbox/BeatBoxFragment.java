package com.bignerdranch.android.beatbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Administrator on 2017/8/21.
 */

public class BeatBoxFragment extends Fragment{

    public static BeatBoxFragment newInstance(){
        return new BeatBoxFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_beat_box,container,false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.fragment_beat_box_recycler_view);
        // 已网格形式布局,数字3标识网格的每行有3列
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(new SoundAdapter());
        return view;
    }

    /**
     * 创建一个使用list_item_sound.xml布局的ViewHolder
     */
    private class SoundHolder extends RecyclerView.ViewHolder{
        private Button mButton;

        public SoundHolder(LayoutInflater inflater,ViewGroup container){
            super(inflater.inflate(R.layout.list_item_sound,container,false));
            mButton = (Button)itemView.findViewById(R.id.list_item_sound_button);
        }
    }

    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{

        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SoundHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(SoundHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
