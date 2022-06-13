package de.griefed.larsonscanner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A Larson Scanner which some may or may not know from the Cylons from Battlestar Galactica or Kitt
 * from Knight Rider.
 *
 * <p>The Larson Scanner is named after Glen A. Larson, who produced the Battlestar Galactica and
 * Knight Rider series, and is responsible for introducing scanning red light effects to the sci-fi
 * TV viewing population of the 1970s and 80s.
 *
 * <p>Basically, a Larson Scanner is a red light which runs from left to right, to left, to right
 * etc. with, usually, a nice fading effect. It's so simple, but oh so awesome!
 *
 * <p>A user by the name of <a href=https://github.com/kreezxil>Kreezxil</a> made a <a
 * href=https://github.com/Griefed/ServerPackCreator/issues/338>request</a> for <a
 * href=https://github.com/Griefed/ServerPackCreator>ServerPackCreator</a> to have some sort of
 * progress-/thinking bar to indicate that ServerPackCreator is currently busy doing server pack
 * things. And off I went to create this little over engineered thing. Enjoy!
 *
 * @author Griefed
 */
@SuppressWarnings("unused")
public class LarsonScanner extends JPanel {

  private static final Color DEFAULT_BACKGROUND_COLOUR = new Color(0, 0, 0);
  private static final Color DEFAULT_EYE_COLOUR = new Color(255, 0, 0);
  private final Thread ANIMATOR_THREAD;
  private final Eye EYE;

  /**
   * Create a Larson Scanner with default settings.
   *
   * <p>Default settings are:
   *
   * <ul>
   *   <li>Scanner background: Black
   *   <li>Timer interval: 100ms
   *   <li>Eye colour: Red
   *   <li>Fractions: 0.4f, 1.0f
   *   <li>Alphas: 100,200,255,200,100
   *   <li>Divider: 25
   *   <li>Number of elements: 5
   *   <li>Aspect ratio forced: false
   *   <li>Shape: Oval
   *   <li>Gradient colours: true
   *   <li>Divider used: true
   *   <li>Rendering quality: Low
   * </ul>
   *
   * @author Griefed
   */
  public LarsonScanner() {
    super();
    setDoubleBuffered(true);
    setLayout(new BorderLayout());
    setBackground(DEFAULT_BACKGROUND_COLOUR);

    EYE = new Eye();
    add(EYE, BorderLayout.CENTER);

    ANIMATOR_THREAD = new Thread(EYE, "animation");
    ANIMATOR_THREAD.start();
  }

  /**
   * Convenience constructor allowing you to specify the interval at which the position changes. For
   * more information regarding possible settings, see {@link LarsonScanner()}.
   *
   * @param updateInterval {@link Short} Interval in milliseconds at which to scroll.
   * @author Griefed
   */
  public LarsonScanner(short updateInterval) {
    super();
    setDoubleBuffered(true);
    setLayout(new BorderLayout());
    setBackground(DEFAULT_BACKGROUND_COLOUR);

    EYE = new Eye(updateInterval);
    add(EYE, BorderLayout.CENTER);

    ANIMATOR_THREAD = new Thread(EYE, "LarsonScanner Eye");
    ANIMATOR_THREAD.start();
  }

  /**
   * Convenience constructor allowing you to specify the interval at which the position changes, as
   * well as the background colours for the scanner and the eye. For more information regarding
   * possible settings, see {@link LarsonScanner()}.
   *
   * @param interval {@link Short} Interval in milliseconds at which to scroll.
   * @param backgroundColor {@link Color} The background colour for the scanner and the eye.
   * @author Griefed
   */
  public LarsonScanner(short interval, @NotNull Color backgroundColor) {
    super();
    setDoubleBuffered(true);
    setLayout(new BorderLayout());
    setBackground(backgroundColor);

    EYE = new Eye(interval, backgroundColor);
    add(EYE, BorderLayout.CENTER);

    ANIMATOR_THREAD = new Thread(EYE, "LarsonScanner Eye");
    ANIMATOR_THREAD.start();
  }

  /**
   * Convenience constructor allowing you to specify the interval at which the position changes, the
   * background colours for the scanner and the eye, and the color of the eye. For more information
   * regarding possible settings, see {@link LarsonScanner()}.
   *
   * @param interval {@link Short} Interval in milliseconds at which to scroll.
   * @param backgroundColor {@link Color} The background colour for the scanner and the eye.
   * @param eyeColor {@link Color} The color of the eye.
   * @author Griefed
   */
  public LarsonScanner(short interval, @NotNull Color backgroundColor, @NotNull Color eyeColor) {
    super();
    setDoubleBuffered(true);
    setLayout(new BorderLayout());
    setBackground(backgroundColor);

    EYE = new Eye(interval, backgroundColor, eyeColor);
    add(EYE, BorderLayout.CENTER);

    ANIMATOR_THREAD = new Thread(EYE, "LarsonScanner Eye");
    ANIMATOR_THREAD.start();
  }

  /**
   * Get the currently set number of elements in the eye.
   *
   * @return {@link Byte} The current number of elements in the eye.
   * @author Griefed
   */
  public byte getNumberOfElements() {
    return EYE.numberOfElements;
  }

  /**
   * Set the number of elements in the eye. This number must be odd, meaning that it must not be
   * divisible by two. Examples: 1, 3, 5, 7, 9, 11, 13, 15, 17 and so on.
   *
   * <p>Default setting: <code>5</code>
   *
   * @param amount {@link Byte} Number of elements in the eye.
   * @throws IllegalArgumentException if the number specified is smaller than 1 or an even number.
   * @author Griefed
   */
  public void setNumberOfElements(byte amount) throws IllegalArgumentException {
    if (amount < 1) {
      throw new IllegalArgumentException(
          "Number of elements must be greater than zero. Specified " + amount);

    } else if ((amount & 1) == 0) {
      throw new IllegalArgumentException(
          "Number of elements must be an odd number. Specified " + amount);

    } else {

      pause();

      EYE.numberOfElements = amount;
      Color[] newColours = new Color[amount];
      for (int i = 0; i < amount; i++) {
        if (i < EYE.eyeColours.length) {
          newColours[i] = EYE.eyeColours[i];
        } else {
          newColours[i] = DEFAULT_EYE_COLOUR;
        }
      }
      EYE.eyeColours = newColours;

      short[] newAlphas = new short[amount];
      int median = (amount + 1) / 2;
      for (int i = 0; i < amount; i++) {
        if (i + 1 < median) {

          newAlphas[i] = (short) (255 / median * (i + 1));

        } else if (i + 1 == median) {

          newAlphas[i] = 255;

        } else {

          newAlphas[i] = (short) ((amount - i) * 255 / median);
        }
      }
      EYE.alphas = newAlphas;

      play();
    }
  }

  /**
   * Set the colour for all elements in the eye to the same colour.
   *
   * <p>Default setting: <code>255,0,0</code>
   *
   * @param color {@link Color} The color to set all elements in the eye to.
   * @author Griefed
   */
  public void setEyeColour(@NotNull Color color) {
    EYE.setEyeColour(color);
  }

  /**
   * Get the array of colours currently used for all elements in the eye, left-to-right / 0-n.
   *
   * @return {@link Color}-array containing the colours of each element in the eye, left-to-right /
   *     0-n.
   * @author Griefed
   */
  public Color[] getEyeColours() {
    return EYE.eyeColours;
  }

  /**
   * Set the colour for each element in the eye, from left to right. This {@link Color}-array must
   * be the same size as the current number of elements in the eye.
   *
   * <p>Default setting: <code>{255,0,0 , 255,0,0 , 255,0,0 , 255,0,0 , 255,0,0}</code>
   *
   * @param colours {@link Color}-array of colours, one for each element in the eye, from left to
   *     right.
   * @throws IllegalArgumentException if the size of the array is unequal to the current number of
   *     elements in the eye.
   * @author Griefed
   */
  public void setEyeColours(@NotNull Color @NotNull [] colours) throws IllegalArgumentException {
    if (colours.length != EYE.numberOfElements) {
      throw new IllegalArgumentException(
          "Color-array must contain exactly "
              + EYE.numberOfElements
              + " entries. Specified "
              + colours.length);
    } else {
      EYE.setEyeColours(colours);
    }
  }

  /**
   * Acquire the background colour of the eye.
   *
   * @return {@link Color} The background colour of the eye.
   * @author Griefed
   */
  public Color getEyeBackground() {
    return EYE.getBackground();
  }

  /**
   * Set the background colour for the eye.
   *
   * <p>Default setting: <code>0,0,0</code>
   *
   * @param backgroundColor {@link Color} The color to set the background of the eye to.
   * @author Griefed
   */
  public void setEyeBackground(@NotNull Color backgroundColor) {
    EYE.setBackground(backgroundColor);
  }

  /**
   * Whether to draw each element oval or rectangular. Set to <code>true</code> to use oval-shapes,
   * <code>false</code> to use rectangular shapes.
   *
   * <p>Default setting: <code>true</code>
   *
   * @param useOval {@link Boolean} Whether to use oval or rectangular shapes.
   * @author Griefed
   */
  public void drawOval(boolean useOval) {
    EYE.ovalShaped = useOval;
  }

  /**
   * Toggle the eye between oval-shape and rectangle-shape.
   *
   * @author Griefed
   */
  public void toggleShape() {
    EYE.ovalShaped = !EYE.ovalShaped;
  }

  /**
   * Return whether the shape is currently set to oval.
   *
   * @return {@link Boolean} <code>true</code> if oval, false otherwise.
   * @author Griefed
   */
  public boolean isShapeOval() {
    return EYE.ovalShaped;
  }

  /**
   * Whether to draw the eye using gradients. Set to <code>true</code> to use gradients, <code>false
   * </code> to use solid colours. Depending on the shape, gradients are drawn in two ways:
   *
   * <ul>
   *   <li>Oval shapes: Gradients are created from the center of element <code>n</code> to the
   *       height of element <code>n</code>. This is most beautiful when forcing the aspect ratio,
   *       as radial gradients are best for circles, not ovals.
   *   <li>Rectangle shapes: Gradients are created from left to right, at half the height of element
   *       <code>n</code>, over the width of element <code>n</code>, for each element.
   * </ul>
   *
   * <p>Default setting: <code>true</code>
   *
   * @param useGradient {@link Boolean} Whether to use gradients or solid colours.
   * @author Griefed
   */
  public void useGradient(boolean useGradient) {
    EYE.useGradients = useGradient;
  }

  /**
   * Toggle the drawing of the eye between gradient and solid colours. For more information, see
   * {@link #useGradient(boolean)}.
   *
   * @author Griefed
   */
  public void toggleGradient() {
    EYE.useGradients = !EYE.useGradients;
  }

