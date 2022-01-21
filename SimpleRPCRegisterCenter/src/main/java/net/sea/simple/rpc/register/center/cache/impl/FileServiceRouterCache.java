package net.sea.simple.rpc.register.center.cache.impl;

import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import net.sea.simple.rpc.exception.RPCServerException;
import net.sea.simple.rpc.register.center.cache.config.FileCacheConfig;
import net.sea.simple.rpc.register.center.cache.constants.CacheConstants;
import net.sea.simple.rpc.server.ServiceInfo;
import net.sea.simple.rpc.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description: 带持久化的路由缓存
 * @Author: hcw
 * @Date: 2022/1/14 11:18
 */
@Slf4j
public class FileServiceRouterCache extends AbstractServiceRouterCache {
    //路由刷新默认时间
    private static long REFRESH_PERIOD = CacheConstants.DEFAULT_REFRESH_PERIOD;

    @Override
    protected String getCacheType() {
        return "persistent";
    }

    @Override
    protected void initAfter() {
        super.initAfter();
        REFRESH_PERIOD = config.getRefreshPeriod();
    }

    @Override
    public void loadAvailableServices() {
        List<ServiceInfo> allAvailableServices = register.findAllAvailableServices();
        if (CollectionUtils.isEmpty(allAvailableServices)) {
            log.warn("未加载到服务列表，从持久化的文件中获取");
            allAvailableServices = loadCacheFromFile();
        }
        if (CollectionUtils.isEmpty(allAvailableServices)) {
            log.warn("未加载到服务列表");
        } else {
            serviceWithVersionMapper = allAvailableServices.stream().collect(Collectors.groupingBy(si -> String.format("%s_%s", si.getServiceName(), si.getVersion())));
            //持久化路由缓存
            saveCache(allAvailableServices);
        }
    }

    /**
     * 持久化路由缓存
     *
     * @param allAvailableServices
     */
    private void saveCache(List<ServiceInfo> allAvailableServices) {
        FileUtils.writeFile(buildCacheFilePath(), JsonUtils.toJson(allAvailableServices));
    }

    /**
     * 构建缓存文件地址
     *
     * @return
     */
    private String buildCacheFilePath() {
        FileCacheConfig fileCacheConfig = (FileCacheConfig) config;
        String cacheFilePath = fileCacheConfig.getCacheFilePath();
        //配置的有完整缓存文件地址时直接使用
        if (StringUtils.isNoneBlank(cacheFilePath)) {
            return cacheFilePath;
        }
        String cacheFileDir = fileCacheConfig.getCacheFileDir();
        //获取缓存文件目录
        cacheFileDir = StringUtils.isBlank(cacheFileDir) ? System.getProperty("java.io.tmpdir") : cacheFilePath;
        String cacheFileName = fileCacheConfig.getCacheFileName();
        cacheFileName = StringUtils.isBlank(cacheFileName) ? String.format("srpc_router_%s.cache", register.getRGSign()) : cacheFileName;
        return new File(cacheFileDir, cacheFileName).getAbsolutePath();
    }

    /**
     * 从文件中获取缓存路由
     *
     * @return
     */
    private List<ServiceInfo> loadCacheFromFile() {
        String content = FileUtils.readFile(buildCacheFilePath());
        if (StringUtils.isBlank(content)) {
            return Collections.EMPTY_LIST;
        }
        return JsonUtils.toBean(content, new TypeToken<List<ServiceInfo>>() {
        });
    }

    /**
     * 文件工具类
     */
    private static class FileUtils {
        /**
         * 写文件
         *
         * @param file    待写入文件
         * @param content 写入内容
         * @param charset 文件编码
         */
        public static void writeFile(String file, String content, String charset) {
            try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                raf.seek(0);
                FileChannel fileChannel = raf.getChannel();
                FileLock fileLock;
                log.info("获取文件锁");
                long start = System.currentTimeMillis();
                while (true) {
                    try {
                        fileLock = fileChannel.tryLock();
                        //超时
                        if ((System.currentTimeMillis() - start) > REFRESH_PERIOD * 0.7) {
                            throw new RPCServerException("获取路由缓存文件锁超时");
                        }
                        break;
                    } catch (Exception e) {
                        log.warn("未获取到文件句柄");
                        TimeUnit.MILLISECONDS.sleep(200);
                    }
                }
                log.info("获取到文件锁");
                raf.write(content.getBytes(charset));
                fileLock.release();
                fileChannel.close();
            } catch (Exception e) {
                log.error("文件写入异常", e);
            }
        }

        /**
         * 写文件
         *
         * @param file    待写入文件
         * @param content 写入内容
         */
        public static void writeFile(String file, String content) {
            writeFile(file, content, "UTF-8");
        }

        /**
         * 读文件
         *
         * @param file    待读取的文件
         * @param charset 文件编码
         * @return
         */
        public static String readFile(String file, String charset) {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
                byte[] buffer = new byte[2048];
                int realLength = 0;
                while ((realLength = randomAccessFile.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, realLength);
                }
                return (new String(byteArrayOutputStream.toByteArray(), charset));
            } catch (Exception e) {
                log.error("读取缓存文件异常", e);
            }
            return "";
        }

        /**
         * 读文件
         *
         * @param file 待读取的文件
         * @return
         */
        public static String readFile(String file) {
            return readFile(file, "UTF-8");
        }
    }
}
