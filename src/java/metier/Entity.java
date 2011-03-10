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
        this.id = id;
    }
    
    public int getId() {
        return id;
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
