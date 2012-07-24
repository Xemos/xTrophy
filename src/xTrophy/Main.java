/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.

 */
package xTrophy;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import xTrophy.Tools.SManager;
import xTrophy.Tools.Sorter;


public class Main extends JavaPlugin{
	private Main plugin;

	public Main(Main main){
		this.plugin = main;
		this.config = plugin.getConfig();
	}

	private FileConfiguration config = null;
	public static final Logger log = Logger.getLogger("Minecraft");
	public static PermissionManager PEX;
	public static HashMap<String, ChatColor> colors = new HashMap<String, ChatColor>();

    public void onDisable() {
        log.info("[TrophyTags] Disabled");
    }

    public void onEnable() {
        setupPEX();
        doUpdate();
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
    
    /*
                player.sendMessage(ChatColor.GREEN + "/TT List");
                player.sendMessage(ChatColor.GREEN + "/TT Set [Tag] [Color]");
                player.sendMessage(ChatColor.GREEN + "/TT Top");
                player.sendMessage(ChatColor.GREEN + "/TT Check [PlayerName]");
                player.sendMessage(ChatColor.GREEN + "/TT Clear");
     */
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
		if ((sender instanceof ConsoleCommandSender)) {

			sender.sendMessage("No commands from console pl0x!");
			return true;

		}
		
		Player player = (Player) sender;
		
    	
    	switch(args[0].toLowerCase()){
    		case "list":{
    			doList(player,  SManager.getSuffixes(player.getName()));
    		}
    		case "set":{
    			if(args.length == 3){
    				doSet(args[2], args[1], player, SManager.getSuffixes(player.getName()));
    			} else {
    				doSet(args[1], player, SManager.getSuffixes(player.getName()));
    	    		
    			}
    		}
    		case "top":{
    			if (args.length == 3){
    				if (args[2].matches("^[0-9]+$") && args[3].matches("^[0-9]+$")) {
    					if (Integer.getInteger(args[3]) < Integer.getInteger(args[2])) {
    						player.sendMessage(colorize("&6Proper use&f: &3/tt top &f[&3minimum&f] [&3maximum&f]"));
    						return true;
    					}
    					if (Integer.getInteger(args[3]) > 50 ||
    							Integer.getInteger(args[2]) > 50) {
    						player.sendMessage("You may not view the list past 50.");
							
    					}
    					doTop(player, args[2], args[3]);
    					return true;
    				}else{
    					player.sendMessage(colorize("&6Proper use&f: &3/tt top &f[&3minimum&f] [&3maximum&f]"));
    					return true;
    				}
    			}
    			if(args.length==2)
    					if(args[2].matches("^[0-9]+$")){
    						doTop(player,args[2]);
    					}else{
    						player.sendMessage(colorize("&6Proper use&f: &3/tt top &f[&3List Size&f]"));
        					return true;
    					}
    			doTop(player);
    			return true;
    		}
    		case "check":{
    			doCheck(player, args[1].toLowerCase());
    			return true;
    		}
    		case "clear":{
    			PEX.getUser(player.getName()).setSuffix(colorize("&f"), null);
    			return true;                
    		}
    		default:{
    			return doHelp(player);
    		}
    		  	
    	}
		    
    }
    
    // True = Color to & ~~ False = & to color
    private String colorTest(String input, Boolean type){
    	String output = null;
    	
    	if(type){
    	switch(input.toLowerCase()){
			case "black": output = "&0"; break;
    		case "navy": output = "&1"; break;
    		case "green": output = "&2"; break;
    		case "teal": output = "&f3"; break;
    		case "red": output = "&f4"; break;
    		case "purple": output = "&f5"; break;
    		case "gold": output = "&f6"; break;
    		case "silver": output = "&f7"; break;
    		case "grey": output = "&f8"; break;
    		case "blue": output = "&f9"; break;
    		case "lime": output = "&fa"; break;
    		case "aqua": output = "&fb"; break;
    		case "rose": output = "&fc"; break;
    		case "pink": output = "&fd"; break;
    		case "yellow": output = "&fe"; break;
    		case "white": output = "&f"; break;
     	}
    	} else {
     	switch(input.toLowerCase()){
    		case "&0": output = "&0Black"; break;
        	case "&1": output = "&1Navy"; break;
        	case "&2": output = "&2Green"; break;
        	case "&3": output = "&3Teal"; break;
        	case "&4": output = "&4Red"; break;
        	case "&5": output = "&5Purple"; break;
        	case "&6": output = "&6Gold"; break;
        	case "&7": output = "&7Silver"; break;
        	case "&8": output = "&8Grey"; break;
        	case "&9": output = "&9Blue"; break;
        	case "&a": output = "&aLime"; break;
        	case "&b": output = "&bAqua"; break;
        	case "&c": output = "&cRose"; break;
        	case "&d": output = "&dPink"; break;
        	case "&e": output = "&eYellow"; break;
        	case "&f": output = "&fWhite"; break;
         	}
     	}
    	
    	return output;
    }
    
    
    private void doSet(String color, String newTag, Player player, List<String> suffixes) {
    	//TODO
    
    }
    
