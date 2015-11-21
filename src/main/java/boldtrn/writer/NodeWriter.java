package boldtrn.writer;

import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.util.List;

/**
 * Created by robin on 21/11/15.
 */
public class NodeWriter {

    public static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(), // OsmId
                new NotNull(), // lat
                new NotNull() // lon
        };

        return processors;
    }

    public static String[] getHeader() {
        return new String[] { "osmId:ID", "lat:double", "lon:double" };
    }

}
