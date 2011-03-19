package net.moosen.huntr.exceptions;

import static net.moosen.huntr.utils.Messages.Error;

/**
 * TODO: Enter class description.
 */
public class StaleApiTokenException extends Exception implements HuntrException
{
    private String error;
    public StaleApiTokenException()
    {
        super();
        this.error = "An error occurred during client authentication.";
    }

    public StaleApiTokenException(final String error)
    {
        super(error);
        Error("AUTHENTICATION EXCEPTION BEING THROWN.");
        this.error = error;
    }

    @Override
    public EXCEPTION_TYPE getType() {
        return EXCEPTION_TYPE.STALE_API_TOKEN;
    }

    @Override
    public String getMessage()
    {
        return this.error;
    }
}
