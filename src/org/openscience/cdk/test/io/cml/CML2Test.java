/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2003-2004  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
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
package org.openscience.cdk.test.io.cml;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.openscience.cdk.Atom;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemModel;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.ChemSequence;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.io.CMLReader;

/**
 * TestCase for the reading CML 2 files using a few test files
 * in data/cmltest.
 *
 * @cdkPackage test
 */
public class CML2Test extends TestCase {

    private org.openscience.cdk.tools.LoggingTool logger;

    public CML2Test(String name) {
        super(name);
        logger = new org.openscience.cdk.tools.LoggingTool(this.getClass().getName());
    }

    public static Test suite() {
        return new TestSuite(CML2Test.class);
    }

    public void testCOONa() {
        String filename = "data/cmltest/COONa.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(4, mol.getAtomCount());
            assertEquals(2, mol.getBondCount());
            assertTrue(GeometryTools.has3DCoordinates(mol));
            assertTrue(!GeometryTools.has2DCoordinates(mol));
            
            Atom[] atoms = mol.getAtoms();
            for (int i=0; i<atoms.length; i++) {
                Atom atom = atoms[i];
                if (atom.getSymbol().equals("Na")) 
                    assertEquals(+1, atom.getFormalCharge()); 
            }
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testNitrate() {
        String filename = "data/cmltest/nitrate.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(4, mol.getAtomCount());
            assertEquals(3, mol.getBondCount());
            assertTrue(GeometryTools.has3DCoordinates(mol));
            assertTrue(!GeometryTools.has2DCoordinates(mol));
            
            Atom[] atoms = mol.getAtoms();
            for (int i=0; i<atoms.length; i++) {
                Atom atom = atoms[i];
                if (atom.getSymbol().equals("N")) 
                    assertEquals(+1, atom.getFormalCharge()); 
            }
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK1() {
        String filename = "data/cmltestok/cs2a.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(38, mol.getAtomCount());
            assertEquals(48, mol.getBondCount());
            assertTrue(GeometryTools.has3DCoordinates(mol));
            assertFalse(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK2() {
        String filename = "data/cmltestok/cs2a.mol.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(38, mol.getAtomCount());
            assertEquals(29, mol.getBondCount());
            assertTrue(GeometryTools.has3DCoordinates(mol));
            assertFalse(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK3() {
        String filename = "data/cmltestok/nsc2dmol.1.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(13, mol.getAtomCount());
            assertEquals(12, mol.getBondCount());
            assertFalse(GeometryTools.has3DCoordinates(mol));
            assertTrue(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK4() {
        String filename = "data/cmltestok/nsc2dmol.2.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(13, mol.getAtomCount());
            assertEquals(12, mol.getBondCount());
            assertFalse(GeometryTools.has3DCoordinates(mol));
            assertTrue(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK5() {
        String filename = "data/cmltestok/nsc2dmol.a1.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(13, mol.getAtomCount());
            assertEquals(12, mol.getBondCount());
            assertFalse(GeometryTools.has3DCoordinates(mol));
            assertTrue(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK6() {
        String filename = "data/cmltestok/nsc2dmol.a2.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(13, mol.getAtomCount());
            assertEquals(12, mol.getBondCount());
            assertFalse(GeometryTools.has3DCoordinates(mol));
            assertTrue(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK7() {
        String filename = "data/cmltestok/nsc3dcml.xml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(27, mol.getAtomCount());
            assertEquals(27, mol.getBondCount());
            assertTrue(GeometryTools.has3DCoordinates(mol));
            assertFalse(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK8() {
        String filename = "data/cmltestok/nsc2dcml.xml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(15, mol.getAtomCount());
            assertEquals(14, mol.getBondCount());
            assertFalse(GeometryTools.has3DCoordinates(mol));
            assertTrue(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK9() {
        String filename = "data/cmltestok/nsc3dmol.1.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(15, mol.getAtomCount());
            assertEquals(15, mol.getBondCount());
            assertTrue(GeometryTools.has3DCoordinates(mol));
            assertFalse(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK10() {
        String filename = "data/cmltestok/nsc3dmol.2.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(15, mol.getAtomCount());
            assertEquals(15, mol.getBondCount());
            assertTrue(GeometryTools.has3DCoordinates(mol));
            assertFalse(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK11() {
        String filename = "data/cmltestok/nsc3dmol.a1.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(15, mol.getAtomCount());
            assertEquals(15, mol.getBondCount());
            assertTrue(GeometryTools.has3DCoordinates(mol));
            assertFalse(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

    public void testCMLOK12() {
        String filename = "data/cmltestok/nsc3dmol.a2.cml";
        logger.info("Testing: " + filename);
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            CMLReader reader = new CMLReader(new InputStreamReader(ins));
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

            // test the resulting ChemFile content
            assertNotNull(chemFile);
            assertEquals(chemFile.getChemSequenceCount(), 1);
            ChemSequence seq = chemFile.getChemSequence(0);
            assertNotNull(seq);
            assertEquals(seq.getChemModelCount(), 1);
            ChemModel model = seq.getChemModel(0);
            assertNotNull(model);
            assertEquals(model.getSetOfMolecules().getMoleculeCount(), 1);

            // test the molecule
            Molecule mol = model.getSetOfMolecules().getMolecule(0);
            assertNotNull(mol);
            assertEquals(15, mol.getAtomCount());
            assertEquals(15, mol.getBondCount());
            assertTrue(GeometryTools.has3DCoordinates(mol));
            assertFalse(GeometryTools.has2DCoordinates(mol));
            
        } catch (Exception e) {
            fail(e.toString());
            e.printStackTrace();
        }
    }

}
