/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package db.factories;

import db.Connexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import metier.Smiley;


public class SmileysFactory {
    public static List<Smiley> getAll() throws SQLException {
        Connexion con = Connexion.getInstance();
        List<Smiley> smileys = new ArrayList<Smiley>();
        
        String sql = "SELECT sID, code, img "+
                     "FROM smileys "+                     
                     "ORDER BY sID ASC";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt);

        ResultSet res = stmt.executeQuery();
        while(res.next())
            smileys.add(resultToSmiley(res));
        
        res.close();
        stmt.close();

        return smileys;
    }
    
    static Smiley resultToSmiley(ResultSet res) throws SQLException {
        return new Smiley(res.getInt("sID"), res.getString("code"), res.getString("img"));
    }
}
