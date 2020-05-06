package me.DuppyIsCool.Reveal;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements CommandExecutor{
	private int time,delay;
	private BukkitTask revealtask;
	public void onEnable() {
		this.saveDefaultConfig();
		try {
			time = this.getConfig().getInt("reveal.time");
		}catch(Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error retrieving server time. Is the config correct?");
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling due to error");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		try {
			delay = this.getConfig().getInt("reveal.delay");
		}catch(Exception e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error retrieving server time. Is the config correct?");
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling due to error");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		Plugin.plugin = this;
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Started a location reveal with the length of "+time + " seconds and delay of "+delay + " seconds.");
		if(delay >= 0 && time >= 0) {
			revealtask = new RevealTask(time).runTaskTimer(this,20 * delay,20);
		}
		else {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "ERROR: Delay or time are less than 0. Disabling plugin");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	public void onDisable() {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling Reveal Plugin");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("stopreveal")) {
			if(sender.hasPermission("reveal.stop") || sender.isOp()){
				if(!revealtask.isCancelled()) {
					revealtask.cancel();
					sender.sendMessage(ChatColor.GREEN + "Current reveal timer stopped!");
					return true;
				}
				else {
						sender.sendMessage(ChatColor.RED + "There is no reveal timer active!");
						return true;
					}
			}
			else {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return true;
			}
		}
		else if (command.getName().equalsIgnoreCase("startreveal")) {
			if((sender.hasPermission("reveal.start") || sender.isOp())) {
				if(revealtask.isCancelled()) {
					revealtask = new RevealTask(time).runTaskTimer(this,20*delay,20);
					sender.sendMessage(ChatColor.GREEN + "Reveal timer started with an interval of "+time + " seconds and a delay of "+delay+" seconds.");
					return true;
				}
				else {
					sender.sendMessage(ChatColor.RED + "There is already a reveal timer active!");
					return true;
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return true;
			}
		}
		else if(command.getName().equalsIgnoreCase("reloadreveal")){
			this.reloadConfig();
			if((sender.hasPermission("reveal.reload") || sender.isOp())) {
				try {
					time = this.getConfig().getInt("reveal.time");
				}catch(Exception e) {
					Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error retrieving server time. Is the config correct?");
					Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling due to error");
					Bukkit.getPluginManager().disablePlugin(this);
				}
				try {
					delay = this.getConfig().getInt("reveal.delay");
				}catch(Exception e) {
					Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error retrieving server time. Is the config correct?");
					Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Disabling due to error");
					Bukkit.getPluginManager().disablePlugin(this);
				}
				sender.sendMessage(ChatColor.GREEN + "Reveal config reloaded!");
				return true;
			}
			else {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return true;
			}
		}
        return false;
    }
}
