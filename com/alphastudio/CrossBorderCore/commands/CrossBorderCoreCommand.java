/*     */ package com.alphastudio.CrossBorderCore.commands;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.command.TabCompleter;
/*     */ import com.alphastudio.CrossBorderCore.CrossBorderCore;
/*     */ import com.alphastudio.CrossBorderCore.config.BorderConfig;
/*     */ import com.alphastudio.CrossBorderCore.config.ConfigManager;
/*     */ 
/*     */ 
/*     */ public class CrossBorderCoreCommand
/*     */   implements CommandExecutor, TabCompleter
/*     */ {
/*     */   private final CrossBorderCore plugin;
/*     */   private final ConfigManager configManager;
/*     */   
/*     */   public CrossBorderCoreCommand(CrossBorderCore plugin, ConfigManager configManager) {
/*  22 */     this.plugin = plugin;
/*  23 */     this.configManager = configManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
/*  28 */     if (args.length == 0) {
/*  29 */       sendHelp(sender);
/*  30 */       return true;
/*     */     } 
/*     */     
/*  33 */     String subCommand = args[0].toLowerCase();
/*     */     
/*  35 */     switch (subCommand) {
/*     */       case "reload":
/*  37 */         if (!sender.hasPermission("crossbordercore.reload")) {
/*  38 */           sender.sendMessage(String.valueOf(ChatColor.RED) + "You don't have permission to use this command.");
/*  39 */           return true;
/*     */         } 
/*  41 */         reloadConfig(sender);
/*  42 */         return true;
/*     */       
/*     */       case "list":
/*  45 */         if (!sender.hasPermission("crossbordercore.list")) {
/*  46 */           sender.sendMessage(String.valueOf(ChatColor.RED) + "You don't have permission to use this command.");
/*  47 */           return true;
/*     */         } 
/*  49 */         listBorders(sender);
/*  50 */         return true;
/*     */       
/*     */       case "info":
/*  53 */         if (!sender.hasPermission("crossbordercore.info")) {
/*  54 */           sender.sendMessage(String.valueOf(ChatColor.RED) + "You don't have permission to use this command.");
/*  55 */           return true;
/*     */         } 
/*  57 */         showInfo(sender);
/*  58 */         return true;
/*     */       
/*     */       case "help":
/*  61 */         sendHelp(sender);
/*  62 */         return true;
/*     */     } 
/*     */     
/*  65 */     sender.sendMessage(String.valueOf(ChatColor.RED) + "Unknown subcommand. Use /crossservertp help for more information.");
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void reloadConfig(CommandSender sender) {
/*     */     try {
/*  72 */       this.configManager.loadConfig();
/*  73 */       sender.sendMessage(String.valueOf(ChatColor.GREEN) + "CrossServerTP configuration reloaded successfully!");
/*  74 */       sender.sendMessage(String.valueOf(ChatColor.GRAY) + "Loaded " + String.valueOf(ChatColor.GRAY) + " border(s).");
/*  75 */     } catch (Exception e) {
/*  76 */       sender.sendMessage(String.valueOf(ChatColor.RED) + "Failed to reload configuration: " + String.valueOf(ChatColor.RED));
/*  77 */       this.plugin.getLogger().warning("Failed to reload config: " + e.getMessage());
/*  78 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void listBorders(CommandSender sender) {
/*  83 */     List<BorderConfig> borders = this.configManager.getBorders();
/*     */     
/*  85 */     if (borders.isEmpty()) {
/*  86 */       sender.sendMessage(String.valueOf(ChatColor.YELLOW) + "No borders configured.");
/*     */       
/*     */       return;
/*     */     } 
/*  90 */     sender.sendMessage(String.valueOf(ChatColor.GOLD) + "=== Configured Borders ===");
/*  91 */     for (int i = 0; i < borders.size(); i++) {
/*  92 */       BorderConfig border = borders.get(i);
/*  93 */       sender.sendMessage(String.valueOf(ChatColor.GRAY) + String.valueOf(ChatColor.GRAY) + ". " + i + 1 + "World: " + String.valueOf(ChatColor.WHITE) + String.valueOf(ChatColor.AQUA) + border
/*  94 */           .getWorld() + " | Side: " + String.valueOf(ChatColor.WHITE) + String.valueOf(ChatColor.AQUA) + 
/*  95 */           String.valueOf(border.getSide()) + " | Coord: " + String.valueOf(ChatColor.WHITE) + String.valueOf(ChatColor.AQUA) + border
/*  96 */           .getCoordinate() + " | Target: " + String.valueOf(ChatColor.WHITE) + String.valueOf(ChatColor.AQUA));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void showInfo(CommandSender sender) {
/* 102 */     sender.sendMessage(String.valueOf(ChatColor.GOLD) + "=== CrossServerTP Info ===");
/* 103 */     sender.sendMessage(String.valueOf(ChatColor.WHITE) + "Version: " + String.valueOf(ChatColor.WHITE) + String.valueOf(ChatColor.AQUA));
/* 104 */     sender.sendMessage(String.valueOf(ChatColor.WHITE) + "Borders: " + String.valueOf(ChatColor.WHITE) + String.valueOf(ChatColor.AQUA));
/* 105 */     sender.sendMessage(String.valueOf(ChatColor.WHITE) + "Check Interval: " + String.valueOf(ChatColor.WHITE) + String.valueOf(ChatColor.AQUA) + " ticks");
/* 106 */     sender.sendMessage(String.valueOf(ChatColor.WHITE) + "Particle Distance: " + String.valueOf(ChatColor.WHITE) + String.valueOf(ChatColor.AQUA) + " blocks");
/*     */   }
/*     */   
/*     */   private void sendHelp(CommandSender sender) {
/* 110 */     sender.sendMessage(String.valueOf(ChatColor.GOLD) + "=== CrossServerTP Commands ===");
/* 111 */     sender.sendMessage(String.valueOf(ChatColor.YELLOW) + "/crossservertp help" + String.valueOf(ChatColor.YELLOW) + " - Show this help message");
/* 112 */     sender.sendMessage(String.valueOf(ChatColor.YELLOW) + "/crossservertp reload" + String.valueOf(ChatColor.YELLOW) + " - Reload the configuration");
/* 113 */     sender.sendMessage(String.valueOf(ChatColor.YELLOW) + "/crossservertp list" + String.valueOf(ChatColor.YELLOW) + " - List all configured borders");
/* 114 */     sender.sendMessage(String.valueOf(ChatColor.YELLOW) + "/crossservertp info" + String.valueOf(ChatColor.YELLOW) + " - Show plugin information");
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
/* 119 */     if (args.length == 1) {
/* 120 */       List<String> subCommands = new ArrayList<>();
/* 121 */       if (sender.hasPermission("crossbordercore.reload")) {
/* 122 */         subCommands.add("reload");
/*     */       }
/* 124 */       if (sender.hasPermission("crossbordercore.list")) {
/* 125 */         subCommands.add("list");
/*     */       }
/* 127 */       if (sender.hasPermission("crossbordercore.info")) {
/* 128 */         subCommands.add("info");
/*     */       }
/* 130 */       subCommands.add("help");
/*     */ 
/*     */       
/* 133 */       String input = args[0].toLowerCase();
/* 134 */       List<String> filtered = new ArrayList<>();
/* 135 */       for (String sub : subCommands) {
/* 136 */         if (sub.startsWith(input)) {
/* 137 */           filtered.add(sub);
/*     */         }
/*     */       } 
/* 140 */       return filtered;
/*     */     } 
/*     */     
/* 143 */     return new ArrayList<>();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\commands\CrossBorderCoreCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */