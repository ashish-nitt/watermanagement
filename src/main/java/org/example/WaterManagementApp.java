package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Stream;

public class WaterManagementApp {
    Integer getDefaultHeadCountFromAppartmentType(int type) {
        switch (type) {
            case 2: return 3;
            case 3: return 5;
        }
        throw new UnsupportedOperationException("Unsupported Appartment Type " + type);
    }
    private static final int WATER_PER_HEAD = 10 * 30;
    private static final double CORPORTATION_WATER_RATE = 1;
    private static final double BOREWELL_WATER_RATE = 1.5;
    private static final NavigableMap<Integer, Double> TANKER_WATER_SLAB_RATES_MAP = new TreeMap<>() {{
        put(0, 2D);
        put(501, 3D);
        put(1501, 5D);
        put(3001, 8D);
    }};

    public String process(String inpFileName) throws IOException {
        String result = "";
        BufferedReader reader = new BufferedReader(new FileReader(getClass().getResource("/" + inpFileName).getFile()));
        String line = "";
        int defaultHeadCount = 0;
        int headCount = 0;
        double corporationWaterRatio = 0;
        double boreWaterRatio = 0;
        while ((line = reader.readLine()) != null) {
            String[] words = line.strip().split("\\s");
            if (words.length > 0) {
                switch (words[0].strip()) {
                    case "ALLOT_WATER":
                        if (headCount > 0) {
                            throw new UnsupportedOperationException("ALLOT_WATER shouldn't be called twice");
                        }
                        defaultHeadCount = getDefaultHeadCountFromAppartmentType(Integer.parseInt(words[1].strip()));
                        headCount = defaultHeadCount;
                        String[] ratio = words[2].strip().split(":");
                        corporationWaterRatio = Integer.parseInt(ratio[0].strip());
                        boreWaterRatio = Integer.parseInt(ratio[1].strip());
                        break;
                    case "ADD_GUESTS":
                        headCount += Integer.parseInt(words[1].strip());
                        break;
                    case "BILL":
                        int totalWater = headCount * WATER_PER_HEAD;
                        double corporationWaterBill = defaultHeadCount * (corporationWaterRatio/(corporationWaterRatio+boreWaterRatio)) * WATER_PER_HEAD * CORPORTATION_WATER_RATE;
                        double borewellWaterBill = defaultHeadCount * (boreWaterRatio/(corporationWaterRatio+boreWaterRatio)) * WATER_PER_HEAD * BOREWELL_WATER_RATE;
                        int tankerWater = (headCount - defaultHeadCount) * WATER_PER_HEAD;
                        double tankerWaterBill= tankerWater * TANKER_WATER_SLAB_RATES_MAP.floorEntry(tankerWater).getValue();
                        double bill = corporationWaterBill + borewellWaterBill + tankerWaterBill;
                        System.out.println(String.format("%d(%.0f/%.0f) %d =>%.0f %.0f %.0f", defaultHeadCount, corporationWaterRatio, boreWaterRatio, headCount - defaultHeadCount, corporationWaterBill, borewellWaterBill, tankerWaterBill));
                        return String.format("%d %d", totalWater, (long)bill);
                    default:
                        throw new UnsupportedOperationException("Unsupported command operation " + words[0]);
                }
            }
        }
        return result;
    }
}