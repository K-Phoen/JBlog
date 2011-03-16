package models;

import db.factories.SmileysFactory;
import java.sql.SQLException;
import java.util.List;
import metier.Smiley;


public class SmileysModel {
    /**
     * Retourne la liste des smileys connus.
     * 
     * @throws SQLException Si une erreur survient lors de la récupération.
     * 
     * @return La liste des smileys.
     */
    public static List<Smiley> getAll() throws SQLException {
        return SmileysFactory.getAll();
    }
}
