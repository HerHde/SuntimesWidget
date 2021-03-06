/**
    Copyright (C) 2014-2018 Forrest Guice
    This file is part of SuntimesWidget.

    SuntimesWidget is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SuntimesWidget is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SuntimesWidget.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.forrestguice.suntimeswidget.notes;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.forrestguice.suntimeswidget.R;
import com.forrestguice.suntimeswidget.SuntimesUtils;
import com.forrestguice.suntimeswidget.calculator.SuntimesRiseSetDataset;
import com.forrestguice.suntimeswidget.settings.SolarEvents;
import com.forrestguice.suntimeswidget.settings.WidgetSettings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * The second implementation of SuntimesNotes; it does the same as the first (but hopefully a little cleaner).
 */
@SuppressWarnings("Convert2Diamond")
public class SuntimesNotes3 implements SuntimesNotes
{
    protected static final SuntimesUtils utils = new SuntimesUtils();

    private ArrayList<NoteData> notesList;

    private int noteIndex = 0;
    private NoteChangedListener changedListener;
    private NoteData currentNote = null;

    private Context context;
    private SuntimesRiseSetDataset dataset;

    public SuntimesNotes3()
    {
        changedListener = new NoteChangedListener()
        {
            @Override
            public void onNoteChanged(NoteData note, int transition) { /* default: do nothing */ }
        };
    }

    @Override
    public void init(Context context, SuntimesRiseSetDataset dataset)
    {
        this.context = context;
        this.dataset = dataset;

        notesList = new ArrayList<NoteData>();
        for (SolarEvents event : SolarEvents.values())
        {
            NoteData note = createNote(event);
            notesList.add(note);
        }
        updateNotes(dataset.now());
    }

    @Override
    public boolean isInitialized()
    {
        return (context != null);
    }

    @Override
    public int noteCount()
    {
        return notesList.size();
    }

    @Override
    public int getNoteIndex()
    {
        if (currentNote != null)
        {
            return notesList.indexOf(currentNote);
        }
        return noteIndex;
    }

    @Override
    public boolean setNoteIndex(int noteIndex)
    {
        if (noteIndex >=0 && noteIndex < notesList.size())
        {
            this.noteIndex = noteIndex;
            NoteData note = notesList.get(noteIndex);
            updateNote(note, dataset.now());
            setNote(note, NoteChangedListener.TRANSITION_NEXT);
            return true;
        }
        return false;
    }

    @Override
    public boolean showNote( SolarEvents.SolarEventField forField )
    {
        return false;
    }

    @Override
    public NoteData getNote()
    {
        if (currentNote != null)
        {
            updateNote(currentNote, dataset.now());
        }
        return currentNote;
    }

    @Override
    public NoteData getNote(int noteIndex)
    {
        if (noteIndex >=0 && noteIndex < notesList.size())
        {
            NoteData note = notesList.get(noteIndex);
            updateNote(note, dataset.now());
            return note;
        }
        return null;
    }

    /**
     * Switch to the next note (in ordered set of notes).
     * @return true if the note was changed, false otherwise
     */
    @Override
    public boolean showNextNote()
    {
        if (dataset.isCalculated())
        {
            SolarEvents currentNoteMode = WidgetSettings.loadTimeNoteRisePref(context, 0);
            int currentNote = currentNoteMode.ordinal();

            int nextNote = 0;
            if (currentNote < SolarEvents.values().length - 1)
                nextNote = currentNote + 1;

            SolarEvents nextNoteMode = SolarEvents.values()[nextNote];
            WidgetSettings.saveTimeNoteRisePref(context, AppWidgetManager.INVALID_APPWIDGET_ID, nextNoteMode);

            //Log.d("showNextNote", "... current = " + currentNote + ", next = " + nextNote + ", mode = " + nextNoteMode.name());
            updateNote(context, dataset.now(), NoteChangedListener.TRANSITION_NEXT);
            return true;

        } else {
            Log.w("showNextNote", "called before data was calculated!");
            return false;
        }
    }

    /**
     * Switch to the previous note (in ordered set of notes).
     * @return true if the note was changed, false otherwise
     */
    @Override
    public boolean showPrevNote()
    {
        if (dataset.isCalculated())
        {
            SolarEvents currentNoteMode = WidgetSettings.loadTimeNoteRisePref(context, AppWidgetManager.INVALID_APPWIDGET_ID);
            int currentNote = currentNoteMode.ordinal();

            int prevNote = SolarEvents.values().length - 1;
            if (currentNote > 0)
            {
                prevNote = currentNote - 1;
            }

            SolarEvents prevNoteMode = SolarEvents.values()[prevNote];
            WidgetSettings.saveTimeNoteRisePref(context, AppWidgetManager.INVALID_APPWIDGET_ID, prevNoteMode);
            updateNote(context, dataset.now(), NoteChangedListener.TRANSITION_PREV);
            return true;

        } else {
            Log.w("showPreviousNote", "called before data was calculated!");
            return false;
        }
    }

