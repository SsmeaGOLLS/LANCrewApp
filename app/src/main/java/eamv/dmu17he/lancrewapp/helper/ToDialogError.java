package eamv.dmu17he.lancrewapp.helper;

import android.app.Activity;
import android.app.AlertDialog;

/**
 * Created by smeag on 14-01-2018.
 */

public class ToDialogError {

    private static ToDialogError mToDialogError;

    private ToDialogError(){}

    public static void initToDialogError(){
        if (mToDialogError == null) {
        mToDialogError = new ToDialogError();
        } else {
            throw new IllegalStateException("ToDialogError is already initialized");
        }
    }

    public static ToDialogError getInstance() {
        if (mToDialogError == null) {
            throw new IllegalStateException("ToDialogError is not initialized");
        }
        return mToDialogError;
    }

    /**
     * Creates a dialog and shows it
     *
     * @param exception
     *            The exception to show in the dialog
     * @param title
     *            The dialog title
     */
    public void createAndShowDialogFromTask(final Exception exception, String title, Activity activity) {
        final Activity mActivity = activity;
        final String mTitle = title;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, mTitle, mActivity);
            }
        });
    }

    /**
     * Creates a dialog and shows it
     *
     * @param exception
     *            The exception to show in the dialog
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(Exception exception, String title, Activity activity) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title, activity);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message
     *            The dialog message
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(final String message, final String title, Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }
}
