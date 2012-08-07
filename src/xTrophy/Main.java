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

		
	 public void onEnable() {
		  	plugin = this;
	        this.config = plugin.getConfig();
	        setupPEX();
	        doUpdate();
	        log.info("[TrophyTags] Enabled");
	    }
	 
	 public void onDisable() {
	        log.info("[TrophyTags] Disabled");
	    }

	private FileConfiguration config = null;
	public static final Logger log = Logger.getLogger("Minecraft");
	public static PermissionManager PEX;
	public static HashMap<String, ChatColor> colors = new HashMap<String, ChatColor>();
    
    public void setupPEX(){
        Plugin p = getServer().getPluginManager().getPlugin("PermissionsEx");
        if(p != null){
            PEX = PermissionsEx.getPermissionManager();
        } else {
            log.info("[TrophyTags] PEX not found. Disabling");
            this.setEnabled(false);
        }
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
		if ((sender instanceof ConsoleCommandSender)) {

			sender.sendMessage("No commands from console pl0x!");
			return true;

		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			doHelp(player);
			return true;
		}
		
    	
    	switch(args[0].toLowerCase()){
    		case "list":{
    			doList(player,  SManager.getSuffixes(player.getName()));
    			return true;
    		}
    		case "set":{
    			if(args.length == 3){
    				doSet(args[2], args[1], player, SManager.getSuffixes(player.getName()));
    				return true;
    			} else if(args.length ==2) {
    				doSet(args[1], player, SManager.getSuffixes(player.getName()));
    				return true;
    	    	} else {
    	    		player.sendMessage(colorize("&6Proper use&f:"));
    	    		player.sendMessage(colorize("&3/tt set &f[&3Tag Name&f] &f[&3Color&f] ~ &6Sets it to the matching color."));
    	    		player.sendMessage(colorize("&3/tt set &f[&3Tag Name&f] ~ &6Scrolls through the matching tags."));
    	    		return true;
    	    	}
    		}
    		case "top":{
    			if (args.length == 3){
    				if (args[1].matches("^[0-9]+$") && args[2].matches("^[0-9]+$")) {
    					if (Integer.parseInt(args[2]) < Integer.parseInt(args[1])) {
    						player.sendMessage(colorize("&6Proper use&f:"));
    						player.sendMessage(colorize("&3/tt top &f[&3minimum&f] [&3maximum&f]"));
    						return true;
    					}
    					if (Integer.parseInt(args[1]) > 50 ||
    							Integer.parseInt(args[2]) > 50) {
    						player.sendMessage(colorize("&3You may not view the list past 50."));
    						return true;
    					}
    					doTop(player, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    					return true;
    				}else{
    					player.sendMessage(colorize("&6Proper use&f:"));
						player.sendMessage(colorize("&3/tt top &f[&3minimum&f] [&3maximum&f]"));
    					return true;
    				}
    			}
    			if(args.length==2)
    					if(args[1].matches("^[0-9]+$")){
    						if (Integer.parseInt(args[1]) < 3) {
        						player.sendMessage(colorize("&3You must view a list larger than 3."));
        						return true;
        					} 
    						if (Integer.parseInt(args[1]) > 50) {
        						player.sendMessage(colorize("&3You may not view the list past 50."));
        						return true;
        					} else {
        						doTop(player, Integer.parseInt(args[1]));
        						return true;
        					}
    					}else{
    						player.sendMessage(colorize("&6Proper use&f:"));
    						player.sendMessage(colorize("&3/tt top &f[&3List Size&f]"));
    						return true;
    					}
    			doTop(player);
    			return true;
    		}
    		case "check":{
    			if(args.length == 2){
    				doCheck(player, args[1].toLowerCase());
    				return true;
    			} else {
    				player.sendMessage(colorize("&6Proper use&f:"));
					player.sendMessage(colorize("&3/tt check &f[&3Player Name&f]"));
    			}
    			
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
    		case "teal": output = "&3"; break;
    		case "red": output = "&4"; break;
    		case "purple": output = "&5"; break;
    		case "gold": output = "&6"; break;
    		case "silver": output = "&7"; break;
    		case "grey": output = "&8"; break;
    		case "blue": output = "&9"; break;
    		case "lime": output = "&a"; break;
    		case "aqua": output = "&b"; break;
    		case "rose": output = "&c"; break;
    		case "pink": output = "&d"; break;
    		case "yellow": output = "&e"; break;
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
    	String[] cutter;
    	int loc = 0;
    	List<String> temp = new ArrayList<String>();
    	
    	for(String matches : suffixes){
    		if(stripper(matches).equalsIgnoreCase(newTag)){
    		temp.add(matches);
    		} 
    	}
    	
    	if(temp.size() == 0){
    		player.sendMessage("&6You do not own the Trophy Tag: " + newTag);
    		return;
    	}
    	
    	search : {
    		for(String stripper: temp){
    			cutter = stripper.split("(&([a-fA-F0-9]))");
    			cutter[0] = stripper.substring(0, 2);
    		    		
     			if(cutter.length > 3){
       				// not it because it is a multi, so skip it	
        		}else if(cutter.length == 3){
        			if(colorTest(stripper.substring(2, 4),false).substring(2).equalsIgnoreCase(color)){
    					newTag = colorize(cutter[0] + "[" + temp.get(loc).substring(2) + cutter[0] + "]&f");
    					break search;
    				}
        		}else{
        			if(colorTest(stripper.substring(0, 2),false).substring(2).equalsIgnoreCase(color)){
           				newTag = colorize(cutter[0] + "[" + cutter[1] + "]&f");
        				break search;
    				}
           		}   
    			loc++;
    		}
    	}
    	
    	config.set("Players." + player.getName() + ".Current",  temp.get(loc));
    	PEX.getUser(player).setSuffix(newTag , null);
		player.sendMessage(colorize("&6Your Trophy Tag is now set to: " + newTag));
		plugin.saveConfig();
    
    }
    
    private void doSet(String newTag, Player player, List<String> suffixes) {
    	String[] cutter;
    	String current ="";
    	int loc;
    	List<String> temp = new ArrayList<String>();
    	
    	for(String matches : suffixes){
    		if(stripper(matches).equalsIgnoreCase(newTag)){
    		temp.add(matches);
    		} 
    	}
    	
    	if(temp.size() == 0){
    		player.sendMessage("&6You do not own the Trophy Tag: " + newTag);
    		return;
    	}
    	
    	if(config.contains("Players." + player.getName() + ".Current")){
    		current = config.getString("Players." + player.getName() + ".Current");
     		loc = temp.indexOf(current) +1;
     		if(temp.size() == loc){
     			loc = 0;
     		}
      	} else {
       		loc = 0;
      	}
    	    
    	config.set("Players." + player.getName() + ".Current",  temp.get(loc));
    	
    	cutter = temp.get(loc).split("(&([a-fA-F0-9]))");
		cutter[0] = temp.get(loc).substring(0, 2);
    	
		if(cutter.length > 2){
			newTag = colorize(cutter[0] + "[" + temp.get(loc).substring(2) + cutter[0] + "]&f");
		}else{
			newTag = colorize(cutter[0] + "[" + cutter[1] + "]&f");
		}
		
		PEX.getUser(player).setSuffix(newTag , null);
		player.sendMessage(colorize("&6Your Trophy Tag is now set to: " + newTag));
		plugin.saveConfig();
	}
    
    private String stripper(String strip){
    	return strip.replaceAll("(&([a-fA-F0-9]))", "");
	}

	private void doTop(Player player) {
    	int x=1;
    	String[] splitter;
    	
    	
    	player.sendMessage(colorize("&2Top &f5 &2Trophy Tag Owners&f:"));
    	
		while(x<=5){
			splitter = config.getString("Top."+x).split("§§");
			player.sendMessage(colorize("&f"+x+". &2"+ splitter[0] +" &f(&4"+splitter[1] + " Trophy Tags&f)"));
			x++;
	    }
    	
		
		
	}
    
    private void doTop(Player player, Integer amount) {
    	int x=1;
    	String[] splitter;
    	
    	if(!config.contains("Top." + amount)){
    		player.sendMessage(colorize("&2There are not " + amount + " Trophy Tag holders&f:"));
    		return;
    	}
    	
    	player.sendMessage(colorize("&2Top &f" + amount + " &2Trophy Tag Owners&f:"));
    	
		while(x<=amount){
			splitter = config.getString("Top."+x).split("§§");
			player.sendMessage(colorize("&f"+x+". &2"+ splitter[0] +" &f(&4"+splitter[1] + " Trophy Tags&f)"));
			x++;
	    }
		
	}
    
    private void doTop(Player player, Integer min, Integer max) {
    	String[] splitter;
    	
    	if(!config.contains("Top." + max)){
    		player.sendMessage(colorize("&2There are not " + max + " Trophy Tag holders&f:"));
    		return;
    	}
    	
    	player.sendMessage(colorize("&2Showing top Trophy Tag Owners between &f " + min + " and " + max + "&f:"));
    	
		while(min<=max){
			splitter = config.getString("Top."+min).split("§§");
			player.sendMessage(colorize("&f"+min+". &2"+ splitter[0] +" &f(&4"+splitter[1] + " Trophy Tags&f)"));
			min++;
	    }
		
	}
    
    private void doUpdate(){
    	int x = 0;
    	String[] splitter;
    	List<String> top = new ArrayList<String>();
    	
    	for(PermissionUser user : PEX.getUsers()){
        	top.add(x, user.getName() + "§§" + SManager.getSuffixes(user.getName()).size());
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
    	
    	if(!SManager.getPermissionStrings(name).contains("whosthere.who")){
    		player.sendMessage("§6The player " + name + " does not exist!");
    		return;
    	}
    	
    	if(tags.size()==0){
    		player.sendMessage("§6" + name + " does not own and Trophy Tags.");
    		return;
    	}
    	
    	for(String stripper: tags){
    		cutter = stripper.split("(&([a-fA-F0-9]))");
    		cutter[0] = stripper.substring(0, 2);
    		
    		if(cutter.length > 3){
   				list = list.concat(colorize(cutter[0] + "[" + stripper.substring(2).toUpperCase() + cutter[0] + "]" + "&f, "));    			
    		}else if(cutter.length == 3){
				list = list.concat(colorize(cutter[0] + "[" + stripper.substring(2).toUpperCase() + cutter[0] + "]" +  "&f, "));    			
    		}else{
   				list = list.concat(colorize(cutter[0] + "[" + cutter[1].toUpperCase() + "]" +  "&f, " ));
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
    	
    	if(tags.size()==0){ 
    		player.sendMessage("§6You do not own any Trophy Tags."); 
    		return;
    	}
    	
    	for(String stripper: tags){
    		cutter = stripper.split("(&([a-fA-F0-9]))");
    		cutter[0] = stripper.substring(0, 2);
    	    		
    		if(cutter.length > 3){
   				list = list.concat(colorize(cutter[0] + "[" + colorize(stripper.substring(2).toUpperCase()) + cutter[0] + "]" + "&f, "));    			
    		}else if(cutter.length == 3){
				list = list.concat(colorize(cutter[0] + "[" + colorize(stripper.substring(2).toUpperCase()) + cutter[0] + "]" + "&f (" + colorTest(stripper.substring(2, 4),false) +  "&f), "));    			
    		}else{
   				list = list.concat(colorize(cutter[0] + "[" + cutter[1].toUpperCase() + "]" + "&f (" + colorTest(cutter[0],false) +  "&f), " ));
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
