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
	private static final String LISTS_TABLE = "lists";
	private static final String PRODUCTS_TABLE = "products";
	private static final String SHOPPING_TABLE = "shopping";
	private static final String EXTRA_NAME = "name";
	private static final String EXTRA_KEY = "key";
	private static final String EXTRA_ID = "id";
	private static final String EXTRA_UPDATE = "update";
	private static final byte LISTS_KEY = 0;
	private static final byte PRODUCTS_KEY = 1;
	private static final boolean UPDATE_KEY = true;
	private Database helper;
	private SQLiteDatabase db;

	public static String getExtraKey() {
		return EXTRA_KEY;
	}

	public static String getExtraUpdate() {
		return EXTRA_UPDATE;
	}

	public static boolean isUpdateKey() {
		return UPDATE_KEY;
	}

	public static String getListsTable() {
		return LISTS_TABLE;
	}

	public static String getProductsTable() {
		return PRODUCTS_TABLE;
	}

	public static String getShoppingTable() {
		return SHOPPING_TABLE;
	}

	public static byte getListsKey() {
		return LISTS_KEY;
	}

	public static byte getProductsKey() {
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
		Cursor cursor = db.rawQuery("SELECT price FROM " + PRODUCTS_TABLE
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

	public Cursor getAllFromTable(String table) {
		openDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);
		// closeDatabase();
		return cursor;
	}

	public void update(String table, ContentValues values, long id) {
		openDatabase();
		db.update(table, values, BaseColumns._ID + " = " + id, null);
		values.clear();
		closeDatabase();
	}

	public void delete(String table, long id) {
		openDatabase();
		db.delete(table, BaseColumns._ID + " = " + id, null);
		closeDatabase();
	}

	public void remove(String column, long id) {
		openDatabase();
		db.delete(SHOPPING_TABLE, column + " = " + id, null);
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

	public void showActivity(Context context, Class<?> c, String name, byte key) {
		Intent intent = new Intent(context, c);
		intent.putExtra(EXTRA_NAME, name);
		intent.putExtra(EXTRA_KEY, key);
		startActivity(intent);
	}

	public void showActivity(Context context, Class<?> c, long id, byte key) {
		Intent intent = new Intent(context, c);
		intent.putExtra(EXTRA_ID, id);
		intent.putExtra(EXTRA_KEY, key);
		intent.putExtra(EXTRA_UPDATE, UPDATE_KEY);
		startActivity(intent);
	}
}