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

import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class Shopping extends CRUD implements ViewFactory {
	private Cursor mCursor;
	private double mTotal;
	private TextSwitcher mTotalSwitcher;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		setCurrentTotal(getId(), mTotal);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping);
		setCurrentTotal(getId(), getCurrentTotal(getId()));
		mTotal = getCurrentTotal(getId());
		Log.i("ID", "ID" + getId());
		Log.i("GET TOTAL", "Total => " + getCurrentTotal(getId()));
		setTitle(getName());
		inflateList(getId());
	}

	private String getName() {
		return getIntent().getExtras().getString("name").toString();
	}

	private long getId() {
		return getIntent().getExtras().getLong("id");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.shopping, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addProducts:
			showActivity(this, Products.class, getName(), getId());
			return super.onOptionsItemSelected(item);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void inflateList(long id) {
		mTotalSwitcher = (TextSwitcher) findViewById(R.id.total);
		mTotalSwitcher.setFactory(this);
		final ListView shopping = (ListView) findViewById(R.id.shopping);
		shopping.setAdapter(getTwoLinesListCheckItemAdapter(this, id));
		shopping.setEmptyView(findViewById(R.id.noProducts));
		shopping.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (shopping.getCheckedItemPositions().get(position)) {
					calculateTotal(getPrice(id));
					setTotal();
				} else {
					calculateTotal(-getPrice(id));
					setTotal();
				}
			}
		});

		shopping.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				optionsBuilder(Shopping.this, id, shopping
						.getCheckedItemPositions().get(position));
				return false;
			}
		});
	}

	private void optionsBuilder(final Context context, final long id,
			final boolean checked) {
		new AlertDialog.Builder(context)
				.setItems(context.getResources().getStringArray(R.array.item),
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									// editQuantity()
									break;
								case 1:
									notification(context, context.getString(
											R.string.item_removed,
											getName(getProductsTable(), id)));
									remove(getProductsTable(), id);
									if (checked) {
										calculateTotal(-getPrice(id));
										setTotal();
									}
									mCursor.requery();
									break;
								}
							}
						}).create().show();
	}

	private double calculateTotal(double price) {
		return mTotal += price;
	}

	// TODO This shouldn't be done this way.
	private ListAdapter getTwoLinesListCheckItemAdapter(Context context, long id) {
		Database helper = new Database(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		mCursor = db
				.rawQuery(
						"SELECT products.* FROM products INNER JOIN shopping ON shopping.products = products._id WHERE shopping.lists = "
								+ id, null);
		startManagingCursor(mCursor);
		return new SimpleCursorAdapter(context,
				R.layout.two_line_multiple_choice, mCursor, new String[] {
						"name", "price" }, new int[] { R.id.text1, R.id.text2 });
	}

	private void setTotal() {
		mTotalSwitcher.setText("Bs. " + getTotal());
	}

	private String getTotal() {
		return new DecimalFormat("####.##").format(Math.abs(mTotal));
	}

	private TextWatcher setWatcher() {
		return new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		};
	}

	@Override
	public View makeView() {
		TextView text = new TextView(this);
		text.setTextAppearance(this, android.R.style.TextAppearance_Large);
		text.setGravity(Gravity.RIGHT);
		text.addTextChangedListener(setWatcher());
		return text;
	}
}