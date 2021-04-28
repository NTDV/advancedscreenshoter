// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.intellij.sdk.settings;

import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AppSettingsComponent {

    private final JPanel myMainPanel;
    private final JBTextField myPath = new JBTextField();
    private final JBTextField myScale = new JBTextField();
    private final JBTextField myRightPadding = new JBTextField();
    private final JBTextField myWhenOutOfMemoryPossible = new JBTextField();

    public AppSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent("Save path: ", myPath)
                .addLabeledComponent("Scale: ", myScale)
                .addLabeledComponent("Right padding: ", myRightPadding)
                .addLabeledComponent("When outOfMemoryPossible: ", myWhenOutOfMemoryPossible)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public String getPath() {
        return myPath.getText();
    }

    public int getScale() {
        return Integer.parseInt(myScale.getText());
    }

    public int getRightPadding() {
        return Integer.parseInt(myRightPadding.getText());
    }

    public int getWhenOutOfMemoryPossible() {
        return Integer.parseInt(myWhenOutOfMemoryPossible.getText());
    }

    public void setPath(String path) {
        myPath.setText(path);
    }

    public void setScale(int scale) {
        myScale.setText(String.valueOf(scale));
    }

    public void setRightPadding(int rightPadding) {
        myRightPadding.setText(String.valueOf(rightPadding));
    }

    public void setWhenOutOfMemoryPossible(int whenOutOfMemoryPossible) {
        myWhenOutOfMemoryPossible.setText(String.valueOf(whenOutOfMemoryPossible));
    }

    public JComponent getPreferredFocusedComponent() {
        return myPath;
    }
}
