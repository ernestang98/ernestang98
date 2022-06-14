package algo;

import entities.Cell;
import entities.Map;
import entities.Robot;
import enums.Constant;
import enums.Direction;
import enums.MovementType;
import moves.*;
import ui.ControlPanel;
import ui.MapLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

import static enums.Constant.MAX_X_VAL;
import static enums.Constant.MAX_Y_VAL;
import static enums.Constant.MIN_XY_VAL;


public class AstarTesting {
    public static int STRAIGHTCOST = 1;
    public static int TURNCOST = 3;
    public static int SPOTCOST = 300;
    private Map map;
    private Robot robot;
    private Cell[][] cellsArray;
    private Cell targetCell;
    private Cell curr;
    private int xTarget;
    private int yTarget;
    private Direction dirTarget;
    //Initialize the closed list
    private boolean[][] closedList = new boolean[MAX_Y_VAL][MAX_X_VAL];
    //Initialize the open list
    private PriorityQueue<Cell> openList = new PriorityQueue<>((Cell c1, Cell c2) ->
            c1.getTotalCost() < c2.getTotalCost() ? -1 : c1.getTotalCost() > c2.getTotalCost() ? 1 : 0
    );


    public AstarTesting(Map map, Robot robot, int targetY, int targetX, Direction direction) {
        this.map = map;
        this.robot = robot;
        this.xTarget = targetX;
        this.yTarget = targetY;
        this.dirTarget = direction;
        this.targetCell = new Cell(targetY, targetX);
        targetCell.setHeadDirection(direction);

    }


