/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs.form.fields;

import java.util.HashMap;
import java.util.Map;
import libs.form.Form;


public class SelectField extends TextField {
    private Map<String, String> options = new HashMap<String, String>();
    private String emptyOptText = "----------";


    public SelectField(String name) {
        super(name);

        setErrorText("invalid_choice", "La valeur proposée est incorrecte.");
    }

    public SelectField addOptions(Map<String, String> options) {
        this.options.putAll(options);

        return this;
    }

    /**
     * Lance la construction des options du select
     *
     * @return string (code html des options)
     */
    protected String makeOptions()
    {
        StringBuilder sb = new StringBuilder("<option value=\"\">");

        sb.append(emptyOptText).append("</option>");

        for(String key : options.keySet())
            proceedOptions(sb, key, options.get(key));

        return sb.toString();
    }

    /**
     * Fonction s'occupant de générer du HTML pour les options
     *
     * @param &$output Référence vers la variable qui contiendra le html
     * @param $name valeur du <option>
     * @param $text texte à afficher pour l'option
     *
     * @return void
     */
    private void proceedOptions(StringBuilder sb, String key, String text) {
        String selected = key.equals(getValue()) ? " selected=\"selected\"" : "";

        sb.append("<option value=\"").append(key).append("\"")
          .append(selected).append(">").append(text).append("</option>");
    }

    @Override
    public boolean isValid() {
        if(super.isValid())  {
            if(!options.containsKey(getValue())) {
                error("invalid_choice");
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public String toHTML() {
        makeClass();

        String opts = makeOptions();

        Map<String, String> html_attrs = getAttrs();
        html_attrs.put("value", "");

        return String.format("<select %s>%s</select>", Form.toStringAttrs(html_attrs), opts);
    }
}
