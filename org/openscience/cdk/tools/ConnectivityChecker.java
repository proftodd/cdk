/* ConnectivityChecker.java
 *
 * $RCSfile$    $Author$    $Date$    $Revision$
 * 
 * Copyright (C) 1997-2001  The Chemistry Development Kit (CDK) project
 * 
 * Contact: steinbeck@ice.mpg.de, gezelter@maul.chem.nd.edu, egonw@sci.kun.nl
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. 
 * 
 */

package org.openscience.cdk.tools;

import org.openscience.cdk.*;
import java.util.*;

/**
 * Tool class for checking whether the (sub)structure in an 
 * AtomContainer is connected
 */
 
public class ConnectivityChecker implements CDKConstants
{
	/**
	 * Check whether a set of atoms in an atomcontainer is connected
	 *
	 * @param   ac  The AtomContainer to be check for connectedness
	 * @return  true if the AtomContainer is connected   
	 */
	public boolean isConnected(AtomContainer atomContainer)
	{
		AtomContainer ac = new AtomContainer();
		Atom atom = null, nextAtom = null; 
		Molecule molecule = new Molecule();
		Vector molecules = new Vector();
		Vector sphere = new Vector();
		for (int f = 0; f < atomContainer.getAtomCount(); f++)
		{
			atomContainer.getAtomAt(f).flags[VISITED] = false;
			ac.addAtom(atomContainer.getAtomAt(f));
		}
		for (int f = 0; f < atomContainer.getBondCount(); f++)
		{
			atomContainer.getBondAt(f).flags[VISITED] = false;
			ac.addBond(atomContainer.getBondAt(f));
		}
		atom = ac.getAtomAt(0);
		sphere.addElement(atom);
		atom.flags[VISITED] = true;
		PathTools.breadthFirstSearch(ac, sphere, molecule);
		if (molecule.getAtomCount() == atomContainer.getAtomCount())
		{
			return true;
		}
		return false;
	}
	


	/**
	 * Partitions the atoms in an AtomContainer into connected components, i.e. molecules
	 *
	 * @param   atomContainer  The AtomContainer to be partitioned into connected components, i.e. molecules
	 * @return  A vector of AtomContainers, each containing a connected molecule   
	 * @exception   Exception  If something goes wrong
	 */
	public  static Vector partitionIntoMolecules(AtomContainer atomContainer) throws java.lang.Exception

	{
		AtomContainer ac = new AtomContainer();
		Atom atom = null, nextAtom = null; 
		Molecule molecule = null;
		Vector molecules = new Vector();
		Vector sphere = new Vector();
		for (int f = 0; f < atomContainer.getAtomCount(); f++)
		{
			atomContainer.getAtomAt(f).flags[VISITED] = false;
			ac.addAtom(atomContainer.getAtomAt(f));
		}
		for (int f = 0; f < atomContainer.getBondCount(); f++)
		{
			atomContainer.getBondAt(f).flags[VISITED] = false;
			ac.addBond(atomContainer.getBondAt(f));
		}
		while(ac.getAtomCount() > 0)
		{
			atom = ac.getAtomAt(0);
			molecule = new Molecule();
			sphere.removeAllElements();
			sphere.addElement(atom);
			atom.flags[VISITED] = true;
			PathTools.breadthFirstSearch(ac, sphere, molecule);
			molecules.addElement(molecule);
			ac.remove(molecule);
		}
		return molecules;
	}
}