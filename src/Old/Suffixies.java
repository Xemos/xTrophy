/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.

 */
package Old;

/*
*well right now theres 2 big issues
[7:48:59 PM] NEO: or small, its all about perspective
[7:49:11 PM] NEO: you cant switch to a tag with the same name if there both multi colored
[7:49:29 PM] NEO: like JUMP or <3
[7:49:32 PM] NEO: if you recall
[7:49:41 PM] NEO: and when you do /tt reset
[7:49:49 PM] NEO: it doesnt work
[7:50:03 PM] NEO: tt reset need to set the users suffix to &f
[7:50:09 PM] NEO: currently it jsut tried to delete it
[7:50:19 PM] NEO: not sure how you would handle the multi tag thing
[7:50:23 PM] NEO: what do you think?
*/


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;


/**
 *
 * @author Mathias
 */
public class Suffixies extends JavaPlugin{
public static final Logger log = Logger.getLogger("Minecraft");
public static PermissionManager PEX;
public static HashMap<String, ChatColor> colors = new HashMap<String, ChatColor>();

    public void onDisable() {
        log.info("[TrophyTags] Disabled");
    }

    public void onEnable() {
        setupColors();
        setupPEX();
        log.info("[TrophyTags] Enabled");
    }

    
    public void setupPEX(){
        Plugin p = getServer().getPluginManager().getPlugin("PermissionsEx");
        if(p != null){
            PEX = PermissionsEx.getPermissionManager();
        } else {
            log.info("[TrophyTags] PEX not found. Disabling");
            this.setEnabled(false);
        }
    }

