package theindex.android.taxi_test.android;

import android.net.Uri;
import android.provider.BaseColumns;

public class HistoryItems   
{  
    //ContentProvider的uri  
    public static final String  AUTHORITY   = "theindex.android.taxi_test.android.HistoryContentProvider";  
      
    //BaseColumns是一个常量接口，里面有一个"_id和"_count"字符串常量  
    public static class HistoryRecords implements BaseColumns  
    {  
        //访问URI  
        public static final Uri  CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/history");  
         
        //自定一类型？
        // 内容类型，新的MIME类型-多个  
        public static final String  CONTENT_TYPE        = "vnd.android.cursor.dir/vnd.google.note";  
        // 新的MIME类型-单个  
        public static final String  CONTENT_ITEM_TYPE   = "vnd.android.cursor.item/vnd.google.note";  
  
        //默认排序常量，按id排序  
        public static final String  DEFAULT_SORT_ORDER  = "_id DESC";  
          
        //字段  
        public static final String  TITLE    = "title";  
        public static final String  NOTE     = "note"; 
        
        public static final String passageName = "passageName";
        public static final String phoneNumber = "phoneNumber";
        public static final String StartStreetName = "startstreetname";
        public static final String StartnLat = "startlat";
        public static final String StartnLon = "startlon";
        public static final String DestStreetName = "deststreetname";
        public static final String DestnLat = "destlat";
        public static final String DestnLon = "destlon";
        
    }  
}  