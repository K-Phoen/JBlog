/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers.modules.admin;

import conf.JSP;
import controllers.Controller;
import controllers.modules.ModuleController;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import libs.HTML;
import libs.form.Form;
import libs.form.fields.BooleanField;
import libs.form.fields.SelectField;
import libs.form.fields.SubmitButton;
import libs.form.fields.TextArea;
import libs.form.fields.TextField;
import metier.Article;
import metier.Category;
import models.ArticlesModel;
import models.SessionModel;


public class ArticlesController extends ModuleController {
    
    public ArticlesController(Controller parent) {
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
        else
            error("Page inconnue", request, response);
    }
    
    
    /**
     * Page d'accueil du site. On récupère la liste des derniers articles et on
     * l'affiche.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doIndex(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        List<Article> elems;
        ArticlesModel mdl = new ArticlesModel();
        
        int page = getCurrentPage(request);
        int nbPages = -1;

        try {
            elems = mdl.getAll(page);
            nbPages = mdl.getNBPages(false);
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }

        request.setAttribute("elems", elems);
        request.setAttribute("PAGE_TITLE", "Gestion des articles");

        if(page > 1)
            request.setAttribute("PREV_PAGE", page - 1);
        if(page < nbPages)
            request.setAttribute("NEXT_PAGE", page + 1);
        
        forward(JSP.ADMIN_LIST_ARTICLES, request, response);
    }

    private void doEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Article a = new Article();
        ArticlesModel mdl = new ArticlesModel();

        // récupération de la liste des catégories
        Map<String, String> categories = new HashMap<String, String>();
        try {
            for (Category c : mdl.getCategories())
                categories.put(String.valueOf(c.getId()), HTML.escape(c.getTitle()));
        } catch (Exception ex) {
            error("Impossible de lire la liste des catégories : "+ex.getMessage(), request, response);
            return;
        }

        // création du formulaire
        Form form = new Form();
        form.add(new TextField("titre").setLabel("Titre"));
        form.add(new SelectField("c_ID").addOptions(categories).setLabel("Catégorie"));
        form.add(new TextArea("contenu").setLabel("Contenu"));
        form.add(new BooleanField("valid").setLabel("Valide").required(false));
        form.add(new SubmitButton("Connexion"));

        // si on a demandé l'édition d'un article, on le charge
        if(request.getParameter("id") != null) {
            int id = Integer.parseInt((String) request.getParameter("id"));
            
            try {
                a = mdl.get(id);

                if(a == null)
                    throw new Exception("L'article n'existe pas");
                
                form.field("c_ID").setValue(String.valueOf(a.getCId()));
                form.field("titre").setValue(a.getTitle());
                form.field("contenu").setValue(a.getContent());
                ((BooleanField)form.field("valid")).setValue(a.isValid());
            } catch (Exception ex) {
                redirect("./admin/articles", "Impossible de charger l'article demandé : "+ex.getMessage(), request, response);
                return;
            }
        }

        // traitement du formulaire
        if(request.getAttribute("HTTP_METHOD").equals("POST")) {
            form.bind(request);

            if(form.isValid()) {
                boolean wasNew = a.isNew();
                
                a.setTitle(request.getParameter("titre"));
                a.setContent(request.getParameter("contenu"));
                a.setValid(((BooleanField)form.field("valid")).isChecked());
                a.setCId(Integer.parseInt(request.getParameter("c_ID")));
                
                if(a.isNew()) {
                    a.setAuthor(((SessionModel) request.getAttribute("session")).getCurrentUser());
                    a.setDate(new Date());
                }

                try {
                    mdl.saveArticle(a);
                } catch (Exception ex) {
                    error("Erreur à la création de l'article : "+ex.getMessage(), request, response);
                    return;
                }

                String msg = wasNew ? "Article enregistré !" : "Article modifié !";
                redirect("./admin/articles/edit/"+a.getId()+"/", msg, request, response);
            }
        }

        // affichage de la page
        request.setAttribute("form", form);
        request.setAttribute("PAGE_TITLE", a.isNew() ? "Nouvel article" : "Edition d'un article");

        forward(JSP.FORM, request, response);
    }
}
