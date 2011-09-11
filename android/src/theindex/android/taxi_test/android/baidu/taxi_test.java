package theindex.android.taxi_test.android.baidu;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;

import theindex.android.taxi_test.R;
import theindex.android.taxi_test.android.CustomItemizedOverlay;
import theindex.android.taxi_test.android.taxi_testApplication;
import theindex.android.taxi_test.android.textModel.orderStatus;
import theindex.android.taxi_test.android.textModel.textModel;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;
import com.baidu.mapapi.RouteOverlay;

public class taxi_test extends MapActivity implements LocationListener {

	public static final int menu1 = Menu.FIRST;
	public static final int menu2 = Menu.FIRST + 1;
	public static final int menu3 = Menu.FIRST + 2;
	public static final int menu4 = Menu.FIRST + 3;
	public static final int menu5 = Menu.FIRST + 4;

	private BMapManager mapManager;
	private MapView mapView;
	private MapController mapController;

	private MKLocationManager mLocationManager = null;
	private MyLocationOverlay myLocationOverlay;
	private MKSearch mMKSearch;

	private boolean originalMakerset = false;
	private boolean destinationMaderset = false;

	private boolean selectOrigin = true;
	private boolean selectDestination = false;

	public GeoPoint startGeoPoint = null;
	public GeoPoint destGeoPoint = null;
	public String startStreetString = "";
	public String DestStreetString = "";

	public GeoPoint currentGpsPosition = null;

	private CustomItemizedOverlay midderoverlayActionStart;
	private CustomItemizedOverlay midderoverlayActionStart2;

	private CustomItemizedOverlay midderoverlayActionDest;
	private CustomItemizedOverlay midderoverlayActionDest2;
	
	
	private GeoPoint pointTobeReversed = null;
	private StringBuffer GeoString = null;
	private StringBuffer LatLonString = null;

	private DisplayMetrics dm;
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	private ArrayAdapter<String> adapterGender;

	private int widthPixels;
	private int heightPixels;
	private LinearLayout linear = null;
	private LinearLayout linearBottom = null;

