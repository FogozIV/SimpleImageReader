package be.fogoziv;

import be.fogoziv.utils.CustomImage;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CustomImage customImage = new CustomImage("test.jpg").resize(100);
        customImage.saveWeirdFormat("test.bin");
        customImage.save("testresized");
        CustomImage customImage1 = CustomImage.fromRawData("test.bin");
        customImage1.save("loaded");
    }
}
