package me.botsko.prism.utils;

import me.botsko.prism.events.BlockStateChange;
import me.botsko.prism.utils.MaterialTag.MatchMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bed.Part;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Locale;

public class BlockUtils {

    // Material at a location that are commonly acceptable to replace.
    private static final Tag<Material> replaceableMaterials = new MaterialTag(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR,
          Material.FIRE, Material.GRAVEL, Material.LAVA, Material.TALL_GRASS, Material.SAND, Material.SNOW, Material.SNOW_BLOCK,
          Material.WATER);

    // Material that has gravity (will fall, not break, when placed on the side of a wall or breaking the block under it)
    private static final MaterialTag fallingMaterials = new MaterialTag(Material.GRAVEL, Material.ANVIL, Material.DRAGON_EGG)
          .append("_CONCRETE_POWDER", MatchMode.SUFFIX)
          .append(Tag.SAND);

    // Material that will detach from the side of a block when that block is broken
    private static final MaterialTag fallsOffWall = new MaterialTag(Material.POWERED_RAIL, Material.DETECTOR_RAIL,
          Material.STICKY_PISTON, Material.PISTON, Material.PISTON_HEAD, Material.MOVING_PISTON, Material.TORCH,
          Material.LADDER, Material.LEVER, Material.REDSTONE_TORCH, Material.NETHER_PORTAL, Material.BEACON,
          Material.VINE, Material.COCOA, Material.TRIPWIRE_HOOK, Material.ACTIVATOR_RAIL, Material.ITEM_FRAME)
          .append(Tag.RAILS, Tag.BUTTONS, Tag.WALL_SIGNS)
          .append(MaterialTag.WALL_BANNERS);

    // Material that will detach from the top of a block when that block is broken
    private static final MaterialTag fallsOffTop = new MaterialTag(Material.STICKY_PISTON, Material.DEAD_BUSH,
          Material.PISTON, Material.PISTON_HEAD, Material.MOVING_PISTON, Material.TORCH, Material.REDSTONE,
          Material.WHEAT, Material.LEVER, Material.STONE_PRESSURE_PLATE, Material.REDSTONE_TORCH,
          Material.SNOW, Material.CACTUS, Material.SUGAR_CANE, Material.NETHER_PORTAL, Material.REPEATER,
          Material.PUMPKIN_STEM, Material.MELON_STEM, Material.LILY_PAD, Material.NETHER_WART, Material.CARROTS,
          Material.POTATOES, Material.BEETROOTS, Material.COMPARATOR, Material.BAMBOO, Material.TURTLE_EGG,
          Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.BEACON,
          Material.ITEM_FRAME, Material.CONDUIT)
          .append(Tag.DOORS, Tag.RAILS, Tag.SAPLINGS, MaterialTag.BANNERS, Tag.STANDING_SIGNS)
          .append(Tag.WOODEN_PRESSURE_PLATES, Tag.BUTTONS, Tag.CARPETS, Tag.FLOWER_POTS)
          .append(MaterialTag.ALL_PLANTS);

    // Material that will be detached by flowing water/lava
    private static final MaterialTag flowBreaks = new MaterialTag(MaterialTag.ALL_PLANTS, MaterialTag.CROPS, MaterialTag.SKULLS)
          .append(Material.CACTUS, Material.REPEATER, Material.COMPARATOR, Material.REDSTONE, Material.LEVER)
          .append(Material.REDSTONE_TORCH, Material.SUGAR_CANE, Material.TORCH, Material.TRIPWIRE)
          .append(Material.TRIPWIRE_HOOK, Material.VINE, Material.END_ROD)
          .append(Tag.BUTTONS, Tag.SAPLINGS, Tag.RAILS, Tag.FLOWER_POTS);

    // Material that can grow/spread to another location
    private static final MaterialTag growableStructure = new MaterialTag(Tag.LEAVES, Tag.LOGS)
          .append(Material.RED_MUSHROOM_BLOCK, Material.BROWN_MUSHROOM_BLOCK, Material.MUSHROOM_STEM);

