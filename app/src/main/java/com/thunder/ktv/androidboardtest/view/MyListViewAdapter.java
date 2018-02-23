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
import com.thunder.ktv.androidboardtest.function.AbsFunction;
import com.thunder.ktv.androidboardtest.function.ButtonFun;
import com.thunder.ktv.androidboardtest.function.GpioFun;
import com.thunder.ktv.androidboardtest.function.RolandPrmFun;
import com.thunder.ktv.androidboardtest.function.SeekFun;

import java.util.List;

/**
 * Created by chengkai on 18-2-7.
 */

public class MyListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MyListViewAdapter";
    private final int ItemViewTypeSeekBar = 0;
    private final int ItemViewTypeButton = 1;
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
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position) instanceof SeekFun){
            return ItemViewTypeSeekBar;
        }else if(list.get(position) instanceof RolandPrmFun ||
                list.get(position) instanceof ButtonFun ||
                list.get(position) instanceof GpioFun){
            return ItemViewTypeButton;
        }
        return ItemViewTypeButton;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(list == null || list.size() < position){
            return;
        }
        AbsFunction absFunction = list.get(position);
        switch (getItemViewType(position)){
            case ItemViewTypeButton:{
                ViewHolderButton viewHolderButton = (ViewHolderButton) holder;
                viewHolderButton.button.setText(absFunction.getShowName());
                viewHolderButton.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        absFunction.doAction(null);
                        viewHolderButton.textViewInfo.setText(absFunction.getShowInfo());
                    }
                });
                viewHolderButton.textViewInfo.setText(absFunction.getShowInfo());
                break;
            }
            case ItemViewTypeSeekBar:{
                ViewHolderSeek viewHolderSeek = (ViewHolderSeek) holder;
                viewHolderSeek.textViewName.setText(absFunction.getShowName());
                viewHolderSeek.seekbar.setMax(absFunction.maxCode - absFunction.minCode);
                viewHolderSeek.seekbar.setProgress((absFunction.defCode - absFunction.minCode));
                viewHolderSeek.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Log.d(TAG, "onBindViewHolder: " + seekBar.getProgress());
                        absFunction.doAction(seekBar.getProgress());
                        viewHolderSeek.textViewInfo.setText(absFunction.getShowInfo());
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                viewHolderSeek.seekbar_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int progress = viewHolderSeek.seekbar.getProgress() - 1;
                        int min = 0;
//                    int min = holder.seekbar.getMin();
                        if(progress < min){
                            progress = min;
                        }
                        viewHolderSeek.seekbar.setProgress(progress);
                    }
                });
                viewHolderSeek.seekbar_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int progress = viewHolderSeek.seekbar.getProgress() + 1;
                        if(progress > viewHolderSeek.seekbar.getMax()){
                            progress = viewHolderSeek.seekbar.getMax();
                        }
                        viewHolderSeek.seekbar.setProgress(progress);
                    }
                });
                viewHolderSeek.textViewInfo.setText(absFunction.getShowInfo());
                break;
            }
            default:{
                break;
            }
        }
    }
    @Override
    public int getItemCount() {
        if(list == null){
            return 0;
        }
        return list.size();
    }
    public static class ViewHolderSeek extends RecyclerView.ViewHolder {
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
    }
    public static class ViewHolderButton extends RecyclerView.ViewHolder {
        TextView textViewInfo;
        Button button;
        public ViewHolderButton(View itemView) {
            super(itemView);
            textViewInfo = itemView.findViewById(R.id.item_tv_info);
            button = itemView.findViewById(R.id.item_bt);
        }
    }
}
