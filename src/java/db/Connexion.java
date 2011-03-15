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

package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


/**
 * Classe de gestion de la connexion à la base de données
 */
public final class Connexion {
    /**
     * Unique instance de la classe
     */
    private static Connexion instance;
    
    /**
     * Connexion à la DB
     */
    private static Connection con;
    
    /*
     * Paramètres de la connexion
     */
    private static String url;
    private static String driver = "com.mysql.jdbc.Driver";
    private static String login;
    private static String pass;


    private Connexion() {
        if(con == null)
            throw new IllegalStateException("La méthode initConnexion doit être appelée avant d'utiliser la classe !");
    }

    /**
     * Exécute une requête de type INSERT ou UPDATE.
     * 
     * @param sql Code SQL à exécuter
     * @param params Paramètres de la requête SQL
     * 
     * @throws SQLException Si l'exécution de la requête cause une erreur.
     * 
     * @return Le résultat de "SELECT LAST_INSERT_ID()", ou -1
     */
    public int execute(String sql, Object ... params) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        bindParams(stmt, params);

        stmt.execute();
        
        int id = -1;
        try {
            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");

            if (rs.next())
                id = rs.getInt(1);
        } catch (SQLException e) {
            
        }
        stmt.close();
        
        return id;
    }
    
    /**
     * Crée une requête préparée
     * 
     * @param sql Code SQL de la requête.
     * 
     * @throws SQLException 
     * 
     * @return L'instance de l'objet représentant la requête
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return con.prepareStatement(sql);
    }

    /**
     * Lie les valeurs passées en paramètre à la requête préparée.
     * 
     * @param stmt
     * @param params
     * 
     * @throws SQLException 
     */
    public static void bindParams(PreparedStatement stmt, Object ... params) throws SQLException {
        for(int i=1; i <= params.length; ++i)
            stmt.setObject(i, params[i-1]);
    }
    
    /**
     * Lie les valeurs passées en paramètre à la requête préparée.
     * 
     * @param stmt
     * @param params
     * 
     * @throws SQLException 
     */
    public static void bindParams(PreparedStatement stmt, List<Object> params) throws SQLException {
        for(int i=1; i <= params.size(); ++i)
            stmt.setObject(i, params.get(i-1));
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        con.close();
    }


    public static void setDriver(String driver) {
        Connexion.driver = driver;
    }

    public static void setUrl(String url) {
        Connexion.url = url;
    }

    public static void setCredentials(String login, String pass) {
        setLogin(login);
        setPass(pass);
    }

    public static void setLogin(String login) {
        Connexion.login = login;
    }

    public static void setPass(String pass) {
        Connexion.pass = pass;
    }

    public static Connexion getInstance() {
        if(instance == null)
            instance = new Connexion();

        return instance;
    }
    
    public static void initConnexion() throws ClassNotFoundException, SQLException {
        if(con != null)
            return;
        
        Class.forName(driver);
        con = DriverManager.getConnection(url, login, pass);
    }
}
