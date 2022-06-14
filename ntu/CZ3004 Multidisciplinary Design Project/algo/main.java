import entities.Cell;
import entities.Map;
import entities.Robot;
import enums.ArenaDetails;
import enums.CellType;
import enums.Constant;
import enums.Direction;
import ui.ControlPanel;
import ui.MapLayout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import algo.AstarTesting;
import algo.AStarTestRun;

import static enums.Constant.MAX_X_VAL;
import static enums.Constant.MAX_Y_VAL;

public class main extends JFrame {
    private MapLayout mapLayout;
    private ControlPanel controlPanel;
    private Map map;
    private Robot robot;

    public static void main(String[] args) throws InterruptedException {
        main gui = new main();
        gui.setVisible(true);
    }

    public main() throws InterruptedException {
        map = new Map();
        robot = new Robot(1,1, Direction.NORTH);
        initGUI();


    }

    private void initGUI(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 650);
        setLayout(new FlowLayout());
        setTitle("MDP simulator");
        mapLayout = new MapLayout(map,robot);
        add(mapLayout);
        controlPanel = new ControlPanel(map,robot,mapLayout);
        add(controlPanel);
        pack();
        setVisible(true);
        mapLayout.updateCellColors();
    }

    private void simulateMovement(ArrayList<Integer> xValArray, ArrayList<Integer> yValArray, ArrayList<Direction> directionArray) throws InterruptedException {
        for(int i= 0; i<xValArray.size();i++){
            //Direction direction=Direction.RIGHT;
            TimeUnit.MILLISECONDS.sleep(500);
            map.setVisited(robot.getyPos(),robot.getxPos(),robot.getDirection());
            robot.setyPos(yValArray.get(i));
            robot.setxPos(xValArray.get(i));
            robot.setDirection(directionArray.get(i));
            mapLayout.updateCellColors();
        }
    }




}
