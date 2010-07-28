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
//#ifndef S60E5
//#define S60E5
//#endif

package com.mawqey.qontacts.threads;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.pim.Contact;

import com.mawqey.qontacts.i18n.L10nResources;
import com.mawqey.qontacts.main.Qontacts;
import com.mawqey.qontacts.models.ContactsModel;
import com.mawqey.qontacts.screens.ContactsListScreen;
//#if S60E5||S60E3FP2
import com.mawqey.qontacts.screens.helpers.ProgressAlert;
//#endif
public class ContactsMultiUpdateThread extends Thread {
	L10nResources l10n;
	//#if S60E5||S60E3FP2
	private ProgressAlert updateProgress;
	//#endif
	private ContactsListScreen listScreen;
	private Qontacts midlet;
	private ContactsModel contactsModel;
	private int selectedContactsCount;
	private int selectedContactsConvertedCount;
	private boolean[] contactsSelectionState;
	
	public ContactsMultiUpdateThread(ContactsListScreen listScreen, Qontacts midlet, ContactsModel contactsModel) {
		this.l10n = L10nResources.getL10nResources(null);
		this.midlet = midlet;
		this.contactsModel = contactsModel;
		this.listScreen = listScreen;
		this.contactsSelectionState = new boolean[this.listScreen.size()];
		this.selectedContactsCount = this.listScreen.getSelectedFlags( this.contactsSelectionState );
		this.selectedContactsConvertedCount = 0;
		//#if S60E5||S60E3FP2
		this.updateProgress = new ProgressAlert(this.l10n.getString("UPDATING_CONTACTS") + " (" + this.selectedContactsConvertedCount + "/" + this.selectedContactsCount + ")", AlertType.INFO, this.selectedContactsCount, this.selectedContactsConvertedCount, this.midlet);
		//#endif
	}
	
	private Vector getContactListSelectedItemsIndices() {
		Vector indices = new Vector();
		
		for (int i = 0; i < this.listScreen.size(); i++) {
			if (this.contactsSelectionState[i]) {
				indices.addElement(new Integer(i));
			}
		}
		
		return indices;
	}
	
	public void run() {
		ContactsMultiUpdateDataThread updateDataThread = new ContactsMultiUpdateDataThread();
		try {
			updateDataThread.start();
			//#if S60E5||S60E3FP2
			Thread.sleep(100);
			this.updateProgress.show();
			//#endif
			while(this.selectedContactsConvertedCount < this.selectedContactsCount) {
				//#if S60E5||S60E3FP2
				this.updateProgress.progressBar.setValue(this.selectedContactsConvertedCount);
				this.updateProgress.setString(this.l10n.getString("UPDATING_CONTACTS") + " (" + this.selectedContactsConvertedCount + "/" + this.selectedContactsCount + ")");
				//#endif
				Thread.sleep(50);
			}
			//#if S60E5||S60E3FP2
			this.updateProgress.progressBar.setValue(this.selectedContactsCount);
			this.updateProgress.setString(this.l10n.getString("UPDATING_CONTACTS") + " (" + this.selectedContactsCount + "/" + this.selectedContactsCount + ")");
			Thread.sleep(500);
			this.updateProgress.remove();
			//#endif
			Thread.sleep(150);
			Alert successAlert = new Alert(this.l10n.getString("SUCCESS"), this.selectedContactsConvertedCount + " " + this.l10n.getString("STRING_CONTACTSSCREEN_UPDATE_QUESTION_CONTACTS") + " " + this.l10n.getString("STRING_CONTACTSSCREEN_UPDATED_SUCCESSFULLY"), null, AlertType.CONFIRMATION);
			successAlert.setTimeout(2000);
			this.midlet.displayManager.display.setCurrent(successAlert, this.listScreen);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private class ContactsMultiUpdateDataThread extends Thread {
		
		public ContactsMultiUpdateDataThread() { }
		
		public void run() {
			Vector selectedItemsIndices = ContactsMultiUpdateThread.this.getContactListSelectedItemsIndices();
			for (int i = ContactsMultiUpdateThread.this.selectedContactsCount - 1; i >= 0; i--) {
				int index = ((Integer) selectedItemsIndices.elementAt(i)).intValue();
				Contact contact = (Contact) ContactsMultiUpdateThread.this.listScreen.deviceContacts.elementAt(index);
				if (ContactsMultiUpdateThread.this.contactsModel.convertContactNumbers(contact)) {
					ContactsMultiUpdateThread.this.selectedContactsConvertedCount++;
					ContactsMultiUpdateThread.this.listScreen.deleteContactsListItem(index);
				}
			}
		}
		
	}

}