  /**
   * Whether the eye is currently being drawn using gradients or solid colours. For more
   * information, see {@link #useGradient(boolean)}.
   *
   * @return {@link Boolean} <code>true</code> when the eye is drawn using gradients.
   * @author Griefed
   */
  public boolean isGradientActive() {
    return EYE.useGradients;
  }

  /**
   * Whether to use a divider to in-/decrement across the width of the Larson Scanner. If the
   * divider is being used, then the position of the eye will be updated with the result of the
   * width of the Scanner divided by the divider. Meaning: Smaller values increase the speed of the
   * eye whilst bigger values decrease it.
   *
   * <p>Increment: next position = current position + width of the Scanner / divider
   *
   * <p>Decrement: next position = current position - width of the Scanner / divider
   *
   * <p>Default setting: <code>true</code>
   *
   * @param useDivider {@link Boolean} Whether to in-/decrement using the divider. <code>true</code>
   *     to use it.
   * @author Griefed
   */
  public void useDivider(boolean useDivider) {
    EYE.useDivider = useDivider;
  }

  /**
   * Toggle the use of the divider. See {@link #useDivider(boolean)} for more information about the
   * divider and how it works.
   *
   * @author Griefed
   */
  public void toggleDivider() {
    EYE.useDivider = !EYE.useDivider;
  }

  /**
   * Whether the divider is currently being used. See {@link #useDivider(boolean)} for more
   * information about the divider and how it works.
   *
   * @return {@link Boolean} <code>true</code> if the divider is being used.
   * @author Griefed
   */
  public boolean isDividerActive() {
    return EYE.useDivider;
  }

  /**
   * Whether to use the Cylon-eye animation or Kitt-eye animation. If the Cylon-animation is chosen,
   * elements in the eye expand from the center outward. If the Kitt-animation is chosen, the eye
   * behaves as follows:
   *
   * <ul>
   *   <li>Left-to-right:
   *       <ul>
   *         <li>From the current position towards the right, each element is drawn. Each element is
   *             drawn with less alpha, leading to the last element, visually the first of the eye,
   *             being or almost being solid in colour.
   *         <li>When the eye leaves the left side, the illusion of the eye emerging is created by
   *             drawing the left most element of the elements already drawn from 0 to the width of
   *             the eye. As the eye leaves the play, the starting element increases in alpha, thus
   *             creating the illusion of the eye emerging from one spot.
   *         <li>When the eye enters the right side, the illusion of the elements gathering is
   *             created by drawing the brightest, most solid in colour, element at the absolute
   *             right, thus creating the illusion of the elements coalescing.
   *       </ul>
   *   <li>Right-to-left:
   *       <ul>
   *         <li>From the current position towards the left, each element is drawn.Each element is
   *             drawn with less alpha, leading to the last element, visually the first or the eye,
   *             being or almost being solid in colour.
   *         <li>When the eye leaves the right side, the illusion of the eye emerging is created by
   *             drawing the right most element of the elements already drawn from the width of the
   *             Larson Scanner to the width of the eye. As the eye leaves the end, the last element
   *             increases in alpha, thus creating the illusion of the eye emerging from one spot.
   *         <li>When the eye enters the left side, the illusion of the elements gathering is
   *             created by drawing the brightest, most solid in colour, element at the absolute
   *             left, thus creating the illusion of the elements coalescing.
   *       </ul>
   * </ul>
   *
   * @author Griefed
   * @param useCylonAnimation {@link Boolean} <code>true</code> to use the Cylon animation.
   */
  public void useCylonAnimation(boolean useCylonAnimation) {
    EYE.cylonAnimation = useCylonAnimation;
  }

  /**
   * Toggle whether the eye os to be animated using the Cylon-eye or Kitt-eye animation. For more
   * information, see {@link LarsonScanner#useCylonAnimation(boolean)}.
   *
   * @author Griefed
   */
  public void toggleCylonAnimation() {
    EYE.cylonAnimation = !EYE.cylonAnimation;
  }

  /**
   * Get whether the eye is currently being animated using the Cylon-eye or Kitt-eye animation. For
   * more information, see {@link LarsonScanner#useCylonAnimation(boolean)}.
   *
   * @author Griefed
   * @return {@link Boolean} <code>true</code> if the eye is being animated as a Cylon-eye.
   */
  public boolean isCylonAnimation() {
    return EYE.cylonAnimation;
  }

  /**
   * Set the enforcement of an aspect ratio. If the aspect ratio is being forced, each element will
   * be drawn with a 1:1 aspect ratio, turning ovals into circles and rectangles into squares. The
   * aspect ratio is being enforced by de-/increasing the height of the eye in the scanner to the
   * width of one element, resulting in a nice and sexy 1:1 ratio.
   *
   * <p>If you plan on using different scanner and eye background colours, you should make use of
   * this setting, otherwise the eye background colour will also prevail.
   *
   * <p>Default setting: <code>false</code>
   *
   * @param force {@link Boolean}
   * @author Griefed
   */
  public void forceAspectRatio(boolean force) {
    EYE.forceAspectRatio = force;
  }

  /**
   * Toggle the enforcement of an aspect ratio. See {@link #forceAspectRatio(boolean)} for more
   * information about an aspect ratio enforcement.
   *
   * @author Griefed
   */
  public void toggleAspectRatio() {
    EYE.forceAspectRatio = !EYE.forceAspectRatio;
  }

  /**
   * Whether the aspect ratio is currently being enforced. See {@link #forceAspectRatio(boolean)}
   * for more information about an aspect ratio enforcement.
   *
   * @return {@link Boolean} <code>true</code> if the aspect ratio is being enforced.
   * @author Griefed
   */
  public boolean isAspectRatioForced() {
    return EYE.forceAspectRatio;
  }

  /**
   * Set the fractions for the distribution of the colours along the gradients of each oval-shaped
   * element. Keep in mind, that the
   *
   * <ol>
   *   <li>fractions <strong>must</strong> range from 0.0f to 1.0f.
   *   <li>first fraction <strong>must</strong> be smaller than the second one.
   *   <li>first fraction <strong>must not</strong> be the same size as the second one
   *   <li>first fraction <strong>can</strong> be 0.0f, but not smaller
   *   <li>second fraction <strong>must</strong> be bigger than the first one
   *   <li>second fraction <strong>must not</strong> be 0.0f
   * </ol>
   *
   * <p>Default setting: <code>0.4f, 1.0f</code>
   *
   * @param fractionOne {@link Float} Fraction to distribute colour across a radial gradient.
   *     Ranging from 0.0f to 1.0f, smaller than <code>fractionTwo</code>.
   * @param fractionTwo {@link Float} Fraction to distribute colour across a radial gradient.
   *     Ranging from 0.0f to 1.0f, bigger than <code>fractionOne</code>.
   * @throws IllegalArgumentException if fractionOne is smaller than 0.0f, if fractionOne is bigger
   *     or equal to 1.0f, if fractionOne is bigger than fractionTwo, or if fractionTwo is bigger
   *     than 1.0f.
   * @author Griefed
   */
  public void setFractions(float fractionOne, float fractionTwo) throws IllegalArgumentException {

    if (fractionOne < 0.0f) {
      throw new IllegalArgumentException(
          "First fraction must not be negative. Specified " + fractionOne);

    } else if (fractionOne >= 1.0f) {
      throw new IllegalArgumentException(
          "First fraction must be smaller than 1.0f. Specified " + fractionOne);

    } else if (fractionOne > fractionTwo) {
      throw new IllegalArgumentException(
          "First fraction must be smaller than fraction two. Specified " + fractionOne);

    } else if (fractionTwo > 1.0f) {
      throw new IllegalArgumentException(
          "Second fraction must be bigger than the first, and smaller or equal to 1.0f. Specified "
              + fractionTwo);

    } else {
      EYE.fractions[0] = fractionOne;
      EYE.fractions[1] = fractionTwo;
    }
  }

  /**
   * Get the currently set fractions for use in radial gradient creation. See {@link
   * #setFractions(float, float)} for more information.
   *
   * @return {@link Float}-array containing the two fractions currently set.
   * @author Griefed
   */
  public float[] getFractions() {
    return EYE.fractions;
  }

  /**
   * Get the currently set alphas for the elements in the eye. See {@link #setAlphas(short[])} for
   * more information.
   *
   * @return {@link Short}-array containing the alpha-values with which the elements in the eye are
   *     being drawn.
   * @author Griefed
   */
  public short[] getAlphas() {
    return EYE.alphas;
  }

  /**
   * Set the alpha values for the elements in the eye, one for each element. The array must contain
   * exactly as many entries as there are number of elements currently being drawn in the eye.
   *
   * <p><Strong>Note:</Strong>
   *
   * <p>Alpha values are reset / automatically set / calculated when you change the number of
   * elements in your configuration. So, if you plan on using custom alpha values and a custom
   * amount of elements, make sure to update your alphas after changing the number of elements!
   *
   * <p>Depending on the currently set shape, these alpha values are used in different ways:
   *
   * <ul>
   *   <li>Oval: Radial gradients are generated, one for each element in the eye. The alpha is
   *       applied to the colour in said gradients, from the center of the element to the edge, with
   *       the alpha continually increasing. Outer elements frequently end up fading into
   *       transparency, especially if larger number of elements are being drawn.
   *   <li>Rectangular: Linear gradients are generated, one for each element in the eye. The alpha
   *       is applied from left-to-right at half the height of each element, for each element. For
   *       each element left-to-center the alpha decreases gradually. For each element
   *       center-to-right the alpha continually increases.
   * </ul>
   *
   * <p>Default setting: <code>100, 200, 255, 200, 100</code>
   *
   * @param alphas {@link Short}-array containing the alpha to draw an element with, one for each
   *     element.
   * @throws IllegalArgumentException if the amount of alphas is unequal to the current number of
   *     elements present in the eye.
   * @author Griefed
   */
  public void setAlphas(short @NotNull [] alphas) throws IllegalArgumentException {
    if (alphas.length != EYE.numberOfElements) {
      throw new IllegalArgumentException(
          "Alpha-array must contain exactly "
              + EYE.numberOfElements
              + " entries. Specified "
              + alphas.length);
    } else {
      EYE.alphas = alphas;
    }
  }

  /**
   * Get the currently set interval at wich the eye is being updated. For more information, see
   * {@link #setInterval(short)}.
   *
   * @return {@link Short} The interval at which the eye is updated.
   * @author Griefed
   */
  public short getInterval() {
    return EYE.interval;
  }

