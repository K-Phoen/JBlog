package controllers.modules.user;

import conf.JSP;
import controllers.Controller;
import controllers.modules.ModuleController;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import libs.form.Form;
import libs.form.fields.PasswordField;
import libs.form.fields.SubmitButton;
import libs.form.fields.TextField;
import models.SessionModel;


/**
 * Sous-contrôleur gérant la connexion et déconnexion.
 */
public class ConnectionController extends ModuleController {
    
    public ConnectionController(Controller parent) {
        super(parent);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if(action == null || action.equals("login"))
            doLogin(request, response);
        else
            doLogout(request, response);
    }

    /**
     * Page de login.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    private void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Form form = new Form();
        SessionModel mdl = (SessionModel) request.getAttribute("session");
        
        if(mdl.isLoggedIn()) {
            redirect("./", "Vous êtes déjà connecté", request, response);
            return;
        }
        
        form.add(new TextField("login").setLabel("Identifiant"));
        form.add(new PasswordField("pass").setLabel("Mot de passe"));
        form.add(new SubmitButton("Connexion"));
        
        request.setAttribute("form", form);
        request.setAttribute("PAGE_TITLE", "Connexion");
        
        if(request.getAttribute("HTTP_METHOD").equals("POST")) {
            form.bind(request);

            if(form.isValid()) {
                String login = request.getParameter("login");
                String pass = request.getParameter("pass");

                boolean auth = false;

                try {
                    auth = mdl.authenticate(login, pass, true);
                } catch (SQLException ex) {
                    error(ex.getMessage(), request, response);
                    return;
                }

                if(auth) {
                    redirect("./", "Connexion réussie !", request, response);
                    return;
                }

                form.triggerError(form.field("login"), "Identifiants incorrects.");
            }
        }
        
        forward(JSP.FORM, request, response);
    }

    /**
     * Page de déconnexion.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    private void doLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("PAGE_TITLE", "Déconnexion");
        
        SessionModel mdl = (SessionModel) request.getAttribute("session");
        
        if(!mdl.isLoggedIn()) {
            redirect("./", "Vous n'êtes pas connecté !", request, response);
            return;
        }
        
        mdl.logout();
        
        redirect("./", "Déconnexion réussie.", request, response);
    }
}
