package com.afsis.yieldestimator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.afsis.yieldestimator.R;
import com.afsis.yieldestimator.crops.Maize;
import com.afsis.yieldestimator.crops.MaizeGrowthStage;
import com.afsis.yieldestimator.util.Notifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends SuperActivity {

    public static String MAIZE_DATA = "maizeData";
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
    }

    private void initLayout() {
        renderSpinner();
        setClickHandlers();
    }

    private void setClickHandlers() {

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        ImageButton btnStageImg = (ImageButton) findViewById(R.id.btStageImg);

        btnSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract user input
                EditText txtCobsPerUnitArea = (EditText) findViewById(R.id.txtCobsPerUnitArea);
                EditText txtRowsPerCob = (EditText) findViewById(R.id.txtRowsPerCob);
                EditText txtKernelsPerRow = (EditText) findViewById(R.id.txtKernelsPerRow);
                Spinner spinGrowthStage = (Spinner) findViewById(R.id.spinGrowthStage);
                Maize maize = new Maize();
                String cobs = txtCobsPerUnitArea.getText().toString().trim();
                String rows = txtRowsPerCob.getText().toString().trim();
                String kernels = txtKernelsPerRow.getText().toString().trim();
                MaizeGrowthStage stage = (MaizeGrowthStage) spinGrowthStage.getSelectedItem();
                if (cobs.isEmpty() || rows.isEmpty() || kernels.isEmpty() || stage == null) {
                    Notifier.showToastMessage(getApplicationContext(), getString(R.string.err_fields_empty));
                } else {
                    // Estimate the yield
                    maize.setCobsPerUnitArea(Integer.parseInt(cobs));
                    maize.setRowsPerCob(Integer.parseInt(rows));
                    maize.setKernelsPerRow(Integer.parseInt(kernels));
                    maize.setGrowthStage(stage);
                    renderResult(maize);
                }
            }
        });

        btnStageImg.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GrowthStageImageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void renderResult(Maize maize) {
        // Switch activity
        Intent i = new Intent(getApplicationContext(), ResultsActivity.class);
        i.putExtra(MAIZE_DATA, maize);
        startActivity(i);
    }

    private void renderSpinner() {
        List<MaizeGrowthStage> lstSpinner = new ArrayList<>();
        lstSpinner.addAll(Arrays.asList(MaizeGrowthStage.values()));

        // Create spinner adapter
        ArrayAdapter<MaizeGrowthStage> customAdapter = new ArrayAdapter<MaizeGrowthStage>(this,
                android.R.layout.simple_spinner_item) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                convertView = super.getDropDownView(position, convertView, parent);
                convertView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams p = convertView.getLayoutParams();
                // Hack to change the height of the drop down, so that it is displayed downward
                //  TODO: change to something cleaner?
                p.height = 65;
                convertView.setLayoutParams(p);
                return convertView;
            }
        };

        customAdapter.addAll(lstSpinner);
        Spinner sItems = (Spinner) findViewById(R.id.spinGrowthStage);
        sItems.setAdapter(customAdapter);
        // Set a default value
        sItems.setSelection(customAdapter.getPosition(MaizeGrowthStage.R1));
    }

}



