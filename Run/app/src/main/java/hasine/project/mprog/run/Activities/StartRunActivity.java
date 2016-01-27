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

public class StartRunActivity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {

    TabLayout tabLayout;
    private ViewPager mViewPager;
    private String pointsroute;
    private double lat_loc, long_loc;
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
        pointsroute = SP.getString("pointsroute", "Couldn't get points route");
        lat_loc = SP.getFloat("lat_loc", 0);
        long_loc = SP.getFloat("long_loc", 1);
        goal = SP.getString("goal", "goal not found");
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama SVP) {
        Log.i(TAG, "loc streetview: " + lat_loc + long_loc);
        myLoc = new LatLng(lat_loc, long_loc);
        SVP.setPosition(myLoc);
//        (lat_loc, long_loc)
        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()
                .bearing(180)
                .build();
        SVP.animateTo(camera, 10000);
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


//    /**
//     * Music player settings
//     */
//    @Override
//    public void pause() {
//        playbackPaused=true;
//        musicSrv.pausePlayer();
//    }
//
//    @Override
//    public void seekTo(int pos) {
//        musicSrv.seek(pos);
//    }
//
//    @Override
//    public void start() {
//        musicSrv.go();
//    }
//
//    public void getSongList() {
//        //retrieve song info
//        ContentResolver musicResolver = getContentResolver();
//        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
//        if(musicCursor!=null && musicCursor.moveToFirst()){
//            //get columns
//            int titleColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media.TITLE);
//            int idColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media._ID);
//            int artistColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media.ARTIST);
//            //add songs to list
//            do {
//                long thisId = musicCursor.getLong(idColumn);
//            }
//            while (musicCursor.moveToNext());
//        }
//
//    }
//
//    @Override
//    public int getDuration() {
//        if(musicSrv!=null && musicBound && musicSrv.isPng())
//            return musicSrv.getDur();
//        return 0;
//    }
//
//    @Override
//    public int getCurrentPosition() {
//        if(musicSrv!=null && musicBound && musicSrv.isPng())
//            return musicSrv.getPosn();
//        return 0;
//    }
//
//    @Override
//    public boolean isPlaying() {
//        if(musicSrv!=null && musicBound)
//            return musicSrv.isPng();
//        return false;
//    }
//
//    @Override
//    public int getBufferPercentage() {
//        return 0;
//    }
//
//    @Override
//    public boolean canPause() {
//        return true;
//    }
//
//    @Override
//    public boolean canSeekBackward() {
//        return true;
//    }
//
//    @Override
//    public boolean canSeekForward() {
//        return true;
//    }
//
//    @Override
//    public int getAudioSessionId() {
//        return 0;
//    }
//
//    private void setController(){
//        //set the controller up
//        controller = new MusicController(this);
//        controller.setPrevNextListeners(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playNext();
//            }
//        }, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playPrev();
//            }
//        });
//        controller.setMediaPlayer(this);
//        controller.setEnabled(true);
//    }
//
//    private void playNext(){
//        musicSrv.playNext();
//        if(playbackPaused) {
//            setController();
//            playbackPaused=false;
//        }
//        controller.show(0);
//    }
//
//    private void playPrev(){
//        musicSrv.playPrev();
//        if(playbackPaused){
//            setController();
//            playbackPaused=false;
//        }
//        controller.show(0);
//    }
//
//    @Override
//    protected void onPause(){
//        super.onPause();
//        paused=true;
//    }
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        if(paused){
//            setController();
//            paused=false;
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        controller.hide();
//        super.onStop();
//    }
//
//
//    private ServiceConnection musicConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
//            musicBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            musicBound = false;
//        }
//    };
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(playIntent==null){
//            playIntent = new Intent(this, MusicService.class);
//            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
//            startService(playIntent);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        stopService(playIntent);
//        musicSrv=null;
//        super.onDestroy();
//    }
}
