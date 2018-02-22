package com.thunder.ktv.androidboardtest.view;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.thunder.ktv.androidboardtest.R;
import com.thunder.ktv.androidboardtest.uartfun.AbsFunction;
import com.thunder.ktv.androidboardtest.uartfun.ButtonFun;
import com.thunder.ktv.androidboardtest.uartfun.RolandPrmFun;
import com.thunder.ktv.androidboardtest.uartfun.SeekFun;

import java.util.List;

/**
 * Created by chengkai on 18-2-7.
 */

public class MyListViewAdapter extends RecyclerView.Adapter<MyListViewAdapter.ViewHolder> {
    private static final String TAG = "MyListViewAdapter";

    @Override
    public MyListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    List<AbsFunction> list = null;
    public MyListViewAdapter(List<AbsFunction> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(MyListViewAdapter.ViewHolder holder, int position) {
        if(list == null || list.size() < position){
            return;
        }
        AbsFunction absFunction = list.get(position);
        if(absFunction instanceof SeekFun){
            SeekFun seekFun = (SeekFun) absFunction;
            holder.button.setVisibility(View.GONE);
            holder.textViewName.setText(seekFun.getShowName());
            holder.seekbar.setMax(seekFun.maxCode - seekFun.minCode);
            holder.seekbar.setProgress((seekFun.defCode - seekFun.minCode));
            holder.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Log.d(TAG, "onBindViewHolder: " + seekBar.getProgress());
                    absFunction.doAction(seekBar.getProgress());
                    holder.textViewInfo.setText(absFunction.getShowInfo());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            holder.seekbar_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int progress = holder.seekbar.getProgress() - 1;
                    int min = 0;
//                    int min = holder.seekbar.getMin();
                    if(progress < min){
                       progress = min;
                   }
                    holder.seekbar.setProgress(progress);
                }
            });
            holder.seekbar_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int progress = holder.seekbar.getProgress() + 1;
                    if(progress > holder.seekbar.getMax()){
                        progress = holder.seekbar.getMax();
                    }
                    holder.seekbar.setProgress(progress);
                }
            });
            holder.textViewInfo.setText(absFunction.getShowInfo());
        }else if(absFunction instanceof ButtonFun || absFunction instanceof RolandPrmFun){
            holder.textViewName.setVisibility(View.GONE);
            holder.seekbar.setVisibility(View.GONE);
            holder.seekbar_down.setVisibility(View.GONE);
            holder.seekbar_up.setVisibility(View.GONE);
            holder.button.setText(absFunction.getShowName());
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    absFunction.doAction(null);
                    holder.textViewInfo.setText(absFunction.getShowInfo());
                }
            });
            holder.textViewInfo.setText(absFunction.getShowInfo());
        }
    }
    @Override
    public int getItemCount() {
        if(list == null){
            return 0;
        }
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewInfo,textViewName;
        Button button;
        SeekBar seekbar;
        Button seekbar_up;
        Button seekbar_down;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.item_tv_name);
            textViewInfo = itemView.findViewById(R.id.item_tv_info);
            button = itemView.findViewById(R.id.item_bt);
            seekbar = itemView.findViewById(R.id.seekBar);
            seekbar_up = itemView.findViewById(R.id.item_bt_seekBar_up);
            seekbar_down = itemView.findViewById(R.id.item_bt_seekBar_down);
        }
    }
}
