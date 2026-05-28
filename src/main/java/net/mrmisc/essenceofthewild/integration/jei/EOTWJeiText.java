package net.mrmisc.essenceofthewild.integration.jei;

public class EOTWJeiText {
    public static String formatTicks(int ticks) {
        int seconds = Math.max(0, ticks + 19) / 20;
        int minutes = seconds / 60;
        int remainder = seconds % 60;
        return minutes + ":" + (remainder < 10 ? "0" : "") + remainder;
    }
}
