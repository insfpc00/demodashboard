package es.fporto.demo.data.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemState {
	FREE, UNAVAILABLE, SOLD;

	private static Map<String, ItemState> namesMap = new HashMap<String, ItemState>(3);

	static {
		namesMap.put("FREE", FREE);
		namesMap.put("UNAVAILABLE", UNAVAILABLE);
		namesMap.put("SOLD", SOLD);
	}

	@JsonValue
	public String toValue() {
		for (Entry<String, ItemState> entry : namesMap.entrySet()) {
			if (entry.getValue() == this)
				return entry.getKey();
		}
		return null;

	}

	@JsonCreator
	public static ItemState forValue(String value) {
		return namesMap.get(value);
	}

}