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
import java.sql.SQLException;


/**
 * Classe de gestion de la connexion à la base de données
 */
public final class Connexion {
    private static Connexion instance;
    
    private static String url;
    private static String driver = "com.mysql.jdbc.Driver";
    private static String login;
    private static String pass;

    private Connection con;


    private Connexion() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        con = DriverManager.getConnection(url, login, pass);
    }

    public void execute(String sql, Object ... params) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql);

        bindParams(stmt, (Object[]) params);

        stmt.execute();
        stmt.close();
    }
    
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return con.prepareStatement(sql);
    }

    public static void bindParams(PreparedStatement stmt, Object ... params) throws SQLException {
        for(int i=1; i <= params.length; ++i)
            stmt.setObject(i, params[i-1]);
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

    public static Connexion getInstance() throws ClassNotFoundException, SQLException {
        if(instance == null)
            instance = new Connexion();

        return instance;
    }
}
