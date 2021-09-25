package task;


import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {
    private final String workFold = "backups";
    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    @Override
    //get all exists notes
    public List<Note> getAll() {
        delete();

        return noteRepKository.findAll();
    }

    @Override
    //find note by key
    //if not exist trows exception
    public Note getByKey(String key) {
        delete();

        return noteRepository.findAll().stream().filter(x -> x.getKey().equals(key)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Not found key = " + key));
    }

    @Override
    //set new note
    //if note with the same key is already exist, then rewrite it
    public boolean set(String key, String note, Duration ttl) {
        //delete notes with expired  lifetime
        delete();

        Note tNote = new Note(key, note, LocalDateTime.now().plus(ttl));
        //find note with the same key
        Long count = noteRepository.findAll().stream().filter(x -> x.getKey().equals(key)).count();
        //if the same key exist
        if (count != 0) {
            //delete exist note
            noteRepository.delete(getByKey(key));
        }
        //save new note
        noteRepository.save(tNote);
        return true;
    }

    @Override
    //remove note by key from repository
    public Note remove(String key) {

        //delete notes with expired  lifetime
        delete();
        //find in repository note by key
        Note tNote = getByKey(key);
        //delete note
        noteRepository.delete(tNote);
        //return removed note
        return tNote;
    }

    @Override
    //save current state of the repository into txt file
    public boolean dump(String fileName) {
        //create file
        File dataBase = new File(fileName);
        //write repository into file
        try (FileWriter writer = new FileWriter(dataBase, true)) {
            List<Note> notes = getAll();
            for (Note note : notes
            ) {
                writer.write(note.toString() + "\n");
            }
            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
        return true;
    }

    @Override
    //load state of the repository from txt file
    public boolean load(String fileName) {
        //array of lanes of text
        List<String> notes = new ArrayList<String>();
        //backup of repository
        List<Note> dbBackup = getAll();
        try {
            //read file by lines
            File dataBase = new File(fileName);

            FileReader fileReader = new FileReader(dataBase);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = reader.readLine();
            while (line != null) {
                notes.add(line);
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //delete exist repository
            noteRepository.deleteAll();
            for (String n : notes
            ) {
                //line of text into entity Note
                Object obj = JSONValue.parse(n);
                JSONObject json = (JSONObject) obj;
                String no = (String) json.get("note");
                String ke = (String) json.get("key");
                LocalDateTime tt = LocalDateTime.parse((String) json.get("ttl"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                Note note = new Note(ke, no, tt);
                noteRepository.save(note);
            }
        } catch (Exception e) {
            //if something goes wrong uses backup of repository
            noteRepository.deleteAll();
            noteRepository.saveAll(dbBackup);
            e.printStackTrace();
        }
        //delete notes with expired lifetime
        delete();

        return true;
    }

    //method delete notes with expired lifetime (ghosts)
    public boolean delete() {
        List<Note> ghosts = noteRepository.findAll().stream().filter(x -> x.getTtl().isBefore(LocalDateTime.now())).collect(Collectors.toList());
        if (ghosts != null) {
            noteRepository.deleteAll(ghosts);
        }
        return true;
    }
}
