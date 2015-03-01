package com.example.kelei.gridimagesearch.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kelei.gridimagesearch.R;

/**
 * Created by kelei on 2/28/15.
 */
public class EditSettingsDialog extends android.support.v4.app.DialogFragment {
    Spinner sImageSize;
    Spinner sColorFilter;
    Spinner sImageType;
    TextView etSiteFilter;

    public interface EditSettingsDialogListener {
        void onFinishEditDialog(String imageSize, String colorFilter, String imageType, String siteFilter);
    }

    public EditSettingsDialog() {
    }

    public static EditSettingsDialog newInstance(String imageSize, String colorFilter, String imageType, String siteFilter) {
        EditSettingsDialog frag = new EditSettingsDialog();
        Bundle args = new Bundle();
        args.putString("imageSize", imageSize);
        args.putString("colorFilter", colorFilter);
        args.putString("imageType", imageType);
        args.putString("siteFilter", siteFilter);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.settings_dialog, null);
        builder.setView(view);

        Bundle args = getArguments();
        sImageSize = (Spinner) view.findViewById(R.id.sImageSize);
        ArrayAdapter<CharSequence> aImageSize = ArrayAdapter.createFromResource(getActivity(),
            R.array.image_size, android.R.layout.simple_spinner_item);
        aImageSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sImageSize.setAdapter(aImageSize);
        if (args != null && args.getString("imageSize") != null) {
            int imageSizePosition = aImageSize.getPosition(args.getString("imageSize"));
            sImageSize.setSelection(imageSizePosition, true);
        }

        sColorFilter = (Spinner) view.findViewById(R.id.sColorFilter);
        ArrayAdapter<CharSequence> aColorFilter = ArrayAdapter.createFromResource(getActivity(),
            R.array.image_color, android.R.layout.simple_spinner_item);
        aColorFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sColorFilter.setAdapter(aColorFilter);
        if (args != null && args.getString("colorFilter") != null) {
            int colorFilterPosition = aColorFilter.getPosition(args.getString("colorFilter"));
            sColorFilter.setSelection(colorFilterPosition, true);
        }

        sImageType = (Spinner) view.findViewById(R.id.sImageType);
        ArrayAdapter<CharSequence> aImageType = ArrayAdapter.createFromResource(getActivity(),
            R.array.image_type, android.R.layout.simple_spinner_item);
        aImageType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sImageType.setAdapter(aImageType);
        if (args != null && args.getString("imageType") != null) {
            int imageTypePosition = aImageType.getPosition(args.getString("imageType"));
            sImageType.setSelection(imageTypePosition, true);
        }

        etSiteFilter = (EditText)view.findViewById(R.id.etSiteFilter);
        if (args != null) {
            etSiteFilter.setText(args.getString("siteFilter"));
        }

        builder.setPositiveButton(getResources().getString(R.string.save),  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String imageSize = sImageSize.getSelectedItem().toString();
                String colorFilter = sColorFilter.getSelectedItem().toString();
                String imageType = sImageType.getSelectedItem().toString();
                String siteFilter = etSiteFilter.getText().toString();

                imageSize = imageType == "any" ? null : imageSize;
                colorFilter = colorFilter == "any" ? null : colorFilter;
                imageType = imageType == "any" ? null : imageType;

                EditSettingsDialogListener listener = (EditSettingsDialogListener) getActivity();
                listener.onFinishEditDialog(imageSize, colorFilter, imageType, siteFilter);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

}
