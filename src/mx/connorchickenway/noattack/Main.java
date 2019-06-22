package mx.connorchickenway.noattack;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

public class Main extends JavaPlugin implements Listener {
 
	private ASkyBlockAPI api;
	private boolean hit_projectile;
	
	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		if(!pm.isPluginEnabled("ASkyBlock")) {
			Bukkit.getConsoleSender().sendMessage("§cFor the plugin to work you need to have the 'ASkyblock' plugin on your server!");
			pm.disablePlugin(this);
			return;
		}
		this.saveDefaultConfig();
		this.api = ASkyBlockAPI.getInstance();
		this.hit_projectile = getConfig().getBoolean("hit_projectile");
		pm.registerEvents(this, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("noattack.ussage")) {
			if(args.length == 0) {
				sender.sendMessage("Usage: /" + label + " <reloadconfig>");
				return false;
			}
			if(args[0].equalsIgnoreCase("reloadconfig")) {
				this.reloadConfig();
				this.hit_projectile = getConfig().getBoolean("hit_arrows");
				sender.sendMessage("config.yml reloaded. " + hit_projectile);
			}
		}
		return false;
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onDamage(EntityDamageByEntityEvent event) {
		if(event.isCancelled()) {
			return;
		}
		Entity entity = event.getEntity();
		Entity damager = event.getDamager();
		if(damager instanceof Projectile) {
			ProjectileSource ps = ((Projectile) damager).getShooter();
			if (ps instanceof Entity) {
                damager = (Entity) ps;
                if(entity.equals(damager)) {
                	if(hit_projectile) {
                		return;
                	}
                }
            }
		}
		if(entity instanceof Player && damager instanceof Player) {
			List<UUID> list = api.getTeamMembers(damager.getUniqueId());
			boolean cancelled = false;
			if(list.size() > 0) {
				if(list.contains(entity.getUniqueId())) {
					cancelled = true;
				}
			} else 
			if(entity.equals(damager)) {
				cancelled = true;
			}
			event.setCancelled(cancelled);
		}

	}

}
