/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers.modules.admin;

import conf.JSP;
import controllers.Controller;
import controllers.modules.ModuleController;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import libs.form.Form;
import libs.form.fields.SubmitButton;
import libs.form.fields.TextField;
import metier.Category;
import models.ArticlesModel;


public class CategoriesController extends ModuleController {
    
    public CategoriesController(Controller parent) {
        super(parent);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act = request.getParameter("act");
        
        if(act == null || act.isEmpty()) {
            error("Page inconnue", request, response);
            return;
        }
        
        if(act.equals("index"))
            doIndex(request, response);
        else if(act.equals("edit"))
            doEdit(request, response);
        else if(act.equals("delete"))
            doDelete(request, response);
        else
            error("Page inconnue", request, response);
    }
    
    
    private void doIndex(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        List<Category> elems;
        ArticlesModel mdl = new ArticlesModel();
        
        try {
            elems = mdl.getCategories();
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }

        request.setAttribute("elems", elems);
        request.setAttribute("PAGE_TITLE", "Gestion des Catégories");

        forward(JSP.ADMIN_LIST_CATEGORIES, request, response);
    }

    private void doEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Category c = new Category();
        ArticlesModel mdl = new ArticlesModel();

        // création du formulaire
        Form form = new Form();
        form.add(new TextField("nom").setLabel("Nom"));
        form.add(new SubmitButton("Envoyer"));

        // si on a demandé l'édition d'un article, on le charge
        if(request.getParameter("id") != null) {
            int id = Integer.parseInt(request.getParameter("id"));
            
            try {
                c = mdl.getCategorie(id);

                if(c == null)
                    throw new Exception("La catégorie n'existe pas.");
                
                form.field("nom").setValue(c.getTitle());
            } catch (Exception ex) {
                redirect("./admin/categories/", "Impossible de charger la catégorie demandée : "+ex.getMessage(), request, response);
                return;
            }
        }

        // traitement du formulaire
        if(request.getAttribute("HTTP_METHOD").equals("POST")) {
            form.bind(request);

            if(form.isValid()) {
                boolean wasNew = c.isNew();
                
                c.setTitle(request.getParameter("nom"));

                try {
                    mdl.saveCategorie(c);
                } catch (SQLException ex) {
                    error("Erreur à la création de l'article : "+ex.getMessage(), request, response);
                    return;
                }

                String msg = wasNew ? "Catégorie enregistrée !" : "Catégorie modifiée !";
                redirect("./admin/categories/edit/"+c.getId()+"/", msg, request, response);
            }
        }

        // affichage de la page
        request.setAttribute("form", form);
        request.setAttribute("PAGE_TITLE", c.isNew() ? "Nouvelle catégorie" : "Edition d'une catégorie");

        forward(JSP.FORM, request, response);
    }

    private void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = -1;
        ArticlesModel mdl = new ArticlesModel();
        
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            id  = -1;
        }
        
        if(id == -1) {
            redirect("./admin/categories/", "Requête incorrecte", request, response);
            return;
        }
        
        try {
            mdl.deleteCategorie(id);
            redirect("./admin/categories/", "Catégorie supprimée.", request, response);
        } catch (Exception ex) {
            redirect("./admin/categories/", "Erreur à la suppression de la catégorie : "+ex.getMessage(), request, response);
        }
    }
}
