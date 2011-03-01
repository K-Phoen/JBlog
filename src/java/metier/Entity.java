/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metier;


public abstract class Entity {
    private int id = 0;
    
    public Entity() {
    }

    public Entity(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public boolean isNew() {
        return getId() == 0;
    }
}
