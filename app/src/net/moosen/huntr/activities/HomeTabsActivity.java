package net.moosen.huntr.activities;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * TODO: Enter class description.
 */
public class HomeTabsActivity extends TabActivity
{
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        TabHost host = getTabHost();

        /*host.addTab(host.newTabSpec("one")
                .setIndicator("CW")
                .setContent(new Intent(this, CWBrowser.class)));

        host.addTab(host.newTabSpec("two")
                .setIndicator("Android")
                .setContent(new Intent(this, AndroidBrowser.class)));*/
    }
}
