package models;

import db.factories.ArticlesFactory;
import db.factories.CategoryFactory;
import java.sql.SQLException;
import java.util.List;
import metier.Category;


public class CategoriesModel {
    /**
     * Retourne les catégories disponibles
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @return La liste des catégories du blog.
     */
    public static List<Category> getAll() throws SQLException, Exception {
        return CategoryFactory.getAll();
    }
    
    /**
     * Récupère une catégorie par son slug.
     * 
     * @param slug Slug servant à identifer la catégorie.
     * 
     * @throws SQLException
     * @throws Exception 
     * 
     * @return La catégorie
     */
    public static Category get(String slug) throws SQLException, Exception {
        return CategoryFactory.get(slug);
    }
    
    /**
     * Récupère une catégorie par son ID.
     * 
     * @param id Identifiant de la catégorie.
     * 
     * @throws SQLException 
     * 
     * @return La catégorie.
     */
    public static Category get(int id) throws SQLException {
        return CategoryFactory.get(id);
    }
    
    /**
     * Enregistre une catégorie.
     * 
     * @param c La catégorie.
     * 
     * @throws SQLException 
     */
    public static void save(Category c) throws SQLException {
        CategoryFactory.save(c);
    }
    
    /**
     * Supprime une catégorie. Si la catégorie à des articles, une
     * IllegalStateException est jetée.
     * 
     * @param id Identifiant de la catégorie.
     * 
     * @throws SQLException 
     */
    public static void delete(int id) throws SQLException {
        if(ArticlesFactory.countArticlesCategorie(id, false) != 0)
            throw new IllegalStateException("Cette catégorie contient des articles !");
        
        CategoryFactory.delete(id);
    }
}
