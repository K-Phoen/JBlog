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
import libs.form.fields.BooleanField;
import libs.form.fields.SubmitButton;
import libs.form.fields.TextArea;
import metier.Comment;
import models.ArticlesModel;


public class CommentsController extends ModuleController {
    
    public CommentsController(Controller parent) {
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
        List<Comment> elems;
        ArticlesModel mdl = new ArticlesModel();
        
        int page = getCurrentPage(request);
        int nbPages = -1;

        try {
            elems = mdl.getAllComments(page);
            nbPages = mdl.getNBPagesComments();
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }

        request.setAttribute("elems", elems);
        request.setAttribute("PAGE_TITLE", "Gestion des commentaires");

        if(page > 1)
            request.setAttribute("PREV_PAGE", page - 1);
        if(page < nbPages)
            request.setAttribute("NEXT_PAGE", page + 1);
        
        forward(JSP.ADMIN_LIST_COMMENTS, request, response);
    }

    private void doEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Comment c = new Comment();
        ArticlesModel mdl = new ArticlesModel();

        // création du formulaire
        Form form = new Form();
        form.add(new TextArea("contenu").setLabel("Contenu"));
        form.add(new BooleanField("valid").setLabel("Valide").required(false));
        form.add(new SubmitButton("Envoyer"));

        // si on a demandé l'édition d'un article, on le charge
        if(request.getParameter("id") != null) {
            int id = Integer.parseInt(request.getParameter("id"));
            
            try {
                c = mdl.getComment(id);

                if(c == null)
                    throw new Exception("Le commentaire n'existe pas");
                
                form.field("contenu").setValue(c.getContent());
                ((BooleanField)form.field("valid")).setValue(c.isValid());
            } catch (Exception ex) {
                redirect("./admin/comments/", "Impossible de charger le commentaire demandé : "+ex.getMessage(), request, response);
                return;
            }
        }

        // traitement du formulaire
        if(request.getAttribute("HTTP_METHOD").equals("POST")) {
            form.bind(request);

            if(form.isValid()) {
                c.setContent(request.getParameter("contenu"));
                c.setValid(((BooleanField)form.field("valid")).isChecked());
                System.out.println("isValid :"+((BooleanField)form.field("valid")).isChecked());
                try {
                    mdl.saveComment(c);
                } catch (Exception ex) {
                    error("Erreur à la sauvegarde du commentaire : "+ex.getMessage(), request, response);
                    return;
                }

                redirect("./admin/comments/edit/"+c.getId()+"/", "Commentaire modifié !", request, response);
            }
        }

        // affichage de la page
        request.setAttribute("form", form);
        request.setAttribute("PAGE_TITLE", "Edition d'un commentaire");

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
            redirect("./admin/comments/", "Requête incorrecte", request, response);
            return;
        }
        
        try {
            Comment c = mdl.getComment(id);

            if(c == null)
                throw new Exception("Le commentaire n'existe pas");
            
            mdl.deleteComment(c);
            redirect("./admin/comments/", "Commentaire supprimé.", request, response);
        } catch (Exception ex) {
            redirect("./admin/v/", "Erreur à la suppression du commentaire : "+ex.getMessage(), request, response);
        }
    }
}
