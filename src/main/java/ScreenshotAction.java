import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.ex.EditorEx;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class ScreenshotAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        EditorEx editor = (EditorEx) event.getData(PlatformDataKeys.EDITOR);
        if (editor == null)
            return;

        BufferedImage resultImage = EditorScreenshoter.takeScreenshot(editor);

        EditorScreenshoter.saveImage(resultImage);
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }
}
