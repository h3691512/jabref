package net.sf.jabref.specialfields;

import java.util.List;
import java.util.Optional;

import net.sf.jabref.gui.undo.NamedCompound;
import net.sf.jabref.model.FieldChange;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.preferences.JabRefPreferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.sf.jabref.Globals.prefs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SpecialFieldsUtilsTest {

    private JabRefPreferences backup;

    @Before
    public void setUp() {
        prefs = JabRefPreferences.getInstance();
        backup = prefs;

        prefs.putBoolean(JabRefPreferences.SPECIALFIELDSENABLED, true);
        prefs.putBoolean(JabRefPreferences.AUTOSYNCSPECIALFIELDSTOKEYWORDS, true);
    }

    @After
    public void tearDown() {
        //clean up preferences to default state
        prefs.overwritePreferences(backup);
    }

    @Test
    public void testSyncKeywordsFromSpecialFieldsBibEntry() {
        BibEntry entry = new BibEntry();
        entry.setField("ranking", "rank2");
        SpecialFieldsUtils.syncKeywordsFromSpecialFields(entry, true, ',');
        assertEquals(Optional.of("rank2"), entry.getField("keywords"));
    }

    @Test
    public void testSyncKeywordsFromSpecialFieldsBibEntryNamedCompoundHasEdits() {
        BibEntry entry = new BibEntry();
        entry.setField("ranking", "rank2");
        List<FieldChange> changes = SpecialFieldsUtils.syncKeywordsFromSpecialFields(entry, true, ',');
        assertTrue(changes.size() > 0);
    }

    @Test
    public void testSyncKeywordsFromSpecialFieldsBibEntryExisitingKeyword() {
        BibEntry entry = new BibEntry();
        entry.setField("ranking", "rank2");
        entry.setField("keywords", "rank3");
        SpecialFieldsUtils.syncKeywordsFromSpecialFields(entry, true, ',');
        assertEquals(Optional.of("rank2"), entry.getField("keywords"));
    }

    @Test
    public void testSyncKeywordsFromSpecialFieldsBibEntryNamedCompoundCorrectContent() {
        BibEntry entry = new BibEntry();
        entry.setField("ranking", "rank2");
        SpecialFieldsUtils.syncKeywordsFromSpecialFields(entry, true, ',');
        assertEquals(Optional.of("rank2"), entry.getField("keywords"));
    }

    @Test
    public void testSyncKeywordsFromSpecialFieldsBibEntryNamedCompoundNoEdits() {
        BibEntry entry = new BibEntry();
        List<FieldChange> changes = SpecialFieldsUtils.syncKeywordsFromSpecialFields(entry, true, ',');
        assertFalse(changes.size() > 0);
    }

    @Test
    public void testSyncSpecialFieldsFromKeywordsBibEntry() {
        BibEntry entry = new BibEntry();
        entry.setField("keywords", "rank2");
        SpecialFieldsUtils.syncSpecialFieldsFromKeywords(entry, ',');
        assertEquals(Optional.of("rank2"), entry.getField("ranking"));
    }

    @Test
    public void testSyncSpecialFieldsFromKeywordsBibEntryNamedCompoundHasEdits() {
        BibEntry entry = new BibEntry();
        entry.setField("keywords", "rank2");
        List<FieldChange> changes = SpecialFieldsUtils.syncSpecialFieldsFromKeywords(entry, ',');
        assertTrue(changes.size() > 0);
    }

    @Test
    public void testSyncSpecialFieldsFromKeywordsBibEntryNamedCompoundCorrectContent() {
        BibEntry entry = new BibEntry();
        entry.setField("keywords", "rank2");
        SpecialFieldsUtils.syncSpecialFieldsFromKeywords(entry, ',');
        assertEquals(Optional.of("rank2"), entry.getField("ranking"));
    }

    @Test
    public void testSyncSpecialFieldsFromKeywordsBibEntryNamedCompoundNoEdit() {
        BibEntry entry = new BibEntry();
        List<FieldChange> changes = SpecialFieldsUtils.syncSpecialFieldsFromKeywords(entry, ',');
        assertFalse(changes.size() > 0);
    }

    @Test
    public void testGetSpecialFieldInstanceFromFieldNameValid() {
        assertEquals(Optional.of(SpecialField.RANK),
                SpecialFieldsUtils.getSpecialFieldInstanceFromFieldName("ranking"));
    }

    @Test
    public void testGetSpecialFieldInstanceFromFieldNameInvalid() {
        assertEquals(Optional.empty(), SpecialFieldsUtils.getSpecialFieldInstanceFromFieldName("title"));
    }

    @Test
    public void testIsSpecialFieldTrue() {
        assertTrue(SpecialFieldsUtils.isSpecialField("ranking"));
    }

    @Test
    public void testIsSpecialFieldFalse() {
        assertFalse(SpecialFieldsUtils.isSpecialField("title"));
    }
}
