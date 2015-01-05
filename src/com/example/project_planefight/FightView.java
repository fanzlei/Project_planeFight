package com.example.project_planefight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class FightView extends View{

	Bitmap back_img,back;
	Bitmap enemy_bitmap;
	int tableWidth,tableHeigth;
	int backIMG_heigth=2000;
	int cutH;
	Bitmap plane;
	int plane_x,plane_y;
	int touchX,touchY;
	List<Enemy> enemys;
	Random random;
	int enemy_x_move=3;
	Paint paint,paint2;
	List<Point> myShot;
	List<Point> enemyShot;
	MediaPlayer bomb;
	boolean isLost=false;
	Timer timer1,timer2,timer3,timer4;
	int enemy_destroyed_num=0;
	Paint paint3;
	public FightView(Context context,int w,int h) {
		super(context);
		// TODO Auto-generated constructor stub
		back_img=BitmapFactory.decodeResource(context.getResources(), R.drawable.back_img);
		plane=BitmapFactory.decodeResource(getResources(), R.drawable.plane);
		enemy_bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
		bomb=MediaPlayer.create(getContext(), R.raw.shot1);
		tableWidth=w;
		tableHeigth=h;
		plane_x=(int) (tableWidth*0.5);
		plane_y=tableHeigth-180;
		cutH=backIMG_heigth-tableHeigth;
		enemys=new ArrayList<Enemy>();
		random=new Random();
		paint=new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		paint2=new Paint();
		paint2.setColor(Color.RED);
		paint2.setAntiAlias(true);
		myShot=new ArrayList<Point>();
		enemyShot=new ArrayList<Point>();
		paint3=new Paint();
		paint3.setStrokeWidth(3);
		paint3.setColor(Color.CYAN);
		paint3.setAntiAlias(true);
		paint3.setTextSize(30);
	    
		final Handler handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what==0x123){
					invalidate();
				}
				
			}
			
		};
		//背景移动,敌机移动定时器
		timer1=new Timer();
		timer1.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(cutH<=3){
					cutH=backIMG_heigth;
				}
				cutH-=3;
				//敌机移动
				for(int i=0;i<enemys.size();i++){
					enemys.get(i).setY();
					enemys.get(i).setX(enemy_x_move);
					enemy_x_move=-enemy_x_move;
				}
				//我方子弹移动
				for(int i=0;i<myShot.size();i++){
					myShot.get(i).y-=30;
				}
				//敌人子弹移动
				for(int i=0;i<enemyShot.size();i++){
					if(enemyShot.get(i).y>tableHeigth){
						
						enemyShot.get(i).y=enemys.get(i).getY()+40;
					}
					enemyShot.get(i).y+=30;
				}
				//敌机是否被打中
				for(int i=0;i<myShot.size();i++){
					for(int j=0;j<enemys.size();j++){
						if(myShot.get(i).x>enemys.get(j).getX()&&
								myShot.get(i).x<enemys.get(j).getX()+50&&
								myShot.get(i).y<enemys.get(j).getY()+40){
							bomb.start();
							//anim.start();
							enemys.remove(j);
							enemy_destroyed_num++;
							//这里要移除子弹，否则出现敌人没有了，子弹还在，使得，子弹回到敌机是出现超出数组范围情况
							enemyShot.remove(j);
						}
					}
				}
				//判断自己是否被打中
				
				for(int i=0;i<enemyShot.size();i++){
					
					if(enemyShot.get(i).x>plane_x&&
							enemyShot.get(i).x<plane_x+35&&
							enemyShot.get(i).y>=plane_y&&
							enemyShot.get(i).y<plane_y+45){
						isLost=true;
					}
				}
				handler.sendEmptyMessage(0x123);
				
			}}, 0, 100);
		//敌机出现定时器
		timer2=new Timer();
		timer2.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Enemy enemy=new Enemy(tableWidth);
				enemys.add(enemy);
				enemyShot.add(new Point(enemy.getX()+34,enemy.getY()));
				for(int i=0;i<enemys.size();i++){
					if(enemys.get(i).getY()>tableHeigth-40){
						enemys.remove(i);
						enemyShot.remove(i);
					}
				}
				handler.sendEmptyMessage(0x123);
			}}, 0, 2000);
		//子弹发射定时器
		timer3=new Timer();
		timer3.schedule(new TimerTask(){

			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				myShot.add(new Point(plane_x+34,plane_y));
				
				
			}}, 0, 300);
		this.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, final MotionEvent event) {
				// TODO Auto-generated method stub
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				touchX=(int) event.getX();
				touchY=(int) event.getY();
			}
			if(event.getAction()==MotionEvent.ACTION_MOVE){
				plane_x+=event.getX()-touchX;
				plane_y+=event.getY()-touchY;
				touchX=(int) event.getX();
				touchY=(int) event.getY();
			}
			
			
			
				return true;
			}});
	}

	
	
	



	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		back=Bitmap.createBitmap(back_img, 0, cutH, tableWidth, tableHeigth);
		canvas.drawBitmap(back, 0, 0, null);
		if(!isLost){
			canvas.drawBitmap(plane, plane_x, plane_y, null);
			for(int i=0;i<enemys.size();i++){
				canvas.drawBitmap(enemy_bitmap,enemys.get(i).getX() , enemys.get(i).getY(), null);
			}
			
			for(int i=0;i<myShot.size();i++){
				if(myShot.get(i).y<0){myShot.remove(i);}
				canvas.drawCircle(myShot.get(i).x,myShot.get(i).y , 5, paint);
				
			}
			for(int i=0;i<enemyShot.size();i++){
				canvas.drawCircle(enemyShot.get(i).x, enemyShot.get(i).y, 3, paint2);
			}
			canvas.drawText("摧毁敌机："+enemy_destroyed_num, tableWidth-230, 70, paint3);
		}
		else{
			MediaPlayer over=MediaPlayer.create(getContext(), R.raw.gameover);
			over.start();
			Paint paint3=new Paint();
			paint3.setStrokeWidth(5);
			paint3.setColor(Color.RED);
			paint3.setAntiAlias(true);
			paint3.setTextSize(50);
			canvas.drawText("GAME OVER\n你被打死了", 150, 400, paint3);
			canvas.drawText("摧毁敌机："+enemy_destroyed_num, (int)(tableWidth*0.5), 70, paint3);
			timer1.cancel();
			timer2.cancel();
			timer3.cancel();
			
		}
	}
	
}
