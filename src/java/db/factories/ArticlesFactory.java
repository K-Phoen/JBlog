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
    /**
     * Retourne la liste des nb articles à partir du numéro first.
     * 
     * @param first Position du premier article à retourner
     * @param nb Nombre d'articles à retourner à partir de cette position
     * @param valid Les articles doivent-ils être valides ?
     * 
     * @throws SQLException Si une erreur survient lors de l'exécution de la 
     *                      requête?
     * @throws Exception Si la transformation d'une ligne de résultat en article
     *                   cause une erreur.
     * 
     * @return La liste des articles
     */
    public static List<Article> getNFirst(int first, int nb, boolean valid) throws SQLException, Exception {
        Map<String, List<Object>> limiters = new HashMap<String, List<Object>>();
        
        if(valid)
            limiters.put("valid = ?", toList(1));
        
        return getList(first, nb, limiters);
    }
    
    /**
     * Retourne la liste des nb articles à partir du numéro first, correspondants
     * à une recherche.
     * 
     * @param search Termes de la recherche
     * @param first Position du premier article à retourner
     * @param nb Nombre d'articles à retourner à partir de cette position
     * @param valid Les articles doivent-ils être valides ?
     * 
     * @throws SQLException Si une erreur survient lors de l'exécution de la 
     *                      requête?
     * @throws Exception Si la transformation d'une ligne de résultat en article
     *                   cause une erreur.
     * 
     * @return La liste des articles correspondant à la recherche
     */
    public static List<Article> getNSearch(String search, int first, int nb, boolean valid) throws SQLException, Exception {
        Map<String, List<Object>> limiters = new HashMap<String, List<Object>>();
        String fSearch = String.format("%%%s%%", search);
        
        limiters.put("(a.title LIKE ? OR content LIKE ?)", toList(fSearch, fSearch));
        if(valid)
            limiters.put("valid = ?", toList(1));
        
        return getList(first, nb, limiters);
    }
    
    /**
     * Retourne la liste des nb articles à partir du numéro first, appartenant
     * à une catégorie.
     * 
     * @param categorie Identifiant de la catégorie
     * @param first Position du premier article à retourner
     * @param nb Nombre d'articles à retourner à partir de cette position
     * @param valid Les articles doivent-ils être valides ?
     * 
     * @throws SQLException Si une erreur survient lors de l'exécution de la 
     *                      requête?
     * @throws Exception Si la transformation d'une ligne de résultat en article
     *                   cause une erreur.
     * 
     * @return La liste des articles d'une catégorie
     */
    public static List<Article> getNCategorie(int categorie, int first, int nb, boolean valid) throws SQLException, Exception {
        Map<String, List<Object>> limiters = new HashMap<String, List<Object>>();
        
        limiters.put("c_ID = ?", toList(categorie));
        if(valid)
            limiters.put("valid = ?", toList(1));
        
        return getList(first, nb, limiters);
    }
    
    /**
     * Retourne un article à partir de son slug.
     * 
     * @param slug Slug de l'article.
     * 
     * @throws SQLException Si une erreur survient lors de l'exécution de la 
     *                      requête?
     * @throws Exception Si la transformation d'une ligne de résultat en article
     *                   cause une erreur.
     * 
     * @return L'article voulu.
     */
    public static Article getBySlug(String slug) throws SQLException, Exception {
        return getOne("a.slug = ?", slug);
    }

    /**
     * Retourne un article à partir de son id.
     * 
     * @param id Id de l'article.
     * 
     * @throws SQLException Si une erreur survient lors de l'exécution de la 
     *                      requête?
     * @throws Exception Si la transformation d'une ligne de résultat en article
     *                   cause une erreur.
     * 
     * @return L'article voulu.
     */
    public static Article get(int id) throws SQLException, Exception {
        return getOne("a.aID = ?", id);
    }
    
    /**
     * Retourne le nombre d'articles.
     * 
     * @param valid Indique on ne doit comptabiliser que les articles valides.
     * 
     * @throws SQLException Si une erreur survient lors de l'exécution de la 
     *                      requête?
     * 
     * @return Le nombre d'articles.
     */
    public static int countArticles(boolean valid) throws SQLException {
        String sql = "SELECT COUNT(1) as total "+
                     "FROM articles "+
                     (valid ? "WHERE valid = 1 " : "");
        
        return getTotal(sql);
    }
    
    /**
     * Retourne le nombre d'articles d'une catégorie.
     * 
     * @param id Identifiant de la catégorie
     * @param valid Indique on ne doit comptabiliser que les articles valides.
     * 
     * @throws SQLException Si une erreur survient lors de l'exécution de la 
     *                      requête?
     * 
     * @return Le nombre d'articles de cette catégorie.
     */
    public static int countArticlesCategorie(int id, boolean valid) throws SQLException {
        String sql = "SELECT COUNT(1) as total "+
                     "FROM articles "+
                     "WHERE c_ID = ? "+
                     (valid ? "AND valid = 1 " : "");
        
        return getTotal(sql, id);
    }
    
    /**
     * Retourne le nombre d'articles correspondants à une recherche.
     * 
     * @param search Termes de la recherche.
     * @param valid Indique on ne doit comptabiliser que les articles valides.
     * 
     * @throws SQLException Si une erreur survient lors de l'exécution de la 
     *                      requête?
     * 
     * @return Le nombre d'articles retournés par la recherche.
     */
    public static int countArticlesSearch(String search, boolean valid) throws SQLException {
        String fSearch = String.format("%%%s%%", search);
        
        String sql = "SELECT COUNT(1) as total "+
                     "FROM articles "+
                     "WHERE (title LIKE ? OR content LIKE ?) "+
                     (valid ? "AND valid = 1 " : "");
        
        return getTotal(sql, fSearch, fSearch);
    }
    
    /**
     * Met à jour le nombre de commentaires d'un article.
     * 
     * @param aID Article à corriger
     * 
     * @throws SQLException Si une erreur survient lors de l'exécution de la 
     *                      requête?
     */
    public static void fixNBComs(int aID) throws SQLException {
        Connexion con = Connexion.getInstance();
        String sql = "UPDATE articles SET nb_coms = (SELECT COUNT(1) FROM commentaires WHERE a_ID = ? AND valide = 1) "+
                     "WHERE aID = ?";
        
        con.execute(sql, aID, aID);
    }
    

    /**
     * Retourne une liste d'articles correspondant aux critères passés.
     * 
     * @param first Position du premier article à retourner
     * @param nb Nombre d'articles à retourner à partir de cette position
     * @param limiters Critères
     * 
     * @throws SQLException Si une erreur survient lors de l'exécution de la 
     *                      requête?
     * @throws Exception Si la transformation d'une ligne de résultat en article
     *                   cause une erreur.
     * 
     * @return Les articles.
     */
    private static List<Article> getList(int first, int nb, Map<String, List<Object>> limiters) throws SQLException, Exception {
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
    
    /**
     * Retourne un article correspondant à la clause passée en paramètre.
     * 
     * @param clause Clause permettant d'identifier l'article.
     * @param param Paramètre de cette clause
     * 
     * @throws SQLException Si une erreur survient lors de l'exécution de la 
     *                      requête?
     * @throws Exception Si la transformation d'une ligne de résultat en article
     *                   cause une erreur.
     * 
     * @return Le premier article correspondant à la clause.
     */
    private static Article getOne(String clause, Object param) throws SQLException, Exception {
        Connexion con = Connexion.getInstance();
        Article a = null;
        
        String sql = "SELECT aID, u_ID, c_ID, a.slug as a_slug, a.title as a_title, content, date, "+
                     "nb_coms, valid, uID, login, first_name, last_name, cID, "+
                     "c.slug, c.title "+
                     "FROM articles a "+
                     "LEFT JOIN users ON users.uID = a.u_ID "+
                     "LEFT JOIN categories c ON c.cID = a.c_ID "+
                     "WHERE "+clause;
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, param);

        ResultSet res = stmt.executeQuery();
        if(res.next())
            a = CommentsFactory.get(resultToArticle(res));
        
        res.close();
        stmt.close();
        
        return a;
    }
    
    private static int getTotal(String sql, Object ... params) throws SQLException {
        Connexion con = Connexion.getInstance();
        
        PreparedStatement stmt = con.prepareStatement(sql);
        Connexion.bindParams(stmt, toList(params));

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
        String sql = "DELETE FROM articles WHERE aID = ?";
        
        Connexion.getInstance().execute(sql, id);
    }
    
    
    private static void insert(Article a) throws SQLException, Exception {
        Connexion con = Connexion.getInstance();
        String sql = "INSERT INTO articles (u_ID, c_ID, slug, title, content, date, nb_coms, valid) "+
                     "VALUES (?, ?, ?, ?, ?, ?, 0, ?)";
        
        int id = con.execute(sql, a.getUId(), a.getCId(), a.getSlug(), a.getTitle(),
                             a.getContent(), a.dateToString("yyyy-MM-dd"), a.isValid());
        a.setId(id);
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
