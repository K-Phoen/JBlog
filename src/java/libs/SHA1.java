/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package libs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SHA1 { 
    private static String convertToHex(byte[] data) { 
        StringBuilder buf = new StringBuilder();
        
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
 
    public static String hash(String text) { 
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            
        } catch (NoSuchAlgorithmException ex) {
            return text;
        }
        
        md.update(text.getBytes(), 0, text.length());
        
        return convertToHex(md.digest());
    } 
} 
