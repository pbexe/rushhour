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
public class MoveDown implements Action{

    private Car car;

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
    
}
