/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers.modules.admin;

import conf.JSP;
import controllers.Controller;
import controllers.modules.ModuleController;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.SessionModel;


public class AdminController extends ModuleController {
    private Map<String, ModuleController> subControllers = new HashMap<String, ModuleController>();


    public AdminController(Controller parent) {
        super(parent);

        initSubModules();
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!((SessionModel) request.getAttribute("session")).isLoggedIn()) {
            redirect("./connexion", "Vous devez être connecté pour accéder à cette page", request, response);
            return;
        }

        String section = request.getParameter("section");

        if(section != null && section.equals("index")) {
            doIndex(request, response);
            return;
        }

        ModuleController controller = subControllers.get(section);

        // pas de contrôleur trouvé = requête incorrect : pas la peine d'aller plus loin
        if(controller == null) {
            error("Erreur 404", request, response);
            return;
        }

        // on passe le relai au sous-contrôleur
        controller.handle(request, response);
    }

    private void initSubModules() {
        addSubModule("articles", new ArticlesController(getRootController()));
        addSubModule("categories", new CategoriesController(getRootController()));
        addSubModule("comments", new CommentsController(getRootController()));
    }

    protected final void addSubModule(String name, ModuleController ctrl) {
        if(subControllers.containsKey(name))
            throw new IllegalArgumentException("Ce module est déjà enregistré");

        subControllers.put(name, ctrl);
    }

    private void doIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("PAGE_TITLE", "Administration");
        forward(JSP.ADMIN_HOME, request, response);
    }
}