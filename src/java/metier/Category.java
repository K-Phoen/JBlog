/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metier;


public final class Category extends Entity {
    private String slug;
    private String title;
    
    
    public Category() {
    }
    
    public Category(int id, String slug, String title) {
        super(id);
        
        setSlug(slug);
        setTitle(title);
    }

    public String getSlug() {
        if(slug == null || slug.isEmpty())
            slug = slugify(getTitle());

        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        setSlug(null);
    }
}
