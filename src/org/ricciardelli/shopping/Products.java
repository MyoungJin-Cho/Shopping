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

public class Products extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products);
		inflateList();
	}

	private void inflateList() {
		final ListView products = (ListView) findViewById(R.id.products);
		products.setAdapter(getMultipleChoiceAdapter(this));
		products.setEmptyView(findViewById(R.id.noProducts));
	}

	private ListAdapter getMultipleChoiceAdapter(Context context) {
		Main.openDatabase(context);
		Cursor cursor = Main.db.rawQuery("SELECT * FROM products", null);
		startManagingCursor(cursor);
		return new SimpleCursorAdapter(context,
				android.R.layout.simple_list_item_multiple_choice, cursor,
				new String[] { "name" }, new int[] { android.R.id.text1 });
	}

	private void addProducts(Context context, long listId, long productId) {
		Main.openDatabase(context);
		Main.db.execSQL("UPDATE products SET list = " + listId
				+ " WHERE _id = " + productId);
		Main.closeDatabase();
	}

	@Override
	public void onClick(View v) {
		// TODO Create methods to achieve this.
		addProducts(Products.this, 2, 1);
	}
}