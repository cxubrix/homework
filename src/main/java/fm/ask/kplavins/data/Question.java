package fm.ask.kplavins.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    private QuestionState state;
    private Date created;
    private String country;

    @JsonIgnore
    private String ip;

    public Question() {
        this(QuestionState.NEW, new Date());
    }

    public Question(QuestionState state, Date created) {
        this.state = state;
        this.created = created;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public QuestionState getState() {
        return state;
    }

    public void setState(QuestionState state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCountry() {
        return country;
    }

    public Long getId() {
        return id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Question [id=" + id + ", content=" + content + ", state=" + state + ", created=" + created
                + ", country=" + country + ", ip=" + ip + "]";
    }

}
