/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import metier.Smiley;


public class BBCode {
    private static List<Smiley> smileys = new ArrayList<Smiley>();
    
    public static void setSmileys(List<Smiley> smileys) {
        BBCode.smileys = Collections.unmodifiableList(smileys);
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
        for(Smiley smiley : smileys)
            text = text.replaceAll(Pattern.quote(smiley.getCode()), smiley.toHTML());

        // sauts de ligne
        text = text.replaceAll("\n", "<br />");

        return text;
    }
}
