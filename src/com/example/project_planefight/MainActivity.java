package com.example.project_planefight;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends Activity {

	MediaPlayer backMusic;
	FightView view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		DisplayMetrics metrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int w=metrics.widthPixels;
		int h=metrics.heightPixels;
		view=new FightView(this,w,h);
		setContentView(view);
		view.bomb=MediaPlayer.create(this, R.raw.shot1);
		backMusic=MediaPlayer.create(this,R.raw.backmusic);
		
		backMusic.start();
		backMusic.setLooping(true);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		view.bomb=null;
		backMusic.stop();
	}

	
}
