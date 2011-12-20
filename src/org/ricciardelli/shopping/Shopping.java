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
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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
		ListView shopping = (ListView) findViewById(R.id.shopping);
		shopping.setAdapter(getAdapter(this, id));
	}

	private ListAdapter getAdapter(Context context, long id) {
		return new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, getProducts(
						context, id));
	}

	private String[] getProducts(Context context, long id) {
		Main.openDatabase(context);
		Cursor cursor = Main.db.rawQuery("SELECT * FROM products WHERE list = "
				+ id, null);
		String[] products = new String[cursor.getCount()];
		short i = 0;
		if (cursor.moveToFirst())
			do {
				products[i] = cursor.getString(1) + " - Bs. "
						+ cursor.getString(3);
				i++;
			} while (cursor.moveToNext());
		Main.closeDatabase();
		cursor.close();
		return products;
	}

	private void setTotal() {
		TextSwitcher total = (TextSwitcher) findViewById(R.id.total);
		total.setFactory(this);
	}

	@Override
	public View makeView() {
		TextView text = new TextView(this);
		text.setTextAppearance(this, android.R.style.TextAppearance_Large);
		text.setGravity(Gravity.RIGHT);
		return text;
	}
}