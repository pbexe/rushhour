/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package search;
import rushhour.Car;

/**
 *
 * @author steven
 */
public interface Action {
    
    public int getCost();
    public String toString();
    public Car getCar();
    public void setCar(Car car);
    public int getDistance();
    public void setDistance(int distance);
    
}
