package com.example.logbook1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String ADD_SIGN = "+";
    static final String SUBTRACT_SIGN = "-";
    static final String MULTIPLY_SIGN = "*";
    static final String DIVIDE_SIGN = "/";
    static final String CLEAR_TEXT = "AC";
    static final String CHANGE_SIGN_TEXT = "+/-";
    static final String EQUAL_SIGN = "=";
    static final String COMMA_SIGN = ",";
    static final String PERCENTAGE_SIGN = "%";

    static final String DIVIDE_BY_ZERO_ERROR = "NOT A NUMBER";
    private boolean calculationPerformed = false;

    String currentNum;
    String previousNum;
    String operator;
    Double result = 0.0;

    TextView calculationDisplay;
    TextView resultDisplay;

    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0;
    Button buttonClear, buttonChangeSign, buttonPercentage, buttonComma;
    Button buttonAdd, buttonSubstract, buttonMutiply, buttonDivide, buttonEqual;
    private Button activeOperatorButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentNum = "0";
        previousNum = "";
        operator = "";
        calculationDisplay = findViewById(R.id.calculationDisplay);
        resultDisplay = findViewById(R.id.resultDisplay);
        assignId(button0, R.id.btn0);
        assignId(button1, R.id.btn1);
        assignId(button2, R.id.btn2);
        assignId(button3, R.id.btn3);
        assignId(button4, R.id.btn4);
        assignId(button5, R.id.btn5);
        assignId(button6, R.id.btn6);
        assignId(button7, R.id.btn7);
        assignId(button8, R.id.btn8);
        assignId(button9, R.id.btn9);
        assignId(buttonClear, R.id.btnClear);
        assignId(buttonChangeSign, R.id.btnChangeSign);
        assignId(buttonPercentage, R.id.btnPercentage);
        assignId(buttonComma, R.id.btnComma);
        assignId(buttonAdd, R.id.btnAdd);
        assignId(buttonSubstract, R.id.btnSubtract);
        assignId(buttonMutiply, R.id.btnMultiply);
        assignId(buttonDivide, R.id.btnDivide);
        assignId(buttonEqual, R.id.btnEqual);
    }

    protected void assignId(Button btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button) view;
        String btnText = btn.getText().toString();
        if (btnText.matches("[0-9]")) {
            append(btnText);
            return;
        }
        switch (btnText) {
            case CLEAR_TEXT:
                clear();
                break;
            case CHANGE_SIGN_TEXT:
                changeSign();
                break;
            case PERCENTAGE_SIGN:
                calculatePercentage();
                break;
            case COMMA_SIGN:
                addDecimal();
                break;
            case EQUAL_SIGN:
                if (!previousNum.equals("") && !operator.equals("")){
                    calculateResult();
                }
            default:
                if (btnText.matches("[-+*/]")) {
                    setOperator(btn);
                }
                break;
        }
    }

    private void append(String number){
        if (calculationPerformed) {
            clear();
            calculationPerformed = false;
        }
        if (currentNum.equals("0") && previousNum.equals("") && operator.equals("")) {
            currentNum = "";
        }
        currentNum += number;
        resultDisplay.setText(currentNum);
        updateCalculationDisplay();
    }
    private void clear() {
//        activeOperatorButton = null;
        currentNum = "0";
        previousNum = "";
        operator = "";
        result = 0.0;
        resultDisplay.setText(currentNum);
        calculationDisplay.setText(currentNum);
        enableAllButtons();
//        updateCalculationDisplay();
//        setActiveOperatorButton(null);
    }
    private void changeSign() {
        if (!currentNum.isEmpty()) {
            double number = Double.parseDouble(currentNum);
            number = -number;
            currentNum = String.valueOf(number);
            resultDisplay.setText(currentNum);
            if (!operator.equals("")) {
                currentNum = "(" + currentNum + ")";
                updateCalculationDisplay();
            }
        }
    }
    private void calculatePercentage() {
        if (!currentNum.isEmpty()) {
            double number = Double.parseDouble(currentNum);
            double percentage = number / 100;
            currentNum = String.valueOf(percentage);
            resultDisplay.setText(currentNum);
            updateCalculationDisplay();
        }
    }

    private void addDecimal() {
        if (currentNum.equals("")) {
            currentNum = "0";
        }
        if (!currentNum.contains(".")) {
            currentNum += ".";
            resultDisplay.setText(currentNum);
        }
    }
    private void setOperator(Button btn) {
            if (!operator.isEmpty()) {
                calculateResult();
            }
            operator = btn.getText().toString();
            calculationPerformed = false;
            if (!currentNum.isEmpty()) {
                previousNum = currentNum;
                currentNum = "";
            }
//        setActiveOperatorButton(btn);
            updateCalculationDisplay();
    }
