package org.intellij.sdk.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "org.intellij.sdk.settings.AdvancedScreenshoterSettings",
        storages = {@Storage("SdkSettingsPlugin.xml")}
)

public class AdvancedScreenshoterSettingsState implements PersistentStateComponent<AdvancedScreenshoterSettingsState> {

    public String path = System.getProperty("user.dir");
    public int scale = 2;
    public int rightPadding = 10;
    public int whenOutOfMemoryPossible = 1024 * 1024;

    public static AdvancedScreenshoterSettingsState getInstance() {
        return ServiceManager.getService(AdvancedScreenshoterSettingsState.class);
    }

    @Nullable
    @Override
    public AdvancedScreenshoterSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AdvancedScreenshoterSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
