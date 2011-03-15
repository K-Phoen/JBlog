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
import metier.Comment;


public class CommentsFactory {
    public static Article get(Article a) throws SQLException, Exception {
        Connexion con = Connexion.getInstance();
        List<Comment> comments = new ArrayList<Comment>();
        
        
        String sql = "SELECT coID, a_ID, pseudo, mail, content, date, valide "+
                     "FROM commentaires WHERE a_ID = ? AND valide = 1 ORDER BY date ASC";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, a.getId());

        ResultSet res = stmt.executeQuery();
        while(res.next())
            comments.add(resultToComment(res));
        
        res.close();
        stmt.close();

        a.setComments(comments);
        
        return a;
    }
    
    public static List<Comment> getNFirst(int first, int nb) throws SQLException, Exception {
        List<Comment> comments = new ArrayList<Comment>();
        String sql = "SELECT coID, a_ID, pseudo, mail, c.content, c.date, valide, a.title "+
                     "FROM commentaires c "+
                     "LEFT JOIN articles a "+
                     "ON a.aID = c.a_ID "+
                     "ORDER BY date DESC LIMIT ?, ?";
        
        PreparedStatement stmt = Connexion.getInstance().prepareStatement(sql);
        
        Connexion.bindParams(stmt, first, nb);

        ResultSet res = stmt.executeQuery();
        while(res.next())
            comments.add(resultToComment(res));
        
        res.close();
        stmt.close();

        return comments;
    }
    
    public static Comment get(int id) throws SQLException, Exception {
        Connexion con = Connexion.getInstance();
        Comment c = null;

        String sql = "SELECT coID, a_ID, pseudo, mail, content, date, valide "+
                     "FROM commentaires "+
                     "WHERE coID = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, id);

        ResultSet res = stmt.executeQuery();
        if(res.next())
            c = resultToComment(res);

        res.close();
        stmt.close();

        return c;
    }
    
    public static int count(boolean valid) throws SQLException {
        Connexion con = Connexion.getInstance();
        
        String sql = "SELECT COUNT(1) as total "+
                     "FROM commentaires "+
                     (valid ? "WHERE valide = 1 " : "");
        PreparedStatement stmt = con.prepareStatement(sql);

        ResultSet res = stmt.executeQuery();
        res.next();
        
        int total = res.getInt("total");
        
        res.close();
        stmt.close();
        
        return total;
    }
    
    public static void save(Comment c) throws SQLException, Exception {
        if(c.isNew())
            insert(c);
        else
            update(c);
    }
    
    /**
     * Supprime un commentaire.
     * 
     * @param id Identifiant du commentaire.
     * 
     * @throws SQLException 
     */
    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM commentaires WHERE coID = ?";
        
        Connexion.getInstance().execute(sql, id);
    }
    
    public static void deleteFromArticle(int id) throws SQLException {
        Connexion con = Connexion.getInstance();
        String sql = "DELETE FROM commentaires WHERE a_ID = ?";
        
        con.execute(sql, id);
    }
    
    
    private static void insert(Comment c) throws SQLException, Exception {
        Connexion con = Connexion.getInstance();
        String sql = "INSERT INTO commentaires (a_ID, pseudo, mail, content, date, valide) "+
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        int id = con.execute(sql, c.getaID(), c.getAuthor(), c.getMail(), c.getContent(),
                             c.dateToString("yyyy-MM-dd HH:mm:ss"), c.isValid());
        c.setId(id);
    }

    private static void update(Comment c) throws SQLException, Exception {
        Connexion con = Connexion.getInstance();
        String sql = "UPDATE commentaires SET a_ID = ?, pseudo = ?, mail = ?, "+
                     "content = ?, date = ?, valide = ? "+
                     "WHERE coID = ?";
        
        con.execute(sql, c.getaID(), c.getAuthor(), c.getMail(), c.getContent(),
                    c.dateToString("yyyy-MM-dd HH:mm:ss"), c.isValid(), c.getId());
    }
    
    private static Comment resultToComment(ResultSet res) throws SQLException, Exception {
        Comment c = new Comment(res.getInt("coID"));
        
        c.setAuthor(res.getString("pseudo"));
        c.setMail(res.getString("mail"));
        c.setContent(res.getString("content"));
        c.setDate(res.getString("date"));
        c.setValid(res.getBoolean("valide"));
        c.setaID(res.getInt("a_ID"));
        
        try {
            c.setArticleTitle(res.getString("title"));
        } catch (SQLException e) {
            // pas grave
        }
        
        return c;
    }
}
