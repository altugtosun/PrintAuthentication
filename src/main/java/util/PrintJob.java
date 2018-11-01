package util;

public class PrintJob {

    String filename;
    String printer;

    public PrintJob(String filename, String printer) {
        this.filename = filename;
        this.printer = printer;
    }

    public String toString() {
        return this.filename;
    }
}
