import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class LoggerOutput implements Observer {
    private static final String PATH = "logger.txt";
    private static List<String> cache = new ArrayList<>();

    public LoggerOutput() {
        EventBus.subscribeTo(EventBus.EV_SHOW, this);
    }

    @Override
    public void update(Observable event, Object param) {
        cache.add((String) param);
    }

    public static void writeFile() {
        FileWriter writer;
        try {
            writer = new FileWriter(PATH, true);
            for (String s : cache) {
                writer.write(s + "\n"); //write output content to a file writer
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