    public void setupColors(){
        colors.put("&0", ChatColor.BLACK);
        colors.put("&1", ChatColor.DARK_BLUE);
        colors.put("&2", ChatColor.DARK_GREEN);
        colors.put("&3", ChatColor.DARK_AQUA);
        colors.put("&4", ChatColor.DARK_RED);
        colors.put("&5", ChatColor.DARK_PURPLE);
        colors.put("&6", ChatColor.GOLD);
        colors.put("&7", ChatColor.GRAY);
        colors.put("&8", ChatColor.DARK_GRAY);
        colors.put("&9", ChatColor.BLUE);
        colors.put("&a", ChatColor.GREEN);
        colors.put("&b", ChatColor.AQUA);
        colors.put("&c", ChatColor.RED);
        colors.put("&d", ChatColor.LIGHT_PURPLE);
        colors.put("&e", ChatColor.YELLOW);
        colors.put("&f", ChatColor.WHITE);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){ return true; }
        final Player player = (Player)sender;
        if(command.getName().equalsIgnoreCase("TT")){
            if(args.length == 0){
                player.sendMessage(ChatColor.GOLD + "~~ Trophy Tags - Commands ~~");
                player.sendMessage(ChatColor.GREEN + "/TT List");
                player.sendMessage(ChatColor.GREEN + "/TT Set [Tag]");
                player.sendMessage(ChatColor.GREEN + "/TT Top");
                player.sendMessage(ChatColor.GREEN + "/TT Check [PlayerName]");
                player.sendMessage(ChatColor.GREEN + "/TT Clear");
                return true;
            }
            if(args[0].equalsIgnoreCase("List")){
                List<String> suffixes = SManager.getSuffixes(player.getName());
                if(!suffixes.isEmpty()){
                    List<String> duplicates = new ArrayList<String>();
                    for(int i = 0; i < suffixes.size(); i++){
                        for(int j = 0; j < suffixes.size(); j++){
                            if(j == i){ continue; }
                            if(SManager.getRawSuffix(suffixes.get(i)).equalsIgnoreCase(SManager.getRawSuffix(suffixes.get(j))) && !suffixes.get(i).equalsIgnoreCase(suffixes.get(j))){
                                if(!duplicates.contains(suffixes.get(i))){ duplicates.add(suffixes.get(i)); }
                                if(!duplicates.contains(suffixes.get(j))){ duplicates.add(suffixes.get(j)); }
                            }
                        }
                    }
                    player.sendMessage(ChatColor.GOLD + "~~ Trophy Tags - Owned Tags ~~");
                    StringBuilder sb = new StringBuilder();
                    for(String s : suffixes){
                        ChatColor color = colors.get(s.substring(0, 2));
                        if(duplicates.contains(s)){
                            if((suffixes.indexOf(s) + 1) == suffixes.size()){
                                if(SManager.isMultiColor(s)){
                                    sb.append(SManager.colorfy(s) + ChatColor.WHITE + " (Multi)");
                                } else {
                                    sb.append(color + "[" + s.substring(2).toUpperCase() + "] " + ChatColor.WHITE + "(" + color + color.name() + ChatColor.WHITE + ")");
                                }
                            } else {
                                if(SManager.isMultiColor(s)){
                                    sb.append(SManager.colorfy(s) + ChatColor.WHITE + " (Multi), ");
                                } else {
                                    sb.append(color + "[" + s.substring(2).toUpperCase() + "] " + ChatColor.WHITE + "(" + color + color.name() + ChatColor.WHITE + "), ");
                                }
                            }
                        } else {
                            if((suffixes.indexOf(s) + 1) == suffixes.size()){
                                if(SManager.isMultiColor(s)){
                                    sb.append(SManager.colorfy(s));
                                } else {
                                    sb.append(color + "[" + s.substring(2).toUpperCase() + "]");
                                }
                            } else {
                                if(SManager.isMultiColor(s)){
                                    sb.append(SManager.colorfy(s) + ChatColor.WHITE + ", ");
                                } else {
                                    sb.append(color + "[" + s.substring(2).toUpperCase() + "]" + ChatColor.WHITE + ", ");
                                }
                            }
                        }
                    }
                    player.sendMessage(sb.toString());
                } else {
                    player.sendMessage(ChatColor.YELLOW + "You do not have any available Trophy Tags");
                }
            } else if(args[0].equalsIgnoreCase("Set")){ //TODO
                if(args.length == 1){
                    player.sendMessage(ChatColor.RED + "Too few arguments! /TT Set [Tag] [Color]");
                    player.sendMessage(ChatColor.YELLOW + "Note: [Color] argument is optional");
                    return true;
                }
                ChatColor color = null;
                if(args.length == 3){
                    if(args[2].substring(0,1).equals("&") && args[2].length() == 2){
                        color = colors.get(args[2]);
                    } else {
                        try {
                            color = ChatColor.valueOf(args[2].toUpperCase());
                        } catch(IllegalArgumentException e) {
                            if(!args[2].equalsIgnoreCase("multi")){
                                player.sendMessage("Invalid color. Defaulting to first available color");
                            }
                        }
                    }
                }
                List<String> suffixes = SManager.getSuffixes(player.getName());
                for(String suff : suffixes){
                    suff = suff.replace("[", "").replace("]", "");
                    if(SManager.getRawSuffix(suff).equalsIgnoreCase(args[1])){
                        if(color != null){
                            if(color == colors.get(suff.substring(0, 2)) && !SManager.isMultiColor(suff)){
                                PEX.getUser(player.getName()).setSuffix(colors.get(suff.substring(0, 2)) + "[" + suff.substring(2).toUpperCase() + "]", null);
                                player.sendMessage(ChatColor.WHITE + "New Tag: " + colors.get(suff.substring(0, 2)) + "[" + suff.substring(2).toUpperCase() + "]" + ChatColor.WHITE + " selected!");
                                return true;
                            }
                        } else {
                            if(args.length == 3 && args[2].equalsIgnoreCase("multi")){
                                if(!SManager.isMultiColor(suff)){ continue; }
                                PEX.getUser(player.getName()).setSuffix(suff.substring(0, 2).toLowerCase() + "[" + SManager.colorCaseFix(suff.substring(2).toUpperCase()) + suff.substring(0, 2).toLowerCase() + "]", null);
                                player.sendMessage(ChatColor.WHITE + "New Tag: " + SManager.colorfy(suff) + ChatColor.WHITE + " selected!");
                                return true;
                            } else {
                                if(SManager.isMultiColor(suff)){
                                    PEX.getUser(player.getName()).setSuffix(suff.substring(0, 2).toLowerCase() + "[" + SManager.colorCaseFix(suff.substring(2).toUpperCase()) + suff.substring(0, 2).toLowerCase() + "]", null);
                                    player.sendMessage(ChatColor.WHITE + "New Tag: " + SManager.colorfy(suff) + ChatColor.WHITE + " selected!");
                                    return true;
                                } else {
                                    PEX.getUser(player.getName()).setSuffix(colors.get(suff.substring(0, 2)) + "[" + suff.substring(2).toUpperCase() + "]", null);
                                    player.sendMessage(ChatColor.WHITE + "New Tag: " + colors.get(suff.substring(0, 2)) + "[" + suff.substring(2).toUpperCase() + "]" + ChatColor.WHITE + " selected!");
                                    return true;
                                }
                            }
                        }
                    }
                }
                player.sendMessage(ChatColor.RED + "Invalid or unowned Trophy Tag");
            } else if(args[0].equalsIgnoreCase("Check")){
                if(args.length == 1){
                    player.sendMessage(ChatColor.RED + "Too few arguments! /TT Check [PlayerName]");
                    return true;
                }
                List<String> suffixes;
                if(getServer().getPlayer(args[1]) != null){
                    suffixes = SManager.getSuffixes(getServer().getPlayer(args[1]).getName());
                } else {
                    suffixes = SManager.getSuffixes(args[1]);
                }
                if(!suffixes.isEmpty()){
                    player.sendMessage(ChatColor.GOLD + "~~ Trophy Tags - Showing Tags for " + args[1].toLowerCase() + "~~");
                    StringBuilder sb = new StringBuilder();
                    for(String s : suffixes){
                        ChatColor color = colors.get(s.substring(0, 2));
                        if((suffixes.indexOf(s) + 1) == suffixes.size()){
                            if(SManager.isMultiColor(s)){
                                sb.append(SManager.colorfy(s));
                            } else {
                                sb.append(color + "[" + s.substring(2).toUpperCase() + "]");
                            }
                        } else {
                            if(SManager.isMultiColor(s)){
                                sb.append(SManager.colorfy(s) + ChatColor.WHITE + ", ");
                            } else {
                                sb.append(color + "[" + s.substring(2).toUpperCase() + "]" + ChatColor.WHITE + ", ");
                            }
                        }
                    }
                    player.sendMessage(sb.toString());
                } else {
                    player.sendMessage(ChatColor.YELLOW + "That player does not have any available Trophy Tags");
                }
            } else if(args[0].equalsIgnoreCase("Top")){
                getServer().getScheduler().scheduleAsyncDelayedTask(this, new Runnable(){
                    public void run(){
                        List<PermissionUser> top10 = new ArrayList<PermissionUser>();
                        for(PermissionUser user : PEX.getUsers()){
                            if(top10.isEmpty()){
                                top10.add(user);
                                continue;
                            }
                            int amount = SManager.getSuffixes(user.getName()).size();
                            for(PermissionUser user1 : top10.subList(0, highIndex(top10))){
                                if(SManager.getSuffixes(user1.getName()).size() <= amount){
                                    top10.add(top10.indexOf(user1), user);
                                    break;
                                }
                            }
                        }
                        player.sendMessage(ChatColor.GOLD + "~~ Trophy Tags - Top 10 ~~");
                        for(int i = 0; i < 10; i++){
                            player.sendMessage((i + 1) + ". " + ChatColor.GOLD + top10.get(i).getName() + ChatColor.WHITE + " (" + ChatColor.GOLD + SManager.getSuffixes(top10.get(i).getName()).size() + " Trophy Tags" + ChatColor.WHITE + ")");
                        }
                    }
                });
            } else if(args[0].equalsIgnoreCase("clear")){
                PEX.getUser(player).setSuffix(null, null);
                player.sendMessage(ChatColor.GREEN + "Tag cleared");
            }
        }
        return true;
    }
    private int highIndex(List<?> list){
        if(list.size() >= 10){
            return 10;
        } else { return list.size(); }
    }
}
