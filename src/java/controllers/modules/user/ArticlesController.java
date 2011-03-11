/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers.modules.user;

import conf.Blog;
import conf.JSP;
import controllers.Controller;
import controllers.modules.ModuleController;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import libs.form.Form;
import libs.form.fields.EmailField;
import libs.form.fields.SubmitButton;
import libs.form.fields.TextArea;
import libs.form.fields.TextField;
import metier.Article;
import metier.Category;
import metier.Comment;
import models.ArticlesModel;
import models.SessionModel;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class ArticlesController extends ModuleController {
    
    public ArticlesController(Controller parent) {
        super(parent);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act = request.getParameter("act");
        
        if(act == null || act.isEmpty()) {
            error("Page inconnue", request, response);
            return;
        }
        
        if(act.equals("search_redirect"))
            doSearchRedirect(request, response);
        else if(act.equals("search"))
            doSearch(request, response);
        else if(act.equals("categorie"))
            doCategorie(request, response);
        else if(act.equals("index"))
            doIndex(request, response);
        else if(act.equals("show"))
            doArticlePage(request, response);
        else if(act.equals("rss"))
            doRSS(request, response);
        else
            error("Page inconnue", request, response);
    }
    
    
    /**
     * Affichage du flux RSS du site. Il reprend les articles affichés sur la
     * première page.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    public void doRSS(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Article> elems;
        
        try {
            elems = (new ArticlesModel()).getLasts(1);
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }
        
        /* début de la création du flux */
        Document flux = new Document();
        
        // élément racine
        Element root = new Element("rss");
        root.setAttribute("version", "2.0");
        
        // channel
        Element channel = new Element("channel");
            // titre
            Element title = new Element("title");
            title.setText(Blog.TITLE);
            channel.addContent(title);
            
            // description
            Element desc = new Element("description");
            desc.setText(""); // rien pour le moment
            channel.addContent(desc);
            
            // description
            Element lastDate = new Element("lastBuildDate");
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
            lastDate.setText(formatter.format(new Date()));
            channel.addContent(lastDate);
            
            // lien
            Element link = new Element("link");
            link.setText(Blog.BASE_URL);
            channel.addContent(link);
            
            // articles
            for(Article a : elems) {
                Element item = new Element("item");
                
                    Element titre = new Element("title");
                    titre.setText(a.getTitle());
                    item.addContent(titre);
                    
                    Element description = new Element("description");
                    description.setText(a.getContent());
                    item.addContent(description);
                    
                    try {
                        Element pubDate = new Element("pubDate");
                        pubDate.setText(a.dateToString("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH));
                        item.addContent(pubDate);
                    } catch (Exception e) { 
                        // pas grave
                    }
                    
                    // lien
                    Element url = new Element("link");
                    url.setText(String.format("%s/article/%s", Blog.BASE_URL, a.getSlug()));
                    item.addContent(url);
                
                channel.addContent(item);
            }
        
        root.addContent(channel);
        
        flux.setRootElement(root);
        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        sortie.output(flux, response.getOutputStream());
    }
    
    /**
     * Page d'accueil du site. On récupère la liste des derniers articles et on
     * l'affiche.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doIndex(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        List<Article> elems;
        ArticlesModel mdl = new ArticlesModel();
        
        int page = getCurrentPage(request);
        int nbPages = -1;

        try {
            elems = mdl.getLasts(page);
            nbPages = mdl.getNBPages();
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }

        displayArticles(elems, nbPages, request, response);
    }
    
    /**
     * Liste des articles d'une catégorie.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doCategorie(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String slug = request.getParameter("slug");
        
        if(slug == null || slug.isEmpty()) {
            error("Requête incorrecte", request, response);
            return;
        }
        
        List<Article> elems;
        Category c;
        ArticlesModel mdl = new ArticlesModel();
        
        int page = getCurrentPage(request);
        int nbPages = -1;

        try {
            c = mdl.getCategorie(slug);
            
            if(c == null)
                throw new Exception("Catégorie inconnue");
            
            elems = mdl.getByCategorie(c.getId(), page);
            nbPages = mdl.getNBPagesCategorie(c.getId());
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }
        
        request.setAttribute("PAGE_TITLE", c.getTitle());
        displayArticles(elems, nbPages, request, response);
    }

    private void doArticlePage(HttpServletRequest request, HttpServletResponse response)
                 throws ServletException, IOException {
        String slug = request.getParameter("slug");
        
        if(slug == null || slug.isEmpty()) {
            error("Requête incorrecte", request, response);
            return;
        }
        
        Article a;
        ArticlesModel mdl = new ArticlesModel();
        SessionModel sessionMdl = (SessionModel) request.getAttribute("session");

        try {
            a = mdl.getBySlug(slug);
            
            if(a == null)
                throw new Exception("Article inconnu.");
            
            if(!a.isValid())
                throw new Exception("Cet article n'est pas valide.");
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }

        /* formulaire d'ajout de commentaire */
        Form form = new Form();

        if(!sessionMdl.isLoggedIn()) {
            // Nom
            form.add(new TextField("nom").setLabel("Nom").setValue(sessionMdl.getName()));
            // mail
            form.add(new EmailField("mail").setLabel("Mail").setValue(sessionMdl.getMail()));
        }
        
        // commentaire
        form.add(
                    new TextArea("comment")
                    .cols("50%")
                    .rows("4")
                    .setLabel("Commentaire")
                );
        form.add(new SubmitButton("Envoyer"));

        /* Traitement du formulaire */
        if(request.getAttribute("HTTP_METHOD").equals("POST")) {
            form.bind(request);

            if(form.isValid()) {
                Comment c = new Comment();
                
                if(sessionMdl.isLoggedIn()) {
                    c.setAuthor(sessionMdl.getCurrentUser().getDisplayName());
                    c.setMail(sessionMdl.getCurrentUser().getMail());
                } else {
                    c.setAuthor(request.getParameter("nom"));
                    c.setMail(request.getParameter("mail"));
                    
                    sessionMdl.saveName(request.getParameter("nom"));
                    sessionMdl.saveMail(request.getParameter("mail"));
                }
                
                c.setContent(request.getParameter("comment"));
                c.setaID(a.getId());
                c.setValid(true);
                c.setDate(new Date());
                
                try {
                    mdl.newComment(a, c);
                } catch (Exception ex) {
                    error("Impossible d'enregistrer le commentaire : "+ex.getMessage(), request, response);
                    return;
                }
                
                redirect("./article/"+a.getSlug(), "Commentaire enregistré", request, response);
                return;
            }
        }

        request.setAttribute("form", form);
        request.setAttribute("article", a);
        request.setAttribute("PAGE_TITLE", a.getTitle());
        request.setAttribute("SHOW_BBCODE_JS", true);

        forward(JSP.ARTICLE, request, response);
    }

    private void doSearchRedirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        
        if(search == null || search.trim().isEmpty()) {
            error("Recherche invalide", request, response);
            return;
        }
        
        redirect(String.format("./search/%s", search), "Recherche en cours ...",
                 request, response);
    }

    private void doSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Article> elems;
        ArticlesModel mdl = new ArticlesModel();
        String search = request.getParameter("search");
        
        if(search == null || search.isEmpty()) {
            error("Requête incorrecte", request, response);
            return;
        }
        
        int page = getCurrentPage(request);
        int nbPages = -1;

        try {
            elems = mdl.search(search, page);
            nbPages = mdl.getNBPagesSearch(search);
        } catch(Exception e) {
            error(e.getMessage(), request, response);
            return;
        }

        request.setAttribute("PAGE_TITLE", "Recherche ...");
        request.setAttribute("SEARCH", search);
        
        displayArticles(elems, nbPages, request, response);
    }
    
    private void displayArticles(List<Article> elems, int nbPages, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = getCurrentPage(request);

        request.setAttribute("elems", elems);
        
        if(page > 1)
            request.setAttribute("PREV_PAGE", page - 1);
        if(page < nbPages)
            request.setAttribute("NEXT_PAGE", page + 1);

        forward(JSP.INDEX, request, response);
    }
}
