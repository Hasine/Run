package hasine.project.mprog.run;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController.MediaPlayerControl;


public class StartActivity extends AppCompatActivity implements MediaPlayerControl {

    private MusicController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setController();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),
                StartActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

//        // Iterate over all tabs and set the custom view
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            tab.setCustomView(PagerAdapter.getTabView(i));
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_run_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
//                Intent gotosettings = new Intent(this, MusicSettings.class);
//                startActivity(gotosettings);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private void setController(){
        //set the controller up
        controller = new MusicController(this);
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
//        controller.setAnchorView(findViewById(R.id.song_list));
//        controller.setEnabled(true);
    }

}
