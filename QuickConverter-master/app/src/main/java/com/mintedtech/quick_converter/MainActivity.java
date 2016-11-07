package com.mintedtech.quick_converter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mintedtech.quick_converter.lib.RadioGridGroup;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        setContentView (R.layout.activity_main);

        setupToolbar ();
        setupFAB ();
    }

    private void setupToolbar ()
    {
        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);
    }

    private void setupFAB ()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById (R.id.fab);
        fab.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View view)
            {
                RadioGridGroup rg = (RadioGridGroup) findViewById (R.id.rg_conversion_choices);
                int selectedRdo = rg.getCheckedAppCompatRadioButtonId ();
                calculateAndOutputResult (findViewById (selectedRdo));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected (item);
    }

    public void calculateAndOutputResult (View view)
    {
        String strInput = getInputtedValue ();
        int selectedRadioButtonIndex = getSelectedRadioButtonIndex (view);

        outputResult (selectedRadioButtonIndex, strInput);
    }

    @NonNull private String getInputtedValue ()
    {
        TextInputEditText inputField = (TextInputEditText) findViewById (R.id.input_number);
        return inputField.getText ().toString ();
    }

    private int getSelectedRadioButtonIndex (View view)
    {
        // IDs of Views - Radio Buttons - Element order must match the above array
        int[] choices = {R.id.rdo_miles_to_km, R.id.rdo_km_to_miles,
                         R.id.rdo_lb_to_kg, R.id.rdo_kg_to_lb, R.id.rdo_lb_to_oz, R.id.rdo_oz_to_lb};

        // get index of currently selected Radio Button
        for (int i = 0; i < choices.length; i++) {
            if (view == findViewById (choices[i])) {
                return i;
            }
        }
        return -1;
    }

    private void outputResult (int selectedRadioButtonIndex, String strInput)
    {
        // Variables, etc.
        View sbParent = findViewById (R.id.sb_parent);
        DecimalFormat formatter;
        double input, result;
        String strResult;
        String strNoConversionType = "No conversion type selected.";
        String strNumberSelectedOrNot =
                strInput.length () == 0 ? "No number entered." : "Number entered.";

        // Conversion Factor - Element order must match the Views Array in the getSel... method
        double[] conversionFactor = {1.60934, .621371,
                                     .453592, 2.20462, 16, .0625};

        // Process
        // If the RadioButton selected matches one of the Radio Buttons in our group
        if (selectedRadioButtonIndex != -1) {

            // if the user entered a number into the Input Edit Text
            if (strInput.length () != 0) {
                formatter = new DecimalFormat ("#,##0.0#");
                input = Double.parseDouble (strInput);
                result = input * conversionFactor[selectedRadioButtonIndex];
                strResult = "The converted Number is: " + formatter.format (result) + '.';
                Snackbar.make (sbParent, strResult, Snackbar.LENGTH_INDEFINITE).show ();
            }
            else {
                Snackbar.make (sbParent, "Please enter a number.", Snackbar.LENGTH_SHORT).show ();
            }
        }
        else {
            Snackbar.make (sbParent, strNumberSelectedOrNot + ' ' + strNoConversionType,
                           Snackbar.LENGTH_LONG).show ();
        }
    }
}
