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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import metier.Article;
import metier.Category;
import metier.User;


public class ArticlesFactory {
    public static List<Article> getNFirst(int first, int nb, boolean valid) throws SQLException, Exception {
        Map<String, List<Object>> limiters = new HashMap<String, List<Object>>();
        
        if(valid)
            limiters.put("valid = ?", toList(1));
        
        return getList(first, nb, limiters);
    }
    
    public static List<Article> getNSearch(String search, int first, int nb, boolean valid) throws SQLException, Exception {
        Map<String, List<Object>> limiters = new HashMap<String, List<Object>>();
        String fSearch = String.format("%%%s%%", search);
        
        limiters.put("(a.title LIKE ? OR content LIKE ?)", toList(fSearch, fSearch));
        if(valid)
            limiters.put("valid = ?", toList(1));
        
        return getList(first, nb, limiters);
    }
    
    public static List<Article> getNCategorie(int categorie, int first, int nb, boolean valid) throws SQLException, Exception {
        Map<String, List<Object>> limiters = new HashMap<String, List<Object>>();
        
        limiters.put("c_ID = ?", toList(categorie));
        if(valid)
            limiters.put("valid = ?", toList(1));
        
        return getList(first, nb, limiters);
    }
    
    public static List<Article> getList(int first, int nb, Map<String, List<Object>> limiters)throws SQLException, Exception {
        Connexion con = Connexion.getInstance();
        List<Article> articles = new ArrayList<Article>();
        StringBuilder where_clause = new StringBuilder();
        List<Object> params = new ArrayList<Object>();
        
        boolean lFirst = true;
        for(String key : limiters.keySet()) {
            params.addAll(limiters.get(key));
            
            if(lFirst) {
                where_clause.append("WHERE ").append(key);
                lFirst = false;
                continue;
            }
            
            where_clause.append(" AND ").append(key);
        }
        
        String sql = "SELECT aID, u_ID, c_ID, a.slug as a_slug, a.title as a_title, content, date, "+
                     "nb_coms, valid, uID, login, first_name, last_name, cID, "+
                     "c.slug, c.title "+
                     "FROM articles a "+
                     "LEFT JOIN users ON users.uID = a.u_ID "+
                     "LEFT JOIN categories c ON c.cID = a.c_ID "+
                     where_clause.toString()+
                     " ORDER BY aID DESC LIMIT ?, ?";
        
        PreparedStatement stmt = con.prepareStatement(sql);
        
        Connexion.bindParams(stmt, params);
        stmt.setObject(params.size()+1, first);
        stmt.setObject(params.size()+2, nb);

        ResultSet res = stmt.executeQuery();
        while(res.next())
            articles.add(resultToArticle(res));
        
        res.close();
        stmt.close();

        return articles;
    }
    
    public static Article getBySlug(String slug) throws SQLException, Exception {
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

    public static Article get(int id) throws SQLException, Exception {
        Connexion con = Connexion.getInstance();
        Article a = null;

        String sql = "SELECT aID, u_ID, c_ID, a.slug as a_slug, a.title as a_title, content, date, "+
                     "nb_coms, valid, uID, login, first_name, last_name, cID, "+
                     "c.slug, c.title "+
                     "FROM articles a "+
                     "LEFT JOIN users ON users.uID = a.u_ID "+
                     "LEFT JOIN categories c ON c.cID = a.c_ID "+
                     "WHERE a.aID = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, id);

        ResultSet res = stmt.executeQuery();
        if(res.next())
            a = CommentsFactory.get(resultToArticle(res));

        res.close();
        stmt.close();

        return a;
    }
    
    public static int countArticles(boolean valid) throws SQLException {
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
    
    public static int countArticlesCategorie(int id, boolean valid) throws SQLException {
        Connexion con = Connexion.getInstance();
        
        String sql = "SELECT COUNT(1) as total "+
                     "FROM articles "+
                     "WHERE c_ID = ? "+
                     (valid ? "AND valid = 1 " : "");
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, id);

        ResultSet res = stmt.executeQuery();
        res.next();
        
        int total = res.getInt("total");
        
        res.close();
        stmt.close();
        
        return total;
    }
    
    public static int countArticlesSearch(String search, boolean valid) throws SQLException {
        Connexion con = Connexion.getInstance();
        String fSearch = String.format("%%%s%%", search);
        
        String sql = "SELECT COUNT(1) as total "+
                     "FROM articles "+
                     "WHERE (title LIKE ? OR content LIKE ?) "+
                     (valid ? "AND valid = 1 " : "");
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, fSearch, fSearch);

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
        a.setSlug(res.getString("a_slug"));
        a.setValid(res.getBoolean("valid"));
        a.setUId(res.getInt("u_ID"));
        a.setCId(res.getInt("c_ID"));

        return a;
    }
    
    private static List<Object> toList(Object ... params) {
        List<Object> list = new ArrayList<Object>();
        
        for(int i=0; i < params.length; ++i)
            list.add(params[i]);
        
        return list;
    }

    public static void save(Article a) throws SQLException, Exception {
        if(a.isNew())
            insert(a);
        else
            update(a);
    }
    
    public static void delete(int id) throws SQLException {
        Connexion con = Connexion.getInstance();
        String sql = "DELETE FROM articles WHERE aID = ?";
        
        con.execute(sql, id);
    }
    
    
    private static void insert(Article a) throws SQLException, Exception {
        Connexion con = Connexion.getInstance();
        String sql = "INSERT INTO articles (u_ID, c_ID, slug, title, content, date, nb_coms, valid) "+
                     "VALUES (?, ?, ?, ?, ?, ?, 0, ?)";
        
        con.execute(sql, a.getUId(), a.getCId(), a.getSlug(), a.getTitle(),
                    a.getContent(), a.dateToString("yyyy-MM-dd"), a.isValid());
    }

    private static void update(Article a) throws SQLException, Exception {
        Connexion con = Connexion.getInstance();
        String sql = "UPDATE articles SET u_ID = ?, c_ID = ?, slug = ?, title = ?, "+
                                         "content = ?, date = ?, nb_coms = ?, "+
                                         "valid  = ? "+
                     "WHERE aID = ?";
        
        con.execute(sql, a.getUId(), a.getCId(), a.getSlug(), a.getTitle(),
                    a.getContent(), a.dateToString("yyyy-MM-dd"), a.getNbComs(),
                    a.isValid(), a.getId());
    }
}
