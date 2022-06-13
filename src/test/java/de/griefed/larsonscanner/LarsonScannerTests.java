package de.griefed.larsonscanner;

import de.griefed.larsonscanner.LarsonScanner.ScannerConfig;
import java.awt.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LarsonScannerTests {

  @Test
  void larsonScannerTests() {
    float[] defaultFractions = {0.4f,1.0f};
    Color[] defaultColours = {new Color(255, 0, 0),new Color(255, 0, 0),new Color(255, 0, 0),new Color(255, 0, 0),new Color(255, 0, 0)};
    short[] defaultAlphas = {100, 200, 255, 200, 100};

    LarsonScanner larsonScanner = new LarsonScanner();
    Assertions.assertEquals(defaultFractions[0],larsonScanner.getFractions()[0]);
    Assertions.assertEquals(defaultFractions[1],larsonScanner.getFractions()[1]);
    Assertions.assertEquals(defaultColours[0],larsonScanner.getEyeColours()[0]);
    Assertions.assertEquals(defaultColours[1],larsonScanner.getEyeColours()[1]);
    Assertions.assertEquals(defaultColours[2],larsonScanner.getEyeColours()[2]);
    Assertions.assertEquals(defaultColours[3],larsonScanner.getEyeColours()[3]);
    Assertions.assertEquals(defaultColours[4],larsonScanner.getEyeColours()[4]);
    Assertions.assertEquals(25.0f, larsonScanner.getGapPercent());
    Assertions.assertEquals(5.0D, larsonScanner.getPartitionDivider());
    Assertions.assertEquals(0, larsonScanner.getQualitySetting());
    Assertions.assertEquals(defaultAlphas[0],larsonScanner.getAlphas()[0]);
    Assertions.assertEquals(defaultAlphas[1],larsonScanner.getAlphas()[1]);
    Assertions.assertEquals(defaultAlphas[2],larsonScanner.getAlphas()[2]);
    Assertions.assertEquals(defaultAlphas[3],larsonScanner.getAlphas()[3]);
    Assertions.assertEquals(defaultAlphas[4],larsonScanner.getAlphas()[4]);
    Assertions.assertEquals(100, larsonScanner.getInterval());
    Assertions.assertEquals(25, larsonScanner.getDivider());
    Assertions.assertEquals(5, larsonScanner.getNumberOfElements());
    Assertions.assertFalse(larsonScanner.isAspectRatioForced());
    Assertions.assertTrue(larsonScanner.isShapeOval());
    Assertions.assertTrue(larsonScanner.isGradientActive());
    Assertions.assertTrue(larsonScanner.isDividerActive());
    Assertions.assertTrue(larsonScanner.isCylonAnimation());

    ScannerConfig defaultConfig = new ScannerConfig();
    Assertions.assertEquals(defaultFractions[0],defaultConfig.getFractions()[0]);
    Assertions.assertEquals(defaultFractions[1],defaultConfig.getFractions()[1]);
    Assertions.assertEquals(defaultColours[0],defaultConfig.getEyeColours()[0]);
    Assertions.assertEquals(defaultColours[1],defaultConfig.getEyeColours()[1]);
    Assertions.assertEquals(defaultColours[2],defaultConfig.getEyeColours()[2]);
    Assertions.assertEquals(defaultColours[3],defaultConfig.getEyeColours()[3]);
    Assertions.assertEquals(defaultColours[4],defaultConfig.getEyeColours()[4]);
    Assertions.assertEquals(25.0f, defaultConfig.getGapPercent());
    Assertions.assertEquals(5.0D, defaultConfig.getPartitionDivider());
    Assertions.assertEquals(0, defaultConfig.getQualitySetting());
    Assertions.assertEquals(defaultAlphas[0],defaultConfig.getAlphas()[0]);
    Assertions.assertEquals(defaultAlphas[1],defaultConfig.getAlphas()[1]);
    Assertions.assertEquals(defaultAlphas[2],defaultConfig.getAlphas()[2]);
    Assertions.assertEquals(defaultAlphas[3],defaultConfig.getAlphas()[3]);
    Assertions.assertEquals(defaultAlphas[4],defaultConfig.getAlphas()[4]);
    Assertions.assertEquals(100, defaultConfig.getInterval());
    Assertions.assertEquals(25, defaultConfig.getDivider());
    Assertions.assertEquals(5, defaultConfig.getNumberOfElements());
    Assertions.assertFalse(defaultConfig.isAspectRatioForced());
    Assertions.assertTrue(defaultConfig.isShapeOval());
    Assertions.assertTrue(defaultConfig.isGradientActive());
    Assertions.assertTrue(defaultConfig.isDividerActive());
    Assertions.assertTrue(defaultConfig.isCylonAnimation());


    float[] customFractions = {0.1f,0.5f};
    Color[] customColours = {new Color(0, 255, 0),new Color(0, 0, 255),new Color(0, 255, 255)};
    short[] customAlphas = {100,100,100};
    ScannerConfig customConfig = new ScannerConfig(
        2,
        new short[]{100,100,100},
        (short) 50,
        (short) 50,
        (byte) 3,
        new float[] {0.1f, 0.5f},
        50.0f,
        10.0D,
        true,
        false,
        false,
        false,
        false,
        new Color[]{new Color(0, 255, 0),new Color(0, 0, 255),new Color(0, 255, 255)},
        new Color(255, 255, 255),
        new Color(125, 125, 125)
    );
    Assertions.assertEquals(customFractions[0],customConfig.getFractions()[0]);
    Assertions.assertEquals(customFractions[1],customConfig.getFractions()[1]);
    Assertions.assertEquals(customColours[0],customConfig.getEyeColours()[0]);
    Assertions.assertEquals(customColours[1],customConfig.getEyeColours()[1]);
    Assertions.assertEquals(customColours[2],customConfig.getEyeColours()[2]);
    Assertions.assertEquals(50.0f, customConfig.getGapPercent());
    Assertions.assertEquals(10.0D, customConfig.getPartitionDivider());
    Assertions.assertEquals(2, customConfig.getQualitySetting());
    Assertions.assertEquals(customAlphas[0],customConfig.getAlphas()[0]);
    Assertions.assertEquals(customAlphas[1],customConfig.getAlphas()[1]);
    Assertions.assertEquals(customAlphas[2],customConfig.getAlphas()[2]);
    Assertions.assertEquals(50, customConfig.getInterval());
    Assertions.assertEquals(50, customConfig.getDivider());
    Assertions.assertEquals(3, customConfig.getNumberOfElements());
    Assertions.assertTrue(customConfig.isAspectRatioForced());
    Assertions.assertFalse(customConfig.isShapeOval());
    Assertions.assertFalse(customConfig.isGradientActive());
    Assertions.assertFalse(customConfig.isDividerActive());
    Assertions.assertFalse(customConfig.isCylonAnimation());


    larsonScanner.loadConfig(customConfig);
    Assertions.assertEquals(customFractions[0],larsonScanner.getFractions()[0]);
    Assertions.assertEquals(customFractions[1],larsonScanner.getFractions()[1]);
    Assertions.assertEquals(customColours[0],larsonScanner.getEyeColours()[0]);
    Assertions.assertEquals(customColours[1],larsonScanner.getEyeColours()[1]);
    Assertions.assertEquals(customColours[2],larsonScanner.getEyeColours()[2]);
    Assertions.assertEquals(50.0f, larsonScanner.getGapPercent());
    Assertions.assertEquals(10.0D, larsonScanner.getPartitionDivider());
    Assertions.assertEquals(2, larsonScanner.getQualitySetting());
    Assertions.assertEquals(customAlphas[0],larsonScanner.getAlphas()[0]);
    Assertions.assertEquals(customAlphas[1],larsonScanner.getAlphas()[1]);
    Assertions.assertEquals(customAlphas[2],larsonScanner.getAlphas()[2]);
    Assertions.assertEquals(50, larsonScanner.getInterval());
    Assertions.assertEquals(50, larsonScanner.getDivider());
    Assertions.assertEquals(3, larsonScanner.getNumberOfElements());
    Assertions.assertTrue(larsonScanner.isAspectRatioForced());
    Assertions.assertFalse(larsonScanner.isShapeOval());
    Assertions.assertFalse(larsonScanner.isGradientActive());
    Assertions.assertFalse(larsonScanner.isDividerActive());
    Assertions.assertFalse(larsonScanner.isCylonAnimation());

    larsonScanner.loadDefaults();
    Assertions.assertEquals(defaultFractions[0],larsonScanner.getFractions()[0]);
    Assertions.assertEquals(defaultFractions[1],larsonScanner.getFractions()[1]);
    Assertions.assertEquals(defaultColours[0],larsonScanner.getEyeColours()[0]);
    Assertions.assertEquals(defaultColours[1],larsonScanner.getEyeColours()[1]);
    Assertions.assertEquals(defaultColours[2],larsonScanner.getEyeColours()[2]);
    Assertions.assertEquals(defaultColours[3],larsonScanner.getEyeColours()[3]);
    Assertions.assertEquals(defaultColours[4],larsonScanner.getEyeColours()[4]);
    Assertions.assertEquals(25.0f, larsonScanner.getGapPercent());
    Assertions.assertEquals(5.0D, larsonScanner.getPartitionDivider());
    Assertions.assertEquals(0, larsonScanner.getQualitySetting());
    Assertions.assertEquals(defaultAlphas[0],larsonScanner.getAlphas()[0]);
    Assertions.assertEquals(defaultAlphas[1],larsonScanner.getAlphas()[1]);
    Assertions.assertEquals(defaultAlphas[2],larsonScanner.getAlphas()[2]);
    Assertions.assertEquals(defaultAlphas[3],larsonScanner.getAlphas()[3]);
    Assertions.assertEquals(defaultAlphas[4],larsonScanner.getAlphas()[4]);
    Assertions.assertEquals(100, larsonScanner.getInterval());
    Assertions.assertEquals(25, larsonScanner.getDivider());
    Assertions.assertEquals(5, larsonScanner.getNumberOfElements());
    Assertions.assertFalse(larsonScanner.isAspectRatioForced());
    Assertions.assertTrue(larsonScanner.isShapeOval());
    Assertions.assertTrue(larsonScanner.isGradientActive());
    Assertions.assertTrue(larsonScanner.isDividerActive());
    Assertions.assertTrue(larsonScanner.isCylonAnimation());
  }
}
