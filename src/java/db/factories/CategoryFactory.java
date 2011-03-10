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
import metier.Category;


public class CategoryFactory {
    
    public static List<Category> getAll() throws SQLException {
        Connexion con = Connexion.getInstance();
        List<Category> categories = new ArrayList<Category>();
        
        String sql = "SELECT cID, slug, title "+
                     "FROM categories "+                     
                     "ORDER BY title ASC";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt);

        ResultSet res = stmt.executeQuery();
        while(res.next())
            categories.add(resultToCategory(res));
        
        res.close();
        stmt.close();

        return categories;
    }
    
    public static Category get(String slug) throws SQLException {
        Connexion con = Connexion.getInstance();
        Category c = null;
        
        String sql = "SELECT cID, title, slug "+
                     "FROM categories "+
                     "WHERE slug = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, slug);

        ResultSet res = stmt.executeQuery();
        if(res.next())
            c = resultToCategory(res);
        
        res.close();
        stmt.close();
        
        return c;
    }
    
    public static Category get(int id) throws SQLException {
        Connexion con = Connexion.getInstance();
        Category c = null;
        
        String sql = "SELECT cID, title, slug "+
                     "FROM categories "+
                     "WHERE cID = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, id);

        ResultSet res = stmt.executeQuery();
        if(res.next())
            c = resultToCategory(res);
        
        res.close();
        stmt.close();
        
        return c;
    }
    
    public static void save(Category c) throws SQLException {
        if(c.isNew())
            insert(c);
        else
            update(c);
    }
    
    private static void insert(Category c) throws SQLException {
        Connexion con = Connexion.getInstance();
        String sql = "INSERT INTO categories (title, slug) "+
                     "VALUES (?, ?)";
        
        int id = con.execute(sql, c.getTitle(), c.getSlug());
        c.setId(id);
    }

    private static void update(Category c) throws SQLException {
        Connexion con = Connexion.getInstance();
        String sql = "UPDATE categories SET title = ?, slug = ? "+
                     "WHERE cID = ?";
        
        con.execute(sql, c.getTitle(), c.getSlug(), c.getId());
    }
    
    static Category resultToCategory(ResultSet res) throws SQLException {
        return new Category(res.getInt("cID"), res.getString("slug"), res.getString("title"));
    }

    public static void delete(int id) throws SQLException {
        Connexion.getInstance().execute("DELETE FROM categories WHERE cID = ?", id);
    }
}