/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs.form.fields;

import libs.EmailValidator;

/**
 *
 * @author kevin
 */
public class EmailField extends TextField {

    public EmailField(String name) {
        super(name);

        setErrorText("invalid_email", "L'adresse mail est invalide.");
    }

    @Override
    public boolean isValid() {
        if(!super.isValid())
            return false;

        if(!EmailValidator.validate(getValue())) {
            error("invalid_email");
            return false;
        }

        return true;
    }
}
