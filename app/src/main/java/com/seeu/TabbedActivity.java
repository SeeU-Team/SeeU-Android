package com.seeu;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TabbedActivity extends AppCompatActivity {

	private TextView mTextMessage;

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = (item) -> {
		switch (item.getItemId()) {
			case R.id.navigation_home:
				mTextMessage.setText(R.string.title_home);
				return true;
			case R.id.navigation_dashboard:
				mTextMessage.setText(R.string.title_dashboard);
				return true;
			case R.id.navigation_notifications:
				mTextMessage.setText(R.string.title_notifications);
				return true;
		}
		return false;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabbed);

		mTextMessage = findViewById(R.id.message);
		BottomNavigationView navigation = findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
	}

}
