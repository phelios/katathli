package com.cyros.phelios.katathli;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
 * Created by phelios on 12/29/14.
 */
public class ReportingFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "2";
    private Helper helper;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ReportingFragment newInstance(int sectionNumber) {
        ReportingFragment fragment = new ReportingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    private List<int[]> processFile(String filename) throws FileNotFoundException{
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
            thisDate = df.format(new Date(Long.parseLong(newLineSplit[0]) * 1000));
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

    public void initGraph(LinearLayout reportHolder) {
        List<int[]> lines;
        try{
            lines = processFile("katathliData");
        } catch (FileNotFoundException e) {
            helper.flashNotify("File not found. Bailing");
            return;
        }

        //String[] labels = new String[lines.size()];

        GraphView.GraphViewData[] graphData = new GraphView.GraphViewData[lines.size()];

        for (int i = 0; i < lines.size(); i++){
            int[] newPoint = lines.get(i);
            graphData[i] = new GraphView.GraphViewData(newPoint[0], newPoint[1]);
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            //labels[i] = df.format(new Date(Long.parseLong(newPoint[0])));
        }

        GraphView graphView = new BarGraphView(
                getActivity() // context
                , "Monthly" // heading
        );
        graphView.addSeries(new GraphViewSeries(graphData)); // data
        //graphView.setHorizontalLabels(labels);

        reportHolder.addView(graphView);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        helper = new Helper(getActivity());
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.reportFrame);
        this.initGraph(layout);
    }

    public ReportingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        return rootView;
    }
}