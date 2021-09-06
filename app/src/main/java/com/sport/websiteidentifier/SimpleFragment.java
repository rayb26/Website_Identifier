package com.sport.websiteidentifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class SimpleFragment extends Fragment {

    ListView listView;
    ProgressBar progressBarSimpleFragment;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View root = inflater.inflate(R.layout.simplefragment, container, false);







        root.invalidate();



        listView = root.findViewById(R.id.listViewDetailed);
        final TextView textView = root.findViewById(R.id.textView3);

        textView.setVisibility(View.VISIBLE);

       // textView.setText(R.string.No_url_entered);

        ArrayList<String> dataForListView = new ArrayList<>();
        final ArrayAdapter<String> adapter;

        adapter=new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                dataForListView);



        //  final TextView textView = root.findViewById(R.id.textView);

      //  textView.setText(R.string.No_url_entered);


        SharedPreferences sharedPreferences
                = Objects.requireNonNull(getActivity()).getSharedPreferences("clientUrl", Context.MODE_PRIVATE);
        final String clientURL = sharedPreferences.getString("clientURL", "");

        assert clientURL != null;
        if (Simple.domainParser(clientURL).contains("Invalid Website")) {

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

                                listView.setAdapter(adapter);

                                String[] seperateData = getDataString.split("!");

                                for(String getData : seperateData){
                                    adapter.add(getData);
                                    textView.setVisibility(View.INVISIBLE);
                                }

                            }
                        });

                    } catch (InterruptedException | IllegalArgumentException | IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "No Connection",
                                        Toast.LENGTH_LONG).show();



                                //  Snackbar.make(getView(), "Tap Once To Enter URL", Snackbar.LENGTH_LONG).setAction("Action", null).show();


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

