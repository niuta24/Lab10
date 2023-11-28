package ua.edu.ucu.apps;

public class TimedDocument extends SmartDocument {
    public TimedDocument(String gcsPath) {
        super(gcsPath);
    }

    @Override
    public String parse() {
        long startTime = System.currentTimeMillis();
        String result = super.parse();
        long endTime = System.currentTimeMillis();
        System.out.
        println("Parsing took " + (endTime - startTime) + " milliseconds.");
        return result;
    }
}
