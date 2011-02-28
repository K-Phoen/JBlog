package controllers;

import controllers.modules.ArticlesController;
import controllers.modules.ModuleController;
import conf.Blog;
import conf.JSP;
import db.Connexion;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Controller extends HttpServlet {
    private static final long serialVersionUID = 1L;
   
    private Map<String, ModuleController> modulesControllers = new HashMap<String, ModuleController>();

    
    @Override
    public void init() throws ServletException {
        Connexion.setCredentials("root", "");
        Connexion.setUrl("jdbc:mysql://localhost/jblog");
        
        initModules();
    }
    
    private void initModules() {
        addModule("articles", new ArticlesController(this));
    }
    
    protected final void addModule(String name, ModuleController ctrl) {
        if(modulesControllers.containsKey(name))
            throw new IllegalArgumentException("Ce module est déjà enregistré");
        
        modulesControllers.put(name, ctrl);
    }
    
    protected final ModuleController getModuleController(String name) {
        return modulesControllers.get(name);
    }
    
    
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ModuleController controller = getModuleController(request.getParameter("module"));
        
        if(controller == null) {
            error("Erreur 404", request, response);
            return;
        }
        
        controller.doGet(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ModuleController controller = getModuleController(request.getParameter("module"));

        if(controller == null) {
            error("Erreur 404", request, response);
            return;
        }

        controller.doPost(request, response);
    }
    
    
    
    private void defineViewVariables(HttpServletRequest request) {
        for(Field key : Blog.class.getFields()) {
            try {
				String val = null;
				val = key.get(val).toString();
                
                request.setAttribute(key.getName(), val);
			} catch (Exception e) {
				
			}
        }
    }
    
    public final void forward(String to, HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        defineViewVariables(request);
        getServletContext().getRequestDispatcher(to).forward(request, response);
    }

    public final void error(HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        error(null, request, response);
    }

    public final void error(String msg, HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        request.setAttribute("error_msg", msg);
        forward(JSP.ERROR, request, response);
    }
}
