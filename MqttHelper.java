package ch.bfh.ti.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import com.google.appinventor.components.annotations.UsesLibraries;

/**
 * Helper Class for MQTT Extension for MIT App Inventor Uses the MQTT Client
 * library of Eclipse Paho author Aline Niggeler
 */
@UsesLibraries(libraries = "org.eclipse.paho.client.mqttv3-1.2.1.jar")
public class MqttHelper {

	private MqttConnectOptions mqttConnectOptions;
	private MqttClient mqttClient = null;
	private boolean connected = false;
	private String password = "";

	/**
	 * Creates a new client
	 * 
	 * @param ip
	 *            IP-Address to broker, format scheme://ipAddress:port schemes
	 *            available tcp
	 * @param client
	 *            name for client
	 * @param password
	 *            password if the broker is password protected
	 */
	public void createClient(String ip, String client, String password) {
		this.password = password;
		try {
			MqttClient tmpClient = new MqttClient(ip, client, null);
			if (mqttClient == null || (!mqttClient.getClientId().equals(tmpClient.getClientId()) && client != null)
					|| !mqttClient.getServerURI().equals(tmpClient.getServerURI())) {
				mqttClient = tmpClient;
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Connect to a broker with connect options
	 * 
	 * @param cleanSession
	 *            enables maintaining of state information between sessions
	 * @param topic
	 *            topic for the last will
	 * @param message
	 *            message for the last will
	 * @param qos
	 *            quality of service for the last will
	 * @param retained
	 *            indicates whether the message should be retained or not
	 */
	public void connect(boolean cleanSession, String topic, String message, int qos, boolean retained)
			throws Exception {
		if (mqttClient == null) {
			throw new Exception("Client is not created yet");
		}
		if (connected == false) {
			mqttConnectOptions = new MqttConnectOptions();
			mqttConnectOptions.setCleanSession(cleanSession);
			mqttConnectOptions.setPassword(password.toCharArray());
			if (!topic.isEmpty()) {
				mqttConnectOptions.setWill(topic, message.getBytes(), qos, retained);
			}
			try {
				mqttClient.connect(mqttConnectOptions);
				connected = true;
			} catch (MqttException e) {
				throw new Exception("Could not connect to server", e);
			}
		}

	}

	/**
	 * publishes the message to the specified topic
	 * 
	 * @param topic
	 *            the topic to publish
	 * @param message
	 *            the published message
	 * @param qos
	 *            quality of service 0,1 or 2
	 * @param retained
	 *            indicates whether the message should be retained or not
	 */
	public void publish(String topic, String message, int qos, boolean retained) throws Exception {
		if (mqttClient == null) {
			throw new Exception("Client is not created yet");
		}
		try {
			mqttClient.publish(topic, message.getBytes(), qos, retained);
		} catch (MqttException e) {
			throw new Exception("Could not publish message", e);
		}
	}

	/**
	 * subscribe on a specified topic with a quality of service
	 * 
	 * @param topic
	 *            topic to subscribe
	 * @param qos
	 *            quality of service 0,1 or 2
	 * @param callback
	 *            callback to receive data and display it
	 */
	public void subscribe(String topic, int qos, MqttCallback callback) throws Exception {
		if (mqttClient == null) {
			throw new Exception("Client is not created yet");
		}
		mqttClient.setCallback(callback);
		try {
			mqttClient.subscribe(topic, qos);
		} catch (MqttException e) {
			throw new Exception("Could not subscribe to topic");
		}

	}

	/**
	 * unsubscribe from a topic
	 * 
	 * @param topic
	 *            topic to unsubscribe
	 */
	public void unsubscribe(String topic) throws Exception {
		if (mqttClient == null) {
			throw new Exception("Client is not created yet");
		}
		try {
			mqttClient.unsubscribe(topic);
		} catch (MqttException e) {
			throw new Exception("Could not unsubscribe from topic", e);
		}

	}

	/**
	 * client disconnects from a broker
	 */
	public void disconnect() throws Exception {
		if (mqttClient == null) {
			throw new Exception("Client is not created yet");
		}
		if (connected == true) {
			try {
				mqttClient.disconnect();
				connected = false;
			} catch (MqttException e) {
				throw new Exception("Could not disconnect from broker");
			}
		}
	}

	/**
	 * show if client is connected
	 * 
	 * @return true if connected, false if not connected
	 */
	public boolean isConnected() {
		return isCreated() && mqttClient.isConnected();
	}

	/**
	 * show if client has been created
	 * 
	 * @return true if client is created, false if not
	 */
	public boolean isCreated() {
		return mqttClient != null;
	}

}