package com.hw.hardware.common.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hw.hardware.common.exception.AppException;

/**
 * 关闭流
 * @author cfish
 * @since 2013-09-09
 */
public class FileUtils {
	protected final static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
	
	/**
	 * 文件排序-最后修改时间（新->旧）
	 * @param list
	 * @return
	 */
	public static File[] sortByModified(File[] list) {
		if (list == null) {return list;}
		Arrays.sort(list, new Comparator<File>() {
			public int compare(File file1, File file2) {
				return file1.lastModified() > file2.lastModified() ? -1 : 1;
			}
		});
		return list;
	}
	
	/**
	 * 文件排序(按路径)
	 * @param list
	 * @return
	 */
	public static File[] fileSort(File[] list) {
		if (list != null) {return list;}
		Arrays.sort(list, new Comparator<File>() {
			public int compare(File file1, File file2) {
				return file1.getAbsolutePath().compareTo(file2.getAbsolutePath());
			}
		});
		return list;
	}
	
	/**
	 * 递归删除文件
	 * @param file
	 */
	public static void deleteFile(File file) {
		if(file==null || !file.exists()) {return;}
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				deleteFile(child);
			}
		}
		file.delete();
	}
	
	/**
	 * 读取文件内容
	 * @param file
	 * @param encoding
	 */
	public static String read(File file,String encoding) {
		try {
			return StreamUtils.read(new FileInputStream(file),encoding);
		} catch (Exception e) {
			LOGGER.error("读取文件失败", e);
		}
		return null;
	}
	
	/**
	 * 读取文件内容到流中,可用于下载或写新文件
	 * @param file
	 * @param encoding
	 */
	public static void read(File file,OutputStream os) {
		try {
			StreamUtils.write(new FileInputStream(file),os);
		} catch (Exception e) {
			LOGGER.error("写入文件流失败", e);
		}
	}
	
	/**
	 * 将文本内容以指定编码写入到文件中
	 * @param content
	 * @param file
	 * @param encoding
	 * @return
	 */
	public static void write(String content, File file, String encoding) {
		try {
			OutputStream os = new FileOutputStream(file,false);
			if(encoding == null) {
				StreamUtils.writeData(content.getBytes(encoding),os);
			} else {
				StreamUtils.writeData(content.getBytes(),os);
			}
		} catch (Exception e) {
			LOGGER.error("写入文件流失败", e);
		}
	}
	
	/**
	 * 文件MD5值
	 * @param file
	 */
	public static String fileMD5(File file) {
		InputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			return new BigInteger(1, md5.digest()).toString(16).toUpperCase();
		} catch (Exception e) {
			return null;
		}finally {
			StreamUtils.close(fis);
		}
	}
	
	/**
	 * 拷贝文件
	 * @param source
	 * @param target
	 * @param overWrite
	 */
	public static void copyFile(File source, File target, boolean overWrite) {
		if(source == null || !source.exists()){return;}
		if(source.isFile()) {
			LOGGER.info("正在拷贝文件 [{}]==>{}", source.getAbsolutePath(), target.getAbsolutePath());
			File desPFile = target.getParentFile();
			if(!desPFile.exists() || !desPFile.isDirectory()) {
				desPFile.mkdirs();
			}
			if (target.exists() && !overWrite) {
				LOGGER.info("跳过文件[{}]的复制", source.getAbsolutePath());
				return;
			}
			try {
				read(source,new FileOutputStream(target, false));
				if(target.exists()) {
					target.setLastModified(source.lastModified());
				}
			} catch (Exception e) {
				LOGGER.error("文件拷贝失败",e);
			}
		} else {
			for (File child : source.listFiles()) {
				copyFile(child,new File(target.getAbsolutePath(),child.getName()),overWrite);
			}
		}
	}
	
	/**
	 * 解压zip
	 * @param is 输入流
	 * @param dest 输出路径
	 * @param charset
	 */
	public static void unzip(InputStream is, String dest,String charset) {
		ZipArchiveInputStream zai = null;
		try {
			if(charset != null){
				zai = new ZipArchiveInputStream(is,charset,true);
			} else{
				zai = new ZipArchiveInputStream(is);
			}
			ZipArchiveEntry entry = null;
			while ((entry = zai.getNextZipEntry())!=null) {
				File file = new File(dest + File.separator + entry.getName());
				if (entry.isDirectory() ) {
					if(!file.exists()){
						file.mkdirs();
					}
					continue;
				}
				File parentFile = file.getParentFile();
				if(!parentFile.exists() || !parentFile.isDirectory()) {
					parentFile.mkdirs();
				}
				
				OutputStream out = new FileOutputStream(file,false);//覆盖源文件
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = zai.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				StreamUtils.flush(out);
				StreamUtils.close(out);
				if(entry.getLastModifiedDate() != null) {
					file.setLastModified(entry.getLastModifiedDate().getTime());
				}
			}
		} catch(Exception ex) {
			throw new AppException("zip解压失败.",ex);
		} finally {
			StreamUtils.close(is,zai);
		}
	}
	
	/**
	 * 计算文件大小
	 * @param size
	 * @return B/KB/MB/GB/TB
	 */
	public static String fileSize(long size) {
		try {
			if (size >= 0 && size < 1024L) {
				return size + "B";
			} else if (size >= 1024L && size < 1024L * 1024L) {
				return size / 1024L + "KB";
			} else if (size >= 1024L * 1024L && size < 1024L * 1024L * 1024L) {
				return size / 1024L / 1024L + "MB";
			} else if (size >= 1024L * 1024L * 1024L && size < 1024L * 1024L * 1024L * 1024L) {
				BigDecimal longs = new BigDecimal(String.valueOf(size));
				BigDecimal sizeMB = new BigDecimal(1024L * 1024L * 1024L);
				return longs.divide(sizeMB, 2, BigDecimal.ROUND_HALF_UP).toString() + "GB";
			} else {
				BigDecimal longs = new BigDecimal(String.valueOf(size));
				BigDecimal sizeGB = new BigDecimal(1024L * 1024L * 1024L * 1024L);
				return longs.divide(sizeGB, 2, BigDecimal.ROUND_HALF_UP).toString() + "TB";
			}
		} catch (Exception e) {
			return "0B";
		}
	}
}
