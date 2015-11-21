package boldtrn.writer;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Created by robin on 21/11/15.
 */
public class WayWriter {

    public static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(), // from
                new NotNull(), // to
                new NotNull(), // speed
        };

        return processors;
    }

    public static String[] getHeader() {
        return new String[] { ":START_ID", ":END_ID", ":TYPE", "speed:short"};
    }

}
