package com.ysk.note;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

//adapter将数据和view组件做适配
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> { //Viewholder作为内部类在内部定义
    private final static String TAG="MyAdapter";
    private List<Note>mlist;
    private  MyItemClickListener mListener;//声明接口
    private MyItemLongClickListener mLongClickListener;
    private MyCheckboxChangedListener myCheckboxChangedListener;
    public  MyAdapter(List<Note>list){
        this.mlist=list;
    }

    public void setList(List<Note> mlist) {
        this.mlist = mlist;
    }

    public void setmListener(MyItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setmLongClickListener(MyItemLongClickListener mLongClickListener) {
        this.mLongClickListener = mLongClickListener;
    }

    public void setMyCheckboxChangedListener(MyCheckboxChangedListener myCheckboxChangedListener) {
        this.myCheckboxChangedListener = myCheckboxChangedListener;
    }

    @Override
    public int getItemCount() {//recyclerView的adpter的工具
        return mlist.size();
    }
    protected  static class ViewHolder extends  RecyclerView.ViewHolder{//自定义内部类
        private TextView text1;
        private TextView text2;
        private TextView time;
        private CheckBox checkBox;

        public CheckBox getCheckBox() {
            return checkBox;
        }
        public ViewHolder(View itemView){
            super(itemView);
            text1=(TextView)itemView.findViewById(R.id.tv1);
            text2=(TextView)itemView.findViewById(R.id.tv2);
            time=(TextView)itemView.findViewById(R.id.time);
            checkBox=(CheckBox)itemView.findViewById(R.id.checkbox);
        }
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {//注意是自定义的MyAdapter.ViewHolder
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Note note=mlist.get(position);
        holder.checkBox.setVisibility(MainActivity.isDeleteMode()?View.VISIBLE:View.INVISIBLE);
        //根据是否处于删除模式去改变当前checkbox是否显示
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override      //CompoundButton,具有两个状态的按钮,已选中或未选中。当按下或点击按钮时,状态会自动更改。
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "onCheckedChangeListener ");
                myCheckboxChangedListener.onChanged();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position=holder.getAdapterPosition();
                mLongClickListener.onItemLongClick(v,position);
                holder.checkBox.setChecked(true);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                mListener.onItemClick(v,position);
            }
        });
       if (note.getTitle().equals("")) {//无标题则取内容的前几个字符
           if (note.getContent().length() <= 10)
               holder.text1.setText(note.getContent().trim());
           else
               holder.text1.setText(note.getContent().substring(0, 10).trim());
           //substring提取字符串中介于两个指定下标之间的字符
       }else {
           holder.text1.setText(note.getTitle().trim());
       }
       if (note.getContent().length()>21){//内容过长，省略部分,用substring截取字符串
           holder.text2.setText(note.getContent().substring(0,21).trim()+"...");

       }else {
           holder.text2.setText(note.getContent().trim());
       }
       holder.time.setText(note.getDate());
       holder.checkBox.setChecked(mlist.get(position).isChecked());
    }

}
