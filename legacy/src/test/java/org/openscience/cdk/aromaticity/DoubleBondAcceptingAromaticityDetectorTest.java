/* Copyright (C) 1997-2007  The Chemistry Development Kit (CDK) project
 *                    2012  Egon Willighagen <egonw@users.sf.net>
 *
 * Contact: cdk-devel@list.sourceforge.net
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.aromaticity;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Point2d;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.data.Atom;
import org.openscience.cdk.core.CDKConstants;
import org.openscience.cdk.tools.CDKTestCase;
import org.openscience.cdk.data.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.SpanningTree;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IRing;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.ringsearch.AllRingsFinder;
import org.openscience.cdk.ringsearch.SSSRFinder;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.templates.TestMoleculeFactory;
import org.openscience.cdk.tools.valencycheck.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.RingSetManipulator;

/**
 * @author steinbeck
 * @author egonw
 * @cdk.module test-standard
 */
public class DoubleBondAcceptingAromaticityDetectorTest extends CDKTestCase {

    public DoubleBondAcceptingAromaticityDetectorTest() {
        super();
    }

    @Test
    public void testDetectAromaticity_IAtomContainer() throws Exception {
        IAtomContainer mol = makeAromaticMolecule();

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        boolean isAromatic = DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol);
        Assert.assertTrue("Molecule is expected to be marked aromatic!", isAromatic);

        int numberOfAromaticAtoms = 0;
        for (int i = 0; i < mol.getAtomCount(); i++) {
            if (((IAtom) mol.getAtom(i)).getFlag(CDKConstants.ISAROMATIC)) numberOfAromaticAtoms++;
        }
        Assert.assertEquals(6, numberOfAromaticAtoms);