    private void doSet(String newTag, Player player, List<String> suffixes) {
    	String[] cutter;
    	String current = PEX.getUser(player).getSuffix();
    	int loc;
    	List<String> temp = new ArrayList<String>();
    	
    	for(String matches : suffixes){
    		if(stripper(matches).toLowerCase() == newTag.toLowerCase()){
    			temp.add(matches);
    		} 
    	}
    	
    	if(temp.size() == 0){
    		player.sendMessage("You do not own the tag " + newTag + ". Type /TT Check to see what you own.");
    		return;
    	}
    	    	   	
    	if(temp.indexOf(current) == temp.size()-1){
    		loc =0;
      	} else {
      		loc = temp.indexOf(current) + 1;
      	}
    	
    	cutter = temp.get(loc).split("(&([a-f][A-F][0-9]))");
		if(cutter.length > 2){
			PEX.getUser(player).setSuffix(colorize(cutter[0]) + "[" + colorize(temp.get(loc).substring(2)) + colorize(cutter[0]) + "]" , null);    			
		}else{
			PEX.getUser(player).setSuffix(colorize(cutter[0]) + "[" + cutter[1] + "]" , null);
		}
		
	}
    
    private String stripper(String strip){
		String temp = "";
			temp.replaceAll("(&([A-F][a-f][0-9]))", "").replaceAll("(§([A-F][a-f][0-9]))", "").replace("[", "").replace("]", "");
    	return strip;
    	
    }

	private void doTop(Player player) {
    	int x=1;
    	String[] splitter;
    	
    	
    	player.sendMessage(colorize("&2Top &f5 &2Trophy Tag Owners&f:"));
    	
		while(x<=5){
			splitter = config.getString("Top."+x).split("§§");
			player.sendMessage(colorize("&f"+x+". &2"+ splitter[0] +" &f(&4"+splitter[1] + " Trophy Tags&f)"));
	    }
    	
		
		
	}
    
    private void doTop(Player player, String amount) {
		// TODO Auto-generated method stub
		
	}
    
    private void doTop(Player player, String min, String max) {
		// TODO Auto-generated method stub
		
	}
    
    private void doUpdate(){
    	int x = 0;
    	String[] splitter;
    	List<String> top = new ArrayList<String>();
    	
    	for(PermissionUser user : PEX.getUsers()){
        	top.add(x, user.getName().toLowerCase() + "§§" + SManager.getSuffixes(user.getName()).size());
        	splitter = top.get(x).split("(§§)");
        	config.set("Players." + splitter[0] + ".Number" , splitter[1]);
       	}
    	
    	doSorting(top);
    	plugin.saveConfig();
    }
   
    

    private void doSorting(List<String> top){
    	int x=1;
    	
    	Collections.sort(top, new Sorter());
    	while(x<=top.size()){
    		config.set("Top."+x,top.get(x-1));
    		x++;
    	}
    	plugin.saveConfig();
    }

	private void doCheck(Player player, String name) {
    	String[] cutter;
    	String list = "";
    	List<String> tags = SManager.getSuffixes(name);
    	for(String stripper: tags){
    		cutter = stripper.split("(&([A-F][a-f][0-9]))");
    		if(cutter.length > 2){
    			list.concat(colorize(cutter[0]) + "[" + colorize(stripper.substring(2)) + colorize(cutter[0]) + "]" + "&f, ");    			
    		}else{
    			list.concat(colorize(cutter[0]) + "[" + cutter[1] + "] &f(" + colorTest(cutter[0],false) + "&f) , " );
    		}
    		
    	}
    	list = list.substring(0, (list.length() - 2));
    	
    	player.sendMessage("§6~~ Trophy Tags - Showing "+tags.size() + " tags for "+ name.replace(name.charAt(0), name.toUpperCase().charAt(0)) + " ~~");
    	player.sendMessage(list);
    	doUpdate();
	}
    
    private void doList(Player player, List<String> tags) {
    	String[] cutter;
    	String list = "";
    	for(String stripper: tags){
    		cutter = stripper.split("(&([a-f0-9]))");
    		if(cutter.length >= 2){
    				list.concat(colorize(cutter[0]) + "[" + colorize(stripper.substring(2)) + colorize(cutter[0]) + "]" + ", ");    			
        		}else{
        			list.concat(colorize(cutter[0]) + "[" + cutter[1] + "]" + ", " );
        		}
    		
    	}
    	list = list.substring(0, (list.length() - 2));
    	
    	player.sendMessage("§6~~ Trophy Tags - " + tags.size() + " Tags Owned ~~");
    	player.sendMessage(list);
    	doUpdate();
	}

	public static String colorize(String original)
    {
      return original.replace("&0", "§0")
        .replace("&1", "§1")
        .replace("&2", "§2")
        .replace("&3", "§3")
        .replace("&4", "§4")
        .replace("&5", "§5")
        .replace("&6", "§6")
        .replace("&7", "§7")
        .replace("&8", "§8")
        .replace("&9", "§9")
        .replace("&a", "§a")
        .replace("&b", "§b")
        .replace("&c", "§c")
        .replace("&d", "§d")
        .replace("&e", "§e")
        .replace("&f", "§f");
    }

	private boolean doHelp(Player player) {

		player.sendMessage(ChatColor.GOLD + "~~ Trophy Tags - Commands ~~");
        player.sendMessage(ChatColor.GREEN + "/TT List");
        player.sendMessage(ChatColor.GREEN + "/TT Set [Tag] // Set [Tag][Color]");
        player.sendMessage(ChatColor.GREEN + "/TT Top // Top [Number] // Top [Min][Max]");
        player.sendMessage(ChatColor.GREEN + "/TT Check [PlayerName]");
        player.sendMessage(ChatColor.GREEN + "/TT Clear");
        return true;
		
	}
    

}
