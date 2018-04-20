package net.sea.simple.rpc.data.codec;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

/**
 * Marshalling序列化工厂
 * 
 * @author sea
 *
 */
public final class MarshallingCodecFactory {

	/**
	 * 创建Jboss Marshaller
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Marshaller buildMarshalling() throws IOException {
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		Marshaller marshaller = marshallerFactory.createMarshaller(configuration);
		return marshaller;
	}

	/**
	 * 创建Jboss Unmarshaller
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Unmarshaller buildUnMarshalling() throws IOException {
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		final Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(configuration);
		return unmarshaller;
	}
}
