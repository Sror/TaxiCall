package theindex.android.taxi_test.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import theindex.android.taxi_test.R;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class HistoryView extends ListActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.historyview);

		List<Map<String, String>> ms = new ArrayList<Map<String, String>>();

		Map<String, String> mymenus = new HashMap<String, String>();
		String[] columns = new String[] { "_id", "title", "note",
				HistoryItems.HistoryRecords.StartStreetName };

		Cursor cur = managedQuery(HistoryItems.HistoryRecords.CONTENT_URI,
				columns, null, null, null);

		String resultshowString = null;
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			
			String startStreetNAME = cur
					.getString(cur
							.getColumnIndex(HistoryItems.HistoryRecords.StartStreetName));
			resultshowString += startStreetNAME;
			mymenus.put("item", startStreetNAME);
			ms.add(mymenus);
			// Log.i("displayNote", "id="+id+", title="+title+", note="+note);
		}
		 Toast.makeText(getApplicationContext(), resultshowString,
					Toast.LENGTH_SHORT).show();
		 
			SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, String>>) ms, R.layout.listitems,
				new String[] { "item" }, new int[] { R.id.menu_item });

		setListAdapter(adapter);
	}
}