    // Material that could possibly cause other material to detach from another block
    private static final EnumSet<Material> detachingBlocks = EnumSet.of(Material.AIR, Material.FIRE, Material.WATER,
          Material.LAVA);
    /**
     * There are several items that are officially different ItemStacks, but for the
     * purposes of what we're doing are really considered one core item. This
     * attempts to be a little lenient on matching the ids.
     * <p>
     * Example: Redstone lamp (off) is 123, (on) is 124 but either id means it's a
     * redstone lamp.
     *
     * @param id1
     * @param id2
     * @return
     */
    private static final EnumMap<Material, Material> baseMaterials = new EnumMap<>(Material.class);

    static {
        baseMaterials.put(Material.GRASS_BLOCK, Material.DIRT);
        baseMaterials.put(Material.MYCELIUM, Material.DIRT);
    }

    public static String dataString(BlockData data) {
        if (data != null) {
            return data.getAsString().replace("minecraft:" + data.getMaterial().name().toLowerCase(Locale.ENGLISH), "");
        }

        return "";
    }

    /**
     * /**
     *
     * @param mat
     * @param loc
     * @param radius
     */
    public static ArrayList<BlockStateChange> removeMaterialFromRadius(Material mat, Location loc, int radius) {
        final Material[] materials = {mat};
        return removeMaterialsFromRadius(materials, loc, radius);
    }

    /**
     * @param materials
     * @param loc
     * @param radius
     */
    public static ArrayList<BlockStateChange> removeMaterialsFromRadius(Material[] materials, Location loc,
                                                                        int radius) {
        final ArrayList<BlockStateChange> blockStateChanges = new ArrayList<>();
        if (loc != null && radius > 0 && materials != null && materials.length > 0) {
            final int x1 = loc.getBlockX();
            final int y1 = loc.getBlockY();
            final int z1 = loc.getBlockZ();
            final World world = loc.getWorld();
            for (int x = x1 - radius; x <= x1 + radius; x++) {
                for (int y = y1 - radius; y <= y1 + radius; y++) {
                    for (int z = z1 - radius; z <= z1 + radius; z++) {
                        loc = new Location(world, x, y, z);
                        final Block b = loc.getBlock();
                        if (b.getType().equals(Material.AIR))
                            continue;
                        if (Arrays.asList(materials).contains(loc.getBlock().getType())) {
                            final BlockState originalBlock = loc.getBlock().getState();
                            loc.getBlock().setType(Material.AIR);
                            final BlockState newBlock = loc.getBlock().getState();
                            blockStateChanges.add(new BlockStateChange(originalBlock, newBlock));
                        }
                    }
                }
            }
        }
        return blockStateChanges;
    }

    /**
     * Extinguish all the fire in a radius
     *
     * @param loc    The location you want to extinguish around
     * @param radius The radius around the location you are extinguish
     */
    public static ArrayList<BlockStateChange> extinguish(Location loc, int radius) {
        return removeMaterialFromRadius(Material.FIRE, loc, radius);
    }

    /**
     * Drains lava and water within (radius) around (loc).
     *
     * @param loc
     * @param radius
     */
    public static ArrayList<BlockStateChange> drain(Location loc, int radius) {
        final Material[] materials = {Material.LAVA, Material.WATER};
        return removeMaterialsFromRadius(materials, loc, radius);
    }

    /**
     * Drains lava blocks (radius) around player's loc.
     *
     * @param loc
     * @param radius
     */
    public static ArrayList<BlockStateChange> drainlava(Location loc, int radius) {
        final Material[] materials = {Material.LAVA};
        return removeMaterialsFromRadius(materials, loc, radius);
    }

    /**
     * Drains water blocks (radius) around player's loc.
     *
     * @param loc
     * @param radius
     */
    public static ArrayList<BlockStateChange> drainwater(Location loc, int radius) {
        final Material[] materials = {Material.WATER};
        return removeMaterialsFromRadius(materials, loc, radius);
    }

