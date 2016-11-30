package com.miarodriguezfo.tuterapia;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by Miguel Rodriguez on 23/10/2016.
 */

public class Utilidades {
    public static boolean CheckPlayServices(Activity context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context, 9000).show();
            }
            else
            {
                Toast.makeText(context,"Dispositivo no soportado", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }


    public static String ObtenerRegistrationTokenEnGcm(Context context) throws  Exception
    {
        InstanceID instanceID = InstanceID.getInstance(context);
        String token = instanceID.getToken(context.getString(R.string.senderid),
                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

        return token;
    }


}
