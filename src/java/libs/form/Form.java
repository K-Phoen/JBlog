/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import libs.form.fields.FormField;


public class Form {
    private Map<String, String> attrs = new HashMap<String, String>();
    private List<FormField> fields = new ArrayList<FormField>();
    private List<String> fieldsNames = new ArrayList<String>();
    private String labelSuffix = " : ";

    private Map<String, String> values = new HashMap<String, String>();
    private Map<String, List<String>> errorsMsg = new HashMap<String, List<String>>();


    public Form() {
        this("post", "");
    }

    public Form(String method, String action) {
        attrs.put("method", method);
        attrs.put("action", action);
    }

    /**
     * Ajoute un champ au formulaire
     *
     * @param field Instance du champ à ajouter
     * @return
     */
    public Form add(FormField field) {
        if(fields.contains(field))
            throw new IllegalArgumentException("Un champ du même nom existe déjà : "+field.getName());

        fields.add(field);
        fieldsNames.add(field.getName());

        return this;
    }

    public FormField field(String fieldName) {
        int pos = fieldsNames.indexOf(fieldName);

        if(pos == -1)
            return null;
        
        return fields.get(pos);
    }

    public void bind(HttpServletRequest request) {
        for(Object key : request.getParameterMap().keySet()){
            FormField field = field((String) key);

            if(field == null || field.getType().equals("submit"))
                continue;

            String val = (String) ((String[]) request.getParameterMap().get(key))[0];
            field.setValue(val);
        }
    }

    public boolean isValid() {
        boolean valid = true;

        for(FormField field : fields)
        {
            if(!field.isValid())
            {
                valid = false;

                for(String error : field.getErrors())
                    triggerError(field, error);
            }

            values.put(field.getName(), field.getValue());
        }

        return valid && errorsMsg.isEmpty();
    }

    public Map<String, List<String>> getErrors() {
        return Collections.unmodifiableMap(errorsMsg);
    }

    /**
     * Ajoute un message d'erreur à la liste des erreurs
     *
     * @param $field nom du champ ou objet représentant le champ lié à l'erreur
     * @param $error_text message d'erreur à afficher
     *
     * @return void
     */
    public void triggerError(FormField field, String errorText)
    {
        if(!fields.contains(field))
            throw new IllegalArgumentException("Le champ "+field.getName()+" n'existe pas.");

        List<String> errorsList = errorsMsg.get(field.getName());
        if(errorsList == null) {
            errorsList = new ArrayList<String>();
            errorsMsg.put(field.getName(), errorsList);
        }

        errorsList.add(errorText);
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

        for(FormField field : fields)
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

        for(String key : attrs.keySet()) {
            String val = attrs.get(key);

            if(val.isEmpty())
                continue;
            
            sb.append(key).append("=\"").append(val).append("\" ");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return asHTML();
    }
}
