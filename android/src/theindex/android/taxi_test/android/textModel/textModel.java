package theindex.android.taxi_test.android.textModel;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

import theindex.android.taxi_test.R;
import theindex.android.taxi_test.android.taxi_testApplication;
import theindex.android.taxi_test.android.baidu.httpRequestBuilder;
import theindex.android.taxi_test.android.baidu.taxi_test;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;

public class textModel extends MapActivity implements LocationListener {

	private BMapManager mapManager;
	private MapView mapView;
	private MapController mapController;
	private MKSearch mMKSearch;
	private MKLocationManager mLocationManager = null;

	private EditText editText1; // passager name

	private EditText editText2; // Start string
	private EditText editText3; // dest string
	private EditText editText4; // phone nubmer

	private String startString = null;
	private String destString = null;
	private String phoneNumberString = null;

	private String startLatLon = null;
	private String destLatlon = null;

	private TextView distanceAndPrice;

	private Button ApplyRequestButton;
	private Button serviceExitButton;
	private Spinner gender;

	private Spinner startSpinner;
	private Spinner destSpinner;

	private ArrayAdapter<String> adapterGender;
	private FrameLayout mapLayout;

	Button startSearchButton;
	Button destSearchButton;

	StringBuffer GeoString = null;
	StringBuffer LatLonString = null;

