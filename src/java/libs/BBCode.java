/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class BBCode {
    private static Map<String, String> smileys = new HashMap<String, String>();
    
    public static void setSmileys(Map<String, String> smileys) {
        BBCode.smileys = Collections.unmodifiableMap(smileys);
    }
    
    public static String parse(String input) {
        // italique
        String text = input.replaceAll("\\[i\\](.+?)\\[/i\\]", "<em>$1</em>");
        // gras
        text = text.replaceAll("\\[b\\](.+?)\\[/b\\]", "<strong>$1</strong>");
        // souligné
        text = text.replaceAll("\\[u\\](.+?)\\[/u\\]", "<u>$1</u>");
        // barré
        text = text.replaceAll("\\[s\\](.+?)\\[/s\\]", "<strike>$1</strike>");
        
        // remplacement des smileys
        String html = "<img src=\"./smileys/%s\" alt=\"%s\" />";
        for(String smiley : smileys.keySet())
            text = text.replaceAll(Pattern.quote(smiley), String.format(html, smileys.get(smiley), smiley));

        // sauts de ligne
        text = text.replaceAll("\n", "<br />");

        return text;
    }
}
