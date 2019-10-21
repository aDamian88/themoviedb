package gr.advantage.adam.themoviedb.helpers;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHandler {

    public void createAlertDialog(Context context, String title, String message, String positiveStr, String negativeStr, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(positiveStr, onClickListener);
        dialog.setNegativeButton(negativeStr, null);
        dialog.show();
    }
}
