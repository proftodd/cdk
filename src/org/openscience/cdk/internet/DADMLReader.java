/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2001-2004  The Chemistry Development Kit (CDK) project
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
 *  
 */
package org.openscience.cdk.internet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemModel;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.ChemSequence;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.SetOfMolecules;
import org.openscience.cdk.exception.UnsupportedChemObjectException;
import org.openscience.cdk.io.ChemObjectReader;
import org.openscience.cdk.io.DefaultChemObjectReader;
import org.openscience.cdk.io.ReaderFactory;
import org.openscience.dadml.DATABASE;
import org.openscience.dadml.DBDEF;
import org.openscience.dadml.DBLIST;
import org.openscience.dadml.FIELD;
import org.openscience.dadml.INDEX;
import org.openscience.dadml.filereaders.DBDEFFileReader;
import org.openscience.dadml.filereaders.DBLISTFileReader;
import org.openscience.dadml.tools.DBDEFInfo;

/**
 * Reads a molecule from a DADML super database.
 *
 * <p>Database Access Definition Markup Language (DADML) is an XML
 * application that makes it possible to define how databases can be accessed
 * via URLs.
 *
 * @author     Egon Willighagen <egonw@sci.kun.nl>
 * @created    2001-12-18
 *
 * @keyword    internet
 * @keyword    database
 * @builddepends dadml.jar
 */
public class DADMLReader extends DefaultChemObjectReader {

    private String superdb;

    private org.openscience.cdk.tools.LoggingTool logger;
    private static final String sax2parser = "org.apache.xerces.parsers.SAXParser";

    private URI query;
    
    /**
     * Contructs a new DADMLReader that can read Molecule from the internet.
     *
     * @param   superdb DADML super database to look up structure from
     */
    public DADMLReader(String superdb) {
        logger = new org.openscience.cdk.tools.LoggingTool(this.getClass().getName());
        this.superdb = superdb;
        this.query = null;
    }

    public String getFormatName() {
        return "DADML network";
    }
    
    /**
     * Sets the query.
     *
     * @param   indexType   Index type (e.g. CAS-NUMBER)
     * @param   value   Index of molecule to download (e.g. 50-00-0)
     */
    public void setQuery(String indexType, String value) {
        try {
            this.query = new URI("dadml://any/" + indexType + "?" + value);
        } catch (URISyntaxException exception) {
            logger.error("Serious error: " + exception.getMessage());
            logger.debug(exception);
        }
    }

    /**
     * Sets the query in the form &quot;dadml://any/CAS-NUMBER?50-00-0&quot;.
     *
     * @param   URI     URI query.
     */
    public void setQuery(URI query) throws URISyntaxException {
        this.query = query;
    }

    /**
     * Takes an object which subclasses ChemObject, e.g.Molecule, and will read this
     * (from file, database, internet etc). If the specific implementation does not
     * support a specific ChemObject it will throw an Exception.
     *
     * @param   object  The object that subclasses ChemObject
     * @return   The ChemObject read
     * @exception   UnsupportedChemObjectException
     */
    public ChemObject read(ChemObject object) throws UnsupportedChemObjectException {
        if (object instanceof Molecule) {
            return (ChemObject)readMolecule();
        } else {
            throw new UnsupportedChemObjectException("Only supported is Molecule.");
        }
    }

    /**
     * Read a Molecule from a DADML super database.
     *
     * @return The Molecule that was read
     */
    private Molecule readMolecule() {
        Molecule molecule = null;
        try {
            URL resource = this.resolveLink(query);
            // this has to be reformulated
            molecule = this.downloadURL(resource);
        } catch (Exception exception) {
            logger.error("File Not Found: " + exception.getMessage());
            logger.debug(exception);
        }
        return molecule;
    }
    
