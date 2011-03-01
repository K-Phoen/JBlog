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
    public static User get(String login, String pass) throws ClassNotFoundException, SQLException {
        Connexion con = Connexion.getInstance();
        
        String sql = "SELECT uID, login, pass, last_name, first_name, mail "+
                     "FROM users WHERE login = ? AND pass = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, login, pass);

        return getOne(stmt);
    }
    
    public static User get(int id) throws ClassNotFoundException, SQLException {
        Connexion con = Connexion.getInstance();
        
        String sql = "SELECT uID, login, pass, last_name, first_name, mail "+
                     "FROM users WHERE uID = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, id);

        return getOne(stmt);
    }
    
    private static User getOne(PreparedStatement stmt) throws SQLException {
        User u = null;
        ResultSet res = stmt.executeQuery();
        
        if(res.next())
            u = resultToUser(res);
        
        res.close();
        stmt.close();
        
        return u;
    }
    
    
    private static User resultToUser(ResultSet res) throws SQLException {
        User u = new User(res.getInt("uID"));
        
        u.setLogin(res.getString("login"));
        u.setHash(res.getString("pass"));
        u.setLastName(res.getString("last_name"));
        u.setFirstName(res.getString("first_name"));
        u.setMail(res.getString("mail"));
        
        return u;
    }
}
