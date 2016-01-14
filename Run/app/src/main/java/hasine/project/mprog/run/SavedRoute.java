package hasine.project.mprog.run;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SavedRoute extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_start, container, false);
    }

}
