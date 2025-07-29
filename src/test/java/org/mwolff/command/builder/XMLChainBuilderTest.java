/** Simple Command Framework.
 *
 * Framework for easy building software that fits the SOLID principles.
 *
 * @author Manfred Wolff <m.wolff@neusta.de>
 *
 *         Download:
 *         https://github.com/simplecommand/command.git
 *
 *         Copyright (C) 2018-2021 Manfred Wolff and the simple command community
 *
 *         This library is free software; you can redistribute it and/or
 *         modify it under the terms of the GNU Lesser General Public
 *         License as published by the Free Software Foundation; either
 *         version 2.1 of the License, or (at your option) any later version.
 *
 *         This library is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         Lesser General Public License for more details.
 *
 *         You should have received a copy of the GNU Lesser General Public
 *         License along with this library; if not, write to the Free Software
 *         Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *         02110-1301
 *         USA */

package org.mwolff.command.builder;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mwolff.command.interfaces.CommandTransition;
import org.mwolff.command.parameterobject.DefaultParameterObject;
import org.mwolff.command.parameterobject.GenericParameterObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mwolff.command.interfaces.CommandTransition.*;

public class XMLChainBuilderTest {

    @Test
    public void testUnUsedSetProcessID() {
        final XMLChainBuilder<Object> xmlChainBuilder = new XMLChainBuilder<>("commandChainProcess.xml");

        final Throwable exception = Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            xmlChainBuilder.setProcessID("");
        });
        assertThat(exception.getMessage(), CoreMatchers.is("Chainbuilder has no process id."));
    }

    @Test
    public void chainbuilderExists() throws Exception {
        final XMLChainBuilder<Object> xmlChainBuilder = new XMLChainBuilder<>("");
        assertThat(xmlChainBuilder, CoreMatchers.instanceOf(XMLChainBuilder.class));
    }

    @Test
    public void createInvalidXMLDocument() throws Exception {
        final GenericParameterObject context = DefaultParameterObject.getInstance();
        final XMLChainBuilder<GenericParameterObject> xmlChainBuilder = new XMLChainBuilder<>("invalidXMLDocument.xml");
        final CommandTransition result = xmlChainBuilder.executeCommand(context);
        assertThat(result, CoreMatchers.is(FAILURE));
    }

    @Test
    public void testExecuteAsProcessMethodForBuilderWIthException() throws Exception {
        final XMLChainBuilder<Object> xmlChainBuilder = new XMLChainBuilder<>("notExists.xml");
        final DefaultParameterObject context = new DefaultParameterObject();
        xmlChainBuilder.executeCommand(context);
        assertNull(xmlChainBuilder.getProcessID());
    }

    @Test
    public void testExecuteAsProcessWithException() throws Exception {
        final XMLChainBuilder<Object> xmlChainBuilder = new XMLChainBuilder<>("/commandChainProcessNotExists.xml");
        final DefaultParameterObject context = new DefaultParameterObject();
        final String result = xmlChainBuilder.executeAsProcess("Start", context);
        assertNull(result);
    }

    @Test
    public void testExecuteAsProcess() throws Exception {
        final XMLChainBuilder<GenericParameterObject> builder = new XMLChainBuilder<>("/commandChainProcess.xml");
        final Throwable exception = Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            builder.executeAsProcess(new DefaultParameterObject());
        });
        assertThat(exception.getMessage(), CoreMatchers.is("Use executeAsProcess(String start, T context"));
    }

    @Test
    public void testExecuteCommand() throws Exception {
        final XMLChainBuilder<Object> xmlChainBuilder = new XMLChainBuilder<>("/commandChainPriority.xml");
        final DefaultParameterObject context = new DefaultParameterObject();
        context.put("resultString", "");
        final CommandTransition transition = xmlChainBuilder.executeCommand(context);
        assertEquals("S-S-", context.getAsString("resultString"));
        assertEquals(transition, SUCCESS);
    }

    @Test
    public void testExecuteCommandSUCCESS() throws Exception {
        final XMLChainBuilder<Object> xmlChainBuilder = new XMLChainBuilder<>("/commandChainEnd.xml");
        final DefaultParameterObject context = new DefaultParameterObject();
        context.put("priority", "");
        final CommandTransition transition = xmlChainBuilder.executeCommandAsChain(context);
        assertEquals(transition, DONE);
    }

    @Test
    public void testExecuteCommandAsChainABORT() throws Exception {
        final XMLChainBuilder<Object> xmlChainBuilder = new XMLChainBuilder<>("/commandChainAbort.xml");
        final DefaultParameterObject context = new DefaultParameterObject();
        final CommandTransition transition = xmlChainBuilder.executeCommandAsChain(context);
        assertEquals(FAILURE, transition);
    }
}
