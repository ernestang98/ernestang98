package com.mdp34.bluetooth;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.mdp34.R;
import com.mdp34.bluetooth.DeviceListAdapter;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothFragment extends AppCompatActivity {
    private static final String TAG = "BluetoothPopUp";
    private String connStatus;
    BluetoothAdapter mBluetoothAdapter;
    public ArrayList<BluetoothDevice> newDevices;
    public ArrayList<BluetoothDevice> pairedBTDevices;
    public DeviceListAdapter newDeviceListAdapter;
    public DeviceListAdapter pairedDeviceListAdapter;
    TextView connStatusTextView;
    ListView otherDevicesListView;
    ListView pairedDevicesListView;
    Button connectBtn;
    ProgressDialog myDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    BluetoothService mBluetoothConnection;
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static BluetoothDevice mBTDevice;

    public static boolean retryConnection = false;
    Handler reconnectionHandler = new Handler();
    public Runnable reconnectionRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (BluetoothService.connStatusFlag == false) {
                    startBTConnection(mBTDevice,myUUID);
                }
                reconnectionHandler.removeCallbacks(reconnectionRunnable);
                retryConnection = false;
            } catch (Exception e) {
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Switch bluetoothSwitch = (Switch) findViewById(R.id.bluetoothSwitch);
        if(mBluetoothAdapter.isEnabled()){
            bluetoothSwitch.setChecked(true);
            bluetoothSwitch.setText("ON");
        }

        otherDevicesListView = (ListView) findViewById(R.id.otherDevicesListView);
        pairedDevicesListView = (ListView) findViewById(R.id.pairedDevicesListView);
        newDevices = new ArrayList<>();
        pairedBTDevices = new ArrayList<>();

        connectBtn = (Button) findViewById(R.id.connectBtn);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        IntentFilter filter2 = new IntentFilter("ConnectionStatus");
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver5, filter2);

        IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver1, BTIntent);

        IntentFilter discoverIntent = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, discoverIntent);

        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);

        otherDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mBluetoothAdapter.cancelDiscovery();
                pairedDevicesListView.setAdapter(pairedDeviceListAdapter);

                String deviceName = newDevices.get(i).getName();
                String deviceAddress = newDevices.get(i).getAddress();
                Log.d(TAG, "onItemClick: A device is selected.");
                Log.d(TAG, "onItemClick: DEVICE NAME: " + deviceName);
                Log.d(TAG, "onItemClick: DEVICE ADDRESS: " + deviceAddress);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Log.d(TAG, "onItemClick: Initiating pairing with " + deviceName);
                    newDevices.get(i).createBond();

                    mBluetoothConnection = new BluetoothService(BluetoothFragment.this);
                    mBTDevice = newDevices.get(i);
                }
            }
        });

        pairedDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mBluetoothAdapter.cancelDiscovery();
                otherDevicesListView.setAdapter(newDeviceListAdapter);

                String deviceName = pairedBTDevices.get(i).getName();
                String deviceAddress = pairedBTDevices.get(i).getAddress();
                Log.d(TAG, "onItemClick: A device is selected.");
                Log.d(TAG, "onItemClick: DEVICE NAME: " + deviceName);
                Log.d(TAG, "onItemClick: DEVICE ADDRESS: " + deviceAddress);

                mBluetoothConnection = new BluetoothService(BluetoothFragment.this);
                mBTDevice = pairedBTDevices.get(i);
            }
        });

        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.d(TAG, "onChecked: Switch button toggled. Enabling/Disabling Bluetooth");
                if(isChecked){
                    compoundButton.setText("ON");
                }else
                {
                    compoundButton.setText("OFF");
                }

                if(mBluetoothAdapter ==null){
                    Toast.makeText(BluetoothFragment.this, "Device Does Not Support Bluetooth capabilities!", Toast.LENGTH_LONG).show();
                    compoundButton.setChecked(false);
                }
                else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Log.d(TAG, "enableDisableBT: enabling Bluetooth");
                        Log.d(TAG, "enableDisableBT: Making device discoverable for 600 seconds.");

                        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 600);
                        startActivity(discoverableIntent);

                        compoundButton.setChecked(true);

                    }
                    if (mBluetoothAdapter.isEnabled()) {
                        Log.d(TAG, "enableDisableBT: disabling Bluetooth");
                        mBluetoothAdapter.disable();

                    }
                }
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBTDevice ==null)
                {
                    Toast.makeText(BluetoothFragment.this, "Please Select a Device before connecting.", Toast.LENGTH_LONG).show();
                }
                else {
                    if (mBluetoothConnection.disconnect() == true){
                        retryConnection = true;
                        Log.d(TAG, "mBroadcastReceiver5: Disconnected from "+mBTDevice.getName());
                        Toast.makeText(BluetoothFragment.this, "Disconnected from "+mBTDevice.getName(), Toast.LENGTH_LONG).show();
                        mBluetoothConnection = new BluetoothService(BluetoothFragment.this);

                        sharedPreferences = getApplicationContext().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("connStatus", "Disconnected");
                        TextView connStatusTextView = findViewById(R.id.connStatusTextView);
                        connStatusTextView.setText("Disconnected");
                        editor.commit();

                        try {
                            myDialog.show();
                        }catch (Exception e){
                            Log.d(TAG, "BluetoothPopUp: mBroadcastReceiver5 Dialog show failure");
                        }
                        reconnectionHandler.postDelayed(reconnectionRunnable, 5000);
                    } else if (mBluetoothConnection.disconnect() == false){
                        startBTConnection(mBTDevice,myUUID);
                    }
                }
            }
        });


        ImageButton backBtn = findViewById(R.id.backBtn);

        connStatusTextView = (TextView) findViewById(R.id.connStatusTextView);
        connStatus ="Disconnected";
        sharedPreferences = getApplicationContext().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("connStatus")) {
            connStatus = sharedPreferences.getString("connStatus", "");
        }

        connStatusTextView.setText(connStatus);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPreferences.edit();
                editor.putString("connStatus", connStatusTextView.getText().toString());
                editor.commit();
                finish();
            }
        });

        myDialog = new ProgressDialog(BluetoothFragment.this);
        myDialog.setMessage("Waiting for other device to reconnect...");
        myDialog.setCancelable(false);
        myDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void toggleButtonScan(View view){
        Log.d(TAG, "toggleButton: Scanning for unpaired devices.");
        newDevices.clear();
        if(mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Toast.makeText(BluetoothFragment.this, "Please turn on Bluetooth first!", Toast.LENGTH_SHORT).show();
            }
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
                Log.d(TAG, "toggleButton: Cancelling Discovery.");

                checkBTPermissions();
                mBluetoothAdapter.startDiscovery();

            } else if (!mBluetoothAdapter.isDiscovering()) {
                checkBTPermissions();
                mBluetoothAdapter.startDiscovery();
            }
            pairedBTDevices.clear();
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            Log.d(TAG, "toggleButton: Number of paired devices found: "+ pairedDevices.size());
            for(BluetoothDevice d : pairedDevices){
                Log.d(TAG, "Paired Devices: "+ d.getName() +" : " + d.getAddress());
                pairedBTDevices.add(d);
                pairedDeviceListAdapter = new DeviceListAdapter(this, R.layout.bluetooth_device_list, pairedBTDevices);
                pairedDevicesListView.setAdapter(pairedDeviceListAdapter);
            }


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(this,"Manifest.permission.ACCESS_FINE_LOCATION") + ContextCompat.checkSelfPermission(this,"Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");

        }
    }
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");

                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                final int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                newDevices.add(device);
                Log.d(TAG, "onReceive: "+ device.getName() +" : " + device.getAddress());
                newDeviceListAdapter = new DeviceListAdapter(context, R.layout.bluetooth_device_list, newDevices);
                otherDevicesListView.setAdapter(newDeviceListAdapter);

            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BOND_BONDED.");
                    Toast.makeText(BluetoothFragment.this, "Successfully paired with " + mDevice.getName(), Toast.LENGTH_SHORT).show();
                    mBTDevice = mDevice;
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.d(TAG, "BOND_BONDING.");
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.d(TAG, "BOND_NONE.");
                }
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver5 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice mDevice = intent.getParcelableExtra("Device");
            String status = intent.getStringExtra("Status");
            sharedPreferences = getApplicationContext().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

            if(status.equals("connected")){
                try {
                    myDialog.dismiss();
                } catch(NullPointerException e){
                    e.printStackTrace();
                }

                Log.d(TAG, "mBroadcastReceiver5: Device now connected to "+mDevice.getName());
                Toast.makeText(BluetoothFragment.this, "Device now connected to "+mDevice.getName(), Toast.LENGTH_LONG).show();
                editor.putString("connStatus", "Connected to " + mDevice.getName());
                connStatusTextView.setText("Connected to " + mDevice.getName());
                editor.putString("connectedDevice", mDevice.getName());
                editor.commit();
            }
            else if(status.equals("disconnected") && retryConnection == false){

                Log.d(TAG, "mBroadcastReceiver5: Disconnected from "+mDevice.getName());
                Toast.makeText(BluetoothFragment.this, "Disconnected from "+mDevice.getName(), Toast.LENGTH_LONG).show();
                mBluetoothConnection = new BluetoothService(BluetoothFragment.this);

                sharedPreferences = getApplicationContext().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("connStatus", "Disconnected");
                TextView connStatusTextView = findViewById(R.id.connStatusTextView);
                connStatusTextView.setText("Disconnected");
                editor.commit();

                try {
                    myDialog.show();
                }catch (Exception e){
                    Log.d(TAG, "BluetoothPopUp: mBroadcastReceiver5 Dialog show failure");
                }
                retryConnection = true;
                reconnectionHandler.postDelayed(reconnectionRunnable, 5000);

            }
            editor.commit();
        }
    };

    public static void startConnection(Context context){
        BluetoothService newService = new BluetoothService(context);
        newService.activateClientThread(mBTDevice, myUUID);
    }

    public void startBTConnection(BluetoothDevice device, UUID uuid){
        mBluetoothConnection.activateClientThread(device, uuid);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mBroadcastReceiver1);
            unregisterReceiver(mBroadcastReceiver2);
            unregisterReceiver(mBroadcastReceiver3);
            unregisterReceiver(mBroadcastReceiver4);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver5);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("mBTDevice", mBTDevice);
        data.putExtra("myUUID",myUUID);
        setResult(RESULT_OK, data);
        super.finish();
    }
}