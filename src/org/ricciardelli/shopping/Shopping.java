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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Shopping extends Activity {
	private Database helper;
	private SQLiteDatabase db;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		inflateList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addList:
			return super.onOptionsItemSelected(item);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openDatabase(Context context) {
		helper = new Database(context);
		db = helper.getWritableDatabase();
	}

	private void closeDatabase() {
		db.close();
		helper.close();
	}

	private ListAdapter getTwoLineListItemAdapter(Context context, String sql) {
		openDatabase(context);
		Cursor cursor = db.rawQuery(sql, null);
		startManagingCursor(cursor);
		return new SimpleCursorAdapter(context, R.layout.two_lines, cursor,
				new String[] { "name", "description" }, new int[] { R.id.text1,
						R.id.text2 });
	}

	private void inflateList() {
		ListView shoppingLists = (ListView) findViewById(R.id.shoppingLists);
		shoppingLists.setAdapter(getTwoLineListItemAdapter(this,
				"SELECT * FROM lists"));
		shoppingLists.setEmptyView(findViewById(R.id.noLists));

		shoppingLists.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// alertBuilder();
				return false;
			}

		});
	}
}