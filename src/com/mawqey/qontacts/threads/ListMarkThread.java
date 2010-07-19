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
package com.mawqey.qontacts.threads;

import javax.microedition.lcdui.AlertType;

import com.mawqey.qontacts.i18n.L10nResources;
import com.mawqey.qontacts.main.Qontacts;
import com.mawqey.qontacts.screens.ContactsListScreen;
import com.mawqey.qontacts.screens.helpers.ProgressAlert;

public class ListMarkThread extends Thread {
	L10nResources l10n;
	private ProgressAlert markProgress;
	private ContactsListScreen list;
	private boolean state;
	
	public ListMarkThread(ContactsListScreen list, boolean state, Qontacts midlet) {
		this.l10n = L10nResources.getL10nResources(null);
		this.list = list;
		this.state = state;
		this.markProgress = new ProgressAlert(this.l10n.getString("PLEASE_WAIT") + " ...", AlertType.ALARM, midlet);
	}
	
	public void run() {
		this.markProgress.show();
		try {
			Thread.sleep(25);
			
	        boolean[] arrTrue = new boolean[this.list.size()];
	        for(int i = 0; i < arrTrue.length; i++) {
	        	arrTrue[i] = this.state;
	        }
	        this.list.setSelectedFlags(arrTrue);
	        
	        this.markProgress.remove();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
