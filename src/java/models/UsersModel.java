package models;

import db.factories.UsersFactory;
import java.sql.SQLException;
import java.util.List;
import metier.User;


public class UsersModel {
    public static List<User> getAll() throws SQLException {
        return UsersFactory.getAll();
    }

    public static User get(int id) throws SQLException {
        return UsersFactory.get(id);
    }

    public static void save(User u) throws SQLException {
        UsersFactory.save(u);
    }

    public static void delete(int id) throws SQLException {
        UsersFactory.delete(id);
    }
}
