package Send;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sendclass")
public class SendClass {

	private String groupName;
	private String instrumentName;
	private String data;
	private String volume;
	
	public String getGroupName() {
		return groupName;
	}

	@XmlElement
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getInstrumentName() {
		return instrumentName;
	}

	@XmlElement
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	
	public String getData() {
		return data;
	}

	@XmlElement
	public void setData(String data) {
		this.data = data;
	}

	public String getVolume() {
		return volume;
	}

	@XmlElement
	public void setVolume(String volume) {
		this.volume = volume;
	}

	
}