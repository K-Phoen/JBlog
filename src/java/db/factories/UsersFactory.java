/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package db.factories;

import db.Connexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import metier.User;


public class UsersFactory {
    public static User get(String login, String pass) throws ClassNotFoundException, SQLException, Exception {
        Connexion con = Connexion.getInstance();
        User u = null;
        
        String sql = "SELECT uID, login, pass, last_name, first_name, mail "+
                     "FROM users WHERE login = ? AND pass = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, login, pass);

        ResultSet res = stmt.executeQuery();
        if(res.next())
            u = resultToUser(res);
        
        res.close();
        stmt.close();
        
        return u;
    }
    
    
    private static User resultToUser(ResultSet res) throws SQLException, Exception {
        User u = new User(res.getInt("uID"));
        
        u.setLogin(res.getString("login"));
        u.setPass(res.getString("pass"));
        u.setLastName(res.getString("last_name"));
        u.setFirstName(res.getString("first_name"));
        u.setMail(res.getString("mail"));
        
        return u;
    }
}
