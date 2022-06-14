package com.mdp34;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.mdp34.bluetooth.BluetoothFragment;
import com.mdp34.bluetooth.BluetoothService;
import com.mdp34.message.MessageFragment;
import com.mdp34.message.SectionsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import com.mdp34.ControllerFragment;

public class MainActivity extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Context context;

    BluetoothDevice mBTDevice;
    private static UUID myUUID;
    ProgressDialog myDialog;
    public static TextView robotStatus;
    public static int robotPosX = 0;
    public static int robotPosY = 0;
    public static int robotDirection = 0;

    public static int robotPosMsgCount = 0;
    public static boolean robotPlaced = false;

    public static int obstacleCount = 0;

    public static ArrayList<Integer> obstacleList = new ArrayList<Integer>();
    public static ArrayList<Integer> obstacleDirectionList = new ArrayList<Integer>();

    public static EditText xField;
    public static EditText yField;
    public static Button btnAddObs;
    public static int obsX;
    public static int obsY;

    public static ArrayList<Integer> robotDirReceived = new ArrayList<Integer>();
    public static ArrayList<Integer> robotPosXReceived = new ArrayList<Integer>();
    public static ArrayList<Integer> robotPosYReceived = new ArrayList<Integer>();
    public static boolean explored = false;
    public static ArrayList<Integer> exploredPathList = new ArrayList<Integer>();

    public static ArrayList<Integer> obstacleOrderReceived = new ArrayList<Integer>();
    public static int targetCount = 0;

    public static final HashMap<Integer, String> targetHash;
    static {
        targetHash = new HashMap<>();
        targetHash.put(11, "1");targetHash.put(12, "2");
        targetHash.put(13, "3");targetHash.put(14, "4");
        targetHash.put(15, "5");targetHash.put(16, "6");
        targetHash.put(17, "7");targetHash.put(18, "8");
        targetHash.put(19, "9");targetHash.put(20, "A");
        targetHash.put(21, "B");targetHash.put(22, "C");
        targetHash.put(23, "D");targetHash.put(24, "E");
        targetHash.put(25, "F");targetHash.put(26, "G");
        targetHash.put(27, "H");targetHash.put(28, "S");
        targetHash.put(29, "T");targetHash.put(30, "U");
        targetHash.put(31, "V");targetHash.put(32, "W");
        targetHash.put(33, "X");targetHash.put(34, "Y");
        targetHash.put(35, "Z");targetHash.put(36, "Up Arrow");
        targetHash.put(37, "Down Arrow");targetHash.put(38, "Right Arrow");
        targetHash.put(39, "Left Arrow");targetHash.put(40, "Stop");
        targetHash.put(41, "?");
    }

    Button btnFastest;

    private static final String TAG = "Main Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //gridmap
        createGrid();
        //message
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(9999);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("incomingMessage"));
        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //button for challenge 2
        btnFastest = (Button) findViewById(R.id.btnFastest);
        btnFastest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                printMessage("AND|CAR|CH2");
            }
        });

        //input for adding obstacle
        xField = (EditText) findViewById(R.id.xField);
        yField = (EditText) findViewById(R.id.yField);

        Spinner spinner = (Spinner) findViewById(R.id.dirField);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dir_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //button for adding obstacle
        btnAddObs = (Button) findViewById(R.id.btnAddObs);
        btnAddObs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                obsX = -1;
                obsY = -1;

                try {
                    obsX = Integer.valueOf(xField.getText().toString());
                    obsY = Integer.valueOf(yField.getText().toString());
                }
                catch (Exception e){}

                //Check if coordinates are valid
                if (obsX >= 0 && obsX <= 19 && obsY >= 0 && obsY <= 19){

                    //Get tvID that has robot
                    int tl_id = (robotPosX*100+robotPosY);
                    int tr_id = ((robotPosX+1)*100+robotPosY);
                    int bl_id = (robotPosX*100+robotPosY-1);
                    int br_id = ((robotPosX+1)*100+robotPosY-1);

                    //Check if coordinate has robot
                    int obsID = obsX*100+obsY;

                    if (obsID == tl_id && robotPlaced == true){}
                    else if (obsID == tr_id && robotPlaced == true){}
                    else if (obsID == bl_id && robotPlaced == true){}
                    else if (obsID == br_id && robotPlaced == true){}
                    else {
                        boolean empty = true;
                        //Check if coordinate has obstacle
                        for(int i = 0; i < obstacleList.size(); i++) {
                            if (obstacleList.get(i) == obsID) {
                                empty = false;
                            }
                        }
                        //If empty, add obstacle
                        if (empty == true){
                            TextView temptv = findViewById(obsID);
                            addObstacle(temptv);
                            Spinner mySpinner = (Spinner) findViewById(R.id.dirField);
                            String dir = mySpinner.getSelectedItem().toString();
                            int noOfRotation = 0;
                            switch (dir){
                                case "East":
                                    noOfRotation = 1;
                                    break;
                                case "South":
                                    noOfRotation = 2;
                                    break;
                                case "West":
                                    noOfRotation = 3;
                                    break;
                            }
                            for (int i = 0; i < noOfRotation; i++){
                                rotateObstacle(obsID);
                            }
                        }
                    }
                }
            }
        });

        // Set up sharedPreferences
        MainActivity.context = getApplicationContext();
        this.sharedPreferences();

        editor.putString("direction","None");
        editor.putString("robotStatus","Not Available");
        if (BluetoothService.connStatusFlag == false) {
            editor.putString("connStatus", "Disconnected");
        }
        editor.putString("message", "");

        editor.commit();

        //dialog
        myDialog = new ProgressDialog(MainActivity.this);
        myDialog.setMessage("Waiting for other device to reconnect...");
        myDialog.setCancelable(false);
        myDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // robot status
        robotStatus = findViewById(R.id.RobotStatusTextView);
        refreshRobotStatus();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bluetooth:
                Intent popup = new Intent(MainActivity.this, BluetoothFragment.class);
                startActivity(popup);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode == Activity.RESULT_OK){
                    mBTDevice = (BluetoothDevice) data.getExtras().getParcelable("mBTDevice");
                    myUUID = (UUID) data.getSerializableExtra("myUUID");
                }
        }
    }

    public static void sharedPreferences() {
        sharedPreferences = MainActivity.getSharedPreferences(MainActivity.context);
        editor = sharedPreferences.edit();
    }

    // Send message to bluetooth
    public static void printMessage(String message) {
        showLog("Entering printMessage");
        editor = sharedPreferences.edit();

        if (BluetoothService.connStatusFlag == true) {
            byte[] bytes = message.getBytes(Charset.defaultCharset());
            BluetoothService.write(bytes);
        }
        showLog(message);
        editor.putString("message", MessageFragment.getMessageReceivedTextView().getText() + "\n" + "ME:" + message);
        editor.commit();
        refreshMessageReceived();
        showLog("Exiting printMessage");
    }

    public static void refreshMessageReceived() {
        MessageFragment.getMessageReceivedTextView().setText(sharedPreferences.getString("message", ""));
    }
    public static void refreshRobotStatus() {
        robotStatus.setText(sharedPreferences.getString("robotStatus", ""));
    }


    public static void receiveMessage(String message) {
        showLog("Entering receiveMessage");
        sharedPreferences();
        editor.putString("message", sharedPreferences.getString("message", "") + "\n" + message);
        editor.commit();
        showLog("Exiting receiveMessage");
    }

    private static void showLog(String message) {
        Log.d(TAG, message);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);
    }

    public static TextView getRobotStatus() {  return robotStatus; }
    private BroadcastReceiver mBroadcastReceiver5 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice mDevice = intent.getParcelableExtra("Device");
            String status = intent.getStringExtra("Status");
            sharedPreferences();

            if(status.equals("connected")){
                try {
                    myDialog.dismiss();
                } catch(NullPointerException e){
                    e.printStackTrace();
                }

                Log.d(TAG, "mBroadcastReceiver5: Device now connected to "+mDevice.getName());
                Toast.makeText(MainActivity.this, "Device now connected to "+mDevice.getName(), Toast.LENGTH_LONG).show();
                if (sharedPreferences.contains("connectedDevice") && (sharedPreferences.getString("connectedDevice", "")) != mDevice.getName()){
                    editor.putString("connectedDevice", mDevice.getName());
                }
                editor.putString("connStatus", "Connected to " + mDevice.getName());
                editor.commit();
            }
            else if(status.equals("disconnected") && BluetoothFragment.retryConnection==false){
                Log.d(TAG, "mBroadcastReceiver5: Disconnected from "+mDevice.getName());
                Toast.makeText(MainActivity.this, "Disconnected from "+mDevice.getName(), Toast.LENGTH_LONG).show();

                editor.putString("connStatus", "Disconnected");
                editor.commit();
                myDialog.show();

                BluetoothFragment.retryConnection = true;
                reconnectionHandler.postDelayed(reconnectionRunnable, 5000);
                Log.d(TAG, "I am disconnected in Main , retryconnection =================================== "+BluetoothFragment.retryConnection);
            }
            editor.commit();
        }
    };

    public static Handler reconnectionHandler = new Handler();
    public Runnable reconnectionRunnable = new Runnable() {
        @Override
        public void run() {
            try {

                if (BluetoothService.connStatusFlag == false) {
                    Log.d(TAG, "I am trying to reconnect now in Main ================================== ");
                    BluetoothFragment.startConnection(MainActivity.this);
                }
                reconnectionHandler.removeCallbacks(reconnectionRunnable);
                BluetoothFragment.retryConnection = false;
                Log.d(TAG, "I am trying to reconnect now in Main, retryconnection =================================== "+BluetoothFragment.retryConnection);
            } catch (Exception e) {
                Log.d(TAG, "reconnection error:",e);
            }
        }
    };

    final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("receivedMessage");
            showLog("receivedMessage: message --- " + message);
            sharedPreferences();

            // receive message for target
            try {
                if (message.length() == 2) {
                    // change the font of obstacle number
                    editor.putString("robotStatus", "Received Target");
                    String targetNo = message.substring(0, 2);
//                    String targetString = targetHash.get(Integer.parseInt(targetNo));
                    Integer tempNo = obstacleOrderReceived.get(targetCount);
                    updateTarget(tempNo, targetNo);

                    targetCount++;

                    if (targetCount == obstacleOrderReceived.size()){
                        obstacleOrderReceived.removeAll(obstacleOrderReceived);
                        targetCount = 0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // receive message for the sequence of obstacle
            try {
                if (message.length() > 5  && message.substring(0, 5).equals("OBST_")) {
                    // change the font of obstacle number
                    editor.putString("robotStatus", "Received obst Order");

                    String obstacleOrd = message.replace("OBST_","");
                    String[] obstacleOrdArr = obstacleOrd.split(",");

                    String obstXcor,obstYcor;
                    Integer obstacleNo = 0;

                    for (int i =0; i < obstacleOrdArr.length; i++) {
                        obstXcor = obstacleOrdArr[i].substring(0,2);
                        obstYcor = obstacleOrdArr[i].substring(2,4);
                        obstacleNo = Integer.parseInt(obstXcor)*100 + Integer.parseInt(obstYcor);
                        for (int j=0 ; j<obstacleList.size();j++) {
                            if (obstacleNo.equals(obstacleList.get(j))) {
                                obstacleOrderReceived.add(obstacleList.get(j));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // receive message for robot position
            try {
                if (message.length()> 4 && message.substring(0, 4).equals("ROW_")) {
                    robotPosMsgCount +=1;
                    String yCor = message.replace("ROW_","");
                    String[] yCorArr = yCor.split(",");
                    showLog("hihiy " + yCorArr[0]);
                    for (int i =0; i < yCorArr.length; i++) {
                        robotPosYReceived.add(Integer.parseInt(yCorArr[i]));
                    }
                }

                if(message.length()> 4 && message.substring(0, 4).equals("COL_")) {
                    robotPosMsgCount +=1;
                    String xCor = message.replace("COL_","");
                    String[] xCorArr = xCor.split(",");
                    showLog("hihi x" + xCorArr[0]);
                    for (int i =0; i< xCorArr.length; i++) {
                        robotPosXReceived.add(Integer.parseInt(xCorArr[i]));
                    }

                }

                if(message.length()> 4 && message.substring(0, 4).equals("DIR_")){
                    robotPosMsgCount +=1;
                    String dir = message.replace("DIR_","");
                    String[] dirArr = dir.split(",");
                    for (int i =0; i < dirArr.length; i++){
                        if (dirArr[i].equals("NORTH")) {
                            robotDirReceived.add(0);
                        } else if (dirArr[i].equals("SOUTH")) {
                            robotDirReceived.add(2);
                        } else if (dirArr[i].equals("EAST")) {
                            robotDirReceived.add(1);
                        } else if (dirArr[i].equals("WEST")) {
                            robotDirReceived.add(3);
                        }
                    }
                }

                if(robotPosMsgCount == 3) {
                    for (int i =0;i<robotDirReceived.size();i++) {
                        if (robotPlaced == true) {
                            int tl_id = (robotPosX * 100 + robotPosY);
                            int tr_id = ((robotPosX + 1) * 100 + robotPosY);
                            int bl_id = (robotPosX * 100 + robotPosY - 1);
                            int br_id = ((robotPosX + 1) * 100 + robotPosY - 1);

                            TextView tv_tl = (TextView) findViewById(tl_id);
                            TextView tv_tr = (TextView) findViewById(tr_id);
                            TextView tv_bl = (TextView) findViewById(bl_id);
                            TextView tv_br = (TextView) findViewById(br_id);

                            tv_tl.setBackgroundResource(R.drawable.dark_gray_cell);
                            tv_tl.setRotation(0);
                            tv_tr.setBackgroundResource(R.drawable.dark_gray_cell);
                            tv_tr.setRotation(0);
                            tv_bl.setBackgroundResource(R.drawable.dark_gray_cell);
                            tv_bl.setRotation(0);
                            tv_br.setBackgroundResource(R.drawable.dark_gray_cell);
                            tv_br.setRotation(0);

                            exploredPathList.add(tl_id);
                            exploredPathList.add(tr_id);
                            exploredPathList.add(bl_id);
                            exploredPathList.add(br_id);
                            explored = true;
                        }
                        robotPlaced = true;
                        editor.putString("robotStatus", "Received Robot Path");
//                        if (robotDirReceived.get(i).equals(0)){
//                            setCar(robotPosXReceived.get(i)-1, robotPosYReceived.get(i)+1, robotDirReceived.get(i));
//                        } else if (robotDirReceived.get(i).equals(1)){
//                            setCar(robotPosXReceived.get(i), robotPosYReceived.get(i)+1, robotDirReceived.get(i));
//                        } else if (robotDirReceived.get(i).equals(2)){
//                            setCar(robotPosXReceived.get(i), robotPosYReceived.get(i), robotDirReceived.get(i));
//                        } else if (robotDirReceived.get(i).equals(3)){
//                            setCar(robotPosXReceived.get(i)-1, robotPosYReceived.get(i), robotDirReceived.get(i));
//                        }
                        setCar(robotPosXReceived.get(i), robotPosYReceived.get(i), robotDirReceived.get(i));

                    }
                    robotPosXReceived.removeAll(robotPosXReceived);
                    robotPosYReceived.removeAll(robotPosYReceived);
                    robotDirReceived.removeAll(robotDirReceived);
                    robotPosMsgCount=0;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            String receivedText = sharedPreferences.getString("message", "") + "\n" +sharedPreferences.getString("connectedDevice", "") + ":" + message;
            editor.putString("message", receivedText);
            editor.commit();
            refreshMessageReceived();
            refreshRobotStatus();
        }
    };

    //Function to return Toast message of Robot Position
    public void printCoor(View view){
        int[] coor = (int[]) view.getTag();
        int x = coor[0];
        int y = coor[1];
        String text = "X: " + x + ", Y: " + y;
        Toast.makeText(getApplicationContext(), "" + text, Toast.LENGTH_SHORT).show();
    }

    //Function to set car
    public void setCar(int x, int y, int direction){

        if (robotPlaced == true){

            //x is most-right column
            if (x == 19){
                x = x-1;
            }

            //y is bottom row
            if (y == 0){
                y = y+1;
            }

            robotPosX = x;
            robotPosY = y;
            robotDirection = (direction + 4) % 4;

            int tl_id = (x*100+y);
            int tr_id = ((x+1)*100+y);
            int bl_id = (x*100+y-1);
            int br_id = ((x+1)*100+y-1);
            TextView tv_tl = findViewById(tl_id);
            TextView tv_tr = findViewById(tr_id);
            TextView tv_bl = findViewById(bl_id);
            TextView tv_br = findViewById(br_id);

            //Robot facing north
            if (robotDirection == 0){

                tv_tl.setBackgroundResource(R.drawable.car_tl);
                tv_tl.setRotation(0);
                tv_tr.setBackgroundResource(R.drawable.car_tr);
                tv_tr.setRotation(0);
                tv_bl.setBackgroundResource(R.drawable.green_cell);
                tv_bl.setRotation(0);
                tv_br.setBackgroundResource(R.drawable.green_cell);
                tv_br.setRotation(0);
            }

            //Robot facing east
            else if (robotDirection == 1){

                tv_tl.setBackgroundResource(R.drawable.green_cell);
                tv_tl.setRotation(0);
                tv_tr.setBackgroundResource(R.drawable.car_tl);
                tv_tr.setRotation(90);
                tv_bl.setBackgroundResource(R.drawable.green_cell);
                tv_bl.setRotation(0);
                tv_br.setBackgroundResource(R.drawable.car_tr);
                tv_br.setRotation(90);
            }

            //Robot facing south
            else if (robotDirection == 2){

                tv_tl.setBackgroundResource(R.drawable.green_cell);
                tv_tl.setRotation(0);
                tv_tr.setBackgroundResource(R.drawable.green_cell);
                tv_tr.setRotation(0);
                tv_bl.setBackgroundResource(R.drawable.car_tr);
                tv_bl.setRotation(180);
                tv_br.setBackgroundResource(R.drawable.car_tl);
                tv_br.setRotation(180);
            }

            //Robot facing west
            else if (robotDirection == 3){

                tv_tl.setBackgroundResource(R.drawable.car_tr);
                tv_tl.setRotation(-90);
                tv_tr.setBackgroundResource(R.drawable.green_cell);
                tv_tr.setRotation(0);
                tv_bl.setBackgroundResource(R.drawable.car_tl);
                tv_bl.setRotation(-90);
                tv_br.setBackgroundResource(R.drawable.green_cell);
                tv_br.setRotation(0);
            }
        }
    }

    //Function to rotate obstacle
    public void rotateObstacle(int tvID){

        int len = obstacleList.size();
        int i = 0;
        int obsDir = 0;
        TextView obs = findViewById(tvID);

        while (i < len){
            if (tvID == obstacleList.get(i)){
                break;
            }
            i++;
        }

        if (i < len){

            obsDir = (obstacleDirectionList.get(i) + 1) % 4;

            if (obsDir == 0){
                obs.setBackgroundResource(R.drawable.obstacle_face_up);
            }
            else if (obsDir == 1){
                obs.setBackgroundResource(R.drawable.obstacle_face_right);
            }
            else if (obsDir == 2){
                obs.setBackgroundResource(R.drawable.obstacle_face_down);
            }
            else if (obsDir == 3){
                obs.setBackgroundResource(R.drawable.obstacle_face_left);
            }

            obstacleDirectionList.set(i, obsDir);

        }


    }

    //Function to update target ID on obstacle
    public void updateTarget(int tvid, String newString){

        TextView tv = findViewById(tvid);
        if (newString.equals("41")) {
            tv.setText("?");
            tv.setTextSize(14);
        } else {
            tv.setText(newString);
            tv.setTextSize(14);
        }

//        if(newString.length()==1){
//            tv.setText(newString);
//            tv.setTextSize(14);
//        } else if (newString.length()>1){
//            if (newString.equals("Up Arrow")) {
//                tv.setText("->");
//                tv.setRotation(-90);
//                tv.setTextSize(14);
//                rotateObstacle(tvid);
//            } else if (newString.equals("Down Arrow")){
//                tv.setText("->");
//                tv.setRotation(90);
//                tv.setTextSize(14);
//                rotateObstacle(tvid);
//                rotateObstacle(tvid);
//                rotateObstacle(tvid);
//            } else if (newString.equals("Left Arrow")){
//                tv.setText("<-");
//                tv.setTextSize(14);
//            } else if (newString.equals("Right Arrow")){
//                tv.setText("->");
//                tv.setTextSize(14);
//            } else if (newString.equals("Stop")){
//                tv.setText("O");
//                tv.setTextSize(14);
//            }
//        }


    }

    public void addObstacle(TextView view){
        view.setBackgroundResource(R.drawable.obstacle_face_up);
        obstacleCount++;
        obstacleList.add(view.getId());
        obstacleDirectionList.add(0);
        view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        view.setText("" + obstacleCount);
        view.setTextColor(getResources().getColor(R.color.white));
        view.setTextSize(7);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                removeObstacle(view.getId());
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateObstacle(view.getId());
            }
        });
    }

    public void removeObstacle(int tvid){
        TextView tv = findViewById(tvid);
        tv.setTextSize(14);
        tv.setBackgroundResource(R.drawable.light_gray_cell);
        tv.setText("");
        for(int i =0; i < obstacleList.size();i++){
            if (obstacleList.get(i) == tvid){
                obstacleList.remove(i);
                obstacleDirectionList.remove(i);
                obstacleCount--;
            }
        }
    }

    //Function to check if grid is empty
    public boolean checkEmpty(TextView tv){

        int tvID = tv.getId();

        //Get tvID that has robot
        int tl_id = (robotPosX*100+robotPosY);
        int tr_id = ((robotPosX+1)*100+robotPosY);
        int bl_id = (robotPosX*100+robotPosY-1);
        int br_id = ((robotPosX+1)*100+robotPosY-1);

        //Check if grid has robot
        if (tvID == tl_id && robotPlaced == true){return false;}
        else if (tvID == tr_id && robotPlaced == true){return false;}
        else if (tvID == bl_id && robotPlaced == true){return false;}
        else if (tvID == br_id && robotPlaced == true){return false;}
        else {
            //Check if grid has obstacle
            for(int i = 0; i < obstacleList.size(); i++) {
                if (obstacleList.get(i) == tvID) {
                    return false;
                }
            }
        }
        return true;
    }


    //Function to create grid map
    public void createGrid(){
        TableLayout stk = findViewById(R.id.table_main);
        for (int i = 20; i >= 0; i--){
            TableRow tbrow = new TableRow(this);
            for (int j = 0; j <= 20; j++) {
                TextView tv1 = new TextView(this);
                if (i == 0 && j == 0){
                    tv1.setBackgroundResource(R.drawable.white_cell);
                    tbrow.addView(tv1);
                }
                else if (i == 0){
                    tv1.setText(""+(j-1));
                    tv1.setTextSize(14);
                    tv1.setBackgroundResource(R.drawable.white_cell);
                    tv1.setGravity(Gravity.CENTER);
                    tbrow.addView(tv1);
                }
                else if (j == 0){
                    tv1.setText(""+(i-1));
                    tv1.setTextSize(14);
                    tv1.setBackgroundResource(R.drawable.white_cell);
                    tv1.setGravity(Gravity.CENTER);
                    tbrow.addView(tv1);
                }
                else {
                    tv1.setId((j-1) * 100 + i - 1);
                    tv1.setGravity(Gravity.CENTER);
                    tv1.setBackgroundResource(R.drawable.light_gray_cell);
                    int[] coor = new int[2];
                    coor[0] = j-1;
                    coor[1] = i-1;
                    tv1.setHeight(27);
                    tv1.setWidth(27);
                    tv1.setTag(coor);
                    tv1.setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View view, DragEvent event) {

                            if (checkEmpty(tv1)) {
                                int dragEvent = event.getAction();

                                //Tell us which view has been dragged
                                final View viewObject = (View) event.getLocalState();

                                switch (dragEvent) {

                                    case DragEvent.ACTION_DRAG_ENTERED:

                                        if (viewObject.getId() == R.id.txt1) {
                                            tv1.setBackgroundResource(R.drawable.light_green_cell);
                                        } else if (viewObject.getId() == R.id.txt2) {
                                            tv1.setBackgroundResource(R.drawable.light_red_cell);
                                        }
                                        break;

                                    case DragEvent.ACTION_DRAG_EXITED:
                                        tv1.setBackgroundResource(R.drawable.light_gray_cell);
                                        break;

                                    case DragEvent.ACTION_DROP:

                                        if (viewObject.getId() == R.id.txt1 && robotPlaced == false) {
                                            int[] robotPos = (int[]) view.getTag();
                                            robotPlaced = true;
                                            setCar(robotPos[0], robotPos[1], robotDirection);
                                            TextView txt1 = findViewById(R.id.txt1);
                                            txt1.setLongClickable(false);
                                            printCoor(tv1);
                                        } else if (viewObject.getId() == R.id.txt2) {
                                            addObstacle(tv1);
                                        }
                                        break;
                                }
                            }
                            return true;
                        }
                    });
                    tbrow.addView(tv1);
                }
            }
            stk.addView(tbrow);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver5);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver5);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        try{
            IntentFilter filter2 = new IntentFilter("ConnectionStatus");
            LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver5, filter2);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        showLog("Entering onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putString(TAG, "onSaveInstanceState");
        showLog("Exiting onSaveInstanceState");
    }
}
