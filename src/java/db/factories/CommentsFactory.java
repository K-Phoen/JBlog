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
    
    public static void save(Comment c) throws SQLException, Exception {
        if(c.isNew())
            insert(c);
        else
            update(c);
    }
    
    
    private static void insert(Comment c) throws SQLException, Exception {
        Connexion con = Connexion.getInstance();
        String sql = "INSERT INTO commentaires (a_ID, pseudo, mail, content, date, valide) "+
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        con.execute(sql, c.getaID(), c.getAuthor(), c.getMail(), c.getContent(),
                    c.dateToString("yyyy-MM-dd HH:mm:ss"), c.isValid());
    }

    private static void update(Comment c) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private static Comment resultToComment(ResultSet res) throws SQLException, Exception {
        Comment c = new Comment(res.getInt("coID"));
        
        c.setAuthor(res.getString("pseudo"));
        c.setMail(res.getString("mail"));
        c.setContent(res.getString("content"));
        c.setDate(res.getString("date"));
        c.setValid(res.getBoolean("valide"));
        
        return c;
    }
}