//    private void setActiveOperatorButton(Button button) {
//        if (activeOperatorButton != null) {
//            activeOperatorButton.setBackgroundColor(getResources().getColor(R.color.orange)); // Change back to the original background resource
//        } else {
//            activeOperatorButton = button;
//            activeOperatorButton.setBackgroundColor(getResources().getColor(R.color.grey));
//        }
//         // Set a background resource to indicate active state
//    }

    private void updateCalculationDisplay() {
        if (currentNum.equals("Error") && previousNum.equals("Error")) {
            calculationDisplay.setText("Error");
        } else {
//            if (!previousNum.isEmpty() && !operator.isEmpty())
            String calculation = previousNum + " " + operator + " " + currentNum;
            calculationDisplay.setText(calculation);
        }

    }

    private void calculateResult() {
        if (currentNum.equals("0") && operator.equals(DIVIDE_SIGN)){
            // Handle division by zero error
            // ...
            updateCalculationDisplay();
            currentNum = DIVIDE_BY_ZERO_ERROR;
            previousNum = DIVIDE_BY_ZERO_ERROR;
            operator = "";
            result = 0.0;
            resultDisplay.setText(currentNum);
            disableAllButtonsExceptAC();
        } else {
            updateCalculationDisplay();
            currentNum = currentNum.replaceAll("[^0-9.+-]", "");
            double firstOperant = Double.parseDouble(previousNum);
            double secondOperand = Double.parseDouble(currentNum);
            switch (operator) {
                case ADD_SIGN:
                    result = firstOperant + secondOperand;
                    break;
                case SUBTRACT_SIGN:
                    result = firstOperant - secondOperand;
                    break;
                case MULTIPLY_SIGN:
                    result = firstOperant * secondOperand;
                    break;
                case DIVIDE_SIGN:
//                    if (secondOperand != 0) {
//                        result = firstOperant / secondOperand;
//                    } else {
//                        // Handle division by zero error
//                        // ...
//                        currentNum = DIVIDE_BY_ZERO_ERROR;
//                        previousNum = DIVIDE_BY_ZERO_ERROR;
//                        operator = "";
//                        result = 0.0;
//                        resultDisplay.setText(currentNum);
////                        updateCalculationDisplay();
//                        return;
//                    }
                    result = firstOperant / secondOperand;
                    break;
            }
            calculationPerformed = true;
            operator = "";
            previousNum = "";
            if (result % 1 == 0) {
                // If it's an integer, convert it to an integer
                int integerResult = result.intValue();
                currentNum = String.valueOf(integerResult);
            } else {
                result = Math.round(result * 100000) / 100000.0;
                currentNum = String.valueOf(result);
            }
            resultDisplay.setText(currentNum);
        }
    }
    private void disableAllButtonsExceptAC() {
        LinearLayout parentLayout = findViewById(R.id.buttonsLayout); // Replace with your actual layout ID

        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            LinearLayout childLayout = (LinearLayout) parentLayout.getChildAt(i);
            for (int j = 0; j < childLayout.getChildCount(); j++) {
                Button button = (Button) childLayout.getChildAt(j);
                if (button.getId() != R.id.btnClear) { // Exclude AC button
                    button.setEnabled(false);
                }
            }
        }
    }
    private void enableAllButtons() {
        LinearLayout parentLayout = findViewById(R.id.buttonsLayout); // Replace with your actual layout ID

        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            LinearLayout childLayout = (LinearLayout) parentLayout.getChildAt(i);
            for (int j = 0; j < childLayout.getChildCount(); j++) {
                Button button = (Button) childLayout.getChildAt(j);
                if (button.getId() != R.id.btnClear) { // Exclude AC button
                    button.setEnabled(true);
                }
            }
        }
    }
}