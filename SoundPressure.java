package ch.bfh.ti.bachelor;

import com.tinkerforge.BrickletSoundPressureLevel;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class SoundPressure {
	
	private static final String UID = "FuT";
	private BrickletSoundPressureLevel spl;
	private int decibel;
	
	public void establishConnection(IPConnection ipcon) {
		spl = new BrickletSoundPressureLevel(UID, ipcon);
	}
	
	public  double getDecibel() {
		try {
			decibel = spl.getDecibel();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decibel/10.0;
	}

}
