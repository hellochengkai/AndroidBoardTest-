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

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.R;
import com.thunder.ktv.androidboardtest.function.AbsFunction;
import com.thunder.ktv.androidboardtest.function.SeekFun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengkai on 18-2-7.
 */

public class MyListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MyListViewAdapter";
    public static final int ItemViewTypeButton = 0;
    public static final int ItemViewTypeSeekBar = 1;
    public static final int ItemViewTypeSwitch = 2;

    private void updataView()
    {
//        this.notifyDataSetChanged();
    }

    public class BaseCode{
        public final static int TYPE_UNKNOW = -1;
        public final static int TYPE_DELAY = 0;
        public final static int TYPE_ECHO = 1;
        public final static int TYPE_MIC = 2;
        public final static int TYPE_MUSIC = 3;
        String name;
        int type;
        int curCode;
        int minCode;
        int maxCode;

        public int getType() {
            return type;
        }

        public void up()
        {
            curCode++;
            if(curCode > maxCode){
                curCode = maxCode;
            }
            updataView();
        }
        public void down()
        {
            curCode--;
            if(curCode < minCode){
                curCode = minCode;
            }
            updataView();
        }

        public BaseCode(String name, int type, int curCode, int minCode, int maxCode) {
            this.name = name;
            this.type = type;
            this.curCode = curCode;
            this.minCode = minCode;
            this.maxCode = maxCode;
        }
    }

    public static Map<Integer,BaseCode> seekMap ;
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
            if(list.get(i).showType == ItemViewTypeSeekBar){
                int CodeType = BaseCode.TYPE_UNKNOW;
                if(list.get(i) instanceof SeekFun){
                    SeekFun seekFun = (SeekFun) list.get(i);
                    CodeType = seekFun.getCodeType();
                }
                seekMap.put(i,new BaseCode(
                        list.get(i).getShowName(),
                        CodeType,
                        list.get(i).defCode - list.get(i).minCode,
                        list.get(i).minCode,
                        list.get(i).maxCode));
            }
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
            int seekVol  = 0;
            BaseCode baseCode = MyListViewAdapter.seekMap.get(position);
            if(baseCode != null){
                seekVol = baseCode.curCode;
            }
            textViewName.setText(absFunction.getShowName());
            seekbar.setMax(absFunction.maxCode - absFunction.minCode);
            seekbar.setProgress(seekVol);

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        MyListViewAdapter.seekMap.get(position).curCode = seekBar.getProgress();
                        BaseCode baseCode = MyListViewAdapter.seekMap.get(position);
                        if(baseCode == null){
                            Log.d(TAG, "baseCode == null position is " + position);
                            return;
                        }
                        baseCode.curCode = seekBar.getProgress();
//                        MyListViewAdapter.seekMap.put(position,seekBar.getProgress());
                        int seekVol = baseCode.curCode;
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
