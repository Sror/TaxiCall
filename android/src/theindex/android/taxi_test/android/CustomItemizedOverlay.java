package theindex.android.taxi_test.android;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;

public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {  
	  
    private ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();  
    private Context context;  
  
    private Paint paintText;
    private Paint backpaint;
    String streetName = null;
    public void initPaint()
    {
    	paintText = new Paint();  
        paintText.setARGB(250, 255, 255, 255);
        paintText.setFakeBoldText(true);
        paintText.setAntiAlias(true);
        paintText.setTextSize(13);
        
        backpaint = new Paint();
        backpaint.setARGB(175, 50, 50, 50);
        backpaint.setAntiAlias(true);
        backpaint.setAlpha(0x40);
    }
    public CustomItemizedOverlay(Drawable defaultMarker) {  
        super(boundCenterBottom(defaultMarker));  
        initPaint();
    }  
  

    public CustomItemizedOverlay(Drawable marker, Context context) {  
        super(boundCenterBottom(marker));  
        this.context = context;
        initPaint();
    }  
    public CustomItemizedOverlay(Drawable marker, Context context, String streetNameString) {  
        super(boundCenterBottom(marker));  
        this.context = context;
        initPaint();
        this.streetName = streetNameString;
    }  
  
    @Override  
    protected OverlayItem createItem(int i) {  
        return overlayItemList.get(i);  
    }  
  
    @Override  
    public int size() {  
        return overlayItemList.size();  
    }  
  
    public void addOverlay(OverlayItem overlayItem) {  
        overlayItemList.add(overlayItem);  
        this.populate();  
    }  
    public void removeAll()
    {
    	if(!overlayItemList.isEmpty())
    	{
    		//overlayItemList.removeAll(overlayItemList);
    		overlayItemList.clear();
    	}
    }
    @Override  
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {  
        super.draw(canvas, mapView, shadow);  
        // Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换  
        Projection projection = mapView.getProjection();  
        // 遍历所有的OverlayItem  
        for (int index = this.size() - 1; index >= 0; index--) {  
            // 得到给定索引的item  
            OverlayItem overLayItem = getItem(index);  
  
            // 把经纬度变换到相对于MapView左上角的屏幕像素坐标  
            Point point = projection.toPixels(overLayItem.getPoint(), null);  
             
            // 绘制文本  
            int mRadius = 5;
            String streetName = overLayItem.getTitle();
            String[] strings = streetName.split("\n");
            int lengthLong;
            if (strings.length>=2) {
            	int lengtha = strings[0].length();
            	int lengthb = strings[1].length();
            	lengthLong = (lengtha > lengthb )? lengtha:lengthb;
            					
			}else {
				lengthLong = strings[0].length();
			}
            int width = lengthLong*14;
            
            //current width is for 9 chinese words 2+mRadius-----130 = 130- 2-7 = 121 /9  = 14
            //so 1 chinese word need 14 width 
            
            //canvas.drawText(overLayItem.getTitle(), point.x + 10, point.y - 15, paintText);
            RectF backRect = new RectF(point.x+2+mRadius,point.y-6*mRadius, point.x+7+width,point.y+2*mRadius);
            canvas.drawRoundRect(backRect,5,5,backpaint);
           // canvas.drawText(overLayItem.getTitle(),point.x+2*mRadius,point.y,paintText);
            canvas.drawText(strings[0], point.x + 10, point.y - 15,paintText);
            if (strings.length>=2) {
            	 canvas.drawText(strings[1],point.x+2*mRadius,point.y,paintText);
                	}
            
        }  
    }  
   
    @Override  
    // 处理点击事件  
    protected boolean onTap(int i) {  
        setFocus(overlayItemList.get(i));  
        Toast.makeText(this.context, overlayItemList.get(i).getSnippet(), Toast.LENGTH_SHORT).show(); 
      return true;  
    }  
}  