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
		setTitle(getString(R.string.add_new, getName()));
		if (getId() == 0)
			findViewById(R.id.price).setVisibility(View.GONE);
	}

	private String getName() {
		return getIntent().getExtras().getString("name").toString();
	}

	private long getId() {
		return getIntent().getExtras().getLong("id");
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
		if (getId() != 0)
			values.put("price", getFormField(R.id.price));
		create(getName(), values);
		notification(this,
				getString(R.string.item_created, getFormField(R.id.name)));
		finish();
	}

	private boolean validateFields(String field) {
		return field.trim().equals("");
	}

	private void validateForm() {
		if (validateFields(getFormField(R.id.name))) {
			notification(this,
					getString(R.string.error_message, getString(R.string.name)));
		} else if (validateFields(getFormField(R.id.price)) && getId() != 0) {
			notification(
					this,
					getString(R.string.error_message, getString(R.string.price)));
		} else {
			save();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			validateForm();
			break;
		case R.id.cancel:
			finish();
			break;
		}
	}
}