  /**
   * Set the interval in milliseconds at which to fire the timer of the eye. Every <code>n</code>
   * milliseconds the position of the eye gets updated and the eye redrawn. Smaller values therefor
   * increase the speed at wich the eye scrolls across the screen, whilst bigger values decrease it.
   *
   * <p>Default setting: <code>100</code>
   *
   * @param updateInterval {@link Short} the interval in milliseconds at which to update the eye.
   * @author Griefed
   */
  public void setInterval(short updateInterval) throws IllegalArgumentException {
    if (updateInterval < 1) {
      throw new IllegalArgumentException(
          "Interval must be greater than 0. Specified " + updateInterval);
    } else {
      EYE.interval = updateInterval;
      // eye.timer.setDelay(updateInterval);
    }
  }

  /**
   * Get the divider with which the position of the eye is currently being in-/decremented. For more
   * information on how the divider affects the eye, see {@link #useDivider(boolean)}.
   *
   * @return {@link Short} The divider with which the position of the eye is currently being
   *     in-/decremented.
   * @author Griefed
   */
  public short getDivider() {
    return EYE.divider;
  }

  /**
   * Set the divider with which the position of the eye is being in-/decremented. For more
   * information on how the divider affects the eye, see {@link #useDivider(boolean)}.
   *
   * <p>Default setting: <code>25</code>
   *
   * @param newStepDivider {@link Short} The new divider with which to in-/decrement the position of
   *     th eye.
   * @author Griefed
   */
  public void setDivider(short newStepDivider) throws IllegalArgumentException {
    if (newStepDivider < 1) {
      throw new IllegalArgumentException(
          "Divider must be greater than 0. Specified " + newStepDivider);
    } else {
      EYE.divider = newStepDivider;
    }
  }

  /**
   * Get the percentage of the width between rectangular elements.
   *
   * @return {@link Float} The percentage of the width between rectangular elements.
   * @author Griefed
   */
  public float getGapPercent() {
    return EYE.gapPercent;
  }

  /**
   * Set the width of the gap between rectangular shapes in percent. The gap between rectangular
   * shapes is <code>n%</code> of the width of one element.
   *
   * <p>Default setting: <code>25.0f</code>
   *
   * @param percentile {@link Float} Width of the gap between rectangular elements, in %.
   * @throws IllegalArgumentException if the specified percentage is smaller than 0.0f.
   * @author Griefed
   */
  public void setGapPercent(float percentile) throws IllegalArgumentException {
    if (percentile < 0.0f) {
      throw new IllegalArgumentException(
          "Gap percent must be a positive, non-negative, number. Specified " + percentile);
    } else {
      EYE.gapPercent = percentile;
      EYE.setNewEyeValues();
    }
  }

  /**
   * Get the partition divider which currently determines the width of the eye.
   *
   * @return {@link Double} The partition divider of the eye.
   * @author Griefed
   */
  public double getPartitionDivider() {
    return EYE.partitionDivider;
  }

  /**
   * The partition divider controls the width of the eye in the Larson Scanner. The total width of
   * the available area is divided by this value, and the resulting value is the total width of the
   * eye in which all elements will be drawn. The smaller the value, the bigger the eye and the
   * other way around.
   *
   * @param partitionDivider {@link Double} The new partition divider to set for the eye.
   * @throws IllegalArgumentException if the specified divider is smaller than or equal to 0.0D.
   * @author Griefed
   */
  public void setPartitionDivider(double partitionDivider) throws IllegalArgumentException {
    if (partitionDivider <= 0.0D) {
      throw new IllegalArgumentException(
          "Partition Divider must be bigger than 0.0D. Specified " + partitionDivider);
    } else {
      EYE.partitionDivider = partitionDivider;
      EYE.setNewEyeValues();
    }
  }

  /**
   * Pause the eye, freezing the animation.
   *
   * @author Griefed
   */
  public void pause() {
    EYE.pauseAnimation();
  }

  /**
   * Unpause the eye, continuing the animation.
   *
   * @author Griefed
   */
  public void play() {
    EYE.playAnimation();
  }

  /**
   * Whether the eye is currently being animated. <code>false</code> if the eye is stopped.
   *
   * @return {@link Boolean} Whether the eye animation is active. <code>false</code> if it is
   *     stopped.
   */
  public boolean isRunning() {
    return EYE.isRunning();
  }

  /**
   * Toggle the animation of the eye on/off.
   *
   * @author Griefed
   */
  public void togglePauseUnpause() {
    EYE.togglePauseUnpause();
  }

  /**
   * Get the integer indicating the last set rendering quality. See {@link #setQualityLow()}, {@link
   * #setQualityMedium()} and {@link #setQualityHigh()} for more information.
   *
   * @return {@link Integer} Number representation of the last set rendering quality.
   * @author Griefed
   */
  public int getQualitySetting() {
    return EYE.lastSetRenderingQuality;
  }

  /**
   * Set the rendering quality of the eye to high. Settings are:
   *
   * <ol>
   *   <li>RenderingHints.KEY_ANTIALIASING<br>
   *       RenderingHints.VALUE_ANTIALIAS_ON
   *   <li>RenderingHints.KEY_ALPHA_INTERPOLATION<br>
   *       RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
   *   <li>RenderingHints.KEY_COLOR_RENDERING<br>
   *       RenderingHints.VALUE_COLOR_RENDER_QUALITY
   *   <li>RenderingHints.KEY_INTERPOLATION<br>
   *       RenderingHints.VALUE_INTERPOLATION_BICUBIC
   *   <li>RenderingHints.KEY_RENDERING<br>
   *       RenderingHints.VALUE_RENDER_QUALITY
   * </ol>
   *
   * <p>Default setting: <code>low</code>
   *
   * @author Griefed
   */
  public void setQualityHigh() {
    EYE.setRenderingQualityHigh();
  }

  /**
   * Set the rendering quality of the eye to medium. Settings are:
   *
   * <ol>
   *   <li>RenderingHints.KEY_ANTIALIASING<br>
   *       RenderingHints.VALUE_ANTIALIAS_DEFAULT
   *   <li>RenderingHints.KEY_ALPHA_INTERPOLATION<br>
   *       RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT
   *   <li>RenderingHints.KEY_COLOR_RENDERING<br>
   *       RenderingHints.VALUE_COLOR_RENDER_DEFAULT
   *   <li>RenderingHints.KEY_INTERPOLATION<br>
   *       RenderingHints.VALUE_INTERPOLATION_BILINEAR
   *   <li>RenderingHints.KEY_RENDERING<br>
   *       RenderingHints.VALUE_RENDER_DEFAULT
   * </ol>
   *
   * <p>Default setting: <code>low</code>
   *
   * @author Griefed
   */
  public void setQualityMedium() {
    EYE.setRenderingQualityMedium();
  }

  /**
   * Set the rendering quality of the eye to low. Settings are:
   *
   * <ol>
   *   <li>RenderingHints.KEY_ANTIALIASING<br>
   *       RenderingHints.VALUE_ANTIALIAS_OFF
   *   <li>RenderingHints.KEY_ALPHA_INTERPOLATION<br>
   *       RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED
   *   <li>RenderingHints.KEY_COLOR_RENDERING<br>
   *       RenderingHints.VALUE_COLOR_RENDER_SPEED
   *   <li>RenderingHints.KEY_INTERPOLATION<br>
   *       RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
   *   <li>RenderingHints.KEY_RENDERING<br>
   *       RenderingHints.VALUE_RENDER_SPEED
   * </ol>
   *
   * <p>Default setting: <code>low</code>
   *
   * @author Griefed
   */
  public void setQualityLow() {
    EYE.setRenderingQualityLow();
  }

  /**
   * Set the configuration of the Larson Scanner with values from the given config.
   *
   * @param config {@link ScannerConfig} The configuration from which to set the Larson Scanner
   *     values.
   * @throws IllegalArgumentException if any of the configured values is invalid.
   * @author Griefed
   */
  public void loadConfig(@NotNull ScannerConfig config) throws IllegalArgumentException {
    if (config.qualitySetting == ScannerConfig.LOW) {
      setQualityLow();
    } else if (config.qualitySetting == ScannerConfig.MEDIUM) {
      setQualityMedium();
    } else {
      setQualityHigh();
    }

    setNumberOfElements(config.numberOfElements);
    setAlphas(config.alphas);
    setEyeColours(config.eyeColours);
    setInterval(config.interval);
    setDivider(config.divider);
    setFractions(config.fractions[0], config.fractions[1]);
    setGapPercent(config.gapPercent);
    setPartitionDivider(config.partitionDivider);

    forceAspectRatio(config.forceAspectRatio);
    drawOval(config.ovalShaped);
    useGradient(config.useGradients);
    useDivider(config.useDivider);
    useCylonAnimation(config.cylonAnimation);
    setBackground(config.scannerBackgroundColour);
    EYE.setBackground(config.eyeBackgroundColour);
  }

  /**
   * Load the default values into the Larson Scanner and eye, resetting it. See {@link
   * #LarsonScanner()} for more information on what the defaults are.
   *
   * @author Griefed
   */
  public void loadDefaults() {
    setQualityLow();
    setNumberOfElements((byte) 5);
    setAlphas(new short[] {100, 200, 255, 200, 100});
    setEyeColour(DEFAULT_EYE_COLOUR);
    setInterval((short) 100);
    setDivider((short) 25);
    setFractions(0.4f, 1.0f);
    setGapPercent(25.0f);
    setPartitionDivider(5.0D);
    forceAspectRatio(false);
    drawOval(true);
    useGradient(true);
    useDivider(true);
    useCylonAnimation(true);
    setBackground(DEFAULT_BACKGROUND_COLOUR);
    EYE.setBackground(DEFAULT_BACKGROUND_COLOUR);
  }

  /**
   * Get the current Larson Scanner configuration as a ScannerConfig.
   *
   * @return {@link ScannerConfig} The current configuration of the Larson Scanner.
   * @author Griefed
   */
  public ScannerConfig getCurrentConfig() {
    return new ScannerConfig(
        EYE.lastSetRenderingQuality,
        EYE.alphas,
        EYE.interval,
        EYE.divider,
        EYE.numberOfElements,
        EYE.fractions,
        EYE.gapPercent,
        EYE.partitionDivider,
        EYE.forceAspectRatio,
        EYE.ovalShaped,
        EYE.useGradients,
        EYE.useDivider,
        EYE.cylonAnimation,
        EYE.eyeColours,
        this.getBackground(),
        EYE.getBackground());
  }

  /**
   * Convenience-class with which to change or acquire the configuration of the LarsonScanner.
   *
   * @author Griefed
   */
  public static class ScannerConfig {

    /** Set the rendering quality of the Larson Scanner to low settings. */
    public static final int LOW = 0;

    /** Set the rendering quality of the Larson Scanner to medium settings. */
    public static final int MEDIUM = 1;

    /** Set the rendering quality of the Larson Scanner to high settings. */
    public static final int HIGH = 2;

