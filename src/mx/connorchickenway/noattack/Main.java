package mx.connorchickenway.noattack;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

public class Main extends JavaPlugin implements Listener {
 
	private ASkyBlockAPI api;
	
	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		if(!pm.isPluginEnabled("ASkyBlock")) {
			Bukkit.getConsoleSender().sendMessage("§cFor the plugin to work you need to have the 'ASkyblock' plugin on your server!");
			pm.disablePlugin(this);
			return;
		}
		this.api = ASkyBlockAPI.getInstance();
		pm.registerEvents(this, this);
	}
	
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onDamage(EntityDamageByEntityEvent event) {
		if(event.isCancelled()) {
			return;
		}
		Entity entity = event.getEntity();
		Entity damager = event.getDamager();
		if(!(entity instanceof Player && damager instanceof Player)) {
			return;
		}
		List<UUID> list = api.getTeamMembers(damager.getUniqueId());
		if(list.contains(entity.getUniqueId())) {
			event.setCancelled(true);
		}
	}

}
