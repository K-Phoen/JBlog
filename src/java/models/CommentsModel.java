package models;

import db.factories.ArticlesFactory;
import db.factories.CommentsFactory;
import java.sql.SQLException;
import java.util.List;
import metier.Article;
import metier.Comment;


public class CommentsModel {
    private static final int COMMENTS_PER_PAGE = 5;
    
    public static List<Comment> getAll(int page) throws SQLException, Exception {
        return CommentsFactory.getNFirst(first(page), COMMENTS_PER_PAGE);
    }

    public static Comment get(int id) throws SQLException, Exception {
        return CommentsFactory.get(id);
    }
    
    /**
     * Enregistre un nouveau commentaire.
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @param a Article dans lequel a été posté le commentaire. Son nombre de
     *          commentaires sera mis à jour.
     * @param c Commentaire à enregistrer.
     */
    public static void saveNew(Article a, Comment c) throws SQLException, Exception {
        // mise à ajour de l'article
        a.addComment(c);
        c.setaID(a.getId());
                
        CommentsModel.save(c);
    }

    public static void save(Comment c) throws SQLException, Exception {
        CommentsFactory.save(c);
        ArticlesFactory.fixNBComs(c.getaID());
    }

    public static void delete(Comment c) throws SQLException {
        CommentsFactory.delete(c.getId());
        ArticlesFactory.fixNBComs(c.getaID());
    }
    
    public static int getNBPages() throws SQLException {
        return (int) Math.ceil(CommentsFactory.count(false) / ((float) COMMENTS_PER_PAGE));
    }
    
    private static int first(int page) {
        return (page - 1) * COMMENTS_PER_PAGE;
    }
}
