package com.sport.websiteidentifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class SimpleFragment extends Fragment {

    ListView listView;

    ArrayList<String> dataForListView = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View root = inflater.inflate(R.layout.simplefragment, container, false);


        root.invalidate();


        listView = root.findViewById(R.id.listView);


        final TextView textView = root.findViewById(R.id.textView);

        textView.setText(R.string.No_url_entered);


        SharedPreferences sharedPreferences
                = Objects.requireNonNull(getActivity()).getSharedPreferences("clientUrl", Context.MODE_PRIVATE);
        final String clientURL = sharedPreferences.getString("clientURL", "");
        textView.setText(clientURL);


        //Below is a final check that there are no shared pref values or anything that will cause app to crash
        assert clientURL != null;
        if (Simple.domainParser(clientURL).contains("Invalid Website")) {
            textView.setText(R.string.No_url_entered);

        } else {

            SharedPreferences sharedPrefSecondcall
                    = Objects.requireNonNull(getActivity()).getSharedPreferences("clientUrl", Context.MODE_PRIVATE);
            final String getURL = sharedPrefSecondcall.getString("clientURL", "");


            Thread getDataThread = new Thread() {
                public void run() {


                    try {
                        final String getDataString = Simple.extractData(getURL);


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                String[] seperateData = getDataString.split("!");
                                dataForListView.addAll(Arrays.asList(seperateData));

                                textView.setText(getDataString);
                            }
                        });

                    } catch (InterruptedException | IllegalArgumentException | IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Error Fetching Interrupted",
                                        Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        });

                    }


                }
            };
            getDataThread.start();
            try {
                getDataThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        return root;

    }




























    }
