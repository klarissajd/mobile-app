package edu.monash.fit2081.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    private double valueOne = Double.NaN;
    private double valueTwo;
    private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char MULTIPLICATION = '*';
    private static final char DIVISION = '/';
    private static final char NO_OPERATION = '?';

    private char CURRENT_ACTION;
    private DecimalFormat decimalFormat;
    public TextView interScreen;  // Intermediate result Screen
    private TextView resultScreen; // Result Screen


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Reference both TextViews
        interScreen =  findViewById(R.id.InterScreen);
        resultScreen =  findViewById(R.id.resultScreen);
        decimalFormat = new DecimalFormat("#.##########");
    }

    public void buttonZeroClick(View v) { interScreen.setText(interScreen.getText() + "0");}

    public void buttonOneClick(View v) { interScreen.setText(interScreen.getText() + "1");}

    public void buttonTwoClick(View v) { interScreen.setText(interScreen.getText() + "2");}

    public void buttonThreeClick(View v) { interScreen.setText(interScreen.getText() + "3");}

    public void buttonFourClick(View v) { interScreen.setText(interScreen.getText() + "4");}

    public void buttonFiveClick(View v) { interScreen.setText(interScreen.getText() + "5");}

    public void buttonSixClick(View v) { interScreen.setText(interScreen.getText() + "6");}

    public void buttonSevenClick(View v) {
        interScreen.setText(interScreen.getText() + "7");
    }

    public void buttonEightClick(View v) {
        interScreen.setText(interScreen.getText() + "8");
    }

    public void buttonNineClick(View v) {
        interScreen.setText(interScreen.getText() + "9");
    }

    public void buttonDotClick(View v) {
        interScreen.setText(interScreen.getText() + ".");
    }


    public void buttonDivisionClick(View v) {
        computeCalculation();
        if (Double.isNaN(valueOne)) {
            showToast("Invalid Key");
        } else {
            CURRENT_ACTION = DIVISION;
            resultScreen.setText(decimalFormat.format(valueOne) + "/");
            interScreen.setText("");
        }
    }

    public void buttonMultiplicationClick(View v) {
        computeCalculation();
        if (Double.isNaN(valueOne)) {
            showToast("Invalid Key");
        } else {
            CURRENT_ACTION = MULTIPLICATION;
            resultScreen.setText(decimalFormat.format(valueOne) + "*");
            interScreen.setText("");
        }
    }

    public void buttonAdditionClick(View v) {
        computeCalculation();
        if (Double.isNaN(valueOne)) {
            showToast("Invalid Key");
        } else {
            CURRENT_ACTION = ADDITION;
            resultScreen.setText(decimalFormat.format(valueOne) + "+");
            interScreen.setText("");
        }
    }

    public void buttonSubtractionClick(View v) {
        computeCalculation();
        if (Double.isNaN(valueOne)) {
            showToast("Invalid Key");
        } else {
            CURRENT_ACTION = SUBTRACTION;
            resultScreen.setText(decimalFormat.format(valueOne) + "-");
            interScreen.setText("");
        }
    }

    public void buttonEqualClick(View v) {
        computeCalculation();
        interScreen.getText().toString();
        resultScreen.setText(resultScreen.getText().toString() + decimalFormat.format(valueTwo) + " = " + decimalFormat.format(valueOne));
        valueOne = Double.NaN;

        CURRENT_ACTION = NO_OPERATION;

        /*
        * Call ComputeCalculation method
        * Update the result TextView by adding the '=' char and result of operation
        * Reset valueOne
        * Set CURRENT_ACTION to NO_OPERATION
        * */
    }

    public void buttonClearClick(View v) {
        if (interScreen.getText().toString() != "") {
            String inter = interScreen.getText().toString();
            String newInter = inter.substring(0, inter.length()-1);
            interScreen.setText(newInter);
        } else {
            valueOne = Double.NaN;
            valueTwo = Double.NaN;
            resultScreen.setText("");
            interScreen.setText("");
        }
        /*
        * if the intermediate TextView has text then
        *       delete the last character
        * else
              * reset valueOne, valueTwo, the content of result TextView,
              * and the content of intermediate TextView
        * */
    }


    private void computeCalculation() {

        if (!Double.isNaN(valueOne)) {
            String valueTwoString = interScreen.getText().toString();
            if (!valueTwoString.equals("")) {
                valueTwo = Double.parseDouble(valueTwoString);
                interScreen.setText(null);
                if (CURRENT_ACTION == ADDITION)
                    valueOne = this.valueOne + valueTwo;
                else if (CURRENT_ACTION == SUBTRACTION)
                    valueOne = this.valueOne - valueTwo;
                else if (CURRENT_ACTION == MULTIPLICATION)
                    valueOne = this.valueOne * valueTwo;
                else if (CURRENT_ACTION == DIVISION)
                    valueOne = this.valueOne / valueTwo;
            }
        } else {
            try {
                valueOne = Double.parseDouble(interScreen.getText().toString());
            } catch (Exception e) {
            }

        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
