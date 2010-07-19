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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.pim.Contact;

import com.mawqey.qontacts.i18n.L10nResources;
import com.mawqey.qontacts.main.Qontacts;
import com.mawqey.qontacts.models.ContactsModel;

public class ContactDetailsScreen extends List implements CommandListener {
	L10nResources l10n;
	private Qontacts midlet;
	private ContactsListScreen listScreen;
	private int contactToDelete;
	private ContactsModel contactsModel;
	private Contact contact;
	private Vector originalNumbers;
	private Vector updatedNumbers;
	private Alert confirmAlert;
	private Alert successAlert;
	
	private Command successOkCommand;
	
	private Command confirmUpdateCommand;
	private Command confirmCancelCommand;
	
	private Command back;
	private Command updateContact;
	private Command about;
	private Command exit;
	
	public ContactDetailsScreen(String title, Contact contact, ContactsListScreen listScreen, int contactToDelete, Qontacts midlet, ContactsModel contactsModel) {
		super(title, Choice.IMPLICIT);
		this.l10n = L10nResources.getL10nResources(null);
		this.midlet = midlet;
		this.contactsModel = contactsModel;
		this.listScreen = listScreen;
		this.contactToDelete = contactToDelete;
		this.contact = contact;
		this.setFitPolicy(TEXT_WRAP_ON);
		
		this.contactsModel.initAtrributesLabels();
		
		Hashtable contactNumbers = this.contactsModel.updateableContactNumbers(this.contact);
		
		String contactName = this.contactsModel.getContactName(contact);
		String nameString = this.l10n.getString("NAME") + "\n" + contactName;
		this.append(nameString, null);
		
		this.originalNumbers = (Vector) contactNumbers.get("Original Numbers");
		Image currentImageIcon = null;
		String currentNumbersLabel = this.l10n.getString("CURRENT_NUMBERS") + "\n(" + this.originalNumbers.size() + ")";
		try {
			currentImageIcon = Image.createImage("/current-numbers-icon.png");
		} catch(Exception ex) {
			//TODO:Handle error pl0x
			System.out.print("Problem creating Image : " + ex.toString());
		}
		this.append(currentNumbersLabel, currentImageIcon);
		Enumeration originalNumbersItems = this.originalNumbers.elements();
		while (originalNumbersItems.hasMoreElements()) {
			Hashtable numberItem = (Hashtable) originalNumbersItems.nextElement();
			String number = (String) numberItem.get("Number");
			Integer attr = (Integer) numberItem.get("Attribute");
			String label = this.contactsModel.getAtrributeLabel(attr);
			
			String textContent = label + "\n" + number;
			this.append(textContent, null);
		}
		
		this.updatedNumbers = (Vector) contactNumbers.get("Updated Numbers");
		Image updatedImageIcon = null;
		String updatedNumbersLabel = this.l10n.getString("UPDATED_NUMBERS") + "\n(" + this.updatedNumbers.size() + ")";
		try {
			updatedImageIcon = Image.createImage("/updated-numbers-icon.png");
		} catch(Exception ex) {
			System.out.print("Problem creating Image : " + ex.toString());
		}
		this.append(updatedNumbersLabel, updatedImageIcon);
		Enumeration updatedNumbersItems = this.updatedNumbers.elements();
		while (updatedNumbersItems.hasMoreElements()) {
			Hashtable numberItem = (Hashtable) updatedNumbersItems.nextElement();
			String number = (String) numberItem.get("Number");
			Integer attr = (Integer) numberItem.get("Attribute");
			String label = this.contactsModel.getAtrributeLabel(attr);
			
			String textContent = label + "\n" + number;
			this.append(textContent, null);
		}
		
		//Init the confirmation question alert
		this.confirmAlert = new Alert(null, this.l10n.getString("STRING_CONTACTSSCREEN_UPDATE_QUESTION") + " " + contactName + this.l10n.getString("STRING_CONTACTSSCREEN_UPDATE_QUESTION_MARK"), null, AlertType.WARNING);
		this.confirmUpdateCommand = new Command(this.l10n.getString("OK"), Command.OK, 1);
		this.confirmCancelCommand = new Command(this.l10n.getString("CANCEL"), Command.CANCEL, 1);
		this.confirmAlert.setTimeout(Alert.FOREVER);
		this.confirmAlert.addCommand(this.confirmUpdateCommand);
		this.confirmAlert.addCommand(this.confirmCancelCommand);
		this.confirmAlert.setCommandListener(this);
		
		//Init the success alert
		this.successAlert = new Alert(this.l10n.getString("SUCCESS"), this.l10n.getString("STRING_CONTACTSSCREEN_DETAILS_OF") + " " + contactName + " " + this.l10n.getString("STRING_CONTACTSSCREEN_UPDATED_SUCCESSFULLY"), null, AlertType.CONFIRMATION);
		this.successOkCommand = new Command(this.l10n.getString("OK"), Command.OK, 1);
		this.successAlert.setTimeout(Alert.FOREVER);
		this.successAlert.addCommand(this.successOkCommand);
		this.successAlert.setCommandListener(this);
		
		this.back = new Command(this.l10n.getString("BACK"), Command.BACK, 1);
		this.updateContact = new Command(this.l10n.getString("UPDATE"), Command.ITEM, 1);
		this.about = new Command(this.l10n.getString("ABOUT"), Command.ITEM, 999);
		this.exit = new Command(this.l10n.getString("EXIT"), Command.EXIT, 1000);
		this.addCommand(this.back);
		this.addCommand(this.updateContact);
		this.addCommand(this.about);
		this.addCommand(this.exit);
		this.setSelectCommand(this.updateContact);
		this.setCommandListener(this);
	}
	
    private boolean updateContact() {
    	if (this.contactsModel.convertContactNumbers(this.contact, this.updatedNumbers)) {
    		this.listScreen.deleteContactsListItem(this.contactToDelete);
    		return true;
    	} else {
    		return false;
    	}
    }
    
	private void showConfirmationAlert() {
		this.midlet.displayManager.display.setCurrent(this.confirmAlert, this);
	}
	
	private void showSuccessAlert() {
		this.midlet.displayManager.display.setCurrent(this.successAlert, this);
	}

	public void commandAction(Command command, Displayable displayable) {
		if (displayable == this.confirmAlert) {
			if (command == this.confirmUpdateCommand || command == Alert.DISMISS_COMMAND) {
				if (this.updateContact()) {
					this.showSuccessAlert();
				} else {
					//TODO:Error handling here pl0x!
					System.out.println("Error!");
				}
			} else if (command == this.confirmCancelCommand) {
				this.midlet.displayManager.display.setCurrent(this);
			}
		} else if (displayable == this.successAlert) {
			if (command == this.successOkCommand || command == Alert.DISMISS_COMMAND) {
				this.midlet.displayManager.back();
			}
		} else if (displayable == this) {
			if (command == this.back) {
				this.midlet.displayManager.back();
			} else if (command == this.updateContact) {
				this.showConfirmationAlert();
			} else if (command == this.about) {
				this.midlet.showAbout();
			} else if (command == this.exit) {
				this.midlet.exitMIDlet();
			}
		}
	}

}
