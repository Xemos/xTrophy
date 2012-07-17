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
    			
    		}
    		case "top":{
    			doTop(player);
    		}
    		case "check":{
    			doCheck(player, args[1].toLowerCase());
    		}
    		case "clear":{
    			PEX.getUser(player.getName()).setSuffix(colorize("&f"), null);
                
    		}
    		default:{
    			return doHelp(player);
    		}
    		  	
    	}
		    
    }
    
    private void doTop(Player player) {
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
    	Collections.sort(top, new Sorter());
    }
    
    private void doUpdate(String player, int number){
    	config.set("Players." + player.toLowerCase() + ".Number", number);
    	plugin.saveConfig();
    	
    }

	private void doCheck(Player player, String name) {
    	String[] cutter;
    	String list = "";
    	List<String> tags = SManager.getSuffixes(name);
    	doUpdate(name,tags.size());
    	for(String stripper: tags){
    		cutter = stripper.split("(&([a-f0-9]))");
    		if(cutter.length > 2){
    			list.concat(colorize(cutter[0]) + "[" + colorize(stripper.substring(2)) + colorize(cutter[0]) + "]" + ", ");    			
    		}else{
    			list.concat(colorize(cutter[0]) + "[" + cutter[1] + "]" + ", " );
    		}
    		
    	}
    	list = list.substring(0, (list.length() - 2));
    	
    	player.sendMessage("§6~~ Trophy Tags - Showing "+tags.size() + " tags for "+ name.replace(name.charAt(0), name.toUpperCase().charAt(0)) + " ~~");
    	player.sendMessage(list);
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
    	doUpdate(player.getName(),tags.size());
    	
    	player.sendMessage("§6~~ Trophy Tags - " + tags.size() + " Tags Owned ~~");
    	player.sendMessage(list);
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
        player.sendMessage(ChatColor.GREEN + "/TT Set [Tag]");
        player.sendMessage(ChatColor.GREEN + "/TT Top");
        player.sendMessage(ChatColor.GREEN + "/TT Check [PlayerName]");
        player.sendMessage(ChatColor.GREEN + "/TT Clear");
        return true;
		
	}
    

}
