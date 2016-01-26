package hasine.project.mprog.run;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class helpActivity extends AppCompatActivity {

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
                Intent gotoMain = new Intent(this, RunMainActivity.class);
                startActivity(gotoMain);
            case R.id.mail:
                Intent gotoMail = new Intent(Intent.ACTION_MAIN, null);
                gotoMail.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(gotoMail);
            case R.id.call:
                Intent gotoCall = new Intent(Intent.ACTION_MAIN, null);
                gotoCall.addCategory(Intent.CATEGORY_APP_CONTACTS);
                startActivity(gotoCall);
        }
        return super.onOptionsItemSelected(item);
    }
}
