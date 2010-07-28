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
package com.mawqey.qontacts.models;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.pim.Contact;
import javax.microedition.pim.ContactList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;

import com.mawqey.qontacts.i18n.L10nResources;

public class ContactsModel {
	L10nResources l10n;
	public ContactList contactList;
	private Hashtable attrToLabel;
	private int[] supportedAttributes;
	
	public ContactsModel() {
		this.l10n = L10nResources.getL10nResources(null);
	}
	
	public void openContactsDB() {
		try {
			PIM pimInstance = PIM.getInstance();
			this.contactList = (ContactList) pimInstance.openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);
			this.supportedAttributes = this.contactList.getSupportedAttributes(Contact.TEL);
			System.out.println("Opening contacts db...");
		} catch (PIMException e) {
			System.out.println("Error: PIM issue " + e.getMessage());
		} catch (SecurityException s) {
        	System.out.println("Error: Security issue " + s.getMessage());
        }
	}
	
	public void closeContactsDB() {
		if (this.contactList != null) {
			try {
				this.contactList.close();
				System.out.println("Closing contacts db...");
			} catch (PIMException e) {
				System.out.println("Error: PIM issue " + e.getMessage());
			} catch (SecurityException s) {
	        	System.out.println("Error: Security issue " + s.getMessage());
	        }
		}
	}
	
	public Vector getContactsItems() {
		Vector contactItems = new Vector();
		try {
			contactItems = getUpdateableContacts(this.contactList.items());
		} catch (PIMException e) {
			System.out.println("Error: PIM issue " + e.getMessage());
		} catch (SecurityException s) {
        	System.out.println("Error: Security issue " + s.getMessage());
        }
		return contactItems;
	}
	
	private Vector getUpdateableContacts(Enumeration contactEnum) {
		Vector items = new Vector();
		
		if (contactEnum == null) {
			return items;
		}
		
		while (contactEnum.hasMoreElements()) {
			Contact contact = (Contact)contactEnum.nextElement();
			if (checkContact(contact)) {
				items.addElement(contact);
			}
		}
		return items;
	}
	
	private boolean checkContact(Contact contact) {
		Hashtable updateableContactNumbers = updateableContactNumbers(contact);
		if (updateableContactNumbers.size() > 0) {
			return true;
		}
		return false;
	}
	
	public Hashtable updateableContactNumbers(Contact contact) {
		Vector _originalNumbersTemp = new Vector();
		Vector _updatedNumbersTemp = new Vector();
		Hashtable _retNumbers = new Hashtable();
		
		int telCount = contact.countValues(Contact.TEL);
		
		if ( telCount > 0 ) {
			for (int i = 0; i < telCount; i++) {
				int _telAttributeInt = contact.getAttributes(Contact.TEL, i);
				Integer _telAttribute = new Integer(getAttributeInt(_telAttributeInt));
				String _telNumberOriginal = contact.getString(Contact.TEL, i);
				String _telNumberUpdated = updateableNumber(_telNumberOriginal);
				
				Hashtable _tempOriginalNumberDetails = new Hashtable(3);
				_tempOriginalNumberDetails.put("Attribute", _telAttribute);
				_tempOriginalNumberDetails.put("Number", _telNumberOriginal);
				_tempOriginalNumberDetails.put("Index", new Integer(i));
				
				_originalNumbersTemp.addElement(_tempOriginalNumberDetails);
				
				if ((_telNumberUpdated != null) && (_telNumberUpdated.length() > 0)) {
					Hashtable _tempUpdatedNumberDetails = new Hashtable(3);
					_tempUpdatedNumberDetails.put("Attribute", _telAttribute);
					_tempUpdatedNumberDetails.put("Number", _telNumberUpdated);
					_tempUpdatedNumberDetails.put("Index", new Integer(i));
					
					_updatedNumbersTemp.addElement(_tempUpdatedNumberDetails);
				}
			}
			
			if ( _updatedNumbersTemp.size() > 0 ) {
				_retNumbers.put("Original Numbers", _originalNumbersTemp);
				_retNumbers.put("Updated Numbers", _updatedNumbersTemp);
			}
		}
		return _retNumbers;
	}
	
	private String cleanNumber(String number) {
		String invalidChars = "- ";
		
		StringBuffer buffer = new StringBuffer();
		
	    for ( int i = 0; i < number.length(); i++ ) {
	    	char c = number.charAt(i);
			int pos = invalidChars.indexOf(c);
			if (pos == -1) {
				buffer.append(c);
			}
	    }
	    
	    return buffer.toString();
	}
	
	private String updateableNumber(String number) {
		if ((number != null) && (number.length() > 0)) {
			number = cleanNumber(number);
			
			String _retNumber = "";
			String _tempPrefix = "";
			String _tempNumber = number;
			
			if (number.startsWith("+974")) {
				_tempPrefix = "+974";
				_tempNumber = number.substring(4);
			} else if (number.startsWith("00974")) {
				_tempPrefix = "00974";
				_tempNumber = number.substring(5);
			}
			
            if ((_tempNumber.length() == 7) && (
            									_tempNumber.startsWith("3") || 
            									_tempNumber.startsWith("4") || 
            									_tempNumber.startsWith("5") || 
            									_tempNumber.startsWith("6") || 
            									_tempNumber.startsWith("7")
            								   ))
            {
            	_retNumber = _tempPrefix + _tempNumber.charAt(0) + _tempNumber;
            }
			return _retNumber;
		}
		return null;
	}
	
	//This function to get the real attribute from the bit array attribute
	public int getAttributeInt(int attribute) {
		
		for (int i = 0; i < this.supportedAttributes.length; i++) {
			int attrValue = (attribute & this.supportedAttributes[i]);
			if ((attrValue == this.supportedAttributes[i]) && (attrValue != Contact.ATTR_PREFERRED)) {
				return attrValue;
			}
		}
		
		return attribute;
	}
	
	public void initAtrributesLabels() {
		this.attrToLabel = new Hashtable();
		//Standard J2ME constants
		for (int i = 0; i < this.supportedAttributes.length; i++) {
			String label = this.contactList.getAttributeLabel(this.supportedAttributes[i]);
			this.attrToLabel.put(new Integer(this.supportedAttributes[i]), label);
		}

		//Override some standards constants
		this.attrToLabel.put(new Integer(0), this.l10n.getString("TEL"));
		this.attrToLabel.put(new Integer(8), this.l10n.getString("TEL_HOME"));
		this.attrToLabel.put(new Integer(512), this.l10n.getString("TEL_BUSINESS"));
		
		//Nonstandard PIM list constants
		this.attrToLabel.put(new Integer(24), this.l10n.getString("MOBILE_HOME"));
		this.attrToLabel.put(new Integer(528), this.l10n.getString("MOBILE_BUSINESS"));
		this.attrToLabel.put(new Integer(16777224), this.l10n.getString("VIDEO_CALL_HOME"));
		this.attrToLabel.put(new Integer(16777728), this.l10n.getString("VIDEO_CALL_BUSINESS"));
		this.attrToLabel.put(new Integer(12), this.l10n.getString("FAX_HOME"));
		this.attrToLabel.put(new Integer(516), this.l10n.getString("FAX_BUSINESS"));		
	}
	
	public String getAtrributeLabel(Integer attr) {
		String _retString = this.l10n.getString("UNKNOWN");
		
		if (this.attrToLabel.containsKey(attr)) {
			_retString = (String) this.attrToLabel.get(attr);
		}
		
		return _retString;
	}
	
	public boolean convertContactNumbers(Contact contact) {
		Hashtable contactNumbers = updateableContactNumbers(contact);
		Vector updatedNumbers = (Vector) contactNumbers.get("Updated Numbers");
		if (convertContactNumbers(contact, updatedNumbers)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean convertContactNumbers(Contact contact, Vector numbers) {
		Enumeration updatedNumbers = numbers.elements();
		
		while (updatedNumbers.hasMoreElements()) {
			Hashtable numberItem = (Hashtable) updatedNumbers.nextElement();
			String number = (String) numberItem.get("Number");
			Integer attr = (Integer) numberItem.get("Attribute");
			Integer index = (Integer) numberItem.get("Index");
			contact.setString(Contact.TEL, index.intValue(), attr.intValue(), number);
		}
		
        try {
            contact.commit();
        } catch (PIMException e) {
        	System.out.println("Error: PIM issue " + e.getMessage());
        	return false;
        } catch (SecurityException s) {
        	System.out.println("Error: Security issue " + s.getMessage());
        	return false;
        }
        return true;
	}
	
	public String getContactName(Contact contact) {
        if (contact == null) {
            return null;
        }

        String displayName = this.l10n.getString("NO_NAME");
        
        // First, see if there is a meaningful name set for the contact.
        if (contact.countValues(Contact.NAME) > 0) {
            final String[] name = contact.getStringArray(Contact.NAME, Contact.ATTR_NONE);
            final String firstName = name[Contact.NAME_GIVEN];
            final String lastName = name[Contact.NAME_FAMILY];
            if (firstName != null && lastName != null) {
                displayName = lastName + this.l10n.getString("NAME_SEPARATOR") + " " + firstName;
            } else if (firstName != null) {
                displayName = firstName;
            } else if (lastName != null) {
                displayName = lastName;
            }

            if (displayName != null) {
                final String namePrefix = name[Contact.NAME_PREFIX];
                if (namePrefix != null) {
                    displayName = namePrefix + " " + displayName;
                }
                return displayName;
            }
        } else if (contact.countValues(Contact.ORG) > 0) {
            final String companyName = contact.getString(Contact.ORG, 0);
            if (companyName != null) {
                return companyName;
            }
        }
        
        return displayName;
	}

}
