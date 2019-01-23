/* Copyright (C) 2012  Egon Willighagen <egonw@users.sf.net>
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. All we ask is that proper credit is given for our work,
 * which includes - but is not limited to - adding the above copyright notice to
 * the beginning of your source code files, and to any copyright notice that you
 * may distribute with programs based on this work.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.renderextra.generators;

import java.awt.Rectangle;
import java.util.List;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.renderbasic.generators.AbstractGeneratorTest;
import org.openscience.cdk.renderbasic.generators.BasicAtomGenerator;
import org.openscience.cdk.renderbasic.generators.BasicBondGenerator;
import org.openscience.cdk.renderbasic.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderextra.generators.BoundsGenerator;

/**
 * @cdk.module test-renderextra
 */
public class BoundsGeneratorTest extends AbstractGeneratorTest {

    private BoundsGenerator generator;

    @Override
    public Rectangle getCustomCanvas() {
        return null;
    }

    @Before
    @Override
    public void setup() {
        super.setup();
        model.registerParameters(new BasicSceneGenerator());
        model.registerParameters(new BasicAtomGenerator());
        model.registerParameters(new BasicBondGenerator());
        this.generator = new BoundsGenerator();
        model.registerParameters(generator);
        super.setTestedGenerator(generator);
    }

    @Test
    public void testEmptyReaction() {
        IReaction emptyReaction = super.builder.newInstance(IReaction.class);

        // nothing should be made
        IRenderingElement root = generator.generate(emptyReaction, model);
        List<IRenderingElement> elements = elementUtil.getAllSimpleElements(root);
        Assert.assertEquals(2, elements.size());
    }

}