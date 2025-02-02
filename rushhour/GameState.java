/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package rushhour;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.HashSet;
import java.util.List;
// import java.util.Map;
// import java.util.Set;
import search.Action;
import search.State;

/**
*
* @author steven
*/
public class GameState implements search.State {
    
    boolean[][] occupiedPositions;
    List<Car> cars; // target car is always the first one
    int nrRows;
    int nrCols;
    
    public GameState(String fileName) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        nrRows = Integer.parseInt(in.readLine().split("\\s")[0]);
        nrCols = Integer.parseInt(in.readLine().split("\\s")[0]);
        String s = in.readLine();
        cars = new ArrayList<Car>();
        while (s != null) {
            cars.add(new Car(s));
            s = in.readLine();
        }
        initOccupied();
    }
    
    public GameState(int nrRows, int nrCols, List<Car> cars) {
        this.nrRows = nrRows;
        this.nrCols = nrCols;
        this.cars = cars;
        initOccupied();
    }
    
    public GameState(GameState gs) {
        nrRows = gs.nrRows;
        nrCols = gs.nrCols;
        occupiedPositions = new boolean[nrRows][nrCols];
        for (int i = 0; i < nrRows; i++) {
            for (int j = 0; j < nrCols; j++) {
                occupiedPositions[i][j] = gs.occupiedPositions[i][j];
            }
        }
        cars = new ArrayList<Car>();
        for (Car c : gs.cars) {
            cars.add(new Car(c));
        }
    }
    
    public void printState() {
        int[][] state = new int[nrRows][nrCols];
        
        for (int i = 0; i < cars.size(); i++) {
            List<Position> l = cars.get(i).getOccupyingPositions();
            for (Position pos : l) {
                state[pos.getRow()][pos.getCol()] = i + 1;
            }
        }
        
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                if (state[i][j] == 0) {
                    System.out.print(".");
                } else {
                    System.out.print(state[i][j] - 1);
                }
            }
            System.out.println();
        }
    }
    
    private void initOccupied() {
        occupiedPositions = new boolean[nrRows][nrCols];
        for (Car c : cars) {
            List<Position> l = c.getOccupyingPositions();
            for (Position pos : l) {
                occupiedPositions[pos.getRow()][pos.getCol()] = true;
            }
        }
    }
    
    public boolean isGoal() {
        Car goalCar = cars.get(0);
        return goalCar.getCol() + goalCar.getLength() == nrCols;
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof GameState)) {
            return false;
        } else {
            GameState gs = (GameState) o;
            return nrRows == gs.nrRows && nrCols == gs.nrCols && cars.equals(gs.cars); // note that we don't need to
            // check equality of
            // occupiedPositions since that
            // follows from the equality of
            // cars
        }
    }
    
    public int hashCode() {
        return cars.hashCode();
    }
    
    public void printToFile(String fn) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(fn));
            out.println(nrRows);
            out.println(nrCols);
            for (Car c : cars) {
                out.println(c.getRow() + " " + c.getCol() + " " + c.getLength() + " " + (c.isVertical() ? "V" : "H"));
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public List<Action> getLegalActions() {
        // List to store all valid moves in
        ArrayList<Action> actions = new ArrayList<Action>();
        
        // Iterate through all of the cars
        for (Car car : cars) {
            
            // Create moves of distance 1 in all directions for the car
            Action left = new MoveLeft(car, 1);
            Action right = new MoveRight(car, 1);
            Action up = new MoveUp(car, 1);
            Action down = new MoveDown(car, 1);
            
            // Check if the move is still legal
            while (isLegal(left)) {
                // Add it then make the move longer
                actions.add(new MoveLeft(left));
                left.setDistance(left.getDistance() + 1);
            }
            
            // Do this again for all of the directions
            while (isLegal(right)) {
                actions.add(new MoveRight(right));
                right.setDistance(right.getDistance() + 1);
            }
            while (isLegal(up)) {
                actions.add(new MoveUp(up));
                up.setDistance(up.getDistance() + 1);
            }
            while (isLegal(down)) {
                actions.add(new MoveDown(down));
                down.setDistance(down.getDistance() + 1);
            }
            
        }
        return actions;
    }
    

    public boolean isLegal(Action action){
        // Get the car and distance it is moving
        Car car = action.getCar();
        int distance = action.getDistance();
        // Check that it is pointing in the right direction to move
        if (action instanceof MoveLeft || action instanceof MoveRight) {
            if (car.isVertical()) {
                return false;
            }
        } else {
            if (!car.isVertical()) {
                return false;
            }
        }
        // Check that it isn't going to pass the edge of the board for each direction
        // I know chaining if else isn't good but  getting `instanceof` to work with them looked much worse...
        if (action instanceof MoveLeft){
            if (car.getCol() - distance < 0) {
                return false;
            }
        } else if (action instanceof MoveRight) {
            if ((car.getCol() + car.getLength() + distance - 1) > nrCols - 1) {
                return false;
            }
        } else if (action instanceof MoveUp) {
            if (car.getRow() - distance < 0) {
                return false;
            }
        } else if (action instanceof MoveDown) {
            if ((car.getRow() + car.getLength() + distance - 1) > nrRows - 1) {
                return false;
            }
        } else {
            return false;
        }
        // Iterate over all of the cars to see if they are in the way
        for (Car otherCar : cars) {
            // A car cannot collide with its self
            if (otherCar != car) {
                // Get all of the positions that the other car is occupying
                List<Position> positions = otherCar.getOccupyingPositions();
                for (Position position : positions) {
                    // Iterate up to the full distance to check that the car doesn't slide through a car to get to its final position.
                    // It is assumed that the car is in a legal position to start with.
                    for (int i = 1; i <= distance; i++) {
                        // Check if the cars are overlapping
                        if (action instanceof MoveLeft){
                            if ((position.getRow() == car.getRow())
                             && (position.getCol() == (car.getCol() - i))) {
                                return false;
                            }
                        } else if (action instanceof MoveRight) {
                            if ((position.getRow() == car.getRow())
                             && (position.getCol() == (car.getCol() + car.getLength() + i - 1))) {
                                return false;
                            } 
                        } else if (action instanceof MoveUp) {
                            if ((position.getCol() == car.getCol())
                             && (position.getRow() == (car.getRow() - i))) {
                                return false;
                            }
                        } else if (action instanceof MoveDown) {
                            if ((position.getCol() == car.getCol())
                             && (position.getRow() == (car.getRow() + car.getLength() + i - 1))) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    
    public State doAction(Action action) {
        // Create a new state to store the moves in
        GameState newState = new GameState(this);
        // Get the car that the move is for
        Car oldCar = action.getCar();
        // Create a new object for the new car
        Car car = null;
        // Find the same car in the new state
        for (Car newCar : newState.getCars()) {
            if (newCar.equals(oldCar)) {
                car = newCar;
                break;
            }
        }
        // If the action is legal, move the car
        if (action instanceof MoveLeft && isLegal(action)) {
            car.setCol(car.getCol() - action.getDistance());
        } else if (action instanceof MoveRight && isLegal(action)) {
            car.setCol(car.getCol() + action.getDistance());
        } else if (action instanceof MoveUp && isLegal(action)) {
            car.setRow(car.getRow() - action.getDistance());
        } else if (action instanceof MoveDown && isLegal(action)) {
            car.setRow(car.getRow() + action.getDistance());
        } else {
            System.out.println("Error with move");
        }
        // printState();
        return newState;
    }
    
    public List<Car> getCars() {
        // Helper function to get the cars
        return cars;
    }
    
    public int getEstimatedDistanceToGoal() {
        // Get the goal car
        Car goalCar = cars.get(0);
        int row = goalCar.getRow();
        int col = goalCar.getCol();
        int length = goalCar.getLength();
        // The initial cost is 1 as if there are no cars in the way, 1 move is required
        int cost = 1;
        // For each car that is in the way, add 1 to the cost.
        for (Car car : cars.subList(1, cars.size())) {
            // If the car is vertical and overlaps the current row to the right of the goal car
            if (car.isVertical() && car.getRow() <= row && car.getRow() + car.getLength() >= row && car.getCol() > col + length - 1) {
                cost++;
            }
        }
        return cost;
    }
    
}
