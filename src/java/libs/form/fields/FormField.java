/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs.form.fields;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import libs.form.Form;

public abstract class FormField {
    private boolean required = true;
    private boolean auto_bound = true;

    private String label = "";

    private List<String> error_messages = new ArrayList<String>();
    private List<String> classes = new ArrayList<String>();

    private Map<String, String> attrs = new HashMap<String, String>();
    private Map<String, String> error_list = new HashMap<String, String>();
    

    public FormField(String name) {
        attrs.put("name", name);
        setID(getName());
        setValue("");

        setErrorText("required", "Ce champ est obligatoire.");
    }

    /**
     * Dit si le contenu du champ est valide et crée les erreurs si besoin
     * (pourra et devra être surchargée)
     *
     * @return bool
     */
    public boolean isValid() {
        // empty considérant les valeurs telles que 0 comme vides
        if(isRequired() && getValue().isEmpty()) {
            error("required");
            return false;
        }

        return true;
    }

    public boolean isRequired() {
        return required;
    }

    /**
     * Rend obligatoire le champ ou pas
     *
     * @param bool status du champ
     *
     * @return obj (le champ en question)
     */
    public FormField required(boolean bool) {
        required = bool;

        return this;
    }

    protected final void setAttr(String name, String val) {
        attrs.put(name, val);
    }

    /**
     * Dés/active un champ
     *
     * @param bool état d'activation
     *
     * @return obj (le champ en question)
     */
    public FormField disabled(boolean bool) {
        if(bool)
            attrs.put("disabled", "disabled");
        else
            attrs.put("disabled", "");

        return this;
    }

    /**
     * Dés/active la lecture seule
     *
     * @param bool état d'activation
     *
     * @return obj (le champ en question)
     */
    public FormField readOnly(boolean bool) {
        if(bool)
            attrs.put("readonly", "readonly");
        else
            attrs.put("readonly", "");

        return this;
    }

    /**
     * Dés/Active la remplissage auto lors de l'appel à la méthode bound()
     * Empêche simplement la valeur de s'afficher, mais elle reste présente et récupérable via getValue() par ex
     *
     * @param bool état (True == activé)
     *
     * @return obj (le champ en question)
     */
    public FormField autoBound(boolean bool) {
        auto_bound = bool;

        return this;
    }

    /**
     * Donne l'état du remplissage auto
     *
     * @return bool
     */
    public boolean canBound() {
        return auto_bound;
    }

    /**
     * Retourne le nom du champ
     *
     * \note Si le champ est un tableau, son attribut name se termine par
     *       [] dans le code HTML. Cette méthode supprime les crochets.
     *
     * @return string
     */
    public final String getName() {
        return attrs.get("name");
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
     * Retourne le type du champ
     *
     * @return string
     */
    public String getType() {
        return attrs.get("type") == null ? "" : attrs.get("type");
    }

    protected final void setType(String type) {
        attrs.put("type", type);
    }

    /**
     * Retourne le contenu du label
     *
     * @return string
     */
    public String getLabel() {
        return label;
    }

    /**
     * Retourne les messages d'erreurs
     *
     * @return array
     */
    public List<String> getErrors()
    {
        return Collections.unmodifiableList(error_messages);
    }

    /**
     * Retourne la valeur du champ
     *
     * @return string
     */
    public String getValue() {
        return attrs.get("value");
    }

    /**
     * Ajoute une classe CSS au champ
     *
     * @param className nom de la class
     *
     * @return obj (le champ en question)
     */
    public FormField addClass(String className) {
        if(!classes.contains(className))
            classes.add(className);

        return this;
    }

    /**
     * Génère le contenu de l'attribut _class_ avec tous les éléments données
     *
     * \note Méthode à appeler en début de __toString()
     *
     * return void
     */
    protected final void makeClass() {
        StringBuilder sb = new StringBuilder();

        for(String c : classes)
            sb.append(c).append(" ");

        attrs.put("class", sb.toString());
    }

    protected Map<String, String> getAttrs() {
        return new HashMap<String, String>(attrs);
    }

    /**
     * Change la valeur d'un champ du formulaire
     *
     * @param val nouvelle valeur
     *
     * @return obj (le champ en question)
     */
    public FormField setValue(String val) {
        attrs.put("value", val);

        return this;
    }

    /**
     * Change la valeur du label
     *
     * @param text nouvelle valeur
     *
     * @return obj (le champ en question)
     */
    public FormField setLabel(String text) {
        label = text;

        return this;
    }

    /**
     * Change la valeur de l'ID du champ
     *
     * @param text nouvelle valeur
     *
     * @return obj (le champ en question)
     */
    public final FormField setID(String text) {
        attrs.put("id", text);

        return this;
    }

    /**
     * Permet de changer le message affiché lors d'une erreur
     *
     * @param errorId id du message d'erreur
     * @param text nouveau message
     *
     * @return obj (le champ en question)
     */
    public final FormField setErrorText(String errorId, String text) {
        error_list.put(errorId, text);

        return this;
    }

    /**
     * Ajoute le message d'erreur correspondant à id dans la liste d'erreurs
     *
     * @param msg identifiant de l'erreur
     * @param params paramètres du message d'erreur
     */
    protected final void error(String msg, Object ... params)  {
        String errorMsg = error_list.get(msg);

        if(errorMsg == null)
            errorMsg = msg;
        
        error_messages.add(String.format(errorMsg, params));
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FormField))
            return false;

        return getName().equals(((FormField) obj).getName());
    }

    @Override
    public String toString() {
        return toHTML();
    }

    public String toHTML() {
        StringBuilder sb = new StringBuilder();
        makeClass();

        Map<String, String> html_attrs = getAttrs();
        html_attrs.put("value", canBound() ? html_attrs.get("value") : "");

        sb.append("<input ").append(Form.toStringAttrs(html_attrs))
          .append(" />");

        return sb.toString();
    }
}
