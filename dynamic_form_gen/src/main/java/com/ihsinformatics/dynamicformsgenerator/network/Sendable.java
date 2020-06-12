package com.ihsinformatics.dynamicformsgenerator.network;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author naveed.iqbal
 * @description: this interface serves as a communication bridge between any
 *               class that need network communication and SendDataToServer
 *               class. Requester class need to implement this interface's
 *               recievedResponse(String resp, int repId) method,
 *               SendDataToServer class will call that recievedResponse(String
 *               resp, int repId) after completing the communication task
 * 
 * 
 */
public interface Sendable {

	public void send(JSONObject data, int respId);
	public void onResponseReceived(JSONObject resp, int respId);

}
