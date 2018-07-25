package com.example.app.calculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.app.calculator.databinding.ActivityMainBinding;

import java.text.DecimalFormat;

enum Action {
    NONE, ADDITION, SUBTRACTION, MULTIPLICATION,
    DIVISION, SIN, COS, TAN, DEG, RAD
}

@SuppressWarnings("unused")
@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private Action currentAction;
    private double valueOne = Double.NaN;
    private double valueTwo = Double.NaN;
    private Action mode = Action.RAD;
    private boolean calculationEnded = false;

    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decimalFormat = new DecimalFormat("#.##########");

        binding = DataBindingUtil.setContentView(this, com.example.app.calculator.R.layout.activity_main);
        binding.buttonDeg.setText(mode == Action.DEG ? Action.DEG.toString() : Action.RAD.toString());

        binding.buttonDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.editText.getText().toString().contains(".")) {
                    editTextSet(".");
                }
            }
        });

        binding.buttonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               editTextSet("0");
            }
        });

        binding.buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSet("1");
            }
        });

        binding.buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSet("2");
            }
        });

        binding.buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSet("3");
            }
        });

        binding.buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSet("4");
            }
        });

        binding.buttonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSet("5");
            }
        });

        binding.buttonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSet("6");
            }
        });

        binding.buttonSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSet("7");
            }
        });

        binding.buttonEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSet("8");
            }
        });

        binding.buttonNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSet("9");
            }
        });

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionIsTrig() && !calculationEnded) return;
                computeCalculation();
                currentAction = Action.ADDITION;
                if (!Double.isNaN(valueOne) || calculationEnded) {
                    infoTextSet(" + ");
                }
                binding.editText.setText(null);
            }
        });

        binding.buttonSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionIsTrig() && !calculationEnded) return;
                computeCalculation();
                currentAction = Action.SUBTRACTION;
                if (!Double.isNaN(valueOne) || calculationEnded) {
                    infoTextSet(" - ");
                }
                binding.editText.setText(null);
            }
        });

        binding.buttonMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionIsTrig() && !calculationEnded) return;
                computeCalculation();
                currentAction = Action.MULTIPLICATION;
                if (!Double.isNaN(valueOne) || calculationEnded) {
                    infoTextSet(" * ");
                }
                binding.editText.setText(null);
            }
        });

        binding.buttonDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionIsTrig() && !calculationEnded) return;
                computeCalculation();
                currentAction = Action.DIVISION;
                if (!Double.isNaN(valueOne) || calculationEnded) {
                    infoTextSet(" / ");
                }
                binding.editText.setText(null);
            }
        });

        binding.buttonSin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAction = Action.SIN;
                binding.infoTextView.setText("sin(");
            }
        });

        binding.buttonCos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAction = Action.COS;
                binding.infoTextView.setText("cos(");
            }
        });

        binding.buttonTan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAction = Action.TAN;
                binding.infoTextView.setText("tan(");
            }
        });

        binding.buttonDeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buttonDeg.setText(mode == Action.RAD ? Action.DEG.toString() : Action.RAD.toString());
                mode = mode == Action.RAD ? Action.DEG : Action.RAD;
            }
        });

        binding.buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int editTextLength = binding.editText.getText().length();
                String infoText = binding.infoTextView.getText().toString();
                boolean actionSelected = infoText.contains("+") || infoText.contains("-")
                        || infoText.contains("*") || infoText.contains("/");
                if (editTextLength > 0 && (!Double.isNaN(valueOne) || (actionIsTrig()))) {
                    computeCalculation();
                    if (currentAction == Action.DIVISION && valueTwo == 0) {
                        Snackbar.make(view, "Cannot divide by 0", Snackbar.LENGTH_SHORT).show();
                        clearAll();
                    } else if (!Double.isNaN(valueTwo)){
                        try {
                            binding.infoTextView.setText(binding.infoTextView.getText().toString() +
                                    decimalFormat.format(valueTwo) + " = " + decimalFormat.format(valueOne));
                        } catch (ArithmeticException e) {
                            Utility.handleException(getContext(), getActivity(), view, e);
                        }
                    } else if (actionIsTrig()) {
                        try {
                            binding.infoTextView.setText(binding.infoTextView.getText().toString() +
                                    decimalFormat.format(Double.parseDouble(binding.editText.getText().toString()))
                                    + ") = " + decimalFormat.format(valueOne));
                        } catch (NumberFormatException | ArithmeticException | NullPointerException e) {
                            Utility.handleException(getContext(), getActivity(), view, e);
                        }
                    }
                    if (!Double.isNaN(valueTwo) || actionIsTrig()) {
                        calculationEnded = true;
                        valueOne = Double.NaN;
                        valueTwo = Double.NaN;
                        currentAction = Action.NONE;
                    }
                } else if (editTextLength > 0 || actionSelected) {
                    Snackbar.make(view, "Bad expression", Snackbar.LENGTH_SHORT).show();
                    clearAll();
                }
                binding.editText.setText(null);
            }
        });

        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence editText = binding.editText.getText();
                int editTextLength = editText.length();
                String infoText = binding.infoTextView.getText().toString();
                if ((editTextLength > 1 && infoText.equals("")) || (editTextLength > 0 && !infoText.equals(""))) {
                    binding.editText.setText(editText.subSequence(0, editTextLength - 1));
                } else {
                    Snackbar snackbar = Snackbar.make(view, "Cleared all", calculationEnded
                            ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT);
                    if (calculationEnded) {
                        final Action copyOfCurrentAction = currentAction;
                        final CharSequence originalInfoText = binding.infoTextView.getText();
                        final boolean copyOfCalculationEnded = calculationEnded;
                        snackbar.setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (copyOfCalculationEnded || originalInfoText.length() > 0) calculationEnded = true;
                                updateCurrentAction(copyOfCurrentAction);
                                binding.infoTextView.setText(originalInfoText);
                                binding.editText.setText(editText);
                                Snackbar.make(view, "Undo successful", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                    snackbar.show();
                    clearAll();
                }

            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BrowserActivity.class));
            }
        });
    }

    private void computeCalculation() {
        if (binding.editText.getText().length() == 0 && !calculationEnded) return;
        if (binding.editText.getText().toString().contains(".")) {
            int indexOfDecimalPoint = binding.editText.getText().toString().indexOf(".");
            try {
                //Is there a number after the decimal point?
                NOP(binding.editText.getText().toString().charAt(indexOfDecimalPoint + 1));
            } catch (StringIndexOutOfBoundsException e) {
                /*
                There wasn't any number after the decimal point, so tell the user what went wrong
                and stop the execution of this method.
                 */
                View view = findViewById(R.id.activity_main);
                Snackbar.make(view, "Bad expression", Snackbar.LENGTH_SHORT).show();
                clearAll();
                return;
            }
        }

        if (!Double.isNaN(valueOne) && !actionIsTrig()) {
            valueTwo = Double.parseDouble(binding.editText.getText().toString());

            switch (currentAction) {
                case ADDITION:
                    valueOne += valueTwo;
                    break;
                case SUBTRACTION:
                    valueOne -= valueTwo;
                    break;
                case MULTIPLICATION:
                    valueOne *= valueTwo;
                    break;
                case DIVISION:
                    if (valueTwo != 0) valueOne /= valueTwo;
                    break;
            }
        } else {
            try {
                if (calculationEnded && binding.infoTextView.getText().length() > 0 && binding.editText.getText().length() == 0
                        && !actionIsTrig()) {
                    int indexOfEqualsSign = binding.infoTextView.getText().toString().indexOf("=");
                    valueOne = Double.parseDouble(binding.infoTextView.getText().toString().substring(indexOfEqualsSign + 2));
                } else if (binding.editText.getText().length() > 0) {
                    valueOne = Double.parseDouble(binding.editText.getText().toString());
                }
            } catch (NumberFormatException | NullPointerException e) {
                Utility.handleException(getContext(), getActivity(), findViewById(R.id.activity_main), e);
            }

            calculationEnded = false;

            if (actionIsTrig()) {
                valueTwo = Double.NaN;
                computeTrig(currentAction, mode);
            }
        }
    }

    private void editTextSet(String s) {
        binding.editText.setText(binding.editText.getText() + s);
    }

    private void infoTextSet(String s) {
        binding.infoTextView.setText(decimalFormat.format(valueOne) + s);
    }

    private void clearAll() {
        valueOne = Double.NaN;
        valueTwo = Double.NaN;
        currentAction = Action.NONE;
        calculationEnded = false;
        binding.editText.setText(null);
        binding.infoTextView.setText(null);
    }

    private void computeTrig(Action action, Action theMode) {
        if (theMode != Action.DEG && theMode != Action.RAD) return;
        int num = action == Action.SIN ? 90 : 180;
        boolean bool = valueOne % num == 0;
        if (theMode == Action.DEG) {
            switch (action) {
                case SIN:
                    valueOne = bool ? Math.round(Math.sin(Math.toRadians(valueOne))) : Math.sin(Math.toRadians(valueOne));
                    break;
                case COS:
                    valueOne = bool ? Math.round(Math.cos(Math.toRadians(valueOne))) : Math.cos(Math.toRadians(valueOne));
                    break;
                case TAN:
                    valueOne = bool ? Math.round(Math.tan(Math.toRadians(valueOne))) : Math.tan(Math.toRadians(valueOne));
                    break;
            }
        } else {
            switch (action) {
                case SIN: valueOne = Math.sin(valueOne); break;
                case COS: valueOne = Math.cos(valueOne); break;
                case TAN: valueOne = Math.tan(valueOne); break;
            }
        }
    }

    private void updateCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }

    private Context getContext() {
        return this;
    }

    private Activity getActivity() {
        return (Activity) this;
    }

    private boolean actionIsTrig() {
        return currentAction == Action.SIN || currentAction == Action.COS || currentAction == Action.TAN;
    }

    private void NOP(Object o) {}
    private void NOP() {}
}