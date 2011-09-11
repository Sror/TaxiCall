package theindex.android.taxi_test.android.baidu;
//package theindex.android.taxi_test.android;
//
//import com.baidu.mapapi.MKAddrInfo;
//import com.baidu.mapapi.MKDrivingRouteResult;
//import com.baidu.mapapi.MKPoiResult;
//import com.baidu.mapapi.MKSearchListener;
//import com.baidu.mapapi.MKTransitRouteResult;
//import com.baidu.mapapi.MKWalkingRouteResult;
//import com.baidu.mapapi.PoiOverlay;
//import com.baidu.mapapi.RouteOverlay;
//import theindex.*;
//
//
////
////    mMKSearch = new MKSearch();
////mMKSearch.init(mBMapMan, new MySearchListener());
//public class Mysearchlistener implements MKSearchListener {
//    @Override
//    public void onGetAddrResult(MKAddrInfo result, int iError) {
//    }
//    
//    /*    MKPlanNode start = new MKPlanNode();
//    start.pt = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6));
//    MKPlanNode end = new MKPlanNode();
//    end.pt = new GeoPoint(40057031, 116307852);
//    // 设置驾车路线搜索策略，时间优先、费用最少或距离最短
//    mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
//    mMKSearch.drivingSearch(null, start, null, end);
//    */
//     
//    @Override
//    public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
//
////    	if (result == null) {
////    		return;
////    		}
////    		RouteOverlay routeOverlay = new RouteOverlay(taxi_test.this, mapView);
////    		// 此处仅展示一个方案作为示例
////    		routeOverlay.setData(result.getPlan(0).getRoute(0));
////    		mapView.getOverlays().add(routeOverlay);
//    }
//     
//    @Override
//    public void onGetPoiResult(MKPoiResult result, int type, int iError) {
//    	//mMKSearch.poiSearchNearBy("KFC", new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6)), 5000);
////    	if (result == null) {
////    		return;
////    		}
////    		PoiOverlay poioverlay = new PoiOverlay(taxi_test.this, mapView);
////    		poioverlay.setData(result.getAllPoi());
////    		mapView.getOverlays().add(poioverlay);
//    }
//     
//    @Override
//    public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
//    }
//     
//    @Override
//    public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
//    }
//    }