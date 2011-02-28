/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs.form;

import java.util.HashMap;
import java.util.Map;
import libs.form.fields.FormField;


public class Form {
    private Map<String, String> attrs = new HashMap<String, String>();
    private Map<String, FormField> fields = new HashMap<String, FormField>();
    private String labelSuffix = "";


    public Form() {
        this("post", "");
    }

    public Form(String method, String action) {
        attrs.put("method", method);
        attrs.put("action", action);
    }

    public Form add(FormField field) {
        if(fields.containsKey(field.getName()))
            throw new IllegalArgumentException("Un champ du même nom existe déjà : "+field.getName());

        fields.put(field.getName(), field);

        return this;
    }

    public FormField field(String fieldName) {
        return fields.get(fieldName);
    }

    /**
     * Change l'attribut "name" du formulaire
     *
     * @param $name La nouvelle valeur de l'attribut
     *
     * @return this
     */
    public Form setName(String name) {
        attrs.put("name", name);

        return this;
    }

    /**
     * Change l'ID du formulaire
     *
     * @param $text nouvelle valeur
     *
     * @return obj (le champ en question)
     */
    public Form setID(String id) {
        attrs.put("id", id);

        return this;
    }

    /**
     * Retourne l'ID du champ
     *
     * @return string
     */
    public String getID() {
        return attrs.get("id");
    }

    /**
     * Retourne le form au format HTML, prêt à être utilisé
     * les champs sont encadrés par des <p>
     *
     * @return string
     */
    public String asHTML()
    {
        StringBuilder output = new StringBuilder("<form ");
        output.append(toStringAttrs(attrs)).append(">");

        for(FormField field : fields.values())
        {
            if(field.getType().equals("hidden"))
                 output.append("\n\t").append(field.toHTML());
            else
            {
               output.append("\n\t<p>");

                if(!field.getLabel().isEmpty())
                    output.append("\n\t\t<label for=")
                          .append(field.getID())
                          .append(">").append(field.getLabel())
                          .append(labelSuffix).append("</label>");

                output.append("\n\t\t").append(field.toHTML());
                output.append("\n\t</p>");
            }
        }

        output.append("\n\r</form>");

        return output.toString();
    }

    public static String toStringAttrs(Map<String, String> attrs) {
        StringBuilder sb = new StringBuilder();

        for(String key : attrs.keySet())
            sb.append(key).append("=\"").append(attrs.get(key)).append("\" ");

        return sb.toString();
    }

    @Override
    public String toString() {
        return asHTML();
    }
}
