package boldtrn.reader;

import boldtrn.writer.NodeWriter;
import boldtrn.writer.WayWriter;
import com.graphhopper.reader.*;
import com.graphhopper.util.Helper;
import gnu.trove.list.TLongList;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OsmReader {

    private final File osmFile;
    private final File toFolder;
    private final int workerThreads = 2;

    public OsmReader(File osmFile, File toFolder) {
        this.osmFile = osmFile;
        this.toFolder = toFolder;
    }

    public void readGraph() throws Exception {

        long counter = 1;
        OSMInputFile in = null;
        ICsvListWriter nodeWriter = null;
        ICsvListWriter edgeWriter = null;
        try {
            in = new OSMInputFile(osmFile).setWorkerThreads(workerThreads).open();
            nodeWriter = new CsvListWriter(new FileWriter(toFolder.getAbsolutePath() + "/nodes.csv"),
                    CsvPreference.STANDARD_PREFERENCE);
            edgeWriter = new CsvListWriter(new FileWriter(toFolder.getAbsolutePath() + "/edges.csv"),
                    CsvPreference.STANDARD_PREFERENCE);

            nodeWriter.writeHeader(NodeWriter.getHeader());
            edgeWriter.writeHeader(WayWriter.getHeader());

            OSMElement item;
            while ((item = in.getNext()) != null) {
                switch (item.getType()) {
                    case OSMElement.NODE:
                        writeNode((OSMNode) item, nodeWriter);
                        break;

                    case OSMElement.WAY:
                        writeWay((OSMWay) item, edgeWriter);
                        break;

                    case OSMElement.RELATION:
                        break;
                }
                if (++counter % 1000000 == 0) {
                    System.out.println(counter + " " + Helper.getMemInfo());
                }
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            throw ex;
        } finally {
            Helper.close(in);
            if (nodeWriter != null) {
                nodeWriter.close();
            }
            if (edgeWriter != null) {
                edgeWriter.close();
            }
        }
    }

    public void writeNode(OSMNode node, ICsvListWriter nodeWriter) throws IOException {
        nodeWriter.write(node.getId(), node.getLat(), node.getLon());

    }

    public void writeWay(OSMWay way, ICsvListWriter wayWriter) throws IOException {
        if(!OsmAnalyzer.isValid(way) || !OsmAnalyzer.wayIsCarAllowed(way)){
            return;
        }

        final boolean isOneWay = OsmAnalyzer.wayIsOneway(way);
        final short speed = OsmAnalyzer.getSpeed(way);
        final TLongList wayNodes = way.getNodes();
        final int size = wayNodes.size();
        long formerKey = Long.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            long key = wayNodes.get(i);

            if (formerKey > Long.MIN_VALUE) {
                // Get the Nodes and Stuff
                writeWay(formerKey, key, speed, wayWriter);
                if (!isOneWay) {
                    writeWay(key, formerKey, speed, wayWriter);
                }
            }

            formerKey = key;
        }

    }

    public void writeWay(long from, long to, short speed, ICsvListWriter wayWriter) throws IOException {
        wayWriter.write(from, to, "CONNECTS",speed);
    }

}
