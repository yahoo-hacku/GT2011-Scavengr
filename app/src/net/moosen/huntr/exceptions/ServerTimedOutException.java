package net.moosen.huntr.exceptions;

import static net.moosen.huntr.utils.Messages.Debug;

/**
 * TODO: Enter class description.
 */
public class ServerTimedOutException extends Exception implements HuntrException
{
    private String error;
    public ServerTimedOutException()
    {
        super();
        this.error = "An error occurred during client authentication.";
    }

    public ServerTimedOutException(final String error)
    {
        super(error);
        Debug("SERVERTIMEDOUT EXCEPTION BEING THROWN.");
        this.error = error;
    }

    @Override
    public EXCEPTION_TYPE getType() {
        return EXCEPTION_TYPE.AUTH_EXCEPTION;
    }

    @Override
    public String getMessage()
    {
        return this.error;
    }
}
