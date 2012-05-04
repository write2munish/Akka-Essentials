package org.akka.essentials.serializer;

import akka.serialization.JSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MySerializer extends JSerializer {
	// create the Gson object
	private Gson gson = new GsonBuilder().serializeNulls().create();

	public int identifier() {
		return 12062010;
	}

	public boolean includeManifest() {
		return true;
	}

	/**
	 * "toBinary" serializes the given object to an Array of Bytes
	 * 
	 * @param arg0
	 * @return
	 */
	public byte[] toBinary(Object arg0) {

		return gson.toJson(arg0).getBytes();

	}

	/**
	 * "fromBinary" deserializes the given array, using the type hint (if any,
	 * see "includeManifest" above) into the optionally provided classLoader.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	@Override
	public Object fromBinaryJava(byte[] arg0, Class<?> arg1) {

		return gson.fromJson(new String(arg0), arg1);

	}

}
