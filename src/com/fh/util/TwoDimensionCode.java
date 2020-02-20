package com.fh.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import com.swetake.util.Qrcode;

/**
 * 二维码
 * 创建人：FH  313596790QQ
 * 创建时间：2018年4月10日
 * @version
 */
public class TwoDimensionCode {

	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 *            存储内容
	 * @param imgPath
	 *            图片路径
	 */
	public static void encoderQRCode(String content, String imgPath) {
		encoderQRCode(content, imgPath, "png", 2);
	}

	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 *            存储内容
	 * @param output
	 *            输出流
	 */
	public static void encoderQRCode(String content, OutputStream output) {
		encoderQRCode(content, output, "png", 2);
	}

	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 *            存储内容
	 * @param imgPath
	 *            图片路径
	 * @param imgType
	 *            图片类型
	 */
	public static void encoderQRCode(String content, String imgPath, String imgType) {
		encoderQRCode(content, imgPath, imgType, 2);
	}

	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 *            存储内容
	 * @param output
	 *            输出流
	 * @param imgType
	 *            图片类型
	 */
	public static void encoderQRCode(String content, OutputStream output,
			String imgType) {
		encoderQRCode(content, output, imgType, 2);
	}

	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 *            存储内容
	 * @param imgPath
	 *            图片路径
	 * @param imgType
	 *            图片类型
	 * @param size
	 *            二维码尺寸
	 */
	public static void encoderQRCode(String content, String imgPath, String imgType,
			int size) {
		try {
			BufferedImage bufImg = qRCodeCommon(content, imgType, size);

			File imgFile = new File(imgPath);
			// 生成二维码QRCode图片
			ImageIO.write(bufImg, imgType, imgFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 *            存储内容
	 * @param output
	 *            输出流
	 * @param imgType
	 *            图片类型
	 * @param size
	 *            二维码尺寸
	 */
	public static void encoderQRCode(String content, OutputStream output,
			String imgType, int size) {
		try {
			BufferedImage bufImg = qRCodeCommon(content, imgType, size);
			// 生成二维码QRCode图片
			ImageIO.write(bufImg, imgType, output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成二维码(QRCode)图片的公共方法
	 * 
	 * @param content
	 *            存储内容
	 * @param imgType
	 *            图片类型
	 * @param size
	 *            二维码尺寸
	 * @return
	 */
	private static BufferedImage qRCodeCommon(String content, String imgType, int size) {
		BufferedImage bufImg = null;
		size = 10;
		try {
			Qrcode qrcodeHandler = new Qrcode();
			// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			qrcodeHandler.setQrcodeErrorCorrect('M');
			qrcodeHandler.setQrcodeEncodeMode('B');
			// 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
			qrcodeHandler.setQrcodeVersion(size);
			// 获得内容的字节数组，设置编码格式
			byte[] contentBytes = content.getBytes("utf-8");
			// 图片尺寸
			//int imgSize = 67 + 12 * (size - 1);
			int imgSize = 67 + 12 * (size - 1);
			
			//System.out.println(imgSize);
			
			bufImg = new BufferedImage(imgSize, imgSize,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			// 设置背景颜色
			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, imgSize, imgSize);

			// 设定图像颜色> BLACK
			gs.setColor(Color.BLACK);
			// 设置偏移量，不设置可能导致解析出错
			int pixoff = 2;
			// 输出内容> 二维码
			if (contentBytes.length > 0 && contentBytes.length < 800) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				throw new Exception("QRCode content bytes length = "
						+ contentBytes.length + " not in [0, 800].");
			}
			gs.dispose();
			bufImg.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufImg;
	}

	/**
	 * 解析二维码（QRCode）
	 * 
	 * @param imgPath
	 *            图片路径
	 * @return
	 */
	public static String decoderQRCode(String imgPath) throws Exception{
		// QRCode 二维码图片的文件
		File imageFile = new File(imgPath);
		BufferedImage bufImg = null;
		String content = null;
		try {
			bufImg = ImageIO.read(imageFile);
			QRCodeDecoder decoder = new QRCodeDecoder();
			content = new String(decoder.decode(new TwoDimensionCodeImage(
					bufImg)), "utf-8");
		} catch (IOException e) {
			//System.out.println("Error: " + e.getMessage());
			//e.printStackTrace();
		} catch (DecodingFailedException dfe) {
			//System.out.println("Error: " + dfe.getMessage());
			//dfe.printStackTrace();
		}
		return content;
	}

	/**
	 * 解析二维码（QRCode）
	 * 
	 * @param input
	 *            输入流
	 * @return
	 */
	public static String decoderQRCode(InputStream input) {
		BufferedImage bufImg = null;
		String content = null;
		try {
			bufImg = ImageIO.read(input);
			QRCodeDecoder decoder = new QRCodeDecoder();
			content = new String(decoder.decode(new TwoDimensionCodeImage(
					bufImg)), "utf-8");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		} catch (DecodingFailedException dfe) {
			System.out.println("Error: " + dfe.getMessage());
			dfe.printStackTrace();
		}
		return content;
	}

	public static void main(String[] args) {
//		test1();
//
		String imgPath = "F:/a.png";
		/*String encoderContent = "Hello 大大、小小,welcome to QRCode!"
				+ "\nMyblog [ http://sjsky.iteye.com ]"
				+ "\nEMail [ sjsky007@gmail.com ]";*/

		String encoderContent = "http://www.baidu.com";
		TwoDimensionCode handler = new TwoDimensionCode();
		handler.encoderQRCode(encoderContent, imgPath, "png");
		// try {
		// OutputStream output = new FileOutputStream(imgPath);
		// handler.encoderQRCode(content, output);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		System.out.println("========encoder success");

		//String decoderContent = handler.decoderQRCode(imgPath);
		System.out.println("解析结果如下：");
		//System.out.println(decoderContent);
		System.out.println("========decoder success!!!");
	}

	public static void test1() {
		String imgPath = "F:/b.png";
//先创建一个二维码
//		String text = strRequiredParam("barcode","二维码信息");
//		String desc = strRequiredParam("desc","文字内容");//二维码下面的文字描述
		String text = "一个大吃货";//二维码下面的文字描述
		String desc = "边娇的头像";//二维码下面的文字描述
		String logoPath = "f:\\a.png";//二维码的logo地址
		int logoWidth = 40; //logo的宽
		int logoHeight = 40;  //logo的高
		try{
			Qrcode qrcode = new Qrcode();
			qrcode.setQrcodeErrorCorrect('H');//设置纠错等级(分为:L、M、H三个等级)
			qrcode.setQrcodeEncodeMode('B');//N代表数字、A代表a-Z、B代表其他字符
			qrcode.setQrcodeVersion(7);//设置版本

			int width = 67+12*(7-1);//设置二维码的宽  二维码像素的大小和版本号有关  但是版本号越大   二维码也越是复杂  这个需要注意
			int height = 67+12*(7-1);//设置二维码的高
			//将内容变为特定UTF-8格式编码的字节码
			byte [] qrData = text.getBytes("UTF-8");

			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
			//创造画笔
			Graphics2D gs = bufferedImage.createGraphics();
			gs.setBackground(Color.WHITE);//设置背景
			gs.setColor(Color.BLACK);//设置画笔颜色
			gs.clearRect(0, 0, width, height);//清除画板内容
			//设置偏移量
			int pixoff = 2;
			boolean [][] d = qrcode.calQrcode(qrData);
			for(int y=0;y<d.length;y++) {
				for(int x=0;x<d.length;x++) {
					if(d[x][y]) {
						gs.fillRect(x*3+pixoff, y*3+pixoff, 3, 3);
					}
				}
			}
			gs.dispose();
			BufferedImage bm = bufferedImage;//二维码
			File logoFile = new File(logoPath); //logo图片
			BufferedImage logoImg = ImageIO.read(logoFile);
	            /* float ratio = 0.5;   //倒圆角
	             if(ratio>0){
	                 logoWidth = logoWidth>width*ratio ? (int)(width*ratio) : logoWidth;
	                 logoHeight = logoHeight>height*ratio ? (int)(height*ratio) : logoHeight;
	             }  */
			int x = (width-logoWidth)/2;
			int y = (height-logoHeight)/2;
			Graphics g = bm.getGraphics();
			g.drawImage(logoImg, x, y, logoWidth, logoHeight, null);
			int whiteWidth = 0;  //白边
			Font font = new Font("黑体", Font.BOLD, 12);
			int fontHeight = g.getFontMetrics(font).getHeight();//得到字体的高度
			//计算需要多少行
			int lineNum = 1;
			int currentLineLen = 0;
			for(int i=0;i<desc.length();i++){
				char c = desc.charAt(i);
				int charWidth = g.getFontMetrics(font).charWidth(c);
				//循环文字得到文字区域的行数
				if(currentLineLen+charWidth>width){
					lineNum++;
					currentLineLen = 0;
					continue;
				}
				currentLineLen += charWidth;
			}
			int totalFontHeight = fontHeight*lineNum; //得到文字区域的高度
			int wordTopMargin = 4;
			BufferedImage bm1 = new BufferedImage(width, height+totalFontHeight+wordTopMargin-whiteWidth, BufferedImage.TYPE_INT_RGB); //创建将文字高度计算到其中的图片
			Graphics g1 = bm1.getGraphics();
			g1.setColor(Color.WHITE);
			g1.fillRect(0, height, width, totalFontHeight+wordTopMargin-whiteWidth); //将文字部分的背景填充成白色
			g1.setColor(Color.black);
			g1.setFont(font);
			g1.drawImage(bm, 0, 0, null); //将创建好的二维码从起点（0,0）开始画在图中
			int currentLineIndex = 0;
			//判断是否只有一行，只有一行就居中显示
			currentLineLen = lineNum-1==currentLineIndex?(width-g.getFontMetrics(font).stringWidth(desc))/2:0;
			int baseLo = g1.getFontMetrics().getAscent();
			for(int i=0;i<desc.length();i++){
				String c = desc.substring(i, i+1);
				int charWidth = g.getFontMetrics(font).stringWidth(c);
				//判断是否需要换行
				if(currentLineLen+charWidth>width){
					currentLineIndex++;
					//判断是否是最后一行  最后一行居中显示
					currentLineLen = lineNum-1==currentLineIndex?(width-g.getFontMetrics(font).stringWidth(desc.substring(i)))/2:0;
					g1.drawString(c, currentLineLen + whiteWidth, height+baseLo+fontHeight*(currentLineIndex)+wordTopMargin);//将单个文字画到对应位置
					currentLineLen = charWidth;
					continue;
				}
				g1.drawString(c, currentLineLen+whiteWidth, height+baseLo+fontHeight*(currentLineIndex) + wordTopMargin);
				currentLineLen += charWidth;
			}
			g1.dispose();
			bm = bm1;
//			response.setContentType("image/jpeg");
			//好了 ，现在通过IO流来传送数据
			ImageIO.write (bm , "JPEG", new File(imgPath));
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
}
