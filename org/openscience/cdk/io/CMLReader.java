/* MDLReader.java
 * 
 * $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 * 
 * Copyright (C) 1997-2001  The Chemistry Development Kit (CDK) project
 * 
 * Contact: steinbeck@ice.mpg.de, gezelter@maul.chem.nd.edu, egonw@sci.kun.nl
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *  */
package org.openscience.cdk.io;

import org.openscience.cdk.*;
import org.openscience.cdk.exception.*;
import org.openscience.cml.*;
import org.openscience.cdopi.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;

/**
 * Reads a molecule in CML format from a Reader
 *
 * @author     egonw
 * @created    February 2001
 */
public class CMLReader implements CDKConstants, ChemObjectReader {

    private static final String pClass = "org.apache.xerces.parsers.SAXParser";
    private XMLReader parser;
    private ContentHandler handler;
    private EntityResolver resolver;
    private Reader input;

    public CMLReader(Reader input) {
	try {
	    parser = (XMLReader)Class.forName(pClass).newInstance();
	    this.input = input;
	} catch (Exception e) {
	    System.out.println("CMLReader: You found a serious bug! Please report it!");
	    System.exit(1);			       
	}
    }

	
    /**
     * Read a ChemFile from input
     *
     * @return The content in a ChemFile object
     */
    public ChemObject read(ChemObject object) throws UnsupportedChemObjectException {
	if (object instanceof ChemFile) {
	    return (ChemObject)readChemFile();
	} else {
	    throw new UnsupportedChemObjectException(
		"Only supported is ChemFile.");
	}
    }

    // private functions

    private ChemFile readChemFile() {
	ChemFileCDO cdo = new ChemFileCDO();
	handler = new CMLHandler((CDOInterface)cdo);
	parser.setContentHandler(handler);
	try {
	    parser.parse(new InputSource(input));
	} catch (IOException e) {
	} catch (SAXException e) {
	}
	return cdo;
    }
}

