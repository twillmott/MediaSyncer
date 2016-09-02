package uk.org.willmott.mediasyncer.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import net.smartam.leeloo.client.request.OAuthClientRequest;

public class TraktService {

    private static String USERID = "twillmott";
    private static String CLIENT_ID = "0866b3b94e7873b57c5fb201291adb4ba601ab4a403bd3546ff6f38cf0a8fb84";
    private static String CLIENT_SECRED = "767159b9d53fe00d40b8e16e5172a785806b7e909570bf0360eade075d4d0a35";
    private Context context;
    /**
     * Authenticate the trakt connection using OAuth2. This method requires a context (activity).
     * The activity must have a intent filter to filter the redirect uri mediasyncer://oauthresponse.
     * @param context
     */
    public void authenticate(Context context) {
        this.context = context;

        try {
            String surl = "https://trakt.tv/oauth/authorize?response_type=code&client_id="
                    + CLIENT_ID + "&redirect_uri=mediasyncer://oauthresponse";

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(surl));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("trakt", e.toString());
        }
    }

    public String getAuthenticationStatus(Intent intent) {
        Uri uri = intent.getData();

        if (uri != null && uri.toString()
                .startsWith("mediasyncer://oauthresponse"))
        {
            String code = uri.getQueryParameter("code");
            // ...
        }
        return null;
    }
}
