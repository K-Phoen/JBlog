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
import metier.Article;


public class ArticlesFactory {
    public static List<Article> getN(int first, int nb) throws ClassNotFoundException, SQLException, Exception {
        Connexion con = Connexion.getInstance();
        List<Article> articles = new ArrayList<Article>();
        
        
        String sql = "SELECT aID, u_ID, c_ID, slug, title, content, date, nb_coms, valid "+
                     "FROM articles ORDER BY aID DESC LIMIT ?, ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, first, nb);

        ResultSet res = stmt.executeQuery();
        while(res.next())
            articles.add(resultToArticle(res));
        
        res.close();
        stmt.close();

        return articles;
    }
    
    public static Article getBySlug(String slug) throws ClassNotFoundException, SQLException, Exception {
        Connexion con = Connexion.getInstance();
        Article a = null;
        
        String sql = "SELECT aID, u_ID, c_ID, slug, title, content, date, nb_coms, valid "+
                     "FROM articles WHERE slug = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, slug);

        ResultSet res = stmt.executeQuery();
        if(res.next()) {
            a = resultToArticle(res);
            
            CommentsFactory.get(a);
        }
        
        res.close();
        stmt.close();
        
        return a;
    }

    private static Article resultToArticle(ResultSet res) throws SQLException, Exception {

            Article a = new Article(res.getInt("aID"), res.getInt("nb_coms"));
            a.setTitle(res.getString("title")); 
            a.setDate(res.getString("date"));
            a.setContent(res.getString("content"));
            a.setUrl(res.getString("slug"));
            a.setValid(res.getBoolean("valid"));

            return a;
    }
}
