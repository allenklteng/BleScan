package com.vitalsigns.ble.scan;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vitalsigns.sdk.ble.scan.BleScanner;
import com.vitalsigns.sdk.ble.scan.BleScannerInterface;
import com.vitalsigns.sdk.utility.RequestPermission;
import com.vitalsigns.sdk.utility.Utility;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
  private static final String LOG_TAG = "MainActivity";
  private static final int BLE_SCAN_TIIMEOUT = 10 * 1000;

  private BleScanner mBleScanner = null;
  private boolean mScanStart = false;
  private ArrayList<String> mDeviceMacList = null;

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

    /// [AT-PM] : Initialize list ; 07/26/2017
    mDeviceMacList = new ArrayList<>();

    /// [AT-PM] : Start BLE scanner ; 07/26/2017
    mBleScanner = new BleScanner();
    if(mBleScanner.initialize(this, mBleScannerFailInitialize))
    {
      if(RequestPermission.accessCoarseLocation(MainActivity.this))
      {
        mBleScanner.startScan(BLE_SCAN_TIIMEOUT, mBleScannerScanTimeout, mBleScannerDeviceFound);
        mScanStart = true;
      }
    }
  }

  @Override
  protected void onStop()
  {
    super.onStop();

    /// [AT-PM] : Stop BLE scanner ; 07/26/2017
    if(mScanStart)
    {
      mBleScanner.stopScan();
      mScanStart = false;
    }

    /// [AT-PM] : Release memory ; 07/26/2017
    mDeviceMacList.clear();
    mDeviceMacList = null;
  }

  private BleScannerInterface.OnDeviceFound mBleScannerDeviceFound = new BleScannerInterface.OnDeviceFound()
  {
    @Override
    public void OnEvent(ScanResult scanResult)
    {
      int rssi = scanResult.getRssi();
      BluetoothDevice bluetoothDevice = scanResult.getDevice();
      String name = bluetoothDevice.getName();
      String mac = bluetoothDevice.getAddress();

      /// [AT-PM] : Add into list ; 07/26/2017
      if(!mDeviceMacList.contains(name))
      {
        mDeviceMacList.add(name);
        ((LinearLayout)findViewById(R.id.list)).addView(new ListItemView(MainActivity.this).setInfo(name, rssi, mac));
        Log.d(LOG_TAG, String.format("Find %d device %s(%s) with RSSI = %d", mDeviceMacList.size(), name, mac, rssi));
      }
    }
  };

  private BleScannerInterface.OnFailInitialize mBleScannerFailInitialize = new BleScannerInterface.OnFailInitialize()
  {
    @Override
    public void OnBleNotSupported()
    {
      Toast.makeText(MainActivity.this, R.string.toast_ble_not_support, Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnBleDisabled()
    {
      Toast.makeText(MainActivity.this, R.string.toast_ble_disabled, Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnLocationServiceDisabled()
    {
      Toast.makeText(MainActivity.this, R.string.toast_location_service_disabled, Toast.LENGTH_LONG).show();
    }
  };

  private BleScannerInterface.OnScanTimeout mBleScannerScanTimeout = new BleScannerInterface.OnScanTimeout()
  {
    @Override
    public void OnEvent()
    {
      mScanStart = false;
      Toast.makeText(MainActivity.this, R.string.toast_ble_timeout, Toast.LENGTH_LONG).show();

      /// [AT-PM] : Restart scanning ; 07/26/2017
      mBleScanner.startScan(BLE_SCAN_TIIMEOUT, mBleScannerScanTimeout, mBleScannerDeviceFound);
      mScanStart = true;
    }
  };
}
