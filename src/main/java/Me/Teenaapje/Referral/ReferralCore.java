package Me.Teenaapje.Referral;

import java.util.List;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import Me.Teenaapje.Referral.Commands.CommandManager;
import Me.Teenaapje.Referral.Database.Database;
import Me.Teenaapje.Referral.PlaceHolders.PlaceHolders;
import Me.Teenaapje.Referral.Utils.ConfigManager;
import Me.Teenaapje.Referral.Utils.Utils;

public class ReferralCore extends JavaPlugin{
	public static ReferralCore core;
	
	public ConfigManager config;
	public ReferralInvites rInvites;
	public ReferralMilestone milestone;
	public Database db;
	private static TaskScheduler scheduler;

	public static TaskScheduler getScheduler() {
		return scheduler;
	}

	public static JavaPlugin getPlugin() {
		return core;
	}

	public void onEnable() {
		scheduler = UniversalScheduler.getScheduler(this);
		saveDefaultConfig();
		// set the plugin
		ReferralCore.core = this;
		
		// get the config
		config 		= new ConfigManager();
		rInvites 	= new ReferralInvites(); 
		milestone 	= new ReferralMilestone();
		db 			= new Database();
		
		// set placeholders if papi is there
		if (ConfigManager.placeholderAPIEnabled) {
			new PlaceHolders().register();
		}
		
		new CommandManager();
		
		new ReferralEvents();
		
		Utils.Console("[Referrel] Initialized - Enjoy");
	}
	
	public void onDisable() {
		db.CloseConnection();
		// set placeholders if papi is there
		if (ConfigManager.placeholderAPIEnabled) {
			new PlaceHolders().unregister();
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public Player GetPlayer(String name) {
		Player player = this.getServer().getPlayer(name);
		if (player != null) {
			return player;
		}
		
		return getServer().getOfflinePlayer(name).getPlayer();
	}
	
	public void UseCommands(List<?> commands, Player player) {
		for (int i = 0; i < commands.size(); i++) {
			String command = (String) commands.get(i);

			getScheduler().runTask(this, new Runnable() {
				@Override
				public void run() {
					getServer().dispatchCommand(getServer().getConsoleSender(), command.replace("<player>", player.getName()));
				}
		    });
		}
	}
	
}