    @Override
    public boolean hasNextNote()
    {
        return noteIndex < notesList.size() - 1;
    }

    @Override
    public boolean hasPrevNote()
    {
        return noteIndex > 0;
    }

    @Override
    public void setOnChangedListener(NoteChangedListener listener)
    {
        changedListener = listener;
    }

    @Override
    public NoteChangedListener getOnChangedListener()
    {
        return changedListener;
    }

    public void updateNotes(Calendar now)
    {
        for (NoteData note : notesList)
        {
            updateNote(note, now);
        }
    }

    /**
     * Create an empty note for a given SolarEvent.
     * @param event the SolarEvent the note will display
     * @return a note object with icon, color, untilString, and noteString set (timestring empty).
     */
    private NoteData createNote(SolarEvents event)
    {
        int noteIcon;
        int noteColor;
        String untilString;
        String noteString;

        switch (event)
        {
            case MORNING_ASTRONOMICAL:
                noteIcon = R.drawable.ic_sunrise_large;
                noteColor = ContextCompat.getColor(context, R.color.sunIcon_color_rising);
                untilString = context.getString(R.string.until);
                noteString = context.getString(R.string.until_astroTwilight);
                break;
            case MORNING_NAUTICAL:
                noteIcon = R.drawable.ic_sunrise_large;
                noteColor = ContextCompat.getColor(context, R.color.sunIcon_color_rising);
                untilString = context.getString(R.string.until);
                noteString = context.getString(R.string.until_nauticalTwilight);
                break;
            case MORNING_CIVIL:
                noteIcon = R.drawable.ic_sunrise_large;
                noteColor = ContextCompat.getColor(context, R.color.sunIcon_color_rising);
                untilString = context.getString(R.string.until);
                noteString = context.getString(R.string.until_civilTwilight);
                break;
            case SUNRISE:
                noteIcon = R.drawable.ic_sunrise_large;
                noteColor = ContextCompat.getColor(context, R.color.sunIcon_color_rising);
                untilString = context.getString(R.string.until);
                noteString = context.getString(R.string.until_sunrise);
                break;

            case NOON:
                noteIcon = R.drawable.ic_noon_large;
                noteColor = ContextCompat.getColor(context, R.color.sunIcon_color_setting);
                untilString = context.getString(R.string.until);
                noteString = context.getString(R.string.until_noon);
                break;

            case SUNSET:
                noteIcon = R.drawable.ic_sunset_large;
                noteColor = ContextCompat.getColor(context, R.color.sunIcon_color_setting);
                untilString = context.getString(R.string.until);
                noteString = context.getString(R.string.until_sunset);
                break;
            case EVENING_CIVIL:
                noteIcon = R.drawable.ic_sunset_large;
                noteColor = ContextCompat.getColor(context, R.color.sunIcon_color_setting);
                untilString = context.getString(R.string.until_end);
                noteString = context.getString(R.string.untilEnd_civilTwilight);
                break;
            case EVENING_NAUTICAL:
                noteIcon = R.drawable.ic_sunset_large;
                noteColor = ContextCompat.getColor(context, R.color.sunIcon_color_setting);
                untilString = context.getString(R.string.until_end);
                noteString = context.getString(R.string.untilEnd_nauticalTwilight);
                break;
            case EVENING_ASTRONOMICAL:
            default:
                noteIcon = R.drawable.ic_sunset_large;
                noteColor = ContextCompat.getColor(context, R.color.sunIcon_color_setting);
                untilString = context.getString(R.string.until_end);
                noteString = context.getString(R.string.untilEnd_astroTwilight);
                break;
        }

        SuntimesUtils.TimeDisplayText timeString = new SuntimesUtils.TimeDisplayText();
        return new NoteData(event, timeString, untilString, noteString, noteIcon, noteColor);
    }

    private String prefixString(SolarEvents event, boolean useSince)
    {
        String prefix;
        if (useSince)
        {
            prefix = context.getString(R.string.since);

        } else {
            switch (event)
            {
                case MORNING_ASTRONOMICAL:
                    prefix = context.getString(R.string.until);
                    break;
                case MORNING_NAUTICAL:
                    prefix = context.getString(R.string.until);
                    break;
                case MORNING_CIVIL:
                    prefix = context.getString(R.string.until);
                    break;
                case SUNRISE:
                    prefix = context.getString(R.string.until);
                    break;
                case NOON:
                    prefix = context.getString(R.string.until);
                    break;
                case SUNSET:
                    prefix = context.getString(R.string.until);
                    break;
                case EVENING_CIVIL:
                    prefix = context.getString(R.string.until_end);
                    break;
                case EVENING_NAUTICAL:
                    prefix = context.getString(R.string.until_end);
                    break;
                case EVENING_ASTRONOMICAL:
                default:
                    prefix = context.getString(R.string.until_end);
                    break;
            }
        }
        return prefix;
    }

