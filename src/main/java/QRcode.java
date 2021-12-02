import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
/**
 * @program:zxing1
 * @author:wihenne
 * @creatTime:2021/12/02
 **/

public class QRcode {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * 生成二维码
     * @param content       扫码内容
     * @param QRcodeWidth   二维码宽度
     * @param QRcodeHeigh   二维码高度
     * @param logoWidth     logo宽度
     * @param logoHeigh     logo高度
     * @param logoPath      logo相对路径(null时不加logo）
     * @param QRcodeColor   二维码颜色
     * @return
     * @throws Exception
     */
    public static String generateQRcode(String content,
                                        int QRcodeWidth,int QRcodeHeigh,int logoWidth, int logoHeigh,
                                        String logoPath, int QRcodeColor) throws Exception{
        /** 定义Map集合封装二维码配置信息 */
        Map<EncodeHintType, Object> hints = new HashMap<>();
        /** 设置二维码图片的内容编码 */
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        /** 设置二维码图片的上、下、左、右间隙 */
        hints.put(EncodeHintType.MARGIN, 1);
        /** 设置二维码的纠错级别 */
        hints.put(EncodeHintType.ERROR_CORRECTION,
                ErrorCorrectionLevel.H);
        /**
         * 创建二维码字节转换对象
         * 第一个参数：二维码图片中的内容
         * 第二个参数：二维码格式器
         * 第三个参数：生成二维码图片的宽度
         * 第四个参数：生成二维码图片的高度
         * 第五个参数：生成二维码需要配置信息
         *  */
        BitMatrix matrix = new
                MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, QRcodeWidth, QRcodeHeigh, hints);

        /** 获取二维码图片真正的宽度  */
        int matrix_width = matrix.getWidth();
        /** 获取二维码图片真正的高度  */
        int matrix_height = matrix.getHeight();
        /** 定义一张空白的缓冲流图片 */
        BufferedImage image = new
                BufferedImage(matrix_width, matrix_height,
                BufferedImage.TYPE_INT_RGB);
        /** 把二维码字节转换对象 转化 到缓冲流图片上 */
        for (int x = 0; x < matrix_width; x++){
            for (int y = 0; y < matrix_height; y++){
                /** 通过x、y坐标获取一点的颜色 true: 黑色  false: 白色 */
                int rgb = matrix.get(x, y) ? QRcodeColor : 0xFFFFFF;
                image.setRGB(x, y, rgb);
            }
        }

        if(!logoPath.equals("null")) {
            /** 获取logo图片 */
            /**
             * static 内不能用this获取绝对路径
             * 通过getCanonicalPath获取当前程序的绝对路径
             */
            File directory = new File("");//参数为空
            String courseFile = directory.getCanonicalPath();
            String path = courseFile + "/File/logo/" +logoPath;
//            System.out.println(path);
            BufferedImage logo = ImageIO.read(new File(path));
            /** 获取缓冲流图片的画笔 */
            Graphics2D g = (Graphics2D) image.getGraphics();
            /** 在二维码图片中间绘制logo */
            g.drawImage(logo, (matrix_width - logoWidth) / 2,
                    (matrix_height - logoHeigh) / 2,
                    logoWidth, logoHeigh, null);

            /** 设置画笔的颜色 */
            g.setColor(Color.WHITE);
            /** 设置画笔的粗细 */
            g.setStroke(new BasicStroke(5.0f));
            /** 设置消除锯齿 */
            g.setRenderingHint(RenderingHints
                    .KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            /** 绘制圆角矩形 */
            g.drawRoundRect((matrix_width - logoWidth) / 2,
                    (matrix_height - logoHeigh) / 2,
                    logoWidth, logoHeigh, 10, 10);

        }

        /** 生成二维码 */
        String qrcodeName = dateToRandom(1)+".png";
        ImageIO.write(image, "png", new File("File/QRcode/" + qrcodeName));//输出带logo图片

        return "File/QRcode/" + qrcodeName;
    }

    /**
     * 生成时间戳随机数
     * @param tailNumber 时间戳+后面几位随机数
     * @return
     */
    public static String dateToRandom(int tailNumber){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        for(int i=0;i<tailNumber;i++){
            result+=random.nextInt(10);
        }
        return newDate+result;
    }

    public static void main(String[] args) throws Exception{
        generateQRcode("https://blog.csdn.net/one_hwx",300,300,90,90,
                "123.png",0x6495ED);
    }
}