    public boolean aStarSearch() {
        Cell temp;
        int xCurr;
        int yCurr;
        //Set this boolean value as false as initially the destination is not reached
        boolean foundDest = false;
        cellsArray = map.getMap();


        // If the source is out of range
        if (!isValid(robot.getyPos(), robot.getxPos())) {
            System.out.println("Source is invalid\n");
            return false;
        }

        // If the destination is out of range
        if (!isValid(yTarget, xTarget)) {
            System.out.println("Destination is invalid\n");
            return false;
        }

        if (isDestination(robot.getyPos(), robot.getxPos(), robot.getDirection(), yTarget, xTarget, dirTarget)) {
            System.out.println("We are already at the destination\n");
            return false;
        }


        //Initialise the closed list to false as no cell has been included yet
        for (int z = 0; z < closedList.length; z++) {
            for (int j = 0; j < closedList[z].length; j++) {
                closedList[z][j] = false;
            }
        }

        // Initialising the parameters of the starting node
        cellsArray[robot.getyPos()][robot.getxPos()].setHeadDirection(robot.getDirection());
        cellsArray[robot.getyPos()][robot.getxPos()].setHeuristicCost(0);
        cellsArray[robot.getyPos()][robot.getxPos()].setTotalCost(0);
        cellsArray[robot.getyPos()][robot.getxPos()].setCost(0);

        openList.add(cellsArray[robot.getyPos()][robot.getxPos()]);


        //Check that the open list is not empty. If empty, exit the Astar algorithm
        while (!openList.isEmpty()) {
            //find the node with the least f cost in the open list.
            //Pop that node with the least f off the open list.
            curr = openList.poll();

            yCurr = curr.getRow();
            xCurr = curr.getCol();

            //push the current node to the closed list so that will not have to visit it again
            closedList[yCurr][xCurr] = true;

            //Check and make sure that the successor is valid. Not an obstacle or out of the arena
            StraightMove straightMove = new StraightMove();  //Straight moves (forward and backward)
            TurningMove turningMove = new TurningMove();  //Turning moves (left and right)
            ReverseTurningMove reverseTurningMove = new ReverseTurningMove();  //Reverse turning moves (left and right)
            SpotMove spotMove = new SpotMove();   //Spot moves (left and right)
            //1----------------Straight move FORWARD--------------------------------------------------------------------
            int[] firstSuccessorCoor = straightMove.makeMove(curr, MovementType.FORWARD);
            Direction firstSuccessorDirection = straightMove.getDirection(curr, MovementType.FORWARD);
            if (isValid(firstSuccessorCoor[0], firstSuccessorCoor[1])) {
                if (isDestination(firstSuccessorCoor[0], firstSuccessorCoor[1], firstSuccessorDirection,
                        yTarget, xTarget, dirTarget)) {
                    //Set the parent of the destination cell
                    temp = cellsArray[firstSuccessorCoor[0]][firstSuccessorCoor[1]];
                    temp.setParent(curr);
                    temp.setHaveParent(true);
                    temp.setHeadDirection(firstSuccessorDirection);
                    System.out.println("The destination cell is found\n");
                    return true;
                }
                //If the successor is already on the closed list, or it is an obstacle, ignore it
                //else do the following
                else if (!closedList[firstSuccessorCoor[0]][firstSuccessorCoor[1]] &&
                        !cellsArray[firstSuccessorCoor[0]][firstSuccessorCoor[1]].isObstacle()) {
                    temp = cellsArray[firstSuccessorCoor[0]][firstSuccessorCoor[1]];
                    updateCost(temp, curr.getCost() + STRAIGHTCOST, firstSuccessorDirection);
                }
                //}
            }
            //2----------------Straight move BACKWARDS--------------------------------------------------------------------
            int[] secondSuccessorCoor = straightMove.makeMove(curr, MovementType.BACKWARD);
            Direction secondSuccessorDirection = straightMove.getDirection(curr, MovementType.BACKWARD);
            if (isValid(secondSuccessorCoor[0], secondSuccessorCoor[1])) {
                if (isDestination(secondSuccessorCoor[0], secondSuccessorCoor[1], secondSuccessorDirection,
                        yTarget, xTarget, dirTarget)) {
                    temp = cellsArray[secondSuccessorCoor[0]][secondSuccessorCoor[1]];
                    temp.setParent(curr);
                    temp.setHaveParent(true);
                    temp.setHeadDirection(secondSuccessorDirection);
                    System.out.println("The destination cell is found\n");
                    return true;
                } else if (!closedList[secondSuccessorCoor[0]][secondSuccessorCoor[1]] &&
                        !cellsArray[secondSuccessorCoor[0]][secondSuccessorCoor[1]].isObstacle()) {
                    temp = cellsArray[secondSuccessorCoor[0]][secondSuccessorCoor[1]];
                    updateCost(temp, curr.getCost() + STRAIGHTCOST, secondSuccessorDirection);
                }
                //  }
            }
            //3----------------Turning move RIGHT--------------------------------------------------------------------
            //Improvement: need to check obstacle for right direction
            int[] thirdSuccessorCoor = turningMove.makeMove(curr, MovementType.RIGHT);
            Direction thirdSuccessorDirection = turningMove.getDirection(curr, MovementType.RIGHT);
            if (isValid(thirdSuccessorCoor[0], thirdSuccessorCoor[1])) {
                if (!checkForObst(MovementType.RIGHT, curr)) {
                    if (isDestination(thirdSuccessorCoor[0], thirdSuccessorCoor[1], thirdSuccessorDirection,
                            yTarget, xTarget, dirTarget)) {
                        temp = cellsArray[thirdSuccessorCoor[0]][thirdSuccessorCoor[1]];
                        temp.setParent(curr);
                        temp.setHaveParent(true);
                        temp.setHeadDirection(thirdSuccessorDirection);
                        System.out.println("The destination cell is found\n");
                        return true;
                    } else if (!closedList[thirdSuccessorCoor[0]][thirdSuccessorCoor[1]] &&
                            !cellsArray[thirdSuccessorCoor[0]][thirdSuccessorCoor[1]].isObstacle()) {
                        temp = cellsArray[thirdSuccessorCoor[0]][thirdSuccessorCoor[1]];
                        updateCost(temp, curr.getCost() + TURNCOST, thirdSuccessorDirection);
                    }
                }
            }
            //4----------------Turning move LEFT--------------------------------------------------------------------
            //Improvement: need to check obstacle for left direction
            int[] fourthSuccessorCoor = turningMove.makeMove(curr, MovementType.LEFT);
            Direction fourthSuccessorDirection = turningMove.getDirection(curr, MovementType.LEFT);
            if (isValid(fourthSuccessorCoor[0], fourthSuccessorCoor[1])) {
                if (!checkForObst(MovementType.LEFT, curr)) {
                    if (isDestination(fourthSuccessorCoor[0], fourthSuccessorCoor[1], fourthSuccessorDirection,
                            yTarget, xTarget, dirTarget)) {
                        temp = cellsArray[fourthSuccessorCoor[0]][fourthSuccessorCoor[1]];
                        temp.setParent(curr);
                        temp.setHaveParent(true);
                        temp.setHeadDirection(fourthSuccessorDirection);
                        System.out.println("The destination cell is found\n");
                        return true;
                    } else if (!closedList[fourthSuccessorCoor[0]][fourthSuccessorCoor[1]] &&
                            !cellsArray[fourthSuccessorCoor[0]][fourthSuccessorCoor[1]].isObstacle()) {
                        temp = cellsArray[fourthSuccessorCoor[0]][fourthSuccessorCoor[1]];
                        updateCost(temp, curr.getCost() + TURNCOST, fourthSuccessorDirection);
                    }
                }
            }
            //5----------------Reverse turning move RIGHT--------------------------------------------------------------------
            //Improvement: need to check obstacle for right direction
            int[] fifthSuccessorCoor = reverseTurningMove.makeMove(curr, MovementType.REVERSERIGHT);
            Direction fifthSuccessorDirection = reverseTurningMove.getDirection(curr, MovementType.REVERSERIGHT);
            if (isValid(fifthSuccessorCoor[0], fifthSuccessorCoor[1])) {
                if (!checkForObst(MovementType.REVERSERIGHT, curr)) {
                    if (isDestination(fifthSuccessorCoor[0], fifthSuccessorCoor[1], fifthSuccessorDirection,
                            yTarget, xTarget, dirTarget)) {
                        temp = cellsArray[fifthSuccessorCoor[0]][fifthSuccessorCoor[1]];
                        temp.setParent(curr);
                        temp.setHaveParent(true);
                        temp.setHeadDirection(fifthSuccessorDirection);
                        System.out.println("The destination cell is found\n");
                        return true;
                    } else if (!closedList[fifthSuccessorCoor[0]][fifthSuccessorCoor[1]] &&
                            !cellsArray[fifthSuccessorCoor[0]][fifthSuccessorCoor[1]].isObstacle()) {
                        temp = cellsArray[fifthSuccessorCoor[0]][fifthSuccessorCoor[1]];
                        updateCost(temp, curr.getCost() + TURNCOST, fifthSuccessorDirection);
                    }
                }
            }
            //6----------------Reverse turning move LEFT--------------------------------------------------------------------
            //Improvement: need to check obstacle for right direction
            int[] sixthSuccessorCoor = reverseTurningMove.makeMove(curr, MovementType.REVERSELEFT);
            Direction sixthSuccessorDirection = reverseTurningMove.getDirection(curr, MovementType.REVERSELEFT);
            if (isValid(sixthSuccessorCoor[0], sixthSuccessorCoor[1])) {
                if (!checkForObst(MovementType.REVERSELEFT, curr)) {
                    if (isDestination(sixthSuccessorCoor[0], sixthSuccessorCoor[1], sixthSuccessorDirection,
                            yTarget, xTarget, dirTarget)) {
                        temp = cellsArray[sixthSuccessorCoor[0]][sixthSuccessorCoor[1]];
                        temp.setParent(curr);
                        temp.setHaveParent(true);
                        temp.setHeadDirection(sixthSuccessorDirection);
                        System.out.println("The destination cell is found\n");
                        return true;
                    } else if (!closedList[sixthSuccessorCoor[0]][sixthSuccessorCoor[1]] &&
                            !cellsArray[sixthSuccessorCoor[0]][sixthSuccessorCoor[1]].isObstacle()) {
                        temp = cellsArray[sixthSuccessorCoor[0]][sixthSuccessorCoor[1]];
                        updateCost(temp, curr.getCost() + TURNCOST, sixthSuccessorDirection);
                    }
                }
            }
            //7----------------Spot move RIGHT--------------------------------------------------------------------
            //Improvement: need to check obstacle for right direction
            int[] seventhSuccessorCoor = spotMove.makeMove(curr, MovementType.RIGHT);
            Direction seventhSuccessorDirection = spotMove.getDirection(curr, MovementType.RIGHT);
            if (isValid(seventhSuccessorCoor[0], seventhSuccessorCoor[1])) {
                //if (!checkForObst(MovementType.REVERSERIGHT, curr)) {
                if (isDestination(seventhSuccessorCoor[0], seventhSuccessorCoor[1], seventhSuccessorDirection,
                        yTarget, xTarget, dirTarget)) {
                    temp = cellsArray[seventhSuccessorCoor[0]][seventhSuccessorCoor[1]];
                    temp.setParent(curr);
                    temp.setHaveParent(true);
                    temp.setHeadDirection(seventhSuccessorDirection);
                    System.out.println("The destination cell is found\n");
                    return true;
                } else if (!closedList[seventhSuccessorCoor[0]][seventhSuccessorCoor[1]] &&
                        !cellsArray[seventhSuccessorCoor[0]][seventhSuccessorCoor[1]].isObstacle()) {
                    temp = cellsArray[seventhSuccessorCoor[0]][seventhSuccessorCoor[1]];
                    updateCost(temp, curr.getCost() + SPOTCOST, seventhSuccessorDirection);
                }
                //}
            }
            //8----------------Spot move LEFT--------------------------------------------------------------------
            //Improvement: need to check obstacle for right direction
            int[] eighthSuccessorCoor = spotMove.makeMove(curr, MovementType.LEFT);
            Direction eighthSuccessorDirection = spotMove.getDirection(curr, MovementType.LEFT);
            if (isValid(eighthSuccessorCoor[0], eighthSuccessorCoor[1])) {
                //if (!checkForObst(MovementType.REVERSERIGHT, curr)) {
                if (isDestination(eighthSuccessorCoor[0], eighthSuccessorCoor[1], eighthSuccessorDirection,
                        yTarget, xTarget, dirTarget)) {
                    temp = cellsArray[eighthSuccessorCoor[0]][eighthSuccessorCoor[1]];
                    temp.setParent(curr);
                    temp.setHaveParent(true);
                    temp.setHeadDirection(eighthSuccessorDirection);
                    System.out.println("The destination cell is found\n");
                    return true;
                } else if (!closedList[eighthSuccessorCoor[0]][eighthSuccessorCoor[1]] &&
                        !cellsArray[eighthSuccessorCoor[0]][eighthSuccessorCoor[1]].isObstacle()) {
                    temp = cellsArray[eighthSuccessorCoor[0]][eighthSuccessorCoor[1]];
                    updateCost(temp, curr.getCost() + SPOTCOST, eighthSuccessorDirection);
                }
                //}
            }

        }
        System.out.println("Failed to find the Destination Cell\n");

        return false;
    }



