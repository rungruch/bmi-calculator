package com.example.bodymassindexcalculator;

import androidx.appcompat.app.AppCompatActivity;


import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> read_file() {

        String filename = "cs361.txt";
        ArrayList<HashMap<String, String>> savedArrayList = null;
        try{
            FileInputStream fileInputStream =  openFileInput(filename);
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            savedArrayList = (ArrayList<HashMap<String, String>>) in.readObject();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return savedArrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg)));


        ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        MyArrList = read_file();
        if(MyArrList==null||MyArrList.isEmpty()){  MyArrList = new ArrayList<HashMap<String, String>>();}
        else{
            final ListView listView = (ListView)findViewById(R.id.listView);

            SimpleAdapter simpleAdapter = new SimpleAdapter(HistoryActivity.this, MyArrList, R.layout.activity_column, new String[]{"date", "weight", "bmi", "cat"}, new int[]{R.id.col_date, R.id.col_weight, R.id.col_bmi,R.id.col_cat});
            listView.setAdapter(simpleAdapter);

        }




    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final TextView title_history=findViewById(R.id.title_history);
        title_history.setTextSize(newConfig.fontScale*26);


        final TextView col_date=findViewById(R.id.col_date2);
        col_date.setTextSize(newConfig.fontScale*16);
        final TextView col_weight=findViewById(R.id.col_weight2);
        col_weight.setTextSize(newConfig.fontScale*16);
        final TextView col_bmi=findViewById(R.id.col_bmi2);
        col_bmi.setTextSize(newConfig.fontScale*16);
        final TextView col_cat=findViewById(R.id.col_cat2);
        col_cat.setTextSize(newConfig.fontScale*16);

        ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        MyArrList = read_file();
        if(MyArrList==null||MyArrList.isEmpty()){  MyArrList = new ArrayList<HashMap<String, String>>();}


        final ListView listView = (ListView)findViewById(R.id.listView);

        SimpleAdapter simpleAdapter = new SimpleAdapter(HistoryActivity.this, MyArrList, R.layout.activity_column, new String[]{"date", "weight", "bmi", "cat"}, new int[]{R.id.col_date, R.id.col_weight, R.id.col_bmi,R.id.col_cat});
        listView.setAdapter(simpleAdapter);



    }



}