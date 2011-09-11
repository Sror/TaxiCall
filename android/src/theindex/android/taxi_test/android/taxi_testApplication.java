package theindex.android.taxi_test.android;



import android.app.Application;


public class taxi_testApplication extends Application {
	
	static taxi_testApplication mDemoApp;
	
	public String originStreetInfo = null;
	public String destSteetInfo = null;
	
	// huangzf
	//todo : search the database to update the history strings; 
	public String[] historyStrings = {"下拉选择地址或者输入地名搜索",
										"首都国际机场T2航站楼",//116.598669,40.084168
										"首都国际机场T3航站楼",//116.622387,40.060397
										"北京西站",//116.327799,39.901826
										"北京站",//116.433617,39.910842
										"中关村",//116.325895,39.989625
										"国贸"  //116.470309,39.915431
	};
	
	public String [] historyLatLon = {"40084168\n116598669",
			"40060397\n116622387",
			"39901826\n116327799",
			
			"39910842\n116433617",
			"39989625\n116325895",
			"39915431\n116470309"
			
			
	};
	                                                    														

	
	
	//todo : 
	public String passageName = "黄";
	public boolean genderMan = true;
	public String passagePhonenumber = "13911650018";
	
	public String[] nearbyPOIsStrings = null;
	@Override
    public void onCreate() {
		mDemoApp = this;
		super.onCreate();
		Initialize();
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
	public void Initialize()
	{
//		historyLatLon[0].setValue(40084168,116598669);
//		historyLatLon[1].setValue(40060397,116622387);
//		historyLatLon[2].setValue(39901826,116327799);
//		
//		historyLatLon[3].setValue(39910842,116433617);
//		historyLatLon[4].setValue(39989625,116325895);
//		historyLatLon[5].setValue(39915431,116470309);
		
	}

}
