package com.seeu;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
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

import com.android.volley.VolleyError;
import com.seeu.member.Member;
import com.seeu.member.MemberHasTeam;
import com.seeu.member.MemberStatus;
import com.seeu.member.edit.EditMemberProfileActivity;
import com.seeu.messages.MessagesFragment;
import com.seeu.nightcenter.NightCenterFragment;
import com.seeu.team.TeamService;
import com.seeu.teamwall.TeamWallFragment;
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

	private Member currentUser;
	private TeamService teamService;

	private BottomNavigationView navigationView;
	private MenuItem nightCenterMenuItem;

	/**
	 * Listener for the bottom navigation menu. Switch between the fragments on user clicks.
	 */
	private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = (item) -> {
		Fragment selectedFragment = null;

		switch (item.getItemId()) {
			case R.id.navigation_teamwall:
				MemberHasTeam memberHasTeam = SharedPreferencesManager.getEntity(this, MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);
				if (null == memberHasTeam) {
					return false;
				}

				selectedFragment = MemberStatus.ALONE.equals(memberHasTeam.getStatus())
						? new NoTeamFragment()
						: new TeamWallFragment();
				break;

			case R.id.navigation_messages:
				selectedFragment = new MessagesFragment();
				break;

			case R.id.navigation_nightcenter:
				selectedFragment = new NightCenterFragment();
		}

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.frame_layout, selectedFragment);
		transaction.commit();

		return true;
	};

	/**
	 * Listener for the left navigation menu that appears on click on the hamburger menu.
	 */
	private NavigationView.OnNavigationItemSelectedListener onLeftMenuNavigationItemSelectedListener = (item) -> {
		// Handle navigation view item clicks here.
		switch (item.getItemId()) {
			case R.id.nav_camera:
				// Handle the camera action
				Intent intent = new Intent(this, EditMemberProfileActivity.class);
				intent.putExtra(Member.STORAGE_KEY, currentUser);

				startActivity(intent);
				break;

			case R.id.nav_gallery:
				break;

			case R.id.nav_slideshow:
				break;

			case R.id.nav_manage:
				break;

			case R.id.nav_share:
				break;

			case R.id.nav_send:
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

		teamService = new TeamService(this);
		currentUser = SharedPreferencesManager.getEntity(this, Member.STORAGE_KEY, Member.class);
		loadMemberTeam();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// When got result from NoTeamFragment, reload the team of the user
		if (NoTeamFragment.TEAM_CREATION_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode) {
			loadMemberTeam();
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
		// The action bar will automatically handle clicks on the Home/Up button,
		// so long as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void loadMemberTeam() {
		teamService.getTeam(currentUser, this);
	}

	@Override
	public void onHeadersResponse(Map<String, String> headers) {
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		if (HttpURLConnection.HTTP_NOT_FOUND == error.networkResponse.statusCode) {
			MemberHasTeam memberHasTeam = new MemberHasTeam();
			memberHasTeam.setMemberId(currentUser.getId());
			memberHasTeam.setStatus(MemberStatus.ALONE);

			SharedPreferencesManager.putEntity(this, MemberHasTeam.STORAGE_KEY, memberHasTeam);

			//Manually displaying the NoTeam fragment - one time only
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.frame_layout, new NoTeamFragment());
			transaction.commit();
		}
	}

	@Override
	public void onResponse(MemberHasTeam response) {
		SharedPreferencesManager.putEntity(this, MemberHasTeam.STORAGE_KEY, response);

		// Show the night center menu
		//navigationView.getMenu().add(nightCenterMenuItem.getGroupId(), nightCenterMenuItem.getItemId(), nightCenterMenuItem.getOrder(), nightCenterMenuItem.getTitle())
		//		.setIcon(nightCenterMenuItem.getIcon());
		// TODO: get status of the team (merged or not), and decide if we must show the night center or not

		//Manually displaying the TeamWall fragment - one time only
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.frame_layout, new TeamWallFragment());
		transaction.commit();
	}
}
