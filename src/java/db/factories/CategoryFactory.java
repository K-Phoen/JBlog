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
    
    public static List<Category> getAll() throws ClassNotFoundException, SQLException, Exception {
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
    
    static Category resultToCategory(ResultSet res) throws SQLException {
        return new Category(res.getInt("cID"), res.getString("slug"), res.getString("title"));
    }   
}


