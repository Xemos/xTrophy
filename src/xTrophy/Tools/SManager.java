/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xTrophy.Tools;

import java.util.ArrayList;
import java.util.List;

import xTrophy.Main;


/**
 *
 * @author Sexy Xemos
 */
public class SManager {

    public static List<String> getSuffixes(String playerName){
        List<String> permissions = getPermissionStrings(playerName);
        List<String> suffixes = new ArrayList<String>();
        for(String perm : permissions){
            if(perm.contains("trophytags.")){
                suffixes.add(perm.replace("trophytags.", ""));
            }
        }
        return suffixes;
    }

    public static List<String> getPermissionStrings(String playerName){
        String[] aPerms = Main.PEX.getUser(playerName).getPermissions("world");
        List<String> permissions = new ArrayList<String>();
        for(String s : aPerms){
            if(!permissions.contains(s)){
                permissions.add(s);
            }
        }
        return permissions;
    }

    
   
    
    

}
