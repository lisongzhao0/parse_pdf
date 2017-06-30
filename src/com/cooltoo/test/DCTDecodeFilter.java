package com.cooltoo.test;

import com.itextpdf.io.codec.TIFFFaxDecoder;
import com.itextpdf.io.codec.TIFFFaxDecompressor;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.filters.IFilterHandler;

/**
 * Created by zhaolisong on 19/04/2017.
 */
public class DCTDecodeFilter implements IFilterHandler {

    @Override
    public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) {
        PdfNumber wn = streamDictionary.getAsNumber(PdfName.Width);
        PdfNumber hn = streamDictionary.getAsNumber(PdfName.Height);
        if(wn != null && hn != null) {
            int width = wn.intValue();
            int height = hn.intValue();
            PdfDictionary param = decodeParams instanceof PdfDictionary?(PdfDictionary)decodeParams:null;
            int k = 0;
            boolean blackIs1 = false;
            boolean byteAlign = false;
            if(param != null) {
                PdfNumber outBuf = param.getAsNumber(PdfName.K);
                if(outBuf != null) {
                    k = outBuf.intValue();
                }

                PdfBoolean decoder = param.getAsBoolean(PdfName.BlackIs1);
                if(decoder != null) {
                    blackIs1 = decoder.getValue();
                }

                decoder = param.getAsBoolean(PdfName.EncodedByteAlign);
                if(decoder != null) {
                    byteAlign = decoder.getValue();
                }
            }

            byte[] var18 = new byte[(width + 7) / 8 * height];
            TIFFFaxDecompressor var19 = new TIFFFaxDecompressor();
            int len;
            if(k != 0 && k <= 0) {
                TIFFFaxDecoder var20 = new TIFFFaxDecoder(1, width, height);
                var20.decodeT6(var18, b, 0, height, 0L);
            } else {
                len = k > 0?1:0;
                len |= byteAlign?4:0;
                var19.SetOptions(1, 3, len, 0);
                var19.decodeRaw(var18, b, width, height);
                if(var19.fails > 0) {
                    byte[] t = new byte[(width + 7) / 8 * height];
                    int oldFails = var19.fails;
                    var19.SetOptions(1, 2, len, 0);
                    var19.decodeRaw(t, b, width, height);
                    if(var19.fails < oldFails) {
                        var18 = t;
                    }
                }
            }

            if(!blackIs1) {
                len = var18.length;

                for(int var21 = 0; var21 < len; ++var21) {
                    var18[var21] = (byte)(var18[var21] ^ 255);
                }
            }

            return var18;
        } else {
            throw new PdfException("Filter CCITTFaxDecode is only supported for images");
        }
    }
}