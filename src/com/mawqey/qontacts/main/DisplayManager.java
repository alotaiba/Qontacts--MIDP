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
package com.mawqey.qontacts.main;
import java.util.Stack;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;


public class DisplayManager {

	private Displayable current;
	public Display display;
	private Stack	stack;
	
	/**
	 * Creates a display manager associated to
	 * the specified display.
	 * 
	 * @param display target display.
	 */
	public DisplayManager(Display display) {
		if (display == null) {
			throw new IllegalArgumentException("Display can not be null.");
		}
		this.display = display;
		this.stack	 = new Stack();
	}
	
	/**
	 * Sets the specified displayable as the current
	 * screen.
	 * 
	 * @param next screen to show.
	 */
	public void next(Displayable next) {
		if (this.current == next) {
			return;
		}
		
		if (this.current != null) {
			this.stack.push(this.current);
		}
		this.current = next;
		this.display.setCurrent(this.current);
	}
	
	/**
	 * Goes back to the last screen.
	 */
	public void back() {
		if (this.stack.size() > 0x00) {
			this.current = (Displayable) this.stack.pop();
			this.display.setCurrent(this.current);
		}
	}
	
}
