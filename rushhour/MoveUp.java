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
public class MoveUp implements Action{

    private Car car;
    private int distance;

    public MoveUp(Action move){
        this.car = move.getCar();
        this.distance = move.getDistance();
    }

    public MoveUp(Car car, int distance){
        this.car = car;
        this.distance = distance;
    }

    public MoveUp(){
    }

    public int getCost() {
        return 1;
    }

    public String toString(){
        return "move up";
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
