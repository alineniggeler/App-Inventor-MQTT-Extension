package ch.bfh.ti.bachelor;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NetworkException;

public class Main {
	private static final String HOST = "localhost";
	private static final int PORT = 4223;
	private static String broker_ip = "tcp://147.87.116.33:1883";
	private static String clientId = "sensors";

	public static void main(String[] args) throws NetworkException, AlreadyConnectedException {
		MqttDebug mqtt = new MqttDebug();
		mqtt.connection(broker_ip, clientId);
		IPConnection ipcon = new IPConnection();
		ipcon.connect(HOST, PORT);

		RGBButton rlb = new RGBButton();
		rlb.establishConnection(ipcon);

		SoundPressure spl = new SoundPressure();
		spl.establishConnection(ipcon);

		// Synchronous
		// mqtt.subscribe("color", new IMqttMessageListener() {
		//
		//
		// @Override
		// public void messageArrived(String topic, MqttMessage message) throws
		// Exception {
		// printColor(rlb, message.toString());
		//
		// }
		// });

		mqtt.subscribe("color", new MqttCallback() {
			@Override
			public void connectionLost(Throwable cause) {
				System.out.println("Connection Lost!");
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				printColor(rlb, message.toString());
				
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
				System.out.println("Delivery Complete");
			}
		});

		mqtt.publish("color", "blue");
		while (true) {
			double decibel = spl.getDecibel();
			mqtt.publish("soundpressure", "" + decibel);
			System.out.println(decibel);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

	}
	


	private static void printColor(RGBButton rlb, String color) {
		System.out.println(color);
		if(color.contains("0")) {
			rlb.setColor(0, 0, 0);
		}else if(color.contains("1")){
			rlb.setColor(255, 255, 255);
		}else {
		switch (color) {
		case "blue":
			rlb.setColor(0, 0, 255);
			break;
		case "red":
			rlb.setColor(255, 0, 0);
			break;
		case "green":
			rlb.setColor(0, 255, 0);

		}}

	}
	
}
