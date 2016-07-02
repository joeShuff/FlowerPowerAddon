package me.Dractus;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class FlowerListener implements Listener {
	
	JavaPlugin plugin;
	
	public FlowerListener(FlowerPower plugin)
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void breakBlock(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		
		Location pos = event.getBlock().getLocation();
		
		Block brokenBlock = event.getBlock();
		
		if (brokenBlock.getType() == Material.YELLOW_FLOWER || brokenBlock.getType() == Material.RED_ROSE || brokenBlock.getType() == Material.DOUBLE_PLANT)
		{
			event.setCancelled(true);
			
			pos.getBlock().setType(Material.AIR);
			
			Random rnd = new Random();
			
			int chosen = rnd.nextInt(1000000);
			
			if (chosen > 100)
			{
				player.getWorld().dropItemNaturally(pos,getItem(player));
			}
			else
			{
				player.sendMessage(ChatColor.RED + "You have unleashed hell into the world...");
				player.getWorld().spawnEntity(new Location(player.getWorld(), 0, 150 , 0), EntityType.ENDER_DRAGON);
			}
		}
	}
	
	@EventHandler
	public void blank(ItemSpawnEvent event)
	{
		if (event.getEntity().getItemStack().getType() == Material.DOUBLE_PLANT)
		{
			event.setCancelled(true);
		}
	}
	
	//This is a list of items that DO NOT have an item entity
	private List<String> NOT = Arrays.asList("CAKE_BLOCK","CROPS","REDSTONE_LAMP_ON","PISTON_EXTENSION","ENDER_PORTAL","PURPUR_DOUBLE_SLAB","GLOWING_REDSTONE_ORE"
												,"REDSTONE_TORCH_OFF","TRIPWIRE","BED_BLOCK","DOUBLE_STEP","BURNING_FURNACE","PORTAL");
	
	private ItemStack getItem(Player player)
	{		
		Random rnd = new Random();
		
		Material[] materials = Material.class.getEnumConstants();
		
		ItemStack stack = new ItemStack(materials[getRandom(0,materials.length)],1);
		
		System.out.println(stack.getType().toString());
		
		if (NOT.contains(stack.getType().toString()))
		{
			return getItem(player);
		}
		
		//Change some of the materials to one that has an item entity
		if (stack.getType().toString().contains("CAULDRON") && !stack.getType().toString().contains("ITEM"))
		{
			stack.setType(Material.getMaterial("CAULDRON_ITEM"));
		}

		if (stack.getType().toString().contains("SIGN"))
		{
			stack.setType(Material.getMaterial("SIGN"));
		}
		
		if (stack.getType().toString().contains("BANNER"))
		{
			stack.setType(Material.getMaterial("BANNER"));
		}
		
		if (stack.getType().toString().contains("BREWING_STAND"))
		{
			stack.setType(Material.getMaterial("BREWING_STAND_ITEM"));
		}
		
		if (stack.getType().toString().contains("DOOR") && !stack.getType().toString().contains("ITEM"))
		{
			stack.setType(Material.getMaterial(stack.getType().toString() + "_ITEM"));
		}
		
		if (stack.getType().toString().contains("WATER") && !stack.getType().toString().contains("BUCKET"))
		{
			return getItem(player);
		}
		
		if (stack.getType().toString().contains("LAVA") && !stack.getType().toString().contains("BUCKET"))
		{
			return getItem(player);
		}
		
		if (stack.getType() == Material.FURNACE || stack.getType() == Material.BURNING_FURNACE || stack.getType() == Material.SOIL)
		{
			stack.setDurability((short) 0);
		}
		
		if (stack.getType() == Material.ENCHANTED_BOOK)
		{
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) stack.getItemMeta();
			
			int amountofEnchs = rnd.nextInt(3) + 1;
			
			for (int i = 0 ; i < amountofEnchs ; i ++)
			{
				meta = addEnchant(meta, rnd);
			}
			
			stack.setItemMeta(meta);
		}
		
		if (stack.getType() == Material.MONSTER_EGG)
		{
			stack.setDurability((short) 50);
		}
		
		if (stack.getType() == Material.SKULL_ITEM)
		{
			stack = new ItemStack(Material.SKULL_ITEM,1,(byte) 3);
			SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
			
			meta.setOwner(player.getName());
			
			stack.setItemMeta(meta);
		}
		
		return new ItemStack(stack);
	}
	
	public int getRandom(int a,int b)
	{
	  return (int) Math.round((Math.random()*b)+a);
	}
	
	@EventHandler
	public void cancelEnderPearl(PlayerTeleportEvent event)
	{
		Location destination = event.getTo();
		Player player = event.getPlayer();
		
		if (event.getCause() == TeleportCause.ENDER_PEARL)
		{
			event.setCancelled(true);
			
			player.teleport(destination);
		}
		
	}
	
	private EnchantmentStorageMeta addEnchant(EnchantmentStorageMeta meta, Random rnd)
	{
		int selector = rnd.nextInt(4) + 1;
		
		if (selector == 1)
		{
			int id = rnd.nextInt(8);
			
			if (!meta.hasConflictingEnchant(Enchantment.getById(id)))
			{
				meta.addStoredEnchant(Enchantment.getById(id), rnd.nextInt(3) + 1,true);
			}
		}
		else if (selector == 2)
		{
			int id = rnd.nextInt(6) + 16;
			
			if (!meta.hasConflictingEnchant(Enchantment.getById(id)))
			{
				meta.addStoredEnchant(Enchantment.getById(id), rnd.nextInt(2) + 1,true);
			}
		}
		else if (selector == 3)
		{
			int id = rnd.nextInt(4) + 32;
			if (!meta.hasConflictingEnchant(Enchantment.getById(id)))
			{
				meta.addStoredEnchant(Enchantment.getById(id), rnd.nextInt(2) + 1,true);
			}
		}
		else
		{
			int id = rnd.nextInt(4) + 48;

			if (!meta.hasConflictingEnchant(Enchantment.getById(id)))
			{
				meta.addStoredEnchant(Enchantment.getById(id), rnd.nextInt(2) + 1,true);
			}
		}
		
		return meta;
	}
}
