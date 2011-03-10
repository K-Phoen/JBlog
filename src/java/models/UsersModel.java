/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import db.factories.UsersFactory;
import java.sql.SQLException;
import java.util.List;
import metier.User;


public class UsersModel {
    public List<User> getAll() throws SQLException {
        return UsersFactory.getAll();
    }

    public User get(int id) throws SQLException {
        return UsersFactory.get(id);
    }

    public void save(User u) throws SQLException {
        UsersFactory.save(u);
    }
}
