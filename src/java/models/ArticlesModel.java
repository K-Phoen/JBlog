/*
 * Copyright (C) 2011 kevin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General public static License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General public static License for more details.
 *
 * You should have received a copy of the GNU General public static License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package models;

import db.factories.ArticlesFactory;
import db.factories.CommentsFactory;
import java.sql.SQLException;
import java.util.List;
import metier.Article;
import metier.Comment;


public class ArticlesModel {
    private static final int ARTICLES_PER_PAGE = 3;
    
    
    /**
     * Retourne les N derniers articles publiés (et valides).
     * 
     * @param page Page en cours de visualisation
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @return La liste des articles.
     */
    public static List<Article> getLasts(int page) throws SQLException, Exception {
        return ArticlesFactory.getNFirst(first(page), ARTICLES_PER_PAGE, true);
    }

    /**
     * Retourne les N derniers articles publiés (sans distinction).
     *
     * @param page Page en cours de visualisation
     *
     * @throws SQLException
     * @throws Exception
     *
     * @return La liste des articles.
     */
    public static List<Article> getAll(int page) throws SQLException, Exception {
        return ArticlesFactory.getNFirst(first(page), ARTICLES_PER_PAGE, false);
    }
    
    /**
     * Retourne les N derniers articles publiés (et valides) dans une catégorie.
     * 
     * @param cId Id de la catégorie à parcourir
     * @param page Page en cours de visualisation
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @return La liste des articles de la catégorie.
     */
    public static List<Article> getByCategorie(int cId, int page) throws SQLException, Exception {
        return ArticlesFactory.getNCategorie(cId, first(page), ARTICLES_PER_PAGE, true);
    }
    
    /**
     * Retourne les N derniers articles publiés (et valides) pour une recherche
     * donnée.
     * 
     * @param page Page en cours de visualisation
     * @param search Recherche en cours
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @return La liste des articles correspondants à la recherche.
     */
    public static List<Article> search(String search, int page) throws SQLException, Exception {
        return ArticlesFactory.getNSearch(search, first(page), ARTICLES_PER_PAGE, true);
    }
    
    public static int getNBPages() throws SQLException {
        return getNBPages(true);
    }

    public static int getNBPages(boolean justValid) throws SQLException {
        return (int) Math.ceil(ArticlesFactory.countArticles(justValid) / ((float)ARTICLES_PER_PAGE));
    }
    
    public static int getNBPagesSearch(String search) throws SQLException {
        return (int) Math.ceil(ArticlesFactory.countArticlesSearch(search, true) / ((float)ARTICLES_PER_PAGE));
    }
    
    public static int getNBPagesCategorie(int id) throws SQLException {
        return (int) Math.ceil(ArticlesFactory.countArticlesCategorie(id, true) / ((float)ARTICLES_PER_PAGE));
    }
    
    public static int getNBArticles(boolean valid) throws SQLException {
        return ArticlesFactory.countArticles(true);
    }
    
    public static int getNBComments(boolean valid) throws SQLException {
        return CommentsFactory.count(true);
    }

    /**
     * Retrouve un article en fonction de son slug.
     * 
     * @param slug Slug servant à identifier l'article.
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @return L'article s'il existe, null sinon.
     */
    public static Article get(String slug) throws SQLException, Exception {
        return ArticlesFactory.getBySlug(slug);
    }

    /**
     * Retrouve un article en fonction de son id.
     * 
     * @param id Identifiant servant à identifier l'article.
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @return L'article s'il existe, null sinon.
     */
    public static Article get(int id) throws SQLException, Exception {
        return ArticlesFactory.get(id);
    }

    /**
     * Enregistre un article.
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @param a Article à enregistrer
     */
    public static void save(Article a) throws SQLException, Exception {
        ArticlesFactory.save(a);
    }
    
    public static void delete(int id) throws SQLException {
        CommentsFactory.deleteFromArticle(id);
        ArticlesFactory.delete(id);
    }
    
    private static int first(int page) {
        return (page - 1) * ARTICLES_PER_PAGE;
    }
}
