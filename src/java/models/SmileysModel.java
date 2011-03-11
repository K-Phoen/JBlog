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
    public static Map<String, String> getAllRaw() throws SQLException {
        Map<String, String> smileys = new HashMap<String, String>();
        
        List<Smiley> list = SmileysFactory.getAll();
        for(Smiley s : list)
            smileys.put(s.getCode(), s.getImg());
        
        return smileys;
    }
    
    public static List<Smiley> getAll() throws SQLException {
        return SmileysFactory.getAll();
    }
}
