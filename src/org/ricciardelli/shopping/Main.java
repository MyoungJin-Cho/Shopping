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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Main extends CRUD {
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
			showForm(getListsKey());
			return super.onOptionsItemSelected(item);
		case R.id.addProducts:
			showForm(getProductsKey());
			return super.onOptionsItemSelected(item);
		case R.id.preferences:
			showActivity(this, Preferences.class, null, 0);
			return super.onOptionsItemSelected(item);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private ListAdapter getTwoLineListItemAdapter(Context context) {
		Cursor cursor = getAllFromTable(getListsTable());
		startManagingCursor(cursor);
		return new SimpleCursorAdapter(context, R.layout.two_line_list_item,
				cursor, new String[] { "name", "description" }, new int[] {
						R.id.text1, R.id.text2 });
	}

	private void inflateList() {
		final ListView shoppingLists = (ListView) findViewById(R.id.shoppingLists);
		shoppingLists.setAdapter(getTwoLineListItemAdapter(this));
		shoppingLists.setEmptyView(findViewById(R.id.noLists));

		shoppingLists.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				optionsBuilder(Main.this, id);
				return false;
			}

		});

		shoppingLists.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showActivity(Main.this, Shopping.class,
						getName(getListsTable(), id), id);
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
									showForm(getListsKey());
									break;
								case 1:
									showActivity(context, Shopping.class,
											getName(getListsTable(), id), id);
									break;
								case 2:
									showActivity(context, Form.class, id,
											getListsKey());
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
										getName(getListsTable(), id)));
								delete(getListsTable(), id);
								remove(id);
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

	private void showForm(byte key) {
		if (key > 0)
			showActivity(this, Form.class, getProductsTable(), key);
		else
			showActivity(this, Form.class, getListsTable(), key);
	}
}