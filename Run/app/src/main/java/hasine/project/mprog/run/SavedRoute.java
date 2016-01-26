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
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

public class SavedRoute extends SupportStreetViewPanoramaFragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private double lat_loc, long_loc;
//    StreetViewPanoramaView SVPV;
    StreetViewPanoramaView SVPV;
    public static final String TAG = SavedRoute.class.getSimpleName();

    public SavedRoute() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_saved_route, container, false);

        SVPV = (StreetViewPanoramaView) view.findViewById(R.id.street_view_panorama);
        SVPV.onCreate(savedInstanceState);
        SVPV.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
                final LatLng myLoc = new LatLng(lat_loc, long_loc);
                streetViewPanorama.setPosition(myLoc);
            }
        });

//        SVPV = (StreetViewPanoramaFragment) getActivity()
//                .getFragmentManager().findFragmentById(R.id.streetviewpanorama);
//        SVPV.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
//            @Override
//            public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
//                final LatLng myLoc = new LatLng(lat_loc, long_loc);
//                streetViewPanorama.setPosition(myLoc);
//            }
//        });
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
