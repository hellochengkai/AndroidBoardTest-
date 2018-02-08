package com.thunder.ktv.androidboardtest.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
    private FunctionInfo functionInfo;
    @Override
    public MyListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public MyListViewAdapter(FunctionInfo functionInfo) {
        this.functionInfo = functionInfo;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyListViewAdapter.ViewHolder holder, int position) {
        final List<IFunction> funlist = functionInfo.getFunction_list();
        List<ActionBase> actlist = functionInfo.getAction_list();
        if(position < funlist.size()){
            String name = funlist.get(position).functionBase.getName();
            String type = funlist.get(position).functionBase.getType();
            String args = funlist.get(position).functionBase.getArgs().toString();
            String info = String.format("方法:%s\n类型:%s\n参数列表:%s",name,type,args);
            SpannableStringBuilder builder = new SpannableStringBuilder(info);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#ff303F9F")),
                    0, info.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.button.setVisibility(View.GONE);
            holder.seekbar.setVisibility(View.GONE);
            holder.textViewInfo.setTextSize(funShowSize);
            holder.textViewInfo.setBackgroundColor(colorPrimary);
            holder.textViewInfo.setText(builder);
        }else{
            ActionBase actionBase = actlist.get(position - funlist.size());
            if(actionBase == null)
                return;
            String name = actionBase.getShow_name();
            String fun = actionBase.getFun();
            final String cmd = actionBase.getCmd();
            String info = String.format("方法:%s\n码值:%s",fun,cmd);
            for (int i  = 0; i < funlist.size();i ++){
                if(funlist.get(i).functionBase.getName().equals(fun))
                {
                    holder.textViewName.setTextSize(actionShowSize);
                    holder.textViewInfo.setTextSize(actionShowSize);

                    holder.button.setVisibility(View.GONE);
                    holder.seekbar.setVisibility(View.GONE);

                    switch ( actionBase.getView_type()){
                        case ShowTypeButton:{
                            holder.button.setVisibility(View.VISIBLE);
                            holder.button.setTextSize(actionShowSize);
                            holder.button.setText(name);
                            final int finalI = i;
                            holder.button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    actionBase.doAction();
                                    funlist.get(finalI).doFunction(cmd);
                                }
                            });
                            break;
                        }
                        case ShowTypeSeekBar:{
                            holder.seekbar.setVisibility(View.VISIBLE);
                            break;
                        }
                        case ShowTypeText:
                        default:{
                            break;
                        }
                    }
                }
            }
            holder.textViewName.setText(name);
            holder.textViewInfo.setText(info);
        }
    }

    @Override
    public int getItemCount() {
        if(functionInfo == null){
            return 0;
        }
        return functionInfo.getAction_list().size() + functionInfo.getFunction_list().size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewInfo,textViewName;
        Button button;
        SeekBar seekbar;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.item_tv_name);
            textViewInfo = itemView.findViewById(R.id.item_tv_info);
            button = itemView.findViewById(R.id.item_bt);
            seekbar = itemView.findViewById(R.id.seekBar);
        }
    }
}
