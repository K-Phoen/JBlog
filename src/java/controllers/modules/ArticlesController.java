/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers.modules;

import conf.JSP;
import controllers.Controller;
import java.io.IOException;
import java.util.Date;
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
import metier.Comment;
import models.ArticlesModel;
import models.SessionModel;


public class ArticlesController extends ModuleController {
    
    public ArticlesController(Controller parent) {
        super(parent);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String slug = request.getParameter("slug");
        String act = request.getParameter("act");
        String search = request.getParameter("search");
        
        if(act != null && act.equals("search"))
            doSearchRedirect(request, response);
        else if(search != null && !search.isEmpty())
            doSearch(search, request, response);
        else if(slug == null || slug.isEmpty())
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
        
        int page = getCurrentPage(request);
        int nbPages = -1;

        try {
            elems = mdl.getLasts(page);
            nbPages = mdl.getNBPages();
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }

        displayArticles(elems, nbPages, request, response);
    }

    private void doArticlePage(String slug, HttpServletRequest request, HttpServletResponse response)
                 throws ServletException, IOException {
        Article a;
        ArticlesModel mdl = new ArticlesModel();
        SessionModel sessionMdl = (SessionModel) request.getAttribute("session");

        try {
            a = mdl.getBySlug(slug);
            
            if(a == null)
                throw new Exception("Article inconnu.");
            
            if(!a.isValid())
                throw new Exception("Cet article n'est pas valide.");
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }

        /* formulaire d'ajout de commentaire */
        Form form = new Form();

        // Nom
        if(!sessionMdl.isLoggedIn())
            form.add(new TextField("nom").setLabel("Nom").setValue(sessionMdl.getName()));
        
        // mail
        if(!sessionMdl.isLoggedIn())
            form.add(new EmailField("mail").setLabel("Mail").setValue(sessionMdl.getMail()));
        
        // commentaire
        form.add(
                    new TextArea("comment")
                    .cols("50%")
                    .rows("4")
                    .setLabel("Commentaire")
                );
        form.add(new SubmitButton("Envoyer"));

        if(request.getAttribute("HTTP_METHOD").equals("POST")) {
            form.bind(request);

            if(form.isValid()) {
                Comment c = new Comment();
                
                if(sessionMdl.isLoggedIn()) {
                    c.setAuthor(sessionMdl.getCurrentUser().getDisplayName());
                    c.setMail(sessionMdl.getCurrentUser().getMail());
                } else {
                    c.setAuthor(request.getParameter("nom"));
                    c.setMail(request.getParameter("mail"));
                    
                    sessionMdl.saveName(request.getParameter("nom"));
                    sessionMdl.saveMail(request.getParameter("mail"));
                }
                
                c.setContent(request.getParameter("comment"));
                c.setaID(a.getId());
                c.setValid(true);
                c.setDate(new Date());
                
                a.addComment(c);
                
                try {
                    mdl.saveComment(c);
                    mdl.saveArticle(a);
                } catch (Exception ex) {
                    error("Impossible d'enregistrer le commentaire : "+ex.getMessage(), request, response);
                }
                
                redirect("./article/"+a.getUrl(), "Commentaire enregistré", request, response);
                return;
            }
        }

        request.setAttribute("form", form);
        request.setAttribute("article", a);
        request.setAttribute("PAGE_TITLE", a.getTitle());

        forward(JSP.ARTICLE, request, response);
    }

    private void doSearchRedirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        
        if(search == null || search.trim().isEmpty()) {
            error("Recherche invalide", request, response);
            return;
        }
        
        redirect(String.format("./search/%s", search), "Recherche en cours ...",
                 request, response);
    }

    private void doSearch(String search, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Article> elems;
        ArticlesModel mdl = new ArticlesModel();
        
        int page = getCurrentPage(request);
        int nbPages = -1;

        try {
            elems = mdl.search(search, page);
            nbPages = mdl.getNBPagesSearch(search);
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }

        request.setAttribute("PAGE_TITLE", "Recherche ...");
        request.setAttribute("SEARCH", search);
        
        displayArticles(elems, nbPages, request, response);
    }
    
    private void displayArticles(List<Article> elems, int nbPages, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = getCurrentPage(request);

        request.setAttribute("elems", elems);
        
        if(page > 1)
            request.setAttribute("PREV_PAGE", page - 1);
        if(page < nbPages)
            request.setAttribute("NEXT_PAGE", page + 1);

        forward(JSP.INDEX, request, response);
    }
    
    private int getCurrentPage(HttpServletRequest request) {
        int page = 1;
        
        if(request.getParameter("page") != null) {
            String p = request.getParameter("page");
            
            try {
                page = Integer.parseInt(p);
            } catch(NumberFormatException e) {
                page = 1;
            }
            
            if(page <= 0)
                page = 1;
        }
        
        return page;
    }
}