	private Handler SearchResulthandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textmodelview);

		mapInit();
		textModelUIinit();

	}

	public void mapInit() {
		mapManager = new BMapManager(getApplication());
		mapManager.init("016BC50B77BDCA523AA871ED77C70E03D399B31C",
				new MKGeneralListener() {
					@Override
					public void onGetPermissionState(int arg0) {
						Toast.makeText(getApplicationContext(),
								"Permission problem", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onGetNetworkState(int arg0) {
						Toast.makeText(getApplicationContext(),
								"Network problem", Toast.LENGTH_LONG).show();
					}
				});
		super.initMapActivity(mapManager);

		mapView = (MapView) findViewById(R.id.textmodelmapview);
		mMKSearch = new MKSearch();
		// guanghua road: 39.913061,116.452582 //
		GeoPoint point = new GeoPoint((int) (39.913061 * 1E6),
				(int) (116.452582 * 1E6));
		mMKSearch.init(mapManager, new Mysearchlistener());
		mapController = mapView.getController();
		mapController.setCenter(point);
		// 设置地图的缩放级别。 这个值的取值范围是[3,18]。
		mapController.setZoom(16);

		mLocationManager = mapManager.getLocationManager();
		// 注册位置更新事件
		mLocationManager.requestLocationUpdates(this);
		// 使用GPS定位
		mLocationManager
				.enableProvider((int) MKLocationManager.MK_GPS_PROVIDER);
	}

	public taxi_testApplication application;

	public String[] historyStrings;
	public Button mapViewButton;

	
	public Button weiboShareButton;
	public Button orderstatusButton;
	

	public void textModelUIinit() {
		application = (taxi_testApplication) this.getApplication();
		historyStrings = application.historyStrings;
		{
			mapLayout = (FrameLayout) findViewById(R.id.textmodelframeLayout1);
			mapLayout.setVisibility(View.GONE);

			// 1 for name
			editText1 = (EditText) findViewById(R.id.textmodeleditText1);
			editText1.setText(application.passageName);

			// 2 for start
			editText2 = (EditText) findViewById(R.id.textmodeleditText2);
			editText2.setOnFocusChangeListener(focusListenerStart);

			// 3 for dest
			editText3 = (EditText) findViewById(R.id.textmodeleditText3);
			editText3.setOnFocusChangeListener(focusListenerDest);

			// 4 for phone number
			editText4 = (EditText) findViewById(R.id.textmodeleditText4);

			distanceAndPrice = (TextView)findViewById(R.id.textmodeltextView4);
			
			ApplyRequestButton = (Button) findViewById(R.id.textmodelapplyButton);
			ApplyRequestButton.setOnClickListener(listenerApplyRequest);
			serviceExitButton = (Button) findViewById(R.id.textmodelserviceExit);
			serviceExitButton.setOnClickListener(listenerAlarmServiceExit);

			startSearchButton = (Button) findViewById(R.id.textmodelbutton1);
			startSearchButton.setOnClickListener(listenerStartSearch);

			destSearchButton = (Button) findViewById(R.id.textmodelbutton2);
			destSearchButton.setOnClickListener(listenerDestSearch);

			mapViewButton = (Button) findViewById(R.id.textmodelGotoMap);
			mapViewButton.setOnClickListener(listenerMapView);

			orderstatusButton = (Button) findViewById(R.id.textmodelOrderStatus);
			orderstatusButton.setOnClickListener(listenerOrderStauts);
			
			weiboShareButton = (Button)findViewById(R.id.weiboShare);
			weiboShareButton.setOnClickListener(listenerweiboShare);
			

		}
		gender = (Spinner) findViewById(R.id.textmodelspinner1);
		{

			// 将可选内容与ArrayAdapter连接起来
			String[] Gendertemps = { "先生", "女士" };
			adapterGender = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, Gendertemps);
			// 设置下拉列表的风格
			adapterGender
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			gender.setAdapter(adapterGender);

			// 添加事件Spinner事件监听
			gender.setOnItemSelectedListener(new SpinnerListenerGender());
			if (application.genderMan) {
				gender.setId(0);
			} else {
				gender.setId(1);
			}

		}
		startSpinner = (Spinner) findViewById(R.id.textmodelStartSpinner);
		{

			ssStart = historyStrings;
			startLatLonPairs = application.historyLatLon;

			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, historyStrings);
			// 设置下拉列表的风格
			adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			startSpinner.setAdapter(adapter);

			// 添加事件Spinner事件监听
			startSpinner
					.setOnItemSelectedListener(new SpinnerListenerStartPoIs());

		}
		destSpinner = (Spinner) findViewById(R.id.textmodespinner2);
		{

			// 将可选内容与ArrayAdapter连接起来
			ssDest = historyStrings;
			destLatLonPairs = application.historyLatLon;
			
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, historyStrings);
			// 设置下拉列表的风格
			adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			destSpinner.setAdapter(adapter);

			// 添加事件Spinner事件监听
			destSpinner
					.setOnItemSelectedListener(new SpinnerListenerDestPoIs());

		}
		{

			if (application.passagePhonenumber == null) {
				TelephonyManager tm = (TelephonyManager) this
						.getSystemService(Context.TELEPHONY_SERVICE);
				String tel = tm.getLine1Number();
				if (tel == null) {
					tel = "请输入手机号码";
					editText4.setTextColor(Color.GRAY);
					editText4.setText(tel);
					editText4.setOnFocusChangeListener(focusListener);

				} else {
					editText4.setText(tel);
					phoneNumberString = tel;

				}

			} else {
				editText4.setText(application.passagePhonenumber);
			}

		}

	}

	private boolean firstTimeFocusChanged = true;
	OnFocusChangeListener focusListener = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (firstTimeFocusChanged) {
					firstTimeFocusChanged = false;
					editText4.setTextColor(Color.BLACK);
					editText4.setText("");
				}
			}
		}
	};

	OnFocusChangeListener focusListenerStart = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				{
					editText2.setText("");
				}
			}
		}
	};
	OnFocusChangeListener focusListenerDest = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				{
					editText3.setText("");
				}
			}
		}
	};

	class SpinnerListenerGender implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// view.setText("你的血型是："+m[arg2]);
			// setTitleStreetName( arg2);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SpinnerListenerDestPoIs implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// view.setText("你的血型是："+m[arg2]);
			// setTitleStreetName( arg2);
			if (ssDest != null) {
				editText3.setText(ssDest[arg2]);
				destString = ssDest[arg2];
				destLatlon = destLatLonPairs[arg2];
			}

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SpinnerListenerStartPoIs implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// view.setText("你的血型是："+m[arg2]);
			// setTitleStreetName( arg2);
			if (ssStart != null) {
				editText2.setText(ssStart[arg2]);
				startString = ssStart[arg2];
				startLatLon = startLatLonPairs[arg2];
			}

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			// 将当前位置转换成地理坐标点
			GeoPoint pt = new GeoPoint(
					(int) (location.getLatitude() * 1000000), (int) (location
							.getLongitude() * 1000000));
			// 将当前位置设置为地图的中心
			// mapController.setCenter(pt);
			// currentGpsPosition = pt;
			mMKSearch.reverseGeocode(pt);
		}

	}

	boolean StartSearchRequestSend = false;
	OnClickListener listenerStartSearch = new OnClickListener() {
		public void onClick(View v) {
			// todo :huangzf: notice the city key words. we may use in many city
			// later
			String keyword = editText2.getText().toString();
			mMKSearch.poiSearchInCity("beijing", keyword);
			StartSearchRequestSend = true;

		}
	};
	boolean DestSearchRequestSend = false;
	OnClickListener listenerDestSearch = new OnClickListener() {
		public void onClick(View v) {
			// todo :huangzf: notice the city key words. we may use in many city
			// later
			String keyword = editText3.getText().toString();
			mMKSearch.poiSearchInCity("beijing", keyword);
			DestSearchRequestSend = true;
		}
	};

	OnClickListener listenerMapView = new OnClickListener() {
		public void onClick(View v) {
			Intent in = new Intent(textModel.this, taxi_test.class);
			startActivity(in);
		}
	};
	
	
	
	OnClickListener listenerweiboShare = new OnClickListener() {
		public void onClick(View v) {
			//	todo huangzf:
//			Intent in = new Intent(textModel.this, AndroidExample.class);
//			startActivity(in);
		}
	};
	OnClickListener listenerOrderStauts = new OnClickListener() {
		public void onClick(View v) {
			Intent in = new Intent(textModel.this, orderStatus.class);
			startActivity(in);
		}
	};
	OnClickListener listenerApplyRequest = new OnClickListener() {
		public void onClick(View v) {
			/*
			 * setContentView(R.layout.main); todo: contact with server to send
			 * the request todo: start a service to monitor the response huangzf
			 * int op 1 : for play 2: for stop 3: for pause for close
			 * this.finish(); 4: exit: stopservice(intent);this.finish();
			 */
			boolean bcheckingResult = inputcheck();
			if (!bcheckingResult) {
				// check result fail
				return;
			}
			routeCompute();
			postRequest();

			// int op = -1;
			// Intent intent = new
			// Intent("theindex.android.taxi_test.android.baidu.AlarmService");
			// Bundle bundle = new Bundle();
			// bundle.putInt("op", op);
			// intent.putExtras(bundle);
			// startService(intent);

		}
	};
	OnClickListener listenerAlarmServiceExit = new OnClickListener() {
		public void onClick(View v) {
			// int op 1 : for play
			// 2: for stop
			// 3: for pause
			// for close this.finish();
			// 4: exit: stopservice(intent);this.finish();
			//int op = -1;
			Intent intent = new Intent(
					"theindex.android.taxi_test.android.baidu.AlarmService");
			// Bundle bundle = new Bundle();
			// bundle.putInt("op", op);
			// intent.putExtras(bundle);
			// startService(intent);
			stopService(intent);
		}
	};

	private boolean inputcheck() {
		String[] warningString = { "", "请输入正确的手机号码", "请输入或者选择正确的起点",
				"请输入或者选择正确的终点" };
		boolean result = true;
		// check if the phoneNumber , start, and dest are right
		// String
		int warningNumber = 0;

		startString = editText2.getText().toString();
		destString = editText3.getText().toString();
		phoneNumberString = editText4.getText().toString();
		if (phoneNumberString.equals("请输入手机号码")
				|| phoneNumberString.length() == 0) {
			phoneNumberString = null;
		}
		if (phoneNumberString == null) {
			warningNumber = 1;
		} else if (startString == null || startString.startsWith("下拉选择地址")) {
			warningNumber = 2;
		} else if (destString == null || destString.startsWith("下拉选择地址")) {
			warningNumber = 3;
		}
		if (warningNumber != 0) {
			result = false;
			Toast.makeText(getApplicationContext(),
					warningString[warningNumber], Toast.LENGTH_SHORT).show();
		}

		return result;
	}

	private void routeCompute()
	{
		Toast.makeText(getApplicationContext(),
				startLatLon+ " " + destLatlon, Toast.LENGTH_SHORT).show();
		MKPlanNode start = new MKPlanNode();
		String[] tempsStrings = startLatLon.split("\n");
		start.pt = new GeoPoint(Integer.parseInt(tempsStrings[0]), Integer.parseInt(tempsStrings[1]));
		
		MKPlanNode end = new MKPlanNode();
		tempsStrings = destLatlon.split("\n");
		end.pt = new GeoPoint(Integer.parseInt(tempsStrings[0]), Integer.parseInt(tempsStrings[1]));
		
		
		mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
		mMKSearch.drivingSearch(null, start, null, end);
	}

	private void postRequest() {

		httpRequestBuilder builder = new httpRequestBuilder();
		HttpResponse result = builder.postOrderData(builder);
		StringBuilder total = null;
		try {
			total = httpRequestBuilder.inputStreamToString(result.getEntity().getContent());
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(), total.toString(),
				Toast.LENGTH_SHORT).show();
	}

	private ArrayAdapter<String> adapter;
	private String[] ssStart = null;
	private String[] ssDest = null;

	private String[] startLatLonPairs = null;
	private String[] destLatLonPairs = null;

	public class Mysearchlistener implements MKSearchListener {

		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			if (result == null) {
				Toast.makeText(getApplicationContext(),
						"not street informaiton ", Toast.LENGTH_SHORT).show();
				return;
			}
			GeoString = new StringBuffer();
			LatLonString = new StringBuffer();
			// 经纬度所对应的位置
			GeoString.append(result.strAddr).append("\n\t");

			// 判断该地址附近是否有POI（Point of Interest,即兴趣点）
			if (result.poiList == null) {
				// mMKSearch.poiSearchNearBy("餐厅", point, 500);
				return;
			}
			if (null != result.poiList) {
				// 遍历所有的兴趣点信息
				for (MKPoiInfo poiInfo : result.poiList) {
					GeoString.append(poiInfo.name).append("\n");
					GeoString.append(poiInfo.address).append("\n\t");

					LatLonString.append(poiInfo.pt.getLatitudeE6()).append("\n");
					LatLonString.append(poiInfo.pt.getLongitudeE6()).append(
							"\t");

				}
			}
			// when the GPS changed, auto update the ssStart POI list
			{
				ssStart = GeoString.toString().split("\t");
				startLatLonPairs = LatLonString.toString().split("\t");
				{
					
					ssStart = MergeArray(ssStart,historyStrings);
					startLatLonPairs = MergeArray(startLatLonPairs,application.historyLatLon);

				}
				{
					adapter = new ArrayAdapter<String>(getApplicationContext(),
							android.R.layout.simple_spinner_item, ssStart);
					// 设置下拉列表的风格
					adapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					startSpinner.setAdapter(adapter);

				}
			}

		}

		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
			if (result == null) {
				return;
			}
			int orderDistance = result.getPlan(0).getDistance();
			int orderPrice = calculatePrice(orderDistance);
			distanceAndPrice.setText("距离和估价     	距离:" + orderDistance / 1000.0 + "公里"
					+ "   " + "价格：" + orderPrice + "元");

			Toast.makeText(getApplicationContext(),
					"距离: " + orderDistance + " 价格 : " + orderPrice,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {

			if (result == null) {
				Toast.makeText(getApplicationContext(), "result search empty",
						Toast.LENGTH_SHORT).show();
				return;
			}
			GeoString = new StringBuffer();
			LatLonString = new StringBuffer();
			ArrayList<MKPoiInfo> POIS = result.getAllPoi();
			if (POIS == null) {
				Toast.makeText(getApplicationContext(), "POIs search empty",
						Toast.LENGTH_SHORT).show();
				return;
			}
			for (int i = 0; i < POIS.size(); i++) {
				// GeoString.append("POS return ");
				GeoString.append(POIS.get(i).name).append("\n");
				GeoString.append(POIS.get(i).address).append("\n\t");
				LatLonString.append(POIS.get(i).pt.getLatitudeE6()).append("\n");
				LatLonString.append(POIS.get(i).pt.getLongitudeE6()).append(
						"\t");

			}
			// update the search POI list
			if (StartSearchRequestSend) {
				ssStart = GeoString.toString().split("\t");
				startLatLonPairs = LatLonString.toString().split("\t");
				{
					
					ssStart = MergeArray(ssStart, historyStrings);
					startLatLonPairs = MergeArray(startLatLonPairs, application.historyLatLon);
					startString = ssStart[0];
					

				}
				{
					adapter = new ArrayAdapter<String>(getApplicationContext(),
							android.R.layout.simple_spinner_item, ssStart);
					// 设置下拉列表的风格
					adapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					startSpinner.setAdapter(adapter);

				}
			} else if (DestSearchRequestSend) {
				ssDest = GeoString.toString().split("\t");
				destLatLonPairs = LatLonString.toString().split("\t");
				{
					ssDest = MergeArray(ssDest, historyStrings);
					destLatLonPairs = MergeArray(destLatLonPairs, application.historyLatLon);
					destString = ssDest[0];// record the start address
				}
				{
					adapter = new ArrayAdapter<String>(getApplicationContext(),
							android.R.layout.simple_spinner_item, ssDest);
					// 设置下拉列表的风格
					adapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					destSpinner.setAdapter(adapter);

				}
			}
			Looper looper;
			looper = Looper.myLooper(); // get the Main looper related with
			// the main thread
			// 如果不给任何参数的话会用当前线程对应的Looper(这里就是Main
			// Looper)为Handler里面的成员mLooper赋值
			SearchResulthandler = new SearchResultHandler(looper);
			// mHandler = new EventHandler();
			// 清除整个MessageQueue里的消息
			SearchResulthandler.removeMessages(0);
			// 得到Message对象
			Message m = SearchResulthandler.obtainMessage(1, 1, 1, GeoString
					.toString());
			// 将Message对象送入到main thread的MessageQueue里面
			SearchResulthandler.sendMessage(m);
			/*
			 * Toast.makeText(getApplicationContext(), GeoString,
			 * Toast.LENGTH_SHORT).show();
			 */

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
		}
	}

	private String SearchResult = null;

	class SearchResultHandler extends Handler {

		public SearchResultHandler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			SearchResult = (String) msg.obj;
			final String ss[] = SearchResult.split("\t");
			final String ssLatlon[] = LatLonString.toString().split("\t");
			if (SearchResult != null) {
				new AlertDialog.Builder(textModel.this).setTitle("复选框")
						.setSingleChoiceItems(ss, 0,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										if (StartSearchRequestSend) {
											// startStreetString = ss[which];
											setTitle(ss[which]);
											editText2.setText(ss[which]);

											startSearchButton.requestFocus();
											//
											StartSearchRequestSend = false;
										} else if (DestSearchRequestSend) {
											// DestStreetString = ss[which];
											setTitle(ss[which]);
											editText3.setText(ss[which]);
											DestSearchRequestSend = false;
										}
										
									
										// SetLayoutItems(intLat, intLon);
										dialog.dismiss();

									}
								}).setNegativeButton("取消", null).show();
			}

			// mapController.setCenter(point);
		}

	}

	public String[] MergeArray(String[] string1, String[] string2) {
		String[] sstemp = new String[string1.length + string2.length];
		System.arraycopy(string1, 0, sstemp, 0, string1.length);
		System.arraycopy(string2, 0, sstemp, string1.length,
				string2.length);
		return sstemp;
	}

	// calculate the price of the distance
	public int calculatePrice(int distance) {
		int price = 0;
		if (distance <= 3000) {
			price = 10;
		} else if (distance <= 15000) {
			price = 10 + 2 * (distance - 3000) / 1000;
		} else if (distance > 15000) {
			price = 10 + 2 * (15000 - 3000) / 1000 + 3 * (distance - 15000)
					/ 1000;
		}
		return price;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onDestroy() {
		if (mapManager != null) {
			mapManager.destroy();
			mapManager = null;
		}
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
