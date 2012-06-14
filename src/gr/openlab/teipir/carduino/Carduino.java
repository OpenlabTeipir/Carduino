/* Carduino v1.0 
 * Openlab {
 * 	Site: openlab.teipir.gr
 * 	email: admin@openlab.teipir.gr 
 * }
 * Author: Evangelos Avgerinos  
 * email: avgerinos@ieee.org
 * 
 * Requirements:
 * 1. Android v2.3.3 or greater
 * 2. Bluetooth
 * 3. Accelerometer
 * 4. Amarino application
 * 
 * This is an android application made for the project Carduino.
 * It uses a device that runs android as a remote control for the a toy car.
 * Communication is established with bluetooth. For the connection and the data
 * transfer we use Amarino libraries and Amarino application.
 * There are two buttons, one to make the car go forward (FW) and one to make
 * the car go backward (BW). Accelerometer plays the role of the steering wheel.
 */
package gr.openlab.teipir.carduino;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import at.abraxas.amarino.Amarino;

public class Carduino extends Activity implements SensorEventListener{
   
	private String DEVICE_ADDRESS=""; //Your Bluetooth MAC Address here
	public Button fw,bw;
	private double sensorY;
	private boolean f = false, b = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.carduino);
        fw = (Button) findViewById(R.id.fw);
        bw = (Button) findViewById(R.id.bw);
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0){
        	Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        	sm.registerListener(this,s,SensorManager.SENSOR_DELAY_GAME);
        }
        Amarino.connect(Carduino.this, DEVICE_ADDRESS);
                
        //Setting the touch listener for FW button.
        fw.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent e) {
				switch(e.getAction()){
				case MotionEvent.ACTION_DOWN:{
					Amarino.sendDataToArduino(Carduino.this, DEVICE_ADDRESS, '#', '8');
					f=true;
					return true; 
					}
				case MotionEvent.ACTION_UP: {
					Amarino.sendDataToArduino(Carduino.this, DEVICE_ADDRESS, '#', '5');
					f=false;
					return false; 
					}
				}
				return false;
			}
		});
        
        //Setting the touch listener for BW button.
        bw.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent e) {
				switch(e.getAction()){
				case MotionEvent.ACTION_DOWN:{ 
					Amarino.sendDataToArduino(Carduino.this, DEVICE_ADDRESS, '#', '2');
					b=true;
					return true;
					}
				case MotionEvent.ACTION_UP: {
					Amarino.sendDataToArduino(Carduino.this, DEVICE_ADDRESS, '#', '5');
					b=false;
					return false;
					}
				}
				return false;
			}
		});
    }
	
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Nothing to do here
		
	}

	public void onSensorChanged(SensorEvent event) {
		
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			sensorY = event.values[1];
		}
		
		if(f){
			if(sensorY > 0.900){
				Amarino.sendDataToArduino(Carduino.this, DEVICE_ADDRESS, '#', '9');
			}
			else if(sensorY < -0.900){
				Amarino.sendDataToArduino(Carduino.this, DEVICE_ADDRESS, '#', '7');
			}
			else{
				Amarino.sendDataToArduino(Carduino.this, DEVICE_ADDRESS, '#', '8');
			}
		}
		
		if(b){
			if(sensorY > 0.900){
				Amarino.sendDataToArduino(Carduino.this, DEVICE_ADDRESS, '#', '3');
			}
			else if(sensorY < -0.900){
				Amarino.sendDataToArduino(Carduino.this, DEVICE_ADDRESS, '#', '1');
			}
			else{
				Amarino.sendDataToArduino(Carduino.this, DEVICE_ADDRESS, '#', '2');
			}
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Amarino.disconnect(Carduino.this, DEVICE_ADDRESS);
		finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Amarino.disconnect(Carduino.this, DEVICE_ADDRESS);
		finish();
	}


    
    
}