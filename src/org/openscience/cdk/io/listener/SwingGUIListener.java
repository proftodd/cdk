/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2003-2004  The Jmol Development Team
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *  02111-1307  USA.
 */
package org.openscience.cdk.io.listener;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.io.ReaderEvent;
import org.openscience.cdk.io.setting.BooleanIOSetting;
import org.openscience.cdk.io.setting.IOSetting;
import org.openscience.cdk.io.setting.OptionIOSetting;
import org.openscience.cdk.io.setting.StringIOSetting;

/**
 * Allows processing of IOSetting quesions which are passed to the user
 * by using Swing dialogs.
 *
 * @cdkPackage io
 *
 * @author  Egon Willighagen <egonw@sci.kun.nl>
 * @created 2003-07-18
 */
public class SwingGUIListener implements ReaderListener, WriterListener {

    private BufferedReader in;
    
    private Component frame = null;
    private int level = 0;
    
    /**
     * 0 = ask no questions
     * 3 = ask all questions
     */
    public SwingGUIListener(Component frame, int level) {
        this.level = level;
        this.frame = frame;
        this.setInputReader(new InputStreamReader(System.in));
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    /**
     * Overwrites the default reader from which the input is taken.
     */
    public void setInputReader(Reader reader) {
        if (reader instanceof BufferedReader) {
            this.in = (BufferedReader)reader;
        } else if (reader == null) {
            this.in = null;
        } else {
            this.in = new BufferedReader(reader);
        }
    }
    
    public void frameRead(ReaderEvent event) {
    };
    
    /**
     * Processes the IOSettings by listing the question, giving the options
     * and asking the user to provide their choice.
     *
     * <p>Note: if the input reader is <code>null</code>, then the method
     * does not wait for an answer, and takes the default.
     */
    public void processIOSettingQuestion(IOSetting setting) {
        // post the question
        if (setting.getLevel() < this.level) {
            String answer = setting.getSetting();
            
            if (setting instanceof BooleanIOSetting) {
                BooleanIOSetting boolSet = (BooleanIOSetting)setting;
                int n = JOptionPane.showConfirmDialog(frame,
                    setting.getQuestion(),
                    setting.getName(),
                    JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    answer = "true";
                } else if (n == JOptionPane.NO_OPTION) {
                    answer = "false";
                } else {
                    // default of setting
                }
            } else if (setting instanceof OptionIOSetting) {
                OptionIOSetting optionSetting = (OptionIOSetting)setting;
                Vector settings = optionSetting.getOptions();
                Enumeration elements = settings.elements();
                Object[] options = new Object[settings.size()];
                for (int i=0; i<options.length; i++) {
                    options[i] = elements.nextElement();
                }
                int n = JOptionPane.showOptionDialog(frame, setting.getQuestion(), setting.getName(),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, setting.getSetting());
                answer = (String)options[n];
            } else if (setting instanceof StringIOSetting) {
                StringIOSetting stringSetting = (StringIOSetting)setting;
                answer = JOptionPane.showInputDialog(frame,
                    setting.getQuestion(),
                    setting.getName(), JOptionPane.QUESTION_MESSAGE, null, null,
                    setting.getSetting()).toString();
            } else {
                answer =  JOptionPane.showInputDialog(frame,
                    setting.getQuestion(),
                    setting.getName(), JOptionPane.QUESTION_MESSAGE, null, null,
                    setting.getSetting()).toString();
            }

            try {
                setting.setSetting(answer);
            } catch (CDKException exception) {
            }
        } // else skip question
        
    };
 
    
 
}



