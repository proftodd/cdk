/* $RCSfile$    
 * $Author$    
 * $Date$    
 * $Revision$
 * 
 * Copyright (C) 1997-2004  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
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
 */
package org.openscience.cdk.test.tools;

import java.io.IOException;
import java.util.Hashtable;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.Atom;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.HydrogenAdder;
import org.openscience.cdk.tools.MFAnalyser;

/**
 * @cdk.module test
 */
public class MFAnalyserTest extends TestCase {
	Molecule molecule;
	
	public MFAnalyserTest(String name)
	{
		super(name);
	}

	public void setUp()
	{
		molecule = MoleculeFactory.makeAlphaPinene();
	}

	public static Test suite() 
	{
		return new TestSuite(MFAnalyserTest.class);
	}

	public void testAnalyseMF()	{
		MFAnalyser mfa = new MFAnalyser("C10H16");
		AtomContainer ac = mfa.getAtomContainer();
		MFAnalyser mfa2 = new MFAnalyser(ac);
		String mf = mfa2.getMolecularFormula();
		assertTrue(mf.equals("C10H16"));
	}
	
    public void testGetAtomContainer() {
        MFAnalyser mfa = new MFAnalyser("C10H16");
        AtomContainer ac = mfa.getAtomContainer();
        assertEquals(26, ac.getAtomCount());        
    }
    
    public void testGetElementCount() {
        MFAnalyser mfa = new MFAnalyser("C10H16");
        assertEquals(2, mfa.getElementCount());        
    
        mfa = new MFAnalyser("CH3OH");
        assertEquals(3, mfa.getElementCount());        
    }
    
    public void testGetAtomCount() {
        MFAnalyser mfa = new MFAnalyser("C10H16");
        assertEquals(10, mfa.getAtomCount("C"));        
        assertEquals(16, mfa.getAtomCount("H"));        
    
        mfa = new MFAnalyser("CH3OH");
        assertEquals(1, mfa.getAtomCount("C"));        
        assertEquals(1, mfa.getAtomCount("O"));        
        assertEquals(4, mfa.getAtomCount("H"));        
    }
    
    public void testGetHeavyAtoms() {
        MFAnalyser mfa = new MFAnalyser("C10H16");
        assertEquals(10, mfa.getHeavyAtoms().size());        
    
        mfa = new MFAnalyser("CH3OH");
        assertEquals(2, mfa.getHeavyAtoms().size());        
    }
    
    public void testRemoveHydrogens() throws IOException, ClassNotFoundException, CDKException{
      Molecule mol=MoleculeFactory.makeAlphaPinene();
      new HydrogenAdder().addHydrogensToSatisfyValency(mol);
      MFAnalyser mfa=new MFAnalyser(mol);
      AtomContainer ac=mfa.removeHydrogens();
      mfa=new MFAnalyser(ac);
      assertEquals(10, ac.getAtomCount());
      assertEquals("C10H16", mfa.getMolecularFormula());//Formula should still contain Hs because hydrogenCount is used for building formula
    }

    /**
     * Test removeHydrogens for B2H6, which contains two multiply bonded H.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws CDKException
     */
    public void testRemoveHydrogensBorane() throws IOException, ClassNotFoundException, CDKException
    {
        SmilesParser parser = new SmilesParser();
        Molecule mol = parser.parseSmiles("B1([H])([H])[H]B([H])([H])[H]1");
        AtomContainer ac = new MFAnalyser(mol).removeHydrogens();

        // Should be two disconnected Bs with H-count == 4
        assertEquals("incorrect atom count", 2, ac.getAtomCount());
        assertEquals("incorrect bond count", 0, ac.getBondCount());
        assertEquals("incorrect hydrogen count", 4, ac.getAtomAt(0).getHydrogenCount());
        assertEquals("incorrect hydrogen count", 4, ac.getAtomAt(1).getHydrogenCount());
    }

    /**
     * Test removeHydrogensPreserveMultiplyBonded for B2H6, which contains two multiply bonded H.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws CDKException
     */
    public void testRemoveHydrogensPreserveMulitplyBondedBorane() throws IOException, ClassNotFoundException, CDKException
    {
        SmilesParser parser = new SmilesParser();
        Molecule mol = parser.parseSmiles("B1([H])([H])[H]B([H])([H])[H]1");
        AtomContainer ac = new MFAnalyser(mol).removeHydrogensPreserveMultiplyBonded();

        // Should be two connected Bs with H-count == 2 and two explicit Hs.
        assertEquals("incorrect atom count", 4, ac.getAtomCount());
        assertEquals("incorrect bond count", 4, ac.getBondCount());

        int b = 0;
        int h = 0;
        for (int i = 0;
                i < ac.getAtomCount();
                i++)
        {
            final Atom atom = ac.getAtomAt(i);
            String sym = atom.getSymbol();
            if (sym.equals("B"))
            {
                // Each B has two explicit and two implicit H.
                b++;
                assertEquals("incorrect hydrogen count", 2, atom.getHydrogenCount());
                Atom[] nbs = ac.getConnectedAtoms(atom);
                assertEquals("incorrect connected count", 2, nbs.length);
                assertEquals("incorrect bond", "H", nbs[0].getSymbol());
                assertEquals("incorrect bond", "H", nbs[1].getSymbol());
            }
            else if (sym.equals("H"))
            {
                h++;
            }
        }
        assertEquals("incorrect no. Bs", 2, b);
        assertEquals("incorrect no. Hs", 2, h);
    }


    public void testGetFormulaHashtable() {
	MFAnalyser mfa=new MFAnalyser(molecule);
	Hashtable formula = mfa.getFormulaHashtable();
	assertEquals(10, ((Integer)formula.get("C")).intValue());
    }
    
}

