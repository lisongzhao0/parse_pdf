package com.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.test.org.jbarcode.JBarcode;
import com.test.org.jbarcode.encode.CodabarEncoder;
import com.test.org.jbarcode.encode.Code11Encoder;
import com.test.org.jbarcode.encode.Code128Encoder;
import com.test.org.jbarcode.encode.Code39Encoder;
import com.test.org.jbarcode.encode.Code39ExtEncoder;
import com.test.org.jbarcode.encode.Code93Encoder;
import com.test.org.jbarcode.encode.Code93ExtEncoder;
import com.test.org.jbarcode.encode.EAN13Encoder;
import com.test.org.jbarcode.encode.EAN8Encoder;
import com.test.org.jbarcode.encode.Interleaved2of5Encoder;
import com.test.org.jbarcode.encode.InvalidAtributeException;
import com.test.org.jbarcode.encode.MSIPlesseyEncoder;
import com.test.org.jbarcode.encode.PostNetEncoder;
import com.test.org.jbarcode.encode.Standard2of5Encoder;
import com.test.org.jbarcode.encode.UPCAEncoder;
import com.test.org.jbarcode.encode.UPCEEncoder;
import com.test.org.jbarcode.paint.BaseLineTextPainter;
import com.test.org.jbarcode.paint.EAN13TextPainter;
import com.test.org.jbarcode.paint.EAN8TextPainter;
import com.test.org.jbarcode.paint.HeightCodedPainter;
import com.test.org.jbarcode.paint.UPCATextPainter;
import com.test.org.jbarcode.paint.UPCETextPainter;
import com.test.org.jbarcode.paint.WideRatioCodedPainter;
import com.test.org.jbarcode.paint.WidthCodedPainter;

/**
 * 条形码创建，需添加jar包：jbarcode-0.2.8.jar
 *
 * @author jianggujin
 *
 */
public class BarcodeCreater
{
    /** 用于生成条形码的对象 **/
    private JBarcode barcode = null;
    public double  barHeight = 17;
    public double  barWideRatio = 1.0;
    public boolean showText = false;
    public boolean checkDigit = true;
    public boolean showCheckDigit = true;

    /**
     * 构造方法
     */
    public BarcodeCreater()
    {
        barcode = new JBarcode(EAN13Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
        barcode.setBarHeight(barHeight);
        try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
        barcode.setShowText(showText);
        barcode.setCheckDigit(checkDigit);
        barcode.setShowCheckDigit(showCheckDigit);
    }

    /**
     * 生成条形码文件
     *
     * @param code
     *           条形码内容
     * @param file
     *           生成文件
     * @throws InvalidAtributeException
     * @throws IOException
     */
    public void write(String code, File file) throws IOException,
            InvalidAtributeException
    {
        ImageIO.write(toBufferedImage(code), "JPEG", file);
    }

    /**
     * 生成条形码并写入指定输出流
     *
     * @param code
     *           条形码内容
     * @param os
     *           输出流
     * @throws IOException
     * @throws InvalidAtributeException
     */
    public void write(String code, OutputStream os) throws IOException,
            InvalidAtributeException
    {
        ImageIO.write(toBufferedImage(code), "JPEG", os);
    }

    /**
     * 创建条形码的BufferedImage图像
     *
     * @param code
     *           条形码内容
     * @return image
     * @throws InvalidAtributeException
     */
    public BufferedImage toBufferedImage(String code)
            throws InvalidAtributeException
    {
        return barcode.createBarcode(code);
    }

    /**
     * 设置编码
     *
     * @param encoder
     */
    public void setEncoder(BarcodeEncoder encoder)
    {
        int val = encoder.ordinal();
        switch (val)
        {
            case 0:
                barcode.setEncoder(EAN13Encoder.getInstance());
                barcode.setPainter(WidthCodedPainter.getInstance());
                barcode.setTextPainter(EAN13TextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 1:
                barcode.setEncoder(UPCAEncoder.getInstance());
                barcode.setPainter(WidthCodedPainter.getInstance());
                barcode.setTextPainter(UPCATextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 2:
                barcode.setEncoder(EAN8Encoder.getInstance());
                barcode.setPainter(WidthCodedPainter.getInstance());
                barcode.setTextPainter(EAN8TextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 3:
                barcode.setEncoder(UPCEEncoder.getInstance());
                barcode.setTextPainter(UPCETextPainter.getInstance());
                barcode.setPainter(WidthCodedPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 4:
                barcode.setEncoder(CodabarEncoder.getInstance());
                barcode.setPainter(WideRatioCodedPainter.getInstance());
                barcode.setTextPainter(BaseLineTextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 5:
                barcode.setEncoder(Code11Encoder.getInstance());
                barcode.setPainter(WidthCodedPainter.getInstance());
                barcode.setTextPainter(BaseLineTextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 6:
                barcode.setEncoder(Code39Encoder.getInstance());
                barcode.setPainter(WideRatioCodedPainter.getInstance());
                barcode.setTextPainter(BaseLineTextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 7:
                barcode.setEncoder(Code39ExtEncoder.getInstance());
                barcode.setPainter(WideRatioCodedPainter.getInstance());
                barcode.setTextPainter(BaseLineTextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 8:
                barcode.setEncoder(Code93Encoder.getInstance());
                barcode.setPainter(WidthCodedPainter.getInstance());
                barcode.setTextPainter(BaseLineTextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 9:
                barcode.setEncoder(Code93ExtEncoder.getInstance());
                barcode.setPainter(WidthCodedPainter.getInstance());
                barcode.setTextPainter(BaseLineTextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 10:
                barcode.setEncoder(Code128Encoder.getInstance());
                barcode.setPainter(WidthCodedPainter.getInstance());
                barcode.setTextPainter(BaseLineTextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 11:
                barcode.setEncoder(MSIPlesseyEncoder.getInstance());
                barcode.setPainter(WidthCodedPainter.getInstance());
                barcode.setTextPainter(BaseLineTextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 12:
                barcode.setEncoder(Standard2of5Encoder.getInstance());
                barcode.setPainter(WideRatioCodedPainter.getInstance());
                barcode.setTextPainter(BaseLineTextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 13:
                barcode.setEncoder(Interleaved2of5Encoder.getInstance());
                barcode.setPainter(WideRatioCodedPainter.getInstance());
                barcode.setTextPainter(BaseLineTextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
            case 14:
                barcode.setEncoder(PostNetEncoder.getInstance());
                barcode.setPainter(HeightCodedPainter.getInstance());
                barcode.setTextPainter(BaseLineTextPainter.getInstance());
                barcode.setBarHeight(barHeight);
                try{barcode.setWideRatio(barWideRatio);} catch (Exception ex){}
                barcode.setShowText(showText);
                barcode.setCheckDigit(checkDigit);
                barcode.setShowCheckDigit(showCheckDigit);
                break;
        }
    }

    /**
     * 条形码编码方式
     *
     * @author jianggujin
     *
     */
    public enum BarcodeEncoder
    {
        Codabar, Code39, Code39Ext, Code93, Code93Ext, Code128, Interleaved2of5
    }

    public static void main(String[] args) throws IOException, InvalidAtributeException {
        BarcodeCreater bc = new BarcodeCreater();
        bc.showText = true;
        bc.showCheckDigit = false;
        bc.barHeight = 30;
        bc.barWideRatio = 4.0;
        for (BarcodeEncoder be : BarcodeEncoder.values()) {
            bc.setEncoder(be);
            try {
                bc.write("01162233", new File("/Users/zhaolisong/Desktop/showText_" + be.name() + ".jpg"));
            }catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}

