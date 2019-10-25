/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rushhour;

import search.Action;
import search.State;

/**
 
 * @author steven
 */
public class MoveLeft implements Action{

    private Car car;
    private int distance;


    public MoveLeft(Action move){
        // this.car = new Car(move.getCar());
        this.car = move.getCar();
        this.distance = move.getDistance();
    }

    public MoveLeft(Car car, int distance){
        this.car = car;
        this.distance = distance;
    }

    public MoveLeft(){
    }


    public int getCost() {
        return 1;
    }

    public String toString(){
        return "move left";
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
