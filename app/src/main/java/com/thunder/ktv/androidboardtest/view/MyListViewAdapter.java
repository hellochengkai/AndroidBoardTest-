package com.thunder.ktv.androidboardtest.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.thunder.ktv.androidboardtest.R;
import com.thunder.ktv.androidboardtest.function.ActionBase;
import com.thunder.ktv.androidboardtest.function.FunctionInfo;
import com.thunder.ktv.androidboardtest.function.IFunction;
import com.thunder.ktv.androidboardtest.uartfun.AbsFunction;
import com.thunder.ktv.androidboardtest.uartfun.SeekFun;

import java.util.List;
import static com.thunder.ktv.androidboardtest.R.color.colorPrimary;

/**
 * Created by chengkai on 18-2-7.
 */

public class MyListViewAdapter extends RecyclerView.Adapter<MyListViewAdapter.ViewHolder> {
    public final static String ShowTypeButton = "button";
    public final static String ShowTypeSeekBar = "seekbar";
    public final static String ShowTypeText = "text";
    private static final String TAG = "MyListViewAdapter";

    final int actionShowSize = 20;
    final int funShowSize = 25;
//    private FunctionInfo functionInfo;
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
//    @Override
//    public void onBindViewHolder(MyListViewAdapter.ViewHolder holder, int position) {
////        final List<IFunction> funlist = functionInfo.getFunction_list();
////        List<ActionBase> actlist = functionInfo.getAction_list();
////        if(position < funlist.size()){
////            String name = funlist.get(position).functionBase.getName();
////            String type = funlist.get(position).functionBase.getType();
////            String args = funlist.get(position).functionBase.getArgs().toString();
////            String info = String.format("方法:%s\n类型:%s\n参数列表:%s",name,type,args);
////            SpannableStringBuilder builder = new SpannableStringBuilder(info);
////            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#ff303F9F")),
////                    0, info.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
////            holder.button.setVisibility(View.GONE);
////            holder.seekbar.setVisibility(View.GONE);
////            holder.textViewInfo.setTextSize(funShowSize);
////            holder.textViewInfo.setBackgroundColor(colorPrimary);
////            holder.textViewInfo.setText(builder);
////        }else{
////            ActionBase actionBase = actlist.get(position - funlist.size());
////            if(actionBase == null)
////                return;
////            String name = actionBase.getShow_name();
////            String fun = actionBase.getFun();
////            final String cmd = actionBase.getCmd();
////            String info = String.format("方法:%s\n码值:%s",fun,cmd);
////            for (int i  = 0; i < funlist.size();i ++){
////                if(funlist.get(i).functionBase.getName().equals(fun))
////                {
////                    holder.textViewName.setTextSize(actionShowSize);
////                    holder.textViewInfo.setTextSize(actionShowSize);
////
////                    holder.button.setVisibility(View.GONE);
////                    holder.seekbar.setVisibility(View.GONE);
////
////                    switch ( actionBase.getView_type()){
////                        case ShowTypeButton:{
////                            holder.button.setVisibility(View.VISIBLE);
////                            holder.button.setTextSize(actionShowSize);
////                            holder.button.setText(name);
////                            final int finalI = i;
////                            holder.button.setOnClickListener(new View.OnClickListener() {
////                                @Override
////                                public void onClick(View v) {
////                                    actionBase.doAction();
////                                    funlist.get(finalI).doFunction(cmd);
////                                }
////                            });
////                            break;
////                        }
////                        case ShowTypeSeekBar:{
////                            holder.seekbar.setVisibility(View.VISIBLE);
////                            break;
////                        }
////                        case ShowTypeText:
////                        default:{
////                            break;
////                        }
////                    }
////                }
////            }
////            holder.textViewName.setText(name);
////            holder.textViewInfo.setText(info);
////        }
//    }
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
            holder.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Log.d(TAG, "onBindViewHolder: " + seekBar.getProgress());
                    absFunction.doAction(seekBar.getProgress());
                    holder.textViewInfo.setText(absFunction.getShowInfo());
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            holder.seekbar_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int progress = holder.seekbar.getProgress() - 1;
                   if(progress < holder.seekbar.getMin()){
                       progress = holder.seekbar.getMin();
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
        }
    }
    @Override
    public int getItemCount() {
//        if(functionInfo == null){
//            return 0;
//        }
//        return functionInfo.getAction_list().size() + functionInfo.getFunction_list().size();
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
