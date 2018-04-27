package net.sea.simple.rpc.data.codec;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * rpc服务编码器
 * 
 * @author sea
 *
 */
@Sharable
public class MarshallingEncoder {

	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	private final Marshaller marshaller;

	public MarshallingEncoder() throws IOException {
		marshaller = MarshallingCodecFactory.buildMarshalling();
	}

	protected void encode(Object msg, ByteBuf out) throws Exception {
		try {
			int lengthPos = out.writerIndex();
			out.writeBytes(LENGTH_PLACEHOLDER);
			ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
			marshaller.start(output);
			marshaller.writeObject(msg);
			marshaller.finish();
			out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
		} finally {
			marshaller.close();
		}
	}
}
