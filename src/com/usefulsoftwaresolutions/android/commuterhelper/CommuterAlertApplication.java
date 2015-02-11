package com.usefulsoftwaresolutions.android.commuterhelper;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.ReportingInteractionMode;

import android.app.Application;

@ReportsCrashes(
		formKey="dGVacG0ydVHnaNHjRjVTUTEtb3FPWGc6MQ",
        mode = ReportingInteractionMode.DIALOG,
        resToastText = R.string.crash_toast_text, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
        resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
        resDialogOkToast = R.string.crash_dialog_ok_toast2,  // optional. displays a Toast message when the user accepts to send a report.
        mailTo = "usefulsoftwaresolutions@gmail.com"	
)
public class CommuterAlertApplication extends Application {
	DbAdapter mDbAdapter;
    @Override
    public void onCreate() {
        super.onCreate();
		///Fabric.with(this, new Crashlytics());

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        mDbAdapter=new DbAdapter(this);
    }

	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		try {
			mDbAdapter.close();
		} catch (Exception ee) {}
		super.onTerminate();
	}
    public DbAdapter getDbAdapter() {
    	return mDbAdapter;
    }
}
