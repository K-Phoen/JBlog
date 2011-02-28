/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs.form.fields;


public class SubmitButton extends FormField {
    public SubmitButton(String name) {
        super(name);
        setValue(name);
        required(false);

       setAttr("type", "submit");
    }
}