    public double calculateHeuristicCosts(int cellRow, int cellCol, int targetRow, int targetCol) {
        double dx = (double) Math.abs(targetCol - cellCol);
        double dy = (double) Math.abs(targetRow - cellRow);
        return Math.sqrt((dx * dx) + (dy * dy));
    }


    public void updateCost(Cell nextCell, double newGcost, Direction nextDirection) {
        //If the successor is null, or it is already in the closed list, return and skip this successor
        //if (nextCell == null || closedList[nextCell.getRow()][nextCell.getCol()]) return;
        //Add the g cost to the h cost to get the total cost
        double newHcost = calculateHeuristicCosts(nextCell.getRow(), nextCell.getCol(), yTarget, xTarget);
        nextCell.setHeuristicCost(newHcost);
        double newTotalCost = nextCell.getHeuristicCost() + newGcost;

        if (!openList.contains(nextCell) && !closedList[nextCell.getRow()][nextCell.getCol()]) {
            nextCell.setParent(curr);
            nextCell.setHaveParent(true);
            nextCell.setTotalCost(newTotalCost);
            nextCell.setCost(newGcost);
            nextCell.setHeadDirection(nextDirection);
            openList.add(nextCell);
            //Update the details of successor cell
        } else {
            if (newGcost < nextCell.getCost()) {
                nextCell.setParent(curr);
                nextCell.setHaveParent(true);
                nextCell.setTotalCost(newTotalCost);
                nextCell.setCost(newGcost);
                nextCell.setHeadDirection(nextDirection);
            }
        }
    }
    public ArrayList<String> tracePath(){
        return _tracePath(999);
    }

