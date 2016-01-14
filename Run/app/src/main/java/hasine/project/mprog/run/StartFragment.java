package hasine.project.mprog.run;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

// In this case, the fragment displays simple text based on the page
public class StartFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = StartFragment.class.getSimpleName();
    private int mPage;
    private View view;

    public static StartFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        StartFragment fragment = new StartFragment();
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
            view = inflater.inflate(R.layout.fragment_page, container, false);
            TextView textView = (TextView) view;
            textView.setText("Fragment #" + mPage);
            Log.i(TAG, "Now in case 1");
            return view;
    }
}
