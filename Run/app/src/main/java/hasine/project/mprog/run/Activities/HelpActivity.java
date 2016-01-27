package hasine.project.mprog.run.Activities;

/**
 * Hasine Efetürk
 * 10173536
 * hasineefeturk@hotmail.com
 *
 * This is the activity whereby you can get info and contact the developer
 *
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import hasine.project.mprog.run.Activities.MainRunActivity;
import hasine.project.mprog.run.R;

public class HelpActivity extends AppCompatActivity {

    public static final String TAG = HelpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent gotoMain = new Intent(this, MainRunActivity.class);
                startActivity(gotoMain);
                break;
            case R.id.mail:
                Intent gotoMail = new Intent(Intent.ACTION_SEND);
                gotoMail.setType("text/plain");
                gotoMail.putExtra(Intent.EXTRA_EMAIL, new String[]{"hasineefeturk@hotmail.com"});
                gotoMail.putExtra(Intent.EXTRA_SUBJECT, "Question about LoveToRun app");

                // To check if there is an app already installed on your phone which can handle
                // this action
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(gotoMail,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;

                if (isIntentSafe) {
                    startActivity(gotoMail);
                }
                break;

            case R.id.call:
                Uri number = Uri.parse("tel:+31646103598");
                Intent gotoCall = new Intent(Intent.ACTION_DIAL, number);

                // To check if there is an app already installed on your phone which can handle
                // this action
                PackageManager packageManager2 = getPackageManager();
                List activities2 = packageManager2.queryIntentActivities(gotoCall,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe2 = activities2.size() > 0;

                if (isIntentSafe2) {
                    startActivity(gotoCall);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
