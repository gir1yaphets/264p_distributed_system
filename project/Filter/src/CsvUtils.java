import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.siegmar.fastcsv.writer.CsvWriter;

public class CsvUtils {

    public static final String EXPORT_CSV_PATH = "./filter.csv";


    public static void print(List<List<String>> data) {
        File file = new File(EXPORT_CSV_PATH);
        CsvWriter csvWriter = new CsvWriter();

        try {
            Collection<String[]> result = new ArrayList<>();

            for (List<String> line : data) {
                String[] stringData = new String[line.size()];
                int i = 0;
                for (String item : line) {
                    stringData[i++] = item;
                }
                result.add(stringData);
            }

            csvWriter.write(file, StandardCharsets.UTF_8, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
