package hasine.project.mprog.run.Fragments;

/**
 * Hasine Efetürk
 * 10173536
 * hasineefeturk@hotmail.com
 *
 * This is the first tab where you can track your run and control the music player.
 */

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hasine.project.mprog.run.Tools.GPSTracker;
import hasine.project.mprog.run.R;


public class FirstTab extends Fragment {

    private static String goal;
    private static final String ARG_SECTION_NUMBER = "section_number";

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
        goal = SP.getString("goal", "goal not found");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_tab, container, false);
            TextView textView = (TextView) view.findViewById(R.id.currDist);
            textView.setText(String.valueOf(getArguments().getInt(ARG_SECTION_NUMBER)));
            TextView textView2 = (TextView) view.findViewById(R.id.goal);
            textView2.setText(this.getString(R.string.goalSetText, goal));

            gps = new GPSTracker(getContext());

            // check if GPS enabled
            if (gps.canGetLocation()) {
                Location location = gps.getLocation();

            } else {
                gps.showSettingsAlert();
            }
        return view;
    }
}
