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

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Form extends CRUD implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form);
		if (getUpdateKey() && getKey() == 0) {
			setTitle(getString(R.string.update_item,
					getName(getListsTable(), getId())));
			findViewById(R.id.save).setVisibility(View.GONE);
			findViewById(R.id.update).setVisibility(View.VISIBLE);
			setFormField(R.id.name, getName(getListsTable(), getId()));
			setFormField(R.id.description,
					getDescription(getListsTable(), getId()));
		} else if (getUpdateKey() && getKey() != 0) {
			setTitle(getString(R.string.update_item,
					getName(getProductsTable(), getId())));
			findViewById(R.id.save).setVisibility(View.GONE);
			findViewById(R.id.update).setVisibility(View.VISIBLE);
			setFormField(R.id.name, getName(getProductsTable(), getId()));
			setFormField(R.id.description,
					getDescription(getProductsTable(), getId()));
			setFormField(R.id.price, String.valueOf(getPrice(getId())));
		} else {
			setTitle(getString(R.string.add_new, getTable()));
		}
		if (getKey() == 0)
			findViewById(R.id.price).setVisibility(View.GONE);
	}

	private boolean getUpdateKey() {
		return getIntent().getExtras().getBoolean("update");
	}

	private byte getKey() {
		return getIntent().getExtras().getByte("key");
	}

	private long getId() {
		return getIntent().getExtras().getLong("id");
	}

	private String getTable() {
		return getIntent().getExtras().getString("name").toString();
	}

	private String getFormField(int res) {
		return ((EditText) findViewById(res)).getText().toString();
	}

	private void setFormField(int res, String text) {
		((EditText) findViewById(res)).setText(text);
	}

	private void save() {
		ContentValues values = new ContentValues();
		values.put("name", getFormField(R.id.name));
		values.put("description", getFormField(R.id.description));
		if (getKey() != 0)
			values.put("price", getFormField(R.id.price));
		create(getTable(), values);
		notification(this,
				getString(R.string.item_created, getFormField(R.id.name)));
		finish();
	}

	private void update() {
		ContentValues values = new ContentValues();
		String table = getListsTable();
		values.put("name", getFormField(R.id.name));
		values.put("description", getFormField(R.id.description));
		if (getKey() != 0) {
			values.put("price", getFormField(R.id.price));
			table = getProductsTable();
		}
		update(table, values, getId());
		notification(this,
				getString(R.string.item_updated, getFormField(R.id.name)));
		finish();
	}

	private boolean validateFields(String field) {
		return field.trim().equals("");
	}

	private boolean validateForm() {
		if (validateFields(getFormField(R.id.name))) {
			notification(this,
					getString(R.string.error_message, getString(R.string.name)));
		} else if (validateFields(getFormField(R.id.price)) && getKey() != 0) {
			notification(
					this,
					getString(R.string.error_message, getString(R.string.price)));
		} else {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			if (validateForm())
				save();
			break;
		case R.id.update:
			if (validateForm())
				update();
			break;
		case R.id.cancel:
			finish();
			break;
		}
	}
}