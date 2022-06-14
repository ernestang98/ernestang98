package com.mdp34;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mdp34.bluetooth.BluetoothService;
import com.mdp34.message.MessageFragment;
import com.mdp34.message.PageViewModel;

import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class ControllerFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "ControllerFragment";

    private PageViewModel pageViewModel;
    TextView tvRobot, tvObstacle;
    Button tvUp,tvDown,tvLeft,tvRight;
    SharedPreferences sharedPreferences;

    Button btnUp, btnLeft, btnRight, btnDown, btnReset,btnSend;

    public int robotPosX, robotPosY, robotDirection;

    public ArrayList<Integer> obstacleList;
    public ArrayList<Integer> directionList;

    public static ControllerFragment newInstance(int index){
        ControllerFragment fragment = new ControllerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // inflate
        View root = inflater.inflate(R.layout.activity_controller, container, false);
        sharedPreferences = getActivity().getSharedPreferences("Shared Preferences", Context.MODE_PRIVATE);

        tvRobot = (TextView) root.findViewById(R.id.txt1);
        tvObstacle = (TextView) root.findViewById(R.id.txt2);

        btnUp = (Button) root.findViewById(R.id.btnUp);
        btnDown = (Button) root.findViewById(R.id.btnDown);
        btnLeft = (Button) root.findViewById(R.id.btnLeft);
        btnRight = (Button) root.findViewById(R.id.btnRight);
        btnReset = (Button) root.findViewById(R.id.btnReset);
        btnSend = (Button) root.findViewById(R.id.btnSend);
        if (MainActivity.robotPlaced == false){
            tvRobot.setOnLongClickListener(longClickListener);
        }
        else {
            tvRobot.setLongClickable(false);
        }
        tvObstacle.setOnLongClickListener(longClickListener);


        btnUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.robotPlaced == true){
                    sendMessage("AND|CAR|f010");
                    moveCarForward();
                }
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.robotPlaced == true){
                    sendMessage("AND|CAR|b010");
                    moveCarBackward();
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.robotPlaced == true){
                    sendMessage("AND|CAR|o090");
                    turnCar(1);
                }
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.robotPlaced == true){
                    sendMessage("AND|CAR|p090");
                    turnCar(-1);
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetMap();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("robotStatus", "Not Available");
                editor.commit();
                MainActivity.refreshRobotStatus();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                obstacleList = MainActivity.obstacleList;
                directionList = MainActivity.obstacleDirectionList;
                int length = MainActivity.obstacleList.size();
                String message ="";

                for(int i=0;i <obstacleList.size();i++){
                    int x = obstacleList.get(i)/100;
                    int y = obstacleList.get(i) % 100;
                    String tempX = "";
                    String tempY = "";
                    String tempDir ="";
                    if (x<10){
                        tempX = "0"+ String.valueOf(x);
                    } else {
                        tempX = String.valueOf(x);
                    }
                    if (y<10) {
                        tempY = "0"+ String.valueOf(y);
                    } else {
                        tempY = String.valueOf(y);
                    }

                    if (directionList.get(i)==0){
                        tempDir = "N";
                    } else if (directionList.get(i) == 2) {
                        tempDir ="S";
                    } else if (directionList.get(i) == 1) {
                        tempDir ="E";
                    } else if (directionList.get(i) == 3) {
                        tempDir ="W";
                    }

                    if (i == obstacleList.size()-1){
                        message += tempX + tempY + tempDir;
                    } else {
                        message += tempX + tempY + tempDir + ',';
                    }
                }
                sendMessage("AND|ALG|" + message);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("robotStatus", "Ready to start");
                editor.commit();
                MainActivity.refreshRobotStatus();
            }
        });

        return root;
    }

    //Function for longClick
    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("","");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myShadowBuilder, v, 0);
            return true;
        }
    };

    //Function to check for Collision
    public boolean checkCollision(int tvID) {

        obstacleList = MainActivity.obstacleList;

        int len = obstacleList.size();
        int i = 0;

        while (i < len) {
            if (tvID == obstacleList.get(i)) {
                return true;
            }
            i++;

        }
        return false;
    }

    //Function to reset gridmap to default
    public void resetMap(){
        if (MainActivity.robotPlaced == true){
            removeCar();
            MainActivity.robotPlaced = false;
        }
        if (MainActivity.obstacleList.size() != 0){
            removeObstacle();
            if (MainActivity.obstacleOrderReceived!=null && MainActivity.obstacleOrderReceived.size()!=0 ) {
                MainActivity.obstacleOrderReceived.removeAll(MainActivity.obstacleOrderReceived);
                MainActivity.targetCount = 0;
            }
            MainActivity.obstacleCount = 0;
        }
        if (MainActivity.explored == true) {
            removedExploredPath();
            MainActivity.explored = false;
        }
    }

    //Function to remove explored path (those in dark grey cell) from gridmap
    public void removedExploredPath(){

        int i = MainActivity.exploredPathList.size() - 1;
        TextView exploredPath;

        while (i >= 0){
            exploredPath = (TextView) ((MainActivity) getActivity()).findViewById(MainActivity.exploredPathList.get(i));
            exploredPath.setBackgroundResource(R.drawable.light_gray_cell);
            MainActivity.exploredPathList.remove(i);
            i--;
        }
    }

    //Function to remove Obstacles from gridmap
    public void removeObstacle(){

        obstacleList = MainActivity.obstacleList;

        int i = obstacleList.size() - 1;
        TextView obsTV;

        while (i >= 0){
            obsTV = (TextView) ((MainActivity) getActivity()).findViewById(obstacleList.get(i));
            obsTV.setTextSize(14);
            obsTV.setBackgroundResource(R.drawable.light_gray_cell);
            obsTV.setRotation(0);
            obsTV.setText("");
            MainActivity.obstacleList.remove(i);
            MainActivity.obstacleDirectionList.remove(i);
            i--;
        }
    }

    //Function to remove Robot Car from gridmap
    public void removeCar(){

        robotPosX = MainActivity.robotPosX;
        robotPosY = MainActivity.robotPosY;

        int tl_id = (robotPosX * 100 + robotPosY);
        int tr_id = ((robotPosX + 1) * 100 + robotPosY);
        int bl_id = (robotPosX * 100 + robotPosY - 1);
        int br_id = ((robotPosX + 1) * 100 + robotPosY - 1);

        TextView tv_tl = (TextView) ((MainActivity) getActivity()).findViewById(tl_id);
        TextView tv_tr = (TextView) ((MainActivity) getActivity()).findViewById(tr_id);
        TextView tv_bl = (TextView) ((MainActivity) getActivity()).findViewById(bl_id);
        TextView tv_br = (TextView) ((MainActivity) getActivity()).findViewById(br_id);

        tv_tl.setBackgroundResource(R.drawable.light_gray_cell);
        tv_tl.setRotation(0);
        tv_tr.setBackgroundResource(R.drawable.light_gray_cell);
        tv_tr.setRotation(0);
        tv_bl.setBackgroundResource(R.drawable.light_gray_cell);
        tv_bl.setRotation(0);
        tv_br.setBackgroundResource(R.drawable.light_gray_cell);
        tv_br.setRotation(0);

        TextView txt1 = getActivity().findViewById(R.id.txt1);
        txt1.setLongClickable(true);
    }

    //Function to update Robot Car position forward
    public void moveCarForward(){

        if (MainActivity.robotPlaced == true) {

            robotPosX = MainActivity.robotPosX;
            robotPosY = MainActivity.robotPosY;
            robotDirection = MainActivity.robotDirection;

            //When robot facing north, check if it is at the top row, check if north got collision
            if (robotDirection == 0 && robotPosY != 19 && !checkCollision(robotPosX*100+robotPosY+1) && !checkCollision((robotPosX+1)*100+robotPosY+1)) {
                removeCar();
                ((MainActivity) getActivity()).setCar(robotPosX, robotPosY + 1, robotDirection);
            }

            //When robot facing east, check if it is at the rightmost column, check if east got collision
            else if (robotDirection == 1 && robotPosX != 19 && !checkCollision((robotPosX+2)*100+robotPosY) && !checkCollision((robotPosX+2)*100+robotPosY-1)) {
                removeCar();
                ((MainActivity) getActivity()).setCar(robotPosX + 1, robotPosY, robotDirection);
            }

            //When robot facing south, check if it is at the bottom row, check if south got collision
            else if (robotDirection == 2 && robotPosY != 0 && !checkCollision(robotPosX*100+robotPosY-2) && !checkCollision((robotPosX+1)*100+robotPosY-2)) {
                removeCar();
                ((MainActivity) getActivity()).setCar(robotPosX, robotPosY - 1, robotDirection);
            }

            //When robot facing west, check if it is at the leftmost column, check if west got collision
            else if (robotDirection == 3 && robotPosX != 0 && !checkCollision((robotPosX-1)*100+robotPosY) && !checkCollision((robotPosX-1)*100+robotPosY-1)) {
                removeCar();
                ((MainActivity) getActivity()).setCar(robotPosX - 1, robotPosY, robotDirection);
            }
        }
    }

    //Function to update Robot Car position backward
    public void moveCarBackward(){

        if (MainActivity.robotPlaced == true) {

            robotPosX = MainActivity.robotPosX;
            robotPosY = MainActivity.robotPosY;
            robotDirection = MainActivity.robotDirection;

            //When robot facing north, check if it is at the bottom row, check if south got collision
            if (robotDirection == 0 && robotPosY != 0 && !checkCollision(robotPosX*100+robotPosY-2) && !checkCollision((robotPosX+1)*100+robotPosY-2)) {
                removeCar();
                ((MainActivity) getActivity()).setCar(robotPosX, robotPosY - 1, robotDirection);
            }

            //When robot facing east, check if it is at the leftmost column, check if west got collision
            else if (robotDirection == 1 && robotPosX != 0 && !checkCollision((robotPosX-1)*100+robotPosY) && !checkCollision((robotPosX-1)*100+robotPosY-1)) {
                removeCar();
                ((MainActivity) getActivity()).setCar(robotPosX - 1, robotPosY, robotDirection);
            }

            //When robot facing south, check if it is at the top row, check if north got collision
            else if (robotDirection == 2 && robotPosY != 19 && !checkCollision(robotPosX*100+robotPosY+1) && !checkCollision((robotPosX+1)*100+robotPosY+1)) {
                removeCar();
                ((MainActivity) getActivity()).setCar(robotPosX, robotPosY + 1, robotDirection);
            }

            //When robot facing west, check if it is at the rightmost column, check if east got collision
            else if (robotDirection == 3 && robotPosX != 19 && !checkCollision((robotPosX+2)*100+robotPosY) && !checkCollision((robotPosX+2)*100+robotPosY-1)) {
                removeCar();
                ((MainActivity) getActivity()).setCar(robotPosX + 1, robotPosY, robotDirection);
            }
        }
    }

    //Function to rotate Robot Car
    public void turnCar(int direction){

        robotPosX = MainActivity.robotPosX;
        robotPosY = MainActivity.robotPosY;
        robotDirection = MainActivity.robotDirection + direction;

        ((MainActivity) getActivity()).setCar(robotPosX, robotPosY, robotDirection);

    }

    private void sendMessage(String message){
        String sentText = message;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("message", sharedPreferences.getString("message", "") + '\n' + "ME:" + sentText);
        editor.commit();
        MessageFragment.getMessageReceivedTextView().setText(sharedPreferences.getString("message", ""));
        if (BluetoothService.connStatusFlag == true) {
            byte[] bytes = sentText.getBytes(Charset.defaultCharset());
            BluetoothService.write(bytes);
        }
    }

}