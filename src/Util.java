import org.bouncycastle.util.encoders.Hex;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Util {

    public static void printByteArray(String name, byte[] bytes) {

        System.out.println(setColour(name, Color.GREEN) + ": "+ Hex.toHexString(bytes));
        System.out.println(setColour(name, Color.GREEN) + setColour(" length: ", Color.GREEN) + bytes.length + " bytes, " + bytes.length * 8 + " bits.");
    }

    public static byte[] zip(final String str) {
        if ((str == null) || (str.length() == 0)) {
          throw new IllegalArgumentException("Cannot zip null or empty string");
        }
      
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
          try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
          }
          return byteArrayOutputStream.toByteArray();
        } catch(IOException e) {
          throw new RuntimeException("Failed to zip content", e);
        }
      }
      
      public static String unzip(final byte[] compressed) {
        if ((compressed == null) || (compressed.length == 0)) {
          throw new IllegalArgumentException("Cannot unzip null or empty bytes");
        }
        if (!isZipped(compressed)) {
          return new String(compressed);
        }
      
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressed)) {
          try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8)) {
              try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                StringBuilder output = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null){
                  output.append(line);
                }
                return output.toString();
              }
            }
          }
        } catch(IOException e) {
          throw new RuntimeException("Failed to unzip content", e);
        }
      }
      
      public static boolean isZipped(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) 
               && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
      }


      enum Color {
        //Color end string, color reset
        RESET("\033[0m"),
    
        // Regular Colors. Normal color, no bold, background color etc.
        BLACK("\033[0;30m"),    // BLACK
        RED("\033[0;31m"),      // RED
        GREEN("\033[0;32m"),    // GREEN
        YELLOW("\033[0;33m"),   // YELLOW
        BLUE("\033[0;34m"),     // BLUE
        MAGENTA("\033[0;35m"),  // MAGENTA
        CYAN("\033[0;36m"),     // CYAN
        WHITE("\033[0;37m"),    // WHITE
    
        // Bold
        BLACK_BOLD("\033[1;30m"),   // BLACK
        RED_BOLD("\033[1;31m"),     // RED
        GREEN_BOLD("\033[1;32m"),   // GREEN
        YELLOW_BOLD("\033[1;33m"),  // YELLOW
        BLUE_BOLD("\033[1;34m"),    // BLUE
        MAGENTA_BOLD("\033[1;35m"), // MAGENTA
        CYAN_BOLD("\033[1;36m"),    // CYAN
        WHITE_BOLD("\033[1;37m"),   // WHITE
    
        // Underline
        BLACK_UNDERLINED("\033[4;30m"),     // BLACK
        RED_UNDERLINED("\033[4;31m"),       // RED
        GREEN_UNDERLINED("\033[4;32m"),     // GREEN
        YELLOW_UNDERLINED("\033[4;33m"),    // YELLOW
        BLUE_UNDERLINED("\033[4;34m"),      // BLUE
        MAGENTA_UNDERLINED("\033[4;35m"),   // MAGENTA
        CYAN_UNDERLINED("\033[4;36m"),      // CYAN
        WHITE_UNDERLINED("\033[4;37m"),     // WHITE
    
        // Background
        BLACK_BACKGROUND("\033[40m"),   // BLACK
        RED_BACKGROUND("\033[41m"),     // RED
        GREEN_BACKGROUND("\033[42m"),   // GREEN
        YELLOW_BACKGROUND("\033[43m"),  // YELLOW
        BLUE_BACKGROUND("\033[44m"),    // BLUE
        MAGENTA_BACKGROUND("\033[45m"), // MAGENTA
        CYAN_BACKGROUND("\033[46m"),    // CYAN
        WHITE_BACKGROUND("\033[47m"),   // WHITE
    
        // High Intensity
        BLACK_BRIGHT("\033[0;90m"),     // BLACK
        RED_BRIGHT("\033[0;91m"),       // RED
        GREEN_BRIGHT("\033[0;92m"),     // GREEN
        YELLOW_BRIGHT("\033[0;93m"),    // YELLOW
        BLUE_BRIGHT("\033[0;94m"),      // BLUE
        MAGENTA_BRIGHT("\033[0;95m"),   // MAGENTA
        CYAN_BRIGHT("\033[0;96m"),      // CYAN
        WHITE_BRIGHT("\033[0;97m"),     // WHITE
    
        // Bold High Intensity
        BLACK_BOLD_BRIGHT("\033[1;90m"),    // BLACK
        RED_BOLD_BRIGHT("\033[1;91m"),      // RED
        GREEN_BOLD_BRIGHT("\033[1;92m"),    // GREEN
        YELLOW_BOLD_BRIGHT("\033[1;93m"),   // YELLOW
        BLUE_BOLD_BRIGHT("\033[1;94m"),     // BLUE
        MAGENTA_BOLD_BRIGHT("\033[1;95m"),  // MAGENTA
        CYAN_BOLD_BRIGHT("\033[1;96m"),     // CYAN
        WHITE_BOLD_BRIGHT("\033[1;97m"),    // WHITE
    
        // High Intensity backgrounds
        BLACK_BACKGROUND_BRIGHT("\033[0;100m"),     // BLACK
        RED_BACKGROUND_BRIGHT("\033[0;101m"),       // RED
        GREEN_BACKGROUND_BRIGHT("\033[0;102m"),     // GREEN
        YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),    // YELLOW
        BLUE_BACKGROUND_BRIGHT("\033[0;104m"),      // BLUE
        MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"),   // MAGENTA
        CYAN_BACKGROUND_BRIGHT("\033[0;106m"),      // CYAN
        WHITE_BACKGROUND_BRIGHT("\033[0;107m");     // WHITE
    
        private final String code;
    
        Color(String code) {
            this.code = code;
        }
    
        @Override
        public String toString() {
            return code;
        }
    }

    public static String setColour (String text, Color colour) {
        return colour + text + Color.RESET;
    }

    public static void printlnc(String text, Color colour)
    {
        System.out.print(colour);
        System.out.println(text);
        System.out.print(Color.RESET + "\r");
    }

    public static void printc(String text, Color colour)
    {
        System.out.print(colour);
        System.out.print(text);
        System.out.print(Color.RESET);
    }
    
}