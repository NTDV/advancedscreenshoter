package com.pulispace.mc.ui.panorama.util;

/*
 * This class is released under Creative Commons CC0.
 * @author Zsolt Pocze, Dimitry Polivaev, Danila Valkovets
 */

import com.intellij.util.ui.ImageUtil;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Hashtable;

public class BigBufferedImage extends BufferedImage {

    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    public static final int MAX_PIXELS_IN_MEMORY = 1024 * 1024;

    public static BufferedImage create(int width, int height, int imageType) {
        if (width * height > MAX_PIXELS_IN_MEMORY) {
            try {
                final File tempDir = new File(TMP_DIR);
                return createBigBufferedImage(tempDir, width, height, imageType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return ImageUtil.createImage(width, height, imageType);
        }
    }

    private static BufferedImage createBigBufferedImage(File tempDir, int width, int height, int imageType)
            throws IOException {
        if (imageType != TYPE_INT_RGB)
            throw new IllegalArgumentException("Unsupported image type: " + imageType);

        FileDataBuffer buffer =
                new FileDataBuffer(tempDir, width * height, 4);
        ColorModel colorModel =
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        new int[]{8, 8, 8, 0},
                        false,
                        false,
                        ComponentColorModel.TRANSLUCENT,
                        DataBuffer.TYPE_BYTE
                );
        BandedSampleModel sampleModel =
                new BandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 3);
        SimpleRaster raster =
                new SimpleRaster(sampleModel, buffer, new Point(0, 0));

        return new BigBufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
    }

    private BigBufferedImage(ColorModel cm, SimpleRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
        super(cm, raster, isRasterPremultiplied, properties);
    }

    public void dispose() {
        ((SimpleRaster) getRaster()).dispose();
    }

    public static void dispose(RenderedImage image) {
        if (image instanceof BigBufferedImage) {
            ((BigBufferedImage) image).dispose();
        }
    }

    private static class SimpleRaster extends WritableRaster {

        public SimpleRaster(SampleModel sampleModel, FileDataBuffer dataBuffer, Point origin) {
            super(sampleModel, dataBuffer, origin);
        }

        public void dispose() {
            ((FileDataBuffer) getDataBuffer()).dispose();
        }

    }

    private static class FileDataBuffer extends DataBuffer {
        private final String id = "buffer-" + System.currentTimeMillis() + "-" + ((int) (Math.random() * 1000));
        private File dir;
        private String path;
        private RandomAccessFile[] accessFiles;
        private MappedByteBuffer[] buffer;

        public FileDataBuffer(File dir, int size, int numBanks) throws IOException {
            super(TYPE_BYTE, size, numBanks);
            this.dir = dir;
            init();
        }

        private void init() throws IOException {
            if (dir == null) {
                dir = new File(".");
            }
            if (!dir.exists()) {
                throw new RuntimeException("FileDataBuffer constructor parameter dir does not exist: " + dir);
            }
            if (!dir.isDirectory()) {
                throw new RuntimeException("FileDataBuffer constructor parameter dir is not a directory: " + dir);
            }
            path = dir.getPath() + "/" + id;
            File subDir = new File(path);
            subDir.mkdir();
            subDir.deleteOnExit();
            buffer = new MappedByteBuffer[banks];
            accessFiles = new RandomAccessFile[banks];
            for (int i = 0; i < banks; i++) {
                File file = new File(path + "/bank" + i + ".dat");
                file.deleteOnExit();
                final RandomAccessFile randomAccessFile = accessFiles[i] = new RandomAccessFile(file, "rw");
                buffer[i] = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, getSize());
            }
        }

        @Override
        public int getElem(int bank, int i) {
            return buffer[bank].get(i) & 0xff;
        }

        @Override
        public void setElem(int bank, int i, int val) {
            buffer[bank].put(i, (byte) val);
        }

        @Override
        protected void finalize() {
            dispose();
        }

        public void dispose() {
            new Thread(this::disposeNow).start();
        }

        private void disposeNow() {
            if (accessFiles != null) {
                for (RandomAccessFile file : accessFiles) {
                    try {
                        file.close();
                    }
                    catch (IOException ignored) { }
                }

                accessFiles = null;
            }

            buffer = null;
            path = null;
        }
    }
}