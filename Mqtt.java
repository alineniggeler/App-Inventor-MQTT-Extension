package ch.bfh.ti.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import android.app.Activity;


/**
 * MQTT Extension for MIT App Inventor
 * Uses the MQTT Client library of Eclipse Paho
 * author Aline Niggeler
 */
@UsesLibraries(libraries = "org.eclipse.paho.client.mqttv3-1.2.1.jar")
@DesignerComponent(version = Mqtt.VERSION, description = "MQTT is a publish/ subscribe protocol to transfer data from a to b. To" +
		"be able to transmit data, you need a broker which is accessible. Also remark that you need a Internet connection to use it", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@SimpleObject(external = true)
public class Mqtt extends AndroidNonvisibleComponent {

	private final ComponentContainer container;
	public static final int VERSION = 1;
	private String mqttMessage ="0.0";
	private MqttHelper mHelper;
	private Activity activity;

	/**
	 * Creates a Mqtt component and an instance of MqttHelper class
	 * @param container container component will be placed in
	 */
	public Mqtt(ComponentContainer container) {
		super(container.$form());
		this.activity = container.$context();
		this.container = container;
		mHelper = new MqttHelper();

	}

	/**
	 * Calls the createClient method in MqttHelper class, which creates a new MqttClient
	 * @param broker IP-Address to broker, needs to have format tcp://your.ip.address:portNumber
	 *                  you can replace tcp with ssl for tls/ssl connection, ws or wss for webSocket/webSocketSecure
	 * @param clientID	the name for your connection to the broker
	 * @param password	for broker which are password protected
	 */
	@SimpleFunction(description = "This block creates a new client with your indicated client name and password. The format for broker has to be: " +
			"tcp://your.ip.address:port or tcp://your.dns.broker:port You can change the port number. Remark that " +
			"your broker has to be configured to receive data over any other port number except 1883")
	public void CreateClient(String broker, String clientID, String password){
		mHelper.createClient(broker, clientID, password);
	}

	/**
	 * Calls the connect method in MqttHelper class, which establish a connection to a broker
	 * @param cleanSession enables maintaining of state information between sessions
	 * @param topicLastWill	if client disconnects ungracefully, the will message will be send on this topic
	 * @param messageLastWill sets the will message
	 * @param qosLastWill	sets the will quality of service
	 * @param retainedLastWill	indicates whether the message should be retained or not
	 */
	@SimpleFunction(description = "Connect to a broker. The clean session is for maintaining state information between " +
			"each session. You have the option to indicate a last will. If the client disconnects ungracefully " +
			"this message will be send on this topic with this quality of service. You can specify whether the message should be" +
            " saved by the broker or not with retainedLastWill. If you do not want to set any last will, set topic and message to an empty " +
			"text, quality of service to 0 and retained to false.")
	public void Connect(boolean cleanSession, String topicLastWill, String messageLastWill, int qosLastWill, boolean retainedLastWill ) throws Exception{

		mHelper.connect(cleanSession, topicLastWill, messageLastWill, qosLastWill, retainedLastWill);
	}

	/**
	 * Calls the publish method in MqttHelper class, which publishes the message on a specified topic
	 * @param topic topic on which the message is published
	 * @param message the published message
	 * @param qualityOfService  specify the quality of service with 0, 1 , 2
	 * @param retainedMessage indicate whether the message should be retained or not
	 */
	@SimpleFunction(description = "Publish your message on a specified topic. The quality of service can be specified with" +
            " 0: at most once, 1: at least once, 2: exactly once. Also you can define whether the message should be retained by broker")
	public void Publish(String topic, String message, int qualityOfService, boolean retainedMessage) throws Exception {
		mHelper.publish(topic, message, qualityOfService, retainedMessage);
	}

	/**
	 * Calls the subscribe method in MqttHelper class, which lets the client subscribe to a topic
     * It also creates a new MqttCallback, which calls either messageArrived, deliveryComplete or connectionLost
	 * @param topic topic to subscribe
	 * @param qualityOfService specify the quality of service with 0, 1, 2
	 */
	@SimpleFunction(description = "Subscribe to a specified topic. The quality of service can be specified with" +
            " 0: at most once, 1: at least once, 2: exactly once")
	public void Subscribe(String topic, int qualityOfService) throws Exception {
		mHelper.subscribe(topic, qualityOfService, new MqttCallback() {

			@Override
			public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
				MessageArrived(arg0, arg1.toString());

			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {
				DeliveryComplete(new String("Delivery Complete"));

			}

			@Override
			public void connectionLost(Throwable arg0) {
				ConnectionLost(new String("Connection was lost"));

			}
		});
	}

	/**
	 * This event will be triggered, when a message is received. It runs on the UI-Thread
	 * @param topic the subscribed topic
	 * @param message the received message
	 */
	@SimpleEvent(description = "This event let you receive a message from a subscribed topic. You can display both topic " +
            "and message")
	public void MessageArrived(final String topic, final String message) {
		activity.runOnUiThread(new Runnable() {

		    @Override
		    public void run() {
		    	EventDispatcher.dispatchEvent(Mqtt.this, "MessageArrived", topic, message);
		    }
		});
	}

	/**
	 * This event will be triggered, when the receiving of data failed due to lost of connection
     * Runs on UI-Thread
	 * @param message the messsage that will be displayed which is defined in Subscribe method
	 */
	@SimpleEvent(description = "This event let you know if the message form a subscribed topic failed due to lost of connection")
	public void ConnectionLost(final String message) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				EventDispatcher.dispatchEvent(Mqtt.this, "ConnectionLost", message);
			}
		});

	}

	/**
	 * This event will be triggered, when the subscriber has received the message
     * Runs on UI-Thread
	 * @param message the message that will be displayed which is defined in Subscribe method
	 */
	@SimpleEvent(description ="This event lets you know if the delivery of the message was completed")
	public void DeliveryComplete(final String message) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				EventDispatcher.dispatchEvent(Mqtt.this, "DeliveryComplete", message);
			}
		});
	}


	/**
	 * Calls the unsubscribe method in the MqttHelper class, which will unsubscribe the subscription of a
     * specified topic
	 * @param topic the unsubscribe topic
	 */
	@SimpleFunction(description = "Unsubscribe from a specified topic. You will no longer receive message of this topic")
	public void Unsubscribe(String topic) throws Exception {
		mHelper.unsubscribe(topic);
	}


	/**
	 * Calls the disconnect method in the MqttHelper class, which will disconnect the clinet from the broker
	 */
	@SimpleFunction(description = "Disconnect the client from broker")
	public void Disconnect() throws Exception {
		mHelper.disconnect();
	}

	/**
	 * Calls the isConnected method in MqttHelper class. This shows, if the client is connected to a broker
	 * @return true if the client has established a connection to a broker
	 */
	@SimpleProperty(description = "Receive true, if the client is connected to a broker and ready to publish and subscribe " +
            "otherwise false")
	public boolean IsConnected() { return mHelper.isConnected();}

	/**
	 * Calls the isCreated method in MqttHelper class. This shows, if the client has been created
	 * @return true if the mqttClient is not null
	 */
	@SimpleProperty(description = "Receive true, if the client has been successfully created, otherwise false")
	public boolean IsCreated(){
		return mHelper.isCreated();
	}

}
