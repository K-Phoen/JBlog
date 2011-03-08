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

/**
 * Modèle gérant la session courante (connexion, déconnexion, authentification,
 * etc).
 */
public class SessionModel {
    private User currentUser = null;
    private HttpServletRequest request;
    private HttpServletResponse response;

    
    public SessionModel(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    /**
     * Tente d'authentifier un utilisateur. Et crée un cookie qui permettra de
     * le ré-authentifier quand la session sera détruite.
     * 
     * @param login Identifiant
     * @param pass Mot de passe (en clair)
     * @param cookie Doit-on créer un cookie ?
     * 
     * @throws SQLException Si une erreur survient à la vérification des identifiants
     * 
     * @return True si la connexion a réussie, false sinon.
     */
    public boolean authenticate(String login, String pass, boolean cookie) throws SQLException {
        User u = UsersFactory.get(login, User.hashPass(pass));
        
        if(u == null)
            return false;
        
        connect(u);
       
        if(cookie) {
            Cookie cLogin = new Cookie("login", login);
            cLogin.setMaxAge(3600 * 24 * 30); // 30 jours

            Cookie cPass = new Cookie("pass", User.hashPass(pass));
            cPass.setMaxAge(3600 * 24 * 30); // 30 jours

            response.addCookie(cLogin);
            response.addCookie(cPass);
        }
        
        return true;
    }
    
    /**
     * Déconnecte l'utilisateur courant.
     */
    public void logout() {
        currentUser = null;
        
        HttpSession session = request.getSession();
        session.invalidate();
        
        Cookie cLogin = new Cookie("login", "");
        cLogin.setMaxAge(-1);
        
        Cookie cPass = new Cookie("pass", "");
        cPass.setMaxAge(-1);
        
        response.addCookie(cLogin);
        response.addCookie(cPass);
    }
    
    /**
     * Indique si l'utilisateur courant est connecté.
     * 
     * @return L'état de la session.
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Tente une connexion automatique de l'utilisateur courant, d'abord en
     * utilisant la session, puis en utilisant les cookies.
     * 
     * @throws SQLException 
     */
    public void tryConnect() throws SQLException {
        HttpSession session = request.getSession();
        Cookie loginCookie = null;
        Cookie passCookie = null;
        
        // pas de cookies disponibles
        if(request.getCookies() == null)
            return;

        for(Cookie c : request.getCookies()) {
            if(c.getName().equals("login"))
                loginCookie = c;
            else if(c.getName().equals("pass"))
                passCookie = c;
        }
        
        
        if(session.getAttribute("currentUserId") != null)
            connect(((Integer) session.getAttribute("currentUserId")).intValue());
        else if(loginCookie != null && passCookie != null) {
            User u = UsersFactory.get(loginCookie.getValue(), passCookie.getValue());
            
            if(u != null)
                connect(u);
        }
    }
    
    /**
     * Retourne l'utilisateur actuellement connecté (ou null s'il ne l'est pas).
     * 
     * @return L'object représentant l'utilisateur.
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Le nom de l'utilisateur courant. On utilise le cookie "displayName" et s'il 
     * n'existe pas on retourne une chaine vide.
     * 
     * @return Le nom de l'utilisateur courant.
     */
    public String getName() {
        for(Cookie c : request.getCookies()) {
            if(c.getName().equals("displayName"))
                return c.getValue();
        }
        
        return "";
    }
    
    /**
     * Le mail de l'utilisateur courant. On utilise le cookie "mail" et s'il 
     * n'existe pas on retourne une chaine vide.
     * 
     * @return Le mail de l'utilisateur courant.
     */
    public String getMail() {
        for(Cookie c : request.getCookies()) {
            if(c.getName().equals("mail"))
                return c.getValue();
        }
        
        return "";
    }
    
    public void saveMail(String mail) {
        Cookie c = new Cookie("mail", mail);
        c.setMaxAge(3600 * 24 * 30); // 30 jours

        response.addCookie(c);
    }

    public void saveName(String name) {
        Cookie c = new Cookie("displayName", name);
        c.setMaxAge(3600 * 24 * 30); // 30 jours

        response.addCookie(c);
    }
    
    /**
     * Connecte un utilisateur.
     * 
     * @param u Utilisateur à connecter.
     */
    private void connect(User u) {
        if(u == null)
            throw new IllegalArgumentException("Impossible de connecter un utilisateur valant null");
        
        currentUser = u;
            
        HttpSession session = request.getSession();
        session.setAttribute("currentUserId", new Integer(u.getId()));
    }

    /**
     * Connecte un utilisateur.
     * 
     * @param id Identifiant de l'utilisateur à connecter.
     */
    private void connect(int id) throws SQLException {
        User u = UsersFactory.get(id);
        
        if(u != null)
            connect(u);
    }
}
