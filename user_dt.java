package com.ikainui.easyquest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class user_dt extends Activity {

	TextView textView1;
	EditText editname;
	EditText editlastname;
	EditText editphone;
	EditText editemail;
	EditText editaddr;
	DatabaseHelper dbHeplper;
	Spinner spinner;
	Spinner spinner_district;
	Spinner spinner_subdistrict;
	TextView textView_zipcode;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().getBooleanExtra("EXIT", false)) {
			
			
	         finish();
	    }
		setContentView(R.layout.user_dt);

		spinner_subdistrict = (Spinner) findViewById(R.id.spinner3);
		spinner_district = (Spinner) findViewById(R.id.spinner2);
		spinner = (Spinner) findViewById(R.id.spinner1);

		dbHeplper = new DatabaseHelper(getApplicationContext());
		try {
			dbHeplper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}

		spinner = (Spinner) findViewById(R.id.spinner1);
		List<String> labels = dbHeplper.getAllUsers();

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.drpdown_item, labels);

		// Drop down layout style - list view with radio button
		// dataAdapter.setDropDownViewResource(android.R.layout.dro);
		dataAdapter.setDropDownViewResource(R.layout.drpdown_item);

		// attaching data adapter to spinner
		spinner.setAdapter(dataAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0,
					View selectedItemView, int position, long id) {
				// TODO Auto-generated method stub
				String valToSet = spinner.getSelectedItem().toString();
				// Log.e("LOG", valToSet);

				List<String> district_txt = dbHeplper.select_district(valToSet);
				show_select_district(district_txt);

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

	public void show_select_district(List<String> acct) {

		spinner_district = (Spinner) findViewById(R.id.spinner2);
		// Log.e("show", acct.get(0));
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.drpdown_item, acct);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(R.layout.drpdown_item);

		// attaching data adapter to spinner
		spinner_district.setAdapter(dataAdapter);

		spinner_district
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0,
							View selectedItemView, int position, long id) {
						// TODO Auto-generated method stub
						String valToSet_p = spinner.getSelectedItem()
								.toString();
						String valToSet_d = spinner_district.getSelectedItem()
								.toString();
						// /Log.e("LOG", valToSet);

						List<String> subdistrict_txt = dbHeplper
								.select_subdistrict(valToSet_p, valToSet_d);
						// Log.e("Log", subdistrict_txt.get(0));
						show_select_subdistrict(subdistrict_txt);

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});

	}

	public void show_select_subdistrict(List<String> acct) {
		spinner_subdistrict = (Spinner) findViewById(R.id.spinner3);
		spinner_district = (Spinner) findViewById(R.id.spinner2);
		spinner = (Spinner) findViewById(R.id.spinner1);
		textView_zipcode = (TextView) findViewById(R.id.textView_zip);

		// Log.e("show", acct.get(0));
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.drpdown_item, acct);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(R.layout.drpdown_item);

		// attaching data adapter to spinner
		spinner_subdistrict.setAdapter(dataAdapter);

		String valToSet_p = spinner.getSelectedItem().toString();
		String valToSet_d = spinner_district.getSelectedItem().toString();

		String zipcode_txt = dbHeplper.select_zipcode(valToSet_p, valToSet_d);
		textView_zipcode.setText(zipcode_txt);

	}

	public void save_user(View v) {
		Log.e("log", "ddddd");
		editname = (EditText) findViewById(R.id.editname);
		editlastname = (EditText) findViewById(R.id.editlastname);
		editphone = (EditText) findViewById(R.id.editphone);
		editemail = (EditText) findViewById(R.id.editemail);
		editaddr = (EditText) findViewById(R.id.editaddr);
		textView_zipcode = (TextView) findViewById(R.id.textView_zip);
		spinner_subdistrict = (Spinner) findViewById(R.id.spinner3);
		spinner_district = (Spinner) findViewById(R.id.spinner2);
		spinner = (Spinner) findViewById(R.id.spinner1);

		String Sname = editname.getText().toString();
		String Slastname = editlastname.getText().toString();
		String Sphone = editphone.getText().toString();
		String Smail = editemail.getText().toString();
		String Saddr = editaddr.getText().toString();
		String valToSet_p = spinner.getSelectedItem().toString();
		String valToSet_d = spinner_district.getSelectedItem().toString();
		String valToSet_s = spinner_subdistrict.getSelectedItem().toString();
		String edit_zipcode = textView_zipcode.getText().toString();

		if (Sname.matches("") && Slastname.matches("") && Sphone.matches("")
				&& Smail.matches("")) {

			Toast.makeText(this, "กรุณากรอกข้อมูล.",
					Toast.LENGTH_SHORT).show();
		} else {

			try {

				String path = "/storage/sdcard0/questionnaire/answer.csv";

				File file = new File(path);

				/*** if exists create text file ***/

				if (!file.exists())

				{

					file.createNewFile();
					/*** Write Text File ***/

					FileWriter writer = new FileWriter(file, true); // True =
																	// Append to
																	// file,
																	// false =
																	// Overwrite

					writer.write(Sname + "," + Slastname + "," + Sphone + ","
							+ Smail +","+Saddr+ "," + valToSet_p + "," + valToSet_d + ","
							+ valToSet_s + "," + edit_zipcode + ",");
					writer.close();
				} else {

					/*** Write Text File ***/

					FileWriter writer = new FileWriter(file, true); // True =
																	// Append to
																	// file,
																	// false =
																	// Overwrite

					writer.write("\r\n" + Sname + "," + Slastname + ","
							+ Sphone + "," + Smail +","+Saddr+ "," + valToSet_p + ","
							+ valToSet_d + "," + valToSet_s + ","
							+ edit_zipcode + ",");
					writer.close();
				}

			} catch (IOException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			Intent i = new Intent(user_dt.this, CSVParsingExampleActivity.class);
			startActivity(i);
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        
	        case R.id.action_settings:
	        	int p = android.os.Process.myPid();
	        	android.os.Process.killProcess(p);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
