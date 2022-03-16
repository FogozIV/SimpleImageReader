package be.fogoziv.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomImage {
    private final BufferedImage image;
    public CustomImage(BufferedImage image){
        //copy the image
        this.image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = this.image.createGraphics();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();
    }

    public CustomImage(String filename) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(filename));
        //convert image to rgb
        this.image = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = this.image.createGraphics();
        g.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
        g.dispose();

    }

    public CustomImage(CustomImage customImage) {
        this(customImage.image);
    }

    public CustomImage resize(int width){
        float ratio = ((float)width)/((float)this.image.getWidth());
        int height = (int)Math.floor((double) ratio * this.image.getHeight());
        return this.resize(width, height);
    }

    public CustomImage resize(int width, int height){
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.drawImage(image, 0,0, width, height, null);
        g.dispose();
        return new CustomImage(resized);
    }

    public CustomImage resizePixel(int xSize, int ySize){
        BufferedImage resized = new BufferedImage(getWidth() * xSize, getHeight() * ySize, BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < getWidth(); x++){
            for(int y = 0; y < getHeight(); y++){
                for(int o_x = 0; o_x < xSize; o_x++){
                    for(int o_y = 0; o_y < ySize; o_y ++){
                        resized.setRGB(x * xSize + o_x, y * ySize + o_y, image.getRGB(x, y));
                    }
                }
            }
        }
        return new CustomImage(resized);
    }

    public List<Byte> toWeirdFormat(){
        List<Byte> bytes = new ArrayList<>();
        bytes.addAll(getBytesFromInteger(getWidth(), 4));
        bytes.addAll(getBytesFromInteger(getHeight(), 4));
        for(int x = 0; x < getWidth(); x++){
            for(int y = 0; y < getHeight(); y++){
                bytes.addAll(getBytesFromInteger(getRGBInt(x, y), 3));
            }
        }
        return bytes;
    }

    public void saveWeirdFormat(String filename) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
        List<Byte> bytes = toWeirdFormat();
        for(Byte b: bytes){
            bos.write(b);
        }
        bos.flush();
    }

    /**
     * Transform integer into bytes msb first
     * @param value
     * @param maxByteAmount max is 4 min is 0
     * @return
     */
    private List<Byte> getBytesFromInteger(int value, int maxByteAmount){
        List<Byte> bytes = new ArrayList<>();
        if(maxByteAmount > 4)
            maxByteAmount = 4;
        if(maxByteAmount < 0)
            maxByteAmount = 0;
        for(int i = maxByteAmount - 1; i >= 0; i--){
            bytes.add((byte) ((value >> (8*i)) & 0xFF));
        }
        return bytes;
    }

    public CustomImage setRGB(int x, int y, int r, int g, int b){
        this.image.setRGB(x, y, (r<<16) | (g << 8) | b);
        return this;
    }

    public CustomImage setRGB(int x, int y, int rgb){
        return this.setRGB(x, y, (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
    }

    public int getRGBInt(int x, int y){
        return image.getRGB(x, y);
    }

    public RGB getRGB(int x, int y){
        int rgb = this.getRGBInt(x, y);
        return new RGB((byte) ((rgb >> 16) & 0xFF), (byte) ((rgb >> 8) & 0xFF), (byte) (rgb & 0xFF));
    }

    public int getWidth(){
        return this.image.getWidth();
    }

    public int getHeight(){
        return this.image.getHeight();
    }

    public CustomImage save(String filename) throws IOException {
        File outputFile = new File(filename + ".png");
        ImageIO.write(this.image, "png", outputFile);
        return this;
    }

    public CustomImage clone(){
        return new CustomImage(this);
    }

    class RGB{
        private final byte red, green, blue;

        public RGB(byte red, byte green, byte blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public byte getRed() {
            return red;
        }

        public byte getGreen() {
            return green;
        }

        public byte getBlue() {
            return blue;
        }
    }

}
