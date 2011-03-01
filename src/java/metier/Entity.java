/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metier;


public abstract class Entity {
    public abstract int getId();
    
    public boolean isNew() {
        return getId() == 0;
    }
}
