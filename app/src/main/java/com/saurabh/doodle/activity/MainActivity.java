package com.saurabh.doodle.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.saurabh.doodle.R;
import com.saurabh.doodle.view.DoodleCanvas;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DoodleCanvas.GetButtonCallback {

    @BindView(R.id.customDraw)
    DoodleCanvas customDrawView;

    @BindView(R.id.btnRedo)
    Button btnRedo;

    @BindView(R.id.btnUndo)
    Button btnUndo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        customDrawView.setCallback(this);
        listeners();
    }

    private void listeners(){
        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDrawView.undoView();
            }
        });

        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDrawView.redoView();
            }
        });
    }

    @Override
    public void undoStatus(boolean status) {
        btnUndo.setEnabled(status);
    }

    @Override
    public void redoStatus(boolean status) {
        btnRedo.setEnabled(status);
    }
}
