package com.thunder.ktv.androidboardtest.view;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.thunder.ktv.androidboardtest.R;
import com.thunder.ktv.androidboardtest.function.AbsFunction;
import com.thunder.ktv.androidboardtest.function.ButtonFun;
import com.thunder.ktv.androidboardtest.function.GpioFun;
import com.thunder.ktv.androidboardtest.function.PlayerVolumeFun;
import com.thunder.ktv.androidboardtest.function.RolandPrmFun;
import com.thunder.ktv.androidboardtest.function.SeekFun;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by chengkai on 18-2-7.
 */

public class MyListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MyListViewAdapter";
    public static final int ItemViewTypeButton = 0;
    public static final int ItemViewTypeSeekBar = 1;
    public static final int ItemViewTypeSwitch = 2;
    public static Map<Integer,Integer> seekMap ;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case ItemViewTypeButton:{
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_item_button, parent, false);
                viewHolder = new ViewHolderButton(v);
                break;
            }
            case ItemViewTypeSeekBar:{
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_item_seek, parent, false);
                viewHolder = new ViewHolderSeek(v);
//                viewHolder.setIsRecyclable(false);
                break;
            }
            case ItemViewTypeSwitch:{
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_item_switch, parent, false);
                viewHolder = new ViewHolderSwitch(v);
                break;
            }
            default:{
                break;
            }
        }
        return viewHolder;
    }
    List<AbsFunction> list = null;
    public MyListViewAdapter(List<AbsFunction> list) {
        this.list = list;
        seekMap = new HashMap<>();
        for (int i = 0;i< list.size();i++){
            seekMap.put(i,list.get(i).defCode - list.get(i).minCode);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).showType;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(list == null || list.size() < position){
            return;
        }
        holder.setIsRecyclable(false);
        MYViewHolder myViewHolder = (MYViewHolder) holder;
        myViewHolder.OnBindViewHolder(list.get(position),position);
    }
    @Override
    public int getItemCount() {
        if(list == null){
            return 0;
        }
        return list.size();
    }

    private static abstract class MYViewHolder extends RecyclerView.ViewHolder{

        public MYViewHolder(View itemView) {
            super(itemView);
        }
        abstract public void OnBindViewHolder(AbsFunction absFunction,int position);
    }

    private static class ViewHolderSeek extends MYViewHolder {
        TextView textViewInfo,textViewName;
        SeekBar seekbar;
        Button seekbar_up;
        Button seekbar_down;
        public ViewHolderSeek(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.item_tv_name);
            textViewInfo = itemView.findViewById(R.id.item_tv_info);
            seekbar = itemView.findViewById(R.id.seekBar);
            seekbar_up = itemView.findViewById(R.id.item_bt_seekBar_up);
            seekbar_down = itemView.findViewById(R.id.item_bt_seekBar_down);
        }

        @Override
        public void OnBindViewHolder(AbsFunction absFunction,int position) {
            int seekVol  = MyListViewAdapter.seekMap.get(position);
            textViewName.setText(absFunction.getShowName());
            seekbar.setMax(absFunction.maxCode - absFunction.minCode);
            seekbar.setProgress(seekVol);

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        MyListViewAdapter.seekMap.remove(position);
                        MyListViewAdapter.seekMap.put(position,seekBar.getProgress());
                        int seekVol  = MyListViewAdapter.seekMap.get(position);

                        Log.d(TAG, fromUser + "  onBindViewHolder: " + absFunction.getShowName() +"   "+ seekVol);

                        absFunction.doAction(seekVol);

                        textViewInfo.setText(absFunction.getShowInfo());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            seekbar_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int progress = seekbar.getProgress() - 1;
                    int min = 0;
//                    int min = holder.seekbar.getMin();
                    if(progress < min){
                        progress = min;
                    }
                    seekbar.setProgress(progress);
                }
            });
            seekbar_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int progress = seekbar.getProgress() + 1;
                    if(progress > seekbar.getMax()){
                        progress = seekbar.getMax();
                    }
                    seekbar.setProgress(progress);
                }
            });
            textViewInfo.setText(absFunction.getShowInfo());
        }
    }
    private static class ViewHolderButton extends MYViewHolder {
        TextView textViewInfo;
        Button button;
        public ViewHolderButton(View itemView) {
            super(itemView);
            textViewInfo = itemView.findViewById(R.id.item_tv_info);
            button = itemView.findViewById(R.id.item_bt);
        }

        @Override
        public void OnBindViewHolder(AbsFunction absFunction,int position) {
            button.setText(absFunction.getShowName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    absFunction.doAction(null);
                    textViewInfo.setText(absFunction.getShowInfo());
                }
            });
            textViewInfo.setText(absFunction.getShowInfo());
        }

    }
    private static class ViewHolderSwitch extends MYViewHolder {
        TextView textViewInfo,textViewName;
        Switch aSwitch;
        public ViewHolderSwitch(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.item_tv_name);
            textViewInfo = itemView.findViewById(R.id.item_tv_info);
            aSwitch = itemView.findViewById(R.id.item_sw);
        }
        public void OnBindViewHolder(AbsFunction absFunction,int position)
        {
            textViewName.setText(absFunction.getShowName());
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    absFunction.doAction(isChecked);
                    textViewInfo.setText(absFunction.getShowInfo());
                }
            });
        }
    }
}
