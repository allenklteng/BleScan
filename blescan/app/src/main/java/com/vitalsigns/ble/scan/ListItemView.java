package com.vitalsigns.ble.scan;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by allen_teng on 25/07/2017.
 */

public class ListItemView extends LinearLayout
{
  public ListItemView(Context context)
  {
    super(context);
    constructor(context);
  }

  public ListItemView(Context context, @Nullable AttributeSet attrs)
  {
    super(context, attrs);
    constructor(context);
  }

  public ListItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    constructor(context);
  }

  public ListItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
  {
    super(context, attrs, defStyleAttr, defStyleRes);
    constructor(context);
  }

  /**
   * Constructor
   * @param context Context
   */
  private void constructor(Context context)
  {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.view_list_item, this, true);
  }

  /**
   * Set information
   * @param name device name
   * @param rssi RSSI value
   * @param mac MAC address
   */
  public ListItemView setInfo(String name, int rssi, String mac)
  {
    ((TextView)findViewById(R.id.name)).setText(name);
    ((TextView)findViewById(R.id.rssi)).setText(String.format("%d", rssi));
    ((TextView)findViewById(R.id.mac)).setText(mac);
    return (this);
  }
}
