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
    
    
    public boolean isLegal(Action action) {
        if (action instanceof MoveLeft) {
            Car car = action.getCar();
            int distance = action.getDistance();
            if (car.isVertical()) {
                return false;
            }
            if (car.getCol() - distance < 0) {
                return false;
            }
            for (Car otherCar : cars) {
                if (otherCar != car) {
                    List<Position> positions = otherCar.getOccupyingPositions();
                    for (Position position : positions) {
                        for (int i = 1; i <= distance; i++) {
                            if ((position.getRow() == car.getRow()) && (position.getCol() == (car.getCol() - i))) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        } else if (action instanceof MoveRight) {
            Car car = action.getCar();
            int distance = action.getDistance();
            if (car.isVertical()) {
                return false;
            }
            if ((car.getCol() + car.getLength() + distance - 1) > nrCols - 1) {
                return false;
            }
            for (Car otherCar : cars) {
                if (otherCar != car) {
                    List<Position> positions = otherCar.getOccupyingPositions();
                    for (Position position : positions) {
                        for (int i = 1; i <= distance; i++) {
                            if ((position.getRow() == car.getRow())
                            && (position.getCol() == (car.getCol() + car.getLength() + i - 1))) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        } else if (action instanceof MoveUp) {
            Car car = action.getCar();
            int distance = action.getDistance();
            if (!car.isVertical()) {
                return false;
            }
            if (car.getRow() - distance < 0) {
                return false;
            }
            for (Car otherCar : cars) {
                if (otherCar != car) {
                    List<Position> positions = otherCar.getOccupyingPositions();
                    for (Position position : positions) {
                        for (int i = 1; i <= distance; i++) {
                            if ((position.getCol() == car.getCol()) && (position.getRow() == (car.getRow() - i))) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        } else if (action instanceof MoveDown) {
            Car car = action.getCar();
            int distance = action.getDistance();
            if (!car.isVertical()) {
                return false;
            }
            if ((car.getRow() + car.getLength() + distance - 1) > nrRows - 1) {
                return false;
            }
            for (Car otherCar : cars) {
                if (otherCar != car) {
                    List<Position> positions = otherCar.getOccupyingPositions();
                    for (Position position : positions) {
                        for (int i = 1; i <= distance; i++) {
                            if ((position.getCol() == car.getCol())
                            && (position.getRow() == (car.getRow() + car.getLength() + i - 1))) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        } else
        return false;
    }
    
    public State doAction(Action action) {
        GameState newState = new GameState(this);
        Car oldCar = action.getCar();
        Car car = null;
        for (Car newCar : newState.getCars()) {
            if (newCar.equals(oldCar)) {
                car = newCar;
                break;
            }
        }
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
        return newState;
    }
    
    public List<Car> getCars() {
        return cars;
    }
    
    public int getEstimatedDistanceToGoal() {
        Car goalCar = cars.get(0);
        int row = goalCar.getRow();
        int col = goalCar.getCol();
        if (goalCar.getCol() + goalCar.getLength() == nrCols) {
            return 0;
        }
        int cost = 1;
        for (Car car : cars.subList(1, cars.size())) {
            if (car.isVertical() && (car.getRow() <= row && car.getRow() + car.getLength() >= row)) {
                List<Position> positions = car.getOccupyingPositions();
                for (Position position : positions) {
                    if ((position.getRow() == row) && (position.getCol() > col)) {
                        cost++;
                        break;
                    }
                }
            }
        }
        return cost;
    }
    
}
