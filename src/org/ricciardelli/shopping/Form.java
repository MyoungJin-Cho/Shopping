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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Form extends Activity implements OnClickListener {

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

	private void save() {
		// if (getId() == 0) {
		// getFormName();
		// getFormDescription();
		// getFormPrice();
		// } else {
		// getFormName();
		// getFormDescription();
		// }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			// if validation is ok then call save()
			break;
		case R.id.cancel:
			finish();
			break;
		}
	}
}