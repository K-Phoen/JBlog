/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs.form.fields;


public class PasswordField extends TextField {
    public PasswordField(String name) {
        super(name);

        setType("password");
    }
}
