package com.test.net.jmge.gif;


//==============================================================================

import com.test.net.jmge.gif.DirectGif89Frame;
import com.test.net.jmge.gif.Gif89Encoder;
import com.test.net.jmge.gif.Gif89Frame;

/** Instances of this Gif89Frame subclass are constructed from bitmaps in the
 *  form of color-index pixels, which accords with a GIF's native palettized
 *  color model.  The class is useful when complete control over a GIF's color
 *  palette is desired.  It is also much more efficient when one is using an
 *  algorithmic frame generator that isn't interested in RGB values (such
 *  as a cellular automaton).
 *  <p>
 *  Objects of this class are normally added to a Gif89Encoder object that has
 *  been provided with an explicit color table at construction.  While you may
 *  also add them to "auto-map" encoders without an exception being thrown, 
 *  there obviously must be at least one DirectGif89Frame object in the sequence
 *  so that a color table may be detected.
 *
 * @version 0.90 beta (15-Jul-2000)
 * @author J. M. G. Elliott (tep@jmge.net)
 * @see Gif89Encoder
 * @see net.jmge.gif.Gif89Frame
 * @see DirectGif89Frame
 */
public class IndexGif89Frame extends Gif89Frame {

  //----------------------------------------------------------------------------
  /** Construct a IndexGif89Frame from color-index pixel data.
   *
   * @param width
   *   Width of the bitmap.
   * @param height
   *   Height of the bitmap.
   * @param ci_pixels
   *   Array containing at least width*height color-index pixels.
   */
  public IndexGif89Frame(int width, int height, byte ci_pixels[])
  {
    theWidth = width;
    theHeight = height;
    ciPixels = new byte[theWidth * theHeight];
    System.arraycopy(ci_pixels, 0, ciPixels, 0, ciPixels.length);
  }

  //----------------------------------------------------------------------------
  Object getPixelSource() { return ciPixels; }  
}