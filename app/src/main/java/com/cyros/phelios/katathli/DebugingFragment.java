package com.cyros.phelios.katathli;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by phelios on 12/30/14.
 */
public class DebugingFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "3";
    private Helper helper;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DebugingFragment newInstance(int sectionNumber) {
        DebugingFragment fragment = new DebugingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    private List<int[]> processFile(String filename) throws FileNotFoundException {
        FileInputStream data;
        List<int[]> lines = new ArrayList<int[]>();

        data = getActivity().openFileInput(filename);

        Scanner scan = new Scanner(data);


        String oldDate = "";
        String thisDate = "";
        int xCount = 0;
        int yCount = 0;

        while(scan.hasNextLine()){
            String newLine = scan.nextLine();
            String[] newLineSplit = newLine.split(",");

            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            thisDate = df.format(new Date(Long.parseLong(newLineSplit[0])));
            if (!thisDate.equals(oldDate) && !oldDate.isEmpty()){
                lines.add(new int[] {xCount, yCount});
                xCount++;
                yCount = 0;
            }

            yCount++;
            oldDate=thisDate;
        }

        return lines;
    }

    public void readFile(TextView debugArea) {
        try {
            FileInputStream data = getActivity().openFileInput("katathliData");
            Scanner scan = new Scanner(data);
            String debug = "";

            while(scan.hasNextLine()){
                String[] newLineSplit = scan.nextLine().split(",");
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                Date newDate = new Date(Long.parseLong(newLineSplit[0]) * 1000);
                String thisDate = df.format(new Date(Long.parseLong(newLineSplit[0])));
                debug += newDate + "\n";
            }

            debugArea.setText(debug);

        } catch (Exception e) {}



    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        helper = new Helper(getActivity());
        TextView layout = (TextView) view.findViewById(R.id.debugOutput);
        this.readFile(layout);
    }

    public DebugingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_debug, container, false);
        return rootView;
    }
}
