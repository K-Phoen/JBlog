package metier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Article extends Entity {
    private int u_ID = 0;
    private int c_ID = 0;
    private String title;
    private String slug;
    private String content;
    private Date date;
    private boolean valid;
    private int nbComs = 0;
    
    private User author;
    private Category category;
    private List<Comment> comments;


    public Article() {
        super();
    }
    
    public Article(int id, int nbComs) {
        super(id);
        this.nbComs = nbComs;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
        setUId(author.getId());
    }
    
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        setCId(category.getId());
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
        setSlug(null);
    }

    /**
     * @return the url
     */
    public String getSlug() {
        if(slug == null || slug.isEmpty())
            slug = slugify(getTitle());

        return slug;
    }

    /**
     * @param url the url to set
     */
    public void setSlug(String url) {
        this.slug = url;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return (Date) date.clone();
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = (Date) date.clone();
    }

    public void setDate(String sDate) throws ParseException {
        setDate(sDate, "yyyy-MM-dd");
    }

    public void setDate(String sDate, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        setDate(sdf.parse(sDate));
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * @return the nbComs
     */
    public int getNbComs() {
        return comments == null ? nbComs : comments.size();
    }

    public String dateToString(String format) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    public String dateToString(String format, Locale locale) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(date);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void addComment(Comment c) {
        comments.add(c);
    }

    public void setUId(int uID) {
        u_ID = uID;
    }
    
    public void setCId(int cID) {
        c_ID = cID;
    }
    
    public int getUId() {
        return u_ID;
    }
    
    public int getCId() {
        return c_ID;
    }
}
