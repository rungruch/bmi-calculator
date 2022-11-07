package com.example.bodymassindexcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    DecimalFormat formatter = new DecimalFormat("#,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg)));


        EditText w_editText = (EditText) findViewById(R.id.weight_editText);
        w_editText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        EditText h_editText = (EditText) findViewById(R.id.height_editText);
        h_editText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});

        final TextView bmi_output_text = findViewById(R.id.box_bmi_value);
        final TextView cat_output_text = findViewById(R.id.box_bmi_type);


        final Button calculate_btn = findViewById(R.id.calculate_button);
        calculate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double weight = 0.0;
                double height = 0.0;
                double bmi = 0.0;
                if (w_editText.getText().toString().isEmpty() || h_editText.getText().toString().isEmpty()) {
                    cat_output_text.setText(R.string.invalid_input);
                    bmi_output_text.setText(R.string.hyphen);
                } else {

                    weight = Double.parseDouble(w_editText.getText().toString());
                    height = Double.parseDouble(h_editText.getText().toString());

                    bmi = weight / ((height / 100) * (height / 100));
                    bmi_output_text.setText(formatter.format(bmi));

                    String bmi_cat = "";
                    if (bmi < 18.50) {
                        cat_output_text.setBackground(getDrawable(R.drawable.bmi_red_border));
                        bmi_cat = getResources().getString(R.string.bmi_class1);
                    } else if (bmi >= 18.50 && bmi < 23.00) {
                        cat_output_text.setBackground(getDrawable(R.drawable.bmi_green_border));
                        bmi_cat = getResources().getString(R.string.bmi_class2);
                    } else if (bmi >= 23.00 && bmi < 25.00) {
                        cat_output_text.setBackground(getDrawable(R.drawable.bmi_blue_border));
                        bmi_cat = getResources().getString(R.string.bmi_class3);
                    } else if (bmi >= 25.00 && bmi < 30) {
                        cat_output_text.setBackground(getDrawable(R.drawable.bmi_purple_border));
                        bmi_cat = getResources().getString(R.string.bmi_class4);
                    } else if (bmi >= 30) {
                        cat_output_text.setBackground(getDrawable(R.drawable.bmi_orange_border));
                        bmi_cat = getResources().getString(R.string.bmi_class5);
                    }

                    cat_output_text.setText(bmi_cat);



                    String filename = "cs361.txt";
                    ArrayList<HashMap<String, String>> MyArrList = read_file();
                    HashMap<String, String> map;
                    map = new HashMap<String, String>();

                         if(MyArrList==null||MyArrList.isEmpty()){
                             MyArrList = new ArrayList<HashMap<String, String>>();
                             map.put(getString(R.string.map_date), (getString(R.string.history_date)));
                             map.put(getString(R.string.map_weight), (getString(R.string.history_weight)));
                             map.put(getString(R.string.map_bmi), (getString(R.string.history_bmi)));
                             map.put(getString(R.string.map_cat), (getString(R.string.history_categories)));
                         }

                   String date = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "th")).format(new Date());


                        map.put(getString(R.string.map_date), date);
                        map.put(getString(R.string.map_weight), formatter.format(weight));
                        map.put(getString(R.string.map_bmi),  formatter.format(bmi));
                        map.put(getString(R.string.map_cat), bmi_cat);
                        MyArrList.add(map);

                    FileOutputStream outputStream;
                   /* String storageState = Environment.getExternalStorageState();
                    if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                        try {
                            File file = new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS), filename);
                            outputStream = new FileOutputStream(file);
                            ObjectOutputStream out = new ObjectOutputStream(outputStream);
                            out.writeObject(MyArrList);
                            outputStream.close();
                          //  Toast.makeText(getApplicationContext(), "Save to : " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }*/

                    try{
                        outputStream = openFileOutput(filename, MODE_PRIVATE);
                       // outputStream.write(string.get)
                        ObjectOutputStream out = new ObjectOutputStream(outputStream);
                        out.writeObject(MyArrList);
                        outputStream.close();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    private ArrayList<HashMap<String, String>> read_file() {

        String filename = "cs361.txt";
       // String storageState = Environment.getExternalStorageState();
        ArrayList<HashMap<String, String>> savedArrayList = null;
       /* if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), filename);
                if(!file.exists())
                {
                   return  savedArrayList;
                }
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                savedArrayList = (ArrayList<HashMap<String, String>>) in.readObject();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/

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
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        int id = item.getItemId();
        if (id == R.id.item1) {
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        final TextView title_text=findViewById(R.id.title_text);
        title_text.setTextSize(newConfig.fontScale*26);

        final TextView title_weight=findViewById(R.id.title_weight);
        title_weight.setTextSize(newConfig.fontScale*16);

        final TextView title_height=findViewById(R.id.title_height);
        title_height.setTextSize(newConfig.fontScale*16);

        final TextView title_bmi_value=findViewById(R.id.title_bmi_value);
        title_bmi_value.setTextSize(newConfig.fontScale*16);

        final TextView title_bmi_type=findViewById(R.id.title_bmi_type);
        title_bmi_type.setTextSize(newConfig.fontScale*16);

        final TextView weight_editText=findViewById(R.id.weight_editText);
        weight_editText.setTextSize(newConfig.fontScale*16);
        final TextView height_editText=findViewById(R.id.height_editText);
        height_editText.setTextSize(newConfig.fontScale*16);
        final TextView box_bmi_value=findViewById(R.id.box_bmi_value);
        box_bmi_value.setTextSize(newConfig.fontScale*16);
        final TextView box_bmi_type=findViewById(R.id.box_bmi_type);
        box_bmi_type.setTextSize(newConfig.fontScale*16);
        final TextView calculate_button=findViewById(R.id.calculate_button);
        calculate_button.setTextSize(newConfig.fontScale*16);


    }


}
class DecimalDigitsInputFilter implements InputFilter {
    final private Pattern mPattern;
    DecimalDigitsInputFilter(int digits, int digitsAfterZero) {
        mPattern = Pattern.compile("[0-9]{0," + (digits - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) { Matcher matcher = mPattern.matcher(dest);
        if (!matcher.matches())
            return "";
        return null;
    } }





