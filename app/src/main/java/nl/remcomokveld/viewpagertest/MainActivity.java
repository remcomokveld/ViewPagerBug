package nl.remcomokveld.viewpagertest;

import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int initialItem = 26211;
    private ViewPager viewPager;
    private boolean methodTracing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(initialItem);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (methodTracing) {
                    Debug.stopMethodTracing();
                    methodTracing = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != initialItem) {
            methodTracing = true;
            Debug.startMethodTracing();
            viewPager.setCurrentItem(initialItem, false);
        } else {
            super.onBackPressed();
        }
    }

    private static class Adapter extends FragmentStatePagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 100000000;
        }
    }


    public static class PageFragment extends android.support.v4.app.Fragment {

        public static PageFragment newInstance(int page) {

            Bundle args = new Bundle();
            args.putInt("page", page);
            PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_page, container, false);
            ((TextView) view.findViewById(R.id.page)).setText(String.valueOf(getArguments().getInt("page")));
            return view;
        }
    }
}
