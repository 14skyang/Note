package com.ysk.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//NoteList
public class MainActivity extends AppCompatActivity {
    private final static String TAG="NoteList";
    private List<Note>mDataList;//数据列表
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private MenuItem mDeleteNote;
    private MenuItem mSelectAll;
    private static boolean mIsDeleteMode=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Connector.getDatabase();//创建数据库和数据表
        mDataList=new ArrayList<Note>();
        refresh();//使得数据表中的数据最新

        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        mRecyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        mMyAdapter=new MyAdapter(mDataList);
        final FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        mToolbar.setNavigationIcon(R.drawable.ic_event_note_white_3x);
        mToolbar.getNavigationIcon().setVisible(true,false);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDeleteMode()){
                    mToolbar.setNavigationIcon(R.drawable.ic_event_note_white_3x);
                    changeDeleteMode(false);
                    for (int i=0;i<mDataList.size();i++){
                        mDataList.get(i).setIsChecked(false);
                    }
                    Log.e(TAG, "disappear_delete_mode ");
                    mMyAdapter.setList(mDataList);
                    mRecyclerView.setAdapter(mMyAdapter);
                    mMyAdapter.notifyDataSetChanged();

                }
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMyAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//定义为默认动画？
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { //滚动监听
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.SCROLL_STATE_IDLE==newState){
                    //暂停移动
                    fab.show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>1||dy<-1){//向上或者向下滑动
                    fab.hide();
                }else {
                    fab.show();
                }
            }
        });
        mMyAdapter.setmListener(new MyItemClickListener() {//这里只需创造接口类对象，并且实现其中的方法，这是一种简写
            //不省略的写法是需要创造一个类实现MyItemClickListener接口，然后父类对象指向子类实例(父类为接口)，然后这里放入父类对象
            @Override
            public void onItemClick(View view, int positon) {
                Note note=mDataList.get(positon);
                if (!mIsDeleteMode){//当前页面未处于删除模式，才可以删除
                    refresh();
                    Intent intent=new Intent(MainActivity.this,NoteEdit.class);
                    note=mDataList.get(positon);
                    intent.putExtra("mode","update");//发送editmode到edit接口
                    intent.putExtra("id",note.getId());//发送note的id到edit接口
                    startActivityForResult(intent,2);//启动活动并且返回结果
                }else {
                    note.setIsChecked(!note.isChecked());
                    MyAdapter.ViewHolder viewHolder=new MyAdapter.ViewHolder(view);
                    viewHolder.getCheckBox().setChecked(note.isChecked());
                }
            }
        });
        mMyAdapter.setmLongClickListener(new MyItemLongClickListener() {//注意括号里的参数
            @Override
            public void onItemLongClick(View view, int position) {
                //更新toolbar里的item
                changeDeleteMode(true);
                mToolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
                Note note=mDataList.get(position);
                note.setIsChecked(true);
                mMyAdapter.notifyDataSetChanged();
            }
        });
        mMyAdapter.setMyCheckboxChangedListener(new MyCheckboxChangedListener() {
            @Override
            public void onChanged() {//监测是否所有的checkbox被选中
                int checkedNum=0;//被选中的checkbox的数量
                for (int i=0;i<mDataList.size();i++){
                    if (mDataList.get(i).isChecked()){
                        checkedNum++;
                    }
                }
                Log.e(TAG, checkedNum+"");
                if (mSelectAll!=null){
                    if (checkedNum==mDataList.size()&&"All".equals(mSelectAll.getTitle())){
                        Log.e(TAG, "CLICK ALL");
                        mSelectAll.setTitle("No All");
                        mSelectAll.setIcon(R.drawable.ic_check_box_white_24dp);
                    }
                    if (checkedNum<mDataList.size()&&"No All".equals(mSelectAll.getTitle())) {
                        Log.e(TAG, "CLICK NO ALL");
                        mSelectAll.setTitle("All");
                        mSelectAll.setIcon(R.drawable.ic_select_all_white_18dp);
                    }
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsDeleteMode){
                    Intent intent=new Intent(MainActivity.this,NoteEdit.class);
                    intent.putExtra("mode","new");//发送editMode到edit接口
                    startActivityForResult(intent,1);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单，当actionbar显示出来的时候，加入item进menu
        getMenuInflater().inflate(R.menu.menu_note_list,menu);
        mDeleteNote=menu.findItem(R.id.delete_note);
        mSelectAll=menu.findItem(R.id.select_all);
        mDeleteNote.setVisible(false);
        mSelectAll.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.about){
            TextView content=(TextView)getLayoutInflater().inflate(R.layout.about_view,null);
            content.setMovementMethod(LinkMovementMethod.getInstance());
            content.setText(Html.fromHtml(getString(R.string.about)));
            new AlertDialog.Builder(this)
                    .setTitle("about")
                    .setView(content)
                    .setInverseBackgroundForced(true)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();//关闭对话框
                        }
                    }).create().show();
            return true;//这是啥？
        }
        if (id==R.id.delete_note){
            int checkedNum=0;//被选中的checkbox的数量
            for (int i=0;i<mDataList.size();i++){
                if (mDataList.get(i).isChecked()){
                    checkedNum++;
                }
            }
            createAlertDialog(checkedNum);
            return  true;
        }
        if (id==R.id.select_all){
            if ("No All".equals(mSelectAll.getTitle())){
                Log.e(TAG, "select_all");
                for (int i=0;i<mDataList.size();i++){
                    mDataList.get(i).setIsChecked(false);
                    mToolbar.setNavigationIcon(R.drawable.ic_event_note_white_3x);
                    changeDeleteMode(false);
                }
            }else {
                Log.e(TAG, "select_all ");
                for (int i=0;i<mDataList.size();i++){
                    mDataList.get(i).setIsChecked(true);
                    View view=mRecyclerView.getChildAt(i);
                    if (view!=null){
                        MyAdapter.ViewHolder viewHolder=new MyAdapter.ViewHolder(view);
                        viewHolder.getCheckBox().setChecked(true);
                    }
                }
            }
            mMyAdapter.setList(mDataList);
            mRecyclerView.setAdapter(mMyAdapter);
            mMyAdapter.notifyDataSetChanged();
        }
        //加日历按钮
        if(id==R.id.calendar){
            Intent intent=new Intent(MainActivity.this,CalendarActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    private void refresh() {
        mDataList = DataSupport.findAll(Note.class);
        Collections.sort(mDataList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refresh();
        mMyAdapter.setList(mDataList);
        mRecyclerView.setAdapter(mMyAdapter);
    }

    private void changeDeleteMode(boolean isDelete) {
        mIsDeleteMode = isDelete;
        mDeleteNote.setVisible(isDelete);
        mSelectAll.setVisible(isDelete);
        if (isDelete) {
            mToolbar.setTitle("");
        } else {
            mToolbar.setTitle("Notepad");
        }
    }
    protected void createAlertDialog(int number){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("你确定想要删除"+number+"条消息吗？");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i=0;i<mDataList.size();i++){
                    if (mDataList.get(i).isChecked()){
                        DataSupport.delete(Note.class,mDataList.get(i).getId());
                    }
                }
                refresh();;
                mMyAdapter.setList(mDataList);
                mRecyclerView.setAdapter(mMyAdapter);
                changeDeleteMode(false);
                mToolbar.setNavigationIcon(R.drawable.ic_event_note_white_3x);
                mMyAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();//builder的用法！！！
    }
    public static boolean isDeleteMode() {
        return mIsDeleteMode;
    }
}