    /**
     * Determines if the material of an existing block at a location is something
     * that's commonly acceptable to replace.
     *
     * @param m the material of the block
     * @return if the material is acceptable to replace
     */
    public static boolean isAcceptableForBlockPlace(Material m) {
        return replaceableMaterials.isTagged(m);
    }

    /**
     * Recursively grabs a list of all blocks directly above Block that are
     * anticipated to fall.
     *
     * @param block the block to fetch blocks above
     * @return the list of blocks directly above the block
     */
    public static ArrayList<Block> findFallingBlocksAboveBlock(final Block block) {
        ArrayList<Block> falling_blocks = new ArrayList<>();

        // Get block above
        Block above = block.getRelative(BlockFace.UP);
        if (BlockUtils.isFallingBlock(above)) {
            falling_blocks.add(above);
            ArrayList<Block> fallingBlocksAbove = findFallingBlocksAboveBlock(above);
            if (fallingBlocksAbove.size() > 0) {
                falling_blocks.addAll(fallingBlocksAbove);
            }
        }
        return falling_blocks;
    }

    /**
     * Determine whether or not a block is capable of falling.
     * <p>
     * Seems like there's got to be another way to do this...
     *
     * @param block the block to check for the ability to fall
     * @return whether the block is capable of falling
     */
    public static boolean isFallingBlock(Block block) {
        return fallingMaterials.isTagged(block.getType());
    }

    /**
     * Searches for detachable blocks on the four acceptable sides of a block.
     *
     * @param block the block to check the sides of
     * @return the list of detachable block on the sides of the block
     */
    public static ArrayList<Block> findSideFaceAttachedBlocks(final Block block) {

        ArrayList<Block> detaching_blocks = new ArrayList<>();

        // Check each of the four sides
        Block blockToCheck = block.getRelative(BlockFace.EAST);
        if (BlockUtils.isSideFaceDetachableMaterial(blockToCheck.getType())) {
            detaching_blocks.add(blockToCheck);
        }
        blockToCheck = block.getRelative(BlockFace.WEST);
        if (BlockUtils.isSideFaceDetachableMaterial(blockToCheck.getType())) {
            detaching_blocks.add(blockToCheck);
        }
        blockToCheck = block.getRelative(BlockFace.NORTH);
        if (BlockUtils.isSideFaceDetachableMaterial(blockToCheck.getType())) {
            detaching_blocks.add(blockToCheck);
        }
        blockToCheck = block.getRelative(BlockFace.SOUTH);
        if (BlockUtils.isSideFaceDetachableMaterial(blockToCheck.getType())) {
            detaching_blocks.add(blockToCheck);
        }

        return detaching_blocks;

    }

    /**
     * Searches around a block for the first block of the given material
     *
     * @param block the block to search around
     * @param m     the material of the surrounding block to look for
     * @return the first surrounding block of the given material found
     */
    public static Block findFirstSurroundingBlockOfType(Block block, Material m) {
        Block blockToCheck = block.getRelative(BlockFace.EAST);
        if (blockToCheck.getType().equals(m)) {
            return blockToCheck;
        }
        blockToCheck = block.getRelative(BlockFace.WEST);
        if (blockToCheck.getType().equals(m)) {
            return blockToCheck;
        }
        blockToCheck = block.getRelative(BlockFace.NORTH);
        if (blockToCheck.getType().equals(m)) {
            return blockToCheck;
        }
        blockToCheck = block.getRelative(BlockFace.SOUTH);
        if (blockToCheck.getType().equals(m)) {
            return blockToCheck;
        }
        return null;
    }

    /**
     * Determine whether or not a block using the given material is going to detach
     * from the side of a block.
     *
     * @param m the material to check for detaching
     * @return whether a block with a given material will detach from the side of a
     * block
     */
    public static boolean isSideFaceDetachableMaterial(Material m) {
        return fallsOffWall.isTagged(m);
    }

