package me.botsko.prism.actions;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class EntityTravelAction extends GenericAction {

    /**
     *
     */
    protected EntityTravelActionData actionData;

    /**
     *
     */
    public EntityTravelAction() {
        actionData = new EntityTravelActionData();
    }

    /**
     * @param entity
     */
    public void setEntity(Entity entity) {
        if (entity != null) {
            if (entity instanceof Player) {
                setPlayer((Player) entity);
            } else {
                setSourceName(entity.getType().name().toLowerCase());
            }
        }
    }

    /**
     * @param to
     */
    public void setToLocation(Location to) {
        if (to != null) {
            actionData.to_x = to.getBlockX();
            actionData.to_y = to.getBlockY();
            actionData.to_z = to.getBlockZ();
        }
    }

    /**
     * @param cause
     */
    public void setCause(TeleportCause cause) {
        if (cause != null) {
            actionData.cause = cause.name().toLowerCase();
        }
    }

    @Override
    public boolean hasExtraData() {
        return actionData != null;
    }

    @Override
    public String serialize() {
        return gson().toJson(actionData);
    }

    @Override
    public void deserialize(String data) {
        if (data != null && data.startsWith("{")) {
            actionData = gson().fromJson(data, EntityTravelActionData.class);
        }
    }

    /**
     * @return
     */
    public EntityTravelActionData getActionData() {
        return actionData;
    }

    /**
     * @return
     */
    @Override
    public String getNiceName() {
        if (actionData != null) {
            final String cause = (actionData.cause == null ? "unknown" : actionData.cause.replace("_", " "));
            return "using " + cause + " to " + actionData.to_x + " " + actionData.to_y + " " + actionData.to_z;
        }
        return "teleported somewhere";
    }

    public class EntityTravelActionData {
        int to_x;
        int to_y;
        int to_z;
        String cause;
    }
}