package sym.com;

import bo.core.system.FilesUtil;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String base = "C:\\Users\\bolinger\\Documents\\SW Projects\\322326 - Prefab Blob\\";
    private static final String skeleton = "base blob - L1\\blob.basin.skeleton.txt";
    private static final String basinCoverMatePath = "blob - L2\\blob.cover_mates.txt";
    private static final String basinConfigPath = "base blob - L1\\blob.basinConfig.txt";
    private static final HashMap<String, String> basinConfigTable = new HashMap<>(
            Map.of(
                    "FB48X66S", "1",
                    "FB48X84F", "2"
            )
    );

    public static void main(String[] args) {
        // read blob.basin.skeleton - user input file
        var skeletonTxt = FilesUtil.read(Paths.get(base + skeleton));

        var skeletonTextLines = skeletonTxt.split("\\n");

        // get requested basin from first line, eg; FB48X84F
        var requestedBasin = skeletonTextLines[0].split(" ")[1].trim();

        // read content from blob.basinConfig.txt
        var basinConfigText = readContent(basinConfigPath);

        // get current basin config value written to blob.basinConfig.txt
        var currentBasinConfig = basinConfigText.split("\\n")[0].split(" ")[1].trim();

        // replace with requested via look-up table
        basinConfigText = basinConfigText.replaceFirst(currentBasinConfig, basinConfigTable.get(requestedBasin));

        // write new basin config to blob.basin.SLDASM
        writeContent(basinConfigText, basinConfigPath);

        /*
        sets basin-cover mate height
         */

        // get basin depth - ASSUMES: basin request in all caps
        var basinDepth = requestedBasin.split("X")[1].split("[A-Z]")[0];

        // read content from blob.cover_mates.txt
        var basinCoverMateText = readContent(basinCoverMatePath);

        var currentCoverMate = basinCoverMateText.split("\\n")[0].split(" ")[1].split("in")[0];

        currentCoverMate = String.valueOf((int) Double.parseDouble(currentCoverMate));

        basinCoverMateText = basinCoverMateText.replaceFirst(currentCoverMate, basinDepth);

        writeContent(basinCoverMateText, basinCoverMatePath);
    }

    private static String readContent(String file) {
        return FilesUtil.read(Paths.get(base + file));
    }

    private static void writeContent(String content, String file) {
        FilesUtil.write(content, Paths.get(base + file));
    }
}
