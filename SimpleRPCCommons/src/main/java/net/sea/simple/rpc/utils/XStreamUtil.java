package net.sea.simple.rpc.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.thoughtworks.xstream.security.AnyTypePermission;

public class XStreamUtil {
    private static String XML_TAG = "<?xml version='1.0' encoding='UTF-8'?>";

    private XStreamUtil() {}

    /**
     * 获取xstream实例
     * 
     * @return
     */
    private static XStream getInstance() {
        XStream xStream = new XStream(new DomDriver("UTF-8")) {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {

                    @SuppressWarnings("rawtypes")
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            return false;
                        }
                        return super.shouldSerializeMember(definedIn, fieldName);
                    }

                };
            }

        };

        // 设置默认的安全校验
        XStream.setupDefaultSecurity(xStream);
        // 使用本地的类加载器
        xStream.setClassLoader(XStreamUtil.class.getClassLoader());
        // 允许所有的类进行转换
        xStream.addPermission(AnyTypePermission.ANY);
        return xStream;
    }

    /**
     * xml转换为对象
     * 
     * @param xml
     * @param clazz
     * @return
     */
    public static <T> T xmlToBean(String xml, Class<T> clazz) {
        if (xml == null || xml.length() == 0) {
            return null;
        }
        XStream xStream = getInstance();
        xStream.processAnnotations(clazz);
        return clazz.cast(xStream.fromXML(xml));
    }

    /**
     * 对象转换为xml
     * 
     * @param obj
     * @return
     */
    public static String beanToXml(Object obj) {
        if (obj == null) {
            return null;
        }
        XStream xStream = getInstance();
        xStream.processAnnotations(obj.getClass());
        // 剔除所有tab、制表符、换行符
        return xStream.toXML(obj).replaceAll("\\s+", " ");
    }

    /**
     * 对象转换为xml（包含xml头部信息）
     * 
     * @param obj
     * @return
     */
    public static String beanToXmlWithTag(Object obj) {
        return new StringBuilder().append(XML_TAG).append(beanToXml(obj)).toString();
    }
}
