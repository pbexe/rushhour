/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rushhour;

import search.Action;

/**
 *
 * @author steven
 */
public class MoveDown implements Action{

    private Car car;
    private int distance;


    public MoveDown(Action move){
        this.car = move.getCar();
        this.distance = move.getDistance();
    }

    public MoveDown(Car car, int distance){
        this.car = car;
        this.distance = distance;
    }

    public MoveDown(){
    }
    
    public int getCost() {
        return 1;
    }

    public String toString(){
        return "move down";
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
