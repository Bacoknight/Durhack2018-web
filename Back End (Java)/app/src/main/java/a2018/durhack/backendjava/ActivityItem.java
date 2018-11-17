class ActivityItem{
    /*
    This class defines a single activity in the timetable, such as a single lecture or seminar.
    */

    private String activityType;
    private String startTime;
    private int durationHours;
    private String roomName;
    private String buildingName;
    private String teacherName;

    ActivityItem(String activityType, String startTime, int durationHours, String roomName, String buildingName,
     String teacherName){
         //This constructor will populate the variables of this class on creation.
         this.activityType = activityType;
         this.startTime = startTime;
         this.durationHours = durationHours;
         this.roomName = roomName;
         this.buildingName = buildingName;
         this.teacherName = teacherName;
     }

     //Getters are implemented below.
     String getActivityType(){
         return activityType;
     }

     String getStartTime(){
         return startTime;
     }
     
     int getDuration(){
         return durationHours;
     }

     String getRoomName(){
         return roomName;
     }
}