/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers.modules.admin;

import conf.JSP;
import controllers.Controller;
import controllers.modules.ModuleController;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metier.Article;
import models.ArticlesModel;


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

        if(page > 1)
            request.setAttribute("PREV_PAGE", page - 1);
        if(page < nbPages)
            request.setAttribute("NEXT_PAGE", page + 1);
        
        request.setAttribute("PAGE_TITLE", "toto");
        forward(JSP.ADMIN_LIST_ARTICLES, request, response);
    }
}
