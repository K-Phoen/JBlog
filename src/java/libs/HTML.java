/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs;


public class HTML {
    public static String escape(String text) {
        return text.replaceAll("&", "&amp;")
                   .replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;");
    }
}
