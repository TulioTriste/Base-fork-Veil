package net.bfcode.bfbase.util.imagemessage;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ImageMessage {
	
    private Color[] colors;
    private String[] lines;
    
    public ImageMessage(BufferedImage image, int height, char imgChar) {
        this.colors = new Color[] { new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 255, 85), new Color(255, 255, 255) };
        ChatColor[][] chatColors = this.toChatColorArray(image, height);
        this.lines = this.toImgMessage(chatColors, imgChar);
    }
    
    public ImageMessage(ChatColor[][] chatColors, char imgChar) {
        this.colors = new Color[] { new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 255, 85), new Color(255, 255, 255) };
        this.lines = this.toImgMessage(chatColors, imgChar);
    }
    
    public ImageMessage(String... imgLines) {
        this.colors = new Color[] { new Color(0, 0, 0), new Color(0, 0, 170), new Color(0, 170, 0), new Color(0, 170, 170), new Color(170, 0, 0), new Color(170, 0, 170), new Color(255, 170, 0), new Color(170, 170, 170), new Color(85, 85, 85), new Color(85, 85, 255), new Color(85, 255, 85), new Color(85, 255, 255), new Color(255, 85, 85), new Color(255, 85, 255), new Color(255, 255, 85), new Color(255, 255, 255) };
        this.lines = imgLines;
    }
    
    public ImageMessage appendText(String... text) {
        for (int y = 0; y < this.lines.length; ++y) {
            if (text.length > y) {
                String[] arrstring = this.lines;
                int n = y;
                arrstring[n] = arrstring[n] + " " + text[y];
            }
        }
        return this;
    }
    
    public ImageMessage appendCenteredText(String... text) {
        for (int y = 0; y < this.lines.length; ++y) {
            if (text.length <= y) {
                return this;
            }
            int len = 65 - this.lines[y].length();
            this.lines[y] += this.center(text[y], len);
        }
        return this;
    }
    
    private ChatColor[][] toChatColorArray(BufferedImage image, int height) {
        double ratio = image.getHeight() / image.getWidth();
        int width = (int)(height / ratio);
        if (width > 10) {
            width = 10;
        }
        BufferedImage resized = this.resizeImage(image, (int)(height / ratio), height);
        ChatColor[][] chatImg = new ChatColor[resized.getWidth()][resized.getHeight()];
        for (int x = 0; x < resized.getWidth(); ++x) {
            for (int y = 0; y < resized.getHeight(); ++y) {
                int rgb = resized.getRGB(x, y);
                chatImg[x][y] = this.getClosestChatColor(new Color(rgb, true));
            }
        }
        return chatImg;
    }
    
    private String[] toImgMessage(ChatColor[][] colors, char imgchar) {
        String[] lines = new String[colors[0].length];
        for (int y = 0; y < colors[0].length; ++y) {
            String line = "";
            for (int x = 0; x < colors.length; ++x) {
                line += (((colors[x][y]) != null) ? (colors[x][y].toString() + imgchar) : ' ');
            }
            lines[y] = line + ChatColor.RESET;
        }
        return lines;
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        AffineTransform af = new AffineTransform();
        af.scale(width / originalImage.getWidth(), height / originalImage.getHeight());
        AffineTransformOp operation = new AffineTransformOp(af, 1);
        return operation.filter(originalImage, null);
    }
    
    private double getDistance(Color c1, Color c2) {
        double rmean = (c1.getRed() + c2.getRed()) / 2.0;
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2.0 + rmean / 256.0;
        double weightG = 4.0;
        double weightB = 2.0 + (255.0 - rmean) / 256.0;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }
    
    private boolean areIdentical(Color c1, Color c2) {
        return Math.abs(c1.getRed() - c2.getRed()) <= 5 && Math.abs(c1.getGreen() - c2.getGreen()) <= 5 && Math.abs(c1.getBlue() - c2.getBlue()) <= 5;
    }
    
    private ChatColor getClosestChatColor(Color color) {
        if (color.getAlpha() < 128) {
            return null;
        }
        int index = 0;
        double best = -1.0;
        for (int i = 0; i < this.colors.length; ++i) {
            if (this.areIdentical(this.colors[i], color)) {
                return ChatColor.values()[i];
            }
        }
        for (int i = 0; i < this.colors.length; ++i) {
            double distance = this.getDistance(color, this.colors[i]);
            if (distance < best || best == -1.0) {
                best = distance;
                index = i;
            }
        }
        return ChatColor.values()[index];
    }
    
    private String center(String s, int length) {
        if (s.length() > length) {
            return s.substring(0, length);
        }
        if (s.length() == length) {
            return s;
        }
        int leftPadding = (length - s.length()) / 2;
        StringBuilder leftBuilder = new StringBuilder();
        for (int i = 0; i < leftPadding; ++i) {
            leftBuilder.append(" ");
        }
        return leftBuilder.toString() + s;
    }
    
    public String[] getLines() {
        return this.lines;
    }
    
    public void sendToPlayer(Player player) {
        for (String line : this.lines) {
            player.sendMessage(line);
        }
    }
}
