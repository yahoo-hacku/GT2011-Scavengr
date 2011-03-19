package net.moosen.huntr.exceptions;

/**
 * TODO: Enter class description.
 */
public interface HuntrException
{
    public static enum EXCEPTION_TYPE
    {
        AUTH_EXCEPTION,
        STALE_API_TOKEN
    }

    public EXCEPTION_TYPE getType();
}
