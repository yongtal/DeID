/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

/**
 *
 * @author angelo
 */
public class multiName {
    private String randomizedID;
    private int count;
    
    public multiName(String id){
        count = 0;
        randomizedID = id;
    }
   public void add(){
       count ++;
   }
   public void setrID(String nrid){
       randomizedID = nrid;
   }
   public int getCount(){
       return count;
   }
   public String getID(){
       return randomizedID;
   }
}
