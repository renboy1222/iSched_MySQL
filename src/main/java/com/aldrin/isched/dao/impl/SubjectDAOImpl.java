/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.isched.dao.impl;

import com.aldrin.isched.dao.SubjectDAO;
import com.aldrin.isched.model.Subject;
import com.aldrin.isched.util.ComboBoxList;
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
public class SubjectDAOImpl extends DBConnection implements SubjectDAO {

    @Override
    public void addSubject(Subject subject) {
        try {
            getDBConn();
            java.sql.PreparedStatement ps = getCon().prepareStatement("INSERT INTO SUBJECT (ID,SUBJECT,UNITS) VALUES  (?,?,?) ");
            ps.setLong(1, getMaxId());
            ps.setString(2, subject.getSubject());
            ps.setInt(3, subject.getUnit());
            ps.execute();
            ps.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSubject(Subject subject) {
        try {
            getDBConn();
            java.sql.PreparedStatement ps = getCon().prepareStatement("UPDATE SUBJECT SET  SUBJECT =?, UNITS=? WHERE SUBJECT.ID = ?");
            ps.setString(1, subject.getSubject());
            ps.setInt(2, subject.getUnit());
            ps.setLong(3, subject.getId());
            ps.execute();
            ps.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSubject(Subject subject) {
        try {
            getDBConn();
            java.sql.PreparedStatement ps = getCon().prepareStatement("UPDATE SUBJECT SET DELETED =? WHERE SUBJECT.ID = ? ");
            ps.setBoolean(1, true);
            ps.setLong(2, subject.getId());
            ps.execute();
            ps.close();
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ComboBoxList> list;

    @Override
    public ArrayList<Subject> selectSubject() {
        ArrayList<Subject> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM SUBJECT WHERE SUBJECT.DELETED =FALSE  ORDER BY SUBJECT ASC ";
            getDBConn();
            Statement st = getCon().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Subject c = new Subject();
                c.setId(rs.getLong("ID"));
                c.setSubject(rs.getString("SUBJECT"));
                c.setUnit(rs.getInt("UNITS"));
                list.add(c);
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
    public void comboBoxSubject() {
        this.setList(new ArrayList<ComboBoxList>());
        try {
            getDBConn();
            PreparedStatement statement;
            ResultSet rs;
            statement = getCon().prepareStatement("SELECT * FROM SUBJECT WHERE SUBJECT.DELETED =FALSE  ORDER BY SUBJECT ASC ");
            rs = statement.executeQuery();
            while (rs.next()) {
                Long idl = rs.getLong("ID");
                String subjectl = rs.getString("SUBJECT");
                this.getList().add(new ComboBoxList(idl, subjectl));
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
                    + "    MAX(SUBJECT.ID) AS ID  \n"
                    + "FROM \n"
                    + "    SUBJECT ");
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
    
    //check if data is duplicate
    @Override
    public Boolean isDuplicate(String name) {
        Boolean maxId = false;
        try {
            getDBConn();
            PreparedStatement statement = getCon().prepareStatement("SELECT   SUBJECT.ID FROM    SUBJECT WHERE SUBJECT ='"+name+"'");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Long idl = rs.getLong("ID");
                if (idl > 0) {
                    maxId = true;
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


}