    private final float[] fractions = {0.4f, 1.0f};
    private Color[] eyeColours = {
      DEFAULT_EYE_COLOUR,
      DEFAULT_EYE_COLOUR,
      DEFAULT_EYE_COLOUR,
      DEFAULT_EYE_COLOUR,
      DEFAULT_EYE_COLOUR
    };
    private Color scannerBackgroundColour = DEFAULT_BACKGROUND_COLOUR;
    private Color eyeBackgroundColour = DEFAULT_BACKGROUND_COLOUR;
    private float gapPercent = 25.0f;
    private int qualitySetting = LOW;
    private short[] alphas = {100, 200, 255, 200, 100};
    private short interval = 100;
    private short divider = 25;
    private byte numberOfElements = 5;
    private double partitionDivider = 5.0D;
    private boolean forceAspectRatio = false;
    private boolean ovalShaped = true;
    private boolean useGradients = true;
    private boolean useDivider = true;
    private boolean cylonAnimation = true;

    /**
     * Create a Larson Scanner configuration with default values.
     *
     * @author Griefed
     */
    public ScannerConfig() {}

    /**
     * Create a Larson Scanner configuration with custom settings.
     *
     * @param qualitySetting {@link Integer} The quality preset to use. Either {@link #LOW}, {@link
     *     #MEDIUM} or {@link #HIGH}. For more information, see {@link
     *     LarsonScanner#setQualityLow()}. {@link LarsonScanner#setQualityMedium()} and {@link
     *     LarsonScanner#setQualityHigh()}.
     * @param alphaValues {@link Short}-array of alpha values for the elements in the eye. Length of
     *     array must equal number of elements in the eye. For more information, see {@link
     *     LarsonScanner#setAlphas(short[])}.
     * @param intervalInMillis {@link Short} The interval in milliseconds at which to update and
     *     redraw the eye. For more information, see {@link LarsonScanner#setInterval(short)}.
     * @param divider {@link Short} The divider with which to in-/decrement the position of the eye.
     *     For more information, see {@link LarsonScanner#useGradient(boolean)}.
     * @param numberOfElements {@link Byte} The number of elements to draw in the eye. For more
     *     information, see {@link LarsonScanner#setNumberOfElements(byte)}.
     * @param fractions {@link Float}-array of fractions to use for radial gradient colour
     *     distribution. For more information, see {@link LarsonScanner#setFractions(float, float)}.
     * @param gapPercent {@link Float} The percentage of the width of the gap between two
     *     rectangular elements. For more information, see {@link
     *     LarsonScanner#setGapPercent(float)}.
     * @param partitionDivider {@link Double} The number with which to divide the width of the
     *     Larson Scanner to set the width of the eye. For more information, see {@link
     *     LarsonScanner#setPartitionDivider(double)}.
     * @param forceAspectRatio {@link Boolean} Whether to force an aspect ratio on the eye. For more
     *     information, see {@link LarsonScanner#forceAspectRatio(boolean)}.
     * @param ovalShaped {@link Boolean} Whether to draw the elements using oval or rectangular
     *     shapes. For more information, see {@link LarsonScanner#drawOval(boolean)}.
     * @param useGradients {@link Boolean} Whether to draw the eye using gradients. For more
     *     information, see {@link LarsonScanner#useGradient(boolean)}.
     * @param useDivider {@link Boolean} Whether to use a divider to in-/decrement across the width
     *     of the Larson Scanner. For more information, see {@link
     *     LarsonScanner#useDivider(boolean)}.
     * @param cylonAnimation {@link Boolean} Whether to animate the Larson Scanner as a Cylon-eye or
     *     Kitt-eye. For more information, see {@link LarsonScanner#useCylonAnimation(boolean)}.
     * @param eyeColours {@link Color}-array containing one colour for each element in the eye. For
     *     more information, see {@link LarsonScanner#setEyeColours(Color[])}.
     * @param scannerBackgroundColour {@link Color} The background colour of the Larson Scanner.
     * @param eyeBackgroundColour {@link Color} The background colour of the eye in the Larson
     *     Scanner.
     * @author Griefed
     */
    public ScannerConfig(
        int qualitySetting,
        short[] alphaValues,
        short intervalInMillis,
        short divider,
        byte numberOfElements,
        float[] fractions,
        float gapPercent,
        double partitionDivider,
        boolean forceAspectRatio,
        boolean ovalShaped,
        boolean useGradients,
        boolean useDivider,
        boolean cylonAnimation,
        @NotNull Color[] eyeColours,
        @NotNull Color scannerBackgroundColour,
        @NotNull Color eyeBackgroundColour)
        throws IllegalArgumentException {

      this.setQualitySetting(qualitySetting);
      this.setNumberOfElements(numberOfElements);
      this.setAlphas(alphaValues);
      this.setEyeColours(eyeColours);
      this.setInterval(intervalInMillis);
      this.setDivider(divider);
      this.setGapPercent(gapPercent);
      this.setPartitionDivider(partitionDivider);
      this.setFractions(fractions[0], fractions[1]);

      this.forceAspectRatio = forceAspectRatio;
      this.ovalShaped = ovalShaped;
      this.useGradients = useGradients;
      this.useDivider = useDivider;
      this.scannerBackgroundColour = scannerBackgroundColour;
      this.eyeBackgroundColour = eyeBackgroundColour;
      this.cylonAnimation = cylonAnimation;
    }

    /**
     * Get this configurations' rendering quality setting.
     *
     * @return {@link Integer} Integer representation of the rendering quality setting.
     * @author Griefed
     */
    public int getQualitySetting() {
      return qualitySetting;
    }

    /**
     * Set this configurations' rendering quality setting. Either {@link #LOW}, {@link #MEDIUM} or
     * {@link #HIGH}. For more information, see {@link LarsonScanner#setQualityLow()}.
     *
     * @param qualitySetting {@link Integer} Integer representation of the rendering quality
     *     setting. Either {@link #LOW}, {@link #MEDIUM} or {@link #HIGH}.
     * @author Griefed
     */
    public void setQualitySetting(int qualitySetting) throws IllegalArgumentException {
      if (qualitySetting < 0 || qualitySetting > 2) {
        throw new IllegalArgumentException(
            "Quality setting must be 0, 1 or 2. Specified " + qualitySetting);
      } else {
        this.qualitySetting = qualitySetting;
      }
    }

    /**
     * Get this configurations' alphas-array.
     *
     * @return {@link Short}-array containing the alphas of this configuration.
     * @author Griefed
     */
    public short[] getAlphas() {
      return alphas;
    }

    /**
     * Set this configurations' alpha-array. For more information, see {@link
     * LarsonScanner#setAlphas(short[])}.
     *
     * @param alphas {@link Short}-array containing the new alphas for this configuration.
     * @author Griefed
     */
    public void setAlphas(short @NotNull [] alphas) throws IllegalArgumentException {
      if (alphas.length != numberOfElements) {
        throw new IllegalArgumentException(
            "Alpha-array must contain exactly "
                + numberOfElements
                + " entries. Specified "
                + alphas.length);
      } else {
        this.alphas = alphas;
      }
    }

    /**
     * Get this configurations' interval.
     *
     * @return {@link Short} The interval of this configuration.
     * @author Griefed
     */
    public short getInterval() {
      return interval;
    }

    /**
     * Set this configurations' interval. For more information, see {@link
     * LarsonScanner#setInterval(short)}.
     *
     * @param intervalInMillis {@link Short} Interval for this configuration.
     * @author Griefed
     */
    public void setInterval(short intervalInMillis) throws IllegalArgumentException {
      if (intervalInMillis < 1) {
        throw new IllegalArgumentException(
            "Interval must be greater than 0. Specified " + intervalInMillis);
      } else {
        this.interval = intervalInMillis;
      }
    }

    /**
     * Get this configurations' divider.
     *
     * @return {@link Short} This configurations' divider.
     * @author Griefed
     */
    public short getDivider() {
      return divider;
    }

    /**
     * Set this configurations' divider. For more information on how the divider affects the eye,
     * see {@link LarsonScanner#useDivider(boolean)}.
     *
     * @param divider {@link Short} Divider for this configuration.
     * @author Griefed
     */
    public void setDivider(short divider) throws IllegalArgumentException {
      if (divider < 1) {
        throw new IllegalArgumentException("Divider must be greater than 0. Specified " + divider);
      } else {
        this.divider = divider;
      }
    }

    /**
     * Get this configurations' number of elements in th eye.
     *
     * @return {@link Byte} This configurations' number of elements in the eye.
     * @author Griefed
     */
    public byte getNumberOfElements() {
      return numberOfElements;
    }

    /**
     * Set this configurations' number of elements in the eye. For more information, see {@link
     * LarsonScanner#setNumberOfElements(byte)}.
     *
     * @param numberOfElements {@link Byte} Number of elements for this configuration.
     * @author Griefed
     */
    public void setNumberOfElements(byte numberOfElements) throws IllegalArgumentException {
      if (numberOfElements < 1) {
        throw new IllegalArgumentException(
            "Number of elements must be greater than zero. Specified " + numberOfElements);
      } else if ((numberOfElements & 1) == 0) {
        throw new IllegalArgumentException(
            "Number of elements must be an odd number. Specified " + numberOfElements);
      } else {
        this.numberOfElements = numberOfElements;
      }
    }

    /**
     * Get this configurations' fractions.
     *
     * @return {@link Float}-array containing this configurations' fractions.
     * @author Griefed
     */
    public float[] getFractions() {
      return fractions;
    }

    /**
     * Set this configurations' fractions. For more information, see {@link
     * LarsonScanner#setFractions(float, float)}.
     *
     * @param fractionOne {@link Float} First fraction for colour distribution for this
     *     configuration.
     * @param fractionTwo {@link Float} Second fraction for colour distribution for this
     *     configuration.
     * @author Griefed
     */
    public void setFractions(float fractionOne, float fractionTwo) throws IllegalArgumentException {
      if (fractionOne < 0.0f) {
        throw new IllegalArgumentException(
            "First fraction must not be negative. Specified " + fractionOne);

      } else if (fractionOne >= 1.0f) {
        throw new IllegalArgumentException(
            "First fraction must be smaller than 1.0f. Specified " + fractionOne);

      } else if (fractionOne > fractionTwo) {
        throw new IllegalArgumentException(
            "First fraction must be smaller than fraction two. Specified " + fractionOne);

      } else if (fractionTwo > 1.0f) {
        throw new IllegalArgumentException(
            "Second fraction must be bigger than the first, and smaller or equal to 1.0f. Specified "
                + fractionTwo);

      } else {
        this.fractions[0] = fractionOne;
        this.fractions[1] = fractionTwo;
      }
    }

    /**
     * Get this configurations' gap percentage.
     *
     * @return {@link Float} This configurations' gap percentage.
     * @author Griefed
     */
    public float getGapPercent() {
      return gapPercent;
    }

