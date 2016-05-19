package Send;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sendclasslist")
@XmlAccessorType(XmlAccessType.FIELD)
public class SendClassList {

    @XmlElement(name = "sendclass")
    private List<SendClass> sendclass = null;

    public List<SendClass> getSendClassList() {
        return sendclass;
    }

    public void setSendClassList(List<SendClass> sendclass) {
        this.sendclass = sendclass;
    }

    @XmlElement(name = "groupName")
    private String groupName = null;

    public String getGroupName(){
        return groupName;

    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    @XmlElement(name = "BPM")
    private Integer BPM = null;

    public Integer getBPM(){
        return BPM;
    }

    public void setBPM(Integer BPM){

        this.BPM = BPM;
    }

}