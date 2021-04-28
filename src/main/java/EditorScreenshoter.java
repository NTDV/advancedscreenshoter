import com.intellij.openapi.editor.ex.DocumentEx;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.EditorGutterComponentEx;
import com.intellij.openapi.ui.Messages;
import com.pulispace.mc.ui.panorama.util.BigBufferedImage;
import org.intellij.sdk.settings.AppSettingsState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditorScreenshoter {
    private static final String namePattern = "\\Editor_%s.png";

    public static BufferedImage takeScreenshot(EditorEx editor) {
        JComponent contentComponent = editor.getContentComponent();
        EditorGutterComponentEx gutterComponent = editor.getGutterComponentEx();
        DocumentEx document = editor.getDocument();


        int gutterWidthScaled = gutterComponent.getWidth() * getScale();
        int contentWidthScaled = (editor.getMaxWidthInRange(0, document.getTextLength()) + getRightPadding())
                * getScale();
        int imageWidthScaled = gutterWidthScaled + contentWidthScaled;

        int imageHeightScaled = Math.min(editor.getLineHeight() * document.getLineCount(), gutterComponent.getHeight())
                * getScale();

        if (imageWidthScaled * imageHeightScaled > getWhenOutOfMemoryPossible() &&
            Messages.showOkCancelDialog(
                    "Image creation may call OutOfMemoryException because of big resolution.",
                    "Too Big Image",
                    "Continue",
                    "Stop",
                    null)
                    != Messages.OK)
            return null;


        BufferedImage resultImage = BigBufferedImage.create(imageWidthScaled, imageHeightScaled, BigBufferedImage.TYPE_INT_RGB);

        Graphics2D resultImageGraphics = resultImage.createGraphics();
        resultImageGraphics.scale(getScale(), getScale());
        //contentComponent.print(resultImageGraphics);
        gutterComponent.print(resultImageGraphics);

        BufferedImage contentImage = BigBufferedImage.create(contentWidthScaled, imageHeightScaled, BigBufferedImage.TYPE_INT_RGB);

        Graphics2D contentImageGraphics = contentImage.createGraphics();
        contentImageGraphics.scale(getScale(), getScale());
        contentComponent.print(contentImageGraphics);
        contentImageGraphics.dispose();

        resultImageGraphics.dispose();

        resultImageGraphics = resultImage.createGraphics();
        resultImageGraphics.drawImage(contentImage, gutterWidthScaled, 0, null);

        resultImageGraphics.dispose();


        return resultImage;
    }

    public static void saveImage(RenderedImage image) {
        if (image == null)
            return;

        try {
            ImageIO.write(image, "png", new File(getPath()));
        } catch (IOException e) {
            Messages.showErrorDialog("Can't save image.", "Screenshot Exception");
        }
    }

    private static String getPath() {
        AppSettingsState settings = AppSettingsState.getInstance();
        return settings.path +
                String.format(namePattern,
                        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                                .format(LocalDateTime.now())
                );
    }

    private static int getScale() {
        AppSettingsState settings = AppSettingsState.getInstance();
        return settings.scale;
    }

    private static int getRightPadding() {
        AppSettingsState settings = AppSettingsState.getInstance();
        return settings.rightPadding;
    }

    private static int getWhenOutOfMemoryPossible() {
        AppSettingsState settings = AppSettingsState.getInstance();
        return settings.whenOutOfMemoryPossible;
    }
}
