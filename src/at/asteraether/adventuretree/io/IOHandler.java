package at.asteraether.adventuretree.io;

import at.asteraether.adventuretree.adventure.TextSpeed;
import at.asteraether.adventuretree.adventure.state.State;
import at.asteraether.adventuretree.exceptions.OutputException;

import java.io.*;

public class IOHandler implements Closeable {

    private final BufferedWriter out;
    private final BufferedReader in;

    private TextSpeed textSpeed;

    public IOHandler(OutputStream outputStream, InputStream inputStream, TextSpeed textSpeed) {
        out = new BufferedWriter(new OutputStreamWriter(outputStream));
        in = new BufferedReader(new InputStreamReader(inputStream));
        this.textSpeed = textSpeed;
    }

    public IOHandler(State start, TextSpeed textSpeed) {
        this(System.out, System.in, textSpeed);
    }

    public IOHandler(State start) {
        this(start, TextSpeed.NORMAL);
    }

    public void setTextSpeed(TextSpeed textSpeed) {
        this.textSpeed = textSpeed;
    }

    public TextSpeed getTextSpeed() {
        return textSpeed;
    }

    public <T> T showPrompt(String header, String prompt, T[] options) {
        T chosen = null;
        do {
            writeNL(prompt);
            write("Choose: ");
            try {
                int choiceIndex = Integer.parseInt(in.readLine());
                chosen = options[choiceIndex - 1];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException | IOException ex) {
                clearConsole();
                writeInstantNL(header);
                newLine();
            }
        } while (chosen == null);
        return chosen;
    }

    public void newLine() throws OutputException {
        try {
            out.newLine();
            out.flush();
        } catch (IOException e) {
            throw new OutputException();
        }
    }

    public void waitForEnter() {
        writeInstant("Press enter to continue...");
        try {
            in.read();
        } catch (IOException ignored) {
        }
    }

    public void writeInstant(String text) throws OutputException {
        try {
            out.write(text);
            out.flush();
        } catch (IOException e) {
            throw new OutputException();
        }
    }

    public void writeInstantNL(String text) throws OutputException {
        writeInstant(text + '\n');
    }

    public void writeNL(String text) throws OutputException {
        write(text + '\n');
    }

    public void write(String text) throws OutputException {
        try {
            for (char c :
                    text.toCharArray()) {
                out.write(c);
                out.flush();
                Thread.sleep(textSpeed.getTextTimeout());
            }

        } catch (IOException | InterruptedException ex) {
            throw new OutputException();
        }
    }

    public void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception ignored) {
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
    }
}
