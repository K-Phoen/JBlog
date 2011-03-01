/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metier;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import libs.SHA1;


public class User extends Entity {
    private String login;
    private String pass;
    private String lastName;
    private String firstName;
    private String mail;

    
    public User(int id) {
        super(id);
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        setHash(hashPass(pass));
    }
    
    /**
     * @param pass the pass to set
     */
    public void setHash(String pass) {
        this.pass = pass;
    }
    
    public static String hashPass(String pass) {
        return SHA1.hash(pass);
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }
}
