package hasine.project.mprog.run.Activities;

/**
 * Hasine Efetürk
 * 10173536
 * hasineefeturk@hotmail.com
 *
 * This activity contains the tabs, sets the adapter and the music controllers.
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import hasine.project.mprog.run.Fragments.FirstTab;
import hasine.project.mprog.run.Tools.LockableViewPager;
import hasine.project.mprog.run.R;
import hasine.project.mprog.run.Fragments.SecondTab;
import hasine.project.mprog.run.Fragments.ThirdTab;

public class StartRunActivity extends AppCompatActivity {

    TabLayout tabLayout;
    private ViewPager mViewPager;
    private double lat_loc, long_loc;
    private String locations;
    private LatLng myLoc;
    private static String goal;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String tabTitles[] = new String[] {"Start Run", "Saved Route", "Runned Routes" };
    private int[] tabIcons = {R.drawable.running, R.drawable.gps_device, R.drawable.map_marker};
    public static String POSITION = "POSITION";
    public static final String TAG = StartRunActivity.class.getSimpleName();

//    /**
//     * Music controllers
//     */
//    private MusicController controller;
//    private MusicService musicSrv;
//    private Intent playIntent;
//    private boolean musicBound=false, paused=false, playbackPaused=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_run);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        LockableViewPager.setSwipeable(true);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        lat_loc = SP.getFloat("lat_loc", 0);
        long_loc = SP.getFloat("long_loc", 1);
        goal = SP.getString("goal", "goal not found");

        Intent gotoStart = getIntent();
        locations = gotoStart.getStringExtra("locations");
        Log.d(TAG, "locations in startrun: " +locations);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent gotoMusic = new Intent(Intent.ACTION_MAIN, null);
            gotoMusic.addCategory(Intent.CATEGORY_APP_MUSIC);
            startActivity(gotoMusic);
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position + 1){
                case 1: return FirstTab.newInstance(position + 1);
                case 2: return SecondTab.newInstance(position + 1);
                case 3: return ThirdTab.newInstance(position + 1);
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return FirstTab.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}
