package utils;

public class Normalizer {
    public static void normalizePrint(String agentClass, String agentName, String message) {
        System.out.println("__" +
                agentClass +
                "__ : " +
                agentName +
                " : " +
                message);
    }
}
