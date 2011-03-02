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
import metier.User;


public class ArticlesFactory {
    public static List<Article> getN(int first, int nb) throws ClassNotFoundException, SQLException, Exception {
        Connexion con = Connexion.getInstance();
        List<Article> articles = new ArrayList<Article>();
        
        
        String sql = "SELECT aID, u_ID, c_ID, slug, title, content, date, "+
                     "nb_coms, valid, uID, login, first_name, last_name "+
                     "FROM articles "+
                     "LEFT JOIN users ON users.uID = articles.u_ID "+
                     "ORDER BY aID DESC LIMIT ?, ?";
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
        
        String sql = "SELECT aID, u_ID, c_ID, slug, title, content, date, "+
                     "nb_coms, valid, uID, login, first_name, last_name "+
                     "FROM articles a "+
                     "LEFT JOIN users u ON u.uID = a.u_ID "+
                     "WHERE slug = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, slug);

        ResultSet res = stmt.executeQuery();
        if(res.next())
            a = CommentsFactory.get(resultToArticle(res));
        
        res.close();
        stmt.close();
        
        return a;
    }

    private static Article resultToArticle(ResultSet res) throws SQLException, Exception {
        Article a = new Article(res.getInt("aID"), res.getInt("nb_coms"));
        User author = UsersFactory.resultToDisplayUser(res);
        
        a.setAuthor(author);

        a.setTitle(res.getString("title")); 
        a.setDate(res.getString("date"));
        a.setContent(res.getString("content"));
        a.setUrl(res.getString("slug"));
        a.setValid(res.getBoolean("valid"));
        a.setUId(res.getInt("u_ID"));
        a.setCId(res.getInt("c_ID"));

        return a;
    }

    public static void save(Article a) throws ClassNotFoundException, SQLException, Exception {
        if(a.isNew())
            insert(a);
        else
            update(a);
    }
    
    
    private static void insert(Article a) throws ClassNotFoundException, SQLException, Exception {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static void update(Article a) throws ClassNotFoundException, SQLException, Exception {
        Connexion con = Connexion.getInstance();
        String sql = "UPDATE articles SET u_ID = ?, c_ID = ?, slug = ?, title = ?, "+
                                         "content = ?, date = ?, nb_coms = ?, "+
                                         "valid  = ? "+
                     "WHERE aID = ?";
        
        con.execute(sql, a.getUId(), a.getCId(), a.getUrl(), a.getTitle(),
                    a.getContent(), a.dateToString("yyyy-MM-dd"), a.getNbComs(),
                    a.isValid(), a.getId());
    }
}
