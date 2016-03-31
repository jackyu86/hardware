package com.hw.hardware.common.tools;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Closeables;
import com.google.common.io.Flushables;
import com.hw.hardware.common.exception.AppException;

/**
 * 数据流操作
 * @author cfish
 * @since 2013-09-09
 */
public class StreamUtils {
	protected final static Logger LOGGER = LoggerFactory.getLogger(StreamUtils.class);
	
	/**
	 * 刷新流对象
	 * @param stream
	 */
	public static void flush(Flushable... flush) {
		if (flush == null || flush.length <= 0) {
			return;
		}
		for (Flushable sh : flush) {
			try {
				Flushables.flush(sh, false);
			} catch (Exception e) {
				LOGGER.warn("IOException thrown while flushing Flushable.", e);
			}
		}
	}
	
	/**
	 * 关闭流对象
	 * @param stream
	 */
	public static void close(Closeable... stream) {
		if (stream == null || stream.length <= 0) {
			return;
		}
		for (Closeable close : stream) {
			try {
				Closeables.close(close, false);
			} catch (Exception e) {
				LOGGER.warn("IOException thrown while closing Closeable.", e);
			}
		}
	}
	
	/**
	 * 从输入流中读取字符串
	 * @param is
	 * @param encoding
	 */
	public static String read(InputStream is,String encoding) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			write(is,os);
			if(encoding == null) {
				return new String(os.toByteArray());
			}
			return new String(os.toByteArray(), encoding);
		} catch (Exception e) {
			LOGGER.error("读取文件失败", e);
		}
		return "";
	}
	
	/**
	 * 写入数据到指定流中,并刷新关闭流对象
	 * @param data
	 * @param out
	 */
	public static void writeData(byte[] data,OutputStream out){
		try {
			if(out == null || data == null) {
				return;
			}
			out.write(data);
		} catch(Exception ex){
			throw new AppException("往OutputStream写入数据异常.",ex);
		} finally {
			flush(out);
			close(out);
		}
	}
	
	/**
	 * 写入数据到指定流中,并刷新关闭流对象
	 * @param is
	 * @param os
	 */
	public static void write(InputStream is,OutputStream os) {
		try {
			byte[] bs = new byte[1024];
			int readed = -1;
			while ((readed = is.read(bs)) != -1) {
				os.write(bs, 0, readed);
			}
			os.flush();
		} catch (Exception e) {
			throw new AppException("往OutputStream写入数据异常.",e);
		} finally {
			close(is,os);
		}
	}
	
	/**
	 * 写入数据到指定流中,并不关闭流对象
	 * @param is
	 * @param os
	 */
	public static void writeUnClose(InputStream is,OutputStream os) {
        try {
            byte[] bs = new byte[1024];
            int readed = -1;
            while ((readed = is.read(bs)) != -1) {
                os.write(bs, 0, readed);
            }
            os.flush();
        } catch (Exception e) {
            throw new AppException("往OutputStream写入数据异常.",e);
        } finally {
            close(is);
        }
    }
}