    public URL resolveLink(URI dadmlRI) {
        boolean found = false; // this is true when a structure is downloaded
        boolean done = false;  // this is true when all URLS have been tested
        
        String indexType = dadmlRI.getPath().substring(1);
        String index = dadmlRI.getQuery();
        
        Molecule molecule = new Molecule();
        DBLIST dblist = new DBLIST();
        try {
            logger.info("Downloading DADML super database: " + this.superdb);
            // Proxy authorization has to be ported from Chemistry Development Kit (CKD)
            // for now, do without authorization
            dblist = DBLISTFileReader.read(superdb, sax2parser);
        } catch (Exception supererror) {
            logger.error(supererror.toString());
        }
        Enumeration dbases = dblist.databases();
        while (!found && !done && dbases.hasMoreElements()) {
            DATABASE database = (DATABASE)dbases.nextElement();
            String dburl = database.getURL() + database.getDefinition();
            DBDEF dbdef = new DBDEF();
            // Proxy authorization has to be ported from Chemistry Development Kit (CKD)
            // for now, do without authorization
            try {
                logger.info("Downloading: " + dburl);
                // do without authorization
                dbdef = DBDEFFileReader.read(dburl, sax2parser);
            } catch (Exception deferror) {
                System.err.println(deferror.toString());
            }
            if (DBDEFInfo.hasINDEX(dbdef, indexType)) {
                // oke, find a nice URL to use for download
                logger.debug("Trying: " + dbdef.getTITLE());
                Enumeration fields = dbdef.fields();
                while (fields.hasMoreElements()) {
                    FIELD field = (FIELD)fields.nextElement();
                    String mime = field.getMIMETYPE();
                    String ftype = field.getTYPE();
                    if ((mime.equals("chemical/x-mdl-mol") ||
                         mime.equals("chemical/x-pdb") ||
                         mime.equals("chemical/x-cml")) &&
                         (ftype.equals("3DSTRUCTURE") ||
                          ftype.equals("2DSTRUCTURE"))) {
                        logger.info("Accepted: " + field.getMIMETYPE() + "," + field.getTYPE());
                        Enumeration indices = field.getINDEX();
                        while (indices.hasMoreElements()) {
                            INDEX ind = (INDEX)indices.nextElement();
                            if (ind.getTYPE().equals(indexType)) {
                                // here is the URL composed
                                String url = dbdef.getURL() + ind.getACCESS_PREFIX() + index + ind.getACCESS_SUFFIX();
                                try {
                                    return new URL(url);
                                } catch (MalformedURLException exception) {
                                    logger.error("Malformed URL: " + exception.getMessage());
                                    logger.debug(exception);
                                }
                           }
                        }
                    } else {
                        // reject other mime types && type structures
                        logger.info("Rejected: " + field.getMIMETYPE() + "," + field.getTYPE());
                    }
                }
            } else {
                logger.warn("Database does not have indexType: " + indexType);
            }
        }
        return null;
    }

    private Molecule downloadURL(URL resource) {
        Molecule molecule = new Molecule();
        try {
            URLConnection connection = resource.openConnection();
            BufferedReader bufReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
            );
            ChemObjectReader reader = new ReaderFactory().createReader(bufReader);
            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
            logger.debug("#sequences: " + chemFile.getChemSequenceCount());
            ChemSequence chemSequence = chemFile.getChemSequence(0);
            logger.debug("#models in sequence: " + chemSequence.getChemModelCount());
            ChemModel chemModel = chemSequence.getChemModel(0);
            SetOfMolecules moleculeSet = chemModel.getSetOfMolecules();
            logger.debug("#mols in model: " + moleculeSet.getMoleculeCount());
            molecule = moleculeSet.getMolecule(0);
        } catch (UnsupportedChemObjectException exception) {
            logger.error("Unsupported ChemObject type: " + exception.getMessage());
            logger.debug(exception);
        } catch (FileNotFoundException exception) {
            logger.error("File not found: " + exception.getMessage());
            logger.debug(exception);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            logger.debug(exception);
        }
        return molecule;
    }

    public void close() throws IOException {
    }
}
