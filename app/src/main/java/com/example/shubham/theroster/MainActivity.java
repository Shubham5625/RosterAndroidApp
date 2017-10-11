package com.example.shubham.theroster;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Initializing the Shared Preferences editor
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    //Declaring a variable for Edit Text
    EditText TextEdit;

    //Declaring two Checkboxes
    CheckBox box1, box2;

    //Declaring a variable for Spinner
    Spinner spinner;

    //Declaring an instance of Radio Group & Radio Button
    RadioGroup radioGroup;
    RadioButton radioButton;

    //Declaring an Int variable for a selected ID in all three methods of the seekbar
    int selectedId;

    //Declaring three different Seek Bars for Pant Size, Shirt Size, Show Size
    SeekBar pantSize;
    SeekBar shirtSize;
    SeekBar shoeSize;

    //Declaring three Text Views to set the seek bar information
    TextView pantSizeText;
    TextView shirtSizeText;
    TextView shoeSizeText;

    //Declaring & Initialising Day, Month and Year variables for Calendar in Integer
    int nYear = 0, nMonth = 0, nDay = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Notification to welcome the user
        Toast.makeText(this,"Welcome to The Roster App!",Toast.LENGTH_LONG).show();

        //Creating an instance of Shared Preferences
        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        editor = prefs.edit();

        //Name
        TextEdit = (EditText) findViewById(R.id.editText);

        //Checkbox
        box1 = (CheckBox) findViewById(R.id.checkBox);
        box2 = (CheckBox) findViewById(R.id.checkBox2);

        //Dropdown Menu
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        spinnerElement();

        //Radio Button
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        // Seek Bar
        pantSize = (SeekBar) findViewById(R.id.seekBar);
        shirtSize = (SeekBar) findViewById(R.id.seekBar2);
        shoeSize = (SeekBar) findViewById(R.id.seekBar3);
        pantSizeText = (TextView) findViewById(R.id.textView10);
        pantSizeText.setText("0/16");
        shirtSizeText = (TextView) findViewById(R.id.textView11);
        shirtSizeText.setText("0/12");
        shoeSizeText = (TextView) findViewById(R.id.textView12);
        shoeSizeText.setText("4/12");

        //Method to set Shared Preferences for Pant Size Seek Bar
        pantSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            int pantSizeData;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                pantSizeData = i;
                editor.putInt("pant", pantSizeData);
                pantSizeText.setText(pantSizeData + "/" + "16");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {}
        });

        //Method to set Shared Preferences for Shirt Size Seek Bar
        shirtSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            int shirtSizeData;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                shirtSizeData = i;
                editor.putInt("shirt", shirtSizeData);
                shirtSizeText.setText(shirtSizeData + "/" + "12");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {}
        });

        //Method to set Shared Preferences for Shoe Size Seek Bar
        shoeSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int shoeSizedata;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                shoeSizedata = i;
                editor.putInt("shoe", shoeSizedata);
                if (shoeSizedata < 9) {
                    shoeSizeText.setText(shoeSizedata + 4 + "/" + "12");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {}
        });

        //Load method to set all the data preferences
        load();
    }

    public void save(View v)
    {
        //  Name
        editor.putString("name", TextEdit.getText().toString());

        // Checkbox
        if (box1.isChecked())
        {
            editor.putString("status", "Not Steady");
        }
        else if (box2.isChecked())
        {
            editor.putString("status", "Steady");
        }

        // Calendar
        editor.putInt("KEY_YEAR", nYear);
        editor.putInt("KEY_MONTH", nMonth);
        editor.putInt("KEY_DAY", nDay);

        //Radio Button
        selectedId = radioGroup.getCheckedRadioButtonId();
        editor.putInt("button", selectedId);

        // Save all changes in the Shared Preferences
        editor.commit();

        // Toast notification to convey all data has been saved
        Toast.makeText(this, "Your Information has been Saved", Toast.LENGTH_LONG).show();
    }


    public void load()
    {

        // Load the name from preferences
        String str = prefs.getString("name", "");
        TextEdit.setText(str);

        // Load the Checkbox from the preferences
        String status = prefs.getString("status", "");
        if (status.equals("Steady"))
        {
            box2.setChecked(true);
        }
        else
        {
            box1.setChecked(true);
        }

        // Load the saved selection for eyes from the preferences
        spinner.setSelection(prefs.getInt("spinnerStatus", 0));

        //Load the date saved in Calendar from the preferences and set on the Text Field
        nYear = prefs.getInt("KEY_YEAR", 2015);
        nMonth = prefs.getInt("KEY_MONTH", 2);
        nDay = prefs.getInt("KEY_DAY", 9);
        if (nYear == 0)
        {
            nYear = 2016;
            nMonth = 10;
            nDay = 2;
        }
        EditText et = (EditText) findViewById(R.id.TextEdit1);
        et.setText(nMonth + "-" + nDay + "-" + nYear);

        // Load all the three Seekbars from the preferences
        pantSize.setProgress(prefs.getInt("pant", 0));
        shirtSize.setProgress(prefs.getInt("shirt", 0));
        shoeSize.setProgress(prefs.getInt("shoe", 0));

        // Load the saved Radiobutton from the preferences
        radioButton = (RadioButton) findViewById(prefs.getInt("button", 2131492963));
        radioButton.setChecked(true);
    }

    //Spinner position to add
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        editor.putInt("spinnerStatus", position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {}

    //Spinner Element being saved
    public void spinnerElement()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.color_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // Calendar method for the calendar button on the UI
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClickCalendar(View view)
    {
        // Process to get Current Date
        if (nYear == 0)
        {
            final Calendar c = Calendar.getInstance();
            nYear = c.get(Calendar.YEAR);
            nMonth = c.get(Calendar.MONTH);
            nDay = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                nYear = year;
                nMonth = monthOfYear + 1;
                nDay = dayOfMonth;
                EditText et = (EditText) findViewById(R.id.TextEdit1);
                // Display Selected date in textbox
                et.setText(nMonth + "-" + nDay + "-" + nYear);
            }
        },
            nYear, (nMonth - 1), nDay);
        date.show();
    }
}





