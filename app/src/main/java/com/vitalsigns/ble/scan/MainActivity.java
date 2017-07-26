package com.vitalsigns.ble.scan;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vitalsigns.sdk.ble.scan.BleScanner;
import com.vitalsigns.sdk.ble.scan.BleScannerInterface;

public class MainActivity extends AppCompatActivity
{
  private static final int BLE_SCAN_TIIMEOUT = 10 * 1000;

  private BleScanner mBleScanner = null;
  private boolean mScanStart = false;

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

    mBleScanner = new BleScanner();
    if(mBleScanner.initialize(this, mBleScannerFailInitialize))
    {
      mBleScanner.startScan(BLE_SCAN_TIIMEOUT, mBleScannerScanTimeout, mBleScannerDeviceFound);
      mScanStart = true;
    }
  }

  @Override
  protected void onStop()
  {
    super.onStop();

    if(mScanStart)
    {
      mBleScanner.stopScan();
      mScanStart = false;
    }
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

      ((LinearLayout)findViewById(R.id.list)).addView(new ListItemView(MainActivity.this).setInfo(name, rssi, mac));
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