	private taxi_testApplication app;
	private long presstime;
	public String[] StreetNamestrings = null;
	private GeoPoint point;

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, menu1, 0, "set origin");
		menu.add(0, menu2, 1, "set des");
		menu.add(0, menu3, 2, "reverseGeo");
		menu.add(0, menu4, 3, "routing");
		menu.add(0, menu5, 4, "order Taxi");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		linear.setVisibility(View.GONE);
		switch (item.getItemId()) {
		case menu1:
			// setTitle("set origin");
			String[] ss = { "dd", "dd" };
			new AlertDialog.Builder(taxi_test.this).setTitle("复选框")
					.setSingleChoiceItems(ss, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).show();
			// Intent in = new Intent(taxi_test.this, getGPSlatlon.class);
			// startActivity(in);
			break;
		case menu2:
			setTitle("set destination...");
			break;
		case menu3:
			setTitle("get reverseGeoCode...");
			getReverseGeoCode(pointTobeReversed);
			break;
		case menu4:
			setTitle("start preordering the taxi...");
			if (destinationMaderset && originalMakerset) {
				MKPlanNode start = new MKPlanNode();

				start.pt = midderoverlayActionStart.getItem(0).getPoint();
				MKPlanNode end = new MKPlanNode();
				end.pt = midderoverlayActionDest.getItem(0).getPoint();//
				mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
				mMKSearch.drivingSearch(null, start, null, end);
			}
			break;
		case menu5:
			setTitle("start preordering the taxi...");
			// Intent in = new Intent(taxi_test.this,
			// SelectstreetInformation.class);
			// startActivity(in);

			mMKSearch.poiSearchNearBy("", point, 500);
			break;

		}
		return super.onOptionsItemSelected(item);
	}
	private String startString=null;
	private String destString = null;
	private String phoneNumberString = null;
	

	private boolean inputcheck() {
		String[] warningString = { "", 
				"请输入正确的手机号码", 
				"请输入或者选择正确的起点",
				"请输入或者选择正确的终点"
				};
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
	public void getReverseGeoCode(GeoPoint point) {
		if (point != null) {
			// mMKSearch.reverseGeocode(new GeoPoint(40057031, 116307852));
			mMKSearch.reverseGeocode(point);

		}
	}

	private void getScreenSize() {
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		widthPixels = dm.widthPixels;
		heightPixels = dm.heightPixels;
		// 获得手机的宽带和高度像素单位为px
		// String str = "手机屏幕分辨率为:" +
		// dm.widthPixels+"　*　"+dm.heightPixels;textview1.setText(str);　
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// view.setText("你的血型是："+m[arg2]);
			// setTitleStreetName( arg2);
			if (selectOrigin) {
				if (StreetNamestrings != null) {
					startStreetString = StreetNamestrings[arg2];
					searchText.setText(StreetNamestrings[arg2]);
				}

			} else if (selectDestination) {
				if (StreetNamestrings != null) {
					DestStreetString = StreetNamestrings[arg2];
					searchText.setText(StreetNamestrings[arg2]);
				}
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SpinnerListenerGender implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// view.setText("你的血型是："+m[arg2]);
			// setTitleStreetName( arg2);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	public void setTitleStreetName(int arg2) {
		setTitle(StreetNamestrings[arg2]);
	}

	public void DrawAbleInitStart() {
		mapOverlays = mapView.getOverlays();
		
		Resources r = getApplicationContext().getResources();
		InputStream is = r.openRawResource(R.drawable.start1);
		BitmapDrawable midderMarkerStart = new BitmapDrawable(is);
		midderMarkerStart.setAntiAlias(true);

		midderMarkerStart.setBounds(0, 0,
				midderMarkerStart.getIntrinsicWidth(), midderMarkerStart
						.getIntrinsicHeight());
		midderoverlayActionStart = new CustomItemizedOverlay(midderMarkerStart,
				this);

		Resources r2 = getApplicationContext().getResources();
		InputStream is2 = r2.openRawResource(R.drawable.start2);
		BitmapDrawable midderMarkerStart2 = new BitmapDrawable(is2);
		midderMarkerStart2.setAntiAlias(true);

		midderMarkerStart2.setBounds(0, 0, midderMarkerStart2
				.getIntrinsicWidth(), midderMarkerStart2.getIntrinsicHeight());
		midderoverlayActionStart2 = new CustomItemizedOverlay(
				midderMarkerStart2, this);
		
		

	}

	public void DrawAbleInitDest()

	{
		Resources r = getApplicationContext().getResources();
		InputStream is = r.openRawResource(R.drawable.end1);
		BitmapDrawable midderMarkerDest = new BitmapDrawable(is);
		midderMarkerDest.setAntiAlias(true);

		midderMarkerDest.setBounds(0, 0, midderMarkerDest.getIntrinsicWidth(),
				midderMarkerDest.getIntrinsicHeight());
		midderoverlayActionDest = new CustomItemizedOverlay(midderMarkerDest,
				this);
		
		Resources r2 = getApplicationContext().getResources();
		InputStream is2 = r2.openRawResource(R.drawable.end2);
		BitmapDrawable midderMarkerDest2 = new BitmapDrawable(is2);
		midderMarkerDest2.setAntiAlias(true);

		midderMarkerDest2.setBounds(0, 0, midderMarkerDest2.getIntrinsicWidth(),
				midderMarkerDest2.getIntrinsicHeight());
		midderoverlayActionDest2 = new CustomItemizedOverlay(midderMarkerDest2,
				this);

	}

	private Button BottomBtn1; // go to GPS position
	private Button BottomBtn2;
	private Button BottomBtn3;
	private Button BottomBtn4;
	private Button BottomBtn5;
	private Spinner gender;
	// private Spinner phoneNumber;

	private Button addressSearchButton;
	private EditText searchText;

	Button textModelView;
	Button orderCheckButton;
	private ImageView targetView;
	public void UIcontrollerInit() {
		
		orderCheckButton =(Button)findViewById(R.id.MapModelorderCheck);
		orderCheckButton.setOnClickListener(listenerOrderCheck);
		getScreenSize();
		linear = (LinearLayout) findViewById(R.id.titleBar);
		linear.setVisibility(View.GONE);

		linearBottom = (LinearLayout) findViewById(R.id.BottomBar);
		linearBottom.setVisibility(View.GONE);

		BottomBtn1 = (Button) findViewById(R.id.BottomButton1);
		BottomBtn1.setOnClickListener(listenerGPSposition);

		BottomBtn2 = (Button) findViewById(R.id.BottomButton2);
		BottomBtn2.setOnClickListener(listenerDest);
		BottomBtn2.setText("确定起点，设置终点");

		BottomBtn3 = (Button) findViewById(R.id.BottomButton3);

		BottomBtn4 = (Button) findViewById(R.id.BottomButton4);
		BottomBtn4.setVisibility(View.GONE);

		BottomBtn5 = (Button) findViewById(R.id.BottomButton5);
		BottomBtn5.setOnClickListener(bottomlisten5);
		BottomBtn5.setVisibility(View.GONE);

		addressSearchButton = (Button) findViewById(R.id.addressSearchbutton);
		addressSearchButton.setOnClickListener(listenerAddressSearch);

		searchText = (EditText) findViewById(R.id.SearchEditText);
		searchText.setTextColor(Color.GRAY);
		searchText.setText("请输入关键字");
		searchText.setOnFocusChangeListener(focusListener);
		addressSearchButton.refreshDrawableState();

		// searchText.setTextColor(Color.BLACK);

		textModelView = (Button) findViewById(R.id.MapModelGotoTextView);
		textModelView.setOnClickListener(listener);
		
		targetView = (ImageView)findViewById(R.id.imageView1);
		targetView.setVisibility(View.GONE);
		
		DrawAbleInitStart();
		DrawAbleInitDest();

		

		{
			// removeLayItems(midderoverlayActionDown);
			Projection projection = this.mapView.getProjection();
			point = projection.fromPixels(widthPixels / 2, heightPixels / 2);
			setTitle(widthPixels + ":" + heightPixels + ":"
					+ point.getLatitudeE6() + ":" + point.getLongitudeE6());
			// addOverLayItems(point.getLatitudeE6(), point.getLoHngitudeE6(),
			// midderoverlayActionStart);

		}

		{
			spinner = (Spinner) findViewById(R.id.spinnerStreetName);
			// 将可选内容与ArrayAdapter连接起来
			String[] temps = { "移动地图并选择位置信息" };
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, temps);
			// 设置下拉列表的风格
			adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			spinner.setAdapter(adapter);

			// 添加事件Spinner事件监听
			spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		}

	}

	public void MapInit() {
		mapManager = new BMapManager(getApplication());
		
		//1. 66C2870284C9B4C2A90791589E7DCD76D1255026
	    //2. 016BC50B77BDCA523AA871ED77C70E03D399B31C
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

		mapView = (MapView) findViewById(R.id.map_View);
		// SET traffic: we do not need traffic
		// mapView.setTraffic(true);
		// level control:

		mapView.setBuiltInZoomControls(false);
		mapView.displayZoomControls(false);
		mMKSearch = new MKSearch();
		mMKSearch.init(mapManager, new Mysearchlistener());
		MKSearch.setPoiPageCapacity(50);

		// guanghua road: 39.913061,116.452582 //
		GeoPoint point = new GeoPoint((int) (39.913061 * 1E6),
				(int) (116.452582 * 1E6));

		Drawable marker = this.getResources().getDrawable(R.drawable.icon);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
				.getIntrinsicHeight());

		mapController = mapView.getController();
		mapController.setCenter(point);
		// 设置地图的缩放级别。 这个值的取值范围是[3,18]。
		mapController.setZoom(16);

		// 添加定位图层
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		// 注册GPS位置更新的事件,让地图能实时显示当前位置
		myLocationOverlay.enableMyLocation();
		// 开启磁场感应传感器
		myLocationOverlay.enableCompass();
		mapView.getOverlays().add(myLocationOverlay);

		mLocationManager = mapManager.getLocationManager();
		// 注册位置更新事件
		mLocationManager.requestLocationUpdates(this);
		// 使用GPS定位
		mLocationManager
				.enableProvider((int) MKLocationManager.MK_GPS_PROVIDER);

		app = (taxi_testApplication) this.getApplication();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		MapInit();
		UIcontrollerInit();
		if (savedInstanceState != null) {
			// int startlat = savedInstanceState.getInt("MyStartLat");
			// int startlon = savedInstanceState.getInt("MyStartLon");
			// int destlat = savedInstanceState.getInt("MyDestLat");
			// int destlon = savedInstanceState.getInt("MyDestLon");
			// if(startlat!=0&& startlon!=0)
			// {
			//				
			// }
		}

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.

		if (startGeoPoint == null) {
			startGeoPoint = new GeoPoint(0, 0);
		}
		if (destGeoPoint == null) {
			destGeoPoint = new GeoPoint(0, 0);
		}

		savedInstanceState.putInt("MyStartLat", startGeoPoint.getLatitudeE6());
		savedInstanceState.putInt("MyStartLon", startGeoPoint.getLongitudeE6());
		savedInstanceState.putInt("MyDestLat", destGeoPoint.getLatitudeE6());
		savedInstanceState.putInt("MyDestLon", destGeoPoint.getLongitudeE6());

		// etc.
		super.onSaveInstanceState(savedInstanceState);
	}

	private boolean firstTimeFocusChanged = true;
	OnFocusChangeListener focusListener = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (firstTimeFocusChanged) {
					firstTimeFocusChanged = false;
					searchText.setTextColor(Color.BLACK);
					searchText.setText("");
				}
			}
		}
	};

	private boolean addressSearchRequestSend = false;
	OnClickListener listenerAddressSearch = new OnClickListener() {
		public void onClick(View v) {

			// todo huangzf:8/4 search address
			String keyword = searchText.getText().toString();
			mMKSearch.poiSearchInCity("beijing", keyword);
			addressSearchRequestSend = true;

		}
	};
	OnClickListener listenerOrderCheck = new OnClickListener() {
		public void onClick(View v) {
			Intent in = new Intent(taxi_test.this, orderStatus.class);
			startActivity(in);
		}
	};
	
	OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
