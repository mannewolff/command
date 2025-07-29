package org.mwolff.command.interfaces;

/** ExecutableCommand interface for the command framework.
 *
 * This interface combines the functionalities of Command and ChainCommand.
 *
 * @author Manfred Wolff */
public interface ExecutableCommand<T extends Object> extends Command<T> {

    /** @since 1.5.0
     * @param parameterObject The parameter object to pass.
     * @return SUCCESS is the next chain should overtake, ABORT otherwise. */
    CommandTransition executeCommandAsChain(T parameterObject);
}
