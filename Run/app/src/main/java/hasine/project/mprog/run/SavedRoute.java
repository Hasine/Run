package hasine.project.mprog.run;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class SavedRoute extends SupportStreetViewPanoramaFragment implements OnStreetViewPanoramaReadyCallback {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private double lat_loc, long_loc;
    public static final String TAG = SavedRoute.class.getSimpleName();
//    private OnFragmentInteractionListener mListener;

    public SavedRoute() {
        // Required empty public constructor
    }

    public static SavedRoute newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SavedRoute fragment = new SavedRoute();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        lat_loc = SP.getFloat("lat_loc", 0);
        long_loc = SP.getFloat("long_loc", 1);
        Log.d(TAG, "lat_loc: " + lat_loc);
        Log.d(TAG, "long_loc: " + long_loc);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama SVP) {
        SVP.setPosition(new LatLng(lat_loc, long_loc));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        StreetViewPanoramaFragment streetViewPanoramaFragment =
//                (StreetViewPanoramaFragment) getFragmentManager()
//                        .findFragmentById(R.id.streetviewpanorama);
//        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        return inflater.inflate(R.layout.fragment_saved_route, container, false);
    }

}
