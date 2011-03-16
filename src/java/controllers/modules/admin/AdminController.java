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


/**
 * Contrôleur principal de la partie admin. Il reprent le même rôle que le 
 * controleur de l'application au niveau de l'admin. Il vérifie les autorisations
 * avant de passer le relai à des sous-contrôleurs.
 * 
 *                                        AdminCtrl
 *                                       /  |   |  \
 *                                      /   /   |   \
 *                                     /   /    |    \
 *                                    /   /     |     \
 *                                   /   /      |      \
 *                                  /    |      |       \
 *                          Articles    Com.    Users.  Cat.
 */
public class AdminController extends ModuleController {
    private Map<String, ModuleController> subControllers = new HashMap<String, ModuleController>();


    public AdminController(Controller parent) {
        super(parent);

        initSubModules();
    }

    /**
     * Avant de passer le relai à un sous-contrôleur, on vérifie que l'user
     * courant a bien les droits nécessaires. Ensuite, soit il a demandé la page
     * d'accueil de l'admin, soit il a demandé une page gérée par un sous-module.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
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
        addSubModule("users", new UsersController(getRootController()));
    }

    protected final void addSubModule(String name, ModuleController ctrl) {
        if(subControllers.containsKey(name))
            throw new IllegalArgumentException("Ce module est déjà enregistré");

        subControllers.put(name, ctrl);
    }

    /**
     * Affiche la page d'accueil de l'administration.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    private void doIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("PAGE_TITLE", "Administration");
        forward(JSP.ADMIN_HOME, request, response);
    }
}