//			if (pointTobeReversed != null) {
//				mMKSearch.reverseGeocode(pointTobeReversed);
//			}
			Intent in = new Intent(taxi_test.this, textModel.class);
			startActivity(in);
			
		}
	};

	private void clearItemOnscreen()
	{
		
		if (selectDestination) {
			removeLayItems(midderoverlayActionDest);
			removeLayItems(midderoverlayActionDest2);
			if (mapOverlays != null) {
				mapOverlays.remove(midderoverlayActionDest);
				mapOverlays.remove(midderoverlayActionDest2);
			}					
		} else if (selectOrigin) {
			removeLayItems(midderoverlayActionStart);
			removeLayItems(midderoverlayActionStart2);
			
			if (mapOverlays != null) {
				mapOverlays.remove(midderoverlayActionStart);
				mapOverlays.remove(midderoverlayActionStart2);
				
			}
		}
	}
	OnClickListener listenerGPSposition = new OnClickListener() {
		public void onClick(View v) {
			if (currentGpsPosition != null) {
				mapController.animateTo(currentGpsPosition);
				
				clearItemOnscreen();
				point = currentGpsPosition;

				if (selectDestination) {
					// DrawAbleInitDest();
					destGeoPoint = point;
					addOverLayItems(point.getLatitudeE6(), point
							.getLongitudeE6(), midderoverlayActionDest);
				} else if (selectOrigin) {
					// DrawAbleInitStart();
					startGeoPoint = point;
					addOverLayItems(point.getLatitudeE6(), point
							.getLongitudeE6(), midderoverlayActionStart);
				}

			}
		}
	};
	private boolean firstClick = true;
	OnClickListener listenerDest = new OnClickListener() {
		public void onClick(View v) {
			selectDestination = true;
			selectOrigin = false;
			if (firstClick) {
				firstClick = false;
				BottomBtn2.setText("确定终点");
				point = projection.fromPixels(widthPixels / 2,
						heightPixels / 2 - 100);
				addOverLayItems(point.getLatitudeE6(), point.getLongitudeE6(),
						midderoverlayActionDest);

			} else {
				BottomBtn1.setVisibility(View.GONE);
				BottomBtn2.setVisibility(View.GONE);
				BottomBtn3.setVisibility(View.GONE);
				BottomBtn4.setVisibility(View.VISIBLE);
				BottomBtn5.setVisibility(View.VISIBLE);
				// dest is determine: fix the view
				destSelected = true;

			}

		}
	};

	//
	OnClickListener bottomlisten5 = new OnClickListener() {
		public void onClick(View v) {
			// MKPlanNode start = new MKPlanNode();
			//
			// start.pt = startGeoPoint;
			// MKPlanNode end = new MKPlanNode();
			// end.pt = destGeoPoint;
			// mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
			// mMKSearch.drivingSearch(null, start, null, end);
			//			
			setContentView(R.layout.requestinformation);
			InitRequestInformationView();

			// InitRequestInformationView();

		}

	};
	private EditText editText1;
	private EditText editText2; // Start string
	private EditText editText3; // dest string
	private EditText editText4; // phone nubmer
	private TextView distanceAndPrice;
	private Button supplyRequestButton;
	private Button serviceExitButton;
	
	private void InitRequestInformationView() {
		{
			editText1 = (EditText) findViewById(R.id.editText1);

			editText2 = (EditText) findViewById(R.id.editText2);
			editText3 = (EditText) findViewById(R.id.editText3);
			editText4 = (EditText) findViewById(R.id.editText4);
			editText1.setText("");
			editText2.setText("");
			editText3.setText("");
			editText4.setText("");
			distanceAndPrice = (TextView) findViewById(R.id.textView4);
			distanceAndPrice.setText("距离:" + orderDistance / 1000.0 + "公里"
					+ "   " + "价格：" + orderPrice + "元");

			editText2.setText(startStreetString);
			editText3.setText(DestStreetString);
			supplyRequestButton = (Button) findViewById(R.id.applyButton);
			supplyRequestButton.setOnClickListener(listenerSupplyRequest);
			serviceExitButton = (Button) findViewById(R.id.serviceExit);
			serviceExitButton.setOnClickListener(listenerAlarmServiceExit);
		
			
		}
		gender = (Spinner) findViewById(R.id.spinner1);
		{

			// 将可选内容与ArrayAdapter连接起来
			String[] temps = { "先生", "女士" };
			adapterGender = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, temps);
			// 设置下拉列表的风格
			adapterGender
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			gender.setAdapter(adapterGender);

			// 添加事件Spinner事件监听
			gender.setOnItemSelectedListener(new SpinnerListenerGender());

		}
		{
			TelephonyManager tm = (TelephonyManager) this
					.getSystemService(Context.TELEPHONY_SERVICE);
			String tel = tm.getLine1Number();
			if (tel == null) {
				tel = "请输入手机号码";
				editText4.setTextColor(Color.GRAY);
				editText4.setText(tel);
				editText4.setOnFocusChangeListener(focusListener2);
			} else {
				editText4.setText(tel);
			}
		}

	}

	private boolean firstTimeFocusChanged2 = true;
	OnFocusChangeListener focusListener2 = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (firstTimeFocusChanged2) {
					firstTimeFocusChanged2 = false;
					editText4.setTextColor(Color.BLACK);
					editText4.setText("");
				}
			}
		}
	};
