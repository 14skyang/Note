package com.ysk.note;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    public int isday(int month,int year) {
        int days;
        int flag=0;
        if(month==1||month==3||month==5||month==7
                ||month==8||month==10||month==12)
        {
            days=31;
            flag= 1;
        }
        if(month==4||month==6||month==9||month==11)
        {
            days=30;
            flag= 2;
        }
        if(month==2)
        {
            if(((year%4==0)&&(year%100!=0))||(year%400==0))
            {
                days=29;
                flag= 3;
            }

            else
            {
                days=28;
                flag= 4;
            }
        }
        return flag;

    }
    float x1 = 0;
    float x=0;
    float y=0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    int year=2018;
    int month=6;
    Calendar calendar=Calendar.getInstance();
    final int momth0=calendar.get(Calendar.MONTH)+1;
    final int year0=calendar.get(Calendar.YEAR);
    final int day0=calendar.get(Calendar.DAY_OF_MONTH);
    int a=5;
    int d=1;
    int f=1;
    int b=35;
    String day[]=new String[42];
    int[] xx=new int[100];
    int[] ss=new int[100];
    TextView lableday[]=new TextView[42];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xx[0]=0;
        ss[0]=0;
        String name[]={"日","一","二","三", "四","五","六"};
        setContentView(R.layout.activity_calendar);
        GridLayout gridLayout=(GridLayout)findViewById(R.id.g1);
        int g=a;
        TextView textView2=(TextView)findViewById(R.id.t1);
        textView2.setTextSize(25);
        for(int i=0;i<7;i++)
        {
            TextView textView1=new TextView(this);
            textView1.setText(name[i]);
            textView1.setTextSize(30);
            textView1.setTextColor(0xFF3041FF);
            textView1.setPadding(20,20,20,20);
            gridLayout.addView(textView1);
        }
        for(int i=0;i<42;i++)
        {
            lableday[i]=new TextView(this);
            lableday[i].setText(" ");
            lableday[i].setTextColor(0xFF666666);
            gridLayout.addView(lableday[i]);
        }
        for(int i=a;i<b;i++)
        {
            if(i==a)
            {
                if(a<0)
                {
                    a=7;
                }
            }
            day[i]=String.valueOf(i-g+1);
            lableday[i].setText(day[i]);

            lableday[i].setTextSize(30);
            lableday[i].setPadding(20,20,20,20);

        }
        final Button bn1=(Button)findViewById(R.id.b1);
        final Button bn2=(Button)findViewById(R.id.b2);
        bn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int plag = 0;
                month=month-1;
                if(month<1)
                {
                    month=12;
                    year=year-1;
                }
                if(isday(month, year)==1)
                {
                    plag=31;
                }
                if(isday(month, year)==2)
                {
                    plag=30;
                }
                if(isday(month, year)==3)
                {
                    plag=29;
                }
                if(isday(month, year)==4)
                {
                    plag=28;
                }
                b=27+a;
                a=b-plag+1;
                if(a<0)
                {
                    b=b+7;
                    a=b-plag+1;
                }
                int k=a;
                for(int i=0;i<42;i++)
                {
                    lableday[i].setText(" ");
                }
                for(int i=a;i<=b;i++)
                {
                    day[i]=String.valueOf(i-k+1);
                    if(year==year0&&month==momth0&&i-k+1==day0)
                    {
                        lableday[i].setTextColor(0xFF3021FF);
                    }else
                    {
                        lableday[i].setTextColor(0xFF666666);
                    }
                    lableday[i].setText(day[i]);
                    if(i==a)
                    {
                        if(a<0)
                        {
                            a=6;
                        }
                    }
                    if(i==b)
                    {
                        b=(b+1)%7;
                    }
                }
                TextView textView2=(TextView)findViewById(R.id.t1);
                textView2.setText(String.valueOf(year)+"年"+String.valueOf(month)+"月");
                TranslateAnimation translateAnimation = new TranslateAnimation(0,500,0,0);
                translateAnimation.setDuration(300);
                CardView cardView=(CardView)findViewById(R.id.cv_item2);
                cardView.startAnimation(translateAnimation);
            }
        });
        bn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int plag = 0;
                GridLayout gridLayout=(GridLayout)findViewById(R.id.g1);
                month=month+1;
                if(month>12)
                {
                    month=1;
                    year=year+1;
                }
                if(isday(month, year)==1)
                {
                    plag=31;
                }
                if(isday(month, year)==2)
                {
                    plag=30;
                }
                if(isday(month, year)==3)
                {
                    plag=29;
                }
                if(isday(month, year)==4)
                {
                    plag = 28;
                }
                a=(b)%7;
                b=a+plag;
                int k=a;
                for(int i=0;i<42;i++)
                {
                    lableday[i].setText(" ");

                }
                for(int i=a;i<b;i++)
                {
                    day[i]=String.valueOf(i-k+1);
                    if(year==year0&&month==momth0&&i-k+1==day0)
                    {
                        lableday[i].setTextColor(0xFF3021FF);
                    }else
                    {
                        lableday[i].setTextColor(0xFF666666);
                    }
                    lableday[i].setText(day[i]);
                    lableday[i].setText(day[i]);
                    lableday[i].setTextSize(30);
                    lableday[i].setPadding(20,20,20,20);
                    if(i==a)
                    {
                        if(a<0)
                        {
                            a=6;
                        }
                    }
                }
                TextView textView2=(TextView)findViewById(R.id.t1);
                textView2.setText(String.valueOf(year)+"年"+String.valueOf(month)+"月");
                TranslateAnimation translateAnimation = new TranslateAnimation(0,-500,0,0);
                translateAnimation.setDuration(300);
                CardView cardView=(CardView)findViewById(R.id.cv_item2);
                cardView.startAnimation(translateAnimation);
            }
        });
        int q=(year0-year)*12;
        int p=q+momth0-month;
        for(int j=0;j<p;j++)
        {
            bn2.performClick();
        }
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,0);
        translateAnimation.setDuration(300);
        CardView cardView=(CardView)findViewById(R.id.cv_item2);
        cardView.setBackgroundColor(Color.parseColor("#00000000"));
        cardView.startAnimation(translateAnimation);

    }
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if(x1 - x2 > 50) {
                TranslateAnimation translateAnimation = new TranslateAnimation(0,-500,0,0);
                translateAnimation.setDuration(300);
                CardView cardView=(CardView)findViewById(R.id.cv_item2);
                cardView.startAnimation(translateAnimation);
                int plag = 0;
                GridLayout gridLayout=(GridLayout)findViewById(R.id.g1);
                month=month+1;
                if(month>12)
                {
                    month=1;
                    year=year+1;
                }
                if(isday(month, year)==1)
                {
                    plag=31;
                }
                if(isday(month, year)==2)
                {
                    plag=30;
                }
                if(isday(month, year)==3)
                {
                    plag=29;
                }
                if(isday(month, year)==4)
                {
                    plag = 28;
                }
                a=(b)%7;
                b=a+plag;
                int k=a;
                for(int i=0;i<42;i++)
                {
                    lableday[i].setText(" ");

                }
                for(int i=a;i<b;i++)
                {
                    day[i]=String.valueOf(i-k+1);
                    if(year==year0&&month==momth0&&i-k+1==day0)
                    {
                        lableday[i].setTextColor(0xFF3021FF);
                    }else
                    {
                        lableday[i].setTextColor(0xFF666666);
                    }
                    lableday[i].setText(day[i]);
                    lableday[i].setText(day[i]);
                    lableday[i].setTextSize(30);
                    lableday[i].setPadding(20,20,20,20);
                    if(i==a)
                    {
                        if(a<0)
                        {
                            a=6;
                        }
                    }
                }
                TextView textView2=(TextView)findViewById(R.id.t1);
                textView2.setText(String.valueOf(year)+"年"+String.valueOf(month)+"月");
            } else if(x2 - x1 > 50) {
                TranslateAnimation translateAnimation = new TranslateAnimation(0,500,0,0);
                translateAnimation.setDuration(300);
                CardView cardView=(CardView)findViewById(R.id.cv_item2);
                cardView.startAnimation(translateAnimation);
                int plag = 0;
                month=month-1;
                if(month<1)
                {
                    month=12;
                    year=year-1;
                }
                if(isday(month, year)==1)
                {
                    plag=31;
                }
                if(isday(month, year)==2)
                {
                    plag=30;
                }
                if(isday(month, year)==3)
                {
                    plag=29;
                }
                if(isday(month, year)==4)
                {
                    plag=28;
                }
                b=27+a;
                a=b-plag+1;
                if(a<0)
                {
                    b=b+7;
                    a=b-plag+1;
                }
                int k=a;
                for(int i=0;i<42;i++)
                {
                    lableday[i].setText(" ");
                }
                for(int i=a;i<=b;i++)
                {
                    day[i]=String.valueOf(i-k+1);
                    if(year==year0&&month==momth0&&i-k+1==day0)
                    {
                        lableday[i].setTextColor(0xFF3021FF);
                    }else
                    {
                        lableday[i].setTextColor(0xFF666666);
                    }
                    lableday[i].setText(day[i]);
                    if(i==a)
                    {
                        if(a<0)
                        {
                            a=6;
                        }
                    }
                    if(i==b)
                    {
                        b=(b+1)%7;
                    }
                }
                TextView textView2=(TextView)findViewById(R.id.t1);
                textView2.setText(String.valueOf(year)+"年"+String.valueOf(month)+"月");
            }
        }
        return super.onTouchEvent(event);
    }
}

