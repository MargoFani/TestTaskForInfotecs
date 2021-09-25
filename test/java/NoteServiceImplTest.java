
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import task.Note;
import task.NoteRepository;
import task.NoteServiceImpl;


import java.time.Duration;
import java.time.LocalDateTime;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NoteServiceImplTest {
    List<Note> testNotes;

    @InjectMocks
    NoteServiceImpl noteService;
    @Mock
    NoteRepository testRepository;

    @Before
    public void setUp() {
        this.testNotes = List.of(
                new Note("testKey1", "note1", LocalDateTime.now().plus(Duration.ofHours(1))),
                new Note("testKey2", "note2", LocalDateTime.now().plus(Duration.ofHours(1))),
                new Note("testKey3", "note3", LocalDateTime.now().plus(Duration.ofHours(1))),
                new Note("testKey4", "note4", LocalDateTime.now().plus(Duration.ofHours(1)))
        );
    }

    @Test
    public void testGetAll() {

        given(testRepository.findAll()).willReturn(this.testNotes);
        List<Note> expected = noteService.getAll();
        assertEquals(expected, this.testNotes);
    }

    @Test
    public void testGetByKey() {

        given(testRepository.findAll()).willReturn(this.testNotes);
        Note expected = noteService.getByKey("testKey2");
        assertEquals(expected, this.testNotes.get(1));

    }

    @Test
    public void testRemove() {
        given(testRepository.findAll()).willReturn(this.testNotes);
        String key = "testKey1";
        noteService.remove(key);
        verify(testRepository).delete(this.testNotes.get(0));
    }

    @Test
    public void testSetKeyExists() {

        Note tNote = new Note("testKey4", "new note4", LocalDateTime.now().plus(Duration.ofHours(4)));

        given(testRepository.findAll()).willReturn(this.testNotes);


        noteService.set("testKey4", "new note4", Duration.ofHours(4));

        verify(testRepository).save(any(Note.class));
    }

    @Test
    public void testSetKeyNotExists() {

        Note tNote = new Note("testKey5", "new note5", LocalDateTime.now().plus(Duration.ofHours(4)));

        given(testRepository.findAll()).willReturn(this.testNotes);

        noteService.set("testKey5", "new note5", Duration.ofHours(4));
        verify(testRepository).save(any(Note.class));
    }

    @Test
    public void testDeleteGhost() {
        Note testSaveN1 = new Note("testKey1", "note1", LocalDateTime.now().plus(Duration.ofHours(1)));
        Note testSaveN2 = new Note("testKey2", "note2", LocalDateTime.now().plus(Duration.ofHours(1)));
        Note testRemoveN3 = new Note("testKey3", "note3", LocalDateTime.now().minus(Duration.ofHours(1)));
        Note testRemoveN4 = new Note("testKey4", "note4", LocalDateTime.now().minus(Duration.ofHours(1)));
        List<Note> beforeDel = List.of(testSaveN1, testSaveN2, testRemoveN3, testRemoveN4);
        List<Note> delList = List.of(testRemoveN3, testRemoveN4);

        given(testRepository.findAll()).willReturn(beforeDel);
        noteService.delete();

        verify(testRepository).deleteAll(delList);
    }
}
