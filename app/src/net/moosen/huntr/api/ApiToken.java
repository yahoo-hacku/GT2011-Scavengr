package net.moosen.huntr.api;

/**
 * TODO: Enter class description.
 */
public class ApiToken
{
    private String token;
    public ApiToken() {}
    public String getToken()
    {
        return this.token;
    }

    public void setToken(final String token)
    {
        this.token = token;
    }

    public ApiToken withToken(final String token)
    {
        this.token= token;
        return this;
    }

    @Override
    public String toString()
    {
        return this.token;
    }
}