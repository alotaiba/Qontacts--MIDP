/**
 * Qontacts Mobile Application
 * Qontacts is a mobile application that updates the address book contacts
 * to the new Qatari numbering scheme.
 * 
 * Copyright (C) 2010  Abdulrahman Saleh Alotaiba
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mawqey.qontacts.screens.helpers;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Gauge;

import com.mawqey.qontacts.main.Qontacts;

public class ProgressAlert extends Alert implements CommandListener {
	
	public Gauge progressBar;
	private Qontacts midlet;

	//indefinite, and continuous running
	public ProgressAlert(String alertText, AlertType alertType, Qontacts midlet) {
		super("", alertText, null, alertType);
		this.midlet = midlet;
		this.progressBar = new Gauge(null, false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING);
		init();
	}
	
	//Preset values
	public ProgressAlert(String alertText, AlertType alertType, int maxValue, int initialValue, Qontacts midlet) {
		super("", alertText, null, alertType);
		this.midlet = midlet;
		this.progressBar = new Gauge(null, false, maxValue, initialValue);
		init();
	}
	
	private void init() {
		//Dummy command
		Command doNothing = new Command(" ", Command.CANCEL, 1000000);
		this.setTimeout(Alert.FOREVER);
		this.setIndicator(this.progressBar);
		this.addCommand(doNothing);
		this.setCommandListener(this);
	}
	
	public void show() {
		this.midlet.displayManager.next(this);
	}
	
	public void remove() {
		//TODO: Check if the current display is this alert before attempting to remove
		this.midlet.displayManager.back();
	}

	public void commandAction(Command command, Displayable displayable) {
		//Do nothing!
	}

}
