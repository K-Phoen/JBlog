/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package db.factories;

import java.sql.ResultSet;
import java.sql.SQLException;
import metier.Category;


public class CategoryFactory {
    static Category resultToCategory(ResultSet res) throws SQLException {
        return new Category(res.getInt("cID"), res.getString("slug"), res.getString("title"));
    }
}
