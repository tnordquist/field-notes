package edu.cnm.deepdive.fieldnotes.service;

import android.content.Context;
import androidx.core.graphics.drawable.IconCompat.IconType;
import edu.cnm.deepdive.fieldnotes.model.dao.NoteDao;
import edu.cnm.deepdive.fieldnotes.model.entity.Note;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PlantRepository {

  private final Context context;
  private final NoteDao noteDao;

  public PlantRepository(Context context) {
    this.context = context;
    NotesDatabase database = NotesDatabase.getInstance();
    noteDao = database.getNoteDao();
  }

  public Single<Note> saveNote(Note note) {
    return (
        (note.getId() > 0)
            ? noteDao
            .update(note)
            .map(new Function<Integer, Note>() {
              @Override
              public Note apply(@NonNull Integer ignored) throws Exception {
                return note;
              }
            })
            : noteDao
                .insert(note)
          .map((id) -> {
            note.setId(id);
            return note;
          })
    )
        .subscribeOn(Schedulers.io());
  }
}
