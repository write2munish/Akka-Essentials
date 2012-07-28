package akka.first.app.java.messages;

import java.util.List;

public class MapData {
	private List<WordCount> dataList;

	public List<WordCount> getDataList() {
		return dataList;
	}
	public MapData(List<WordCount> dataList) {
		this.dataList = dataList;
	}
}
