import de.griefed.larsonscanner.LarsonScanner.ScannerConfig;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Playground {

  private static final LarsonScanner larsonScanner = new LarsonScanner();
  private static ScannerConfig config;

  public static void main(String[] args) {
    int width = 1400;

    SwingUtilities.invokeLater(
        () -> {
          JFrame frame = new JFrame();

          JPanel alphaPanel = new JPanel(true);
          alphaPanel.setLayout(new BoxLayout(alphaPanel, BoxLayout.Y_AXIS));
          JLabel alphasLabel = new JLabel("Alphas");
          JTextField alphasField = new JTextField();
          alphasField.setText("100,200,255,200,100");
          alphasField.setToolTipText(
              "Transparency for each element in the eye. Higher value = less transparency. 255 = none.");
          JButton alphasButton = new JButton("Alphas");
          alphasButton.addActionListener(
              e -> {
                String[] alphaValues = alphasField.getText().split(",");
                larsonScanner.setAlphas(
                    new short[] {
                        new Short(alphaValues[0]),
                        new Short(alphaValues[1]),
                        new Short(alphaValues[2]),
                        new Short(alphaValues[3]),
                        new Short(alphaValues[4])
                    });
              });
          alphaPanel.add(alphasLabel);
          alphaPanel.add(alphasField);
          alphaPanel.add(alphasButton);

          JPanel intervalPanel = new JPanel(true);
          intervalPanel.setLayout(new BoxLayout(intervalPanel, BoxLayout.Y_AXIS));
          JLabel intervalLabel = new JLabel("Interval");
          JTextField intervalField = new JTextField();
          intervalField.setText("1000");
          intervalField.setToolTipText("Interval at which to draw eye");
          JButton intervalButton = new JButton("Interval");
          intervalButton.addActionListener(
              e -> larsonScanner.setInterval(new Short(intervalField.getText())));
          intervalPanel.add(intervalLabel);
          intervalPanel.add(intervalField);
          intervalPanel.add(intervalButton);

          JPanel dividerPanel = new JPanel(true);
          dividerPanel.setLayout(new BoxLayout(dividerPanel, BoxLayout.Y_AXIS));
          JLabel dividerLabel = new JLabel("Divider");
          JTextField dividerField = new JTextField();
          dividerField.setText("25");
          dividerField.setToolTipText(
              "Divider influences speed of eye. Width / divider is added/subtracted to/from position.");
          JButton dividerButton = new JButton("Divider");
          dividerButton.addActionListener(
              e -> larsonScanner.setDivider(new Short(dividerField.getText())));
          dividerPanel.add(dividerLabel);
          dividerPanel.add(dividerField);
          dividerPanel.add(dividerButton);

          JPanel elementsPanel = new JPanel(true);
          elementsPanel.setLayout(new BoxLayout(elementsPanel, BoxLayout.Y_AXIS));
          JLabel elementsLabel = new JLabel("Elements");
          JTextField elementsField = new JTextField();
          elementsField.setText("5");
          elementsField.setToolTipText("Amount of elements (eyeballs) to draw.");
          JButton elementsButton = new JButton("Elements");
          elementsButton.addActionListener(
              e -> larsonScanner.setNumberOfElements(Byte.parseByte(elementsField.getText())));
          elementsPanel.add(elementsLabel);
          elementsPanel.add(elementsField);
          elementsPanel.add(elementsButton);

          JPanel fractionsPanel = new JPanel(true);
          fractionsPanel.setLayout(new BoxLayout(fractionsPanel, BoxLayout.Y_AXIS));
          JLabel fractionsLabel = new JLabel("Fractions");
          JTextField fractionsField = new JTextField();
          fractionsField.setText("0.4f, 1.0f");
          fractionsField.setToolTipText("Distribution used when gradient are used for painting.");
          JButton fractionsButton = new JButton("Fractions");
          fractionsButton.addActionListener(
              e -> {
                String[] fractionValues = fractionsField.getText().split(",");
                larsonScanner.setFractions(
                    Float.parseFloat(fractionValues[0]), Float.parseFloat(fractionValues[1]));
              });
          fractionsPanel.add(fractionsLabel);
          fractionsPanel.add(fractionsField);
          fractionsPanel.add(fractionsButton);

          JPanel gapPanel = new JPanel(true);
          gapPanel.setLayout(new BoxLayout(gapPanel, BoxLayout.Y_AXIS));
          JLabel gapLabel = new JLabel("Gap Percent");
          JTextField gapField = new JTextField();
          gapField.setText("25.0f");
          gapField.setToolTipText("Width of the gap between tectangles in % of element width");
          JButton gapButton = new JButton("Gap Percent");
          gapButton.addActionListener(
              e -> larsonScanner.setGapPercent(Float.parseFloat(gapField.getText())));
          gapPanel.add(gapLabel);
          gapPanel.add(gapField);
          gapPanel.add(gapButton);

          JPanel eyesPanel = new JPanel(true);
          eyesPanel.setLayout(new BoxLayout(eyesPanel, BoxLayout.Y_AXIS));
          JLabel eyesLabel = new JLabel("Eye Colours");
          JTextField eyesField = new JTextField();
          eyesField.setText("255,0,0;255,0,0;255,0,0;255,0,0;255,0,0");
          JButton eyesButton = new JButton("Eye Colour");
          eyesButton.addActionListener(
              e -> {
                String[] colorValues = eyesField.getText().split(";");
                Color[] colors = new Color[colorValues.length];
                for (int i = 0; i < colorValues.length; i++) {
                  String[] values = colorValues[i].split(",");
                  colors[i] =
                      new Color(
                          Integer.parseInt(values[0]),
                          Integer.parseInt(values[1]),
                          Integer.parseInt(values[2]));
                }
                larsonScanner.setEyeColours(colors);
              });
          eyesPanel.add(eyesLabel);
          eyesPanel.add(eyesField);
          eyesPanel.add(eyesButton);

          JPanel scannerBackgroundPanel = new JPanel(true);
          scannerBackgroundPanel.setLayout(new BoxLayout(scannerBackgroundPanel, BoxLayout.Y_AXIS));
          JLabel scannerBackgroundLabel = new JLabel("Scanner Background");
          JTextField scannerBackgroundField = new JTextField();
          scannerBackgroundField.setText("255,0,0");
          scannerBackgroundField.setToolTipText(
              "Background behind the scanner. Only visible is aspect ratio is forced and LarsconScanner is larger than the eye display.");
          JButton scannerBackgroundButton = new JButton("Scanner Background Colour");
          scannerBackgroundButton.addActionListener(
              e -> {
                String[] colorValues = scannerBackgroundField.getText().split(",");
                larsonScanner.setBackground(
                    new Color(
                        Integer.parseInt(colorValues[0]),
                        Integer.parseInt(colorValues[1]),
                        Integer.parseInt(colorValues[2])));
              });
          scannerBackgroundPanel.add(scannerBackgroundLabel);
          scannerBackgroundPanel.add(scannerBackgroundField);
          scannerBackgroundPanel.add(scannerBackgroundButton);

          JPanel eyeBackgroundPanel = new JPanel(true);
          eyeBackgroundPanel.setLayout(new BoxLayout(eyeBackgroundPanel, BoxLayout.Y_AXIS));
          JLabel eyeBackgroundLabel = new JLabel("Eye Background");
          JTextField eyeBackgroundField = new JTextField();
          eyeBackgroundField.setText("255,0,0");
          eyeBackgroundField.setToolTipText("Set they color of all elements in the eye.");
          JButton eyeBackgroundButton = new JButton("Set Eye Background Colour");
          eyeBackgroundButton.addActionListener(
              e -> {
                String[] colorValues = eyeBackgroundField.getText().split(",");
                larsonScanner.setEyeBackground(
                    new Color(
                        Integer.parseInt(colorValues[0]),
                        Integer.parseInt(colorValues[1]),
                        Integer.parseInt(colorValues[2])));
              });
          eyeBackgroundPanel.add(eyeBackgroundLabel);
          eyeBackgroundPanel.add(eyeBackgroundField);
          eyeBackgroundPanel.add(eyeBackgroundButton);

          JPanel partitionPanel = new JPanel(true);
          partitionPanel.setLayout(new BoxLayout(partitionPanel, BoxLayout.Y_AXIS));
          JLabel partitionLabel = new JLabel("Partition Divider");
          JTextField partitionField = new JTextField();
          partitionField.setText("5.0D");
          partitionField.setToolTipText("Determines the width of the eye.");
          JButton partitionButton = new JButton("Set Partition Divider");
          partitionButton.addActionListener(
              e -> larsonScanner.setPartitionDivider(Double.parseDouble(partitionField.getText())));
          partitionPanel.add(partitionLabel);
          partitionPanel.add(partitionField);
          partitionPanel.add(partitionButton);

          JPanel customizePanel = new JPanel(true);
          customizePanel.setLayout(new BoxLayout(customizePanel, BoxLayout.X_AXIS));
          customizePanel.setMaximumSize(new Dimension(width, 80));
          customizePanel.add(alphaPanel);
          customizePanel.add(intervalPanel);
          customizePanel.add(dividerPanel);
          customizePanel.add(elementsPanel);
          customizePanel.add(fractionsPanel);
          customizePanel.add(gapPanel);
          customizePanel.add(partitionPanel);
          customizePanel.add(eyesPanel);
          customizePanel.add(scannerBackgroundPanel);
          customizePanel.add(eyeBackgroundPanel);

          JPanel toggles = new JPanel(true);
          toggles.setLayout(new BoxLayout(toggles, BoxLayout.X_AXIS));
          JButton startStop = new JButton("Start/Stop");
          startStop.addActionListener(e -> larsonScanner.togglePauseUnpause());
          JButton toggleShape = new JButton("Toggle Shape");
          toggleShape.addActionListener(e -> larsonScanner.toggleShape());
          JButton toggleAspectRatioForce = new JButton("Toggle aspect ratio");
          toggleAspectRatioForce.addActionListener(
              e -> larsonScanner.forceAspectRatio(!larsonScanner.isAspectRatioForced()));
          JButton toggleGradient = new JButton("Toggle Gradient");
          toggleGradient.addActionListener(e -> larsonScanner.toggleGradient());
          JButton toggleDivider = new JButton("Toggle Divider");
          toggleDivider.addActionListener(e -> larsonScanner.toggleDivider());
          JButton toggleAnimation = new JButton("Toggle Animation");
          toggleAnimation.addActionListener(e -> larsonScanner.toggleCylonAnimation());
          JButton low = new JButton("Low");
          low.addActionListener(e -> larsonScanner.setQualityLow());
          JButton mid = new JButton("Medium");
          mid.addActionListener(e -> larsonScanner.setQualityMedium());
          JButton high = new JButton("High");
          high.addActionListener(e -> larsonScanner.setQualityHigh());
          JButton loadDefaults = new JButton("Load Defaults");
          loadDefaults.addActionListener(e -> larsonScanner.loadDefaults());
          JButton getCurrentConfig = new JButton("Get Current Config");
          getCurrentConfig.addActionListener(e -> getConfig());
          JButton loadSavedConfig = new JButton("Load Saved Config");
          loadSavedConfig.addActionListener(e -> larsonScanner.loadConfig(config));

          toggles.add(startStop);
          toggles.add(toggleShape);
          toggles.add(toggleAspectRatioForce);
          toggles.add(toggleGradient);
          toggles.add(toggleDivider);
          toggles.add(toggleAnimation);
          toggles.add(low);
          toggles.add(mid);
          toggles.add(high);
          toggles.add(loadDefaults);
          toggles.add(getCurrentConfig);
          toggles.add(loadSavedConfig);

          JPanel config = new JPanel(true);
          config.setLayout(new BoxLayout(config, BoxLayout.Y_AXIS));
          config.add(customizePanel);
          config.add(toggles);
          config.add(larsonScanner);

          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setPreferredSize(new Dimension(width, 182));
          frame.setSize(new Dimension(width, 182));
          frame.add(config);
          frame.setLocationRelativeTo(null);
          frame.pack();
          frame.setVisible(true);

          larsonScanner.play();
        });
  }

  private static void getConfig() {
    config = larsonScanner.getCurrentConfig();
  }
}
