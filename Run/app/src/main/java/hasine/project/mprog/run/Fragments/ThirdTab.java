package hasine.project.mprog.run.Fragments;

/**
 * Hasine Efetürk
 * 10173536
 * hasineefeturk@hotmail.com
 *
 * You can find your previous runs here.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hasine.project.mprog.run.Tools.GPSTracker;
import hasine.project.mprog.run.R;


public class ThirdTab extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    // GPSTracker class
    GPSTracker gps;


    public ThirdTab() {
        // Required empty public constructor
    }

    public static ThirdTab newInstance(int sectionNumber) {
        ThirdTab fragment = new ThirdTab();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third_tab, container, false);
        TextView textView = (TextView) view.findViewById(R.id.textt);
        textView.setText("THIRDDTABB");
        return view;
    }
}

