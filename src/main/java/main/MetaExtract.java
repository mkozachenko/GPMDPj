package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MetaExtract {

    public static void extract(String filePath){
        String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
        try {
            InputStream input = new FileInputStream(new File(filePath));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            if (fileName.matches(CommonData.supportedFormats)) {
                parser.parse(input, handler, metadata, parseCtx);
                input.close();
                // List all metadata
                //String[] metadataNames = metadata.names();

                /*for (String name : metadataNames) {
                    System.out.println(name + ": " + metadata.get(name));
                }*/
                // Retrieve the necessary info from metadata
                // Names - title, xmpDM:artist etc. - mentioned below may differ based
                /*System.out.println("----------------------------------------------");
                System.out.println("Title: " + metadata.get("title"));
                System.out.println("Artists: " + metadata.get("xmpDM:artist"));
                System.out.println("Composer : " + metadata.get("xmpDM:composer"));
                System.out.println("Genre : " + metadata.get("xmpDM:genre"));
                System.out.println("Album : " + metadata.get("xmpDM:album"));*/
                CommonData.song_name.add(metadata.get("title"));
                CommonData.song_artist.add(metadata.get("xmpDM:artist"));
                CommonData.song_album.add(metadata.get("xmpDM:album"));
                CommonData.song_year.add(metadata.get("xmpDM:releaseDate"));
                CommonData.song_number.add(metadata.get("xmpDM:trackNumber"));
                CommonData.song_genres.add(metadata.get("xmpDM:genre"));
                CommonData.song_filename.add(fileName);
                CommonData.song_path.add(filePath);
                //CommonData.data = new String[]{metadata.get("title"), metadata.get("xmpDM:artist"), metadata.get("xmpDM:album"), metadata.get("xmpDM:releaseDate"), metadata.get("xmpDM:trackNumber"), metadata.get("xmpDM:genre"), filePath, fileName};
            }
            } catch(FileNotFoundException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            } catch(SAXException e){
                e.printStackTrace();
            } catch(TikaException e){
                e.printStackTrace();
            }

}
}