    /**
     * Update a note with respect to given time 'now'.
     * @param note the note object to be updated
     * @param now the time to update the note against
     */
    private void updateNote(NoteData note, Calendar now)
    {
        Calendar date, dateOther;
        switch (note.noteMode)
        {
            case MORNING_ASTRONOMICAL:
                date = dataset.dataAstro.sunriseCalendarToday();
                dateOther = dataset.dataAstro.sunriseCalendarOther();
                break;
            case MORNING_NAUTICAL:
                date = dataset.dataNautical.sunriseCalendarToday();
                dateOther = dataset.dataNautical.sunriseCalendarOther();
                break;
            case MORNING_CIVIL:
                date = dataset.dataCivil.sunriseCalendarToday();
                dateOther = dataset.dataCivil.sunriseCalendarOther();
                break;
            case SUNRISE:
                date = dataset.dataActual.sunriseCalendarToday();
                dateOther = dataset.dataActual.sunriseCalendarOther();
                break;

            case NOON:
                date = dataset.dataNoon.sunriseCalendarToday();
                dateOther = dataset.dataNoon.sunriseCalendarOther();
                break;

            case SUNSET:
                date = dataset.dataActual.sunsetCalendarToday();
                dateOther = dataset.dataActual.sunsetCalendarOther();
                break;
            case EVENING_CIVIL:
                date = dataset.dataCivil.sunsetCalendarToday();
                dateOther = dataset.dataCivil.sunsetCalendarOther();
                break;
            case EVENING_NAUTICAL:
                date = dataset.dataNautical.sunsetCalendarToday();
                dateOther = dataset.dataNautical.sunsetCalendarOther();
                break;
            case EVENING_ASTRONOMICAL:
            default:
                date = dataset.dataAstro.sunsetCalendarToday();
                dateOther = dataset.dataAstro.sunsetCalendarOther();
                break;
        }

        Date eventTime = null;
        Date time = now.getTime();

        boolean afterToday = (date == null || time.after(date.getTime()));
        if (afterToday)
        {
            if (dateOther != null)
            {
                eventTime = dateOther.getTime();
            }
        } else {
            eventTime = date.getTime();
        }

        note.tomorrow = afterToday;
        note.timeText = utils.timeDeltaDisplayString(time, eventTime);
        note.prefixText = prefixString(note.noteMode, (note.timeText.getRawValue() < 0));
        note.time = eventTime;
    }

    public void resetNoteIndex()
    {
        Calendar now = dataset.now();
        Date time = now.getTime();
        long nearestTime = -1;
        NoteData nearestNote = notesList.get(0);

        for (NoteData note : notesList)
        {
            if (note.time != null)
            {
                long timeUntil = note.time.getTime() - time.getTime();
                if (timeUntil < nearestTime || nearestTime < 0)
                {
                    nearestTime = timeUntil;
                    nearestNote = note;
                }
            }
        }

        //Log.d("resetNoteIndex", "reset to " + nearestNote.noteMode);
        WidgetSettings.saveTimeNoteRisePref(context, 0, nearestNote.noteMode);
    }


    @Override
    public void updateNote(Context context)
    {
        updateNote(context, dataset.now(), NoteChangedListener.TRANSITION_NONE);
    }

    @Override
    public void updateNote(Context context, Calendar now)
    {
        updateNote(context, now, NoteChangedListener.TRANSITION_NONE);
    }

    @Override
    public void updateNote(Context context, Calendar now, int transition)
    {
        int choice = WidgetSettings.loadTimeNoteRisePref(context, AppWidgetManager.INVALID_APPWIDGET_ID).ordinal();
        NoteData chosenNote = notesList.get(choice);

        NoteData updatedNote = new NoteData(chosenNote);
        updateNote(updatedNote, now);

        if (currentNote == null || !currentNote.equals(updatedNote))
        {
            //Log.d("updateNote", "changing the note to " + updatedNote.toString() + "[" + choice + "]");
            setNote(updatedNote, NoteChangedListener.TRANSITION_NEXT);
        }
    }

    @Override
    public void setNote(NoteData note, int transition)
    {
        currentNote = note;
        changedListener.onNoteChanged(currentNote, transition);
    }
}
