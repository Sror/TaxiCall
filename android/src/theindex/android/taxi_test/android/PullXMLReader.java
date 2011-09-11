package theindex.android.taxi_test.android;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.baidu.mapapi.GeoPoint;

public class PullXMLReader {

	public static GeoPoint readGeoPointfromXML(InputStream inStream) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(inStream, "UTF-8");
			int eventType = parser.getEventType();
			GeoPoint point = null;
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
					
					break;

				case XmlPullParser.START_TAG:// 开始元素事件
					String name = parser.getName();
					if (name.equalsIgnoreCase("location")) {
						point = new GeoPoint(0, 0);
					} else if (point != null) {
						if (name.equalsIgnoreCase("lng")) {
							double dtemp = Double.parseDouble(parser.nextText());
							int a  = (int) (dtemp*1000000);
							point.setLongitudeE6(a);// 如果后面是Text元素,即返回它的值
						} 
						else if (name.equalsIgnoreCase("lat")) {
							double dtemp = Double.parseDouble(parser.nextText());
							int a  = (int) (dtemp*1000000);
							point.setLatitudeE6(a);
						}
					}
					break;
				case XmlPullParser.END_TAG:// 结束元素事件
					
					break;
				}
				eventType = parser.next();

			}
			inStream.close();
			return point;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

//<response>
//<location>
//<lng>116.460857</lng>
//<lat>39.911316</lat>
//</location>
//</response>



//<persons>
//<person id="23">
//<name>李明</name>
//<age>30</age>
//</person>
//<person id="20">
//<name>李向梅</name>
//<age>25</age>
//</person>
//</persons> 


//public static GeoPoint readGeoPointfromXML(InputStream inStream) {
//	XmlPullParser parser = Xml.newPullParser();
//	try {
//		parser.setInput(inStream, "UTF-8");
//		int eventType = parser.getEventType();
//		GeoPoint point = null;
//		
//		while (eventType != XmlPullParser.END_DOCUMENT) {
//			switch (eventType) {
//			case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
//				point = new GeoPoint(0, 0);
//				break;
//
//			case XmlPullParser.START_TAG:// 开始元素事件
//				String name = parser.getName();
//				if (name.equalsIgnoreCase("person")) {
//					currentPerson = new Person();
//					currentPerson.setId(new Integer(parser
//							.getAttributeValue(null, "id")));
//				} else if (currentPerson != null) {
//					if (name.equalsIgnoreCase("name")) {
//						currentPerson.setName(parser.nextText());// 如果后面是Text元素,即返回它的值
//					} else if (name.equalsIgnoreCase("age")) {
//						currentPerson.setAge(new Short(parser.nextText()));
//					}
//				}
//				break;
//			case XmlPullParser.END_TAG:// 结束元素事件
//				if (parser.getName().equalsIgnoreCase("person")
//						&& currentPerson != null) {
//					persons.add(currentPerson);
//					currentPerson = null;
//
//				}
//				break;
//			}
//			eventType = parser.next();
//
//		}
//		inStream.close();
//		return point;
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//	return null;
//}