package net.bfcode.bfbase.util.imagemessage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class AnimatedMessage
{
    private ImageMessage[] images;
    private int index;
    
    public AnimatedMessage(final ImageMessage... images) {
        this.index = 0;
        this.images = images;
    }
    
    public AnimatedMessage(final File gifFile, final int height, final char imgChar) {
        this.index = 0;
        final List<BufferedImage> frames = this.getFrames(gifFile);
        this.images = new ImageMessage[frames.size()];
        for (int i = 0; i < frames.size(); ++i) {
            this.images[i] = new ImageMessage(frames.get(i), height, imgChar);
        }
    }
    
    public List<BufferedImage> getFrames(final File input) {
        final ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        try {
            final ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
            final ImageInputStream in = ImageIO.createImageInputStream(input);
            reader.setInput(in);
            for (int count = reader.getNumImages(true), i = 0; i < count; ++i) {
                final BufferedImage image = reader.read(i);
                images.add(image);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return images;
    }
    
    public ImageMessage current() {
        return this.images[this.index];
    }
    
    public ImageMessage next() {
        ++this.index;
        if (this.index >= this.images.length) {
            this.index = 0;
            return this.images[this.index];
        }
        return this.images[this.index];
    }
    
    public ImageMessage previous() {
        --this.index;
        if (this.index <= 0) {
            this.index = this.images.length - 1;
            return this.images[this.index];
        }
        return this.images[this.index];
    }
    
    public ImageMessage getIndex(final int index) {
        return this.images[index];
    }
}
