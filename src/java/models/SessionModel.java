/*
 * Copyright (C) 2011 kevin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package models;

import db.factories.UsersFactory;
import java.sql.SQLException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import metier.User;


public class SessionModel {
    private User currentUser = null;
    
    
    public boolean authenticate(String login, String pass, HttpServletRequest request) throws ClassNotFoundException, SQLException {
        User u = UsersFactory.get(login, pass);
        
        if(u == null)
            return false;
        
        connect(u, request);
        
        return true;
    }
    
    public boolean authenticate(String login, String pass, HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException {
        if(!authenticate(login, pass, request))
            return false;
       
        Cookie c = new Cookie("login", String.format("%s||%s", login, pass));
        c.setMaxAge(3600 * 24 * 30); // 30 jours
        
        response.addCookie(c);
        
        return true;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void tryConnect(HttpServletRequest request) throws ClassNotFoundException, SQLException {
        HttpSession session = request.getSession();
        Cookie authCookie = null;
        
        for(Cookie c : request.getCookies()) {
            if(c.getName().equals("login"))
                authCookie = c;
        }
        
        if(session.getAttribute("currentUserId") != null)
            connect((int) session.getAttribute("currentUserId"), request);
        else if(authCookie != null) {
            String[] data = authCookie.getValue().split("||");
            
            if(data.length == 2)
                authenticate(data[0], data[1], request);
        }
    }
    
    private void connect(User u, HttpServletRequest request) {
        if(u == null)
            throw new IllegalArgumentException("Impossible de connecter un utilisateur valant null");
        
        currentUser = u;
            
        HttpSession session = request.getSession();
        session.setAttribute("currentUserId", u.getId());
    }

    private void connect(int id, HttpServletRequest request) throws ClassNotFoundException, SQLException {
        User u = UsersFactory.get(id);
        
        if(u != null)
            connect(u, request);
    }
}
