# osm2csv

This is a simple java application that converts .osm.pbf files to .csv files. The purpose of the .csv files is to be imported into Neo4J to create a database that enables routing.

## Usage

Create jar with:
`gradle jar`

Use the jar with:
`java -jar build/libs/osm2csv.jar -from <something.osm.pbf> -to <folder that shall contain the csv files>`

## Note

Right now this tool is quite specific. It filters all non driving related ways and creates edges that connect each node.
Extending or changing the logic should be straightforward.