    public ArrayList<String> tracePath(int limit){
        return _tracePath(limit);
    }

    private ArrayList<String> _tracePath(int limit) {
        ArrayList<String> path = new ArrayList<String>();
        String command;
        int count = 0; //Number of instructions
        int row = yTarget;
        int col = xTarget;


        while (cellsArray[row][col].haveParent() && !(cellsArray[row][col].getParent().getRow() == row && cellsArray[row][col].getParent().getCol() == col)) {
            if (count >= limit) break;
            command = pathing(row, col);
            path.add(0, command);
            int temp_row = cellsArray[row][col].getParent().getRow();
            int temp_col = cellsArray[row][col].getParent().getCol();
            row = temp_row;
            col = temp_col;
            count++;
        }
        return path;
    }

    public String pathing(int currentY, int currentX) {
        this.curr = cellsArray[currentY][currentX];
        Cell parent = curr.getParent();
        if (parent == null) return "";
        int yParent = parent.getRow();
        int xParent = parent.getCol();
        int yCurr = curr.getRow();
        int xCurr = curr.getCol();
        String path = "";
        switch (parent.getHeadDirection()) {
            case NORTH -> {
                if (xCurr == xParent) {
                    path = yCurr > yParent ? "W" : "S";
                } else {
                    if (xCurr > xParent && yCurr > yParent) {
                        path = "E";
                    } else if (xCurr > xParent && yCurr < yParent) {
                        path = "C";
                    } else if (xCurr < xParent && yCurr > yParent) {
                        path = "Q";
                    } else if (xCurr < xParent && yCurr < yParent) {
                        path = "Z";
                    } else if (xCurr > xParent && yCurr == yParent) {
                        path = "D";
                    } else if (xCurr < xParent && yCurr == yParent) {
                        path = "A";
                    }

                }
            }
            case EAST -> {
                if (yCurr == yParent) {
                    path = xCurr > xParent ? "W" : "S";
                } else {
                    if (xCurr > xParent && yCurr < yParent) {
                        path = "E";
                    } else if (xCurr < xParent && yCurr < yParent) {
                        path = "C";
                    } else if (xCurr > xParent && yCurr > yParent) {
                        path = "Q";
                    } else if (xCurr < xParent && yCurr > yParent) {
                        path = "Z";
                    } else if (xCurr == xParent && yCurr < yParent) {
                        path = "D";
                    } else if (xCurr == xParent && yCurr > yParent) {
                        path = "A";
                    }
                }
            }
            case SOUTH -> {
                if (xCurr == xParent) {
                    path = yCurr > yParent ? "S" : "W";
                } else {
                    if (xCurr < xParent && yCurr < yParent) {
                        path = "E";
                    } else if (xCurr < xParent && yCurr > yParent) {
                        path = "C";
                    } else if (xCurr > xParent && yCurr < yParent) {
                        path = "Q";
                    } else if (xCurr > xParent && yCurr > yParent) {
                        path = "Z";
                    } else if (xCurr < xParent && yCurr == yParent) {
                        path = "D";
                    } else if (xCurr > xParent && yCurr == yParent) {
                        path = "A";
                    }
                }
            }
            case WEST -> {
                if (yCurr == yParent) path = xCurr > xParent ? "S" : "W";
                else {
                    if (xCurr < xParent && yCurr > yParent) {
                        path = "E";
                    } else if (xCurr > xParent && yCurr > yParent) {
                        path = "C";
                    } else if (xCurr < xParent && yCurr < yParent) {
                        path = "Q";
                    } else if (xCurr > xParent && yCurr < yParent) {
                        path = "Z";
                    } else if (xCurr == xParent && yCurr > yParent) {
                        path = "D";
                    } else if (xCurr == xParent && yCurr < yParent) {
                        path = "A";
                    }
                }
            }
        }
        return path;
    }

