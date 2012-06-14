package gr.openlab.teipir.carduino;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		Thread th = new Thread(){
			public void run(){
				try{
					sleep(3000);
				}
				catch(InterruptedException e){
					Toast.makeText(Splash.this, "Thread interrupted.", Toast.LENGTH_SHORT).show();
					e.printStackTrace(); 
				}
				finally{
					Intent w = new Intent("gr.openlab.teipir.carduino.CARDUINO"); 
					startActivity(w);
				}
			}
		};
		th.start(); 
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	
	

}
