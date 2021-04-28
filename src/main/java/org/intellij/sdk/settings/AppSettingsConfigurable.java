// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.intellij.sdk.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Advanced Screenshoter";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        return !mySettingsComponent.getPath().equals(settings.path) ||
                mySettingsComponent.getScale() != settings.scale ||
                mySettingsComponent.getRightPadding() != settings.rightPadding ||
                mySettingsComponent.getWhenOutOfMemoryPossible() != settings.whenOutOfMemoryPossible;
    }

    @Override
    public void apply() {
        AppSettingsState settings = AppSettingsState.getInstance();
        settings.path = mySettingsComponent.getPath();
        settings.scale = mySettingsComponent.getScale();
        settings.rightPadding = mySettingsComponent.getRightPadding();
        settings.whenOutOfMemoryPossible = mySettingsComponent.getWhenOutOfMemoryPossible();
    }

    @Override
    public void reset() {
        mySettingsComponent.setPath(System.getProperty("user.dir"));
        mySettingsComponent.setScale(2);
        mySettingsComponent.setRightPadding(10);
        mySettingsComponent.setWhenOutOfMemoryPossible(7_216_000);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

}
