/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers.modules;

import conf.JSP;
import controllers.Controller;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import libs.form.Form;
import libs.form.fields.EmailField;
import libs.form.fields.SubmitButton;
import libs.form.fields.TextArea;
import libs.form.fields.TextField;
import metier.Article;
import models.ArticlesModel;


public class ArticlesController extends ModuleController {
    
    public ArticlesController(Controller parent) {
        super(parent);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String slug = request.getParameter("slug");
        
        // page d'accueil
        if(slug == null || slug.trim().isEmpty())
            doIndex(request, response);
        else
            doArticlePage(slug.trim(), request, response);
    }
    
    /**
     * Page d'accueil du site. On récupère la racine du répertoire à explorer
     * et on l'affiche.
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

        try {
            elems = mdl.getLasts();
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }

        request.setAttribute("elems", elems);

        forward(JSP.INDEX, request, response);
    }

    private void doArticlePage(String slug, HttpServletRequest request, HttpServletResponse response)
                 throws ServletException, IOException {
        Article a;
        ArticlesModel mdl = new ArticlesModel();

        try {
            a = mdl.getBySlug(slug);
            
            if(a == null)
                throw new Exception("Article inconnu");
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }

        // formulaire d'ajout de commentaire
        Form form = createCommentForm();

        if(request.getAttribute("HTTP_METHOD").equals("POST")) {
            form.bind(request);

            if(form.isValid()) {
                error("Impossible d'enregistrer le commentaire", request, response);
                return;
            }
        }

        request.setAttribute("form", form);
        request.setAttribute("article", a);

        forward(JSP.ARTICLE, request, response);
    }

    private Form createCommentForm() {
        Form form = new Form();

        form.add(new TextField("nom").setLabel("Nom"));
        form.add(new EmailField("mail").setLabel("Mail"));
        form.add(
                    new TextArea("comment")
                    .cols("50%")
                    .setMinLength(10)
                    .setLabel("Commentaire")
                );
        form.add(new SubmitButton("Envoyer"));

        return form;
    }
}