        int numberOfAromaticBonds = 0;
        for (int i = 0; i < mol.getBondCount(); i++) {
            if (((IBond) mol.getBond(i)).getFlag(CDKConstants.ISAROMATIC)) numberOfAromaticBonds++;
        }
        Assert.assertEquals(6, numberOfAromaticBonds);

    }

    @Test
    public void testConstructor() {
        // For autogenerated constructor
        DoubleBondAcceptingAromaticityDetector detector = new DoubleBondAcceptingAromaticityDetector();
        Assert.assertNotNull(detector);
    }

    @Test
    public void testNMethylPyrrol() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        IAtomContainer mol = sp.parseSmiles("c1ccn(C)c1");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue("Expected the molecule to be aromatic.",
                DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));

        IRingSet ringset = (new SSSRFinder(mol)).findSSSR();
        int numberOfAromaticRings = 0;
        RingSetManipulator.markAromaticRings(ringset);
        for (int i = 0; i < ringset.getAtomContainerCount(); i++) {
            if (ringset.getAtomContainer(i).getFlag(CDKConstants.ISAROMATIC)) numberOfAromaticRings++;
        }
        Assert.assertEquals(1, numberOfAromaticRings);
    }

    @Test
    public void testPyridine() throws Exception {
        IAtomContainer mol = new AtomContainer();
        mol.addAtom(new Atom("N"));
        mol.addAtom(new Atom("C"));
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addAtom(new Atom("C"));
        mol.addBond(1, 2, IBond.Order.DOUBLE);
        mol.addAtom(new Atom("C"));
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addAtom(new Atom("C"));
        mol.addBond(3, 4, IBond.Order.DOUBLE);
        mol.addAtom(new Atom("C"));
        mol.addBond(4, 5, IBond.Order.SINGLE);
        mol.addBond(0, 5, IBond.Order.DOUBLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue("Expected the molecule to be aromatic.",
                DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));

        Iterator<IAtom> atoms = mol.atoms().iterator();
        while (atoms.hasNext()) {
            Assert.assertTrue(atoms.next().getFlag(CDKConstants.ISAROMATIC));
        }

        IRingSet ringset = (new SSSRFinder(mol)).findSSSR();
        int numberOfAromaticRings = 0;
        RingSetManipulator.markAromaticRings(ringset);
        for (int i = 0; i < ringset.getAtomContainerCount(); i++) {
            if (ringset.getAtomContainer(i).getFlag(CDKConstants.ISAROMATIC)) numberOfAromaticRings++;
        }
        Assert.assertEquals(1, numberOfAromaticRings);
    }

    @Test
    public void testCyclopentadienyl() throws Exception {
        IAtomContainer mol = new AtomContainer();
        mol.addAtom(new Atom("C"));
        mol.getAtom(0).setFormalCharge(-1);
        for (int i = 1; i < 5; i++) {
            mol.addAtom(new Atom("C"));
            mol.getAtom(i).setHybridization(IAtomType.Hybridization.SP2);
            mol.addBond(i - 1, i, IBond.Order.SINGLE);
        }
        mol.addBond(0, 4, IBond.Order.SINGLE);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue("Expected the molecule to be aromatic.",
                DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));

        Iterator<IAtom> atoms = mol.atoms().iterator();
        while (atoms.hasNext()) {
            Assert.assertTrue(atoms.next().getFlag(CDKConstants.ISAROMATIC));
        }
    }

    @Test
    public void testPyridineOxide() throws Exception {
        IAtomContainer molecule = TestMoleculeFactory.makePyridineOxide();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule));
    }

    @Test
    public void testPyridineOxide_SP2() throws Exception {
        IAtomContainer molecule = TestMoleculeFactory.makePyridineOxide();
        Iterator<IBond> bonds = molecule.bonds().iterator();
        while (bonds.hasNext())
            bonds.next().setOrder(Order.SINGLE);
        for (int i = 0; i < 6; i++) {
            molecule.getAtom(i).setHybridization(IAtomType.Hybridization.SP2);
        }
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule));
    }

    @Test
    public void testFuran() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        IAtomContainer mol = sp.parseSmiles("c1cocc1");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue("Molecule is not detected aromatic",
                DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));

        IRingSet ringset = (new SSSRFinder(mol)).findSSSR();
        int numberOfAromaticRings = 0;
        RingSetManipulator.markAromaticRings(ringset);
        for (int i = 0; i < ringset.getAtomContainerCount(); i++) {
            if (ringset.getAtomContainer(i).getFlag(CDKConstants.ISAROMATIC)) numberOfAromaticRings++;
        }
        Assert.assertEquals(1, numberOfAromaticRings);
    }

    /**
     * A unit test for JUnit The special difficulty with Azulene is that only the
     * outermost larger 10-ring is aromatic according to Hueckel rule.
     */
    @Test
    public void testAzulene() throws Exception {
        boolean[] testResults = {true, true, true, true, true, true, true, true, true, true};
        IAtomContainer molecule = TestMoleculeFactory.makeAzulene();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        Assert.assertTrue("Expected the molecule to be aromatic.",
                DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule));
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals("Atom " + f + " is not correctly marked", testResults[f],
                    molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * A unit test for JUnit. The N has to be counted correctly.
     */
    @Test
    public void testIndole() throws Exception {
        IAtomContainer molecule = TestMoleculeFactory.makeIndole();
        boolean testResults[] = {true, true, true, true, true, true, true, true, true};
        //boolean isAromatic = false;
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        Assert.assertTrue("Expected the molecule to be aromatic.",
                DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule));
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals("Atom " + f + " is not correctly marked", testResults[f],
                    molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * A unit test for JUnit. The N has to be counted correctly.
     */
    @Test
    public void testPyrrole() throws Exception {
        IAtomContainer molecule = TestMoleculeFactory.makePyrrole();
        boolean testResults[] = {true, true, true, true, true};
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        Assert.assertTrue("Expected the molecule to be aromatic.",
                DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule));
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals("Atom " + f + " is not correctly marked", testResults[f],
                    molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * A unit test for JUnit
     */
    @Test
    public void testThiazole() throws Exception {
        IAtomContainer molecule = TestMoleculeFactory.makeThiazole();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        Assert.assertTrue("Molecule is not detected as aromatic",
                DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule));

        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertTrue("Atom " + f + " is not correctly marked",
                    molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * A unit test for JUnit
     */
    @Test
    public void testTetraDehydroDecaline() throws Exception {
        boolean isAromatic = false;
        //boolean testResults[] = {true, false, false};
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        IAtomContainer mol = sp.parseSmiles("C1CCCc2c1cccc2");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue("Expected the molecule to be aromatic.",
                DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
        IRingSet rs = (new AllRingsFinder()).findAllRings(mol);
        RingSetManipulator.markAromaticRings(rs);
        IRing r = null;
        int aromacount = 0;
        Iterator<IAtomContainer> rings = rs.atomContainers().iterator();
        while (rings.hasNext()) {
            r = (IRing) rings.next();
            isAromatic = r.getFlag(CDKConstants.ISAROMATIC);

            if (isAromatic) aromacount++;
        }
        Assert.assertEquals(1, aromacount);
    }

    /**
     * This is a bug reported for JCP.
     *
     * @cdk.bug 956924
     */
    @Test
    public void testSFBug956924() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        IAtomContainer mol = sp.parseSmiles("[cH+]1cccccc1"); // tropylium cation
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertEquals(IAtomType.Hybridization.PLANAR3, mol.getAtom(0).getHybridization());
        for (int f = 1; f < mol.getAtomCount(); f++) {
            Assert.assertEquals(IAtomType.Hybridization.SP2, mol.getAtom(f).getHybridization());
        }
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
        Assert.assertEquals(7, mol.getAtomCount());
        for (int f = 0; f < mol.getAtomCount(); f++) {
            Assert.assertNotNull(mol.getAtom(f));
            Assert.assertTrue(mol.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * This is a bug reported for JCP.
     *
     * @cdk.bug 956923
     */
    @Test
    public void testSFBug956923() throws Exception {
        boolean testResults[] = {false, false, false, false, false, false, false, false};
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        IAtomContainer mol = sp.parseSmiles("O=C1C=CC=CC=C1"); // tropone
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertFalse(DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
        Assert.assertEquals(testResults.length, mol.getAtomCount());
        for (int f = 0; f < mol.getAtomCount(); f++) {
            Assert.assertEquals(testResults[f], mol.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    @Test
    public void testNoxide() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("C=1C=CC(=CC1)CNC2=CC=C(C=C2N(=O)=O)S(=O)(=O)C(Cl)(Cl)Br");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
    }

    /**
     * A unit test for JUnit
     */
    @Test
    public void testPorphyrine() throws Exception {
        boolean isAromatic = false;
        boolean testResults[] = {false, false, false, false, false, true, true, true, true, true, false, true, true,
                true, false, true, true, false, false, true, true, false, false, false, true, true, false, false,
                false, true, true, false, false, false, false, true, true, true, true, false, false, false};

        String filename = "data/mdl/porphyrin.mol";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        MDLV2000Reader reader = new MDLV2000Reader(ins);
        IAtomContainer molecule = (IAtomContainer) reader.read(DefaultChemObjectBuilder.getInstance().newInstance(
                IAtomContainer.class));
        reader.close();

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        isAromatic = DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule);
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals(testResults[f], molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
        Assert.assertTrue(isAromatic);
    }

    /**
     * A unit test for JUnit
     *
     * @cdk.bug 698152
     */
    @Test
    public void testBug698152() throws Exception {
        //boolean isAromatic = false;
        boolean[] testResults = {true, true, true, true, true, true, false, false, false, false, false, false, false,
                false, false, false, false, false};

        String filename = "data/mdl/bug698152.mol";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        MDLV2000Reader reader = new MDLV2000Reader(ins);
        IAtomContainer molecule = (IAtomContainer) reader.read(DefaultChemObjectBuilder.getInstance().newInstance(
                IAtomContainer.class));
        reader.close();

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule);
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals(testResults[f], molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * A unit test for JUnit
     */
    @Test
    public void testQuinone() throws Exception {
        IAtomContainer molecule = TestMoleculeFactory.makeQuinone();
        boolean[] testResults = {false, true, true, true, true, true, true, false};

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule);
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertEquals(testResults[f], molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }

    }

    /**
     * @cdk.bug 1328739
     */
    @Test
    public void testBug1328739() throws Exception {
        String filename = "data/mdl/bug1328739.mol";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        MDLV2000Reader reader = new MDLV2000Reader(ins);
        IAtomContainer molecule = (IAtomContainer) reader.read(DefaultChemObjectBuilder.getInstance().newInstance(
                IAtomContainer.class));
        reader.close();

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule);

        Assert.assertEquals(15, molecule.getBondCount());
        Assert.assertTrue(molecule.getBond(0).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(1).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(2).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(3).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(4).getFlag(CDKConstants.ISAROMATIC));
        Assert.assertTrue(molecule.getBond(6).getFlag(CDKConstants.ISAROMATIC));
    }

    /**
     * A unit test for JUnit
     */
    @Test
    public void testBenzene() throws Exception {
        IAtomContainer molecule = TestMoleculeFactory.makeBenzene();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule);
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertTrue(molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    @Test
    public void testCyclobutadiene() throws Exception {
        // anti-aromatic
        IAtomContainer molecule = TestMoleculeFactory.makeCyclobutadiene();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);

        Assert.assertFalse(DoubleBondAcceptingAromaticityDetector.detectAromaticity(molecule));
        for (int f = 0; f < molecule.getAtomCount(); f++) {
            Assert.assertFalse(molecule.getAtom(f).getFlag(CDKConstants.ISAROMATIC));
        }
    }

    private IAtomContainer makeAromaticMolecule() {
        IAtomContainer mol = DefaultChemObjectBuilder.getInstance().newInstance(IAtomContainer.class);
        IAtom a1 = mol.getBuilder().newInstance(IAtom.class, "C");
        a1.setPoint2d(new Point2d(329.99999999999994, 971.0));
        mol.addAtom(a1);
        IAtom a2 = mol.getBuilder().newInstance(IAtom.class, "C");
        a2.setPoint2d(new Point2d(298.8230854637602, 989.0));
        mol.addAtom(a2);
        IAtom a3 = mol.getBuilder().newInstance(IAtom.class, "C");
        a3.setPoint2d(new Point2d(298.8230854637602, 1025.0));
        mol.addAtom(a3);
        IAtom a4 = mol.getBuilder().newInstance(IAtom.class, "C");
        a4.setPoint2d(new Point2d(330.0, 1043.0));
        mol.addAtom(a4);
        IAtom a5 = mol.getBuilder().newInstance(IAtom.class, "C");
        a5.setPoint2d(new Point2d(361.1769145362398, 1025.0));
        mol.addAtom(a5);
        IAtom a6 = mol.getBuilder().newInstance(IAtom.class, "C");
        a6.setPoint2d(new Point2d(361.1769145362398, 989.0));
        mol.addAtom(a6);
        IAtom a7 = mol.getBuilder().newInstance(IAtom.class, "C");
        a7.setPoint2d(new Point2d(392.3538290724796, 971.0));
        mol.addAtom(a7);
        IAtom a8 = mol.getBuilder().newInstance(IAtom.class, "C");
        a8.setPoint2d(new Point2d(423.5307436087194, 989.0));
        mol.addAtom(a8);
        IAtom a9 = mol.getBuilder().newInstance(IAtom.class, "C");
        a9.setPoint2d(new Point2d(423.5307436087194, 1025.0));
        mol.addAtom(a9);
        IAtom a10 = mol.getBuilder().newInstance(IAtom.class, "C");
        a10.setPoint2d(new Point2d(392.3538290724796, 1043.0));
        mol.addAtom(a10);
        IBond b1 = mol.getBuilder().newInstance(IBond.class, a1, a2, IBond.Order.DOUBLE);
        mol.addBond(b1);
        IBond b2 = mol.getBuilder().newInstance(IBond.class, a2, a3, IBond.Order.SINGLE);
        mol.addBond(b2);
        IBond b3 = mol.getBuilder().newInstance(IBond.class, a3, a4, IBond.Order.DOUBLE);
        mol.addBond(b3);
        IBond b4 = mol.getBuilder().newInstance(IBond.class, a4, a5, IBond.Order.SINGLE);
        mol.addBond(b4);
        IBond b5 = mol.getBuilder().newInstance(IBond.class, a5, a6, IBond.Order.DOUBLE);
        mol.addBond(b5);
        IBond b6 = mol.getBuilder().newInstance(IBond.class, a6, a1, IBond.Order.SINGLE);
        mol.addBond(b6);
        IBond b7 = mol.getBuilder().newInstance(IBond.class, a6, a7, IBond.Order.SINGLE);
        mol.addBond(b7);
        IBond b8 = mol.getBuilder().newInstance(IBond.class, a7, a8, IBond.Order.SINGLE);
        mol.addBond(b8);
        IBond b9 = mol.getBuilder().newInstance(IBond.class, a8, a9, IBond.Order.SINGLE);
        mol.addBond(b9);
        IBond b10 = mol.getBuilder().newInstance(IBond.class, a9, a10, IBond.Order.SINGLE);
        mol.addBond(b10);
        IBond b11 = mol.getBuilder().newInstance(IBond.class, a10, a5, IBond.Order.SINGLE);
        mol.addBond(b11);
        return mol;
    }

    /**
     * @cdk.bug 1957684
     */
    @Test
    public void test3Amino2MethylPyridine() throws Exception {

        IAtomContainer mol = new AtomContainer();
        IAtom a1 = mol.getBuilder().newInstance(IAtom.class, "N");
        a1.setPoint2d(new Point2d(3.7321, 1.345));
        mol.addAtom(a1);
        IAtom a2 = mol.getBuilder().newInstance(IAtom.class, "N");
        a2.setPoint2d(new Point2d(4.5981, -1.155));
        mol.addAtom(a2);
        IAtom a3 = mol.getBuilder().newInstance(IAtom.class, "C");
        a3.setPoint2d(new Point2d(2.866, -0.155));
        mol.addAtom(a3);
        IAtom a4 = mol.getBuilder().newInstance(IAtom.class, "C");
        a4.setPoint2d(new Point2d(3.7321, 0.345));
        mol.addAtom(a4);
        IAtom a5 = mol.getBuilder().newInstance(IAtom.class, "C");
        a5.setPoint2d(new Point2d(2.866, -1.155));
        mol.addAtom(a5);
        IAtom a6 = mol.getBuilder().newInstance(IAtom.class, "C");
        a6.setPoint2d(new Point2d(2.0, 0.345));
        mol.addAtom(a6);
        IAtom a7 = mol.getBuilder().newInstance(IAtom.class, "C");
        a7.setPoint2d(new Point2d(4.5981, -0.155));
        mol.addAtom(a7);
        IAtom a8 = mol.getBuilder().newInstance(IAtom.class, "C");
        a8.setPoint2d(new Point2d(3.7321, -1.655));
        mol.addAtom(a8);
        IAtom a9 = mol.getBuilder().newInstance(IAtom.class, "H");
        a9.setPoint2d(new Point2d(2.3291, -1.465));
        mol.addAtom(a9);
        IAtom a10 = mol.getBuilder().newInstance(IAtom.class, "H");
        a10.setPoint2d(new Point2d(2.31, 0.8819));
        mol.addAtom(a10);
        IAtom a11 = mol.getBuilder().newInstance(IAtom.class, "H");
        a11.setPoint2d(new Point2d(1.4631, 0.655));
        mol.addAtom(a11);
        IAtom a12 = mol.getBuilder().newInstance(IAtom.class, "H");
        a12.setPoint2d(new Point2d(1.69, -0.1919));
        mol.addAtom(a12);
        IAtom a13 = mol.getBuilder().newInstance(IAtom.class, "H");
        a13.setPoint2d(new Point2d(5.135, 0.155));
        mol.addAtom(a13);
        IAtom a14 = mol.getBuilder().newInstance(IAtom.class, "H");
        a14.setPoint2d(new Point2d(3.7321, -2.275));
        mol.addAtom(a14);
        IAtom a15 = mol.getBuilder().newInstance(IAtom.class, "H");
        a15.setPoint2d(new Point2d(4.269, 1.655));
        mol.addAtom(a15);
        IAtom a16 = mol.getBuilder().newInstance(IAtom.class, "H");
        a16.setPoint2d(new Point2d(3.1951, 1.655));
        mol.addAtom(a16);
        IBond b1 = mol.getBuilder().newInstance(IBond.class, a1, a4, IBond.Order.SINGLE);
        mol.addBond(b1);
        IBond b2 = mol.getBuilder().newInstance(IBond.class, a1, a15, IBond.Order.SINGLE);
        mol.addBond(b2);
        IBond b3 = mol.getBuilder().newInstance(IBond.class, a1, a16, IBond.Order.SINGLE);
        mol.addBond(b3);
        IBond b4 = mol.getBuilder().newInstance(IBond.class, a2, a7, IBond.Order.DOUBLE);
        mol.addBond(b4);
        IBond b5 = mol.getBuilder().newInstance(IBond.class, a2, a8, IBond.Order.SINGLE);
        mol.addBond(b5);
        IBond b6 = mol.getBuilder().newInstance(IBond.class, a3, a4, IBond.Order.DOUBLE);
        mol.addBond(b6);
        IBond b7 = mol.getBuilder().newInstance(IBond.class, a3, a5, IBond.Order.SINGLE);
        mol.addBond(b7);
        IBond b8 = mol.getBuilder().newInstance(IBond.class, a3, a6, IBond.Order.SINGLE);
        mol.addBond(b8);
        IBond b9 = mol.getBuilder().newInstance(IBond.class, a4, a7, IBond.Order.SINGLE);
        mol.addBond(b9);
        IBond b10 = mol.getBuilder().newInstance(IBond.class, a5, a8, IBond.Order.DOUBLE);
        mol.addBond(b10);
        IBond b11 = mol.getBuilder().newInstance(IBond.class, a5, a9, IBond.Order.SINGLE);
        mol.addBond(b11);
        IBond b12 = mol.getBuilder().newInstance(IBond.class, a6, a10, IBond.Order.SINGLE);
        mol.addBond(b12);
        IBond b13 = mol.getBuilder().newInstance(IBond.class, a6, a11, IBond.Order.SINGLE);
        mol.addBond(b13);
        IBond b14 = mol.getBuilder().newInstance(IBond.class, a6, a12, IBond.Order.SINGLE);
        mol.addBond(b14);
        IBond b15 = mol.getBuilder().newInstance(IBond.class, a7, a13, IBond.Order.SINGLE);
        mol.addBond(b15);
        IBond b16 = mol.getBuilder().newInstance(IBond.class, a8, a14, IBond.Order.SINGLE);
        mol.addBond(b16);

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        boolean isAromatic = DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol);
        Assert.assertTrue(isAromatic);

        Iterator<IAtom> atoms = mol.atoms().iterator();
        int nCarom = 0;
        int nCalip = 0;
        int nNarom = 0;
        int nNaliph = 0;
        while (atoms.hasNext()) {
            IAtom atom = atoms.next();
            if (atom.getSymbol().equals("C")) {
                if (atom.getFlag(CDKConstants.ISAROMATIC))
                    nCarom++;
                else
                    nCalip++;
            } else if (atom.getSymbol().equals("N")) {
                if (atom.getFlag(CDKConstants.ISAROMATIC))
                    nNarom++;
                else
                    nNaliph++;
            }

        }
        Assert.assertEquals(5, nCarom);
        Assert.assertEquals(1, nCalip);
        Assert.assertEquals(1, nNarom);
        Assert.assertEquals(1, nNaliph);
    }

    /**
     * @cdk.bug 2185255
     * @throws CDKException
     */
    @Test
    public void testPolyCyclicSystem() throws Exception {
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(DefaultChemObjectBuilder.getInstance());
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

        IAtomContainer kekuleForm = sp.parseSmiles("C1=CC2=CC3=CC4=C(C=CC=C4)C=C3C=C2C=C1");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(kekuleForm);
        adder.addImplicitHydrogens(kekuleForm);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(kekuleForm);

        IAtomContainer aromaticForm = sp.parseSmiles("c1ccc2cc3cc4ccccc4cc3cc2c1");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(aromaticForm);
        adder.addImplicitHydrogens(aromaticForm);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(aromaticForm);

        boolean isAromatic = DoubleBondAcceptingAromaticityDetector.detectAromaticity(kekuleForm);
        Assert.assertTrue(isAromatic);

        isAromatic = DoubleBondAcceptingAromaticityDetector.detectAromaticity(aromaticForm);
        Assert.assertTrue(isAromatic);

        // double bond locations may alter. So, we can expect things like in a 'diff': "BondDiff{order:SINGLE/DOUBLE}"
        // but, we should not see aromaticity differences
        for (IAtom atom : aromaticForm.atoms()) {
            if ("C".equals(atom.getSymbol())) {
                Assert.assertEquals("C.sp2", atom.getAtomTypeName());
                Assert.assertTrue(atom.getFlag(CDKConstants.ISAROMATIC));
            }
        }
        for (IAtom atom : kekuleForm.atoms()) {
            if ("C".equals(atom.getSymbol())) {
                Assert.assertEquals("C.sp2", atom.getAtomTypeName());
                Assert.assertTrue(atom.getFlag(CDKConstants.ISAROMATIC));
            }
        }
    }

    /**
     * @cdk.bug 1579235
     */
    @Test
    public void testIndolizine() throws CDKException {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer aromaticForm = sp.parseSmiles("c2cc1cccn1cc2");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(aromaticForm);
        boolean isAromatic = DoubleBondAcceptingAromaticityDetector.detectAromaticity(aromaticForm);
        Assert.assertTrue(isAromatic);

        // all atoms are supposed to be aromatic
        for (IAtom atom : aromaticForm.atoms()) {
            Assert.assertTrue(atom.toString() + " should be aromatic", atom.getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * @cdk.bug 2976054
     */
    @Test
    public void testAnotherNitrogen_SP2() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("c1cnc2s[cH][cH]n12");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
        for (IAtom atom : mol.atoms())
            Assert.assertTrue(atom.getFlag(CDKConstants.ISAROMATIC));
    }

    /**
     * Tests to check for aromaticity issues in SMARTS matches
     *
     * @throws Exception
     */
    @Test
    public void testAromaticNOxide() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("O=n1ccccc1");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
        for (IAtom atom : mol.atoms()) {
            if (atom.getSymbol().equals("O")) continue;
            Assert.assertTrue(atom.getSymbol() + " was not aromatic but should have been",
                    atom.getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * Tests to check for aromaticity issues in SMARTS matches
     *
     * @throws Exception
     */
    @Test
    public void testAromaticNOxideCharged() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("[O-][n+]1ccccc1");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
        for (IAtom atom : mol.atoms()) {
            if (atom.getSymbol().equals("O")) {
                Assert.assertFalse("The oxygen should not be labeled as aromatic",
                        atom.getFlag(CDKConstants.ISAROMATIC));
                continue;
            }
            Assert.assertTrue(atom.getSymbol() + " was not aromatic but should have been",
                    atom.getFlag(CDKConstants.ISAROMATIC));
        }
    }

    @Test
    public void testSMILES() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("C=1N=CNC=1");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
        for (IAtom atom : mol.atoms())
            Assert.assertTrue("Atom is expected to be aromatic: " + atom, atom.getFlag(CDKConstants.ISAROMATIC));
    }

    @Test
    public void testSMILES2() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("OCN1C=CN=C1");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
        for (int i = 2; i <= 6; i++) {
            IAtom atom = mol.getAtom(i);
            Assert.assertTrue("Atom is expected to be aromatic: " + atom, atom.getFlag(CDKConstants.ISAROMATIC));
        }
    }

    @Test
    public void testSMILES3() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("OC(=O)N1C=CN=C1");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
        for (int i = 3; i <= 7; i++) {
            IAtom atom = mol.getAtom(i);
            Assert.assertTrue("Atom is expected to be aromatic: " + atom, atom.getFlag(CDKConstants.ISAROMATIC));
        }
    }

    /**
     * @cdk.bug 3001616
     */
    @Test
    public void test3001616() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("OC(=O)N1C=NC2=CC=CC=C12");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
        for (IAtom atom : mol.atoms()) {
            if (atom.getSymbol().equals("N")) {
                Assert.assertTrue(atom.getFlag(CDKConstants.ISAROMATIC));
                List<IBond> conbonds = mol.getConnectedBondsList(atom);
                if (conbonds.size() == 2) {
                    Assert.assertTrue(conbonds.get(0).getFlag(CDKConstants.ISAROMATIC));
                    Assert.assertTrue(conbonds.get(1).getFlag(CDKConstants.ISAROMATIC));
                } else if (conbonds.size() == 3) {
                    for (IBond bond : conbonds) {
                        if (bond.getOrder().equals(IBond.Order.SINGLE)) continue;
                        Assert.assertTrue(bond.getFlag(CDKConstants.ISAROMATIC));
                    }
                }
            }
        }
        SpanningTree st = new SpanningTree(mol);
        IRingSet ringSet = st.getAllRings();
        for (IAtomContainer ring : ringSet.atomContainers()) {
            for (IBond bond : ring.bonds()) {
                Assert.assertTrue(bond.getFlag(CDKConstants.ISAROMATIC));
            }
        }
    }

    /**
     * @cdk.bug 2853035
     */
    @Test
    public void testBug2853035() throws Exception {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer mol = sp.parseSmiles("C(=O)c1cnn2ccccc12");
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
        Assert.assertTrue(DoubleBondAcceptingAromaticityDetector.detectAromaticity(mol));
        for (IAtom atom : mol.atoms()) {
            if (atom.getSymbol().equals("N")) {
                Assert.assertTrue(atom.getFlag(CDKConstants.ISAROMATIC));
                List<IBond> conbonds = mol.getConnectedBondsList(atom);
                for (IBond bond : conbonds) {
                    if (bond.getOrder().equals(IBond.Order.SINGLE)) continue;
                    Assert.assertTrue(bond.getFlag(CDKConstants.ISAROMATIC));
                }
            }
        }
        SpanningTree st = new SpanningTree(mol);
        IRingSet ringSet = st.getAllRings();
        for (IAtomContainer ring : ringSet.atomContainers()) {
            for (IBond bond : ring.bonds()) {
                Assert.assertTrue(bond.getFlag(CDKConstants.ISAROMATIC));
            }
        }
    }

    /**
     * Tests that 1-4 benzoquinone is detected to be aromatic by the DoubleBondAcceptingAromaticityDetector but
     * not the CDKHueckelAromaticityDetector
     */
    @Test
    public void testBenzoquinone() throws Exception {

        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();

        IAtomContainer benzoquinone = TestMoleculeFactory.makeCyclohexane();

        benzoquinone.getBond(1).setOrder(IBond.Order.DOUBLE);
        benzoquinone.getBond(4).setOrder(IBond.Order.DOUBLE);

        IAtom o7 = builder.newInstance(IAtom.class, "O");
        IAtom o8 = builder.newInstance(IAtom.class, "O");

        IBond c1o7 = builder.newInstance(IBond.class, benzoquinone.getAtom(0), o7, IBond.Order.DOUBLE);
        IBond c4o8 = builder.newInstance(IBond.class, benzoquinone.getAtom(3), o8, IBond.Order.DOUBLE);

        benzoquinone.addAtom(o7);
        benzoquinone.addAtom(o8);
        benzoquinone.addBond(c1o7);
        benzoquinone.addBond(c4o8);

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(benzoquinone);

        Assert.assertFalse("Hueckel method detect aromaticity", Aromaticity.cdkLegacy().apply(benzoquinone));
        Assert.assertTrue("DoubleBond Accepting AromaticityDetector method did not detect aromaticity",
                DoubleBondAcceptingAromaticityDetector.detectAromaticity(benzoquinone));

    }

}
