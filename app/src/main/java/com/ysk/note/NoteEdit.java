package com.ysk.note;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;


public class NoteEdit extends AppCompatActivity {
    private EditText mText1;
    private EditText mText2;
    private String mEditMode;//从上一个activity获得的参数，区分update和create
    private String mSaveMode;//保存模式，区分为删除，保存，和直接退出活动的默认保存
    private  int mUpdateId= -1;
    private Note mNote;////这个页面只存在一个note
    private boolean mIsSaveDefault=true;//保存作为默认
    private static final String SAVEMODE_DELETE="DELETE";
    private static final String SAVEMODE_SAVE="SAVE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);
        mText1=(EditText)findViewById(R.id.edittext1);
        mText2=(EditText)findViewById(R.id.edittext2);
        init();
    }
    private void init(){
        Intent intent=getIntent();//这样不会销毁上一个活动
        mEditMode=intent.getStringExtra("mode");
        mSaveMode="";
        if (mEditMode.equals("update")){//编辑模式为更新
            try{//如果数据库出错，那么mUpdateId可能为0，所以需要添加一个try-catch
                mUpdateId=intent.getIntExtra("id",0);
                mNote= DataSupport.find(Note.class,mUpdateId);
                mText1.setText(mNote.getTitle());
                mText2.setText(mNote.getContent());
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
    private void newNote(){
        mNote=new Note();
        String title=mText1.getText().toString();
        String content=mText2.getText().toString();
        if (title.equals("")&&content.equals("")){//标题内容均为空，没东西保存
            return;

        }
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式化，即设置日期格式
        mNote.setTitle(mText1.getText().toString());//标题
        mNote.setContent(mText2.getText().toString());
        mNote.setDate(df.format(new Date()));//API27版本的util里的Date函数
        mNote.save();
    }
    private void updateNote(int id){
        ContentValues contentValues=new ContentValues();
        contentValues.put("title",mText1.getText().toString());
        contentValues.put("content",mText2.getText().toString());
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        contentValues.put("date",df.format(new Date()));
        DataSupport.update(Note.class,contentValues,id);
        if (mText1.getText().toString().equals("")&&mText2.getText().toString().equals("")){
            DataSupport.delete(Note.class,id);//删掉那些标题内容都为空的
            mIsSaveDefault=false;
        }
    }
    private  void deleteNote(int id){
        if (mUpdateId!=-1){
            DataSupport.delete(Note.class,id);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mSaveMode.equals(SAVEMODE_DELETE)){
            if (mSaveMode.equals(SAVEMODE_SAVE)){
                mIsSaveDefault=false;
            }
            switch (mEditMode){
                case "new":
                    newNote();
                    break;
                case "update":
                    updateNote(mUpdateId);
                    break;
                    default:
                        break;
            }
            if (mIsSaveDefault){
                Toast.makeText(NoteEdit.this,"默认保存",Toast.LENGTH_SHORT).show();
            }
        }else {
            deleteNote(mUpdateId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_edit,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.save){
            mSaveMode=SAVEMODE_SAVE;
            finish();
            return  true;
        }
        if (id==R.id.menuitem_delete){
            mSaveMode=SAVEMODE_DELETE;
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
