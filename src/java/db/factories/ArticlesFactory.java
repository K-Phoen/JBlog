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
import metier.Category;
import metier.User;


public class ArticlesFactory {
    public static List<Article> getN(int first, int nb, boolean valid) throws ClassNotFoundException, SQLException, Exception {
        Connexion con = Connexion.getInstance();
        List<Article> articles = new ArrayList<Article>();
        
        
        String sql = "SELECT aID, u_ID, c_ID, a.slug as a_slug, a.title as a_title, content, date, "+
                     "nb_coms, valid, uID, login, first_name, last_name, cID, "+
                     "c.slug, c.title "+
                     "FROM articles a "+
                     "LEFT JOIN users ON users.uID = a.u_ID "+
                     "LEFT JOIN categories c ON c.cID = a.c_ID "+
                     (valid ? "WHERE valid = 1 " : "")+
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
        
        String sql = "SELECT aID, u_ID, c_ID, a.slug as a_slug, a.title as a_title, content, date, "+
                     "nb_coms, valid, uID, login, first_name, last_name, cID, "+
                     "c.slug, c.title "+
                     "FROM articles a "+
                     "LEFT JOIN users ON users.uID = a.u_ID "+
                     "LEFT JOIN categories c ON c.cID = a.c_ID "+
                     "WHERE a.slug = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, slug);

        ResultSet res = stmt.executeQuery();
        if(res.next())
            a = CommentsFactory.get(resultToArticle(res));
        
        res.close();
        stmt.close();
        
        return a;
    }
    
    public static int countArticles(boolean valid) throws SQLException, ClassNotFoundException {
        Connexion con = Connexion.getInstance();
        
        String sql = "SELECT COUNT(1) as total "+
                     "FROM articles "+
                     (valid ? "WHERE valid = 1 " : "");
        PreparedStatement stmt = con.prepareStatement(sql);

        ResultSet res = stmt.executeQuery();
        res.next();
        
        int total = res.getInt("total");
        
        res.close();
        stmt.close();
        
        return total;
    }

    private static Article resultToArticle(ResultSet res) throws SQLException, Exception {
        Article a = new Article(res.getInt("aID"), res.getInt("nb_coms"));
        User author = UsersFactory.resultToDisplayUser(res);
        Category category = CategoryFactory.resultToCategory(res);
        
        a.setAuthor(author);
        a.setCategory(category);

        a.setTitle(res.getString("a_title")); 
        a.setDate(res.getString("date"));
        a.setContent(res.getString("content"));
        a.setUrl(res.getString("a_slug"));
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
