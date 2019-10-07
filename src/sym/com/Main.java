package sym.com;

import bo.core.system.FilesUtil;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String base = "C:\\Users\\bolinger\\Documents\\SW Projects\\322326 - Prefab Blob\\";
    private static final String skeleton = "base blob - L1\\blob.basin.skeleton.txt";
    private static final String coverBasinMate = "blob - L2\\blob.cover_mates.txt";
    private static final HashMap<String, String> basinConfigTable = new HashMap<>(
            Map.of(
                    "FB48X66S", "1"
            )
    );

    public static void main(String[] args) {
        var skeletonTxt = FilesUtil.read(Paths.get(base + skeleton));

        var basinMateDepth = skeletonTxt.split("\\n")[0].split(" ")[1];

        basinMateDepth = (Double.parseDouble(basinMateDepth) + 0.25) + "in\n";

        var coverBasinMateTxt = FilesUtil.read(Paths.get(base + coverBasinMate));

        var currentBasinCoverMateDepth = coverBasinMateTxt.split("\\n")[0].split(" ")[1] + "\n";

        var newCoverMate = coverBasinMateTxt.replace(currentBasinCoverMateDepth, basinMateDepth);

        FilesUtil.write(newCoverMate, Paths.get(base + coverBasinMate));
    }
}
