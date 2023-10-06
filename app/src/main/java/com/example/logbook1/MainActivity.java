package com.example.logbook1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    static final String DIVIDE_BY_ZERO_ERROR = "ERROR";
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
    Button pressedButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentNum = "";
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
                calculateResult();
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
        currentNum += number;
        resultDisplay.setText(currentNum);
        updateCalculationDisplay();
    }
    private void clear() {
        pressedButton = null;
        currentNum = "";
        previousNum = "";
        operator = "";
        result = 0.0;
        resultDisplay.setText("0");
        calculationDisplay.setText("0");
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
        }
    }

    private void addDecimal() {
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
//            updateButtonState(btn.getId());
        }
        updateCalculationDisplay();
    }
//    private void updateButtonState(int id) {
//        Button clickedButton = findViewById(id); // Replace with your actual button ID
//
//        if (pressedButton != null) {
//            pressedButton.getBackground().clearColorFilter();
////            pressedButton.setPressed(false); // Un-press the previously pressed button
//            pressedButton.setEnabled(true); // Enable the previously pressed button
//        }
//
//        pressedButton = clickedButton; // Update the pressed button
////        pressedButton.setPressed(true); // Press the newly clicked button
//        pressedButton.setEnabled(false);
//        pressedButton.setBackgroundColor(Color.GRAY);
//
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
        if (!currentNum.isEmpty()) {
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
                    if (secondOperand != 0) {
                        result = firstOperant / secondOperand;
                    } else {
                        // Handle division by zero error
                        // ...
                        currentNum = DIVIDE_BY_ZERO_ERROR;
                        previousNum = DIVIDE_BY_ZERO_ERROR;
                        operator = "";
                        result = 0.0;
                        resultDisplay.setText(currentNum);
                        updateCalculationDisplay();
                        return;
                    }
                    break;
            }
            calculationPerformed = true;
            if (result % 1 == 0) {
                // If it's an integer, convert it to an integer
                int integerResult = result.intValue();
                currentNum = String.valueOf(integerResult);
            } else {
                result = Math.round(result * 1000000) / 1000000.0;
                currentNum = String.valueOf(result);
            }
            resultDisplay.setText(currentNum);
        }
    }
}