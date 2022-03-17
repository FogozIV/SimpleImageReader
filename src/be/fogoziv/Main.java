package be.fogoziv;

import be.fogoziv.utils.CustomImage;

import java.io.IOException;
import java.util.*;

public class Main {
    /*public static void main(String[] args) throws IOException {
        CustomImage customImage = new CustomImage("test.jpg").resize(100);
        customImage.saveWeirdFormat("test.bin");
        customImage.save("testresized");
        CustomImage customImage1 = CustomImage.fromRawData("test.bin");
        customImage1.save("loaded");
    }*/
    private static class Option {
        String flag, opt;
        public Option(String flag, String opt) { this.flag = flag; this.opt = opt; }

        @Override
        public String toString() {
            return "Option{" +
                    "flag='" + flag + '\'' +
                    ", opt='" + opt + '\'' +
                    '}';
        }
    }

    static public void main(String[] args) {
        List<String> argsList = new ArrayList<String>();
        Map<String, String> options = new HashMap<>();
        //List<Option> optsList = new ArrayList<Option>();
        List<String> doubleOptsList = new ArrayList<String>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                    if (args[i].length() < 2)
                        throw new IllegalArgumentException("Not a valid argument: "+args[i]);
                    if (args[i].charAt(1) == '-') {
                        if (args[i].length() < 3)
                            throw new IllegalArgumentException("Not a valid argument: "+args[i]);
                        // --opt
                        doubleOptsList.add(args[i].substring(2).toLowerCase());
                    } else {
                        if (args.length-1 == i)
                            throw new IllegalArgumentException("Expected arg after: "+args[i]);
                        // -opt
                        //optsList.add(new Option(args[i].substring(1), args[i+1]));
                        options.put(args[i].substring(1).toLowerCase(), args[i+1]);
                        i++;
                    }
                    break;
                default:
                    // arg
                    argsList.add(args[i]);
                    break;
            }
        }
        String[] requiredParameters = {"file", "output"};

        for(String str: requiredParameters){
            if(!options.containsKey(str)){
                throw new IllegalArgumentException("Missing parameter : " + str);
            }
        }

        int width = -1;
        int height = -1;

        if(options.containsKey("width")){
            width = Integer.parseInt(options.get("width"));
        }
        if(options.containsKey("height")){
            height = Integer.parseInt(options.get("height"));
        }
        CustomImage customImage;
        try {
             customImage = new CustomImage(options.get("file"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Wrong filename or unable to access file");
        }

        if(width != -1 || height != -1){
            if(width == -1){
                customImage = customImage.resize(height, false);
            }else if(height == -1){
                customImage = customImage.resize(width);
            }else{
                customImage = customImage.resize(width, height);
            }
        }

        if(!doubleOptsList.contains("nosaveresized")){
            try {
                customImage.save(options.get("output"));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error while saving resized image");
            }
        }
        if(!doubleOptsList.contains("nosavebin")){
            try {
                customImage.saveWeirdFormat(options.get("output") + ".bin");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error while saving binary");
            }
        }

        // etc
    }
}
