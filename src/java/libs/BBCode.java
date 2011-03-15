package libs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import metier.Smiley;


/**
 * Classe de gestion du BBCode et des smileys. Elle permet de transformer le
 * BBCode en HTML.
 */
public class BBCode {
    /**
     * Liste des smileys connus.
     */
    private static List<Smiley> smileys = new ArrayList<Smiley>();
    
    
    /**
     * Change la liste des smileys connus. La liste passée en paramètre ne sera
     * pas modifiée (ses éléments non plus).
     * 
     * @param smileys Nouvelle liste de smileys.
     */
    public static void setSmileys(List<Smiley> smileys) {
        BBCode.smileys = Collections.unmodifiableList(smileys);
    }
    
    /**
     * Remplace dans le texte passé en paramètre le BBCode par son équivalent
     * HTML.
     * 
     * @param input Texte en BBCode.
     * 
     * @return Equivalent en HTML.
     */
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
