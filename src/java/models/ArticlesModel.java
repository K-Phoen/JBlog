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

import db.factories.ArticlesFactory;
import db.factories.CategoryFactory;
import db.factories.CommentsFactory;
import java.sql.SQLException;
import java.util.List;
import metier.Article;
import metier.Category;
import metier.Comment;


public class ArticlesModel {
    private static final int ARTICLES_PER_PAGE = 1;
        
    public List<Article> getLasts(int page) throws SQLException, ClassNotFoundException, Exception {
        //int nbPages = ArticlesFactory.countArticles(true) / ARTICLES_PER_PAGE;
        int first = (page - 1) * ARTICLES_PER_PAGE;
        
        return ArticlesFactory.getN(first, ARTICLES_PER_PAGE, true);
    }

    public Article getBySlug(String slug) throws SQLException, ClassNotFoundException, Exception {
        return ArticlesFactory.getBySlug(slug);
    }
    
    public void saveComment(Comment c) throws ClassNotFoundException, SQLException, Exception {
        CommentsFactory.save(c);
    }

    public void saveArticle(Article a) throws ClassNotFoundException, SQLException, Exception {
        ArticlesFactory.save(a);
    }
    
    public List<Category> getCategories() throws ClassNotFoundException, SQLException, Exception
    {
        return CategoryFactory.getAll();
    }
}
