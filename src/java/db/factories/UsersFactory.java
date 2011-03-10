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
import java.util.List;
import metier.User;


public class UsersFactory {
    public static List<User> getAll() throws SQLException {
        Connexion con = Connexion.getInstance();
        List<User> users = new ArrayList<User>();

        String sql = "SELECT uID, login, pass, last_name, first_name, mail "+
                     "FROM users "+
                     "ORDER BY login ASC";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt);

        ResultSet res = stmt.executeQuery();
        while(res.next())
            users.add(resultToUser(res));

        res.close();
        stmt.close();

        return users;
    }
    
    public static User get(String login, String pass) throws SQLException {
        Connexion con = Connexion.getInstance();
        
        String sql = "SELECT uID, login, pass, last_name, first_name, mail "+
                     "FROM users WHERE login = ? AND pass = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, login, pass);

        return getOne(stmt);
    }
    
    public static User get(int id) throws SQLException {
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

    public static void save(User u) throws SQLException {
        if(u.isNew())
            insert(u);
        else
            update(u);
    }

    private static void insert(User u) throws SQLException {
        Connexion con = Connexion.getInstance();
        String sql = "INSERT INTO users (login, last_name, first_name, mail, pass) "+
                     "VALUES (?, ?, ?, ?, ?)";

        con.execute(sql, u.getLogin(), u.getLastName(), u.getFirstName(),
                    u.getMail(), u.getPass());
    }

    private static void update(User u) throws SQLException {
        Connexion con = Connexion.getInstance();
        String sql = "UPDATE users SET last_name = ?, first_name = ?, mail = ?, pass = ? "+
                     "WHERE uID = ?";

        con.execute(sql, u.getLastName(), u.getFirstName(), u.getMail(),
                    u.getPass(), u.getId());
    }

    public static void delete(int id) throws SQLException {
        Connexion.getInstance().execute("DELETE FROM users WHERE uID = ?", id);
    }
    
    
    private static User resultToUser(ResultSet res) throws SQLException {
        User u = resultToDisplayUser(res);
        
        u.setHash(res.getString("pass"));
        u.setMail(res.getString("mail"));
        
        return u;
    }
    
    static User resultToDisplayUser(ResultSet res) throws SQLException {
        User u = new User(res.getInt("uID"));
        
        u.setLogin(res.getString("login"));
        u.setLastName(res.getString("last_name"));
        u.setFirstName(res.getString("first_name"));
        
        return u;
    }
}
