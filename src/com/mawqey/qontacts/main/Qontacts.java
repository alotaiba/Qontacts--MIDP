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

/*
 * Definitions for Antenna Preprocessor to Target Devices:
 * MainScreen.java, ContactsListLoadThread.java, ContactsMultiUpdateThread.java
 * 
 * S60E5    - Symbian S60 5th Edition
 * 				Large icon80.png
 * S60E3FP2 - Symbian S60 3rd Edition FP2
 * 				Medium icon48.png
 * S60E3FP1 - Symbian S60 3rd Edition FP2
 * 				ContactsListLoadThread.java - Disable progress alerts
 * 				ContactsMultiUpdateThread.java - Disable progress alerts
 * 				Medium icon48.png
 * Series40 - Nokia Series 40 Any Edition
 * 				MainScreen.java - Disable opening the link
 * 				ContactsListLoadThread.java - Disable progress alerts
 * 				ContactsMultiUpdateThread.java - Disable progress alerts
 * 				Small icon26.png
 * 
 */

package com.mawqey.qontacts.main;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.mawqey.qontacts.i18n.L10nResources;
import com.mawqey.qontacts.screens.MainScreen;
import com.mawqey.qontacts.screens.helpers.AlertScreen;

public class Qontacts extends MIDlet {
	L10nResources l10n;
	public DisplayManager displayManager;
	public String mQontactsVersion;
	public String mQontactsVendor;
	public String mQontactsURL;
	private MainScreen mainScreen;
	/**
	 * Creates several screens and navigates between them.
	 */
	public Qontacts() {
		this.displayManager = new DisplayManager(Display.getDisplay(this));
		this.l10n = L10nResources.getL10nResources(null);
		this.mQontactsVersion = getAppProperty("MIDlet-Version");
		this.mQontactsVendor = getAppProperty("MIDlet-Vendor");
		this.mQontactsURL = "http://qontactsapp.com";
		
		Item[] items = new Item[5];
		items[0] = new StringItem(this.l10n.getString("STRING_MAIN_HOW_TO_TITLE"), "");
		items[1] = new TextField(null, this.l10n.getString("STRING_MAIN_BACKUP_CONTACTS"), this.l10n.getString("STRING_MAIN_BACKUP_CONTACTS").length(), TextField.UNEDITABLE);
		items[2] = new StringItem(null, this.mQontactsURL, StringItem.HYPERLINK);
		items[3] = new StringItem(this.l10n.getString("STRING_MAIN_TO_START"), "");
		items[4] = new TextField(null, this.l10n.getString("STRING_MAIN_INSTRUCTION"), this.l10n.getString("STRING_MAIN_INSTRUCTION").length(), TextField.UNEDITABLE);
		
		this.mainScreen = new MainScreen(this.l10n.getString("QONTACTS"), items, this);
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		this.displayManager.next(this.mainScreen);
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {}
	
	public void exitMIDlet() {
        try {
			destroyApp(true);
		} catch (MIDletStateChangeException e) {}
        notifyDestroyed();
    }
	
	public void showAbout() {
		String newLine = "\n";
		Character copyrightSign = new Character('\u00A9');
		
		String version = this.l10n.getString("VERSION") + ":" + newLine + "v." + this.mQontactsVersion + newLine + newLine;
		String developedBy = this.l10n.getString("DEVELOPED_BY") + ": " + this.l10n.getString("DEVELOPER") + newLine + newLine;
		String copyrightNotice = this.l10n.getString("COPYRIGHT") + copyrightSign + " 2010 " + this.l10n.getString("DEVELOPER") + ". " + this.l10n.getString("ALL_RIGHTS_RESERVED") + newLine;
		
		String aboutString = version + developedBy + copyrightNotice + this.mQontactsURL;
		
		AlertScreen about = new AlertScreen(this.l10n.getString("QONTACTS"), aboutString, AlertType.INFO, this);
		about.show();
	}
}
