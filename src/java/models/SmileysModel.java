/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import db.factories.SmileysFactory;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import metier.Smiley;


public class SmileysModel {
    public static List<Smiley> getAll() throws SQLException {
        return SmileysFactory.getAll();
    }
}
