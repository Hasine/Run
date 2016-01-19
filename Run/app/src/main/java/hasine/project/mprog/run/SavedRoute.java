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
    StreetViewPanoramaView SVPV;
    StreetViewPanorama mPanorama;
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
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_page, container, false);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_saved_route, container, false );

        SVPV = (StreetViewPanoramaView) view.findViewById(R.id.street_view_panorama);
        SVPV.onCreate(savedInstanceState);


        initStreetView();
        return view;
    }

    private void initStreetView() {
        SVPV.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                mPanorama = panorama;
                showStreetView(new LatLng(40.7506, -73.9936));
            }
        });
    }

    private void showStreetView( LatLng latLng ) {
        if( mPanorama == null )
            return;

        StreetViewPanoramaCamera.Builder builder = new StreetViewPanoramaCamera.Builder( mPanorama.getPanoramaCamera() );
        builder.tilt( 0.0f );
        builder.zoom( 0.0f );
        builder.bearing( 0.0f );
        mPanorama.animateTo( builder.build(), 0 );
        mPanorama.setPosition( latLng, 300 );
        mPanorama.setStreetNamesEnabled(true);
    }
}
