package theindex.android.taxi_test.android.textModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;

import theindex.android.taxi_test.R;
import theindex.android.taxi_test.android.CustomItemizedOverlay;
import theindex.android.taxi_test.android.baidu.httpRequestBuilder;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.OverlayItem;

public class taxiMapView extends MapActivity {

	private BMapManager mapManager;
	private MapView mapView;
	private MapController mapController;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.taxionmap);
		// 初始化MapActivity
		mapManager = new BMapManager(getApplication());
		// init方法的第一个参数需填入申请的API Key
		mapManager.init("016BC50B77BDCA523AA871ED77C70E03D399B31C", null);
		super.initMapActivity(mapManager);

		mapView = (MapView) findViewById(R.id.map_View);
		// 设置地图模式为交通地图
		mapView.setTraffic(true);
		// 设置启用内置的缩放控件
		mapView.setBuiltInZoomControls(true);

		// 用给定的经纬度构造一个GeoPoint（纬度，经度）
		// GeoPoint point = new GeoPoint((int) (47.118440 * 1E6), (int)
		// (87.493147 * 1E6));
		GeoPoint point = new GeoPoint((int) (39.913061 * 1E6),
				(int) (116.452582 * 1E6));
		// 创建标记maker
		// 取得地图控制器对象，用于控制MapView
		mapController = mapView.getController();
		// 设置地图的中心
		mapController.setCenter(point);
		// 设置地图默认的缩放级别
		mapController.setZoom(12);

		mapOverlays = mapView.getOverlays();

		progressDialog = new ProgressDialog(this);

		MapItemInit();
		UpdateTaxiPosition();

	}

	ProgressDialog progressDialog;
	private CustomItemizedOverlay midderoverlayTaxiCar;

	public void MapItemInit() {
		Resources r = getApplicationContext().getResources();
		InputStream is = r.openRawResource(R.drawable.taxi);
		BitmapDrawable midderMarkerStart = new BitmapDrawable(is);
		midderMarkerStart.setAntiAlias(true);

		midderMarkerStart.setBounds(0, 0,
				midderMarkerStart.getIntrinsicWidth(), midderMarkerStart
						.getIntrinsicHeight());
		midderoverlayTaxiCar = new CustomItemizedOverlay(midderMarkerStart,
				this);
	}

	final Handler msgHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			super.handleMessage(msg);
		}
	};

	final int UPDATE_TIME = 5000; // 1000ms
	final int FIRST_REQUEST_DELAY = 1000; // 1000ms
	private List<Overlay> mapOverlays;
	
	
	class MyHandler2 extends Handler {

		public MyHandler2(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			}
	}
	
	
	class MyHandler extends Handler {

		public MyHandler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
//			progressDialog = ProgressDialog.show(taxiMapView.this, "查询",
//					"网络通信中...");

			String responsString = requestLatLonByID();
			
//			new Thread() {
//				public void run() {
//					{
//						Looper looper = Looper.getMainLooper();
//						MyHandler2 msghandler = new MyHandler2(looper);
//
//					
//						Message msg = msghandler.obtainMessage(1, 1, 1, "其他线程发消息了");
//						msghandler.sendMessage(msg);
//
//					}
//				}
//			}.start();

			String lngString = responsString.substring(responsString
					.indexOf("<lng>") + 5, responsString.indexOf("</lng>"));
			String latString = responsString.substring(responsString
					.indexOf("<lat>") + 5, responsString.indexOf("</lat>"));

			double dLng = Double.parseDouble(lngString);
			double dLat = Double.parseDouble(latString);
			int iLng = (int) (dLng * 1000000);
			int iLat = (int) (dLat * 1000000);
			GeoPoint point = new GeoPoint(iLat, iLng);

			if (point != null) {
				removeLayItems(midderoverlayTaxiCar);
				if (mapOverlays != null) {
					mapOverlays.remove(midderoverlayTaxiCar);
				}
				addOverLayItems(point.getLatitudeE6(), point.getLongitudeE6(),
						midderoverlayTaxiCar);
				mapController.setCenter(point);
			}

		}
	}

	public void addOverLayItems(int lat, int lon, CustomItemizedOverlay overlay) {

		GeoPoint point = new GeoPoint((int) lat, (int) lon);
		Log.w("huangzf", lat + " : " + lon);
		OverlayItem overlayItem = null;

		overlayItem = new OverlayItem(point, "接受您预定的出租车当前位置", "出租车位置");
		overlay.addOverlay(overlayItem);
		/**
		 * 往地图上添加自定义的ItemizedOverlay
		 */

		mapOverlays.add(overlay);

	}

	public void removeLayItems(CustomItemizedOverlay overlay) {
		overlay.removeAll();
	}

	public String requestLatLonByID() {
		httpRequestBuilder builder = new httpRequestBuilder();
		String a = "13501341544080801";
		HttpResponse result = builder.RequestTaxiDataByID(a);
		StringBuilder total = null;
		InputStream iStream = null;
		try {
			iStream = result.getEntity().getContent();
			total = httpRequestBuilder.inputStreamToString(iStream);

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(), total.toString(),
				Toast.LENGTH_SHORT).show();
		return total.toString();

	}

	public GeoPoint getGeopointFromXML(String string) {
		GeoPoint point = new GeoPoint(0, 0);

		return point;
	}

	Timer mTimer;
	private Handler handler;

	public void UpdateTaxiPosition() {
		mTimer = new Timer();

		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Looper looper = Looper.getMainLooper(); // 主线程的Looper对象
				// 这里以主线程的Looper对象创建了handler，
				// 所以，这个handler发送的Message会被传递给主线程的MessageQueue。
				handler = new MyHandler(looper);

				// 构建Message对象
				// 第一个参数：是自己指定的message代号，方便在handler选择性地接收
				// 第二三个参数没有什么意义
				// 第四个参数需要封装的对象
				Message msg = handler.obtainMessage(1, 1, 1, "其他线程发消息了");
				handler.sendMessage(msg); // 发送消息
			}
		}, FIRST_REQUEST_DELAY, UPDATE_TIME);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onDestroy() {
		if (mapManager != null) {
			mapManager.destroy();
			mapManager = null;
		}
		mTimer.cancel();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mapManager != null) {
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mapManager != null) {
			mapManager.start();
		}

		super.onResume();
	}

}
