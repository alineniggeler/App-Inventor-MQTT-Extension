package ch.bfh.ti.bachelor;

import com.tinkerforge.BrickletRGBLEDButton;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class RGBButton {
	
	
	private static final String UID = "D41";
	private BrickletRGBLEDButton rlb;
	
	public void establishConnection(IPConnection ipcon) {
		rlb = new BrickletRGBLEDButton(UID, ipcon);
		
	}
	
	public void setColor(int red, int green, int blue) {
		try {
			rlb.setColor(red, green, blue);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
