package co.minesweepers.chumessage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MessagesActivity extends AppCompatActivity implements View.OnClickListener, MyAdapter.Callback, ActivityCompat.OnRequestPermissionsResultCallback {

	private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

	private RecyclerView mRecyclerView;
	private MyAdapter mAdapter;

	private String mClickedText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mAdapter = new MyAdapter(this);
		mRecyclerView.setAdapter(mAdapter);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_messages, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MessagesActivity.this);
		builder.setTitle("Enter new message");

		final EditText input = new EditText(MessagesActivity.this);
		input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		builder.setView(input);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mAdapter.add(input.getText().toString());
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.show();
	}

	private String getNumber() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(getAssets().open("number")));
			return reader.readLine(); // there is just one line in the file
		} catch (IOException e) {
			Log.e("MessagesActivity", "Create a file name number in assets folder and put in a number of format +13334445555");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					//log the exception
				}
			}
		}

		return null;
	}

	@Override
	public void onClick(String text) {
		mClickedText = text;
		int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
		if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
			sendSMS();
		} else {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_SEND_SMS: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					sendSMS();
				} else {
					showToast("Need sms permission to send message");
				}
			}
		}
	}

	private void sendSMS() {
		SmsManager smsManager = SmsManager.getDefault();
		String number = getNumber();
		if (Util.isEmpty(number)) {
			showToast("Cannot sent sms to empty number");
			return;
		}
		smsManager.sendTextMessage(number, null, mClickedText, null, null);
		showToast("Message sent");
	}

	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
}
