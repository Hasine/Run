package hasine.project.mprog.run;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class MyAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] {"Start Run", "Saved Route", "Runned Routes" };
    private Context context;
    public static final String TAG = MyAdapter.class.getSimpleName();

    public MyAdapter(FragmentManager fm, StartActivity context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case (1):
                return PageFragment.newInstance(position + 1);
            case (2):
                return PageFragment.newInstance(position + 1);
            case (3):
//                return SavedRouteActivity;
        }
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}