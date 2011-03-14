package net.moosen.huntr.exceptions;

import static net.moosen.huntr.utils.Messages.Error;

/**
 * TODO: Enter class description.
 */
public class StaleApiTokenException extends Exception
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
    public String getMessage()
    {
        return this.error;
    }
}
