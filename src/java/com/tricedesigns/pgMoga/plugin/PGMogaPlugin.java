package com.tricedesigns.pgMoga.plugin;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.bda.controller.Controller;
import com.bda.controller.ControllerListener;
import com.bda.controller.KeyEvent;
import com.bda.controller.MotionEvent;
import com.bda.controller.StateEvent;

import android.app.Activity;
import android.util.Log;

public class PGMogaPlugin extends CordovaPlugin {

	Controller mController = null;
	final ExampleControllerListener mListener = new ExampleControllerListener();
	final ExamplePlayer mPlayer = new ExamplePlayer();
	boolean hasUpdate = false;
	
	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        
		if (action.equals("init")) {
			Log.d("PGMogaPlugin", "INIT");
			Activity activity = cordova.getActivity();
			mController = Controller.getInstance( activity );
			mController.init();
			mController.setListener(mListener, null);
			mController.onResume();
			
			int initialDelay = 0; 
			int period = (1000/45);       
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
			  public void run() {
				  writeControllerToWebView();
			  }
			};
			timer.scheduleAtFixedRate(task, initialDelay, period);
            return true;
        }
        return false;
    }
	
	protected void writeControllerToWebView() {
		
		if (!hasUpdate) return;
		
		String script = new StringBuilder()
		    .append("window.PGMoga.update( { ")
		    .append("a: " ).append( mPlayer.mButtonA ).append( ", " )
		    .append("b: " ).append( mPlayer.mButtonB ).append( ", " )
		    
		    .append("axisX: " ).append( mPlayer.mAxisX ).append( ", " )
		    .append("axisY: " ).append( mPlayer.mAxisY ).append( ", " )
		    .append("axisZ: " ).append( mPlayer.mAxisZ ).append( ", " )
		    .append("axisRZ: " ).append( mPlayer.mAxisRZ )
		    
		    .append(" } )")
		    .toString();
		    
		this.webView.sendJavascript( script );
		hasUpdate = false;
	}
    
    @Override
    public void onDestroy() {
		mController.exit();
		super.onDestroy();
    }
    
	@Override
	public void onPause(boolean multitasking) {
		mController.onPause();
		super.onPause(multitasking);
	}

	@Override
	public void onResume(boolean multitasking) {
		super.onResume(multitasking);
		this.resumeListener();
	}
	
	protected void resumeListener() {
		mController.onResume();

		mPlayer.mConnection = mController.getState(Controller.STATE_CONNECTION);
		mPlayer.mControllerVersion = mController.getState(Controller.STATE_CURRENT_PRODUCT_VERSION); // Get
																										// the
																										// Controller
																										// Version
		mPlayer.mButtonA = mController.getKeyCode(Controller.KEYCODE_BUTTON_A);
		mPlayer.mButtonB = mController.getKeyCode(Controller.KEYCODE_BUTTON_B);
		mPlayer.mButtonStart = mController.getKeyCode(Controller.KEYCODE_BUTTON_START);
		mPlayer.mAxisX = mController.getAxisValue(Controller.AXIS_X);
		mPlayer.mAxisY = mController.getAxisValue(Controller.AXIS_Y);
		mPlayer.mAxisZ = mController.getAxisValue(Controller.AXIS_Z);
		mPlayer.mAxisRZ = mController.getAxisValue(Controller.AXIS_RZ);
		
		hasUpdate = true;
		//writeControllerToWebView();
	}
    

	class ExampleControllerListener implements ControllerListener {

		@Override
		public void onKeyEvent(KeyEvent event) {

			switch (mController.getState(Controller.STATE_CURRENT_PRODUCT_VERSION)) {
			case Controller.ACTION_VERSION_MOGA:
				switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_BUTTON_A:
					mPlayer.mButtonA = event.getAction();
					break;

				case KeyEvent.KEYCODE_BUTTON_B:
					mPlayer.mButtonB = event.getAction();
					break;

				case KeyEvent.KEYCODE_BUTTON_START:
					mPlayer.mButtonStart = event.getAction();
					break;
				}
				break;
			case Controller.ACTION_VERSION_MOGAPRO:
				switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_BUTTON_X:
					mPlayer.mButtonA = event.getAction();
					break;

				case KeyEvent.KEYCODE_BUTTON_Y:
					mPlayer.mButtonB = event.getAction();
					break;

				case KeyEvent.KEYCODE_BUTTON_START:
					mPlayer.mButtonStart = event.getAction();
					break;
				}
			}
			//writeControllerToWebView();
			hasUpdate = true;
		}

		@Override
		public void onMotionEvent(MotionEvent event) {
			mPlayer.mAxisX = event.getAxisValue(MotionEvent.AXIS_X);
			mPlayer.mAxisY = event.getAxisValue(MotionEvent.AXIS_Y);
			mPlayer.mAxisZ = event.getAxisValue(MotionEvent.AXIS_Z);
			mPlayer.mAxisRZ = event.getAxisValue(MotionEvent.AXIS_RZ);
			//writeControllerToWebView();
			hasUpdate = true;
		}

		@Override
		public void onStateEvent(StateEvent event) {
			switch (event.getState()) {
			case StateEvent.STATE_CONNECTION:
				mPlayer.mConnection = event.getAction();
				break;
			case StateEvent.STATE_CURRENT_PRODUCT_VERSION:
				mPlayer.mControllerVersion = event.getAction();
				break;
			}

			//writeControllerToWebView();
			hasUpdate = true;
		}
	}

	class ExamplePlayer {
		static final float DEFAULT_SCALE = 4.0f;
		static final float DEFAULT_X = 0.0f;
		static final float DEFAULT_Y = 0.0f;

		boolean gotPadVersion = false;

		int mConnection = StateEvent.ACTION_DISCONNECTED;
		int mControllerVersion = StateEvent.STATE_UNKNOWN;
		int mButtonA = KeyEvent.ACTION_UP;
		int mButtonB = KeyEvent.ACTION_UP;
		int mButtonStart = KeyEvent.ACTION_UP;
		float mAxisX = 0.0f;
		float mAxisY = 0.0f;
		float mAxisZ = 0.0f;
		float mAxisRZ = 0.0f;
	}
}
