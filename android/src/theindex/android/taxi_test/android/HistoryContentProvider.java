package theindex.android.taxi_test.android;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class HistoryContentProvider extends ContentProvider  
{  
    private DatabaseHelper mOpenHelper = null;  
      
    private static final UriMatcher sUriMatcher;  
    private static HashMap<String, String> sTaxiProjectionMap;  
    static {  
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);  
        sUriMatcher.addURI(HistoryItems.AUTHORITY, "history", 1);  
        sUriMatcher.addURI(HistoryItems.AUTHORITY, "history/#", 2);  
          
        sTaxiProjectionMap = new HashMap<String, String>();  
        sTaxiProjectionMap.put(HistoryItems.HistoryRecords._ID, HistoryItems.HistoryRecords._ID);  
        sTaxiProjectionMap.put(HistoryItems.HistoryRecords.TITLE, HistoryItems.HistoryRecords.TITLE);  
        sTaxiProjectionMap.put(HistoryItems.HistoryRecords.NOTE, HistoryItems.HistoryRecords.NOTE);
        
        sTaxiProjectionMap.put(HistoryItems.HistoryRecords.passageName, HistoryItems.HistoryRecords.passageName);  
        sTaxiProjectionMap.put(HistoryItems.HistoryRecords.phoneNumber, HistoryItems.HistoryRecords.phoneNumber); 
        sTaxiProjectionMap.put(HistoryItems.HistoryRecords.StartStreetName, HistoryItems.HistoryRecords.StartStreetName);  
        sTaxiProjectionMap.put(HistoryItems.HistoryRecords.StartnLat, HistoryItems.HistoryRecords.StartnLat);  
        sTaxiProjectionMap.put(HistoryItems.HistoryRecords.StartnLon, HistoryItems.HistoryRecords.StartnLon);  
        sTaxiProjectionMap.put(HistoryItems.HistoryRecords.DestStreetName, HistoryItems.HistoryRecords.DestStreetName);
        sTaxiProjectionMap.put(HistoryItems.HistoryRecords.DestnLat, HistoryItems.HistoryRecords.DestnLat);  
        sTaxiProjectionMap.put(HistoryItems.HistoryRecords.DestnLon, HistoryItems.HistoryRecords.DestnLon);  
          
        
    }  
      
      
      
    // 数据库名  
    private static final String DATABASE_NAME = "taxihistory.db";  
    private static final int DATABASE_VERSION = 2;  
    // 表名  
    private static final String NOTES_TABLE_NAME = "history";  
    // 创建表SQL语句  
    private static final String  
    CREATE_TABLE = "CREATE TABLE " + NOTES_TABLE_NAME + " (" +
    		HistoryItems.HistoryRecords._ID + " INTEGER PRIMARY KEY," + 
            HistoryItems.HistoryRecords.TITLE + " TEXT," + 
            HistoryItems.HistoryRecords.NOTE  + " TEXT," +
            HistoryItems.HistoryRecords.passageName + " TEXT," +
            HistoryItems.HistoryRecords.phoneNumber + " TEXT," +
            HistoryItems.HistoryRecords.StartStreetName + " TEXT," +
            HistoryItems.HistoryRecords.StartnLat + " TEXT," +
            HistoryItems.HistoryRecords.StartnLon + " TEXT," +
            HistoryItems.HistoryRecords.DestStreetName + " TEXT," +
            HistoryItems.HistoryRecords.DestnLat + " TEXT," +
            HistoryItems.HistoryRecords.DestnLon + " TEXT" +                 
            ");";  
    //内部类SQLiteOpenHelper  
    private static class DatabaseHelper extends SQLiteOpenHelper   
    {  
        public DatabaseHelper(Context context)   
        {  
            super(context, DATABASE_NAME, null, DATABASE_VERSION);  
        }  
  
        public void onCreate(SQLiteDatabase db)   
        {  
            db.execSQL(CREATE_TABLE);  
        }  
  
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)   
        {  
            //TODO db.execSQL("DROP TABLE IF EXISTS notes");  
            onCreate(db);  
        }  
    }  
      
      
      
      
    //创建  
    public boolean onCreate()   
    {  
        mOpenHelper = new DatabaseHelper(getContext());  
        return true;  
    }  
      
    // 查询操作  
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,  
            String sortOrder)   
    {  
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();  
          
        switch (sUriMatcher.match(uri))   
        {  
        case 1://notes，所有  
            qb.setTables(NOTES_TABLE_NAME);   
            qb.setProjectionMap(sTaxiProjectionMap);  
            break;  
        case 2://notes/#，根据id查询  
            qb.setTables(NOTES_TABLE_NAME);  
            qb.setProjectionMap(sTaxiProjectionMap);  
            qb.appendWhere(HistoryItems.HistoryRecords._ID + "=" + uri.getPathSegments().get(1));  
            break;  
        default:  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }  
          
        //排序方式  
        String orderBy;  
        if (sortOrder == null) {  
            orderBy = HistoryItems.HistoryRecords.DEFAULT_SORT_ORDER;  
        } else {  
            orderBy = sortOrder;  
        }  
          
        //获取数据库实例  
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();  
          
        //返回游标集合  
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);  
        c.setNotificationUri(getContext().getContentResolver(), uri);  
        return c;  
    }  
      
    // 插入数据库  
    public Uri insert(Uri uri, ContentValues initialValues)   
    {  
        if (sUriMatcher.match(uri) != 1)   
        {  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }  
          
        ContentValues cv;  
        if (initialValues != null)   
        {  
            cv = new ContentValues(initialValues);  
        } else {  
            cv = new ContentValues();  
        }  
          
        if (cv.containsKey(HistoryItems.HistoryRecords.TITLE) == false)   
        {  
            Resources r = Resources.getSystem();  
            cv.put(HistoryItems.HistoryRecords.TITLE,  
                    r.getString(android.R.string.untitled));  
        }  
        if (cv.containsKey(HistoryItems.HistoryRecords.NOTE) == false)   
        {  
            cv.put(HistoryItems.HistoryRecords.NOTE, "");  
        }
        
        //1 for 
        if (cv.containsKey(HistoryItems.HistoryRecords.passageName) == false)   
        {  
            cv.put(HistoryItems.HistoryRecords.passageName, "");  
        }  
        //2 
        if (cv.containsKey(HistoryItems.HistoryRecords.phoneNumber) == false)   
        {  
            cv.put(HistoryItems.HistoryRecords.phoneNumber, "");  
        } 
        //3
        if (cv.containsKey(HistoryItems.HistoryRecords.StartStreetName) == false)   
        {  
            cv.put(HistoryItems.HistoryRecords.StartStreetName, "");  
        }  
        //4
        if (cv.containsKey(HistoryItems.HistoryRecords.StartnLat) == false)   
        {  
            cv.put(HistoryItems.HistoryRecords.StartnLat, "");  
        }  
        //5
        if (cv.containsKey(HistoryItems.HistoryRecords.StartnLon) == false)   
        {  
            cv.put(HistoryItems.HistoryRecords.StartnLon, "");  
        }  
        //6
        if (cv.containsKey(HistoryItems.HistoryRecords.DestStreetName) == false)   
        {  
            cv.put(HistoryItems.HistoryRecords.DestStreetName, "");  
        }  
        //7
        if (cv.containsKey(HistoryItems.HistoryRecords.DestnLat) == false)   
        {  
            cv.put(HistoryItems.HistoryRecords.DestnLat, "");  
        }  
        //8
        if (cv.containsKey(HistoryItems.HistoryRecords.DestnLon) == false)   
        {  
            cv.put(HistoryItems.HistoryRecords.DestnLon, "");  
        }  

             
        
          
        //获取数据库实例  
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();  
        //插入数据，返回id  
        long rowId = db.insert(NOTES_TABLE_NAME, HistoryItems.HistoryRecords.NOTE, cv);  
        //如果成功插入返回uri  
        if (rowId > 0)   
        {  
            Uri noteUri = ContentUris.withAppendedId(HistoryItems.HistoryRecords.CONTENT_URI,rowId);  
            getContext().getContentResolver().notifyChange(noteUri, null);  
            return noteUri;  
        }  
        return null;  
    }  
      
    //删除数据  
    public int delete(Uri uri, String where, String[] whereArgs)   
    {  
        //获取数据库实例  
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();  
          
        int count;  
        //根据指定条件删除  
        switch (sUriMatcher.match(uri))   
        {  
        case 1:  
            count = db.delete(NOTES_TABLE_NAME, where, whereArgs);  
            break;  
        case 2:  
            String noteId = uri.getPathSegments().get(1);  
            count = db.delete(NOTES_TABLE_NAME,  
            		HistoryItems.HistoryRecords._ID + "=" + noteId + (!where.isEmpty() ? " AND (" + where+ ")" : ""),   
                whereArgs);  
            break;  
        default:  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }  
          
        getContext().getContentResolver().notifyChange(uri, null);  
          
        return count;  
    }  
  
    // 更新数据  
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs)   
    {  
        //获取数据库实例  
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();  
          
        int count;  
        //根据指定条件更新  
        switch (sUriMatcher.match(uri))   
        {  
        case 1:  
            count = db.update(NOTES_TABLE_NAME, values, where, whereArgs);  
            break;  
        case 2:  
            String noteId = uri.getPathSegments().get(1);  
            count = db.update(NOTES_TABLE_NAME, values,  
            		HistoryItems.HistoryRecords._ID + "=" + noteId + (!where.isEmpty() ? " AND (" + where  
                                    + ")" : ""),   
                    whereArgs);  
            break;  
        default:  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }  
  
        getContext().getContentResolver().notifyChange(uri, null);  
        return count;  
    }  
      
    // 如果有自定义类型，必须实现该方法  
    public String getType(Uri uri)   
    {  
        switch (sUriMatcher.match(uri))   
        {  
        case 1:  
            return HistoryItems.HistoryRecords.CONTENT_TYPE;  
  
        case 2:  
            return HistoryItems.HistoryRecords.CONTENT_ITEM_TYPE;  
  
        default:  
            throw new IllegalArgumentException("Unknown URI " + uri);  
        }  
    }  
      
}




