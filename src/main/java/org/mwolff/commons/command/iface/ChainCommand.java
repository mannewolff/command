/**
    Simple Command Framework.

    Framework for easy building software that fits the SOLID principles.
    @author Manfred Wolff <m.wolff@neusta.de>
    Download: https://github.com/simplecommand/SimpleCommandFramework


    Copyright (C) 2015 neusta software development

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
    USA
 */
package org.mwolff.commons.command.iface;

import org.apache.log4j.Logger;

/**
 * ChainCommand interface for the command framework. The behavior of this method
 * is the chain of responsibility pattern.
 * 
 * Commands may act with generic command contexts. A context actually is a
 * parameter object which passes information along the whole chain.
 * 
 * @author Manfred Wolff <m.wolff@neusta.de>
 */
public interface ChainCommand<T extends ParameterObject> extends Command<T> {

    final Logger LOG = Logger.getLogger(ChainCommand.class);

    /**
     * Executes a command as a chain. Best way to execute a command chain is to
     * execute it as a chain because exceptions are automatically handled. An
     * other way is to use the <code>executeOnly</code> method.
     *
     * @param parameterObject
     *            The parameter object to pass
     * @return False if there is an error or the whole task is completed. True
     *         if the next command should overtake.
     */
    boolean executeAsChain(T parameterObject);
    
    /**
     * Executes the command as a chain straight forward (no error handling).
     * 
     * @param parameterObject The parameter object to pass.
     * 
     * @since 1.3.0
     */
    default void executeOnly(T parameterObject) {
    }

}