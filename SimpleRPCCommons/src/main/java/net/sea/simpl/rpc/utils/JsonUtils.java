package net.sea.simpl.rpc.utils;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * JSON工具类
 * 
 * @author sea
 *
 */
public class JsonUtils {
	private static Gson GSON_INST = createGson();

	/**
	 * 创建GSON对象
	 * 
	 * @return
	 */
	private static Gson createGson() {
		return createGson("yyyy-MM-dd HH:mm:ss:SSS");
	}

	/**
	 * 创建GSON对象
	 * 
	 * @param datePattern
	 * @return
	 */
	private static Gson createGson(String datePattern) {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation() // 不对没有用@Expose注解的属性进行操作
				.enableComplexMapKeySerialization() // 当Map的key为复杂对象时,需要开启该方法
				.serializeNulls() // 当字段值为空或null时，依然对该字段进行转换
				.setDateFormat(datePattern) // 时间转化为特定格式
				.setPrettyPrinting() // 对结果进行格式化，增加换行
				.disableHtmlEscaping() // 防止特殊字符出现乱码
				// .registerTypeAdapter(User.class, new UserAdapter()) //
				// 为某特定对象设置固定的序列或反序列方式，自定义Adapter需实现JsonSerializer或者JsonDeserializer接口
				.create();
	}

	/**
	 * 转换为json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		return GSON_INST.toJson(obj);
	}

	/**
	 * json数据转换为实体对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T toBean(String json, Class<T> clazz) {
		return GSON_INST.fromJson(json, clazz);
	}

	/**
	 * json数据转换为实体对象
	 * 
	 * @param json
	 * @param token
	 * @return
	 */
	public static <T> T toBean(String json, TypeToken<T> token) {
		return GSON_INST.fromJson(json, token.getType());
	}

	public static void main(String[] args) {
		String json = "[1,2,34,435,-10]";
		System.out.println(toBean(json, new TypeToken<List<Integer>>() {
		}));
	}
}
