package net.sf.jabref.specialfields;

import net.sf.jabref.Globals;
import net.sf.jabref.gui.undo.NamedCompound;
import net.sf.jabref.logic.l10n.Localization;
import net.sf.jabref.model.database.event.EntryAddedEvent;
import net.sf.jabref.model.entry.BibEntry;

import com.google.common.eventbus.Subscribe;

public class SpecialFieldDatabaseChangeListener {

    private static SpecialFieldDatabaseChangeListener INSTANCE;

    public static SpecialFieldDatabaseChangeListener getInstance() {
        if (SpecialFieldDatabaseChangeListener.INSTANCE == null) {
            SpecialFieldDatabaseChangeListener.INSTANCE = new SpecialFieldDatabaseChangeListener();
        }
        return SpecialFieldDatabaseChangeListener.INSTANCE;
    }

    @Subscribe
    public void listen(EntryAddedEvent event) {
        if (Globals.prefs.isKeywordSyncEnabled()) {
            final BibEntry entry = event.getBibEntry();
            // NamedCompount code similar to SpecialFieldUpdateListener
            NamedCompound nc = new NamedCompound(Localization.lang("Synchronized special fields based on keywords"));
            SpecialFieldsUtils.syncSpecialFieldsFromKeywords(entry, nc);
            // Don't insert the compound into the undoManager,
            // it would be added before the component which undoes the insertion of the entry and creates heavy problems
            // (which prohibits the undo the deleting multiple entries)
        }
    }

}
