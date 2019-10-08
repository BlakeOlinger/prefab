package sym.com;

import bo.core.system.FilesUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String base = "C:\\Users\\bolinger\\Desktop\\Prefab Blob - Cover Blob\\";
    private static final String skeleton = "base blob - L1\\blob.basin.skeleton.txt";
    private static final String basinCoverMatePath = "blob - L2\\blob.cover_mates.txt";
    private static final String basinConfigPath = "base blob - L1\\blob.basinConfig.txt";
    private static final String upperGuideRailMatesPath = "blob - L3\\blob.L3_basinCoverL2_upperGuidRailL2_upperGuideRailMates.txt";
    private static final String baseElbowPath = "base blob - L1\\blob.baseElbow.txt";
    private static final String upperGuideRailPath = "base blob - L1\\blob.upperGuideRail.txt";
    private static final String dischargeCouplingPath = "base blob - L1\\blob.dischargeCoupling.txt";
    private static final String dischargeCouplingPipePath = "base blob - L1\\blob.dischargePipe_dischargeCoupling.txt";
    private static final HashMap<String, String> basinConfigTable = new HashMap<>(
            Map.of(
                    "FB48X66S", "1",
                    "FB48X84F", "2"
            )
    );
    private static final HashMap<String, String[]> coverHatchClearAccessTable = new HashMap<>(
            Map.of(
                    "C48HSA-ONNNN", new String[]{"36", "24"}
            )
    );
    private static final HashMap<String, String> baseElbowUpperGuideRailXOffsetTable = new HashMap<>(
            Map.of(
                    "BERS-0320V", "11.88"
            )
    );
    private static final HashMap<String, String> baseElbowFrontPlaneOffsetTable = new HashMap<>(
            Map.of(
                    "BERS-0320V", "3.75"
            )
    );
    private static final HashMap<String, String> upperGuideRailFrontPlaneOffsetTable = new HashMap<>(
            Map.of(
                    "UGB-STNLS", "0.91"
            )
    );
    private static final HashMap<String, String> dischargeCouplingFrontPlaneOffsetTable = new HashMap<>(
            Map.of(
                    "C300S", "1.63"
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
//        writeContent(basinConfigText, basinConfigPath);
        //
        // END SET BASIN CONFIG


        // set basin-cover mate height
        //

        // get basin depth - ASSUMES: basin request in all caps
        var basinDepth = requestedBasin.split("X")[1].split("[A-Z]")[0];

        // read content from blob.cover_mates.txt
        var basinCoverMateText = readContent(basinCoverMatePath);

        var currentCoverMate = getDimensionFromLine(basinCoverMateText, 0);

        currentCoverMate = String.valueOf((int) Double.parseDouble(currentCoverMate));

        basinCoverMateText = basinCoverMateText.replaceFirst(currentCoverMate, basinDepth);

//        writeContent(basinCoverMateText, basinCoverMatePath);
        //
        // END set basin-cover mate height


        // START - upper guide rail mates
        // guidRailHeight from blob.L3_basinCoverL2_upperGuidRailL2_upperGuideRailMates.txt is equal to basin height
        // without adding 1/4"
        var upperGuideRailMatesContent = readContent(upperGuideRailMatesPath);

        var currentUpperGuideRailMateHeight = getDimensionFromLine(upperGuideRailMatesContent, 0);

        // sets guideRailHeight = basin depth
        upperGuideRailMatesContent = upperGuideRailMatesContent.replaceFirst(currentUpperGuideRailMateHeight, basinDepth);

        var requestedCover = skeletonTextLines[1].trim();

        var clearAccessY = coverHatchClearAccessTable.get(requestedCover)[1];

        var guideRailZOffset = Double.parseDouble(clearAccessY) / 2;

        var currentGuideRailZOffset = getDimensionFromLine(upperGuideRailMatesContent, 5);

        var guideRailZOffsetLine = upperGuideRailMatesContent.split("\\n")[5];

        var newGuideRailZOffsetLine = guideRailZOffsetLine.replace(currentGuideRailZOffset, String.valueOf(guideRailZOffset));

        // sets guideRailZOffset - placement against the inner clear access L bracket
        upperGuideRailMatesContent = upperGuideRailMatesContent.replace(guideRailZOffsetLine, newGuideRailZOffsetLine);

        // get on line 3 of blob.basin.skeleton the base elbow requested and compare to the lookup table for the upper guide rail X offset
        var currentGuideRailXOffset = upperGuideRailMatesContent.split("\\n")[3].split("=")[1];

        var requestedBaseElbow = skeletonTextLines[2];

        var currentGuideRailXOffsetLine = upperGuideRailMatesContent.split("\\n")[3];

        var newGuideRailXOffsetLine = currentGuideRailXOffsetLine.replace(currentGuideRailXOffset, "") + " " + baseElbowUpperGuideRailXOffsetTable.get(requestedBaseElbow) + "in";

        upperGuideRailMatesContent = upperGuideRailMatesContent.replace(currentGuideRailXOffsetLine, newGuideRailXOffsetLine);

//        writeContent(upperGuideRailMatesContent, upperGuideRailMatesPath);
        //
        // END WRITE UPPER GUIDE RAIL XYZ MATES


    }

    private static String getDimensionFromLine(@NotNull String content, int lineIndex) {
        return content.split("\\n")[lineIndex].split(" ")[1].split("in")[0];
    }

    private static String readContent(String file) {
        return FilesUtil.read(Paths.get(base + file));
    }

    private static void writeContent(String content, String file) {
        FilesUtil.write(content, Paths.get(base + file));
    }
}
