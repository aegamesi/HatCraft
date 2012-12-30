package com.aegamesi.mc.hatcraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandHandler implements CommandExecutor {
	public HatCraft plugin;

	public CommandHandler(HatCraft plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		boolean isPlayer = sender instanceof Player;
		Player p = null;
		if (isPlayer)
			p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("hat")) {
			if (!isPlayer) {
				sender.sendMessage("You can't use /hat from the console!");
				return true;
			}
			if (!sender.hasPermission("hatcraft.canhat")) {
				sender.sendMessage("You don't have permission!");
				return true;
			}
			ItemStack stack = p.getInventory().getItemInHand();
			if (HatCraft.disallowed.contains(stack.getTypeId())) {
				String msg = plugin.getConfig().getString("message");
				sender.sendMessage(msg);
				return true;
			}
			p.getInventory().remove(stack);
			if (p.getInventory().getHelmet() != null)
				p.getInventory().addItem(p.getInventory().getHelmet());
			p.getInventory().setHelmet(stack);
			sender.sendMessage("Successfully wore hat!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("backpack")) {
			if (!isPlayer) {
				sender.sendMessage("You can't use /backpack from the console!");
				return true;
			}
			if (!sender.hasPermission("hatcraft.backpack")) {
				sender.sendMessage("You don't have permission!");
				return true;
			}
			if (args.length > 0 && !sender.hasPermission("hatcraft.backpack.other")) {
				sender.sendMessage("You don't have permission to view the backpack of other players!");
				return true;
			}
			String player = args.length > 0 ? args[0] : sender.getName();
			String configKey = "backpack." + player.toLowerCase();
			ItemStack[] inv = plugin.getConfig().contains(configKey) ? plugin.getConfig().getList(configKey).toArray(new ItemStack[0]) : null;
			BackpackHolder bph = new BackpackHolder(plugin.getConfig().getInt("backpack-size"), player, inv);
			p.openInventory(bph.getInventory());
			return true;
		}
		return false;
	}
}