    /**
     * Searches for detachable blocks on the four acceptable sides of a block.
     *
     * @param block
     * @return
     */
    public static ArrayList<Block> findTopFaceAttachedBlocks(final Block block) {
        ArrayList<Block> detaching_blocks = new ArrayList<>();

        // Find any block on top of this that will detach
        Block blockToCheck = block.getRelative(BlockFace.UP);
        if (BlockUtils.isTopFaceDetachableMaterial(blockToCheck.getType())) {
            detaching_blocks.add(blockToCheck);
            if (blockToCheck.getType().equals(Material.CACTUS) || blockToCheck.getType().equals(Material.SUGAR_CANE)) {
                // For cactus and sugar cane, we can even have blocks above
                ArrayList<Block> additionalBlocks = findTopFaceAttachedBlocks(blockToCheck);
                if (!additionalBlocks.isEmpty()) {
                    detaching_blocks.addAll(additionalBlocks);
                }
            }
        }

        return detaching_blocks;

    }

    /**
     * Determine whether or not a block is going to detach from the top of a block.
     *
     * @param m the material to check for detaching
     * @return whether a block with a given material will detach from the top of a
     * block
     */
    public static boolean isTopFaceDetachableMaterial(Material m) {
        return fallsOffTop.isTagged(m);
    }

    /**
     * Determine whether or not a block location is filled by a material that means
     * an attachable material is now detached.
     *
     * @param m
     * @return
     */
    public static boolean materialMeansBlockDetachment(Material m) {
        return detachingBlocks.contains(m);
    }

    /**
     * Searches for detachable entities in a
     *
     * @param block
     * @return
     */
    public static ArrayList<Entity> findHangingEntities(final Block block) {

        ArrayList<Entity> entities = new ArrayList<>();

        Entity[] foundEntities = block.getChunk().getEntities();
        if (foundEntities.length > 0) {
            for (Entity e : foundEntities) {
                // Some modded servers seems to list entities in the chunk
                // that exists in other worlds. No idea why but we can at
                // least check for it.
                // https://snowy-evening.com/botsko/prism/318/
                if (!block.getWorld().equals(e.getWorld()))
                    continue;
                // Let's limit this to only entities within 1 block of the current.
                if (block.getLocation().distance(e.getLocation()) < 2 && isHangingEntity(e)) {
                    entities.add(e);
                }
            }
        }

        return entities;

    }

    /**
     * Is an entity a hanging type, attachable to a block.
     *
     * @param entity the entity to check for being a hanging type
     * @return if an entity is a hanging type attachable to a block
     */
    public static boolean isHangingEntity(Entity entity) {
        EntityType type = entity.getType();

        switch (type) {
            case ITEM_FRAME:
            case PAINTING:
                return true;
            default:
                return false;
        }
    }

    public static BlockFace getRelativeFaceLeft(BlockFace in) {
        switch (in) {
            case NORTH:
                return BlockFace.WEST;

            case EAST:
                return BlockFace.NORTH;

            case SOUTH:
                return BlockFace.EAST;

            case WEST:
                return BlockFace.SOUTH;

            default:
                throw new IllegalArgumentException("Only cardinal directions are supported");
        }
    }

    public static BlockFace getRelativeFaceRight(BlockFace in) {
        switch (in) {
            case NORTH:
                return BlockFace.EAST;

            case EAST:
                return BlockFace.SOUTH;

            case SOUTH:
                return BlockFace.WEST;

            case WEST:
                return BlockFace.NORTH;

            default:
                throw new IllegalArgumentException("Only cardinal directions are supported");
        }
    }

    /**
     * Gets the other block that is part of a double length block
     *
     * @param block the block to get the sibling of
     */
    public static Block getSiblingForDoubleLengthBlock(Block block) {
        return getSiblingForDoubleLengthBlock(block.getState());
    }

