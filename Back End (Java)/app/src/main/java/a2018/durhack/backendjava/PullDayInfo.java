
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class PullDayInfo{
    /*
    This class, when called, will pull timetable information from the Durham timetable website.
    */
    
    private List<ActivityItem> dailyActivities = new ArrayList<>();

    PullDayInfo(String courseCode, int dayNumber){
        
        /*
        On construction, get the table from the internet and take the info from that day.
        A constructor cannot return a value.
        Note the following 'translation':
        dayNumber = 0 ==> Monday
        dayNumber = 1 ==> Tuesday
        dayNumber = 2 ==> Wednesday
        dayNumber = 3 ==> Thursday
        dayNumber = 4 ==> Friday
        dayNumber = 5 ==> Saturday (not used)
        dayNumber = 6 ==> Sunday (also not used)
        */

        final Document courseWeek;

        try {
            //Generate the correct URL and pull the HTML from the site.
            String urlSecondHalf = "%0D%0A?days=1-5&weeks=&periods=5-41&template=module+textspreadsheet&height=100&week=100";
            String urlFirstHalf = "https://timetable.dur.ac.uk/reporting/textspreadsheet;module;name;";

            username = "wthw55";
            password = "ImNotThatStupid";
            String totalLogin = username + ":" + password;
            byte[] byteData = totalLogin.getBytes("UTF-8");
            String encodedData = Base64.encodeToString(byteData, Base64.DEFAULT);
             courseWeek = Jsoup.connect(urlFirstHalf + courseCode + urlSecondHalf)
                    .header("Authorization", "Basic " + encodedData)
                    .get();

            //Filter out the relevant table. Here we exploit the fact that the relevant tables have a border size of 1 whereas irrelevant ones don't.
            Elements tables = courseWeek.select("table[border=1]");

            //From the list of tables, select the table corresponding to the day queried.
            Elements rows = tables.get(dayNumber).select("tr");

            //We now iterate over all the rows in this day to pull out relevant information.
            for (Element row : rows) {
                //Ensure we're using the right table by requiring the number of columns to match expectations.
                if (row.select("td").size() == 10) {
                    //We have the correct table.
                    String Activity = row.select("td").get(0).text();
                    String Start = row.select("td").get(3).text();
                    String Duration = row.select("td").get(5).text();
                    String Room = row.select("td").get(6).text();
                    String Staff = row.select("td").get(7).text();

                    //Remove the case of the table header and populate the lectureEntries ArrayList.
                    if (!Activity.equals("Activity")) {
                        //This isn't a header, we can add it to the ArrayList.
                        //Check what sort of activities they are.

                        //If the staff field has "Physpoolstaff01", we replace the staff with "Various" because it's probably less intimidating.
                        if (Staff.contains("Physpoolstaff")) {
                            Staff = "Various";
                        }

                        //Then, filter out multiple teachers, just have the first.
                        //There are some cases where "Physpoolstaff01" also had a teacher, so this comes after.
                        if (Staff.indexOf(',') >= 0) {
                            //We have two (or more) names. We only take the first, else it makes the entry messy.
                            Staff = Staff.substring(0, Staff.indexOf(','));
                        }

                        //Remove the D/ from the room names. Not sure why they're there in the first place.
                        Room = Room.replace("D/", "");

                        //We now sort by activity type. At the moment we only consider lectures, tutorials and practicals.
                        // WORKA, WORK are currently assumed to be practicals.
                        if (Activity.contains("LECT")) {
                            //This activity is a lecture.
                            Activity = "Lecture";
                            timetableEntries.add(new TimetableEntry(Activity, Start, Duration, Room, Staff));
                        } else if (Activity.contains("TUT")) {
                            //This is a tutorial.
                            //Note we opted to explicitly change room to "Various" here as there were strange cases where it wouldn't change properly.
                            Activity = "Tutorial";
                            Room = "Various";
                            timetableEntries.add(new TimetableEntry(Activity, Start, Duration, Room, Staff));
                        } else if (Activity.contains("PRAC")) {
                            //It's a lab practical.
                            Activity = "Labs";
                            Room = "Labs";
                            timetableEntries.add(new TimetableEntry(Activity, Start, Duration, Room, Staff));
                        } else if (Activity.contains("WORK")) {
                            //This will filter out both "WORK" and "WORKA", so we use a further filter.
                            if (Activity.contains("WORKA")) {
                                //This is the "WORKA" activity ONLY.
                                //We therefore set the Room to various explicitly, using similar justification as for tutorials.
                                Activity = "Lecture";
                                Room = "Various";
                                timetableEntries.add(new TimetableEntry(Activity, Start, Duration, Room, Staff));
                            } else {
                                //This is the "WORK" activity only.
                                //The "WORK" activity had only one staff member and room so it is left as-is.
                                Activity = "Lecture";
                                timetableEntries.add(new TimetableEntry(Activity, Start, Duration, Room, Staff));
                            }
                        } else {
                            //This is something we did not see when testing.
                            Activity = "Other";
                            timetableEntries.add(new TimetableEntry(Activity, Start, Duration, Room, Staff));
                        }
                    }
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        Log.e("DailyTimetableData", "Error while accessing timetable: " + e.toString());
        timetableEntries.add(new TimetableEntry("ERROR", "ERROR", "ERROR", "ERROR", "ERROR"));
    }

    //Check if the day is empty. If so, create an entry which can be 'translated' in the adapter.
    if (timetableEntries.size() == 0) {
        timetableEntries.add(new TimetableEntry("EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY"));
    }
}
}
}