package theindex.android.taxi_test.android.baidu;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class AlarmService extends Service {

	private static final String TAG = "MyService";
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.v(TAG, "onCreate");
		Toast.makeText(getApplicationContext(), "service create",
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onDestroy() {
		Log.v(TAG, "onDestroy");
		Toast.makeText(getApplicationContext(), "service destroy",
				Toast.LENGTH_SHORT).show();
		mTimer.cancel();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.v(TAG, "onStart");
		Toast.makeText(getApplicationContext(), "service start",
				Toast.LENGTH_SHORT).show();
		startAlarm();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {

				int op = bundle.getInt("op");
				switch (op) {
				case 1:
					play();
					
					break;
				case 2:
					stop();
					break;
				case 3:
					pause();
					break;
					default:
						break;
				}

			}
		}

	}
	private Handler handler;
	class MyHandler extends Handler {

		public MyHandler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			
			Toast.makeText(getApplicationContext(), "A Warning !!!!!@@@@",
					Toast.LENGTH_SHORT).show();
		}
	}
	Timer mTimer = new Timer();;
	public void startAlarm()
	{
		
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Looper looper = Looper.getMainLooper(); // 主线程的Looper对象
			    handler = new MyHandler(looper);
			    Message msg = handler.obtainMessage(1, 1, 1, "其他线程发消息了");
				handler.sendMessage(msg); // 发送消息
			}
		},5000,3000);  // start 5 seconds, then every 3 seconds repeat the action
	}
	public void play() {

	}

	public void pause() {

	}

	public void stop() {

	}

}