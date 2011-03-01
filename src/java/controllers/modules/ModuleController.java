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
    
    public abstract void handle(HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException;

    
    protected final void redirect(String to, String msg, HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        ctrl.redirect(to, msg, request, response);
    }
    
    protected final void forward(String to, HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        ctrl.forward(to, request, response);
    }

    protected final void error(HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        ctrl.error(request, response);
    }

    protected final void error(String msg, HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        ctrl.error(msg, request, response);
    }
}
