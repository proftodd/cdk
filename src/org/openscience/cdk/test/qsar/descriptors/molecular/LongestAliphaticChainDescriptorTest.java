package org.openscience.cdk.test.qsar.descriptors.molecular;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.LongestAliphaticChainDescriptor;
import org.openscience.cdk.qsar.result.IntegerResult;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.test.CDKTestCase;

/**
 * TestSuite that runs all QSAR tests.
 * @author      chhoppe from EUROSCREEN
 * @cdk.module test-qsar
 */

public class LongestAliphaticChainDescriptorTest extends CDKTestCase{
	public  LongestAliphaticChainDescriptorTest() {}
    
	public static Test suite() {
		return new TestSuite(LongestAliphaticChainDescriptorTest.class);
	}
    
	public void test1LongestAliphaticChainDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
		IMolecularDescriptor descriptor = new LongestAliphaticChainDescriptor();
		Object[] params = {new Boolean(true)};
		descriptor.setParameters(params);
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		IAtomContainer mol = sp.parseSmiles("CCCCc1ccccc1"); // benzol
		//logger.debug("test1>:"+((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
		assertEquals(4, ((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
	}
	public void test2LargestChainDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
		IMolecularDescriptor descriptor = new LongestAliphaticChainDescriptor();
		Object[] params = {new Boolean(true)};
		descriptor.setParameters(params);
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		IAtomContainer mol = sp.parseSmiles("C=CC=Cc1ccccc1"); 
		//logger.debug("test2>"+((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
		assertEquals(4, ((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
	}
	public void test3LargestChainDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
		IMolecularDescriptor descriptor = new LongestAliphaticChainDescriptor();
		Object[] params = {new Boolean(true)};
		descriptor.setParameters(params);
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		IAtomContainer mol = sp.parseSmiles("C=C(CCC1CC1C(C)C(C)C)C(C)CC2CCCC2"); 
		//logger.debug("test3>"+((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
		assertEquals(5, ((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
	}
	public void test4LargestChainDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
		IMolecularDescriptor descriptor = new LongestAliphaticChainDescriptor();
		Object[] params = {new Boolean(true)};
		descriptor.setParameters(params);
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		IAtomContainer mol = sp.parseSmiles("CCCCNCC"); 
		//logger.debug("test4>"+((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
		assertEquals(4, ((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
	}
	public void test5LargestChainDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
		IMolecularDescriptor descriptor = new LongestAliphaticChainDescriptor();
		Object[] params = {new Boolean(true)};
		descriptor.setParameters(params);
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		IAtomContainer mol = sp.parseSmiles("CC(C)(C)c1ccccc1"); 
		//logger.debug("test5>"+((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
		assertEquals(3, ((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
	}
	
	public void test6LargestChainDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
		IMolecularDescriptor descriptor = new LongestAliphaticChainDescriptor();
		Object[] params = {new Boolean(true)};
		descriptor.setParameters(params);
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		IAtomContainer mol = sp.parseSmiles("CC(C)(C)c2ccc(OCCCC(=O)Nc1nccs1)cc2"); 
		//logger.debug("test6>"+((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
		assertEquals(4, ((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
	}
	public void test7LargestChainDescriptor() throws ClassNotFoundException, CDKException, java.lang.Exception {
		IMolecularDescriptor descriptor = new LongestAliphaticChainDescriptor();
		Object[] params = {new Boolean(true)};
		descriptor.setParameters(params);
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		IAtomContainer mol = sp.parseSmiles("CC(=O)N1CCN(CC1)c2ccc(NC(=O)COc3ccc(cc3)C(C)(C)C)cc2"); 
		//logger.debug("test7>"+((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
		assertEquals(2, ((IntegerResult)descriptor.calculate(mol).getValue()).intValue());
	}
}
