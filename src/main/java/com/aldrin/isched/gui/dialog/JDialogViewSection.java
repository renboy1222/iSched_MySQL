/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.aldrin.isched.gui.dialog;

import com.aldrin.isched.dao.impl.ScheduleDAOImpl;
import com.aldrin.isched.gui.JFrameMain;
import com.aldrin.isched.model.Schedule;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Java Programming with Aldrin
 */
public class JDialogViewSection extends javax.swing.JDialog {

    private JFrameMain jFrameMain;
    private Long sectionId;

    public JDialogViewSection(JFrameMain parent, boolean modal, Long sectionId) {
        super(parent, modal);
        this.jFrameMain = parent;
        initComponents();
        setTable();
        this.sectionId = sectionId;
        selectSubject(sectionId);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSubject = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SECTIONS");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTableSubject.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTableSubject);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 28, 549, 317));

        setSize(new java.awt.Dimension(595, 401));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableSubject;
    // End of variables declaration//GEN-END:variables
  //   ID, SUBJECT, CODE,INSTRUCTOR, DAY, TIME, ROOM 
    DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "SECTION", "SUBJECT", "INSTRUCTOR", "DAY", "TIME", "ROOM"}, 0) {
        public Class getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return String.class;
            }
            switch (columnIndex) {
                case 1:
                    return String.class;
                case 2:
                    return String.class;
                case 3:
                    return String.class;
                case 4:
                    return String.class;
                case 5:
                    return String.class;
                case 6:
                    return Integer.class;
                default:
                    return String.class;
            }
        }

        public boolean isCellEditable(int row, int col) {
            if (col < 10) {
                return false;

            } else {
                return true;
            }
        }
    };

    private TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);

    private void setTable() {
        jTableSubject.setCellSelectionEnabled(true);
        jTableSubject = new JTable(tableModel);
        jScrollPane1.setViewportView(jTableSubject);
//        jTableSubject.addMouseListener(this);
        jTableSubject.setRowSorter(sorter);

        TableColumn[] column = new TableColumn[100];
        column[1] = jTableSubject.getColumnModel().getColumn(1);
        column[1].setPreferredWidth(80);

        column[3] = jTableSubject.getColumnModel().getColumn(3);
        column[3].setPreferredWidth(200);
        column[5] = jTableSubject.getColumnModel().getColumn(5);
        column[5].setPreferredWidth(100);

        TableColumn hide0 = jTableSubject.getColumnModel().getColumn(0);
        hide0.setMinWidth(0);
        hide0.setMaxWidth(0);
        hide0.setPreferredWidth(0);

    }

    private ScheduleDAOImpl scheduleDAOImpl = new ScheduleDAOImpl();
    private ArrayList<Schedule> scheduleList;
//   ID, CODE, SUBJECT,INSTRUCTOR, DAY, TIME, ROOM 

    private void selectSubject(Long sectionId) {
        tableModel.setRowCount(0);
        scheduleList = scheduleDAOImpl.selectScheduleBySectionId(sectionId);
        tableModel.setRowCount(0);
        for (Schedule s : scheduleList) {
            tableModel.addRow(new Object[]{s.getId(), s.getSection().getCode(), s.getSubject().getSubject(), s.getInstructor().getInstructor(), s.getDay().getDay(), s.getTime().getTime(), s.getRoom().getRoom()});
        }
    }

}
