package metier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Article extends Entity {
    private int id = 0;
    private String title;
    private String url;
    private String content;
    private Date date;
    private boolean valid;
    private int nbComs = 0;
    
    private List<Comment> comments;


    public Article() {
    }

    public Article(int id) {
        this.id = id;
    }
    
    public Article(int id, int nbComs) {
        this.id = id;
        this.nbComs = nbComs;
    }

    public int getId() {
        return id;
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
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
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
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
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

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    public List<Comment> getComments() {
        return comments;
    }
}
