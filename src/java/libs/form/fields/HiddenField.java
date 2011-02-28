/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs.form.fields;


public class HiddenField extends TextField {
    public HiddenField(String name, String val) {
        super(name);

        setValue(val);
        setType("hidden");
    }
}