    /**
     * Set this configurations' gap percentage. For more information, see {@link
     * LarsonScanner#setGapPercent(float)}.
     *
     * @param gapPercent {@link Float} Gap percentage for this configuration.
     * @author Griefed
     */
    public void setGapPercent(float gapPercent) throws IllegalArgumentException {
      if (gapPercent < 0.0f) {
        throw new IllegalArgumentException(
            "Gap percent must be a positive, non-negative, number. Specified " + gapPercent);
      } else {
        this.gapPercent = gapPercent;
      }
    }

    /**
     * Get this configurations' partition divider.
     *
     * @return {@link Double} This configurations' partition divider.
     * @author Griefed
     */
    public double getPartitionDivider() {
      return partitionDivider;
    }

    /**
     * Set this configurations' partition divider. For more information, see {@link
     * LarsonScanner#setPartitionDivider(double)}.
     *
     * @param partitionDivider {@link Double} Partition divider for this configuration.
     * @throws IllegalArgumentException if the specified divider is smaller than or equal to 0.0D.
     */
    public void setPartitionDivider(double partitionDivider) throws IllegalArgumentException {
      if (partitionDivider <= 0.0D) {
        throw new IllegalArgumentException(
            "Partition Divider must be bigger than 0.0D. Specified " + partitionDivider);
      } else {
        this.partitionDivider = partitionDivider;
      }
    }

    /**
     * Whether the aspect ratio is enforced in this configuration.
     *
     * @return {@link Boolean} Whether the aspect ratio is enforced in this configuration.
     * @author Griefed
     */
    public boolean isAspectRatioForced() {
      return forceAspectRatio;
    }

    /**
     * Set whether this configurations' aspect ratio enforcing should be enabled. For more
     * information, see {@link LarsonScanner#forceAspectRatio(boolean)}.
     *
     * @param forceAspectRatio {@link Boolean} Whether this configuration should enforce aspect
     *     ratio.
     * @author Griefed
     */
    public void setForceAspectRatio(boolean forceAspectRatio) {
      this.forceAspectRatio = forceAspectRatio;
    }

    /**
     * Whether the elements for this configuration are drawn as ovals or rectangles.
     *
     * @return {@link Boolean} Whether elements are drawn as ovals or rectangles.
     * @author Griefed
     */
    public boolean isShapeOval() {
      return ovalShaped;
    }

    /**
     * Set whether the elements for this configuration should be drawn as ovals or rectangles. For
     * more information, see {@link LarsonScanner#drawOval(boolean)}.
     *
     * @param ovalShaped Whether the elements for this configuration should be drawn as ovals.
     * @author Griefed
     */
    public void setOvalShaped(boolean ovalShaped) {
      this.ovalShaped = ovalShaped;
    }

    /**
     * Whether the elements for this configuration are drawn using gradients.
     *
     * @return {@link Boolean} Whether elements are drawn using gradients.
     * @author Griefed
     */
    public boolean isGradientActive() {
      return useGradients;
    }

    /**
     * Set whether the elements for this configuration should be drawn using gradients. For more
     * information, see {@link LarsonScanner#useGradient(boolean)}.
     *
     * @param useGradients {@link Boolean} Whether the elements for this configuration should be
     *     drawn using gradients.
     * @author Griefed
     */
    public void setUseGradients(boolean useGradients) {
      this.useGradients = useGradients;
    }

    /**
     * Whether the position of this configuration in-/decrements using a divider.
     *
     * @return {@link Boolean} Whether the position in-/decrements using a divider.
     * @author Griefed
     */
    public boolean isDividerActive() {
      return useDivider;
    }

    /**
     * Set whether the position for this configuration in-/decrements using a divider. For more
     * information, see {@link LarsonScanner#useDivider(boolean)}.
     *
     * @param useDivider {@link Boolean} Whether the position for this configuration should be
     *     in-/decrement using a divider.
     * @author Griefed
     */
    public void setUseDivider(boolean useDivider) {
      this.useDivider = useDivider;
    }

    /**
     * Whether this configuration animates the eye as a Cylon-eye, or Kitt-eye.
     *
     * @author Griefed
     * @return {@link Boolean} Whether this configuration animates the eye as a Cylon-eye, or
     *     Kitt-eye.
     */
    public boolean isCylonAnimation() {
      return cylonAnimation;
    }

    /**
     * Set whether this configuration should animate the eye as a Cylon-eye, or Kitt-eye. For more
     * information, see {@link LarsonScanner#useCylonAnimation(boolean)}.
     *
     * @author Griefed
     * @param cylonAnimation {@link Boolean} Whether this configuration animates the eye as a
     *     Cylon-eye, or Kitt-eye.
     */
    public void setCylonAnimation(boolean cylonAnimation) {
      this.cylonAnimation = cylonAnimation;
    }

    /**
     * Get this configurations' eye colours.
     *
     * @return {@link Color}-array containing the colours for each element in the eye of this
     *     configuration.
     * @author Griefed
     */
    public Color[] getEyeColours() {
      return eyeColours;
    }

    /**
     * Set the colours for each element in the eye for this configuration. For more information, see
     * {@link LarsonScanner#setEyeColours(Color[])}.
     *
     * @param eyeColours {@link Color}-array containing the colours for each element in this
     *     configurations' eye.
     * @author Griefed
     */
    public void setEyeColours(Color @NotNull [] eyeColours) throws IllegalArgumentException {
      if (eyeColours.length != numberOfElements) {
        throw new IllegalArgumentException(
            "Color-array must contain exactly "
                + numberOfElements
                + " entries. Specified "
                + eyeColours.length);
      } else {
        this.eyeColours = eyeColours;
      }
    }

    /**
     * Get this configurations' background colour for the Larson Scanner.
     *
     * @return {@link Color} This configurations' background colour for the Larson Scanner.
     * @author Griefed
     */
    public Color getScannerBackgroundColour() {
      return scannerBackgroundColour;
    }

    /**
     * Set the background colour for the Larson Scanner for this configuration.
     *
     * @param scannerBackgroundColour {@link Color} The background color for the Larson Scanner.
     * @author Griefed
     */
    public void setScannerBackgroundColour(@NotNull Color scannerBackgroundColour) {
      this.scannerBackgroundColour = scannerBackgroundColour;
    }

    /**
     * Set the background colour for the eye of the Larson Scanner of this configuration.
     *
     * @return {@link Color} This configurations' background colour for the eye.
     * @author Griefed
     */
    public Color getEyeBackgroundColour() {
      return eyeBackgroundColour;
    }

    /**
     * Set the background color for the eye of the Larson Scanner for this configuration.
     *
     * @param eyeBackgroundColour {@link Color} The background colour for the eye of the Larson
     *     Scanner of this configuration.
     * @author Griefed
     */
    public void setEyeBackgroundColour(@NotNull Color eyeBackgroundColour) {
      this.eyeBackgroundColour = eyeBackgroundColour;
    }
  }

  /**
   * The heart and soul of the Larson Scanner, the eye. This is the element which is being drawn and
   * animated within the panel of the LarsonScanner itself.
   *
   * @author Griefed
   */
  private class Eye extends JComponent implements Runnable {

    private final RenderingHints renderingHints =
        new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    // private final Timer timer;
    private final float[] fractions = {0.4f, 1.0f};
    private volatile boolean paused = true;
    private Color[] eyeColours = {
      DEFAULT_EYE_COLOUR,
      DEFAULT_EYE_COLOUR,
      DEFAULT_EYE_COLOUR,
      DEFAULT_EYE_COLOUR,
      DEFAULT_EYE_COLOUR
    };
    private float gapPercent = 25.0f;
    private double width;
    private double height;
    private double elementWidth;
    private double partition;
    private double gapWidth;
    private double totalGapWidth;
    private double halfOfTotalGapWidth;
    private double partitionDivider = 5.0D;
    private int lastSetRenderingQuality = 0;
    private short[] alphas = {100, 200, 255, 200, 100};
    private short p;
    private short interval = 100;
    private short divider = 25;
    private byte numberOfElements = 5;
    private boolean increasePosition = true;
    private boolean forceAspectRatio = false;
    private boolean ovalShaped = true;
    private boolean useGradients = true;
    private boolean useDivider = true;
    private boolean cylonAnimation = true;

    /**
     * Default constructor for our eye, setting the background colour to black, the rendering
     * quality to low and the interval of the timer to a default value of 100ms.
     *
     * @author Griefed
     */
    public Eye() {
      super();
      setDoubleBuffered(true);
      setBackground(DEFAULT_BACKGROUND_COLOUR);
      setRenderingQualityLow();
    }

    /**
     * Convenience constructor for an eye. This allows you to set the interval in ms at which the
     * eye is being drawn.
     *
     * @param updateInterval {@link Short} Interval in milliseconds at which to scroll.
     * @author Griefed
     */
    public Eye(short updateInterval) {
      super();
      this.interval = updateInterval;
      setDoubleBuffered(true);
      setBackground(DEFAULT_BACKGROUND_COLOUR);
      setRenderingQualityLow();
    }

    /**
     * Convenience constructor for an eye. This allows you to set the interval in ms at which the
     * eye is being drawn as well as the background of the eye.
     *
     * @param updateInterval {@link Short} Interval in milliseconds at which to scroll.
     * @param backgroundColor {@link Color} The background colour for the scanner and the eye.
     * @author Griefed
     */
    public Eye(short updateInterval, @NotNull Color backgroundColor) {
      super();
      this.interval = updateInterval;
      setDoubleBuffered(true);
      setBackground(backgroundColor);
      setRenderingQualityLow();
    }

    /**
     * Convenience constructor for an eye. This allows you to set the interval in ms at which the
     * eye is being drawn, the background of the eye, as well as the colour of the eye.
     *
     * @param updateInterval {@link Short} Interval in milliseconds at which to scroll.
     * @param backgroundColor {@link Color} The background colour for the scanner and the eye.
     * @param eyeColor {@link Color} The color of the eye.
     * @author Griefed
     */
    public Eye(short updateInterval, @NotNull Color backgroundColor, @NotNull Color eyeColor) {
      super();
      this.interval = updateInterval;
      setDoubleBuffered(true);
      setEyeColour(eyeColor);
      setBackground(backgroundColor);
      setRenderingQualityLow();
    }

    /**
     * Animate the eye! This method gets called after the thread is created in the constructor of
     * the parent {@link LarsonScanner} and started from there.
     *
     * <p>By setting <code>paused</code> to either true or false you can pause or unpause the
     * animation respectively.
     *
     * <p>If the animation is not paused, the position of the eye gets updated by calling {@link
     * #updatePosition()} and then the eye gets drawn.
     *
     * @author Griefed
     */
    @Override
    public void run() {
      //noinspection InfiniteLoopStatement
      while (true) {
        try {
          //noinspection BusyWait
          Thread.sleep(EYE.interval);

          if (paused) {
            synchronized (this) {
              while (paused) wait();
            }
          }
        } catch (InterruptedException ignored) {
        }
        EYE.updatePosition();
        EYE.repaint();
      }
    }

