package us.philipli.gradebook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.coreorb.selectiondialogs.data.SelectableColor;
import pl.coreorb.selectiondialogs.dialogs.ColorSelectDialog;
import pl.coreorb.selectiondialogs.utils.ColorPalettes;
import pl.coreorb.selectiondialogs.views.SelectedItemView;

/**
 * Fragment for sample app. This one is more interesting :)
 */
public class ColorDialogFragment extends Fragment implements
        ColorSelectDialog.OnColorSelectedListener {

    private static final String TAG_SELECT_COLOR_DIALOG = "TAG_SELECT_COLOR_DIALOG";

    private SelectedItemView colorSIV;

    public ColorDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_color_dialog, container, false);

        colorSIV = (SelectedItemView) rootView.findViewById(R.id.color_siv);
        colorSIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorSelectDialog();
            }
        });

        SelectableColor color = colorSIV.getSelectedColor();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView hint = (TextView) getView().findViewById(R.id.selectiondialogs_hint_tv);
        if (hint != null) {
            ((ViewGroup) hint.getParent()).removeView(hint);
        }

        TextView item = (TextView) getView().findViewById(R.id.selectiondialogs_name_tv);
        if (item != null) {
            item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }

        ColorSelectDialog colorDialog = (ColorSelectDialog) getFragmentManager().findFragmentByTag(TAG_SELECT_COLOR_DIALOG);
        if (colorDialog != null) {
            colorDialog.setOnColorSelectedListener(this);
        }
    }

    /**
     * Displays selected color in {@link SelectedItemView} view.
     * @param selectedItem selected {@link SelectableColor} object containing: id, name and color value.
     */
    @Override
    public void onColorSelected(SelectableColor selectedItem) {
        colorSIV.setSelectedColor(selectedItem);
    }

    /**
     * Shows color selection dialog with default Material Design icons.
     */
    private void showColorSelectDialog() {

        new ColorSelectDialog.Builder(getContext())
                .setColors(ColorPalettes.loadMaterialDesignColors500(getContext(), false))
                .setTitle(R.string.selectiondialogs_color_dialog_title)
                .setSortColorsByName(true)
                .setOnColorSelectedListener(this)
                .build().show(getFragmentManager(), TAG_SELECT_COLOR_DIALOG);

    }

}