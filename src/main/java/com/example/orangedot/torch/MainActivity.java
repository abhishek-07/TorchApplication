package com.example.orangedot.torch;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {
    private static final int TAKE_PICTURE = 1;
    boolean hasFlash = false;
    int progressSeekbar;
    Switch s;
    SeekBar seekBar;
    String str[] = {"STATIC", "PULSATING"};
    Spinner spn;
    View view;
    private CameraManager cam;
    private String camid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spn = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, str);
        spn.setAdapter(adp);
        seekBar = (SeekBar) findViewById(R.id.seekbar1);
        // view = (View) findViewById(R.id.seekbar1);
        seekBar.setOnSeekBarChangeListener(this);
        //seekBar.setVisibility(View.GONE);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progressSeekbar = progress;
        Toast.makeText(this, "FLASH PULSE SET TO " + progress, Toast.LENGTH_SHORT).show();
        if (progress > 0) {
            timeSplit();
        }
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        Toast.makeText(this, "Set the pulse frequency", Toast.LENGTH_SHORT).show();
    }

    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public void onItemSelected(AdapterView<?> parent, View v, int p, long id) {
        if (p == 2) {
            seekBar.setVisibility(parent.VISIBLE);
        } else {
            seekBar.setVisibility(parent.GONE);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void flash(View c) {
        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
        cam = (CameraManager) getSystemService(CAMERA_SERVICE);
        s = (Switch) findViewById(R.id.switchbutton1);
        if (hasFlash) {
            turn_on();
        }
    }

    public void camfunc(View v1) {
        Intent i = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, TAKE_PICTURE);
        }
    }
    @TargetApi(23)
    private void turn_on() {
        try {
            camid = cam.getCameraIdList()[0];
            if (s.isChecked()) {
                cam.setTorchMode(camid, true);
                Toast.makeText(this, "FLASH LIGHT TURNED ON", Toast.LENGTH_SHORT).show();
            } else {
                cam.setTorchMode(camid, false);
                Toast.makeText(this, "FLASH LIGHT TURNED OFF", Toast.LENGTH_SHORT).show();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void timeSplit() {
        for (int i = 0; i < progressSeekbar; i++) {
            try {
                try {
                    cam.setTorchMode(camid, true);
                    Thread.sleep(500);
                    cam.setTorchMode(camid, false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}