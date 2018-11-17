
/**
 * Takes some information about an academic timetable event and exports it to someone's Google Calendar.
 *
 * @author TacticalError
 * @version 0.1
 */

import com.google.api.services.calendar.*;

public class ExportEvent
{
    private String calendar;

    /**
     * Constructor for objects of class ExportEvent
     */
    public ExportEvent(String calendar)
    {
        this.calendar = calendar;
    }

   
    public int sampleMethod(String eventName, String eventType, String eventLoc, String eventPlace, CalendarColor color)
    {
        return 0;
    }
}
