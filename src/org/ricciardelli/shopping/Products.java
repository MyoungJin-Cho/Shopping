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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Products extends CRUD implements View.OnClickListener {
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
		products.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				optionsBuilder(Products.this, id);
				return false;
			}
		});
	}

	private void optionsBuilder(final Context context, final long id) {
		new AlertDialog.Builder(context)
				.setItems(context.getResources().getStringArray(R.array.crud),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									showActivity(context, Form.class,
											getProducts(), getProductsKey());
									break;
								case 1:
									// READ ?
									break;
								case 2:
									showActivity(context, Form.class, id,
											getProductsKey());
									break;
								case 3:
									confirmationBuilder(context, id);
									break;
								}
							}

						}).create().show();
	}

	private void confirmationBuilder(final Context context, final long id) {
		new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.confirmation_title))
				.setMessage(context.getString(R.string.confirmation_message))
				.setPositiveButton(context.getString(R.string.yes),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								notification(context, context.getString(
										R.string.item_deleted,
										getName(getProducts(), id)));
								delete(getProducts(), id);
								inflateList();
							}
						})
				.setNegativeButton(context.getString(R.string.no),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).setCancelable(false).create().show();
	}

	private ListAdapter getMultipleChoiceAdapter(Context context) {
		// TODO Create a method or something like that to get the products that
		// are not related with the current shopping list.
		Cursor cursor = getAllFromTable(getProducts());
		startManagingCursor(cursor);
		return new SimpleCursorAdapter(context,
				android.R.layout.simple_list_item_multiple_choice, cursor,
				new String[] { "name" }, new int[] { android.R.id.text1 });
	}

	// TODO This shouldn't be done this way.
	private void addProducts(Context context, long productId) {
		Database helper = new Database(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		if (!db.rawQuery(
				"SELECT * FROM shopping WHERE lists = " + getListId()
						+ " AND products = " + productId + "", null)
				.moveToFirst()
				&& productId != 0)
			db.execSQL("INSERT INTO shopping VALUES (" + getListId() + ","
					+ productId + " )");
		helper.close();
		db.close();
	}

	private long[] getCheckedItemIds() {
		long[] ids = new long[products.getCheckedItemPositions().size()];
		short j = 0;
		for (short i = 0; i < products.getCount(); i++)
			if (products.getCheckedItemPositions().get(i)) {
				ids[j] = products.getItemIdAtPosition(i);
				j++;
			}
		return ids;
	}

	private int getSize(long[] array) {
		int size = 0;
		for (long item : array)
			if (item == 0)
				break;
			else
				size++;
		return size;
	}

	@Override
	public void onClick(View v) {
		for (short i = 0; i < getCheckedItemIds().length; i++)
			addProducts(Products.this, getCheckedItemIds()[i]);
		notification(
				this,
				getResources().getQuantityString(R.plurals.products_added,
						getSize(getCheckedItemIds()),
						getSize(getCheckedItemIds())));
		finish();
	}
}