package cn.imnu.o2o.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	/**
	 * 将CommonsMultipartFile转化为File
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile=new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException e) {
			logger.error(e.toString());
			e.printStackTrace();
			
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
	}
	/**
	 * 处理缩略图   并返回新生成图片的相对值路径
	 * @param shopImgInputStream
	 * @param targetAddr
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String generateThumbnail(InputStream thumbnailInputStream,String fileName,String targetAddr) throws UnsupportedEncodingException {
		basePath = URLDecoder.decode(basePath,"utf-8");
		//设置文件随机名
		String realFileName = getRandomFileName();
		//获取文件扩展名
		String extension = getFileExtension(fileName);
		//新文件存储在target下
		makeDirPath(targetAddr);
		//生成图片相对路径
		String relativeAddr = targetAddr+realFileName+extension;
		logger.debug("current relativeAddr is:"+relativeAddr);
		//根路径加相对路径
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);

		logger.debug("current relativeAddr is:"+PathUtil.getImgBasePath()+relativeAddr);

		
		try {
			Thumbnails.of(thumbnailInputStream).size(200, 200)
			.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
			.outputQuality(0.8f).toFile("/Program Files/timgnew.jpg");
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr;
	}
	/**
	 * 创建目标路径涉及到的目录,即/aaa/bbb/cccc/xxx.jpg
	 * 那么aaa bbb cccc这三个文件夹都得自动创建
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath()+targetAddr;
		File dirPath = new File(realFileParentPath);
		if(!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}
	//获取 . 后的内容
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
	/**
	 * 生成随机文件,当前年月日小时分钟秒钟+五位随机数
	 * @return
	 */
	public static String getRandomFileName() {
		//获取随机的五位数
		int rannum = r.nextInt(89999)+10000;
		String nowTimeStr=sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}
	public static void main(String[] args) throws IOException {
		
		basePath = URLDecoder.decode(basePath,"utf-8");
		Thumbnails.of(new File("/Program Files/timg.jpg")).size(200, 200)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
				.outputQuality(0.8f).toFile("/Program Files/timgnew.jpg");
	}
}
