package id.rootca.sivion.dsigner;

import android.util.Base64;

/**
 * Created by dianw on 8/26/15.
 */
public interface DroidSignerConstants {
    public static final String APP_ID = "419c6697-14b7-4853-880e-b68e3731e316";
    public static final String APP_SECRET = "s3cr3t";
    public static final String APP_AUTHORIZATION = new String(Base64.encode((APP_ID + ":" + APP_SECRET).getBytes(), Base64.DEFAULT));
    public static final String DATABASE_NAME = "dsigner.db";
}
