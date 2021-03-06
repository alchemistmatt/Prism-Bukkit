package me.botsko.prism.actions;

public class PlayerDeathAction extends GenericAction {

	/**
	 * 
	 */
	protected String cause;

	/**
	 * 
	 */
	protected String attacker;

	/**
	 * 
	 * @param cause
	 */
	public void setCause(String cause) {
		this.cause = cause;
	}

	/**
	 * 
	 * @param attacker
	 */
	public void setAttacker(String attacker) {
		this.attacker = attacker;
	}

	/**
	 * 
	 */
	
	@Override
	public void deserialize(String data) {
		if (data != null) {
			final String[] dataArr = data.split(":");
			cause = dataArr[0];
			if (dataArr.length > 1) {
				attacker = dataArr[1];
			}
		}
	}

	@Override
	public boolean hasExtraData() {
		return cause!=null;
	}

	@Override
	public String serialize() {
		if(cause != null) {
			if(attacker != null) {
				return cause + ":" + attacker;
			}
			else {
				return cause;
			}
		}
		
		return "";
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String getNiceName() {
		String name = "";
		if (attacker != null && !attacker.isEmpty()) {
			name += attacker;
		}
		if (cause != null && !cause.isEmpty()) {
			name += "(" + cause + ")";
		}
		return name;
	}
}