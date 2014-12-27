package com.cyros.phelios.katathli;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;


public class HomeActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    private String dataFileName = "katathliData";
    private String eventFileName = "katahliEvent";
    private int SAD = 0;
    private int HAPPY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSendEventButtonClicked(View v){
        EditText newEvent = (EditText)findViewById(R.id.eventNote);

        String newEventContent = newEvent.getText().toString();
        if(newEventContent == null || newEventContent.isEmpty()){
            return;
        }

        this.saveToFile(this.eventFileName, newEventContent);
        newEvent.setText("");
        this.flashNotify("New event added");
    }

    public void onSadButtonClicked(View v){
        this.saveEmotion("" + this.SAD);
    }

    public void onHappyButtonClicked(View v){
        this.saveEmotion("" + this.HAPPY);
    }

    private void saveEmotion(String emotion){
        this.saveToFile(this.dataFileName, emotion);
        this.flashNotify("Your mood have been saved");
    }

    private void saveToFile(String filename, String data) {
        long epoch = System.currentTimeMillis()/1000;

        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_APPEND);
            fos.write((epoch + ","+ data + "\n").getBytes());
            fos.close();
        } catch (FileNotFoundException e) {}
        catch (IOException e) {}
    }

    private void flashNotify(String notification){
        final TextView flashArea = (TextView)findViewById(R.id.flashNotification);
        flashArea.setText(notification);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(150); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(5);

        Animation.AnimationListener flashEnd = new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {};
            public void onAnimationRepeat(Animation animation) {};
            public void onAnimationEnd(Animation anim){
                flashArea.setText("");
            }
        };

        anim.setAnimationListener(flashEnd);

        flashArea.startAnimation(anim);

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return InputFragment.newInstance(position);
                case 1:
                    return ReportingFragment.newInstance(position);
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class InputFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "1";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static InputFragment newInstance(int sectionNumber) {
            InputFragment fragment = new InputFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public InputFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_input, container, false);
            return rootView;
        }
    }

    public static class ReportingFragment extends Fragment {

        static View mActivity;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "2";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ReportingFragment newInstance(int sectionNumber) {
            ReportingFragment fragment = new ReportingFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            //initGraph();

            return fragment;
        }

        public void initGraph(LinearLayout reportHolder) {
            FileInputStream data;
            try {
                data = getActivity().openFileInput("katathliData");
            } catch (FileNotFoundException e) {
                return;
            }

            Scanner scan = new Scanner(data);
            List<String[]> lines = new ArrayList<String[]>();
            while(scan.hasNextLine()){
                String newLine = scan.nextLine();
                String[] newLineSplit = newLine.split(",");
                lines.add(new String[] {newLineSplit[0], newLineSplit[1]});
            }

            String[] labels = new String[lines.size()];



            GraphView.GraphViewData[] graphData = new GraphView.GraphViewData[lines.size()];

            for (int i = 0; i < lines.size(); i++){
                String[] newPoint = lines.get(i);
                graphData[i] = new GraphView.GraphViewData(Double.parseDouble(newPoint[0]), Double.parseDouble(newPoint[1]));
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                labels[i] = df.format(new Date(Long.parseLong(newPoint[0])));
            }

            GraphView graphView = new LineGraphView(
                    getActivity() // context
                    , "Monthly" // heading
            );
            graphView.addSeries(new GraphViewSeries(graphData)); // data
            graphView.setHorizontalLabels(labels);

            reportHolder.addView(graphView);
        }

        @Override
        public void onStart() {
            super.onStart();
            mActivity = getView();
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.reportFrame);
            this.initGraph(layout);
        }

        public ReportingFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_report, container, false);
            return rootView;
        }
    }

}
