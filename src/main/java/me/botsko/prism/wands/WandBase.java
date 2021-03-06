package me.botsko.prism.wands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.botsko.prism.utils.InventoryUtils;

public abstract class WandBase implements Wand {

	/**
	 * 
	 */
	protected boolean item_given = false;

	/**
	 * 
	 */
	protected String wand_mode;

	/**
	 * 
	 */
	protected Material item = Material.AIR;

	/**
	 * 
	 */
	protected ItemStack original_item;

	/**
	 * 
	 */
	public void setItemWasGiven(boolean given) {
		this.item_given = given;
	}

	/**
	 * 
	 */
	public boolean itemWasGiven() {
		return item_given;
	}

	/**
	 * 
	 * @param mode
	 */
	public void setWandMode(String mode) {
		wand_mode = mode;
	}

	/**
	 *
	 */
	public String getWandMode() {
		return wand_mode;
	}

	@Override
	public Material getItem() {
		return item;
	}

	public void setItem(Material material) {
		item = material;
	}

	/**
	 * 
	 * @param key
	 */
	public void setItemFromKey(String key) {
		item = Material.matchMaterial(key);
	}

	/**
	 * 
	 * @param item
	 */
	public void setOriginallyHeldItem(ItemStack item) {
		if (item.getType() != Material.AIR) {
			original_item = item;
		}
	}

	/**
	 * 
	 */
	public void disable(Player player) {
		final PlayerInventory inv = player.getInventory();
		if (itemWasGiven()) {
			int itemSlot;
			// Likely is what they're holding
			if (inv.getItemInMainHand().getType() == item) {
				itemSlot = inv.getHeldItemSlot();
			}
			else {
				itemSlot = InventoryUtils.inventoryHasItem(inv, item);
			}
			if (itemSlot > -1) {
				InventoryUtils.subtractAmountFromPlayerInvSlot(inv, itemSlot, 1);
				InventoryUtils.updateInventory(player);
			}
		}
		if (original_item != null) {
			InventoryUtils.moveItemToHand(inv, original_item.getType());
		}
	}
}
