package com.example.sakira;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sakira.R;

public class SelectActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    Button submit, clear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Algorithm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        submit = (Button)findViewById(R.id.submit);
        clear = (Button)findViewById(R.id.clear);
        radioGroup = (RadioGroup)findViewById(R.id.groupradio);

        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override

                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId)
                    {

                        RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                    }
                });
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {


                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(SelectActivity.this,
                            "No answer has been selected",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                else {

                    RadioButton radioButton
                            = (RadioButton)radioGroup
                            .findViewById(selectedId);

                    Toast.makeText(SelectActivity.this,radioButton.getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SelectActivity.this,MainActivity.class);
                    intent.putExtra("Algo",radioButton.getText());
                    startActivity(intent);
                    finish();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                // Clear RadioGroup
                // i.e. reset all the Radio Buttons
                radioGroup.clearCheck();
            }
        });
    }
}