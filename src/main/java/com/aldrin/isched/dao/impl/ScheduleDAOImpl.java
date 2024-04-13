/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.isched.dao.impl;

import com.aldrin.isched.dao.ScheduleDAO;
import com.aldrin.isched.model.CampusHead;
import com.aldrin.isched.model.Day;
import com.aldrin.isched.model.Instructor;
import com.aldrin.isched.model.Room;
import com.aldrin.isched.model.Schedule;
import com.aldrin.isched.model.School;
import com.aldrin.isched.model.SchoolYear;
import com.aldrin.isched.model.Section;
import com.aldrin.isched.model.Semester;
import com.aldrin.isched.model.Subject;
import com.aldrin.isched.model.Time;
import com.aldrin.isched.util.ComboBoxList;
import com.aldrin.isched.util.TimeValidator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ALDRIN B. C.
 */
@Setter
@Getter
public class ScheduleDAOImpl extends DBConnection implements ScheduleDAO {

    @Override
    public void addSchedule(Schedule schedule) {
        try {
            getDBConn();
            java.sql.PreparedStatement ps = getCon().prepareStatement("INSERT INTO SCHEDULE (ID,SCHOOL_YEAR_ID,INSTRUCTOR_ID,SUBJECT_ID,SECTION_ID, DAY_ID,TIME_ID,ROOM_ID,CAMPUS_HEAD_ID,SCHOOL_ID) VALUES  (?,?,?,?,?,?,?,?,?,?) ");
            ps.setLong(1, getMaxId());
            ps.setLong(2, schedule.getSchoolYear().getId());
            ps.setLong(3, schedule.getInstructor().getId());
            ps.setLong(4, schedule.getSubject().getId());
            ps.setLong(5, schedule.getSection().getId());
            ps.setLong(6, schedule.getDay().getId());
            ps.setLong(7, schedule.getTime().getId());
            ps.setLong(8, schedule.getRoom().getId());
            ps.setLong(9, schedule.getCampusHead().getId());
            ps.setLong(10, schedule.getSchool().getId());
            ps.execute();
            ps.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSchedule(Schedule schedule) {
        try {
            getDBConn();
            java.sql.PreparedStatement ps = getCon().prepareStatement("UPDATE SCHEDULE SET SCHOOL_YEAR_ID =?, INSTRUCTOR_ID =?, SUBJECT_ID=?,"
                    + " SECTION_ID =?,DAY_ID=?,TIME_ID =?,ROOM_ID=?  WHERE SCHEDULE.ID = ?");
            ps.setLong(1, schedule.getSchoolYear().getId());
            ps.setLong(2, schedule.getInstructor().getId());
            ps.setLong(3, schedule.getSubject().getId());
            ps.setLong(4, schedule.getSection().getId());
            ps.setLong(5, schedule.getDay().getId());
            ps.setLong(6, schedule.getTime().getId());
            ps.setLong(7, schedule.getRoom().getId());
            ps.setLong(8, schedule.getId());
            ps.execute();
            ps.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSchedule(Schedule day) {
        try {
            getDBConn();
            java.sql.PreparedStatement ps = getCon().prepareStatement("UPDATE SCHEDULE SET DELETED =? WHERE SCHEDULE.ID = ? ");
            ps.setBoolean(1, true);
            ps.setLong(2, day.getId());
            ps.execute();
            ps.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ComboBoxList> list;

    @Override
    public ArrayList<Schedule> selectSchedule(Long syId) {
        ArrayList<Schedule> list = new ArrayList<>();
        try {
            String query = "SELECT \n"
                    + "    SCHEDULE.ID, \n"
                    + "    SCHEDULE.INSTRUCTOR_ID, \n"
                    + "    SCHEDULE.SUBJECT_ID, \n"
                    + "    SCHEDULE.SECTION_ID, \n"
                    + "    SCHEDULE.DAY_ID, \n"
                    + "    SCHEDULE.TIME_ID, \n"
                    + "    SCHEDULE.ROOM_ID, \n"
                    + "    SCHEDULE.SCHOOL_YEAR_ID, \n"
                    + "    SCHEDULE.CAMPUS_HEAD_ID, \n"
                    + "    SCHEDULE.SCHOOL_ID, \n"
                    + "    SCHOOL_YEAR.SCHOOL_YEAR, \n"
                    + "    SEMESTER.SEMESTER, \n"
                    + "    INSTRUCTOR.INSTRUCTOR, \n"
                    + "    SECTION.CODE, \n"
                    + "    SUBJECT.SUBJECT, \n"
                    + "    SUBJECT.UNITS, \n"
                    + "    DAY.DAY, \n"
                    + "    TIME.TIME, \n"
                    + "    ROOM.ROOM \n"
                    + "FROM \n"
                    + "    SCHEDULE \n"
                    + "INNER JOIN \n"
                    + "    CAMPUS_HEAD \n"
                    + "ON (SCHEDULE.CAMPUS_HEAD_ID = CAMPUS_HEAD.ID) \n"
                    + "INNER JOIN \n"
                    + "    DAY \n"
                    + "ON (SCHEDULE.DAY_ID = DAY.ID) \n"
                    + "INNER JOIN \n"
                    + "    INSTRUCTOR \n"
                    + "ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) \n"
                    + "INNER JOIN \n"
                    + "    ROOM \n"
                    + "ON (SCHEDULE.ROOM_ID = ROOM.ID) \n"
                    + "INNER JOIN \n"
                    + "    SCHOOL \n"
                    + "ON (SCHEDULE.SCHOOL_ID = SCHOOL.ID) \n"
                    + "INNER JOIN \n"
                    + "    SCHOOL_YEAR \n"
                    + "ON (SCHEDULE.SCHOOL_YEAR_ID = SCHOOL_YEAR.ID) \n"
                    + "INNER JOIN \n"
                    + "    SECTION \n"
                    + "ON (SCHEDULE.SECTION_ID = SECTION.ID) \n"
                    + "INNER JOIN \n"
                    + "    SUBJECT \n"
                    + "ON (SCHEDULE.SUBJECT_ID = SUBJECT.ID) \n"
                    + "INNER JOIN \n"
                    + "    TIME \n"
                    + "ON (SCHEDULE.TIME_ID = TIME.ID) \n"
                    + "INNER JOIN \n"
                    + "    SEMESTER \n"
                    + "ON (SCHOOL_YEAR.SEMESTER_ID = SEMESTER.ID) WHERE SCHEDULE.DELETED =FALSE AND  SCHEDULE.SCHOOL_YEAR_ID ="+syId;
            getDBConn();
            Statement st = getCon().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Schedule schedule = new Schedule();
                Instructor instructor = new Instructor();
                Subject subject = new Subject();
                Section section = new Section();
                Day day = new Day();
                Time time = new Time();
                Room room = new Room();
                SchoolYear schoolYear = new SchoolYear();
                CampusHead campusHead = new CampusHead();
                School school = new School();
                Semester semester = new Semester();
                schedule.setId(rs.getLong("ID"));
                instructor.setId(rs.getLong("INSTRUCTOR_ID"));
                instructor.setInstructor(rs.getString("INSTRUCTOR"));
                subject.setId(rs.getLong("SUBJECT_ID"));
                subject.setSubject(rs.getString("SUBJECT"));
                subject.setUnit(rs.getInt("UNITS"));
                section.setId(rs.getLong("SECTION_ID"));
                section.setCode(rs.getString("CODE"));
                day.setId(rs.getLong("DAY_ID"));
                day.setDay(rs.getString("DAY"));
                time.setId(rs.getLong("TIME_ID"));
                time.setTime(rs.getString("TIME"));
                room.setId(rs.getLong("ROOM_ID"));
                room.setRoom(rs.getString("ROOM"));
                schoolYear.setId(rs.getLong("SCHOOL_YEAR_ID"));

//              ID,INSTRUCTOR_ID,SUBJECT_ID,SECTION_ID,DAY_ID,TIME_ID,ROOM_ID,
                schedule.setInstructor(instructor);
                schedule.setSubject(subject);
                schedule.setSection(section);
                schedule.setDay(day);
                schedule.setTime(time);
                schedule.setRoom(room);
                schedule.setSchoolYear(schoolYear);
                list.add(schedule);
            }
            rs.close();
            st.close();
            closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public void comboBoxSchedule() {
        this.setList(new ArrayList<ComboBoxList>());
        try {
            getDBConn();
            PreparedStatement statement;
            ResultSet rs;
            statement = getCon().prepareStatement("SELECT * FROM DAY WHERE DAY.DELETED =FALSE  ORDER BY DAY ASC ");
            rs = statement.executeQuery();
            while (rs.next()) {
                Long idl = rs.getLong("ID");
                String namel = rs.getString("DAY");
                this.getList().add(new ComboBoxList(idl, namel));
            }
            rs.close();
            statement.close();
            closeConnection();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Long getMaxId() {
        Long maxId = null;
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("SELECT \n"
                    + "    MAX(SCHEDULE.ID) AS ID  \n"
                    + "FROM \n"
                    + "    SCHEDULE ");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Long idl = rs.getLong("ID");
                if (idl == 0) {
                    maxId = 1L;
                } else {
                    maxId = idl + 1;
                }
            }
            rs.close();
            statement.close();
//            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxId;
    }

    @Override
    public Boolean isConflictSchedule(Long dayId, Long roomId, String time,Long syId) {
        Boolean maxId = false;
        TimeValidator tv = new TimeValidator(time);
        String start = tv.getStartTime() + ":00";
        String end = tv.getEndTime() + ":00";
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                    SELECT 
                                                                        SCHEDULE.ID 
                                                                    FROM 
                                                                        SCHEDULE 
                                                                    INNER JOIN 
                                                                        TIME 
                                                                    ON (SCHEDULE.TIME_ID = TIME.ID) 
                                                                    INNER JOIN 
                                                                        ROOM 
                                                                    ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                    INNER JOIN 
                                                                        DAY 
                                                                    ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                    INNER JOIN 
                                                                        INSTRUCTOR 
                                                                    ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) WHERE   SCHEDULE.DAY_ID =? AND  SCHEDULE.ROOM_ID =? AND  TIME.START_TIME < ? AND TIME.END_TIME > ?  AND SCHEDULE.SCHOOL_YEAR_ID =?
                                                                    """);
            statement.setLong(1, dayId);
            statement.setLong(2, roomId);
            statement.setString(3, end);
            statement.setString(4, start);
            statement.setLong(5, syId);
            
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {

                Long idl = rs.getLong("ID");
                if (idl > 0) {
                    maxId = true;
                }
                System.out.println("id" + maxId);
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxId;
    }

    @Override
    public Integer instructorsSubject(Long instructorId, Long syId) {
        Integer maxId = 0;
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                    SELECT  COUNT(SCHEDULE.ID) AS ID
                                                                          FROM 
                                                                          SCHEDULE 
                                                                          INNER JOIN 
                                                                          TIME 
                                                                          ON (SCHEDULE.TIME_ID = TIME.ID) 
                                                                          INNER JOIN 
                                                                             ROOM 
                                                                             ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                          INNER JOIN 
                                                                              DAY 
                                                                               ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                          INNER JOIN 
                                                                              INSTRUCTOR 
                                                                              ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) WHERE   SCHEDULE.INSTRUCTOR_ID =? AND SCHEDULE.SCHOOL_YEAR_ID=? AND SCHEDULE.DELETED =FALSE
                                                                    """);
            statement.setLong(1, instructorId);
            statement.setLong(2, syId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Integer idl = rs.getInt("ID");
                if (idl > 0) {
                    maxId = idl;
                }
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxId;
    }

    @Override
    public Integer subjectsUse(Long subjectId,Long syId) {
        Integer maxId = 0;
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                    SELECT  COUNT(SCHEDULE.ID) AS ID
                                                                        FROM 
                                                                        SCHEDULE 
                                                                        INNER JOIN 
                                                                        TIME 
                                                                        ON (SCHEDULE.TIME_ID = TIME.ID) 
                                                                        INNER JOIN 
                                                                           ROOM 
                                                                           ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                        INNER JOIN 
                                                                            DAY 
                                                                             ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                        INNER JOIN 
                                                                            INSTRUCTOR 
                                                                            ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) WHERE   SCHEDULE.SUBJECT_ID =? AND   SCHEDULE.SCHOOL_YEAR_ID =?  AND SCHEDULE.DELETED =FALSE                                                  
                                                                    """);
            statement.setLong(1, subjectId);
            statement.setLong(2, syId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Integer idl = rs.getInt("ID");
                if (idl > 0) {
                    maxId = idl;
                }
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxId;
    }

    @Override
    public Integer sectionUse(Long sectionId,Long syId) {
        Integer maxId = 0;
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                    SELECT  COUNT(SCHEDULE.ID) AS ID
                                                                        FROM 
                                                                        SCHEDULE 
                                                                        INNER JOIN 
                                                                        TIME 
                                                                        ON (SCHEDULE.TIME_ID = TIME.ID) 
                                                                        INNER JOIN 
                                                                           ROOM 
                                                                           ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                        INNER JOIN 
                                                                            DAY 
                                                                             ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                        INNER JOIN 
                                                                            INSTRUCTOR 
                                                                            ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) WHERE   SCHEDULE.SECTION_ID =? AND   SCHEDULE.SCHOOL_YEAR_ID =?     AND SCHEDULE.DELETED =FALSE                                                  
                                                                    """);
            statement.setLong(1, sectionId);
            statement.setLong(2, syId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Integer idl = rs.getInt("ID");
                if (idl > 0) {
                    maxId = idl;
                }
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxId;
    }

    @Override
    public ArrayList<Schedule> selectScheduleBySubjectId(Long subjectId) {
        ArrayList<Schedule> list = new ArrayList<>();
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                   SELECT 
                                                                       SCHEDULE.ID, 
                                                                       SUBJECT.SUBJECT, 
                                                                       SECTION.CODE, 
                                                                       INSTRUCTOR.INSTRUCTOR, 
                                                                       DAY.DAY, 
                                                                       TIME.TIME, 
                                                                       ROOM.ROOM 
                                                                   FROM 
                                                                       SCHEDULE 
                                                                   INNER JOIN 
                                                                       DAY 
                                                                   ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                   INNER JOIN 
                                                                       INSTRUCTOR 
                                                                   ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) 
                                                                   INNER JOIN 
                                                                       ROOM 
                                                                   ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                   INNER JOIN 
                                                                       SECTION 
                                                                   ON (SCHEDULE.SECTION_ID = SECTION.ID) 
                                                                   INNER JOIN 
                                                                       SUBJECT 
                                                                   ON (SCHEDULE.SUBJECT_ID = SUBJECT.ID) 
                                                                   INNER JOIN 
                                                                       TIME 
                                                                   ON (SCHEDULE.TIME_ID = TIME.ID) WHERE SCHEDULE.SUBJECT_ID =?
                                                                    """);
            statement.setLong(1, subjectId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                Subject subject = new Subject();
                Section section = new Section();
                Instructor instructor = new Instructor();
                Day day = new Day();
                Time time = new Time();
                Room room = new Room();
                schedule.setId(rs.getLong("ID"));
                subject.setSubject(rs.getString("SUBJECT"));
                section.setCode(rs.getString("CODE"));
                instructor.setInstructor(rs.getString("INSTRUCTOR"));
                day.setDay(rs.getString("DAY"));
                time.setTime(rs.getString("TIME"));
                room.setRoom(rs.getString("ROOM"));
                schedule.setSubject(subject);
                schedule.setSection(section);
                schedule.setInstructor(instructor);
                schedule.setDay(day);
                schedule.setTime(time);
                schedule.setRoom(room);
                list.add(schedule);
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<Schedule> selectScheduleByInstructorId(Long instructorId) {
        ArrayList<Schedule> list = new ArrayList<>();
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                   SELECT 
                                                                       SCHEDULE.ID, 
                                                                       SUBJECT.SUBJECT, 
                                                                       SECTION.CODE, 
                                                                       INSTRUCTOR.INSTRUCTOR, 
                                                                       DAY.DAY, 
                                                                       TIME.TIME, 
                                                                       ROOM.ROOM 
                                                                   FROM 
                                                                       SCHEDULE 
                                                                   INNER JOIN 
                                                                       DAY 
                                                                   ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                   INNER JOIN 
                                                                       INSTRUCTOR 
                                                                   ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) 
                                                                   INNER JOIN 
                                                                       ROOM 
                                                                   ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                   INNER JOIN 
                                                                       SECTION 
                                                                   ON (SCHEDULE.SECTION_ID = SECTION.ID) 
                                                                   INNER JOIN 
                                                                       SUBJECT 
                                                                   ON (SCHEDULE.SUBJECT_ID = SUBJECT.ID) 
                                                                   INNER JOIN 
                                                                       TIME 
                                                                   ON (SCHEDULE.TIME_ID = TIME.ID) WHERE SCHEDULE.INSTRUCTOR_ID =?
                                                                    """);
            statement.setLong(1, instructorId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                Subject subject = new Subject();
                Section section = new Section();
                Instructor instructor = new Instructor();
                Day day = new Day();
                Time time = new Time();
                Room room = new Room();
                schedule.setId(rs.getLong("ID"));
                subject.setSubject(rs.getString("SUBJECT"));
                section.setCode(rs.getString("CODE"));
                instructor.setInstructor(rs.getString("INSTRUCTOR"));
                day.setDay(rs.getString("DAY"));
                time.setTime(rs.getString("TIME"));
                room.setRoom(rs.getString("ROOM"));
                schedule.setSubject(subject);
                schedule.setSection(section);
                schedule.setInstructor(instructor);
                schedule.setDay(day);
                schedule.setTime(time);
                schedule.setRoom(room);
                list.add(schedule);
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<Schedule> selectScheduleBySectionId(Long sectionId) {
        ArrayList<Schedule> list = new ArrayList<>();
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                   SELECT 
                                                                       SCHEDULE.ID, 
                                                                       SUBJECT.SUBJECT, 
                                                                       SECTION.CODE, 
                                                                       INSTRUCTOR.INSTRUCTOR, 
                                                                       DAY.DAY, 
                                                                       TIME.TIME, 
                                                                       ROOM.ROOM 
                                                                   FROM 
                                                                       SCHEDULE 
                                                                   INNER JOIN 
                                                                       DAY 
                                                                   ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                   INNER JOIN 
                                                                       INSTRUCTOR 
                                                                   ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) 
                                                                   INNER JOIN 
                                                                       ROOM 
                                                                   ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                   INNER JOIN 
                                                                       SECTION 
                                                                   ON (SCHEDULE.SECTION_ID = SECTION.ID) 
                                                                   INNER JOIN 
                                                                       SUBJECT 
                                                                   ON (SCHEDULE.SUBJECT_ID = SUBJECT.ID) 
                                                                   INNER JOIN 
                                                                       TIME 
                                                                   ON (SCHEDULE.TIME_ID = TIME.ID) WHERE SCHEDULE.SECTION_ID =?
                                                                    """);
            statement.setLong(1, sectionId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                Subject subject = new Subject();
                Section section = new Section();
                Instructor instructor = new Instructor();
                Day day = new Day();
                Time time = new Time();
                Room room = new Room();
                schedule.setId(rs.getLong("ID"));
                subject.setSubject(rs.getString("SUBJECT"));
                section.setCode(rs.getString("CODE"));
                instructor.setInstructor(rs.getString("INSTRUCTOR"));
                day.setDay(rs.getString("DAY"));
                time.setTime(rs.getString("TIME"));
                room.setRoom(rs.getString("ROOM"));
                schedule.setSubject(subject);
                schedule.setSection(section);
                schedule.setInstructor(instructor);
                schedule.setDay(day);
                schedule.setTime(time);
                schedule.setRoom(room);
                list.add(schedule);
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<Schedule> selectScheduleByDayId(Long dayId) {
        ArrayList<Schedule> list = new ArrayList<>();
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                   SELECT 
                                                                       SCHEDULE.ID, 
                                                                       SUBJECT.SUBJECT, 
                                                                       SECTION.CODE, 
                                                                       INSTRUCTOR.INSTRUCTOR, 
                                                                       DAY.DAY, 
                                                                       TIME.TIME, 
                                                                       ROOM.ROOM 
                                                                   FROM 
                                                                       SCHEDULE 
                                                                   INNER JOIN 
                                                                       DAY 
                                                                   ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                   INNER JOIN 
                                                                       INSTRUCTOR 
                                                                   ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) 
                                                                   INNER JOIN 
                                                                       ROOM 
                                                                   ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                   INNER JOIN 
                                                                       SECTION 
                                                                   ON (SCHEDULE.SECTION_ID = SECTION.ID) 
                                                                   INNER JOIN 
                                                                       SUBJECT 
                                                                   ON (SCHEDULE.SUBJECT_ID = SUBJECT.ID) 
                                                                   INNER JOIN 
                                                                       TIME 
                                                                   ON (SCHEDULE.TIME_ID = TIME.ID) WHERE SCHEDULE.DAY_ID =?  ORDER BY SECTION.CODE  ASC
                                                                    """);
            statement.setLong(1, dayId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                Subject subject = new Subject();
                Section section = new Section();
                Instructor instructor = new Instructor();
                Day day = new Day();
                Time time = new Time();
                Room room = new Room();
                schedule.setId(rs.getLong("ID"));
                subject.setSubject(rs.getString("SUBJECT"));
                section.setCode(rs.getString("CODE"));
                instructor.setInstructor(rs.getString("INSTRUCTOR"));
                day.setDay(rs.getString("DAY"));
                time.setTime(rs.getString("TIME"));
                room.setRoom(rs.getString("ROOM"));
                schedule.setSubject(subject);
                schedule.setSection(section);
                schedule.setInstructor(instructor);
                schedule.setDay(day);
                schedule.setTime(time);
                schedule.setRoom(room);
                list.add(schedule);
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer dayUse(Long dayId) {
        Integer maxId = 0;
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                    SELECT  COUNT(SCHEDULE.ID) AS ID
                                                                        FROM 
                                                                        SCHEDULE 
                                                                        INNER JOIN 
                                                                        TIME 
                                                                        ON (SCHEDULE.TIME_ID = TIME.ID) 
                                                                        INNER JOIN 
                                                                           ROOM 
                                                                           ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                        INNER JOIN 
                                                                            DAY 
                                                                             ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                        INNER JOIN 
                                                                            INSTRUCTOR 
                                                                            ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) WHERE   SCHEDULE.DAY_ID =?                                                      
                                                                    """);
            statement.setLong(1, dayId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Integer idl = rs.getInt("ID");
                if (idl > 0) {
                    maxId = idl;
                }
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxId;
    }

    @Override
    public ArrayList<Schedule> selectScheduleByTimeId(Long timeId) {
        ArrayList<Schedule> list = new ArrayList<>();
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                   SELECT 
                                                                       SCHEDULE.ID, 
                                                                       SUBJECT.SUBJECT, 
                                                                       SECTION.CODE, 
                                                                       INSTRUCTOR.INSTRUCTOR, 
                                                                       DAY.DAY, 
                                                                       TIME.TIME, 
                                                                       ROOM.ROOM 
                                                                   FROM 
                                                                       SCHEDULE 
                                                                   INNER JOIN 
                                                                       DAY 
                                                                   ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                   INNER JOIN 
                                                                       INSTRUCTOR 
                                                                   ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) 
                                                                   INNER JOIN 
                                                                       ROOM 
                                                                   ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                   INNER JOIN 
                                                                       SECTION 
                                                                   ON (SCHEDULE.SECTION_ID = SECTION.ID) 
                                                                   INNER JOIN 
                                                                       SUBJECT 
                                                                   ON (SCHEDULE.SUBJECT_ID = SUBJECT.ID) 
                                                                   INNER JOIN 
                                                                       TIME 
                                                                   ON (SCHEDULE.TIME_ID = TIME.ID) WHERE SCHEDULE.TIME_ID =?  ORDER BY DAY.DAY  ASC
                                                                    """);
            statement.setLong(1, timeId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                Subject subject = new Subject();
                Section section = new Section();
                Instructor instructor = new Instructor();
                Day day = new Day();
                Time time = new Time();
                Room room = new Room();
                schedule.setId(rs.getLong("ID"));
                subject.setSubject(rs.getString("SUBJECT"));
                section.setCode(rs.getString("CODE"));
                instructor.setInstructor(rs.getString("INSTRUCTOR"));
                day.setDay(rs.getString("DAY"));
                time.setTime(rs.getString("TIME"));
                room.setRoom(rs.getString("ROOM"));
                schedule.setSubject(subject);
                schedule.setSection(section);
                schedule.setInstructor(instructor);
                schedule.setDay(day);
                schedule.setTime(time);
                schedule.setRoom(room);
                list.add(schedule);
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer timeUse(Long timeId) {
        Integer maxId = 0;
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                    SELECT  COUNT(SCHEDULE.ID) AS ID
                                                                        FROM 
                                                                        SCHEDULE 
                                                                        INNER JOIN 
                                                                        TIME 
                                                                        ON (SCHEDULE.TIME_ID = TIME.ID) 
                                                                        INNER JOIN 
                                                                           ROOM 
                                                                           ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                        INNER JOIN 
                                                                            DAY 
                                                                             ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                        INNER JOIN 
                                                                            INSTRUCTOR 
                                                                            ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) WHERE   SCHEDULE.TIME_ID =?                                                      
                                                                    """);
            statement.setLong(1, timeId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Integer idl = rs.getInt("ID");
                if (idl > 0) {
                    maxId = idl;
                }
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxId;
    }

    @Override
    public ArrayList<Schedule> selectScheduleByRoomId(Long roomId) {
        ArrayList<Schedule> list = new ArrayList<>();
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                   SELECT 
                                                                       SCHEDULE.ID, 
                                                                       SUBJECT.SUBJECT, 
                                                                       SECTION.CODE, 
                                                                       INSTRUCTOR.INSTRUCTOR, 
                                                                       DAY.DAY, 
                                                                       TIME.TIME, 
                                                                       ROOM.ROOM 
                                                                   FROM 
                                                                       SCHEDULE 
                                                                   INNER JOIN 
                                                                       DAY 
                                                                   ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                   INNER JOIN 
                                                                       INSTRUCTOR 
                                                                   ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) 
                                                                   INNER JOIN 
                                                                       ROOM 
                                                                   ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                   INNER JOIN 
                                                                       SECTION 
                                                                   ON (SCHEDULE.SECTION_ID = SECTION.ID) 
                                                                   INNER JOIN 
                                                                       SUBJECT 
                                                                   ON (SCHEDULE.SUBJECT_ID = SUBJECT.ID) 
                                                                   INNER JOIN 
                                                                       TIME 
                                                                   ON (SCHEDULE.TIME_ID = TIME.ID) WHERE SCHEDULE.ROOM_ID =?  ORDER BY DAY.DAY,TIME.TIME,INSTRUCTOR.INSTRUCTOR  ASC 
                                                                    """);
            statement.setLong(1, roomId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                Subject subject = new Subject();
                Section section = new Section();
                Instructor instructor = new Instructor();
                Day day = new Day();
                Time time = new Time();
                Room room = new Room();
                schedule.setId(rs.getLong("ID"));
                subject.setSubject(rs.getString("SUBJECT"));
                section.setCode(rs.getString("CODE"));
                instructor.setInstructor(rs.getString("INSTRUCTOR"));
                day.setDay(rs.getString("DAY"));
                time.setTime(rs.getString("TIME"));
                room.setRoom(rs.getString("ROOM"));
                schedule.setSubject(subject);
                schedule.setSection(section);
                schedule.setInstructor(instructor);
                schedule.setDay(day);
                schedule.setTime(time);
                schedule.setRoom(room);
                list.add(schedule);
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer roomUse(Long roomId) {
        Integer maxId = 0;
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                    SELECT  COUNT(SCHEDULE.ID) AS ID
                                                                        FROM 
                                                                        SCHEDULE 
                                                                        INNER JOIN 
                                                                        TIME 
                                                                        ON (SCHEDULE.TIME_ID = TIME.ID) 
                                                                        INNER JOIN 
                                                                           ROOM 
                                                                           ON (SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                        INNER JOIN 
                                                                            DAY 
                                                                             ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                        INNER JOIN 
                                                                            INSTRUCTOR 
                                                                            ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) WHERE   SCHEDULE.ROOM_ID =?                                                      
                                                                    """);
            statement.setLong(1, roomId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Integer idl = rs.getInt("ID");
                if (idl > 0) {
                    maxId = idl;
                }
            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxId;
    }

    @Override
    public Schedule conflictScheduleDetails(Long dayId, Long roomId, String time, Long syId) {
        Schedule schedule = null;
        TimeValidator tv = new TimeValidator(time);
        String start = tv.getStartTime() + ":00";
        String end = tv.getEndTime() + ":00";
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("""
                                                                    SELECT 
                                                                         SCHOOL.SCHOOL_NAME, 
                                                                         SCHOOL.ADDRESS, 
                                                                         SCHOOL.TEL, 
                                                                         SCHOOL.TELEFAX, 
                                                                         SCHOOL.SITE, 
                                                                         SCHOOL.TITLE, 
                                                                         SCHOOL.CAMPUS, 
                                                                         SEMESTER.SEMESTER, 
                                                                         SCHOOL_YEAR.SCHOOL_YEAR, 
                                                                         INSTRUCTOR.INSTRUCTOR, 
                                                                         SUBJECT.UNITS, 
                                                                         SECTION.CODE, 
                                                                         SUBJECT.SUBJECT, 
                                                                         TIME.TIME, 
                                                                         TIME.START_TIME, 
                                                                         TIME.END_TIME, 
                                                                         DAY.DAY, 
                                                                         ROOM.ROOM, 
                                                                         CAMPUS_HEAD.CAMPUS_HEAD, 
                                                                         CAMPUS_HEAD.SATELITE_DIRECTOR, 
                                                                         CAMPUS_HEAD.VPAA 
                                                                     FROM 
                                                                         SCHEDULE 
                                                                     INNER JOIN     CAMPUS_HEAD 
                                                                     ON (SCHEDULE.CAMPUS_HEAD_ID = CAMPUS_HEAD.ID) 
                                                                     INNER JOIN     DAY 
                                                                     ON (SCHEDULE.DAY_ID = DAY.ID) 
                                                                     INNER JOIN    INSTRUCTOR 
                                                                     ON (SCHEDULE.INSTRUCTOR_ID = INSTRUCTOR.ID) 
                                                                     INNER JOIN     ROOM 
                                                                     ON ( SCHEDULE.ROOM_ID = ROOM.ID) 
                                                                     INNER JOIN     SCHOOL 
                                                                     ON ( SCHEDULE.SCHOOL_ID = SCHOOL.ID) 
                                                                     INNER JOIN     SCHOOL_YEAR 
                                                                     ON (SCHEDULE.SCHOOL_YEAR_ID = SCHOOL_YEAR.ID) 
                                                                     INNER JOIN     SECTION 
                                                                     ON (SCHEDULE.SECTION_ID = SECTION.ID) 
                                                                     INNER JOIN    SEMESTER 
                                                                     ON (SCHOOL_YEAR.SEMESTER_ID = SEMESTER.ID) 
                                                                     INNER JOIN     SUBJECT 
                                                                     ON ( SCHEDULE.SUBJECT_ID = SUBJECT.ID) 
                                                                     INNER JOIN     TIME 
                                                                     ON (SCHEDULE.TIME_ID = TIME.ID) WHERE   SCHEDULE.DAY_ID =? AND  SCHEDULE.ROOM_ID =? AND SCHEDULE.SCHOOL_YEAR_ID =? AND  TIME.START_TIME < ? AND TIME.END_TIME > ? 
                                                                    """);
            statement.setLong(1, dayId);
            statement.setLong(2, roomId);
            statement.setLong(3, syId);
            statement.setString(4, end);
            statement.setString(5, start);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Schedule sched = new Schedule();
                Instructor instructor = new Instructor();
                Subject subject = new Subject();
                Section section =new Section();
                Day day = new Day();
                Time timel =new Time();
                Room rooml =new Room();
                
                
                String ins =rs.getString("INSTRUCTOR");
                String sub =rs.getString("SUBJECT");
                String sec =rs.getString("CODE");
                String da =rs.getString("DAY");
                String ti =rs.getString("TIME");
                String ro =rs.getString("ROOM");
                subject.setSubject(sub);
                section.setCode(sec);
                day.setDay(da);
                timel.setTime(ti);
                rooml.setRoom(ro);
                instructor.setInstructor(ins);
                
                sched.setInstructor(instructor);
                sched.setSection(section);
                sched.setSubject(subject);
                sched.setDay(day);
                sched.setTime(timel);
                sched.setRoom(rooml);
                
                schedule=sched;
                

            }
            rs.close();
            statement.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedule;
    }

}
