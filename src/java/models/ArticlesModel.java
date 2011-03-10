/*
 * Copyright (C) 2011 kevin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package models;

import db.factories.ArticlesFactory;
import db.factories.CategoryFactory;
import db.factories.CommentsFactory;
import java.sql.SQLException;
import java.util.List;
import metier.Article;
import metier.Category;
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
    public List<Article> getLasts(int page) throws SQLException, Exception {
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
    public List<Article> getAll(int page) throws SQLException, Exception {
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
    public List<Article> getLastsCategorie(int cId, int page) throws SQLException, Exception {
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
    public List<Article> search(String search, int page) throws SQLException, Exception {
        return ArticlesFactory.getNSearch(search, first(page), ARTICLES_PER_PAGE, true);
    }
    
    public int getNBPages() throws SQLException {
        return getNBPages(true);
    }

    public int getNBPages(boolean justValid) throws SQLException {
        return ArticlesFactory.countArticles(justValid) / ARTICLES_PER_PAGE;
    }
    
    public int getNBPagesSearch(String search) throws SQLException {
        return ArticlesFactory.countArticlesSearch(search, true) / ARTICLES_PER_PAGE;
    }
    
    public int getNBPagesCategorie(int id) throws SQLException {
        return ArticlesFactory.countArticlesCategorie(id, true) / ARTICLES_PER_PAGE;
    }
    
    public int getNBArticles(boolean valid) throws SQLException {
        return ArticlesFactory.countArticles(true);
    }
    
    public int getNBComments(boolean valid) throws SQLException {
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
    public Article getBySlug(String slug) throws SQLException, Exception {
        return ArticlesFactory.getBySlug(slug);
    }

    public Article get(int id) throws SQLException, Exception {
        return ArticlesFactory.get(id);
    }
    
    /**
     * Enregistre un commentaire.
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @param a Article dans lequel a été posté le commentaire. Son nombre de
     *          commentaires sera mis à jour.
     * @param c Commentaire à enregistrer.
     */
    public void saveComment(Article a, Comment c) throws SQLException, Exception {
        // mise à ajour de l'article
        a.addComment(c);
        saveArticle(a);
                
        CommentsFactory.save(c);
    }

    /**
     * Enregistre un article.
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @param a Article à enregistrer
     */
    public void saveArticle(Article a) throws SQLException, Exception {
        ArticlesFactory.save(a);
    }
    
    public void saveCategorie(Category c) throws SQLException {
        CategoryFactory.save(c);
    }
    
    /**
     * Retourne les catégories disponibles
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @return La liste des catégories du blog.
     */
    public List<Category> getCategories() throws SQLException, Exception {
        return CategoryFactory.getAll();
    }
    
    public Category getCategorie(String slug) throws SQLException, Exception {
        return CategoryFactory.get(slug);
    }
    
    public Category getCategorie(int id) throws SQLException {
        return CategoryFactory.get(id);
    }
    
    private int first(int page) {
        return (page - 1) * ARTICLES_PER_PAGE;
    }

    public void deleteArticle(int id) throws SQLException {
        CommentsFactory.deleteFromArticle(id);
        ArticlesFactory.delete(id);
    }

    public void deleteCategorie(int id) throws SQLException {
        if(ArticlesFactory.NBInCategory(id) != 0)
            throw new IllegalStateException("Cette catégorie contient des articles !");
        
        CategoryFactory.delete(id);
    }
}
