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
package com.mawqey.qontacts.screens;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.pim.Contact;

import com.mawqey.qontacts.i18n.L10nResources;
import com.mawqey.qontacts.main.Qontacts;
import com.mawqey.qontacts.models.ContactsModel;
import com.mawqey.qontacts.screens.helpers.AlertScreen;
import com.mawqey.qontacts.threads.ContactsMultiUpdateThread;
import com.mawqey.qontacts.threads.ListMarkThread;

public class ContactsListScreen extends List implements CommandListener {
	L10nResources l10n;
	public Vector deviceContacts;
	private Qontacts midlet;
	private ContactsModel contactsModel;
	private Alert confirm;
	
	private Command confirmUpdate;
	private Command confirmCancel;
	
	private Command back;
	private Command previewContact;
	private Command updateSelectedContacts;
	private Command markAll;
	private Command unmarkAll;
	private Command about;
	private Command exit;

	public ContactsListScreen(String title, String[] stringElements, Vector deviceContacts, Qontacts midlet, ContactsModel contactsModel) {
		super(title, Choice.MULTIPLE, stringElements, null);
		this.l10n = L10nResources.getL10nResources(null);
		//Set up the midlet to access its methods
		this.midlet = midlet;
		this.contactsModel = contactsModel;
		this.deviceContacts = deviceContacts;
		this.setFitPolicy(TEXT_WRAP_ON);
		init();
	}
	
	public void init() {
		this.confirm = new Alert(null);
		this.confirmUpdate = new Command(this.l10n.getString("OK"), Command.OK, 1);
		this.confirmCancel = new Command(this.l10n.getString("CANCEL"), Command.CANCEL, 1);
		this.confirm.setTimeout(Alert.FOREVER);
		this.confirm.setType(AlertType.WARNING);
		this.confirm.addCommand(this.confirmUpdate);
		this.confirm.addCommand(this.confirmCancel);
		this.confirm.setCommandListener(this);
		
		this.back = new Command(this.l10n.getString("BACK"), Command.BACK, 1);
		this.previewContact = new Command(this.l10n.getString("PREVIEW_CONTACT"), Command.ITEM, 1);
		this.updateSelectedContacts = new Command(this.l10n.getString("UPDATE_SELECTED"), Command.ITEM, 2);
		this.markAll = new Command(this.l10n.getString("MARK_ALL"), Command.ITEM, 3);
		this.unmarkAll = new Command(this.l10n.getString("UNMARK_ALL"), Command.ITEM, 4);
		this.about = new Command(this.l10n.getString("ABOUT"), Command.ITEM, 999);
		this.exit = new Command(this.l10n.getString("EXIT"), Command.EXIT, 1000);
		
		this.addCommand(this.back);
		this.addCommand(this.previewContact);
		this.addCommand(this.updateSelectedContacts);
		this.addCommand(this.markAll);
		this.addCommand(this.unmarkAll);
		this.addCommand(this.about);
		this.addCommand(this.exit);
		
		this.setCommandListener(this);
		
	}
	
	public void deleteContactsListItem(int contactToDelete) {
		this.delete(contactToDelete);
		this.deviceContacts.removeElementAt(contactToDelete);
	}
	
	private void showConfirmationAlert(int selectedContactsCount) {
		this.confirm.setString(this.l10n.getString("STRING_CONTACTSSCREEN_UPDATE_QUESTION") + " " + selectedContactsCount + " " + this.l10n.getString("STRING_CONTACTSSCREEN_UPDATE_QUESTION_CONTACTS") + this.l10n.getString("STRING_CONTACTSSCREEN_UPDATE_QUESTION_MARK"));
		this.midlet.displayManager.next(this.confirm);
	}
	
	private void markAllContacts(boolean state) {
		//Seriously?
		ListMarkThread markThread = new ListMarkThread(this, state, this.midlet);
		markThread.start();
	}
	
	public void commandAction(Command command, Displayable displayable) {
		if (displayable == this.confirm) {
			if (command == this.confirmUpdate || command == Alert.DISMISS_COMMAND) {
				this.midlet.displayManager.back();
				ContactsMultiUpdateThread updateThread = new ContactsMultiUpdateThread(this, this.midlet, this.contactsModel);
				updateThread.start();
			} else if (command == this.confirmCancel) {
				this.midlet.displayManager.back();
			}
		} else if (displayable == this) {
			if (command == this.back) {
				this.midlet.displayManager.back();
			} else if (command == this.previewContact) {
				boolean[] arrSel = new boolean[this.size()];
				int selectedItems = this.getSelectedFlags( arrSel );
				if (selectedItems == 0) {
					AlertScreen alert = new AlertScreen(this.l10n.getString("ERROR"), this.l10n.getString("STRING_CONTACTSSCREEN_NO_CONTACT_SELECTED"), AlertType.ERROR, this.midlet);
					alert.show();
				} else if (selectedItems == 1) {
					Contact contact = null;
					int currentIndex = 0;
					for (int i = 0; i < arrSel.length; i++) {
						if (arrSel[i] == true) {
							contact = (Contact) this.deviceContacts.elementAt(i);
							currentIndex = i;
							break;
						}
					}
					ContactDetailsScreen contactDetailsScreen = new ContactDetailsScreen(this.l10n.getString("PREVIEW_CONTACT"), contact, this, currentIndex, this.midlet, this.contactsModel);
					this.midlet.displayManager.next(contactDetailsScreen);
				} else {
					AlertScreen alert = new AlertScreen(this.l10n.getString("ERROR"), this.l10n.getString("STRING_CONTACTSSCREEN_ONE_CONTACT_ONLY"), AlertType.ERROR, this.midlet);
					alert.show();
				}
			} else if (command == this.updateSelectedContacts) {
				boolean[] arrSel = new boolean[this.size()];
				int selectedItems = this.getSelectedFlags( arrSel );
				if (selectedItems == 0) {
					AlertScreen alert = new AlertScreen(this.l10n.getString("ERROR"), this.l10n.getString("STRING_CONTACTSSCREEN_NO_CONTACT_SELECTED"), AlertType.ERROR, this.midlet);
					alert.show();
				} else {
					this.showConfirmationAlert(selectedItems);
				}
			} else if (command == this.markAll) {
				markAllContacts(true);
			} else if (command == this.unmarkAll) {
				markAllContacts(false);
			} else if (command == this.about) {
				this.midlet.showAbout();
			} else if (command == this.exit) {
				this.midlet.exitMIDlet();
			}
		}
	}
}
