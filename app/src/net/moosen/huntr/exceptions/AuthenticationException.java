package net.moosen.huntr.exceptions;

import android.util.Log;

/**
 * TODO: Enter class description.
 */
public class AuthenticationException extends Exception {
    private String error;
    public AuthenticationException()
    {
        super();
        this.error = "An error occurred during client authentication.";
    }

    public AuthenticationException(final String error)
    {
        super(error);
        Log.d(getClass().getCanonicalName(), "AUTHENTICATION EXCEPTION BEING THROWN.");
        this.error = error;
    }

    @Override
    public String getMessage()
    {
        return this.error;
    }
}
