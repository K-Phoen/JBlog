/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers.modules;

import controllers.Controller;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class ModuleController {
    Controller ctrl;
    
    public ModuleController(Controller parentCtrl) {
        this.ctrl = parentCtrl;
    }
    
    public abstract void doGet(HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException;
    
    public abstract void doPost(HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException;
    
    
    public final void forward(String to, HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        ctrl.forward(to, request, response);
    }

    public final void error(HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        ctrl.error(request, response);
    }

    protected final void error(String msg, HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        ctrl.error(msg, request, response);
    }
}
