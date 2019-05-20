package com.ialex.stmvumeter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.macroyau.blue2serial.BluetoothDeviceListDialog;
import com.macroyau.blue2serial.BluetoothSerial;
import com.macroyau.blue2serial.BluetoothSerialRawListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity
        implements BluetoothSerialRawListener, BluetoothDeviceListDialog.OnDeviceSelectedListener {

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final byte CMD_BACKLIGHT_ON = 0x20;
    private static final byte CMD_BACKLIGHT_OFF = 0x10;

    private BluetoothSerial bluetoothSerial;

    private ScrollView svTerminal;
    private TextView tvTerminal;
    private EditText etSend;
    private LineChartView mChart;
    private ToggleButton mBacklightToggleButton;

    private MenuItem actionConnect, actionDisconnect;

    private boolean crlf = true;

    private Queue<Integer> scheduled = new LinkedBlockingQueue<>();

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            redrawChart();
            mHandler.postDelayed(this, 16);
        }
    };

    private Axis mVerticalAxis;

    private void redrawChart() {
        List<PointValue> values = new ArrayList<>();
        for (int i = 0; i < mValues.size(); i++) {
            values.add(new PointValue(i, mValues.get(i).floatValue()));
        }

        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values)
                .setColor(Color.WHITE)
                .setHasPoints(false);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisYLeft(mVerticalAxis);

        mChart.setLineChartData(data);
        mChart.setZoomEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find UI views and set listeners
        svTerminal = findViewById(R.id.terminal);
        tvTerminal = findViewById(R.id.tv_terminal);
        mChart = findViewById(R.id.chart);
        etSend = findViewById(R.id.et_send);
        etSend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String send = etSend.getText().toString().trim();
                    if (send.length() > 0) {
                        bluetoothSerial.write(send, crlf);
                        etSend.setText("");
                    }
                }
                return false;
            }
        });

        mBacklightToggleButton = findViewById(R.id.backlight_toggle);
        mBacklightToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bluetoothSerial.write(new byte[] {CMD_BACKLIGHT_ON});
                } else {
                    bluetoothSerial.write(new byte[] {CMD_BACKLIGHT_OFF});
                }
            }
        });

        mVerticalAxis = new Axis();
        mVerticalAxis.setName("dB");
        mVerticalAxis.setHasLines(true);

        List<AxisValue> verticalAxisValues = new ArrayList<>();
        verticalAxisValues.add(new AxisValue(0.0f));
        verticalAxisValues.add(new AxisValue(20.0f));
        verticalAxisValues.add(new AxisValue(40.0f));
        verticalAxisValues.add(new AxisValue(60.0f));
        verticalAxisValues.add(new AxisValue(80.0f));
        verticalAxisValues.add(new AxisValue(100.0f));

        mVerticalAxis.setValues(verticalAxisValues);

        Viewport v = new Viewport(mChart.getMaximumViewport());
        v.top = 100;
        v.bottom = 0;
        mChart.setMaximumViewport(v);
        mChart.setCurrentViewport(v);
        mChart.setViewportCalculationEnabled(false);

        mHandler.postDelayed(mRunnable, 1000);

        // Create a new instance of BluetoothSerial
        bluetoothSerial = new BluetoothSerial(this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check Bluetooth availability on the device and set up the Bluetooth adapter
        bluetoothSerial.setup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open a Bluetooth serial port and get ready to establish a connection
        if (bluetoothSerial.checkBluetooth() && bluetoothSerial.isBluetoothEnabled()) {
            if (!bluetoothSerial.isConnected()) {
                bluetoothSerial.start();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect from the remote device and close the serial port
        bluetoothSerial.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        actionConnect = menu.findItem(R.id.action_connect);
        actionDisconnect = menu.findItem(R.id.action_disconnect);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_connect) {
            showDeviceListDialog();
            return true;
        } else if (id == R.id.action_disconnect) {
            bluetoothSerial.stop();
            return true;
        } else if (id == R.id.action_crlf) {
            crlf = !item.isChecked();
            item.setChecked(crlf);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void invalidateOptionsMenu() {
        if (bluetoothSerial == null)
            return;

        // Show or hide the "Connect" and "Disconnect" buttons on the app bar
        if (bluetoothSerial.isConnected()) {
            if (actionConnect != null)
                actionConnect.setVisible(false);
            if (actionDisconnect != null)
                actionDisconnect.setVisible(true);
        } else {
            if (actionConnect != null)
                actionConnect.setVisible(true);
            if (actionDisconnect != null)
                actionDisconnect.setVisible(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                // Set up Bluetooth serial port when Bluetooth adapter is turned on
                if (resultCode == Activity.RESULT_OK) {
                    bluetoothSerial.setup();
                }
                break;
        }
    }

    private void updateBluetoothState() {
        // Get the current Bluetooth state
        final int state;
        if (bluetoothSerial != null)
            state = bluetoothSerial.getState();
        else
            state = BluetoothSerial.STATE_DISCONNECTED;

        // Display the current state on the app bar as the subtitle
        String subtitle;
        switch (state) {
            case BluetoothSerial.STATE_CONNECTING:
                subtitle = getString(R.string.status_connecting);
                break;
            case BluetoothSerial.STATE_CONNECTED:
                subtitle = getString(R.string.status_connected, bluetoothSerial.getConnectedDeviceName());
                break;
            default:
                subtitle = getString(R.string.status_disconnected);
                break;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subtitle);
        }
    }

    private void showDeviceListDialog() {
        // Display dialog for selecting a remote Bluetooth device
        BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(this);
        dialog.setOnDeviceSelectedListener(this);
        dialog.setTitle(R.string.paired_devices);
        dialog.setDevices(bluetoothSerial.getPairedDevices());
        dialog.showAddress(true);
        dialog.show();
    }

    /* Implementation of BluetoothSerialListener */

    @Override
    public void onBluetoothNotSupported() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.no_bluetooth)
                .setPositiveButton(R.string.action_quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBluetoothDisabled() {
        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetooth, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void onBluetoothDeviceDisconnected() {
        invalidateOptionsMenu();
        updateBluetoothState();
    }

    @Override
    public void onConnectingBluetoothDevice() {
        updateBluetoothState();
    }

    @Override
    public void onBluetoothDeviceConnected(String name, String address) {
        invalidateOptionsMenu();
        updateBluetoothState();
    }

    long writeTime;
    StringBuilder sb = new StringBuilder();

    int lMSB = 0;
    int lLSB = 0;

    private ArrayList<Double> mValues = new ArrayList<>(5000);

    double p1 = 77.84;
    double p2 = -5.442e+04;
    double q1 = -673.4;

    double getDbValue(int adc) {
        return (p1*adc + p2) / (adc + q1);
    }

    @Override
    public void onBluetoothSerialReadRaw(byte[] bytes) {
        for (byte val : bytes) {
            int res = ((int) val ) * 16;

            mValues.add(getDbValue(res));
        }
//        for (byte val : bytes) {
//            int mask = (val & 0xC0) >>> 6;
//
//            if (mask == 0) {
//                lMSB = (val & 0x3F);
////                Log.d("ReadRaw", "Got MSB: " + lMSB);
//            } else if (mask == 1) {
//                lLSB = (val & 0x3F);
////                Log.d("ReadRaw", "Got LSB: " + lLSB);
//                int res = lLSB | (lMSB << 6);
//
//                if (mValues.size() > 450) {
//                    mValues.remove(0);
//                }
//
//                Log.d("Db", getDbValue(res) + "");
//                mValues.add(getDbValue(res));
//            } else if (mask == 2) {
//                int chk = val & 0x3F;
//
////                Log.d("ReadRaw", "got checksum");
//                int res = lLSB | (lMSB << 6);
//
////                Log.d("ReadRaw", "Original number verified: " + res);
////
////                if (chk == (lMSB ^ lLSB)) {
////
////                }
//            }
//        }
    }

    @Override
    public void onBluetoothSerialWriteRaw(byte[] bytes) {

    }

    @Override
    public void onBluetoothSerialRead(String message) {
        // Print the incoming message on the terminal screen
//        tvTerminal.append(getString(R.string.terminal_message_template,
//                bluetoothSerial.getConnectedDeviceName(),
//                message));
//        svTerminal.post(scrollTerminalToBottom);
//
//        sb.append(message);
    }

    @Override
    public void onBluetoothSerialWrite(String message) {
        // Print the outgoing message on the terminal screen
        tvTerminal.append(getString(R.string.terminal_message_template,
                bluetoothSerial.getLocalAdapterName(),
                message));
        svTerminal.post(scrollTerminalToBottom);
        writeTime = System.currentTimeMillis();
    }

    /* Implementation of BluetoothDeviceListDialog.OnDeviceSelectedListener */

    @Override
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        // Connect to the selected remote Bluetooth device
        bluetoothSerial.connect(device);
    }

    /* End of the implementation of listeners */

    private final Runnable scrollTerminalToBottom = new Runnable() {
        @Override
        public void run() {
            // Scroll the terminal screen to the bottom
            svTerminal.fullScroll(ScrollView.FOCUS_DOWN);
        }
    };
}
