package net.sea.simple.rpc.data.codec;

import java.io.IOException;
import java.io.StreamCorruptedException;

import net.sea.simple.rpc.exception.RPCServerException;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import io.netty.buffer.ByteBuf;

/**
 * RPC服务解码器
 * 
 * @author sea
 *
 */
public class MarshallingDecoder {

	private final Unmarshaller unmarshaller;

	/**
	 * Creates a new decoder whose maximum object size is {@code 1048576} bytes.
	 * If the size of the received object is greater than {@code 1048576} bytes,
	 * a {@link StreamCorruptedException} will be raised.
	 * 
	 * @throws IOException
	 * 
	 */
	public MarshallingDecoder() throws IOException {
		unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
	}

	@SuppressWarnings("unchecked")
	public <T> T decode(ByteBuf in) throws RPCServerException {
		int objectSize = in.readInt();
		ByteBuf buf = in.slice(in.readerIndex(), objectSize);
		ByteInput input = new ChannelBufferByteInput(buf);
		try {
			unmarshaller.start(input);
			Object obj = unmarshaller.readObject();
			unmarshaller.finish();
			in.readerIndex(in.readerIndex() + objectSize);
			return (T) obj;
		} catch (Exception e) {
			throw new RPCServerException(e);
		} finally {
			try {
				unmarshaller.close();
			} catch (IOException e) {
				throw new RPCServerException(e);
			}
		}
	}
}
