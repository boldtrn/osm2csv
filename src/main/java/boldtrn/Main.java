package boldtrn;

import boldtrn.reader.OsmReader;

import java.io.File;
import java.io.IOException;

/**
 * Created by robin on 21/11/15.
 */
public class Main {

    public static void main(String[] args){

        if(args.length != 4){
            System.err.println("You have to pass exactly 4 Arguments: -from <osm file to read from> -to <folder to write the csv files to>");
            System.exit(0);
        }

        String osmFileLocation = args[1];
        String toFolderLocation = args[3];

        File osmFile = new File(osmFileLocation);
        File toFolder = new File(toFolderLocation);

        System.out.println("Reading the osmFile: "+osmFile+" and write it to the folder: "+toFolder);

        if(!osmFile.exists()){
            System.err.println("The osm file cannot be found!");
            System.exit(0);
        }

        if(!toFolder.exists()){
            System.err.println("The destination folder cannot be found! Please create it.");
            System.exit(0);
        }

        // Starting the whole thing!
        OsmReader reader = new OsmReader(osmFile, toFolder);
        try {
            reader.readGraph();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
