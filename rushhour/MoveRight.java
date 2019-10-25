/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rushhour;

import search.Action;
import search.State;

/**
 *
 * @author steven
 */
public class MoveRight implements Action{

    private Car car;
    private int distance;

    public MoveRight(Action move){
        // this.car = new Car(move.getCar());
        this.car = move.getCar();
        this.distance = move.getDistance();
    }

    public MoveRight(Car car, int distance){
        this.car = car;
        this.distance = distance;
    }

    public MoveRight(){
    }

    public int getCost() {
        return 1;
    }

    public String toString(){
        return "move right";
    }
    
    public void setCar(Car car){
        this.car = car;
    }

    public Car getCar(){
        return this.car;
    }
    
    public int getDistance(){
        return this.distance;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }
}
