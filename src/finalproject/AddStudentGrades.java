package finalproject;
import finalproject.Connection.ConnectPLMDB;
import finalproject.FieldValidation.IntDigitFilter;
import finalproject.FieldValidation.DigitDecimalFilter;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import finalproject.Table.TableActionCellRender;
import java.text.*;
import java.awt.print.*;
import javax.swing.JTable;
import javax.swing.text.AbstractDocument;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class AddStudentGrades extends javax.swing.JFrame {
    Connection conn = null; 
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean isConnectionOpen = false; 

    public AddStudentGrades() {
        initComponents();
        refresh();
        addGradeChangeListener();
        toggleFilter(true);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        //cmbBlock
        cmbBlock.setBackground(Color.WHITE);
        cmbBlock.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbBlock.setForeground(Color.BLACK);
        UIManager.put("cmbBlock.buttonBackground", Color.WHITE);
        UIManager.put("cmbBlock.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbBlock.buttonHighlight", Color.WHITE);
        UIManager.put("cmbBlock.buttonShadow", Color.WHITE);
        
        //cmbSemester
        cmbSemester.setBackground(Color.WHITE);
        cmbSemester.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbSemester.setForeground(Color.BLACK);
        UIManager.put("cmbSemester.buttonBackground", Color.WHITE);
        UIManager.put("cmbSemester.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbSemester.buttonHighlight", Color.WHITE);
        UIManager.put("cmbSemester.buttonShadow", Color.WHITE);
        
        //cmbSchoolYear
        cmbSchoolYear.setBackground(Color.WHITE);
        cmbSchoolYear.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbSchoolYear.setForeground(Color.BLACK);
        UIManager.put("cmbSchoolYear.buttonBackground", Color.WHITE);
        UIManager.put("cmbSchoolYear.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbSchoolYear.buttonHighlight", Color.WHITE);
        UIManager.put("cmbSchoolYear.buttonShadow", Color.WHITE);
    }
    public void refresh() {
        try {
            conn = ConnectPLMDB.Connect();
            isConnectionOpen = true;
            // Populate cmbBlock with block numbers
            ps = conn.prepareStatement("SELECT DISTINCT block_no FROM subject_schedule ORDER BY block_no ASC");
            rs = ps.executeQuery();
            cmbBlock.removeAllItems();
            while (rs.next()) {
                cmbBlock.addItem(rs.getString("block_no"));
            }
            rs.close();
            ps.close();

            // Populate jComboBox1 with subject codes and descriptions
            ps = conn.prepareStatement("SELECT subject_code, description FROM subject ORDER BY subject_code ASC");
            rs = ps.executeQuery();
            cmbSubject.removeAllItems();
            while (rs.next()) {
                String subjectCode = rs.getString("subject_code");
                String description = rs.getString("description");
                cmbSubject.addItem(subjectCode + " - " + description);
            }
            rs.close();
            ps.close();

            // Populate cmbSchoolYear with school years
            ps = conn.prepareStatement("SELECT syear FROM schoolyear ORDER BY syear DESC");
            rs = ps.executeQuery();
            cmbSchoolYear.removeAllItems();
            while (rs.next()) {
                cmbSchoolYear.addItem(rs.getString("syear"));
            }
            rs.close();
            ps.close();

            // Populate cmbSemester with semesters
            ps = conn.prepareStatement("SELECT semester FROM semester ORDER BY semester ASC");
            rs = ps.executeQuery();
            cmbSemester.removeAllItems();
            while (rs.next()) {
                cmbSemester.addItem(rs.getString("semester"));
            }
            rs.close();
            ps.close();           

            conn.close();
            isConnectionOpen = false;
        } catch (Exception e) {
            e.printStackTrace();
            isConnectionOpen = false;
        }
    }
    private void toggleFilter(boolean status){
        applyDocumentDecimalFilter(txtGrade, status);
    }
    private void applyDocumentDigitFilter(JTextField textField, int digitCount, boolean apply) {
        if (apply) {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new IntDigitFilter(digitCount));
        } else {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(null);
        }
    }
    private void applyDocumentDecimalFilter(JTextField textField, boolean apply) {
        if (apply) {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DigitDecimalFilter());
        } else {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(null);
        }
    }
    private void addGradeChangeListener() {
        txtGrade.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateEquivalentAndRemarks();
            }
            public void removeUpdate(DocumentEvent e) {
                updateEquivalentAndRemarks();
            }
            public void changedUpdate(DocumentEvent e) {
                updateEquivalentAndRemarks();
            }
        });
    }
     private void updateEquivalentAndRemarks() {
        try {
            double grade = Double.parseDouble(txtGrade.getText());
            if (grade >= 98 && grade <= 100) {
                txtEquivalent.setText("1.00");
            } else if (grade >= 95 && grade < 98) {
                txtEquivalent.setText("1.25");
            } else if (grade >= 92 && grade < 95) {
                txtEquivalent.setText("1.50");
            } else if (grade >= 89 && grade < 92) {
                txtEquivalent.setText("1.75");
            } else if (grade >= 86 && grade < 89) {
                txtEquivalent.setText("2.00");
            } else if (grade >= 83 && grade < 86) {
                txtEquivalent.setText("2.25");
            } else if (grade >= 80 && grade < 83) {
                txtEquivalent.setText("2.50");
            } else if (grade >= 77 && grade < 80) {
                txtEquivalent.setText("2.75");
            } else if (grade >= 75 && grade < 77) {
                txtEquivalent.setText("3.00");
            } else if (grade < 75) {
                txtEquivalent.setText("5.00");
            } else {
                txtEquivalent.setText("");
            }

            // Update remarks based on the grade
            if (grade < 75) {
                txtRemark.setText("Failed");
            } else {
                txtRemark.setText("Passed");
            }
        } catch (NumberFormatException e) {
            txtEquivalent.setText("");
            txtRemark.setText("");
        }
    }
    private void updateSubjectDescription(String subjectCode) {
        if (isConnectionOpen) {
            return; // Do not proceed if a connection is currently open
        }

        try {
            conn = ConnectPLMDB.Connect();
            ps = conn.prepareStatement("SELECT description FROM subject WHERE subject_code = ?");
            ps.setString(1, subjectCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                txtDescription.setText(rs.getString("description"));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        com.raven.swing.RoundPanel roundPanel1 = new com.raven.swing.RoundPanel();
        header2 = new finalproject.Menu.Header();
        dashboard1 = new finalproject.Menu.Dashboard();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtGrade = new javax.swing.JTextField();
        btnCancel = new finalproject.Components.Button();
        btnSave = new finalproject.Components.Button();
        cmbSubject = new javax.swing.JComboBox<>();
        cmbSchoolYear = new javax.swing.JComboBox<>();
        cmbSemester = new javax.swing.JComboBox<>();
        cmbBlock = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtEquivalent = new javax.swing.JTextField();
        txtRemark = new javax.swing.JTextField();
        txtDescription = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        cmbSubjectCode = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        roundPanel1.setBackground(new java.awt.Color(255, 255, 255));
        roundPanel1.setPreferredSize(new java.awt.Dimension(1220, 612));

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(43, 58, 103));
        jLabel1.setText("Add Student Grade");

        jLabel5.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel5.setText("Subject Code");

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel3.setText("School Year");

        jLabel4.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel4.setText("Semester");

        jLabel6.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel6.setText("Block");

        jPanel2.setBackground(new java.awt.Color(224, 234, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1291, 50));

        jLabel13.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(43, 58, 103));
        jLabel13.setText("Student Information");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(668, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel10.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel10.setText("Equivalent");

        txtGrade.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        txtGrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGradeActionPerformed(evt);
            }
        });

        btnCancel.setBorder(null);
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.setBorderColor(new java.awt.Color(91, 142, 225));
        btnCancel.setBorderPainted(false);
        btnCancel.setColor(new java.awt.Color(91, 142, 225));
        btnCancel.setColorClick(new java.awt.Color(73, 114, 180));
        btnCancel.setColorOver(new java.awt.Color(82, 128, 203));
        btnCancel.setFocusPainted(false);
        btnCancel.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 12)); // NOI18N
        btnCancel.setRadius(20);
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(91, 142, 225));
        btnSave.setBorder(null);
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Submit");
        btnSave.setBorderColor(new java.awt.Color(91, 142, 225));
        btnSave.setBorderPainted(false);
        btnSave.setColor(new java.awt.Color(91, 142, 225));
        btnSave.setColorClick(new java.awt.Color(73, 114, 180));
        btnSave.setColorOver(new java.awt.Color(82, 128, 203));
        btnSave.setFocusPainted(false);
        btnSave.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 12)); // NOI18N
        btnSave.setRadius(20);
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveMouseClicked(evt);
            }
        });

        cmbSubject.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSubject.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSubjectItemStateChanged(evt);
            }
        });
        cmbSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSubjectActionPerformed(evt);
            }
        });

        cmbSchoolYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbBlock.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbBlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBlockActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(224, 234, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(1291, 50));

        jLabel14.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(43, 58, 103));
        jLabel14.setText("Academic Information");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        jLabel16.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel16.setText("Description");

        jPanel9.setBackground(new java.awt.Color(224, 234, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(1291, 50));

        jLabel19.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(43, 58, 103));
        jLabel19.setText("Performance");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(665, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        jLabel17.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel17.setText("Subject Code");

        jLabel18.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel18.setText("Grade");

        jLabel20.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel20.setText("Remark");

        txtEquivalent.setEditable(false);
        txtEquivalent.setBackground(new java.awt.Color(255, 255, 255));
        txtEquivalent.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        txtEquivalent.setBorder(null);

        txtRemark.setEditable(false);
        txtRemark.setBackground(new java.awt.Color(255, 255, 255));
        txtRemark.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        txtRemark.setBorder(null);

        txtDescription.setEditable(false);
        txtDescription.setBackground(new java.awt.Color(255, 255, 255));
        txtDescription.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        txtDescription.setBorder(null);
        txtDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescriptionActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel21.setText("%");

        cmbSubjectCode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(dashboard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(cmbSubjectCode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbBlock, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbSchoolYear, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 973, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(roundPanel1Layout.createSequentialGroup()
                                    .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(roundPanel1Layout.createSequentialGroup()
                                            .addComponent(txtGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel21)))
                                    .addGap(35, 35, 35)
                                    .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtEquivalent, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(27, 27, 27)
                                    .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtRemark, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel5)
                                .addComponent(cmbSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(roundPanel1Layout.createSequentialGroup()
                                    .addGap(252, 252, 252)
                                    .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel16)))
                                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 970, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 970, Short.MAX_VALUE)))))
                .addGap(35, 45, Short.MAX_VALUE))
            .addComponent(header2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addComponent(header2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(dashboard1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(10, 10, 10))
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel1)
                        .addGap(12, 12, 12)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSubjectCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47))
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cmbBlock, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbSchoolYear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(46, 46, 46)))
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEquivalent, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRemark, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(97, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
        );

        roundPanel1.getAccessibleContext().setAccessibleName("");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        StudentGrades studGrade = new StudentGrades();
        studGrade.setVisible(true);

        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed

    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescriptionActionPerformed

    private void cmbSubjectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSubjectItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                    String selectedItem = (String) cmbSubject.getSelectedItem();
                    if (selectedItem != null) {
                        String subjectCode = selectedItem.split(" - ")[0];
                        updateSubjectDescription(subjectCode);
                    }
                }
    }//GEN-LAST:event_cmbSubjectItemStateChanged

    private void cmbSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubjectActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cmbSubjectActionPerformed

    private void txtGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGradeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGradeActionPerformed

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        // TODO add your handling code here:
        String syear = cmbSchoolYear.getSelectedItem().toString();
        String semester = cmbSemester.getSelectedItem().toString();
        String studentNo = txtStudentNo.getText();
        String subjectCode = cmbSubject.getSelectedItem().toString().split(" - ")[0];
        String blockNo = cmbBlock.getSelectedItem().toString();
        String grade = txtEquivalent.getText();

        if (syear.isEmpty() || semester.isEmpty() || studentNo.isEmpty() || subjectCode.isEmpty() || blockNo.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return;
        }

        try {
            conn = ConnectPLMDB.Connect();
            ps = conn.prepareStatement("INSERT INTO plm.grades (syear, semester, student_no, subject_code, block_no, grade) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, syear);
            ps.setString(2, semester);
            ps.setString(3, studentNo);
            ps.setString(4, subjectCode);
            ps.setString(5, blockNo);
            ps.setDouble(6, Double.parseDouble(grade));
            ps.executeUpdate();
            conn.close();
            JOptionPane.showMessageDialog(null, "Grade submitted successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error submitting grade: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnSaveMouseClicked

    private void cmbBlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBlockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbBlockActionPerformed


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddStudentGrades().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private finalproject.Components.Button btnCancel;
    private finalproject.Components.Button btnSave;
    private javax.swing.JComboBox<String> cmbBlock;
    private javax.swing.JComboBox<String> cmbSchoolYear;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JComboBox<String> cmbSubject;
    private javax.swing.JComboBox<String> cmbSubjectCode;
    private finalproject.Menu.Dashboard dashboard1;
    private finalproject.Menu.Header header2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtEquivalent;
    private javax.swing.JTextField txtGrade;
    private javax.swing.JTextField txtRemark;
    // End of variables declaration//GEN-END:variables
}
