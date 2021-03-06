package com.example.app.calculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.app.calculator.databinding.ActivityMainBinding;
import com.util.Color;
import com.util.Settings;
import com.util.Utility;

import java.text.DecimalFormat;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    private enum Action {
        NONE, ADDITION, SUBTRACTION, MULTIPLICATION,
        DIVISION, SIN, COS, TAN, DEG, RAD;

        boolean isTrig() {
            return this == SIN || this == COS || this == TAN;
        }
    }

    private ActivityMainBinding binding;
    private DrawerLayout mDrawerLayout;
    private Settings[] settings;

    private Action currentAction = Action.NONE;
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
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        final ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);

        settings = new Settings[2];

        for (int i = 0; i < settings.length; i++) {
            if (i != 0) {
                ((SettingsSaver)getApplication()).makeSettings();
            }
            settings[i] = ((SettingsSaver)getApplication()).getSettings();
        }

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.settings_button:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;
                    case R.id.next_button:
                        startActivity(new Intent(getApplicationContext(), BrowserActivity.class));
                        break;
                    case R.id.contact_button:
                        int[] intArr = {97, 108, 97, 110, 108, 117, 117, 52, 64, 103, 109, 97, 105, 108, 46, 99, 111, 109};
                        StringBuilder builder = new StringBuilder();
                        for (int i : intArr) {
                            builder.append((char) i);
                        }
                        Utility.INSTANCE.sendDebugEmail(builder.toString(), "Android calculator",
                                getApplicationContext(), getActivity());
                        break;
                    case R.id.share_button:
                        Utility.INSTANCE.share(getActivity(), "Android calculator",
                                "Hey, come check out this open source calculator app at " +
                                        "https://github.com/AlanLuu/android-calculator");
                        break;
                }
                return true;
            }
        });

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
                String editText = binding.editText.getText().toString();
                if (editText.length() > 0 && Character.isDigit(editText.charAt(0)) && currentAction.isTrig()) {
                    double result = Double.parseDouble(editText);
                    valueOne = currentAction == Action.SIN ? Math.sin(result) :
                            currentAction ==  Action.COS ? Math.cos(result) : Math.tan(result);
                } else if (currentAction.isTrig()) {
                    return;
                }
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
                String editText = binding.editText.getText().toString();
                if (editText.length() > 0 && Character.isDigit(editText.charAt(0)) && currentAction.isTrig()) {
                    double result = Double.parseDouble(editText);
                    valueOne = currentAction == Action.SIN ? Math.sin(result) :
                            currentAction ==  Action.COS ? Math.cos(result) : Math.tan(result);
                } else if (currentAction.isTrig()) {
                    return;
                }
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
                String editText = binding.editText.getText().toString();
                if (editText.length() > 0 && Character.isDigit(editText.charAt(0)) && currentAction.isTrig()) {
                    double result = Double.parseDouble(editText);
                    valueOne = currentAction == Action.SIN ? Math.sin(result) :
                            currentAction ==  Action.COS ? Math.cos(result) : Math.tan(result);
                } else if (currentAction.isTrig()) {
                    return;
                }
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
                String editText = binding.editText.getText().toString();
                if (editText.length() > 0 && Character.isDigit(editText.charAt(0)) && currentAction.isTrig()) {
                    double result = Double.parseDouble(editText);
                    valueOne = currentAction == Action.SIN ? Math.sin(result) :
                            currentAction ==  Action.COS ? Math.cos(result) : Math.tan(result);
                } else if (currentAction.isTrig()) {
                    return;
                }
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
                if (settings[1].isSwitchOn()) {
                    makeSnackbar(view, "Trigonometry is disabled", Color.Companion.parseColor("#d12414"));
                    return;
                }
                currentAction = Action.SIN;
                binding.infoTextView.setText("sin(");
            }
        });

        binding.buttonCos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settings[1].isSwitchOn()) {
                    makeSnackbar(view, "Trigonometry is disabled", Color.Companion.parseColor("#d12414"));
                    return;
                }
                currentAction = Action.COS;
                binding.infoTextView.setText("cos(");
            }
        });

        binding.buttonTan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settings[1].isSwitchOn()) {
                    makeSnackbar(view, "Trigonometry is disabled", Color.Companion.parseColor("#d12414"));
                    return;
                }
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

        binding.buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editText = binding.editText.getText().toString();
                if (editText.length() == 0) {
                    binding.editText.setText("-");
                }
            }
        });

        binding.buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int editTextLength = binding.editText.getText().length();
                String infoText = binding.infoTextView.getText().toString();
                String editText = binding.editText.getText().toString();
                boolean actionSelected = infoText.contains("+") || infoText.contains("-")
                        || infoText.contains("*") || infoText.contains("/");
                if (editTextLength > 0 && (!Double.isNaN(valueOne) || (currentAction.isTrig()))) {
                    computeCalculation();
                    if (currentAction == Action.DIVISION && valueTwo == 0) {
                        showErrorMessage(view, "Cannot divide by 0");
                        clearAll();
                    } else if (!Double.isNaN(valueTwo)){
                        try {
                            binding.infoTextView.setText(infoText + decimalFormat.format(valueTwo) + " = " + decimalFormat.format(valueOne));
                        } catch (ArithmeticException e) {
                            handleException(e);
                        }
                    } else if (currentAction.isTrig()) {
                        try {
                            binding.infoTextView.setText(binding.infoTextView.getText().toString() +
                                    decimalFormat.format(Double.parseDouble(binding.editText.getText().toString()))
                                    + ") = " + (valueOne == -0 ? (int) Double.parseDouble(valueOne + "") :
                                    decimalFormat.format(valueOne)));
                        } catch (NumberFormatException | ArithmeticException | NullPointerException e) {
                            handleException(e);
                        }
                    }
                    if (!Double.isNaN(valueTwo) || currentAction.isTrig()) {
                        calculationEnded = true;
                        valueOne = Double.NaN;
                        valueTwo = Double.NaN;
                        currentAction = Action.NONE;
                    }
                } else if (editTextLength > 0) {
                    if (editText.contains(".") && editText.substring(editText.length() - 1).equals(".")) {
                        computeCalculation();
                        return;
                    }
                    if (editText.substring(0, 1).equals(".") || editTextLength > 1 && editText.contains("-") &&
                            editText.charAt(editText.indexOf("-") + 1) == '.') {
                        String newStr = editText.contains("-") &&
                                editText.charAt(editText.indexOf("-") + 1) == '.' ? "-0" + editText.substring(editText.indexOf("."))
                                : "0" + editText;
                        binding.infoTextView.setText(newStr + " = " + newStr);
                    } else if (editText.length() != 1 || !editText.equals("-")) {
                        if (!editText.contains(".") && editText.substring(0, 1).equals("0") ||
                                !editText.contains(".") && editText.contains("-") &&
                                        editText.substring(1, 2).equals("0")) {
                            editText = (int) Double.parseDouble(editText) + "";
                        }
                        binding.infoTextView.setText(editText + " = " + editText);
                    } else {
                        showErrorMessage(view, "Bad expression");
                    }
                    calculationEnded = true;
                    valueOne = Double.NaN;
                    valueTwo = Double.NaN;
                } else if (actionSelected && !calculationEnded) {
                    showErrorMessage(view, "Bad expression");
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
                } else if (isPortrait() || isLandscape() && calculationEnded) {
                    Snackbar snackbar = Snackbar.make(view, "Cleared all", settings[0].isSwitchOn() || calculationEnded
                            ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT);
                    if (settings[0].isSwitchOn() || calculationEnded) {
                        final Action copyOfCurrentAction = currentAction;
                        final CharSequence originalInfoText = binding.infoTextView.getText();
                        final boolean copyOfCalculationEnded = calculationEnded;
                        snackbar.setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (copyOfCalculationEnded || originalInfoText.length() > 0) calculationEnded = true;
                                MainActivity.this.currentAction = copyOfCurrentAction;
                                binding.infoTextView.setText(originalInfoText);
                                binding.editText.setText(editText);
                                Snackbar.make(view, "Undo successful", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        snackbar.setActionTextColor(Color.Companion.parseColor("#46bdbf"));
                    }
                    snackbar.show();
                    clearAll();
                } else {
                    Toast.makeText(getApplicationContext(), "Cleared all", Toast.LENGTH_SHORT).show();
                    clearAll();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void computeCalculation() {
        String editText = binding.editText.getText().toString();
        String infoText = binding.infoTextView.getText().toString();
        if (editText.length() == 0 && !calculationEnded) return;

        if ((editText.contains(".") && editText.substring(editText.length() - 1).equals(".")) ||
                (editText.length() == 1 && editText.equals("-"))) {
            showErrorMessage(findViewById(R.id.activity_main), "Bad expression");
            clearAll();
            return;
        }

        if (!Double.isNaN(valueOne) && !currentAction.isTrig()) {
            valueTwo = Double.parseDouble(editText);

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
                if (calculationEnded && infoText.length() > 0 && editText.length() == 0 && !currentAction.isTrig()) {
                    int indexOfEqualsSign = infoText.indexOf("=");
                    valueOne = Double.parseDouble(infoText.substring(indexOfEqualsSign + 2));
                } else if (binding.editText.getText().length() > 0) {
                    valueOne = Double.parseDouble(editText);
                }
            } catch (NumberFormatException | NullPointerException e) {
                handleException(e);
            }

            calculationEnded = false;

            if (currentAction.isTrig()) {
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

    @SuppressWarnings("SameParameterValue")
    private void makeSnackbar(View view, String text, int color) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
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

    private void showErrorMessage(View view, String message) {
        if (isPortrait()) {
            makeSnackbar(view, message, Color.Companion.parseColor("#d12414"));
        } else {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleException(Throwable e) {
        Utility.INSTANCE.handleException(getApplicationContext(), this, findViewById(R.id.activity_main), e);
    }

    private Activity getActivity() {
        return this;
    }

    private boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}