//public class MainActivity extends Activity   
//{  
//    Uri uri = Uri.parse("content://com.google.provider.csqNotePad/notes");  
//      
//    public void onCreate(Bundle savedInstanceState)   
//    {  
//        super.onCreate(savedInstanceState);  
//        setContentView(R.layout.main);  
//          
//        /* 插入数据 */  
//        ContentValues values = new ContentValues();  
//        values.put("title", "title1");  
//        values.put("note", "note1");  
//        getContentResolver().insert(uri, values);  
//        values.clear();  
//        values.put("title", "title2");  
//        values.put("note", "note2");  
//        getContentResolver().insert(uri, values);  
//          
//        /* 显示 */  
//        displayNote();  
//        Log.i("displayNote", "****************************************");  
//        updateFirst();  
//        displayNote();  
//        Log.i("displayNote", "****************************************");  
//        deleteFirst();  
//        displayNote();  
//        Log.i("displayNote", "****************************************");  
//    }  
//      
//      
//    private void displayNote()  
//    {  
//        String[] columns = new String[] { "_id", "title",  "note"};  
//          
//        Cursor cur = managedQuery(uri, columns, null, null, null);  
//          
//        for(cur.moveToFirst();!cur.isAfterLast();cur.moveToNext())  
//        {  
//            String id = cur.getString(cur.getColumnIndex("_id"));  
//            String title = cur.getString(cur.getColumnIndex("title"));  
//            String note = cur.getString(cur.getColumnIndex("note"));  
//            Log.i("displayNote", "id="+id+", title="+title+", note="+note);  
//        }  
//    }  
//      
//    private void updateFirst()  
//    {  
//        ContentValues values = new ContentValues();  
//        values.put("title", "update1");  
//        values.put("note", "updatenote1");  
//          
//        getContentResolver().update(uri, values, "title = 'title1'", null);  
//    }  
//      
//    private void deleteFirst()  
//    {  
//        getContentResolver().delete(uri, "title = 'update1'", null);  
//    }  
//      
//      
//}