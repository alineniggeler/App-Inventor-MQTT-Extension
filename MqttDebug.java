package ch.bfh.ti.bachelor;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public class MqttDebug {
	private MqttClient client;
	
	public void connection(String brokerIP, String clientId) {
		try {
			client = new MqttClient(brokerIP, clientId,null);
		} catch (MqttException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(false);
		try {
			client.connect(connOpts);
		} catch (MqttSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void publish(String topic, String text) {
		try {
			
			
			MqttMessage message = new MqttMessage(text.getBytes());
			client.publish(topic, text.getBytes(), 0, false);
	
			
			
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void subscribe(String topic, IMqttMessageListener iMqttMessageListener) {
		try {
			client.subscribe(topic,iMqttMessageListener);
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}
	
	public void subscribe(String topic, MqttCallback callback) {
		client.setCallback(callback);
		try {
			client.subscribe(topic);
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

}
