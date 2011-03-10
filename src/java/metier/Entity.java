/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metier;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;


public abstract class Entity {
    private int id = 0;
    
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");


    public Entity() {
    }

    public Entity(int id) {
        setId(id);
    }
    
    public int getId() {
        return id;
    }
    
    public final void setId(int id) {
        if(!isNew())
            throw new IllegalAccessError("Impossible de modifier l'id");
        
        this.id = id;
    }
    
    public boolean isNew() {
        return getId() == 0;
    }
    
    protected final String slugify(String txt) {
        String nowhitespace = WHITESPACE.matcher(txt).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");

        return slug.toLowerCase(Locale.ENGLISH);
    }
}