    /**
     * Draw the eye! Depending on whether oval shape is selected or gradients are to be used, the
     * eye is drawn in different ways. However, all draw calls come from here. I guess you could say
     * that this is the heart and soul of the eye itself. The eyeball, mayhaps? For details on how
     * each animation behaves, see {@link LarsonScanner#useCylonAnimation(boolean)}.
     *
     * @param g the <code>Graphics</code> object to protect
     * @author Griefed
     */
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      updateValues();

      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHints(renderingHints);

      byte startY = 0;

      g2d.setColor(this.getBackground());
      g2d.fillRect(0, 0, (int) width, (int) height);
      //g2d.drawRect(0, 0, (int) width, (int) height);

      if (ovalShaped) {

        if (cylonAnimation) {

          drawCylonOval(g2d, startY);

        } else {

          drawKittOval(g2d, startY);
        }

      } else {

        if (cylonAnimation) {

          drawCylonRect(g2d, startY);

        } else {

          drawKittRect(g2d, startY);
        }
      }
      g2d.drawRect(0, 0, (int) width, (int) height);
      Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Pause the animation of the eye.
     *
     * @author Griefed
     */
    private synchronized void pauseAnimation() {
      paused = true;
    }

    /**
     * Continue the animation of the eye.
     *
     * @author Griefed
     */
    private synchronized void playAnimation() {
      paused = false;
      notify();
    }

    /**
     * Toggle the current state of the animation.
     *
     * <p>If the animation is paused, then invoking this will unpause it.
     *
     * <p>If the animation is not paused, then invoking this will pause it.
     *
     * @author Griefed
     */
    private synchronized void togglePauseUnpause() {
      paused = !paused;

      if (!paused) {
        notify();
      }
    }

    /**
     * Get the current status of the animation, whether it is paused or not.
     *
     * @return {@link Boolean} Status of the animation. <code>true</code> if it is paused, false
     *     otherwise.
     * @author Griefed
     */
    private synchronized boolean isRunning() {
      return paused;
    }

