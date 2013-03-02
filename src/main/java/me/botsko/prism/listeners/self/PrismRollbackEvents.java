package me.botsko.prism.listeners.self;

import java.util.ArrayList;

import me.botsko.prism.Prism;
import me.botsko.prism.actions.PrismProcessAction;
import me.botsko.prism.actions.PrismRollbackAction;
import me.botsko.prism.appliers.PrismProcessType;
import me.botsko.prism.events.BlockStateChange;
import me.botsko.prism.events.PrismBlocksRollbackEvent;

import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PrismRollbackEvents implements Listener {

	
	/**
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPrismBlocksRollbackEvent(final PrismBlocksRollbackEvent event){

		// Get all block changes for this event
		ArrayList<BlockStateChange> blockStateChanges = event.getBlockStateChanges();
		if(!blockStateChanges.isEmpty()){
			
			// Create an entry for the rollback as a whole
			PrismProcessAction primaryAction = new PrismProcessAction("prism-process", PrismProcessType.ROLLBACK, event.onBehalfOf(), event.getCommandParams() );
			int id = Prism.actionsRecorder.insertActionIntoDatabase( primaryAction );
			if(id == 0){
				return;
			}
			for(BlockStateChange stateChange : blockStateChanges){
				
				BlockState orig = stateChange.getOriginalBlock();
				BlockState newBlock = stateChange.getNewBlock();

				// Build the action
				PrismRollbackAction action = new PrismRollbackAction("prism-rollback", orig.getTypeId(), orig.getRawData(), newBlock.getTypeId(), newBlock.getRawData(), event.onBehalfOf().getName(), id);
				action.setWorldName(orig.getWorld().getName());
				action.setX(orig.getX());
				action.setY(orig.getY());
				action.setZ(orig.getZ());

				Prism.actionsRecorder.addToQueue( action );
			}
			Prism.actionsRecorder.save();
		}
	}
}