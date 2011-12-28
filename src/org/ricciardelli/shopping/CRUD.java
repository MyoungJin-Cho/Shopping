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
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.widget.Toast;

public class CRUD extends Activity {
	private static final String LISTS = "lists";
	private static final String PRODUCTS = "products";
	private static final String EXTRA_NAME = "name";
	private static final String EXTRA_ID = "id";
	private static final long LISTS_KEY = 0;
	private static final long PRODUCTS_KEY = 1;
	private Database helper;
	private SQLiteDatabase db;

	public static String getLists() {
		return LISTS;
	}

	public static String getProducts() {
		return PRODUCTS;
	}

	public static long getListsKey() {
		return LISTS_KEY;
	}

	public static long getProductsKey() {
		return PRODUCTS_KEY;
	}

	public static String getExtraName() {
		return EXTRA_NAME;
	}

	public static String getExtraId() {
		return EXTRA_ID;
	}

	private void openDatabase() {
		helper = new Database(this);
		db = helper.getWritableDatabase();
	}

	private void closeDatabase() {
		db.close();
		helper.close();
	}

	public String getName(String table, long id) {
		openDatabase();
		Cursor cursor = db.rawQuery("SELECT name FROM " + table
				+ " WHERE _id = " + id, null);
		cursor.moveToFirst();
		String name = cursor.getString(0).toString();
		cursor.close();
		closeDatabase();
		return name;
	}

	public String getDescription(String table, long id) {
		openDatabase();
		Cursor cursor = db.rawQuery("SELECT description FROM " + table
				+ " WHERE _id = " + id, null);
		cursor.moveToFirst();
		String description = cursor.getString(0).toString();
		cursor.close();
		closeDatabase();
		return description;
	}

	public double getPrice(long id) {
		openDatabase();
		Cursor cursor = db.rawQuery("SELECT price FROM " + PRODUCTS
				+ " WHERE _id = " + id, null);
		cursor.moveToFirst();
		double price = cursor.getDouble(0);
		closeDatabase();
		return price;
	}

	public void create(String table, ContentValues values) {
		openDatabase();
		db.insert(table, null, values);
		values.clear();
		closeDatabase();
	}

	public Cursor getAllRows(String table) {
		openDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);
		// closeDatabase();
		return cursor;
	}

	public void update(String table, ContentValues values, long id) {
		openDatabase();
		db.update(table, values, BaseColumns._ID, null);
		closeDatabase();
	}

	public void delete(String table, long id) {
		openDatabase();
		db.execSQL("DELETE FROM " + table + " WHERE _id = " + id);
		closeDatabase();
	}

	public void notification(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public void showActivity(Context context, Class<?> c, String name, long id) {
		Intent intent = new Intent(context, c);
		intent.putExtra(EXTRA_NAME, name);
		intent.putExtra(EXTRA_ID, id);
		startActivity(intent);
	}
}