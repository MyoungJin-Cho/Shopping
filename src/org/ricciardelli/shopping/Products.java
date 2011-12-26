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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Products extends Activity implements OnClickListener {
	ListView products;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products);
		inflateList();
	}

	private long getListId() {
		return getIntent().getExtras().getLong("id");
	}

	private void inflateList() {
		products = (ListView) findViewById(R.id.products);
		products.setAdapter(getMultipleChoiceAdapter(this));
		products.setEmptyView(findViewById(R.id.noProducts));
	}

	private ListAdapter getMultipleChoiceAdapter(Context context) {
		// TODO Create a method or something like that to get the products that
		// are not related with the current shopping list.
		Main.openDatabase(context);
		Cursor cursor = Main.db.rawQuery("SELECT * FROM products", null);
		startManagingCursor(cursor);
		return new SimpleCursorAdapter(context,
				android.R.layout.simple_list_item_multiple_choice, cursor,
				new String[] { "name" }, new int[] { android.R.id.text1 });
	}

	private void addProducts(Context context, long productId) {
		Main.openDatabase(context);
		if (!Main.db.rawQuery(
				"SELECT * FROM shopping WHERE lists = " + getListId()
						+ " AND products = " + productId + "", null)
				.moveToFirst())
			Main.db.execSQL("INSERT INTO shopping VALUES (" + getListId() + ","
					+ productId + " )");
		Main.closeDatabase();
	}

	private long[] getCheckedItemIds() {
		long[] ids = new long[products.getCheckedItemPositions().size()];
		short j = 0;
		for (short i = 0; i < products.getCheckedItemPositions().size(); i++) {
			if (products.getCheckedItemPositions().valueAt(i)) {
				ids[j] = products.getCheckedItemPositions().keyAt(i) + 1;
				j++;
			}
		}
		return ids;
	}

	@Override
	public void onClick(View v) {
		for (short i = 0; i < getCheckedItemIds().length; i++)
			addProducts(Products.this, getCheckedItemIds()[i]);
		Toast.makeText(this, "Products added.", Toast.LENGTH_SHORT).show();
		finish();
	}
}
