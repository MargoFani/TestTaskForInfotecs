package task;


import org.springframework.context.annotation.Primary;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;

@Table(name = "note")
@Entity
public class Note {
    @Id
    private String key;
    private String note;
    private LocalDateTime ttl;

    public Note() {
    }

    public Note(String key, String note, LocalDateTime ttl) {
        this.key = key;
        this.note = note;
        this.ttl = ttl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getTtl() {
        return ttl;
    }

    public void setTtl(LocalDateTime ttl) {
        this.ttl = ttl;
    }


    @Override
    public String toString() {
        return "{" +
                "\"key\":\"" + key + '\"' +
                ", \"note\":\"" + note + '\"' +
                ", \"ttl\":\"" + ttl + '\"' +
                '}';
    }
}
