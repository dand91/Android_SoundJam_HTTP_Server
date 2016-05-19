package Send;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sendclass")
public class SendClass {


    private String instrumentName;
    private String data;
    private String volume;
    private Integer bars;
    private Boolean hasData;

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

    public Integer getBars() {
        return bars;
    }

    @XmlElement
    public void setBars(Integer bars) {
        this.bars = bars;
    }

    public Boolean getHasData() {
        return hasData;
    }

    @XmlElement
    public void setHasData(Boolean hasData) {
        this.hasData = hasData;
    }


}