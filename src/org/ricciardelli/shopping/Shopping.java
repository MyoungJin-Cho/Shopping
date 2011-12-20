/* This file is part of Shopping.
 * Shopping is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Shopping is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Shopping.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ricciardelli.shopping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class Shopping extends Activity implements ViewFactory {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping);
		Intent intent = getIntent();
		setTitle(intent.getExtras().getString("name").toString());
		inflateList(intent.getExtras().getLong("id"));
		setTotal();
	}

	private void inflateList(long id) {
		final ListView shopping = (ListView) findViewById(R.id.shopping);
		shopping.setAdapter(getTwoLinesListCheckItemAdapter(this, id));
		shopping.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (shopping.getCheckedItemPositions().get(position)) {
					Log.i("PRICE", getPrice(Shopping.this, id));
				}
			}
		});
	}

	private String getPrice(Context context, long id) {
		Main.openDatabase(context);
		Cursor cursor = Main.db.rawQuery("SELECT * FROM products WHERE _id = "
				+ id, null);
		cursor.moveToFirst();
		String price = cursor.getString(3);
		Main.closeDatabase();
		cursor.close();
		return price;
	}

	private ListAdapter getTwoLinesListCheckItemAdapter(Context context, long id) {
		Main.openDatabase(context);
		Cursor cursor = Main.db.rawQuery("SELECT * FROM products WHERE list = "
				+ id, null);
		startManagingCursor(cursor);
		return new SimpleCursorAdapter(context,
				R.layout.two_lines_multiple_choice, cursor, new String[] {
						"name", "price" }, new int[] { R.id.text1, R.id.text2 });
	}

	private void setTotal() {
		TextSwitcher total = (TextSwitcher) findViewById(R.id.total);
		total.setFactory(this);
		total.setText(getTotal());
	}

	private String getTotal() {
		// TODO Calculate total.
		return "Bs.";
	}

	@Override
	public View makeView() {
		TextView text = new TextView(this);
		text.setTextAppearance(this, android.R.style.TextAppearance_Large);
		text.setGravity(Gravity.RIGHT);
		return text;
	}
}