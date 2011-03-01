package metier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Comment extends Entity {
    private int aID = 0;
    private String author;
    private String mail;
    private String content;
    private Date date;
    private boolean valid = true;

    
    public Comment() {
    }
    
    public Comment(int id) {
        super(id);
    }

    /**
     * @return the aID
     */
    public int getaID() {
        return aID;
    }

    /**
     * @param aID the aID to set
     */
    public void setaID(int aID) {
        this.aID = aID;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
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
    
    public String dateToString(String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
