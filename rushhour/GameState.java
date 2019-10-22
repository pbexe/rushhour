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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        cars = new ArrayList();
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
        cars = new ArrayList();
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
            return nrRows == gs.nrRows && nrCols == gs.nrCols && cars.equals(gs.cars); // note that we don't need to check equality of occupiedPositions since that follows from the equality of cars
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isLegal(Action action) {
        if(action instanceof MoveLeft){
            Car car = action.getCar();
            if (car.isVertical()){
                return false;
            }
            if (car.getCol() == 0){
                return false;
            }
            for (Car otherCar : cars) {
                List<Position> positions = otherCar.getOccupyingPositions();
                for (Position position : positions) {
                    // If the space to the left is taken
                    if ((position.getRow() == car.getRow()) && (position.getCol() == (car.getCol() - 1))){
                        return false;
                    }
                }
            }
            return true;
        }
        else if(action instanceof MoveRight){
            Car car = action.getCar();
            if (car.isVertical()){
                return false;
            }
            if (car.getCol() == nrCols - car.getLength() - 1){
                return false;
            }
            for (Car otherCar : cars) {
                List<Position> positions = otherCar.getOccupyingPositions();
                for (Position position : positions) {
                    // If the space to the right is taken
                    if ((position.getRow() == car.getRow()) && (position.getCol() == (car.getCol() + car.getLength()))){
                        return false;
                    }
                }
            }
            return true;
        }
        else if(action instanceof MoveUp){
            Car car = action.getCar();
            if (!car.isVertical()){
                return false;
            }
            if (car.getRow() == 0){
                return false;
            }
            for (Car otherCar : cars) {
                List<Position> positions = otherCar.getOccupyingPositions();
                for (Position position : positions) {
                    // If the space above is taken
                    if ((position.getCol() == car.getCol()) && (position.getRow() == (car.getRow() - 1))){
                        return false;
                    }
                }
            }
            return true;
        }
        else if(action instanceof MoveDown){
            Car car = action.getCar();
            if (!car.isVertical()){
                return false;
            }
            if (car.getRow() == nrRows - car.getLength() - 1){
                return false;
            }
            for (Car otherCar : cars) {
                List<Position> positions = otherCar.getOccupyingPositions();
                for (Position position : positions) {
                    // If the space below is taken
                    if ((position.getCol() == car.getCol()) && (position.getRow() == (car.getRow() + car.getLength()))){
                        return false;
                    }
                }
            }
            return true;
        }
        else
            return false;
    }

    public State doAction(Action action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getEstimatedDistanceToGoal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
