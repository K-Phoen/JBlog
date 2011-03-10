/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs.form.fields;


public class BooleanField extends TextField {
    public BooleanField(String name) {
        super(name);

        setType("checkbox");
        super.setValue("1");
        setValue(false);
    }

    /**
     * Coche ou décoche la checkbox
     *
     * @param bool état de la checkbox
     *
     * @return obj (le champ en question)
     */
    public final BooleanField setValue(boolean bool) {
        setAttr("checked", bool ? "checked" : "");
        
        return this;
    }

    @Override
    public final BooleanField setValue(String val) {
        setValue(val != null && !val.trim().isEmpty());

        return this;
    }

    @Override
    public String getValue() {
        return isChecked() ? "1" : "0";
    }
    
    public boolean isChecked() {
        return getAttrs().get("checked").equals("checked");
    }

    @Override
    public boolean isValid() {
        if(isRequired() && !isChecked()) {
            error("required");
            return false;
        }
        
        return true;
    }
}
