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
    public static void get(Article a) throws ClassNotFoundException, SQLException, Exception {
        Connexion con = Connexion.getInstance();
        List<Comment> comments = new ArrayList<Comment>();
        
        
        String sql = "SELECT coID, a_ID, pseudo, mail, content, date, valide "+
                     "FROM commentaires WHERE a_ID = ? ORDER BY coID DESC";
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, a.getId());

        ResultSet res = stmt.executeQuery();
        while(res.next())
            comments.add(resultToComment(res));
        
        res.close();
        stmt.close();

        a.setComments(comments);
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
