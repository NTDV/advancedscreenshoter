package org.intellij.sdk.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AdvancedScreenshoterSettingsConfigurable implements Configurable {

    private AdvancedScreenshoterSettingsComponent mySettingsComponent;

    // A default constructor with no arguments is required because thi`s implementation
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
        mySettingsComponent = new AdvancedScreenshoterSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AdvancedScreenshoterSettingsState settings = AdvancedScreenshoterSettingsState.getInstance();
        return !mySettingsComponent.getPath().equals(settings.path) ||
                mySettingsComponent.getScale() != settings.scale ||
                mySettingsComponent.getRightPadding() != settings.rightPadding ||
                mySettingsComponent.getWhenOutOfMemoryPossible() != settings.whenOutOfMemoryPossible;
    }

    @Override
    public void apply() {
        AdvancedScreenshoterSettingsState settings = AdvancedScreenshoterSettingsState.getInstance();
        settings.path = mySettingsComponent.getPath();
        settings.scale = mySettingsComponent.getScale();
        settings.rightPadding = mySettingsComponent.getRightPadding();
        settings.whenOutOfMemoryPossible = mySettingsComponent.getWhenOutOfMemoryPossible();
    }

    @Override
    public void reset() {
        AdvancedScreenshoterSettingsState settings = AdvancedScreenshoterSettingsState.getInstance();
        mySettingsComponent.setPath(settings.path);
        mySettingsComponent.setScale(settings.scale);
        mySettingsComponent.setRightPadding(settings.rightPadding);
        mySettingsComponent.setWhenOutOfMemoryPossible(settings.whenOutOfMemoryPossible);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
