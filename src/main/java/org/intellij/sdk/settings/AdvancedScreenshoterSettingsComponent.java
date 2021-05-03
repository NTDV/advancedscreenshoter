package org.intellij.sdk.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.ui.TextFieldWithHistoryWithBrowseButton;
import com.intellij.ui.components.fields.IntegerField;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.SwingHelper;

import javax.swing.*;

public class AdvancedScreenshoterSettingsComponent {

    private final JPanel myMainPanel;
    private final TextFieldWithHistoryWithBrowseButton myPath;
    private final IntegerField myScale = new IntegerField();
    private final IntegerField myRightPadding = new IntegerField();
    private final IntegerField myWhenOutOfMemoryPossible = new IntegerField();

    public AdvancedScreenshoterSettingsComponent() {
        FileChooserDescriptor singleFolderPicker = new FileChooserDescriptor(false, true, false, false, false, false);
        myPath = SwingHelper.createTextFieldWithHistoryWithBrowseButton(null, "Save to Directory...", singleFolderPicker, ContainerUtil::emptyList);

        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent("Save path: ", myPath)
                .addLabeledComponent("Scale: ", myScale)
                .addLabeledComponent("Right padding: ", myRightPadding)
                .addLabeledComponent("Bytes before outOfMemoryPossible: ", myWhenOutOfMemoryPossible)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public String getPath() {
        return myPath.getText();
    }

    public int getScale() {
        return myScale.getValue();
    }

    public int getRightPadding() {
        return myRightPadding.getValue();
    }

    public int getWhenOutOfMemoryPossible() {
        return myWhenOutOfMemoryPossible.getValue();
    }

    public void setPath(String path) {
        myPath.setText(path);
    }

    public void setScale(int scale) {
        myScale.setValue(scale);
    }

    public void setRightPadding(int rightPadding) {
        myRightPadding.setValue(rightPadding);
    }

    public void setWhenOutOfMemoryPossible(int whenOutOfMemoryPossible) {
        myWhenOutOfMemoryPossible.setValue(whenOutOfMemoryPossible);
    }
}