    public boolean isDestination(int row, int col, Direction cellDirection, int yTarget, int xTarget, Direction dirTarget) {
        if (row == yTarget && col == xTarget && cellDirection == dirTarget)
            return (true);
        else
            return (false);
    }

    public boolean checkForObst(MovementType moveCommand, Cell currentCell) {

        int yPos = currentCell.getRow();
        int xPos = currentCell.getCol();
        //Check obstacles when Movement is Right--------------------------------------------------------------------
        if (moveCommand == MovementType.RIGHT) {
            if (currentCell.getHeadDirection() == Direction.NORTH) {
                //done
                for (int i = 0; i <= 3; i++) {
                    for (int j = -1; j < 3; j++) {
                        if (cellsArray[yPos + i][xPos + j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.SOUTH) {
                //done
                for (int i = 0; i <= 3; i++) {
                    for (int j = -1; j < 3; j++) {
                        if (cellsArray[yPos - i][xPos - j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.EAST) {
                //done
                for (int i = -1; i < 3; i++) {
                    for (int j = 0; j <= 3; j++) {
                        if (cellsArray[yPos - i][xPos + j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.WEST) {
                //done
                for (int i = -1; i < 3; i++) {
                    for (int j = 0; j <= 3; j++) {
                        if (cellsArray[yPos + i][xPos - j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            }
        }
        //Check obstacles when Movement is Left--------------------------------------------------------------------
        if (moveCommand == MovementType.LEFT) {
            if (currentCell.getHeadDirection() == Direction.NORTH) {
                //done
                for (int i = 0; i <= 3; i++) {
                    for (int j = -1; j < 3; j++) {
                        if (cellsArray[yPos + i][xPos - j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.SOUTH) {
                //done
                for (int i = 0; i <= 3; i++) {
                    for (int j = -1; j < 3; j++) {
                        if (cellsArray[yPos - i][xPos + j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.EAST) {
                //done
                for (int i = -1; i < 3; i++) {
                    for (int j = 0; j <= 3; j++) {
                        if (cellsArray[yPos + i][xPos + j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.WEST) {
                //done
                for (int i = -1; i < 3; i++) {
                    for (int j = 0; j <= 3; j++) {
                        if (cellsArray[yPos - i][xPos - j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            }
        }
        //Check obstacles when Movement is Reverse Right--------------------------------------------------------------------
        if (moveCommand == MovementType.REVERSERIGHT) {
            if (currentCell.getHeadDirection() == Direction.NORTH) {
                //done
                for (int i = 1; i <= 4; i++) {
                    for (int j = -1; j <= 2; j++) {
                        if (cellsArray[yPos - i][xPos + j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.SOUTH) {
                //done
                for (int i = 1; i <= 4; i++) {
                    for (int j = -1; j <= 2; j++) {
                        if (cellsArray[yPos + i][xPos - j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.EAST) {
                //done
                for (int i = -1; i <= 2; i++) {
                    for (int j = 1; j <= 4; j++) {
                        if (cellsArray[yPos - i][xPos - j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.WEST) {
                //done
                for (int i = -1; i <= 2; i++) {
                    for (int j = 1; j <= 4; j++) {
                        if (cellsArray[yPos + i][xPos + j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            }
        }
        //Check obstacles when Movement is Reverse Left--------------------------------------------------------------------
        if (moveCommand == MovementType.REVERSELEFT) {
            if (currentCell.getHeadDirection() == Direction.NORTH) {
                //done
                for (int i = 1; i <= 4; i++) {
                    for (int j = -1; j <= 2; j++) {
                        if (cellsArray[yPos - i][xPos - j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.SOUTH) {
                //done
                for (int i = 1; i <= 4; i++) {
                    for (int j = -1; j <= 2; j++) {
                        if (cellsArray[yPos + i][xPos + j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.EAST) {
                //done
                for (int i = -1; i <= 2; i++) {
                    for (int j = 1; j <= 4; j++) {
                        if (cellsArray[yPos + i][xPos - j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            } else if (currentCell.getHeadDirection() == Direction.WEST) {
                //done
                for (int i = -1; i <= 2; i++) {
                    for (int j = 1; j <= 4; j++) {
                        if (cellsArray[yPos - i][xPos + j].isObstacle() == true) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean isValid(int row, int col) {
        return (row - 1 >= Constant.MIN_XY_VAL) && (row + 1 < MAX_Y_VAL) && (col - 1 >= Constant.MIN_XY_VAL)
                && (col + 1 < MAX_X_VAL);
    }


}

