/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers.modules.admin;

import conf.JSP;
import controllers.Controller;
import controllers.modules.ModuleController;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.SessionModel;


public class AdminController extends ModuleController {
    
    public AdminController(Controller parent) {
        super(parent);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act = request.getParameter("act");
        
        if(!((SessionModel) request.getAttribute("session")).isLoggedIn()) {
            redirect("./connexion", "Vous devez être connecté pour accéder à cette page", request, response);
            return;
        }
        
        if(act == null || act.isEmpty()) {
            error("Page inconnue", request, response);
            return;
        }
        
        if(act.equals("index"))
            doIndex(request, response);
        else
            error("Page inconnue", request, response);
    }

    private void doIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("PAGE_TITLE", "Administration");
        forward(JSP.ADMIN_HOME, request, response);
    }
}