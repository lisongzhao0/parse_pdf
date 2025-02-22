/* ================================================================
 * JBarcode : Java Barcode Library
 * ================================================================
 *
 * Project Info:  http://jbcode.sourceforge.net
 * Project Lead:  Flavio Sampaio (flavio@ronisons.com);
 *
 * (C) Copyright 2005, by Flavio Sampaio
 *
 * This library is free software; you can redistribute it and/or modify it underthe terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package com.test.org.jbarcode.encode;

/**
 * TODO: Description.
 * 
 * @author Fl�vio Sampaio
 * @since 0.1
 */
public class InvalidAtributeException extends Exception {

	private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
	public InvalidAtributeException() {
		super();
	}

	/**
	 * @param message
	 */
	public InvalidAtributeException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidAtributeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public InvalidAtributeException(Throwable cause) {
		super(cause);
	}

}
