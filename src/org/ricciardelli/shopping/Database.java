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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
	private static String DB_NAME = "shopping.db";
	private static int DB_VERSION = 26;

	public Database(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE lists (_id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, description TEXT)");
		db.execSQL("CREATE TABLE products (_id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, description TEXT, price DECIMAL)");
		db.execSQL("CREATE TABLE shopping (lists INTEGER NOT NULL, products INTEGER NOT NULL, FOREIGN KEY (lists) REFERENCES lists(_id), FOREIGN KEY (products) REFERENCES products(_id))");

		db.execSQL("INSERT INTO lists VALUES (null, 'Testing', 'This is a sample list.')");
		db.execSQL("INSERT INTO lists VALUES (null, 'Sample List', 'Shopping sample list.')");

		db.execSQL("INSERT INTO products VALUES (null, 'Rice', 'Sample product', 21.54)");
		db.execSQL("INSERT INTO products VALUES (null, 'Soap', 'Sample product', 13.0)");
		db.execSQL("INSERT INTO products VALUES (null, 'Mouse', 'Sample product', 87.08)");
		db.execSQL("INSERT INTO products VALUES (null, 'Pizza', 'Sample product', 20.50)");

		db.execSQL("INSERT INTO products VALUES (null, 'Apple', 'Sample product', 10.0)");
		db.execSQL("INSERT INTO products VALUES (null, 'Banana', 'Sample product', 20.50)");
		db.execSQL("INSERT INTO products VALUES (null, 'Orange', 'Sample product', 80.50)");
		db.execSQL("INSERT INTO products VALUES (20, 'Pineapple', 'Sample product', 200)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS lists");
		db.execSQL("DROP TABLE IF EXISTS products");
		db.execSQL("DROP TABLE IF EXISTS shopping");
		onCreate(db);
	}
}