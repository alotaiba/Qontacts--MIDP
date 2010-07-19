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
//#ifndef Series40
//#define Series40
//#endif

package com.mawqey.qontacts.screens;
//#if S60E5||S60E3FP2||S60E3FP1
//@import javax.microedition.io.ConnectionNotFoundException;
//#endif
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;

import com.mawqey.qontacts.i18n.L10nResources;
import com.mawqey.qontacts.main.Qontacts;
import com.mawqey.qontacts.models.ContactsModel;
import com.mawqey.qontacts.threads.ContactsListLoadThread;

public class MainScreen extends Form implements CommandListener, ItemCommandListener {
	L10nResources l10n;
	private Command start;
	private Command about;
	private Command exit;
	private Command openLink;
	private Qontacts midlet;
	private ContactsModel contactsModel;
	
	public MainScreen(String title, Item[] items, Qontacts midlet) {
		super(title, items);
		this.l10n = L10nResources.getL10nResources(null);
		this.midlet = midlet;
		
		this.start = new Command(this.l10n.getString("START"), Command.OK, 1);
		this.about = new Command(this.l10n.getString("ABOUT"), Command.ITEM, 999);
		this.exit = new Command(this.l10n.getString("EXIT"), Command.EXIT, 1000);
		
		this.addCommand(this.start);
		this.addCommand(this.about);
		this.addCommand(this.exit);
		this.setCommandListener(this);
		//#if S60E5||S60E3FP2||S60E3FP1
//@		this.openLink = new Command(this.l10n.getString("OPEN"), Command.SCREEN, 1);
//@		items[2].setDefaultCommand(this.openLink);
//@		items[2].setItemCommandListener(this);
		//#endif
		this.midlet.displayManager.display.setCurrentItem(items[0]);
	}
	
	public void commandAction(Command command, Displayable displayable) {
		if (command == this.exit) {
			this.midlet.exitMIDlet();
		} else if (command == this.about) {
			this.midlet.showAbout();
		} else if (command == this.start) {
			this.contactsModel = new ContactsModel();
			this.contactsModel.openContactsDB();
			ContactsListLoadThread contactsListLoadThread = new ContactsListLoadThread(this.midlet, this.contactsModel);
			contactsListLoadThread.start();
		}
	}

	public void commandAction(Command command, Item item) {
		if (command == this.openLink) {
			//#if S60E5||S60E3FP2||S60E3FP1
//@			try {
//@				this.midlet.platformRequest(this.midlet.mQontactsURL);
//@			} catch (ConnectionNotFoundException e) {
//@				e.printStackTrace();
//@			}
			//#endif
		}
	}

}
