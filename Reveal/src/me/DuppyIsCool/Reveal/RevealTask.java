package me.DuppyIsCool.Reveal;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;


public class RevealTask extends BukkitRunnable{ 
	String initialMessage,countdownMessage;
	int counter,initial;
	ArrayList<Integer> countdowns = new ArrayList<Integer>();
	public RevealTask(int counter) {
		this.counter = counter;
		this.initial = counter;
		if(counter <= 0) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Reveal time cannot be less than or equal to 0");
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling due to error");
			Bukkit.getPluginManager().disablePlugin(Plugin.plugin);
		}
		
		try {
			initialMessage = Plugin.plugin.getConfig().getString("reveal.initialmessage");
		}catch(Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error retrieving initialmessage. Is the config correct?");
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling due to error");
			Bukkit.getPluginManager().disablePlugin(Plugin.plugin);
		}
		try {
			countdownMessage = Plugin.plugin.getConfig().getString("reveal.countdownmessage");
		}catch(Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error retrieving countdownmessage. Is the config correct?");
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling due to error");
			Bukkit.getPluginManager().disablePlugin(Plugin.plugin);
		}
		try {
			ArrayList<String> temp = (ArrayList<String>) Plugin.plugin.getConfig().getStringList("reveal.countdowns");
			for(String e : temp)
				countdowns.add(Integer.parseInt(e));
		}catch(Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error retrieving server countdown times. Is the config correct?");
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling due to error");
			Bukkit.getPluginManager().disablePlugin(Plugin.plugin);
		}	
	}
	
	public void run() {
		
		if(counter <= 0) {
			if(Plugin.plugin.getConfig().getString("reveal.message") != null){
				if((initialMessage.length() > 0) && Bukkit.getServer().getOnlinePlayers().size() > 0)
					Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', initialMessage));
				for(Player p : Bukkit.getOnlinePlayers()) {
					String message = ChatColor.translateAlternateColorCodes('&',Plugin.plugin.getConfig().getString("reveal.message"));
					message = message.replace("%player%", p.getName());
					message = message.replace("%x%", ""+p.getLocation().getBlockX());
					message = message.replace("%y%", ""+p.getLocation().getBlockY());
					message = message.replace("%z%", ""+p.getLocation().getBlockZ());
					Bukkit.getServer().broadcastMessage(message);
				}
				counter = initial;
			}
			else {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "ERROR: No message string detected. Stopping reveal timer.");
				this.cancel();
			}
		}
		else if(countdowns.contains(counter)) {
			countdownMessage = ChatColor.translateAlternateColorCodes('&',Plugin.plugin.getConfig().getString("reveal.countdownmessage"));
			if((counter > 60) && !(countdownMessage.contains("%minutes%"))){
				countdownMessage = countdownMessage.replace("%seconds%",""+counter);
			}
			else if(counter <= 60) {
				countdownMessage = countdownMessage.replace("%seconds%",""+counter);
				countdownMessage = countdownMessage.replace("%minutes%","0");
			}
			else {
				countdownMessage = countdownMessage.replace("%seconds%",""+(counter%60));
				countdownMessage = countdownMessage.replace("%minutes%",""+(counter/60));
			}
			
			Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', countdownMessage));
			counter--;
		}
		else
			counter--;
	}
	
}
