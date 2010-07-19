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

import com.mawqey.qontacts.i18n.L10nResources;
import com.mawqey.qontacts.main.Qontacts;

public class AlertScreen extends Alert implements CommandListener {
	L10nResources l10n;
	private Qontacts midlet;
	private Command dismiss;

	public AlertScreen(String title, Qontacts midlet) {
		super(title);
		this.l10n = L10nResources.getL10nResources(null);
		this.midlet = midlet;
		initWithOK();
	}

	public AlertScreen(String title, String alertText, AlertType alertType, Qontacts midlet) {
		super(title, alertText, null, alertType);
		this.l10n = L10nResources.getL10nResources(null);
		this.midlet = midlet;
		initWithOK();
	}
	
	private void initWithOK() {
		//Dummy command
		this.dismiss = new Command(this.l10n.getString("OK"), Command.OK, 1);
		this.setTimeout(Alert.FOREVER);
		this.addCommand(this.dismiss);
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
		if (command == this.dismiss || command == Alert.DISMISS_COMMAND) {
			this.remove();
		}
	}

}
