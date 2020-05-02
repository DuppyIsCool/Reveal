package me.DuppyIsCool.Reveal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;


public class RevealTask extends BukkitRunnable{ 
	String initialMessage;
	@Override
	public void run() {
		try {
			initialMessage = Plugin.plugin.getConfig().getString("reveal.initialmessage");
		}catch(Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error retrieving server time. Is the config correct?");
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling due to error");
			Bukkit.getPluginManager().disablePlugin(Plugin.plugin);
		}		
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
		}
		else {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "ERROR: No message string detected. Stopping reveal timer.");
			this.cancel();
		}
	}
	
}
