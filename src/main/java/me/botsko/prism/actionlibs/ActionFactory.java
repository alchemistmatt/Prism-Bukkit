package me.botsko.prism.actionlibs;

import java.util.Locale;
import java.util.Map;

import me.botsko.prism.actions.BlockAction;
import me.botsko.prism.actions.BlockChangeAction;
import me.botsko.prism.actions.BlockShiftAction;
import me.botsko.prism.actions.EntityAction;
import me.botsko.prism.actions.EntityTravelAction;
import me.botsko.prism.actions.GrowAction;
import me.botsko.prism.actions.Handler;
import me.botsko.prism.actions.HangingItemAction;
import me.botsko.prism.actions.ItemStackAction;
import me.botsko.prism.actions.PlayerAction;
import me.botsko.prism.actions.PlayerDeathAction;
import me.botsko.prism.actions.PrismProcessAction;
import me.botsko.prism.actions.PrismRollbackAction;
import me.botsko.prism.actions.SignAction;
import me.botsko.prism.actions.UseAction;
import me.botsko.prism.actions.VehicleAction;
import me.botsko.prism.appliers.PrismProcessType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ActionFactory {

	/**
	 * GenericAction
	 *
	 * @param action_type the action
	 * @param player Offline Player
	 */
	public static Handler createBlock(String action_type, OfflinePlayer player) {
		final BlockAction a = new BlockAction();
		a.setActionType(action_type);
		a.setPlayer(player);
		return a;
	}

	/**
	 * BlockAction
	 *
	 * @param action_type the action
	 * @param block the block
	 * @param player Offline Player
	 */
	public static Handler createBlock(String action_type, Block block, OfflinePlayer player) {
		final BlockAction a = new BlockAction();
		a.setActionType(action_type);
		a.setBlock(block);
		a.setPlayer(player);
		return a;
	}

	public static Handler createBlock(String action_type, Block block, String nonPlayer) {
		final Handler a = createBlock(action_type, block, (OfflinePlayer) null);
		a.setSourceName(nonPlayer);
		return a;
	}

	/**
	 * BlockAction
	 *
	 * @param action_type the action
	 * @param state the block state
	 * @param player Offline Player
	 */
	public static Handler createBlock(String action_type, BlockState state, OfflinePlayer player) {
		final BlockAction a = new BlockAction();
		a.setActionType(action_type);
		a.setBlock(state);
		a.setPlayer(player);
		return a;
	}

	public static Handler createBlock(String action_type, BlockState block, String nonPlayer) {
		final Handler a = createBlock(action_type, block, (OfflinePlayer) null);
		a.setSourceName(nonPlayer);
		return a;
	}

	/**
	 * BlockChangeAction | WorldeditAction
	 *
	 * @param action_type the action
	 * @param player Offline Player
	 */
	public static Handler createBlockChange(String action_type, Location loc, Material oldMat, BlockData oldData,
			Material newMat, BlockData newData, OfflinePlayer player) {
		final BlockChangeAction a = new BlockChangeAction();
		a.setActionType(action_type);
		a.setMaterial(newMat);
		a.setBlockData(newData);
		a.setOldMaterial(oldMat);
		a.setOldBlockData(oldData);
		a.setPlayer(player);
		a.setLoc(loc);
		return a;
	}

	public static Handler createBlockChange(String action_type, Location loc, Material oldMat, BlockData oldData,
			Material newMat, BlockData newData, String nonPlayer) {
		final Handler a = createBlockChange(action_type, loc, oldMat, oldData, newMat, newData, (OfflinePlayer) null);
		a.setSourceName(nonPlayer);
		return a;
	}

	/**
	 * BlockShiftAction
	 *
	 * @param action_type the action
	 */
	public static Handler createBlockShift(String action_type, Block from, Location to, String nonPlayer) {
		final BlockShiftAction a = new BlockShiftAction();
		a.setActionType(action_type);
		a.setBlock(from);
		a.setSourceName(nonPlayer);
		a.setLoc(to);
		return a;
	}

	/**
	 * EntityAction
	 *
	 * @param action_type the action
	 * @param player the acting player
	 */
	public static Handler createEntity(String action_type, Entity entity, OfflinePlayer player) {
		return ActionFactory.createEntity(action_type, entity, player, null);
	}

	public static Handler createEntity(String action_type, Entity entity, String nonPlayer) {
		return ActionFactory.createEntity(action_type, entity, nonPlayer, null);
	}

	public static Handler createEntity(String action_type, Entity entity, OfflinePlayer player, String dyeUsed) {
		final EntityAction a = new EntityAction();
		a.setActionType(action_type);
		a.setPlayer(player);
		a.setEntity(entity, dyeUsed);
		return a;
	}

	public static Handler createEntity(String action_type, Entity entity, String nonPlayer, String dyeUsed) {
		final Handler a = createEntity(action_type, entity, (OfflinePlayer) null, dyeUsed);
		a.setSourceName(nonPlayer);
		return a;
	}

	/**
	 * EntityTravelAction
	 *
	 * @param action_type the action
	 */
	public static Handler createEntityTravel(String action_type, Entity entity, Location from, Location to,
			TeleportCause cause) {
		final EntityTravelAction a = new EntityTravelAction();
		a.setEntity(entity);
		a.setActionType(action_type);
		a.setLoc(from);
		a.setToLocation(to);
		a.setCause(cause);
		return a;
	}

	/**
	 * GrowAction
	 *
	 * @param action_type the action
	 * @param player the player
	 */
	public static Handler createGrow(String action_type, BlockState blockstate, OfflinePlayer player) {
		final GrowAction a = new GrowAction();
		a.setActionType(action_type);
		a.setBlock(blockstate);
		a.setPlayer(player);
		return a;
	}

	public static Handler createGrow(String action_type, BlockState blockstate, String nonPlayer) {
		final Handler a = createGrow(action_type, blockstate, (OfflinePlayer) null);
		a.setSourceName(nonPlayer);
		return a;
	}

	/**
	 * HangingItemAction
	 *
	 * @param action_type the action
	 * @param player the player
	 */
	public static Handler createHangingItem(String action_type, Hanging hanging, OfflinePlayer player) {
		final HangingItemAction a = new HangingItemAction();
		a.setActionType(action_type);
		a.setHanging(hanging);
		a.setPlayer(player);
		return a;
	}

	public static Handler createHangingItem(String action_type, Hanging hanging, String nonPlayer) {
		final Handler a = createHangingItem(action_type, hanging, (OfflinePlayer) null);
		a.setSourceName(nonPlayer);
		return a;
	}

	/**
	 * ItemStackAction
	 *
	 * @param action_type the action
	 * @param player the acting player
	 */
	public static Handler createItemStack(String action_type, ItemStack item, Map<Enchantment, Integer> enchantments,
			Location loc, OfflinePlayer player) {
		return ActionFactory.createItemStack(action_type, item, 1, -1, enchantments, loc, player);
	}

	public static Handler createItemStack(String action_type, ItemStack item, int quantity, int slot,
			Map<Enchantment, Integer> enchantments, Location loc, OfflinePlayer player) {
		final ItemStackAction a = createItemStack(action_type, item, quantity, enchantments, loc, player);
		a.setSlot(String.valueOf(slot));
		
		return a;
	}

	public static Handler createItemStack(String action_type, ItemStack item, int quantity, BlockFace slot,
			Map<Enchantment, Integer> enchantments, Location loc, OfflinePlayer player) {
		final ItemStackAction a = createItemStack(action_type, item, quantity, enchantments, loc, player);
		a.setSlot(slot.name().toLowerCase(Locale.ENGLISH));
		
		return a;
	}

	public static Handler createItemStack(String action_type, ItemStack item, int quantity, int slot,
			Map<Enchantment, Integer> enchantments, Location loc, String sourceName) {
		final ItemStackAction a = new ItemStackAction();
		a.setActionType(action_type);
		a.setLoc(loc);
		a.setSourceName(sourceName);
		a.setItem(item, quantity, enchantments);
		a.setSlot(String.valueOf(slot));
		return a;
	}

	public static Handler createItemStack(String action_type, ItemStack item, int quantity, EquipmentSlot slot,
			Map<Enchantment, Integer> enchantments, Location loc, OfflinePlayer player) {
		final ItemStackAction a = createItemStack(action_type, item, quantity, enchantments, loc, player);
		a.setSlot(slot.name().toLowerCase(Locale.ENGLISH));
		
		return a;
	}
	
	private static ItemStackAction createItemStack(String action_type, ItemStack item, int quantity,
			Map<Enchantment, Integer> enchantments, Location loc, OfflinePlayer player) {
		final ItemStackAction a = new ItemStackAction();
		a.setActionType(action_type);
		a.setLoc(loc);
		a.setPlayer(player);
		a.setItem(item, quantity, enchantments);
		return a;
	}

	/**
	 * PlayerAction
	 *
	 * @param action_type the action
	 * @param player the acting player
	 */
	public static Handler createPlayer(String action_type, Player player, String additionalInfo) {
		final PlayerAction a = new PlayerAction();
		a.setActionType(action_type);
		a.setPlayer(player);
		a.setLoc(player.getLocation());
		a.deserialize(additionalInfo);
		return a;
	}

	/**
	 * PlayerDeathAction
	 *
	 * @param action_type the action
	 * @param player the acting player
	 */
	public static Handler createPlayerDeath(String action_type, Player player, String cause, String attacker) {
		final PlayerDeathAction a = new PlayerDeathAction();
		a.setActionType(action_type);
		a.setPlayer(player);
		a.setLoc(player.getLocation());
		a.setCause(cause);
		a.setAttacker(attacker);
		return a;
	}

	/**
	 * PrismProcessActionData
	 *
	 * @param action_type the action
	 * @param player the acting player
	 */
	public static Handler createPrismProcess(String action_type, PrismProcessType processType, Player player,
			String parameters) {
		final PrismProcessAction a = new PrismProcessAction();
		a.setActionType(action_type);
		a.setPlayer(player);
		a.setLoc(player.getLocation());
		a.setProcessData(processType, parameters);
		return a;
	}

	/**
	 * PrismRollbackAction
	 *
	 * @param action_type the action
	 * @param player the acting player
	 */
	public static Handler createPrismRollback(String action_type, BlockState oldblock, BlockState newBlock,
			OfflinePlayer player, long parent_id) {
		final PrismRollbackAction a = new PrismRollbackAction();
		a.setActionType(action_type);
		a.setPlayer(player);
		a.setLoc(oldblock.getLocation());
		a.setBlockChange(oldblock, newBlock, parent_id);
		return a;
	}

	/**
	 * SignAction
	 *
	 * @param action_type the action
	 * @param block the block acted on
	 * @param player the acting player
	 */
	public static Handler createSign(String action_type, Block block, String[] lines, OfflinePlayer player) {
		final SignAction a = new SignAction();
		a.setActionType(action_type);
		a.setPlayer(player);
		a.setBlock(block, lines);
		return a;
	}

	/**
	 * UseAction
	 *
	 * @param action_type the action
	 * @param block the block acted on
	 * @param player the acting player
	 */
	public static Handler createUse(String action_type, Material item, Block block, OfflinePlayer player) {
		final UseAction a = new UseAction();
		a.setActionType(action_type);
		a.setPlayer(player);
		a.setLoc(block.getLocation());
		a.setMaterial(item);
		return a;
	}

	/**
	 * VehicleAction
	 *
	 * @param action_type the action
	 * @param player the acting player
	 */
	public static Handler createVehicle(String action_type, Vehicle vehicle, OfflinePlayer player) {
		final VehicleAction a = new VehicleAction();
		a.setActionType(action_type);
		a.setPlayer(player);
		a.setLoc(vehicle.getLocation());
		a.setVehicle(vehicle);
		return a;
	}

	public static Handler createVehicle(String action_type, Vehicle vehicle, String nonPlayer) {
		final Handler a = createVehicle(action_type, vehicle, (OfflinePlayer) null);
		a.setSourceName(nonPlayer);
		return a;
	}

	// Backwards compat:

	/*
	 * @Deprecated public static Handler create(String action_type, String player) {
	 * return createBlock(action_type, player); }
	 * 
	 * @Deprecated public static Handler create(String action_type, Block block,
	 * String player) { return createBlock(action_type, block, player); }
	 * 
	 * @Deprecated public static Handler create(String action_type, Location loc,
	 * int oldId, byte oldSubid, int newId, byte newSubid, String player) { return
	 * createBlockChange(action_type, loc, oldId, oldSubid, newId, newSubid,
	 * player); }
	 * 
	 * @Deprecated public static Handler create(String action_type, Block from,
	 * Location to, String player) { return createBlockShift(action_type, from, to,
	 * player); }
	 * 
	 * @Deprecated public static Handler create(String action_type, Entity entity,
	 * String player) { return createEntity( action_type, entity, player); }
	 * 
	 * public static Handler create(String action_type, Entity entity, String
	 * player, String dyeUsed) { return createEntity(action_type, entity, player,
	 * dyeUsed); }
	 * 
	 * @Deprecated public static Handler create(String action_type, Entity entity,
	 * Location from, Location to, TeleportCause cause) { return
	 * createEntityTravel(action_type, entity, from, to, cause); }
	 * 
	 * @Deprecated public static Handler create(String action_type, BlockState
	 * blockstate, String player) { return createGrow(action_type, blockstate,
	 * player); }
	 * 
	 * @Deprecated public static Handler create(String action_type, Hanging hanging,
	 * String player) { return createHangingItem(action_type, hanging, player); }
	 * 
	 * @Deprecated public static Handler create(String action_type, ItemStack item,
	 * Map<Enchantment, Integer> enchantments, Location loc, String player) { return
	 * createItemStack(action_type, item, enchantments, loc, player); }
	 * 
	 * @Deprecated public static Handler create(String action_type, ItemStack item,
	 * int quantity, int slot, Map<Enchantment, Integer> enchantments, Location loc,
	 * String player) { return createItemStack(action_type, item, quantity, slot,
	 * enchantments, loc, player); }
	 * 
	 * @Deprecated public static Handler create(String action_type, Player player,
	 * String additionalInfo) { return createPlayer(action_type, player,
	 * additionalInfo); }
	 * 
	 * @Deprecated public static Handler create(String action_type, Player player,
	 * String cause, String attacker) { return createPlayerDeath(action_type,
	 * player, cause, attacker); }
	 * 
	 * @Deprecated public static Handler create(String action_type, PrismProcessType
	 * processType, Player player, String parameters) { final PrismProcessAction a =
	 * new PrismProcessAction(); a.setActionType( action_type ); a.setPlayerName(
	 * player ); a.setLoc( player.getLocation() ); a.setProcessData( processType,
	 * parameters ); return a; }
	 * 
	 * @Deprecated public static Handler create(String action_type, BlockState
	 * oldblock, BlockState newBlock, String player, int parent_id) { final
	 * PrismRollbackAction a = new PrismRollbackAction(); a.setActionType(
	 * action_type ); a.setPlayerName( player ); a.setLoc( oldblock.getLocation() );
	 * a.setBlockChange( oldblock, newBlock, parent_id ); return a; }
	 * 
	 * @Deprecated public static Handler create(String action_type, Block block,
	 * String[] lines, String player) { return createSign(action_type, block, lines,
	 * player); }
	 * 
	 * @Deprecated public static Handler create(String action_type, String
	 * item_used, Block block, String player) { return createUse(action_type,
	 * item_used, block, player); }
	 * 
	 * @Deprecated public static Handler create(String action_type, Vehicle vehicle,
	 * String player) { return createVehicle(action_type, vehicle, player); }
	 */
}