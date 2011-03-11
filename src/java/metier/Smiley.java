/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metier;


public class Smiley extends Entity {
    private String code;
    private String img;

    
    public Smiley(int id, String code, String img) {
        setId(id);
        setCode(code);
        setImg(img);
    }
    
    public String getCode() {
        return code;
    }

    public final void setCode(String code) {
        this.code = code;
    }

    public String getImg() {
        return img;
    }

    public final void setImg(String img) {
        this.img = img;
    }
    
    public String toHTML() {
        String html = "<img src=\"./smileys/%s\" alt=\"%s\" />";
        
        return String.format(html, getImg(), getCode());
    }
}
