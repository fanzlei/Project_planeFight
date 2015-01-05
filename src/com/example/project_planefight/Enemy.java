package com.example.project_planefight;

import java.util.Random;

public class Enemy {

	private int enemy_bitmap_width=30;
	private int enemy_bitmap_heigth=40;
	private int x,y;
	Random random;
	private int shotX,shotY;//×Óµ¯Î»ÖÃ
	public Enemy(int screenWidth){
		random=new Random();
		x=random.nextInt(screenWidth-enemy_bitmap_width);
		y=0;
		
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setX(int move){
		x+=move;
	}
	public void setY(){
		y+=3;
	}
	public void shot(){
		shotX=(int) (x+enemy_bitmap_width*0.5);
		shotY=y+enemy_bitmap_heigth;
	}
	public void setShotY(){
		shotY+=3;
	}
}
