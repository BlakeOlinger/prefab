package sym.com;

import bo.core.system.FilesUtil;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String base = "C:\\Users\\bolinger\\Documents\\SW Projects\\322326 - Prefab Blob\\";
    private static final String skeleton = "base blob - L1\\blob.basin.skeleton.txt";
    private static final String coverBasinMate = "blob - L2\\blob.cover_mates.txt";
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
        var basinConfigText = FilesUtil.read(Paths.get(base + basinConfigPath));

        // get current basin config value written to blob.basinConfig.txt
        var currentBasinConfig = basinConfigText.split("\\n")[0].split(" ")[1].trim();

        // replace with requested via look-up table
        basinConfigText = basinConfigText.replaceFirst(currentBasinConfig, basinConfigTable.get(requestedBasin));

        // write new basin config to blob.basin.SLDASM
        FilesUtil.write(basinConfigText, Paths.get(base + basinConfigPath));


    }
}