//	OnClickListener listenerHistoryButton = new OnClickListener() {
//		public void onClick(View v) {
//			// Uri uri =
//			// Uri.parse("content://theindex.android.taxi_test.android.HistoryContentProvider/history");
//			ContentValues values = new ContentValues();
//			values.put("title", "title1");
//			values.put("note", "note1");
//
//			values.put(HistoryItems.HistoryRecords.passageName, "xx先生");
//			values.put(HistoryItems.HistoryRecords.phoneNumber, editText4
//					.getText().toString());
//			values.put(HistoryItems.HistoryRecords.StartStreetName, editText2
//					.getText().toString());
//			values.put(HistoryItems.HistoryRecords.DestStreetName, editText3
//					.getText().toString());
//
//			if (startGeoPoint != null) {
//				values.put(HistoryItems.HistoryRecords.StartnLat, startGeoPoint
//						.getLatitudeE6()
//						+ " ");
//				values.put(HistoryItems.HistoryRecords.StartnLon, startGeoPoint
//						.getLongitudeE6()
//						+ " ");
//
//			}
//			if (destGeoPoint != null) {
//				values.put(HistoryItems.HistoryRecords.DestnLat, destGeoPoint
//						.getLatitudeE6()
//						+ " ");
//				values.put(HistoryItems.HistoryRecords.DestnLon, destGeoPoint
//						.getLongitudeE6()
//						+ " ");
//			}
//			getContentResolver().insert(
//					HistoryItems.HistoryRecords.CONTENT_URI, values);
//		}
//	};
	
	
//	OnClickListener listenerGetLatLonByID = new OnClickListener() {
//		public void onClick(View v) {
//			httpRequestBuilder builder = new httpRequestBuilder();
//			String a = "13501341544080801";
//			HttpResponse result = builder.RequestTaxiDataByID(a);
//			StringBuilder total = null;
//			try {
//				total = builder.inputStreamToString(result.getEntity().getContent());
//			} catch (IllegalStateException e) {
//				
//				e.printStackTrace();
//			} catch (IOException e) {
//				
//				e.printStackTrace();
//			}
//			Toast.makeText(getApplicationContext(), total.toString(),
//					Toast.LENGTH_SHORT).show();
//		}
//	};
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
	OnClickListener listenerGoBackTomap = new OnClickListener() {
		public void onClick(View v) {
			setContentView(R.layout.main);
		}
	};
	OnClickListener listenerSupplyRequest = new OnClickListener() {
		public void onClick(View v) {
			// setContentView(R.layout.main);
			// todo: contact with server to send the request
			// todo: start a service to monitor the response huangzf

			// int op 1 : for play
			// 2: for stop
			// 3: for pause
			// for close this.finish();
			// 4: exit: stopservice(intent);this.finish();
			
//			int op = -1;
//			Intent intent = new Intent(
//					"theindex.android.taxi_test.android.baidu.AlarmService");
//			Bundle bundle = new Bundle();
//			bundle.putInt("op", op);
//			intent.putExtras(bundle);
//			startService(intent);
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
			
			
			;
			Toast.makeText(getApplicationContext(), total.toString(),
					Toast.LENGTH_SHORT).show();
			

		}
	};
	
	
	private Handler handler;

	private Handler SearchResulthandler;
	private int AUTOHIDDENTIME2 = 7000;
	private int AOTOHIDDENTIME1 = 8000;

	class MyHandler extends Handler {

		public MyHandler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			presstime = System.currentTimeMillis() - presstime;
			if (presstime > AUTOHIDDENTIME2) {
				super.handleMessage(msg);
				// textView.setText("我是主线程的Handler，收到了消息：" (String)msg.obj);
				linear.setVisibility(View.GONE);
				linearBottom.setVisibility(View.GONE);

				// mapController.setCenter(point);
			}

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
				new AlertDialog.Builder(taxi_test.this).setTitle("复选框")
						.setSingleChoiceItems(ss, 0,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										if (selectOrigin) {
											startStreetString = ss[which];
											setTitle(ss[which]);
										} else if (selectDestination) {
											DestStreetString = ss[which];
											setTitle(ss[which]);
										}
										String[] latlons = ssLatlon[which]
												.split("\n");
										int intLat = Integer
												.parseInt(latlons[0]);
										int intLon = Integer
												.parseInt(latlons[1]);

										SetLayoutItems(intLat, intLon);
										dialog.dismiss();

									}
								}).setNegativeButton("取消", null).show();
			}

			// mapController.setCenter(point);
		}

	}

	public void UpperButtonAutoHidder() {
		Timer mTimer = new Timer();

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
		}, AOTOHIDDENTIME1);
	}

	// private GeoPoint point ;
	private Projection projection;
	private boolean destSelected = false;

	public boolean dispatchTouchEvent(MotionEvent ev) {

		int actionType = ev.getAction();

		switch (actionType) {
		case MotionEvent.ACTION_MOVE:
			// react properly
			break;
		case MotionEvent.ACTION_DOWN: {
			targetView.setVisibility(View.VISIBLE);
			clearItemOnscreen();
			projection = this.mapView.getProjection();
			//point = projection.fromPixels(widthPixels / 2, heightPixels / 2);
			// /2 is not the right middle, why,????
			point = projection.fromPixels(widthPixels / 2, heightPixels / 2);
			
			setTitle(widthPixels + ":" + heightPixels + ":"
					+ point.getLatitudeE6() + ":" + point.getLongitudeE6());
			
			if (selectDestination) {
				// DrawAbleInitDest();
				destGeoPoint = point;
				addOverLayItems(point.getLatitudeE6(), point.getLongitudeE6(),
						midderoverlayActionDest2);
			} else if (selectOrigin) {
				// DrawAbleInitStart();
				startGeoPoint = point;
				addOverLayItems(point.getLatitudeE6(), point.getLongitudeE6(),
						midderoverlayActionStart2);
			}
		}
			break;
		case MotionEvent.ACTION_UP:
			targetView.setVisibility(View.GONE);
			
			linear.setVisibility(View.VISIBLE);
			linearBottom.setVisibility(View.VISIBLE);
			UpperButtonAutoHidder();
			presstime = System.currentTimeMillis();

			if (!destSelected) {
				clearItemOnscreen();
				// setTitle("click detectd");
				// MotionEvent.
				// DrawAbleInit();
				projection = this.mapView.getProjection();
				point = projection
						.fromPixels(widthPixels / 2, heightPixels / 2-50);
				setTitle(widthPixels + ":" + heightPixels + ":"
						+ point.getLatitudeE6() + ":" + point.getLongitudeE6());

				if (selectDestination) {
					// DrawAbleInitDest();
					destGeoPoint = point;
					addOverLayItems(point.getLatitudeE6(), point
							.getLongitudeE6(), midderoverlayActionDest);
				} else if (selectOrigin) {
					// DrawAbleInitStart();
					startGeoPoint = point;
					addOverLayItems(point.getLatitudeE6(), point
							.getLongitudeE6(), midderoverlayActionStart);
				}

				mMKSearch.poiSearchNearBy("餐厅", point, 500);
				getReverseGeoCode(point);
				s1 = null;

			}
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

	private void SetLayoutItems(int lat, int lon) {
		if (!destSelected) {

			clearItemOnscreen();

			if (lat == 0 && lon == 0) {
				projection = this.mapView.getProjection();
				point = projection
						.fromPixels(widthPixels / 2, heightPixels / 2);
				setTitle(widthPixels + ":" + heightPixels + ":"
						+ point.getLatitudeE6() + ":" + point.getLongitudeE6());
			} else {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
				point = new GeoPoint(lat, lon);
				mapController.animateTo(point);

			}
			if (selectDestination) {
				// DrawAbleInitDest();
				destGeoPoint = point;
				addOverLayItems(point.getLatitudeE6(), point.getLongitudeE6(),
						midderoverlayActionDest);
			} else if (selectOrigin) {
				// DrawAbleInitStart();
				startGeoPoint = point;
				addOverLayItems(point.getLatitudeE6(), point.getLongitudeE6(),
						midderoverlayActionStart);
			}
		}
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

//	@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//            if(keyCode == KeyEvent.KEYCODE_BACK){
//                    return false;
//            }
//            return false;
//    }
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

	@Override
	protected boolean isLocationDisplayed() {
		return myLocationOverlay.isMyLocationEnabled();
	}

	/**
	 * 当位置发生变化时触发此方法
	 * 
	 * @param location
	 *            当前位置
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			// 将当前位置转换成地理坐标点
			final GeoPoint pt = new GeoPoint(
					(int) (location.getLatitude() * 1000000), (int) (location
							.getLongitude() * 1000000));
			// 将当前位置设置为地图的中心
			mapController.setCenter(pt);
			currentGpsPosition = pt;
		}
	}

	private List<Overlay> mapOverlays;

	public void addOverLayItems(int lat, int lon, CustomItemizedOverlay overlay) {

		GeoPoint point = new GeoPoint((int) lat, (int) lon);
		OverlayItem overlayItem = null;
		if (selectOrigin) {
			overlayItem = new OverlayItem(point, "新增测试 起点", "测试起点");
		} else if (selectDestination) {
			overlayItem = new OverlayItem(point, "新增测试终点", "测试终点");
		} else {
			overlayItem = new OverlayItem(point, "地图中心", "测试中心");
		}
		overlay.addOverlay(overlayItem);
		/**
		 * 往地图上添加自定义的ItemizedOverlay
		 */

		mapOverlays.add(overlay);
	}

	public void addOverLayItems(int lat, int lon,
			CustomItemizedOverlay overlay, String streetname) {

		GeoPoint point = new GeoPoint((int) lat, (int) lon);
		OverlayItem overlayItem = null;
		overlayItem = new OverlayItem(point, streetname, "");
		overlay.addOverlay(overlayItem);
		/**
		 * 往地图上添加自定义的ItemizedOverlay
		 */
		mapOverlays.add(overlay);
	}

	public void removeLayItems(CustomItemizedOverlay overlay) {
		overlay.removeAll();
	}

	private int orderDistance;
	private int orderPrice;
	public String[] s1;

	public class Mysearchlistener implements MKSearchListener {

		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			if (result == null) {
				Toast.makeText(getApplicationContext(),
						"not street informaiton ", Toast.LENGTH_SHORT).show();
				return;
			}
			GeoString = new StringBuffer();
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

				}
			}

			if (selectOrigin) {
				app.originStreetInfo = GeoString.toString();
			} else if (selectDestination) {
				app.destSteetInfo = GeoString.toString();
			}
			// 将地址信息、兴趣点信息显示在TextView上
			// Toast.makeText(getApplicationContext(), GeoString.toString(),
			// Toast.LENGTH_SHORT).show();
			//

			StreetNamestrings = GeoString.toString().split("\t");
			setTitle(StreetNamestrings[0]);
			{
				adapter = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_spinner_item, StreetNamestrings);
				// 设置下拉列表的风格
				adapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// 将adapter 添加到spinner中
				spinner.setAdapter(adapter);
				//todo huangzf 8-11 
				searchText.setText(StreetNamestrings[0]);

			}
			{
				clearItemOnscreen();
				if (selectDestination) {
					

				
					if (StreetNamestrings.length >= 2) {
						addOverLayItems(point.getLatitudeE6(), point
								.getLongitudeE6(), midderoverlayActionDest,
								StreetNamestrings[1]);
					} else {
						addOverLayItems(point.getLatitudeE6(), point
								.getLongitudeE6(), midderoverlayActionDest,
								StreetNamestrings[0]);
					}
				} else if (selectOrigin) {
					
					if (StreetNamestrings.length >= 2) {
						addOverLayItems(point.getLatitudeE6(), point
								.getLongitudeE6(), midderoverlayActionStart,
								StreetNamestrings[1]);
					} else {
						addOverLayItems(point.getLatitudeE6(), point
								.getLongitudeE6(), midderoverlayActionStart,
								StreetNamestrings[0]);
					}
				}

			}

		}

		/*
		 * MKPlanNode start = new MKPlanNode(); start.pt = new GeoPoint((int)
		 * (39.915 * 1E6), (int) (116.404 * 1E6)); MKPlanNode end = new
		 * MKPlanNode(); end.pt = new GeoPoint(40057031, 116307852); //
		 * 设置驾车路线搜索策略，时间优先、费用最少或距离最短
		 * mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
		 * mMKSearch.drivingSearch(null, start, null, end);
		 */

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {

			if (result == null) {
				return;
			}
			orderDistance = result.getPlan(0).getDistance();
			orderPrice = calculatePrice(orderDistance);
			distanceAndPrice.setText("距离:" + orderDistance / 1000.0 + "公里"
					+ "   " + "价格：" + orderPrice + "元");

			Toast.makeText(getApplicationContext(),
					"距离: " + orderDistance + " 价格 : " + orderPrice,
					Toast.LENGTH_SHORT).show();
			RouteOverlay routeOverlay = new RouteOverlay(taxi_test.this,
					mapView);
			// 此处仅展示一个方案作为示例
			routeOverlay.setData(result.getPlan(0).getRoute(0));
			mapView.getOverlays().add(routeOverlay);

		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			// mMKSearch.poiSearchNearBy("KFC", new GeoPoint((int) (39.915 *
			// 1E6), (int) (116.404 * 1E6)), 5000);

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
				LatLonString.append(POIS.get(i).pt.getLatitudeE6()).append(":");
				LatLonString.append(POIS.get(i).pt.getLongitudeE6()).append(
						"\t");

			}
			String[] s2 = GeoString.toString().split("\t");
			if (addressSearchRequestSend == true) {
				addressSearchRequestSend = false;

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
				Message m = SearchResulthandler.obtainMessage(1, 1, 1,
						GeoString.toString());
				// 将Message对象送入到main thread的MessageQueue里面
				SearchResulthandler.sendMessage(m);
				/*
				 * Toast.makeText(getApplicationContext(), GeoString,
				 * Toast.LENGTH_SHORT).show();
				 */
				return;
			}

			{
				adapter = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_spinner_item, s2);
				// 设置下拉列表的风格
				adapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				if (StreetNamestrings != null) {
					for (int i = 0; i < StreetNamestrings.length; i++) {
						adapter.add(StreetNamestrings[i]);
					}
				}
				if (s1 != null) {
					for (int i = 0; i < s1.length; i++) {
						adapter.add("old" + s1[i]);
					}
				}
				s1 = s2;
				// 将adapter 添加到spinner中
				spinner.setAdapter(adapter);
				if (selectOrigin) {
					startStreetString = StreetNamestrings[0];
				} else if (selectDestination) {
					DestStreetString = StreetNamestrings[0];
				}

			}
			Toast.makeText(getApplicationContext(), "POI search finish",
					Toast.LENGTH_SHORT).show();
			// PoiOverlay poioverlay = new PoiOverlay(taxi_test.this, mapView);
			// poioverlay.setData(result.getAllPoi());
			// mapView.getOverlays().add(poioverlay);

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
}

