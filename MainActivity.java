package com.example.myapplication;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText inputAmount;
    private Button btnRoll, btnAscending, btnDescending;
    private GridLayout diceContainer;
    private TextView resultLabel;

    private final ArrayList<Integer> diceValues = new ArrayList<>();
    private final Random generator = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (view, windowInsets) -> {
                    Insets system = windowInsets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    view.setPadding(system.left, system.top,
                            system.right, system.bottom);

                    return windowInsets;
                });

        inputAmount = findViewById(R.id.editTextDiceNumber);
        btnRoll = findViewById(R.id.drawButton);
        btnAscending = findViewById(R.id.buttonUp);
        btnDescending = findViewById(R.id.buttonDown);
        diceContainer = findViewById(R.id.gridDices);
        resultLabel = findViewById(R.id.textViewResults);

        btnRoll.setOnClickListener(view -> rollDice());

        btnAscending.setOnClickListener(view -> {
            if (diceValues.size() == 0) {
                showMessage("Najpierw wylosuj kostki");
                return;
            }

            Collections.sort(diceValues);
            updateScreen();
        });

        btnDescending.setOnClickListener(view -> {
            if (diceValues.isEmpty()) {
                showMessage("Najpierw wylosuj kostki");
                return;
            }

            Collections.sort(diceValues, Collections.reverseOrder());
            updateScreen();
        });
    }

    private void rollDice() {
        String enteredText = inputAmount.getText().toString().trim();

        int amount;

        try {
            amount = Integer.parseInt(enteredText);
        } catch (NumberFormatException exception) {
            showMessage("Podaj poprawną liczbę kostek");
            return;
        }

        if (amount < 1) {
            showMessage("Podaj poprawną liczbę kostek");
            return;
        }

        if (amount > 100) {
            showMessage("Maksymalnie 100 kostek");
            return;
        }

        diceValues.clear();

        int counter = 0;

        while (counter < amount) {
            int result = generator.nextInt(6) + 1;
            diceValues.add(result);
            counter++;
        }

        updateScreen();
    }

    private void updateScreen() {
        StringBuilder results = new StringBuilder();

        for (int index = 0; index < diceValues.size(); index++) {
            results.append(diceValues.get(index));

            if (index != diceValues.size() - 1) {
                results.append(", ");
            }
        }

        resultLabel.setText(results.toString());

        diceContainer.removeAllViews();
        diceContainer.setColumnCount(5);

        int diceSize = (int) (56 * getResources()
                .getDisplayMetrics().density);

        for (Integer number : diceValues) {
            TextView diceView = new TextView(this);

            diceView.setText(String.valueOf(number));
            diceView.setTextSize(24);
            diceView.setGravity(Gravity.CENTER);
            diceView.setTextColor(0xFF000000);
            diceView.setBackgroundColor(0xFFE0E0E0);

            GridLayout.LayoutParams layoutParams =
                    new GridLayout.LayoutParams();

            layoutParams.width = diceSize;
            layoutParams.height = diceSize;

            diceView.setLayoutParams(layoutParams);

            diceContainer.addView(diceView);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}