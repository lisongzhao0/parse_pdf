package com.happy.gene.pdfreport.pdf.def.element;

import com.happy.gene.pdfreport.pdf.IPosition;
import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.util.ParametersUtil;
import com.happy.gene.pdfreport.pdf.util.XmlDataCache;
import com.happy.gene.pdfreport.pdf.util.log.Logger;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.twelvemonkeys.image.ResampleOp;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.dom4j.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;

/**
 * Created by zhaolisong on 12/04/2017.
 */
public class ImageDef extends AbstractDef implements IXml<ImageDef>, IPosition<ImageDef> {

    private String id;
    private String url;
    private String imageType;
    private String fit;
    private float x;
    private float y;
    private float width;
    private float height;
    private float opacity = 1.0f;

    public String getId() {
        return id;
    }
    public ImageDef setId(String id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }
    public ImageDef setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getImageType() {
        return imageType;
    }
    public ImageDef setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }
    public ImageDef setWidth(float width) {
        this.width = width;
        return this;
    }

    public float getHeight() {
        return height;
    }
    public ImageDef setHeight(float height) {
        this.height = height;
        return this;
    }

    public ImageDef translate(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public ImageDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"image".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.id        = element.attributeValue("id");
        this.catalog   = element.attributeValue("catalog");
        this.visible   = getStringUtil().isEmpty(element.attributeValue("visible")) ? "true" : element.attributeValue("visible");
        this.url       = element.attributeValue("url");
        this.imageType = element.attributeValue("type");
        this.fit       = element.attributeValue("fit");
        this.fit       = (!"a".equalsIgnoreCase(this.fit) && !"f".equalsIgnoreCase(this.fit)) ? "F" : this.fit;
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));} catch (Exception ex) {this.setZOrder(0);}
        try { this.x      = Float.parseFloat(element.attributeValue("x"));        } catch (Exception ex) {this.x = 0.0f;}
        try { this.y      = Float.parseFloat(element.attributeValue("y"));        } catch (Exception ex) {this.y = 0.0f;}
        try { this.width  = Float.parseFloat(element.attributeValue("width"));    } catch (Exception ex) {this.width  = 0.0f;}
        try { this.height = Float.parseFloat(element.attributeValue("height"));   } catch (Exception ex) {this.height = 0.0f;}
        try { this.opacity= Float.parseFloat(element.attributeValue("opacity"));  } catch (Exception ex) {this.opacity= 1.0f;}
        return this;
    }

    public Image generate(Document pdf, PageDef pageDef) throws Exception {
        if (!isVisible()) { return null; }
        PdfPage page = pdf.getPdfDocument().getLastPage();
        setPageStartNumberInPdf(pdf.getPdfDocument().getPageNumber(page));
        try {
            ParametersUtil parametersUtil = ParametersUtil.getInstance();
            String tmpValue = parametersUtil.replaceParameter(url);

            if (null!=tmpValue && !"".equals(tmpValue.trim())) {
                if (tmpValue.indexOf("$") >= 0){
                    tmpValue = parametersUtil.replaceParameter(tmpValue);
                }
                if ('.' == tmpValue.trim().charAt(0)) {
                    tmpValue = XmlDataCache.getInstance().getParameter(XmlDataCache.SECOND_KEY_IMAGE_BASE_DIR) + tmpValue.trim();
                }
                if (tmpValue.indexOf("$") >= 0){
                    return null;
                }
            }
            else {
                return null;
            }

//            String outFilePath = System.getenv("HOME") + "/tmp/" + System.nanoTime();
//            Thumbnails.of(tmpValue).scale(0.25).toFile(outFilePath);


            Image image = null;
//            BufferedImage bufferedImage = zoomImage(tmpValue, 1f);
//            if (null!=bufferedImage) {
//                ImageIO.write(bufferedImage, tmpValue.substring(tmpValue.lastIndexOf(".") + 1), new File(outFilePath));
//                image = new Image(ImageDataFactory.create(outFilePath, false));
//            }
//            else {
                image = new Image(ImageDataFactory.create(tmpValue, false));
//            }
            image.setFixedPosition(pdf.getPdfDocument().getPageNumber(page), x, y);
            image.setOpacity(opacity);
            if ("a".equalsIgnoreCase(fit)) {
                image.scaleAbsolute(width, height);
            }
            else {
                image.scaleToFit(width, height);
            }
            pdf.add(image);
            return image;
        }
        catch (Exception ex) {
            Logger.error("parse image has exception: {}", ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public ImageDef clone() {
        ImageDef newOne = new ImageDef();
        newOne.setZOrder(this.getZOrder());
        newOne.id    = this.id;
        newOne.url   = this.url;
        newOne.imageType = this.imageType;
        newOne.fit   = this.fit;
        newOne.x     = this.x;
        newOne.y     = this.y;
        newOne.width = this.width;
        newOne.height= this.height;

        return newOne;
    }


    /**
     * @param src
     *            原始图像路径
     * @param fResizeTimes
     *            倍数,比如0.5就是缩小一半,0.98等等double类型
     * @return 返回处理后的图像
     */
    public BufferedImage zoomImage(String src, float fResizeTimes) {
        BufferedImage result = null;
        System.out.println("创建缩略图, rate=" + fResizeTimes + " image=" + src);
        try {
            System.out.println(src);
            File srcfile = new File(src);
            if (!srcfile.exists()) {
                System.out.println("文件不存在");
            }

            BufferedImage im = null;
            try { im = ImageIO.read(srcfile); } catch (Exception ex) { return null; }

            if (srcfile.length()<200*1024) {
                return im;
            }


            /* 原始图像的宽度和高度 */
            int width = im.getWidth();
            int height = im.getHeight();

            float fWidth = getWidth()/width;
            float fHeight= getHeight()/height;

            fResizeTimes = Math.max(fHeight, fWidth);

            //压缩计算
            float resizeTimes = fResizeTimes;  /*这个参数是要转化成的倍数,如果是1就是转化成1倍*/

            /* 调整后的图片的宽度和高度 */
            int toWidth = (int) (width * resizeTimes);
            int toHeight = (int) (height * resizeTimes);

            /* 新生成结果图片 */
            BufferedImageOp resampler = new ResampleOp(toWidth, toHeight, ResampleOp.FILTER_LANCZOS);
            result = resampler.filter(im, null);


        }
        catch (NoClassDefFoundError ex) {
            System.out.println("创建缩略图发生异常" + ex.getMessage());
        }
        catch (Exception ex) {
            System.out.println("创建缩略图发生异常" + ex.getMessage());
        }

        return result;
    }
}