// public void SetOverLayItems() {
// /**
// * 创建图标资源（用于显示在overlayItem所标记的位置）
// */
// Drawable marker = this.getResources().getDrawable(R.drawable.start);
// // 为maker定义位置和边界
// marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
// .getIntrinsicHeight());
//
// /**
// * 创建自定义的ItemizedOverlay
// */
// CustomItemizedOverlay overlay = new CustomItemizedOverlay(marker, this);
// GeoPoint point = new GeoPoint((int) (39.911498 * 1E6),
// (int) (116.44627 * 1E6));
// // 创建标记（
// OverlayItem overlayItem = new OverlayItem(point, "这个是一个测试起点", "测试起点");
// // 将标记添加到图层中（可添加多个OverlayItem）
// overlay.addOverlay(overlayItem);
// /**
// * 创建并添加第二个标记：（经度：116.44627 纬度：39.911498）
// */
// point = new GeoPoint((int) (39.91212 * 1E6), (int) (116.45352 * 1E6));
// // 创建标记（
// overlayItem = new OverlayItem(point, "这个是一个测试终点", "测试终点");
// // 将标记添加到图层中（可添加多个OverlayItem）
// overlay.addOverlay(overlayItem);
//
// /**
// * 往地图上添加自定义的ItemizedOverlay
// */
// List<Overlay> mapOverlays = mapView.getOverlays();
// mapOverlays.add(overlay);
//
// };