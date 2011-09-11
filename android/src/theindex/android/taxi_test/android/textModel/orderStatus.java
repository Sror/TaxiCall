package theindex.android.taxi_test.android.textModel;



import theindex.android.taxi_test.R;
import theindex.android.taxi_test.android.baidu.taxi_test;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class orderStatus extends Activity {
    /** Called when the activity is first created */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderstatus);
        UIcontrollerInit();
        setUIvalue();
    }
    
    public void setUIvalue()
    {
    	orderResult.setText("您的订车服务已经提交，谢谢您使用本系统");
    	taxiInformationTextView.setText("接受您本次预定的出租车信息如下:\n" +
    			"司机姓名: 王先生\n" +
    			"联系电话：123456789\n" +
    			"车牌号码： 京A88888\n" +
    			"");
    	
    	
    }
    TextView orderResult;
    TextView taxiInformationTextView;
    Button showOnMapButton;
    Button textModelViewButton;
    Button mapModelViewButton;
    public void UIcontrollerInit()
    {
    	orderResult =(TextView)findViewById(R.id.orderstatustextViewResult);
    	taxiInformationTextView = (TextView)findViewById(R.id.orderstatustextView1);
    	
    	showOnMapButton=(Button)findViewById(R.id.OSbutton1);
    	showOnMapButton.setOnClickListener(listener1);
    	
    	textModelViewButton = (Button)findViewById(R.id.OSbutton2);
    	textModelViewButton.setOnClickListener(listener2);
    	
    	mapModelViewButton=(Button)findViewById(R.id.OSbutton3);
    	mapModelViewButton.setOnClickListener(listener3);
    }
    
	OnClickListener listener1 = new OnClickListener() {
		public void onClick(View v) {

				//todo 
			Intent in = new Intent(orderStatus.this,taxiMapView.class);
			startActivity(in);
		}
	};
	OnClickListener listener2 = new OnClickListener() {
		public void onClick(View v) {

				//todo 
			Intent in = new Intent(orderStatus.this, textModel.class);
			startActivity(in);
		}
	};
	OnClickListener listener3 = new OnClickListener() {
		public void onClick(View v) {

				//todo 
			Intent in = new Intent(orderStatus.this, taxi_test.class);
			startActivity(in);
		}
	};
}