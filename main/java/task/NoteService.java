package task;

import java.time.Duration;
import java.util.List;

public interface NoteService {
    List<Note> getAll();

    Note getByKey(String key);

    boolean set(String key, String note, Duration ttl);

    Note remove(String key);

    boolean dump(String fileName);

    boolean load(String fileName);
}
