package com.changcai.buyer.listener;


/**
 * 监听接口.
 */
public interface DialogListener {

	public void setPositiveAction(String name);

	public void setOnCancelAction(String name);
	
	public void setOtherAction(String name);
	
	public void setNegativeAction(String name);
	
}
