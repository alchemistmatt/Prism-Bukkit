package me.botsko.prism.actions;

import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actionlibs.QueryParameters;
import me.botsko.prism.appliers.ChangeResult;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Handler {
    /**
     * @return the id
     */
    long getId();

    /**
     * @param id the id to set
     */
    void setId(long id);

    /**
     * @return the action_time
     */
    long getUnixEpoch();

    /**
     * @param epoch the display_time to set
     */
    void setUnixEpoch(long epoch);

    /**
     * @return the display_date
     */
    String getDisplayDate();

    /**
     * @return the display_time
     */
    String getDisplayTime();

    boolean hasExtraData();

    /**
     * @return
     */
    String getTimeSince();

    /**
     * @return the action_type
     */
    ActionType getActionType();

    /**
     * @param type
     */
    void setActionType(ActionType type);

    /**
     * @param world the world to set
     */
    void setWorld(World world);

    Location getLoc();

    /**
     * @return the name of the event cause
     */
    String getSourceName();

    /**
     * @param name the custom name for the event cause
     */
    void setSourceName(String name);


    /**
     * @param x the x to set
     */
    void setX(double x);

    /**
     * @param y the y to set
     */
    void setY(double y);

    /**
     * @param z the z to set
     */
    void setZ(double z);

    /**
     *
     */
    Material getMaterial();

    /**
     * @param material
     */
    void setMaterial(Material material);

    /**
     *
     */
    BlockData getBlockData();

    /**
     * @param state
     */
    void setBlockData(BlockData state);

    short getDurability();

    void setDurability(short durability);

    String serialize();

    void deserialize(String data);

    /**
     *
     */
    Material getOldMaterial();

    /**
     * @param material
     */
    void setOldMaterial(Material material);

    /**
     *
     */
    BlockData getOldBlockData();

    /**
     * @param state
     */
    void setOldBlockData(BlockData state);

    short getOldDurability();

    void setOldDurability(short durability);

    /**
     * @return
     */
    int getAggregateCount();

    /**
     * @param aggregateCount
     */
    void setAggregateCount(int aggregateCount);

    /**
     *
     */
    String getNiceName();

    UUID getUUID();

    void setUUID(UUID uuid);

    /**
     *
     */
    boolean isCanceled();

    /**
     * @param cancel
     */
    void setCanceled(boolean cancel);

    /**
     * @param player
     * @param parameters
     * @param is_preview
     * @return
     */
    ChangeResult applyRollback(Player player, QueryParameters parameters, boolean is_preview);

    /**
     * @param player
     * @param parameters
     * @param is_preview
     * @return
     */
    ChangeResult applyRestore(Player player, QueryParameters parameters, boolean is_preview);

    /**
     * @param player
     * @param parameters
     * @param is_preview
     * @return
     */
    ChangeResult applyUndo(Player player, QueryParameters parameters, boolean is_preview);

    /**
     * @param player
     * @param parameters
     * @param is_preview
     * @return
     */
    ChangeResult applyDeferred(Player player, QueryParameters parameters, boolean is_preview);

    String getCustomDesc();

    void setCustomDesc(String description);

}