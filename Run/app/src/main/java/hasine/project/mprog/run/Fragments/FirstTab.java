package hasine.project.mprog.run.Fragments;

/**
 * Hasine Efetürk
 * 10173536
 * hasineefeturk@hotmail.com
 *
 * This is the first tab where you can track your run and control the music player.
 */

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hasine.project.mprog.run.Tools.SongsManager;
import hasine.project.mprog.run.Tools.GPSTracker;
import hasine.project.mprog.run.R;
import hasine.project.mprog.run.Tools.Utilities;


public class FirstTab extends Fragment implements MediaPlayer.OnCompletionListener {

    private static String goal;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ImageButton btnPlay, btnShuffle;
    private boolean isShuffle = false;
    private TextView songTitleLabel, songCurrentDurationLabel, songTotalDurationLabel;

    ContentResolver cr;
    private  MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    private SongsManager songManager;
    private Utilities utils;
    private int currentSongIndex = 0;
    private List<SongsManager.Item> songsList = new ArrayList<>();

    public static final String TAG = FirstTab.class.getSimpleName();

    // GPSTracker class
    GPSTracker gps;


    public FirstTab() {
        // Required empty public constructor
    }

    public static FirstTab newInstance(int sectionNumber) {
        FirstTab fragment = new FirstTab();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        goal = SP.getString("goal", "0");

        // Mediaplayer
        mp = new MediaPlayer();
        cr = getContext().getContentResolver();
        songManager = new SongsManager(cr);
        utils = new Utilities();

        mp.setOnCompletionListener(this);

        // Getting all songs list
        songsList = songManager.getPlayList();
        Log.d(TAG, "songman.getplaylist" + songManager.getPlayList());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_tab, container, false);
        TextView textView = (TextView) view.findViewById(R.id.currDist);
        textView.setText(R.string.currDistTextFirst);
        TextView textView2 = (TextView) view.findViewById(R.id.goal);
        textView2.setText(this.getString(R.string.goalSetText, goal));

        // All player buttons
        ImageButton btnNext = (ImageButton) view.findViewById(R.id.btnNext);
        ImageButton btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        ImageButton btnStop = (ImageButton) view.findViewById(R.id.btnStop);
        btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        btnShuffle = (ImageButton) view.findViewById(R.id.btnShuffle);
        songTitleLabel = (TextView) view.findViewById(R.id.songTitle);
        songCurrentDurationLabel = (TextView) view.findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) view.findViewById(R.id.songTotalDurationLabel);

        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mp.isPlaying()){
                    mp.pause();
                    // Changing Button Image to pause image
                    btnPlay.setImageResource(R.drawable.pause);
                } else {
                    int mpAt = mp.getCurrentPosition();
                    if (mpAt == 0){
                        playSong(currentSongIndex);
                    } else {
                        mp.seekTo(mpAt);
                        mp.start();
                    }

                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if (currentSongIndex < (songsList.size() - 1) && isShuffle) {
                    // shuffle is on - play a random song
                    Random rand = new Random();
                    currentSongIndex = rand.nextInt(songsList.size());
                    playSong(currentSongIndex);
                } else if (currentSongIndex < (songsList.size() - 1)) {
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    // play first song
                    playSong(0);
                    currentSongIndex = 0;
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (currentSongIndex > 0) {
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                } else {
                    // play last song
                    playSong(songsList.size() - 1);
                    currentSongIndex = songsList.size() - 1;
                }

            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isShuffle){
                    isShuffle = false;
                    Toast.makeText(getContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.shuffle);
                    songsList = songManager.getPlayList();
                }else{
                    isShuffle= true;
                    Toast.makeText(getContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mp.stop();
                btnPlay.setImageResource(R.drawable.play);
            }
        });

        gps = new GPSTracker(getContext());

            // check if GPS enabled
            if (gps.canGetLocation()) {
                Location location = gps.getLocation();

            } else {
                gps.showSettingsAlert();
            }
        return view;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if(isShuffle){
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt(songsList.size());
            playSong(currentSongIndex);
        } else{
            // shuffle is not ON - play next song
            if(currentSongIndex < (songsList.size() - 1)){
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            }else{
                // play first song
                playSong(0);
                currentSongIndex = 0;
            }
        }
    }


    /**
     * Receiving song index from playlist view
     * and play the song
     * */
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Random rand = new Random();
        if(resultCode == 100){
            currentSongIndex = rand.nextInt();
            // play selected song
            playSong(currentSongIndex);
        }

    }

    public void  playSong(int songIndex){
        // Play song
        try {
            mp.reset();
            Log.d(TAG, "songlist in playsong method: " + songsList);
            mp.setDataSource(getContext(), songsList.get(songIndex).getURI());
            mp.prepare();
            mp.start();
            // Displaying song title
            String songTitle = songsList.get(songIndex).getTitle();
            songTitleLabel.setText(songTitle);

            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.pause);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText(utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText(utils.milliSecondsToTimer(currentDuration));

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        mp.release();
        mp = null;
    }
}
