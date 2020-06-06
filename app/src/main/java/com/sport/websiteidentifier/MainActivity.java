package com.sport.websiteidentifier;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.sport.websiteidentifier.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SelectWebsite.class);
//                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog,null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                // Get the custom alert dialog view widgets reference
                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                final EditText url = (EditText) dialogView.findViewById(R.id.et_name);

                final AlertDialog dialog = builder.create();

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String clientURL = url.getText().toString();

                        String ValidatedURL = Simple.checkIfSiteIsValid(clientURL);

                        if(Simple.domainParser(clientURL).contains("Invalid Website")){
                                //Alert User that Invalid Website
                            Toast.makeText(getApplicationContext(), "Enter Valid Website",
                                    Toast.LENGTH_LONG).show();

                            String testURL = "http://www.google.com";

                            SharedPreferences sharedPreferences
                                    = getSharedPreferences("clientUrl",
                                    MODE_PRIVATE);


                            SharedPreferences.Editor myEdit
                                    = sharedPreferences.edit();

                            myEdit.putString(
                                    "clientURL",
                                    testURL);

                            myEdit.apply();
                            finish();

                            startActivity(getIntent());
                            dialog.cancel();

                            }else{

//
//                            //Valid Website
//                            SharedPreferences sharedPreferences
//                                    = getSharedPreferences("clientUrl",
//                                    MODE_PRIVATE);
//
//
//                            SharedPreferences.Editor myEdit
//                                    = sharedPreferences.edit();
//
//                            myEdit.putString(
//                                    "clientURL",
//                                    testURL);
//
//                            myEdit.apply();
//                            finish();
//
//                            startActivity(getIntent());
//                            dialog.cancel();
                        }








                    }
                });

                // Set negative/no button click listener
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss/cancel the alert dialog
                        //dialog.cancel();
                        dialog.dismiss();

                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Tap Button To Enter URL", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return true;
            }
        });
    }
}