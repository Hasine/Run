package hasine.project.mprog.run.Fragments;

/**
 * Hasine Efetürk
 * 10173536
 * hasineefeturk@hotmail.com
 *
 * Instantiate the second tab with the StreetViewPanoramaView.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

import hasine.project.mprog.run.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SecondTab extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    StreetViewPanoramaView SVPV;
    private double lat_loc, long_loc;
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TAG = SecondTab.class.getSimpleName();

    public SecondTab() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SecondTab newInstance(int sectionNumber) {
        SecondTab fragment = new SecondTab();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        lat_loc = SP.getFloat("lat_loc", 0);
        long_loc = SP.getFloat("long_loc", 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_tab, container, false);
        SVPV = (StreetViewPanoramaView) view.findViewById(R.id.svpv);
        SVPV.onCreate(savedInstanceState);
        SVPV.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(final StreetViewPanorama panorama) {
                final LatLng myLoc = new LatLng(lat_loc, long_loc);
                panorama.setPosition(myLoc);
                panorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama
                        .OnStreetViewPanoramaChangeListener() {
                    @Override
                    public void onStreetViewPanoramaChange(StreetViewPanoramaLocation myLoc) {
                        if (myLoc != null && myLoc.links != null) {
                            // location is present
                            Log.d(TAG,"inside if in onstreetviewchange");
                            panorama.setPosition(String.valueOf(myLoc));
                        } else {
                            // location not available
                            Log.d(TAG,"inside else");
                            Toast.makeText(getContext(), "StreetView on this location is not available",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return view;
    }

    public final void onResume(){
        super.onResume();
        SVPV.onResume();
    }

    public final void onPause(){
        super.onPause();
        SVPV.onPause();
    }

    public final void onDestroy(){
        super.onDestroy();
        SVPV.onDestroy();
    }

    public final void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        SVPV.onSaveInstanceState(outState);
    }

    public final void onLowMemory(){
        super.onLowMemory();
        SVPV.onLowMemory();
    }
}
