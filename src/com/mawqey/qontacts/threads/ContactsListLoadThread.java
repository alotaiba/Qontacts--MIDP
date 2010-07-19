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

package com.mawqey.qontacts.threads;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
//#if S60E5||S60E3FP2
//@import javax.microedition.lcdui.AlertType;
//#endif
import javax.microedition.pim.Contact;

import com.mawqey.qontacts.i18n.L10nResources;
import com.mawqey.qontacts.main.Qontacts;
import com.mawqey.qontacts.models.ContactsModel;
import com.mawqey.qontacts.screens.ContactsListScreen;
//#if S60E5||S60E3FP2
//@import com.mawqey.qontacts.screens.helpers.ProgressAlert;
//#endif

public class ContactsListLoadThread extends Thread {
	L10nResources l10n;
	//#if S60E5||S60E3FP2
//@	private ProgressAlert analyzeProgress;
	//#endif
	private Qontacts midlet;
	private ContactsModel contactsModel;
	
	public ContactsListLoadThread(Qontacts midlet, ContactsModel contactsModel) {
		this.l10n = L10nResources.getL10nResources(null);
		this.midlet = midlet;
		this.contactsModel = contactsModel;
		//#if S60E5||S60E3FP2
//@		this.analyzeProgress = new ProgressAlert(this.l10n.getString("ANALYZING_CONTACTS") + " ...", AlertType.ALARM, this.midlet);
		//#endif
	}
	
	public void run() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//#if S60E5||S60E3FP2
//@		this.analyzeProgress.show();
		//#endif
		Vector contactItems = this.contactsModel.getContactsItems();
		Vector contactNames = new Vector();
		for (Enumeration e = contactItems.elements(); e.hasMoreElements();)
		{
			Contact contact = (Contact) e.nextElement();
			Hashtable contactNumbers = this.contactsModel.updateableContactNumbers(contact);
			Vector updatedNumbers = (Vector) contactNumbers.get("Updated Numbers");
			String contactName = this.contactsModel.getContactName(contact) + " (" + updatedNumbers.size() + ")";
			contactNames.addElement(contactName);
		}
		int count = contactNames.size(); 
		String[] contactNamesStrings = new String[count]; 
		contactNames.copyInto(contactNamesStrings);
		//#if S60E5||S60E3FP2
//@		this.analyzeProgress.remove();
		//#endif
		
		//Mark all as selected
		
		ContactsListScreen contactListScreen = new ContactsListScreen(this.l10n.getString("CONTACTS"), contactNamesStrings, contactItems, this.midlet, this.contactsModel);
		
        boolean[] arrTrue = new boolean[contactListScreen.size()];
        for(int i = 0; i < arrTrue.length; i++) {
        	arrTrue[i] = true;
        }
        contactListScreen.setSelectedFlags(arrTrue);
		
		this.midlet.displayManager.next(contactListScreen);
	}
}
