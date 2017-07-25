package com.vitalsigns.ble.scan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onStart()
  {
    super.onStart();

    ((LinearLayout)findViewById(R.id.list)).addView(new ListItemView(this).setInfo("Test", -100, "aa:bb:cc:dd:ee:ff"));
  }
}
