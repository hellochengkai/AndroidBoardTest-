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
import com.thunder.ktv.androidboardtest.function.basefun.AbsFunction;
import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;
import com.thunder.ktv.androidboardtest.function.basefun.SeekFun;
import com.thunder.ktv.androidboardtest.function.basefun.SwitchListFun;
import com.thunder.ktv.thunderjni.until.Logger;

import java.util.List;

/**
 * Created by chengkai on 18-2-7.
 */

public class MyListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MyListViewAdapter";
    public static final int ItemViewTypeButton = 0;
    public static final int ItemViewTypeSeekBar = 1;
    public static final int ItemViewTypeSwitch = 2;

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
    public static List<AbsFunction> list = null;
    public MyListViewAdapter(List<AbsFunction> list) {
        this.list = list;
//        seekMap = new HashMap<>();
//        for (int i = 0;i< list.size();i++){
//            if(list.get(i).showType == ItemViewTypeSeekBar){
//                if(list.get(i) instanceof SeekFun){
//                    SeekFun seekFun = (SeekFun) list.get(i);
//                    seekMap.put(i,seekFun);
//                }
//            }
//        }
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
            textViewName.setText(absFunction.getShowName());
            Log.d(TAG, "OnBindViewHolder: position " + position);
            if(!( absFunction instanceof SeekFun)){
                return;
            }
            SeekFun seekFun = (SeekFun) absFunction;
            seekbar.setMax(seekFun.max);
            seekbar.setProgress(seekFun.cur);

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekFun.cur = seekBar.getProgress();
                        Log.d(TAG, fromUser + "  onBindViewHolder: " + absFunction.getShowName() +"   "+ seekFun.cur);
                        absFunction.doAction(seekFun.cur);
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
                    if(progress < min){
                        progress = min;
                    }
                    seekbar.setProgress(progress);
                }
            });
            seekbar_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.debug(TAG,"position == " + position);
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
        TextView textViewInfo,textViewName;
        Button[] buttons = new Button[4];
        public ViewHolderButton(View itemView) {
            super(itemView);
            textViewInfo = itemView.findViewById(R.id.item_tv_info);
            textViewName = itemView.findViewById(R.id.item_tv_name);
            buttons[0] = itemView.findViewById(R.id.item_bt1);
            buttons[1] = itemView.findViewById(R.id.item_bt2);
            buttons[2] = itemView.findViewById(R.id.item_bt3);
            buttons[3] = itemView.findViewById(R.id.item_bt4);
        }

        @Override
        public void OnBindViewHolder(AbsFunction absFunction,int position) {
            textViewName.setText(absFunction.getShowName());
            textViewName.setVisibility(View.GONE);
            if(absFunction instanceof ButtonListFun){
                ButtonListFun buttonListFun = (ButtonListFun) absFunction;
                for (int i = 0;i< buttons.length;i++){
                    if(i >= buttonListFun.buttonBaseList.size()){
                        buttons[i].setVisibility(View.GONE);
                    }else{
                        ButtonListFun.ButtonBase buttonBase = (ButtonListFun.ButtonBase) buttonListFun.buttonBaseList.get(i);
                        if(null == buttonBase){
                            buttons[i].setVisibility(View.GONE);
                            continue;
                        }
                        buttons[i].setText(buttonBase.name);
                        int finalI = i;
                        buttons[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Logger.debug(TAG,"position == " + position);
                                buttonBase.doAction(null);
                                textViewInfo.setText(buttonListFun.getShowInfo());
                            }
                        });
                    }
                }
            }
            textViewInfo.setText(absFunction.getShowInfo());
        }
    }
    private static class ViewHolderSwitch extends MYViewHolder {
        TextView textViewInfo;
        Switch[] switches = new Switch[2];
        TextView[] textViewNames = new TextView[2];
        public ViewHolderSwitch(View itemView) {
            super(itemView);
            textViewInfo = itemView.findViewById(R.id.item_tv_info);
            textViewNames[0] = itemView.findViewById(R.id.item_tv_name1);
            textViewNames[1] = itemView.findViewById(R.id.item_tv_name2);
            switches[0] = itemView.findViewById(R.id.item_sw1);
            switches[1] = itemView.findViewById(R.id.item_sw2);
        }
        public void OnBindViewHolder(AbsFunction absFunction,int position)
        {
            if(absFunction instanceof SwitchListFun){
                SwitchListFun switchListFun = (SwitchListFun) absFunction;
                for (int i = 0;i< switches.length;i++){
                    if(i >= switchListFun.switchBaseList.size()){
                        switches[i].setVisibility(View.GONE);
                    }else{
                        SwitchListFun.SwitchBase switchBase = (SwitchListFun.SwitchBase) switchListFun.switchBaseList.get(i);
                        if(null == switchBase){
                            switches[i].setVisibility(View.GONE);
                            continue;
                        }
                        switches[i].setChecked(switchBase.isChecked);
                        switches[i].setText(switchBase.getName());
                        int finalI = i;
                        textViewNames[i].setText(absFunction.getShowName());
                        switches[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Logger.debug(TAG,"position == " + position);
                                switchBase.doAction(isChecked);
                                switchBase.isChecked = isChecked;
                                textViewInfo.setText(switchListFun.getShowInfo());
                            }
                        });
                    }
                }
            }
        }
    }
}
