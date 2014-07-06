package option;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sourceforge.peers.media.AbstractSoundManager;

public class RecordManager extends AbstractSoundManager{

	private ByteArrayOutputStream byteArrayOutputStream;
	
	@Override
	public byte[] readData() {
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		return byteArray;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init() {
		byteArrayOutputStream = new ByteArrayOutputStream();
	}

	@Override
	public int writeData(byte[] bytes, int off, int len) {
		try {
			byteArrayOutputStream.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
