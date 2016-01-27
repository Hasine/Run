package hasine.project.mprog.run.Activities;

/**
 * Hasine Efetürk
 * 10173536
 * hasineefeturk@hotmail.com
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import hasine.project.mprog.run.R;

public class MainRunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_run);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void make(View view) {
        Intent gotoSearch = new Intent(this, MakeRunActivity.class);
        startActivity(gotoSearch);
    }

    public void start(View view) {
        Intent gotoStart = new Intent(this, StartRunActivity.class);
        startActivity(gotoStart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_run_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.help:
                Intent gotoHelp = new Intent(this, HelpActivity.class);
                startActivity(gotoHelp);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
