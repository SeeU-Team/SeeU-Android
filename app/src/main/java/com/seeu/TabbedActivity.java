package com.seeu;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.login.LoginManager;
import com.seeu.common.subviews.Mark;
import com.seeu.member.Member;
import com.seeu.member.MemberHasTeam;
import com.seeu.member.MemberStatus;
import com.seeu.member.edit.EditMemberProfileActivity;
import com.seeu.member.profile.MemberProfileActivity;
import com.seeu.messages.MessagesFragment;
import com.seeu.nightcenter.NightCenterFragment;
import com.seeu.team.TeamService;
import com.seeu.teamwall.TeamWallFragment;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.ImageUtils;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Created by thomasfouan on 16/03/2018.
 *
 * Main activity of the application. This activity manages the bottom navigation menu where the user can switch between :
 * 		- the teamwall
 * 		- the messages
 * 		- the night center
 */
public class TabbedActivity extends AppCompatActivity implements CustomResponseListener<MemberHasTeam> {

	public static boolean isAppRunning;
	private Fragment selectedFragment;

	private Member currentUser;
	private TeamService teamService;

	private BottomNavigationView navigationView;
	private MenuItem nightCenterMenuItem;

	private ImageView memberProfilePicture;
	private DownloadImageAndSetBackgroundTask asyncTask;

	private boolean loadFirstFragment;
	private boolean isAlreadyLoadingMemberTeam;

	/**
	 * Listener for the bottom navigation menu. Switch between the fragments on user clicks.
	 */
	private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = (item) -> {
		Fragment newSelectedFragment = null;

		MemberHasTeam memberHasTeam = SharedPreferencesManager.getObject(this, MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);
		if (null == memberHasTeam) {
			return false;
		}

		switch (item.getItemId()) {
			case R.id.navigation_teamwall:
				newSelectedFragment = MemberStatus.ALONE.equals(memberHasTeam.getStatus())
						? new NoTeamFragment()
						: new TeamWallFragment();
				break;

			case R.id.navigation_messages:
				newSelectedFragment = new MessagesFragment();
				break;

			case R.id.navigation_nightcenter:
				newSelectedFragment = new NightCenterFragment();
		}

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		if (null != newSelectedFragment
				&& null != selectedFragment) {
			transaction.remove(selectedFragment);
		}

		transaction.replace(R.id.frame_layout, newSelectedFragment);
		transaction.commit();

		selectedFragment = newSelectedFragment;

		return true;
	};

	/**
	 * Listener for the left navigation menu that appears on click on the hamburger menu.
	 */
	private NavigationView.OnNavigationItemSelectedListener onLeftMenuNavigationItemSelectedListener = (item) -> {
		Intent intent;
		// Handle navigation view item clicks here.
		switch (item.getItemId()) {
			case R.id.nav_member_profile:
				intent = new Intent(this, MemberProfileActivity.class);
				intent.putExtra(Member.STORAGE_KEY, currentUser);

				startActivity(intent);
				break;

			case R.id.nav_edit_member_profile:
				intent = new Intent(this, EditMemberProfileActivity.class);
				intent.putExtra(Member.STORAGE_KEY, currentUser);

				startActivity(intent);
				break;

			case R.id.nav_team_profile:
				break;

			case R.id.nav_disconnection:
				LoginManager.getInstance().logOut();
				finishAndRemoveTask();
				break;
		}

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbed_activity);

		isAppRunning = true;

		navigationView = findViewById(R.id.navigation);
		navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		nightCenterMenuItem = navigationView.getMenu().findItem(R.id.navigation_nightcenter);
		// By default, hide the night center menu
		navigationView.getMenu().removeItem(nightCenterMenuItem.getItemId());

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(onLeftMenuNavigationItemSelectedListener);
		View navHeader = navigationView.getHeaderView(0);

		teamService = new TeamService(this);
		currentUser = SharedPreferencesManager.getEntity(this, Member.STORAGE_KEY, Member.class);
		loadMemberTeam(true);
		loadLeftNavViewHeader(navHeader);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Refresh member's team when user navigates through menu items
		if (!isAlreadyLoadingMemberTeam) {
			loadMemberTeam(false);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != asyncTask) {
			asyncTask.cancelDownload();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isAppRunning = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// When got result from NoTeamFragment, reload the team of the user
		if (NoTeamFragment.TEAM_CREATION_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode) {
			loadMemberTeam(true);
		}
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
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
		// Handle action bar item clicks here.
		// The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_settings:
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void loadLeftNavViewHeader(View view) {
		memberProfilePicture = view.findViewById(R.id.memberPicture);
		TextView memberMarkView = view.findViewById(R.id.memberMark);
		TextView memberName = view.findViewById(R.id.memberName);
		Mark mark = new Mark(memberMarkView);

		ImageUtils.runJustBeforeBeingDrawn(memberProfilePicture, () -> {
			asyncTask = new DownloadImageAndSetBackgroundTask(memberProfilePicture,100);
			asyncTask.execute(currentUser.getProfilePhotoUrl());
		});

		mark.setMark(currentUser.getMark());
		memberName.setText(currentUser.getName());
	}

	private void updateNightCenterMenuVisibility() {
		MemberHasTeam memberHasTeam = SharedPreferencesManager.getObject(this, MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);

		if (!MemberStatus.ALONE.equals(memberHasTeam.getStatus())
				&& memberHasTeam.getTeam().isMerged()) {

			// Add the night center menu if it has not be added yet
			MenuItem navMenuItem = navigationView.getMenu().findItem(nightCenterMenuItem.getItemId());
			if (null == navMenuItem) {
				navigationView.getMenu()
						.add(nightCenterMenuItem.getGroupId(),
								nightCenterMenuItem.getItemId(),
								nightCenterMenuItem.getOrder(),
								nightCenterMenuItem.getTitle())
						.setIcon(nightCenterMenuItem.getIcon());
			}
		} else {
			navigationView.getMenu()
					.removeItem(nightCenterMenuItem.getItemId());
		}
	}

	private void loadMemberTeam(boolean loadFirstFragment) {
		this.loadFirstFragment = loadFirstFragment;
		teamService.getTeam(currentUser, this);
		isAlreadyLoadingMemberTeam = true;
	}

	@Override
	public void onHeadersResponse(Map<String, String> headers) {
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		if (null != error.networkResponse
				&& HttpURLConnection.HTTP_NOT_FOUND == error.networkResponse.statusCode) {
			MemberHasTeam memberHasTeam = new MemberHasTeam();
			memberHasTeam.setMemberId(currentUser.getId());
			memberHasTeam.setStatus(MemberStatus.ALONE);

			SharedPreferencesManager.putObject(this, MemberHasTeam.STORAGE_KEY, memberHasTeam);
			updateNightCenterMenuVisibility();

			// Manually displaying the NoTeam fragment - one time only
			if (loadFirstFragment) {
				selectedFragment = new NoTeamFragment();

				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.frame_layout, selectedFragment);
				transaction.commit();
			}
		}
		isAlreadyLoadingMemberTeam = false;
	}

	@Override
	public void onResponse(MemberHasTeam response) {
		SharedPreferencesManager.putObject(this, MemberHasTeam.STORAGE_KEY, response);
		updateNightCenterMenuVisibility();

		//Manually displaying the TeamWall fragment - one time only
		if (loadFirstFragment) {
			selectedFragment = new TeamWallFragment();

			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.frame_layout, selectedFragment);
			transaction.commit();
		}
		isAlreadyLoadingMemberTeam = false;
	}
}