    public static Block getSiblingForDoubleLengthBlock(BlockState block) {
		/*
		  Handle special double-length blocks
		 */
        BlockData data = block.getBlockData();

        if (data instanceof Chest) {
            Chest chest = (Chest) data;
            BlockFace facing = chest.getFacing();

            switch (chest.getType()) {
                case LEFT:
                    return block.getBlock().getRelative(getRelativeFaceRight(facing));

                case RIGHT:
                    return block.getBlock().getRelative(getRelativeFaceLeft(facing));

                case SINGLE:
                    return null;
            }
        } else if (data instanceof Bed) {
            Bed bed = (Bed) data;

            if (bed.getPart() == Part.FOOT) {
                return block.getBlock().getRelative(bed.getFacing());
            } else {
                return block.getBlock().getRelative(bed.getFacing().getOppositeFace());
            }
        } else if (data instanceof Bisected && !(data instanceof Stairs) && !(data instanceof TrapDoor)) {
            Bisected bisected = (Bisected) data;

            if (bisected.getHalf() == Half.BOTTOM) {
                return block.getBlock().getRelative(BlockFace.UP);
            } else {
                return block.getBlock().getRelative(BlockFace.DOWN);
            }
        }

        return null;
    }

    /**
     * Gets the block to log when interacting with a connected block. If not a
     * double block, the passed block is returned
     *
     * @param block
     */
    public static Block getBaseBlock(Block block) {
        BlockData data = block.getBlockData();

        if (data instanceof Bed) {
            Bed bed = (Bed) data;

            if (bed.getPart() == Part.HEAD) {
                return block.getRelative(bed.getFacing().getOppositeFace());
            }
        } else if (data instanceof Bisected && !(data instanceof Stairs) && !(data instanceof TrapDoor)) {
            Bisected bisected = (Bisected) data;

            if (bisected.getHalf() == Half.TOP) {
                return block.getRelative(BlockFace.DOWN);
            }
        }

        return block;
    }

    /**
     * @param m
     * @return
     */
    public static boolean canFlowBreakMaterial(Material m) {
        return flowBreaks.isTagged(m);
    }

    /**
     * @param m
     * @return
     */
    public static boolean materialRequiresSoil(Material m) {
        return MaterialTag.CROPS.isTagged(m);
    }

    /**
     * @param currBlock
     */
    public static ArrayList<Block> findConnectedBlocksOfType(Material type, Block currBlock,
                                                             ArrayList<Location> foundLocations) {

        ArrayList<Block> foundBlocks = new ArrayList<>();

        if (foundLocations == null) {
            foundLocations = new ArrayList<>();
        }

        foundLocations.add(currBlock.getLocation());

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 1; y++) {
                    Block newblock = currBlock.getRelative(x, y, z);
                    // ensure it matches the type and wasn't already found
                    if (newblock.getType() == type && !foundLocations.contains(newblock.getLocation())) {
                        foundBlocks.add(newblock);
                        ArrayList<Block> additionalBlocks = findConnectedBlocksOfType(type, newblock, foundLocations);
                        if (additionalBlocks.size() > 0) {
                            foundBlocks.addAll(additionalBlocks);
                        }
                    }
                }
            }
        }

        return foundBlocks;

    }

    /**
     * @param m
     * @param loc
     * @return
     */
    public static Block getFirstBlockOfMaterialBelow(Material m, Location loc) {
        for (int y = (int) loc.getY(); y > 0; y--) {
            loc.setY(y);
            if (loc.getBlock().getType().equals(m)) {
                return loc.getBlock();
            }
        }
        return null;
    }

    /**
     * @param m
     * @return
     */
    public static boolean isGrowableStructure(Material m) {
        return growableStructure.isTagged(m);
    }

    public static boolean areBlockIdsSameCoreItem(Material mat1, Material mat2) {

        // Get the obvious one out of the way.
        if (mat1 == mat2)
            return true;

        mat1 = baseMaterials.get(mat1);
        mat2 = baseMaterials.get(mat2);

        return mat1 != null && mat1 == mat2;
    }
}
