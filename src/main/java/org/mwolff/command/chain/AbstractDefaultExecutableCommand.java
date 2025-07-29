/** Simple Command Framework.
 * 
 * Framework for easy building software that fits the SOLID principles.
 * 
 * @author Manfred Wolff <info@mwolff.org>
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

package org.mwolff.command.chain;

import org.mwolff.command.AbstractDefaultCommand;
import org.mwolff.command.interfaces.CommandTransition;
import org.mwolff.command.interfaces.ExecutableCommand;

/**
 * Provides a default implementation for a command that is part of a chain.
 * <p>
 * This abstract class simplifies the creation of executable commands within a
 * chain of responsibility. It adapts the result of the standard
 * {@link #executeCommand(Object)} method for chain-based execution.
 *
 * @param <T> The type of the parameter object.
 */
public abstract class AbstractDefaultExecutableCommand<T> extends AbstractDefaultCommand<T>
        implements ExecutableCommand<T> {

    /**
     * Executes the command and determines the next step in the command chain.
     * If the underlying {@link #executeCommand(Object)} returns {@code SUCCESS}, this method returns {@code NEXT} to continue the chain. Otherwise, it returns {@code DONE} to stop the chain's execution.
     * @see ExecutableCommand#executeCommandAsChain(java.lang.Object)
     */
    @Override
    public CommandTransition executeCommandAsChain(T parameterObject) {
        return executeCommand(parameterObject) == CommandTransition.SUCCESS
                ? CommandTransition.NEXT
                : CommandTransition.DONE;
    }
}

