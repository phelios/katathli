package com.cyros.phelios.katathli;

/**
 * Created by phelios on 12/28/14.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class InputFragment extends Fragment {

    private String dataFileName = "katathliData";
    private String eventFileName = "katahliEvent";
    private int SAD = 0;
    private int HAPPY = 1;

    private Helper helper;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private final String ARG_SECTION_NUMBER = "1";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public InputFragment newInstance(int sectionNumber) {
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
        return inflater.inflate(R.layout.fragment_input, container, false);

    }


    public void onSendEventButtonClicked(View v){
        EditText newEvent = (EditText)getView().findViewById(R.id.eventNote);

        String newEventContent = newEvent.getText().toString();
        if(newEventContent == null || newEventContent.isEmpty()){
            return;
        }

        this.saveToFile(this.eventFileName, newEventContent);
        newEvent.setText("");
        helper.flashNotify("New event added");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        helper = new Helper(getActivity());
        this.handleEvents();
    }

    private void handleEvents(){
        Button sadButton = (Button) getView().findViewById(R.id.sadButton);
        sadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSadButtonClicked(v);
            }
        });

        Button happyButton = (Button) getView().findViewById(R.id.happyButton);
        happyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHappyButtonClicked(v);
            }
        });

        Button sendButton = (Button) getView().findViewById(R.id.sendStatus);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendEventButtonClicked(v);
            }
        });
    }

    public void onSadButtonClicked(View v){
        this.saveEmotion("" + this.SAD);
    }

    public void onHappyButtonClicked(View v){
        this.saveEmotion("" + this.HAPPY);
    }

    private void saveEmotion(String emotion){
        this.saveToFile(this.dataFileName, emotion);
        helper.flashNotify("Your mood have been saved");
    }

    private void saveToFile(String filename, String data) {
        long epoch = System.currentTimeMillis()/1000;

        try {
            FileOutputStream fos = getActivity().openFileOutput(filename, Context.MODE_APPEND);
            fos.write((epoch + ","+ data + "\n").getBytes());
            fos.close();
        } catch (FileNotFoundException e) {}
        catch (IOException e) {}
    }
}
