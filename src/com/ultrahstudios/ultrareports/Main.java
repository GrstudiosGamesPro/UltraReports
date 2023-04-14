package com.ultrahstudios.ultrareports;

import java.io.File;
import java.util.ArrayList;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

    String ruta;
    String DBdataRuta;
    

	
	@Override
	public void onEnable () {
		System.out.println("Ultra Reports iniciado con exito");
		 InsertConfig();
	}
	
	@Override
	public void onDisable () {
		System.out.println ("Ultra Reports fallo al iniciar");
	}
	
	public boolean onCommand (CommandSender sender, Command cmd, String arg, String[] args) {
	    Player PlayerReports = Bukkit.getPlayer(args[0]);
	    
		Player player = (Player) sender;
		String PlayerName = PlayerReports.getName();
		
	    String SuccefullyReporterMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("WarningMessageForReporter").replace("%player%", args[0]));
	    String EjecutarComandoAlRevazarLimiteDeReportes = this.getConfig().getString("Command");
	    String PlayerReport = this.getConfig().getString("AccountsReport." + PlayerName);
	    File config = new File(this.getDataFolder(), "config.yml");
	    int ReportLimit = this.getConfig().getInt("ReportLimit");
	    List<String> NewDataPlayer = new ArrayList<>();
	    String SearchPlayers = "AccountsReport." + PlayerName + ".Reporters";
		List<String> PlayersGet = this.getConfig().getStringList(SearchPlayers);
	    boolean noContain = false;
	    boolean PlayerOnline = PlayerReports.isOnline();
	    
	    
	    if (arg.length() <= 0) {
	    	 String SuccefullyReporterMessag2e = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("NotPlayerSelect").replace("%player%", args[0]));
	    	 player.sendMessage(SuccefullyReporterMessag2e);
	    	 return false;
	    }
	    
	    	    
		if (cmd.getName().equalsIgnoreCase("report") && config.exists() && args.length > 0) {		
			
			if (!PlayerOnline) {
				player.sendMessage("This player not is online");
			}else {
				
				if (PlayerName != player.getName()) {
					if (PlayerReport == null) {
						
						
						this.getConfig().set("AccountsReport."+PlayerName, "");
						this.getConfig().set("AccountsReport."+PlayerName+".Reports", 1);
						this.getConfig().set("AccountsReport."+PlayerName+".Reporters", "");
						NewDataPlayer.add(player.getName());
						this.getConfig().set("AccountsReport." + PlayerName + ".Reporters", NewDataPlayer);
						
						String isSendReport = this.getConfig().getString("WarningMessageForReporter");
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', isSendReport.replace("%player%", PlayerName)));
						this.saveConfig();
					}else {
						
						
							String AlreadyReport = this.getConfig().getString("AlreadyReportMessage");

							for (String elements : PlayersGet) {					
									if (elements.contains(player.getName())) {
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', AlreadyReport));	
										noContain = true;
									}else {
										noContain = false;
									}							
							}
							
							if (noContain == false) {
                                String AlreadySearchPlayers = "AccountsReport." + PlayerName + ".Reporters";
								
								List<String> PlayersSetNewData = this.getConfig().getStringList(AlreadySearchPlayers);
								PlayersSetNewData.add(player.getName());
								
								this.getConfig().set("AccountsReport." + PlayerName + ".Reporters", PlayersSetNewData);
								
								String isSendReport = this.getConfig().getString("WarningMessageForReporter");
								
								int Reports = this.getConfig().getInt("AccountsReport." + PlayerName + ".Reports");
								
								this.getConfig().set("AccountsReport." + PlayerName + ".Reports", Reports + 1);
								
								
								this.saveConfig();
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', isSendReport.replace("%player%", PlayerName)));
							}
							
							if (this.getConfig().getInt("AccountsReport."+args[0]+".Reports") >= ReportLimit) {
								ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
								Bukkit.dispatchCommand(console, EjecutarComandoAlRevazarLimiteDeReportes.replace("%player%", PlayerName));
								player.sendMessage(SuccefullyReporterMessage);
						    }
					}
				}else {
					String MsgSendYourSelf = this.getConfig().getString("ReportYourself");
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', MsgSendYourSelf));
				}
			
			}
				
		}else {
			InsertConfig();
		}
		return true;
	}
	
	
	public void InsertConfig () {
		File config = new File(this.getDataFolder(), "config.yml");
		
		ruta = config.getPath();
		
		if (!config.exists()) {
			this.getConfig().options().copyDefaults(true);
			saveDefaultConfig();
		}
	}
}