package task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {

    private final NoteServiceImpl noteService;

    @Autowired
    public NoteController(NoteServiceImpl noteService) {
        this.noteService = noteService;
    }


    @GetMapping
    @Transactional
    public List<Note> getAll() {
        return noteService.getAll();
    }


    @GetMapping("/{key}")
    @Transactional
    public Note getByKey(@PathVariable("key") String key) {
        return noteService.getByKey(key);
    }


    @PostMapping("/set")
    @Transactional
    public boolean set(@RequestParam(value = "key") String key,
                       @RequestParam(value = "note") String note,
                       @RequestParam(value = "ttl", required = false, defaultValue = "PT3M") Duration ttl) {
        return noteService.set(key, note, ttl);
    }


    @DeleteMapping("/{key}")
    @Transactional
    public Note remove(@PathVariable("key") String key) {
        return noteService.remove(key);
    }


    @GetMapping("/dump/{name}")
    @Transactional
    public boolean dump(@PathVariable("name") String name) {
        return noteService.dump(name);

    }


    @PostMapping("/load/{name}")
    @Transactional
    public boolean load(@PathVariable("name") String name) {
        return noteService.load(name);

    }
}