    /**
     * Set the rendering quality with which the eye is being drawn to high.
     *
     * @author Griefed
     */
    private void setRenderingQualityHigh() {
      lastSetRenderingQuality = 2;
      renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      renderingHints.put(
          RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

      renderingHints.put(
          RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

      renderingHints.put(
          RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

      renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    /**
     * Set the rendering quality with which the eye is being drawn to medium.
     *
     * @author Griefed
     */
    private void setRenderingQualityMedium() {
      lastSetRenderingQuality = 1;
      renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);

      renderingHints.put(
          RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

      renderingHints.put(
          RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);

      renderingHints.put(
          RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

      renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
    }

    /**
     * Set the rendering quality with which the eye is being drawn to low. Default with which a new
     * eye is instantiated.
     *
     * @author Griefed
     */
    private void setRenderingQualityLow() {
      lastSetRenderingQuality = 0;
      renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

      renderingHints.put(
          RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

      renderingHints.put(
          RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);

      renderingHints.put(
          RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

      renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    }

    /**
     * Set new colours for each element in the eye.
     *
     * @param colours {@link Color}-array consisting one colour for each element in the eye.
     * @author Griefed
     */
    private void setEyeColours(@NotNull Color @NotNull [] colours) {
      eyeColours = colours;
    }

    /**
     * Set the same colour for every element in the eye.
     *
     * @param color {@link Color} The color to set for each element in the eye.
     * @author Griefed
     */
    private void setEyeColour(@NotNull Color color) {
      Color[] newColours = new Color[numberOfElements];
      for (int i = 0; i < numberOfElements; i++) {
        newColours[i] = color;
      }
      eyeColours = newColours;
    }

    /**
     * Update the width, height, partitioning and element width.
     *
     * <p>If the aspect ratio is being enforced, then the height of the eye are set to the width of
     * one element, resulting in a 1:1 aspect ratio.<br>
     * Otherwise, the width and height of the eye are set to the width and height of the
     * encompassing Larson Scanner.
     *
     * <p>If a change in width or height was detected, the size of the eye, the partitioning and the
     * element width are updated.
     *
     * @return {@link Boolean} <code>true</code> if anything was updated.
     * @author Griefed
     */
    private boolean updateValues() {
      boolean updated = false;

      if (forceAspectRatio) {

        if (width != LarsonScanner.this.getWidth()
            || height != partition / (double) numberOfElements) {

          width = LarsonScanner.this.getWidth();
          height = partition / (double) numberOfElements;
          updated = true;
        }

      } else {

        if (width != LarsonScanner.this.getWidth()) {

          width = LarsonScanner.this.getWidth();
          updated = true;
        }

        if (height != LarsonScanner.this.getHeight()) {

          height = LarsonScanner.this.getHeight();
          updated = true;
        }
      }

      if (updated) {

        setNewEyeValues();
      }

      return updated;
    }

    /**
     * Set the new values for the eyes
     * <li>Size with width and height
     * <li>partitioning of the eye
     * <li>width of a single element in the eye
     * <li>width of the pag between two elements when drawing as rectangles
     * <li>total width of all gaps when drawing as rectangles
     * <li>half of the total width of all gaps when drawing as rectangles
     *
     * @author Griefed
     */
    private void setNewEyeValues() {
      setSize((int) width, (int) height);

      // The eye itself is to be a fifth of the whole width
      partition = width / partitionDivider;
      elementWidth = partition / (double) numberOfElements;

      gapWidth = elementWidth / 100.0D * gapPercent;
      totalGapWidth = gapWidth * (numberOfElements - 2);
      halfOfTotalGapWidth = totalGapWidth / 2.0D;
    }

    /**
     * Draw our elements in oval shape. If <code>useGradient</code> is set, then gradients are used
     * for painting, otherwise our ovals are painted with solid colours. For details on how this
     * animation behaves, see {@link LarsonScanner#useCylonAnimation(boolean)}.
     *
     * @param g2d {@link Graphics2D} to fill and draw with.
     * @param startY {@link Byte} The play of an element along the Y-axis.
     * @author Griefed
     */
    private void drawCylonOval(@NotNull Graphics2D g2d, byte startY) {

      for (byte element = 0; element < numberOfElements; element++) {

        double startOfElement = calcCylonOvalStart(element);

        if (useGradients) {
          g2d.setPaint(
              cylonRadialGradient(element, getCenter(startOfElement), eyeColours[element]));
        } else {
          g2d.setColor(eyeColours[element]);
        }

        g2d.fillOval((int) startOfElement, startY, (int) elementWidth, (int) height);
      }
    }

    /**
     * Calculate the Y-coordinate of the center of the element currently being drawn.
     *
     * <p>Divide the width of an element by two and add that to the play of the current element
     * being drawn.
     *
     * @param start {@link Double} The play point of the element currently being drawn.
     * @return {@link Double} The Y-coordinate of the center of the element currently being drawn.
     * @author Griefed
     */
    private double getCenter(double start) {
      return start + elementWidth / 2.0D;
    }

    /**
     * Calculate the play of an element in oval shape. Steps are:
     *
     * <ul>
     *   <li>Multiply the width of one element with the number of elements in the eye. Divide by two
     *   <li>multiply the width of one element with the number of the element currently being drawn
     *       (0 to total number of elements in the eye)
     *   <li>add the values from above together
     *   <li>subtract the above value from the current position in the eye being drawn at
     * </ul>
     *
     * <p>The element width multiplied with the number of elements, then divided by two, gives the
     * half of the total width of the eye itself. Added to that the width of one element, multiplied
     * with the number of the current element being drawn in the eye, and we get the play of the
     * eye we are currently drawing. To put this into relation along the width of the whole Larson
     * Scanner, we need to subtract that value from the current position along the Larson Scanners
     * total width.
     *
     * @param element {@link Byte} The number of the currently being drawn element, ranging from 0
     *     to the amount of elements in the eye.
     * @return {@link Double} The X-coordinate where the current element starts.
     * @author Griefed
     */
    private double calcCylonOvalStart(byte element) {
      return p
          - (elementWidth * (double) numberOfElements / 2.0D)
          + elementWidth * (double) element;
    }

    /**
     * Create the radial gradient for the currently being drawn, oval-shaped, element. Radial
     * gradients extend from the 2D center point of the current element to the height of one
     * element.
     *
     * @param element {@link Byte} The number of the element currently being drawn.
     * @param centerX {@link Double} The X-coordinate of the center of the element currently being
     *     drawn.
     * @param color {@link Color} The color of the element currently being drawn.
     * @return {@link RadialGradientPaint} A radial gradient for the currently being drawn,
     *     oval-shaped, element.
     * @author Griefed
     */
    private @NotNull RadialGradientPaint cylonRadialGradient(
        byte element, double centerX, @NotNull Color color) {

      Color[] colors = {colorWithAlpha(alphas[element], color), getBackground()};
      return new RadialGradientPaint(
          new Point((int) centerX, (int) (height / 2)), (float) (0.5f * height), fractions, colors);
    }

    /**
     * Draw our element in rectangular shape. If <code>useGradient</code> is set, then gradients are
     * used for painting, otherwise our rectangles are painted with solid colours. For details on
     * how this animation behaves, see {@link LarsonScanner#useCylonAnimation(boolean)}.
     *
     * @param g2d {@link Graphics2D} to fill and draw with.
     * @param startY {@link Byte} The play of an element along the Y-axis.
     * @author Griefed
     */
    private void drawCylonRect(@NotNull Graphics2D g2d, byte startY) {

      for (byte element = 0; element < numberOfElements; element++) {

        double startOfElement = calcCylonRectStart(element);

        if (useGradients) {
          int median = (numberOfElements + 1) / 2;

          if (element + 1 < median) {

            g2d.setPaint(ascCylonRectGradient(element, startOfElement));

          } else if (element + 1 == median) {

            g2d.setPaint(eyeColours[element]);

          } else {

            g2d.setPaint(descCylonRectGradient(element, startOfElement));
          }

        } else {
          g2d.setColor(eyeColours[element]);
        }

        g2d.fillRect((int) startOfElement, startY, (int) elementWidth, (int) height);
      }
    }

    /**
     * Calculate the play of an element in rectangular shape. Steps are:
     *
     * <ul>
     *   <li><strong>Calculate the width of a gap:</strong>
     *       <ul>
     *         <li>Divide the width of one element by 100
     *         <li>multiply with the currently set gap-percentage. (Default <code>25.0f</code>
     *       </ul>
     *   <li><strong>Calculate the half of the total gap width, across all elements:</strong>
     *       <ul>
     *         <li>Subtract two from the number of elements in the eye. No gaps before the first, or
     *             after the last element, we only care about the ones between.
     *         <li>multiply with the width of one gap
     *         <li>divide by two
     *       </ul>
     *   <li><strong>When the current element is the first of all elements in the eye:</strong>
     *       <ul>
     *         <li>Multiply the width of an element with the number of elements in the eye. Divide
     *             by two
     *         <li>subtract the previously calculated half of the total gap width, across all
     *             elements
     *         <li>subtract that from the current position in the eye being drawn at
     *       </ul>
     *   <li><strong>When the current element is not the first of all elements in the eye:</strong>
     *       <ul>
     *         <li>Multiply the width of an element with the number of elements in the eye. Divide
     *             by two
     *         <li>subtract the previously calculated half of the total gap width, across all
     *             elements
     *         <li>add the width of one element to the gap width, multiply with the number of the
     *             element currently being drawn
     *         <li>Subtract that from the current position in the eye being drawn at
     *       </ul>
     * </ul>
     *
     * @param element {@link Byte} The number of the element currently being drawn.
     * @return {@link Double} The X-coordinate where the current element starts.
     * @author Griefed
     */
    private double calcCylonRectStart(byte element) {
      if (element == 0) {
        return p - (elementWidth * numberOfElements / 2.0D) - halfOfTotalGapWidth;

      } else {
        return p
            - (elementWidth * numberOfElements / 2.0D)
            - halfOfTotalGapWidth
            + (elementWidth + gapWidth) * element;
      }
    }

    /**
     * Create the gradient for the currently being drawn, rectangular-shaped, element, when the
     * element is to the left of the center element and when animating as a Cylon-eye. Elements to
     * the left of the center element must have gradients which increase in color intensity towards
     * the center of the eye.
     *
     * <p>Every gradient for these rectangular shapes is drawn from:
     *
     * <ul>
     *   <li><code>X-coordinate:</code> Acquired from {@link #calcCylonRectStart(byte)}
     *   <li><code>Y-coordinate:</code> Half of the height of one element.
     * </ul>
     *
     * to:
     *
     * <ul>
     *   <li><code>X-coordinate:</code> Acquired from {@link #calcCylonRectStart(byte)} plus the
     *       width of one element.
     *   <li><code>Y-coordinate:</code> Half of the height of one element.
     * </ul>
     *
     * @param element {@link Byte} The number of the current element being drawn.
     * @param startX {@link Double} The X-coordinate where the current element starts.
     * @return {@link GradientPaint} The gradient with which to draw the current element.
     * @author Griefed
     */
    @Contract("_, _ -> new")
    private @NotNull GradientPaint ascCylonRectGradient(byte element, double startX) {
      return new GradientPaint(
          (float) startX,
          (float) (height / 2.0D),
          colorWithAlpha((short) (alphas[element] - alphas[element] / 2), eyeColours[element]),
          (float) (startX + elementWidth),
          (float) (height / 2.0D),
          colorWithAlpha(alphas[element], eyeColours[element]));
    }

    /**
     * Create the gradient for the currently being drawn, rectangular-shaped, element, when the
     * element is to the right of the center element and when animating as a Cylon-eye. Elements to
     * the right of the center element must have gradients which decrease in color intensity towards
     * the end of the eye.
     *
     * <p>Every gradient for these rectangular shapes is drawn from:
     *
     * <ul>
     *   <li><code>X-coordinate:</code> Acquired from {@link #calcCylonRectStart(byte)}
     *   <li><code>Y-coordinate:</code> Half of the height of one element.
     * </ul>
     *
     * to:
     *
     * <ul>
     *   <li><code>X-coordinate:</code> Acquired from {@link #calcCylonRectStart(byte)} plus the
     *       width of one element.
     *   <li><code>Y-coordinate:</code> Half of the height of one element.
     * </ul>
     *
     * @param element {@link Byte} The number of the current element being drawn.
     * @param startX {@link Double} The X-coordinate where the current element starts.
     * @return {@link GradientPaint} The gradient with which to draw the current element.
     * @author Griefed
     */
    @Contract("_, _ -> new")
    private @NotNull GradientPaint descCylonRectGradient(byte element, double startX) {
      return new GradientPaint(
          (float) startX,
          (float) (height / 2.0D),
          colorWithAlpha(alphas[element], eyeColours[element]),
          (float) (startX + elementWidth),
          (float) (height / 2.0D),
          colorWithAlpha((short) (alphas[element] - alphas[element] / 2), eyeColours[element]));
    }

    /**
     * Draw our elements in oval shape, starting from the current position to the current position
     * plus the width of the eye. If <code>useGradient</code> is set, then gradients are used for
     * painting, otherwise our ovals are painted with solid colours. For details on how this
     * animation behaves, see {@link LarsonScanner#useCylonAnimation(boolean)}.
     *
     * @param g2d {@link Graphics2D} to fill and draw with.
     * @param startY {@link Byte} The play of an element along the Y-axis.
     * @author Griefed
     */
    private void drawKittOval(@NotNull Graphics2D g2d, byte startY) {

      double startOfElement;
      double drawnElementsWidth;
      byte elementToDraw;

      for (byte element = 0; element < numberOfElements; element++) {

        startOfElement = calcKittOvalStart(element);

        drawKittOval(g2d, startY, startOfElement, element);
      }

      if (increasePosition) {
        // Going left to right

        drawnElementsWidth = p + numberOfElements * elementWidth;

        if (drawnElementsWidth > width) {
          /*
           * We are entering the nether on the right side, so we draw the brightest element at the
           * most right position to create the illusion of the elements gathering.
           */
          startOfElement = width - elementWidth;
          if (useGradients) {
            g2d.setPaint(
                kittRadialGradient(
                    (byte) (numberOfElements - 1),
                    getCenter(startOfElement),
                    eyeColours[numberOfElements - 1]));
          } else {
            g2d.setColor(eyeColours[numberOfElements - 1]);
          }
          g2d.fillOval((int) startOfElement, startY, (int) elementWidth, (int) height);

        } else if (p < 0) {
          /*
           * We are leaving the nether on the left side, so we need to draw that the next element
           * after the ones already visible to create the illusion of the eye emerging.
           */
          elementToDraw = (byte) (numberOfElements - (drawnElementsWidth / elementWidth) - 1);

          if (useGradients) {
            g2d.setPaint(
                kittRadialGradient(elementToDraw, getCenter(0), eyeColours[elementToDraw]));
          } else {
            g2d.setColor(eyeColours[elementToDraw]);
          }
          g2d.fillOval(0, startY, (int) elementWidth, (int) height);
        }

      } else {
        // Going right to left

        drawnElementsWidth = p - numberOfElements * elementWidth;

        if (drawnElementsWidth < 0) {
          /*
           * We are entering the nether on the left side, so we draw the brightest element at the
           * most left position to create the illusion of the elements gathering.
           */
          if (useGradients) {
            g2d.setPaint(
                kittRadialGradient(
                    (byte) (numberOfElements - 1), getCenter(0), eyeColours[numberOfElements - 1]));
          } else {
            g2d.setColor(eyeColours[numberOfElements - 1]);
          }
          g2d.fillOval(0, startY, (int) elementWidth, (int) height);

        } else if (p > width) {
          /*
           * We are leaving the nether on the right side, so we need to draw the next element
           * after the ones already visible to create the illusion of the eye emerging.
           */
          startOfElement = width - elementWidth;
          elementToDraw =
              (byte) (numberOfElements - ((width - drawnElementsWidth) / elementWidth) - 1);

          if (elementToDraw < numberOfElements) {
            drawKittOval(g2d, startY, startOfElement, elementToDraw);
          }
        }
      }
    }

    /**
     * Helper method to slightly cleanup {@link #drawKittOval(Graphics2D, byte)}.
     *
     * @param g2d {@link Graphics2D} to fill and draw with.
     * @param startY {@link Byte} The play of an element along the Y-axis.
     * @param startOfElement {@link Double} The play of the current element along the Larson
     *     Scanner.
     * @param element {@link Byte} The element we are currently drawing.
     * @author Griefed
     */
    private void drawKittOval(
        @NotNull Graphics2D g2d, byte startY, double startOfElement, byte element) {
      if (useGradients) {
        g2d.setPaint(kittRadialGradient(element, getCenter(startOfElement), eyeColours[element]));
      } else {
        g2d.setColor(eyeColours[element]);
      }

      g2d.fillOval((int) startOfElement, startY, (int) elementWidth, (int) height);
    }

    /**
     * Calculate the play of an element in oval shape when animating in Kitt-style and moving from
     * left to right, or right to left, depending on the direction.
     *
     * <p><strong>left to right:</strong>
     *
     * <ul>
     *   <li>Multiply the element currently being drawn with the width of one element
     *   <li>add the previous to the current position from which we are drawing
     * </ul>
     *
     * The element width multiplied with the element currently being drawn, then added on top of the
     * current position in the Larson Scanner, results in the starting point along the X-axis from
     * which to draw the current element.
     *
     * <p><strong>right to left</strong>
     *
     * <ul>
     *   <li>Multiply the element currently being drawn, plus 1, with the width of one element
     *   <li>subtract the previous from the current position from which we are drawing
     * </ul>
     *
     * The element width multiplied with the element currently being drawn, plus 1 because we want
     * the element right from the leftmost one, then subtracted from the current position in the
     * Larson Scanner, results in the starting point along the X-axis from which to draw the current
     * element.
     *
     * @param element {@link Byte} The number of the currently being drawn element, ranging from 0
     *     to the amount of elements in the eye.
     * @return {@link Double} The X-coordinate where the current element starts.
     * @author Griefed
     */
    private double calcKittOvalStart(byte element) {
      if (increasePosition) {
        return p + element * elementWidth;
      } else {
        return p - (element + 1) * elementWidth;
      }
    }

    /**
     * Create the radial gradient for the currently being drawn, oval-shaped, element, when
     * animating Kitt-style. Radial gradients extend from the 2D center point of the current element
     * to the height of one element. As the alpha decreases in the Kitt-style animation, we work
     * with percentages of 255, where an alpha value of 255 equals 0 transparency.
     *
     * @param element {@link Byte} The number of the element currently being drawn.
     * @param centerX {@link Double} The X-coordinate of the center of the element currently being
     *     drawn.
     * @param color {@link Color} The color of the element currently being drawn.
     * @return {@link RadialGradientPaint} A radial gradient for the currently being drawn,
     *     oval-shaped, element.
     * @author Griefed
     */
    private @NotNull RadialGradientPaint kittRadialGradient(
        byte element, double centerX, @NotNull Color color) {

      short alpha = (short) (255.0D / numberOfElements * (double) (element + 1));
      Color[] colors = {colorWithAlpha(alpha, color), getBackground()};
      return new RadialGradientPaint(
          new Point((int) centerX, (int) (height / 2)), (float) (0.5f * height), fractions, colors);
    }

    /**
     * Draw our element in rectangular shape. If <code>useGradient</code> is set, then gradients are
     * used for painting, otherwise our rectangles are painted with solid colours. For details on
     * how this animation behaves, see {@link LarsonScanner#useCylonAnimation(boolean)}.
     *
     * @param g2d {@link Graphics2D} to fill and draw with.
     * @param startY {@link Byte} The play of an element along the Y-axis.
     * @author Griefed
     */
    private void drawKittRect(@NotNull Graphics2D g2d, byte startY) {

      double startOfElement;
      double drawnElementsWidth;
      byte elementToDraw;

      for (byte element = 0; element < numberOfElements; element++) {

        startOfElement = calcKittRectStart(element);

        if (useGradients) {

          g2d.setPaint(kittRectGradient(element, startOfElement));

        } else {

          g2d.setColor(eyeColours[element]);
        }

        g2d.fillRect((int) startOfElement, startY, (int) elementWidth, (int) height);
      }

      if (increasePosition) {
        // Going left to right

        drawnElementsWidth = p + numberOfElements * elementWidth;

        if (drawnElementsWidth > width) {
          /*
           * We are entering the nether on the right side, so we draw the brightest element at the
           * most right position to create the illusion of the elements gathering.
           */
          startOfElement = width - elementWidth;
          g2d.setColor(eyeColours[numberOfElements - 1]);

          g2d.fillRect((int) startOfElement, startY, (int) elementWidth, (int) height);

        } else if (p < 0) {
          /*
           * We are leaving the nether on the left side, so we need to draw that the next element
           * after the ones already visible to create the illusion of the eye emerging.
           */
          elementToDraw = (byte) (numberOfElements - (drawnElementsWidth / elementWidth) - 1);

          if (useGradients) {
            g2d.setPaint(kittRectGradient(elementToDraw, getCenter(0)));
          } else {
            g2d.setColor(eyeColours[elementToDraw]);
          }
          g2d.fillRect(0, startY, (int) elementWidth, (int) height);
        }

      } else {
        // Going right to left

        drawnElementsWidth = p - numberOfElements * elementWidth;

        if (drawnElementsWidth < 0) {
          /*
           * We are entering the nether on the left side, so we draw the brightest element at the
           * most left position to create the illusion of the elements gathering.
           */
          if (useGradients) {
            g2d.setPaint(kittRectGradient((byte) (numberOfElements - 1), getCenter(0)));
          } else {
            g2d.setColor(eyeColours[numberOfElements - 1]);
          }
          g2d.fillRect(0, startY, (int) elementWidth, (int) height);

        } else if (p > width) {
          /*
           * We are leaving the nether on the right side, so we need to draw that the next element
           * after the ones already visible to create the illusion of the eye emerging.
           */
          startOfElement = width - elementWidth;
          elementToDraw =
              (byte) (numberOfElements - ((width - drawnElementsWidth) / elementWidth) - 1);

          if (useGradients) {
            g2d.setPaint(kittRectGradient(elementToDraw, getCenter(startOfElement)));
          } else {
            g2d.setColor(eyeColours[elementToDraw]);
          }
          g2d.fillRect((int) startOfElement, startY, (int) elementWidth, (int) height);
        }
      }
    }

    /**
     * Calculate the play of a Kitt-style animated rectangle. When the element we are drawing is
     * the very first element, the play of said rectangle is simply the current position along the
     * X-axis. Otherwise, when we are scrolling
     *
     * <p><strong>left to right</strong>
     *
     * <ul>
     *   <li>Add the width of an element with the width of the gap between two elements
     *   <li>multiply with the number of the current element
     *   <li>add the result to the current position along the X-axis.
     * </ul>
     *
     * <strong>right to left</strong>
     *
     * <ul>
     *   <li>Add the width of an element with the width of the gap between two elements
     *   <li>multiply with the number of the current element
     *   <li>subtract the result from the current position along the X-axis.
     * </ul>
     *
     * @param element {@link Byte} The number of the element currently being drawn.
     * @return {@link Double} The X-coordinate where the current element starts.
     * @author Griefed
     */
    private double calcKittRectStart(byte element) {
      if (element == 0) {
        return p;
      }
      if (increasePosition) {

        return p + element * (elementWidth + gapWidth);

      } else {

        return p - element * (elementWidth + gapWidth);
      }
    }

    /**
     * Create the gradient for the currently being drawn, rectangular-shaped, element, when
     * animating as a Kitt-eye. Elements have gradients with increased alpha, so less transparency,
     * the further they go from left to right, or the other way around when going right to left.
     *
     * <p>Every gradient for these rectangular shapes is drawn from:
     *
     * <ul>
     *   <li><code>X-coordinate:</code> Acquired from {@link #calcKittRectStart(byte)}
     *   <li><code>Y-coordinate:</code> Half of the height of one element.
     * </ul>
     *
     * to:
     *
     * <ul>
     *   <li><code>X-coordinate:</code> Acquired from {@link #calcKittRectStart(byte)} plus the
     *       width of one element.
     *   <li><code>Y-coordinate:</code> Half of the height of one element.
     * </ul>
     *
     * @param element {@link Byte} The number of the current element being drawn.
     * @param startX {@link Double} The X-coordinate where the current element starts.
     * @return {@link GradientPaint} The gradient with which to draw the current element.
     * @author Griefed
     */
    @Contract("_, _ -> new")
    private @NotNull GradientPaint kittRectGradient(byte element, double startX) {
      short alphaOne = (short) (255.0D / numberOfElements * (double) (element + 1));
      short alphaTwo = (short) (255.0D / numberOfElements * (double) (element + 1));

      if (increasePosition) {
        return new GradientPaint(
            (float) startX,
            (float) (height / 2.0D),
            colorWithAlpha(alphaOne, eyeColours[element]),
            (float) (startX + elementWidth),
            (float) (height / 2.0D),
            colorWithAlpha(alphaTwo, eyeColours[element]));
      } else {
        return new GradientPaint(
            (float) startX,
            (float) (height / 2.0D),
            colorWithAlpha(alphaTwo, eyeColours[element]),
            (float) (startX + elementWidth),
            (float) (height / 2.0D),
            colorWithAlpha(alphaOne, eyeColours[element]));
      }
    }

    /**
     * Set the alpha value for the current colour of the current element being drawn. The alpha
     * value must be a number in the range of <code>0</code> to <code>255</code>.
     *
     * @param alpha {@link Short} Alpha value to set with the given colour. Ranging from <code>0
     *     </code> to <code>255</code>.
     * @param color {@link Color} Color of the element currently being drawn.
     * @return {@link Color} The color of the element currently being drawn with an alpha value set.
     * @throws IllegalArgumentException if the specified alpha-value is smaller than <code>0</code>
     *     or greater than <code>255</code>.
     * @author Griefed
     */
    @Contract("_, _ -> new")
    private @NotNull Color colorWithAlpha(short alpha, @NotNull Color color)
        throws IllegalArgumentException {
      if (alpha < 0 || alpha > 255) {
        throw new IllegalArgumentException("Alpha must be 0 to 255. Specified " + alpha);
      }
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    /**
     * Update the position at which we are currently drawing the eye.
     *
     * <ul>
     *   <li>Cylon Style
     *       <ul>
     *         <li>If <code>useDivider</code> is set, then the position is in-/decremented with the
     *             division of the width of the Larson Scanner and the currently set divider.
     *         <li>If <code>useDivider</code> is not set, then the position is in-/decremented by 1.
     *         <li>When the window the Larson Scanner resides in is resized, and the position is out
     *             of the visible area, then the position is forcefully set to either the width of
     *             the Larson Scanner, or 0, depending on whether the position is outside the
     *             visible field to the left or right.
     *       </ul>
     *   <li>Kitt Style
     *       <ul>
     *         <li>If <code>useDivider</code> is set, then the position is in-/decremented with the
     *             division of the width of the Larson Scanner and the currently set divider.
     *         <li>If <code>useDivider</code> is not set, then the position is in-/decremented by 1.
     *         <li>When the window the Larson Scanner resides in is resized, and the position is out
     *             of the visible area, then the position is forcefully set to either the width plus
     *             the max width of the eye of the Larson Scanner, or 0 minus the max width of the
     *             eye, depending on whether the position is outside the visible field to the left
     *             or right.
     *         <li>In Kitt-style animation, the position does <strong>not</strong> scroll from 0 to
     *             the width of the Larson Scanner and the other way around. Instead, the total
     *             scroll width of the Larson Scanner is increased in both directions by the total
     *             width of the eye
     *       </ul>
     * </ul>
     *
     * @author Griefed
     */
    private void updatePosition() {
      if (cylonAnimation) {
        updatePositionCylonStyle();
      } else {
        updatePositionKittStyle();
      }
    }

    /**
     * See {@link #updatePosition()} for details.
     *
     * @author Griefed
     */
    private void updatePositionCylonStyle() {
      if (p < 0) {
        // switch to left to right

        increasePosition = true;
        p = 0;
        return;

      } else if (p > width) {
        // switch to right to left

        increasePosition = false;
        p = (short) width;
        return;
      }

      if (useDivider) {

        if (increasePosition && p < width) {
          // left to right

          p += width / divider;

        } else if (!increasePosition && p > 0) {
          // right to left

          p -= width / divider;

        }

      } else {

        if (increasePosition && p < width) {
          // left to right

          p++;

        } else if (!increasePosition && p > 0) {
          // right to left

          p--;

        }
      }
    }

    /**
     * See {@link #updatePosition()} for details.
     *
     * @author Griefed
     */
    private void updatePositionKittStyle() {
      double widthElements;
      if (ovalShaped) {
        widthElements = numberOfElements * elementWidth;
      } else {
        widthElements = numberOfElements * elementWidth + totalGapWidth;
      }
      double maxWidth = width + widthElements;
      double maxNegative = 0 - widthElements;

      if (p < maxNegative) {
        // switch to left to right

        increasePosition = true;
        p = (short) maxNegative;
        return;

      } else if (p > maxWidth) {
        // switch to right to left

        increasePosition = false;
        p = (short) maxWidth;
        return;
      }

      if (useDivider) {

        if (increasePosition && p < maxWidth) {
          // left to right

          p += maxWidth / divider;

        } else if (!increasePosition && p > maxNegative) {
          // right to left

          p -= maxWidth / divider;

        }

      } else {

        if (increasePosition && p < maxWidth) {
          // left to right

          p++;

        } else if (!increasePosition && p > maxNegative) {
          // right to left

          p--;

        }
      }
    }
  }
}
