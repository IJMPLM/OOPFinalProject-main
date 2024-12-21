package finalproject;
import finalproject.Connection.ConnectPLMDB;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import finalproject.Table.TableActionCellRender;
import javax.swing.text.AbstractDocument;
import finalproject.FieldValidation.CharacterCountFilter;
import finalproject.FieldValidation.IntDigitFilter;
import finalproject.FieldValidation.IntValueFilter;
import finalproject.FieldValidation.MilitaryTimeFilter;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author didi
 */
public class AddSubjectSchedule extends javax.swing.JFrame {
    Connection conn = null; 
    PreparedStatement ps = null;
    ResultSet rs = null; 
    boolean isConnectionOpen = false; // Flag to track connection state
   
    public AddSubjectSchedule() {
        initComponents();
        refresh();
        toggleFilter(true);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        //cmbSchoolYear
        cmbSchoolYear.setBackground(Color.WHITE);
        cmbSchoolYear.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbSchoolYear.setForeground(Color.BLACK);
        UIManager.put("cmbSchoolYear.buttonBackground", Color.WHITE);
        UIManager.put("cmbSchoolYear.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbSchoolYear.buttonHighlight", Color.WHITE);
        UIManager.put("cmbSchoolYear.buttonShadow", Color.WHITE);
        
        //cmbSemester
        cmbSemester.setBackground(Color.WHITE);
        cmbSemester.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbSemester.setForeground(Color.BLACK);
        UIManager.put("cmbSemester.buttonBackground", Color.WHITE);
        UIManager.put("cmbSemester.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbSemester.buttonHighlight", Color.WHITE);
        UIManager.put("cmbSemester.buttonShadow", Color.WHITE);
        
        //cmbCollege
        cmbCollege.setBackground(Color.WHITE);
        cmbCollege.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbCollege.setForeground(Color.BLACK);
        UIManager.put("cmbCollege.buttonBackground", Color.WHITE);
        UIManager.put("cmbCollege.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbCollege.buttonHighlight", Color.WHITE);
        UIManager.put("cmbCollege.buttonShadow", Color.WHITE);
        
        //cmbSubjectCode
        cmbSubjectCode.setBackground(Color.WHITE);
        cmbSubjectCode.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbSubjectCode.setForeground(Color.BLACK);
        UIManager.put("cmbSubjectCode.buttonBackground", Color.WHITE);
        UIManager.put("cmbSubjectCode.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbSubjectCode.buttonHighlight", Color.WHITE);
        UIManager.put("cmbSubjectCode.buttonShadow", Color.WHITE);
        
        //cmbDay
        cmbDay.setBackground(Color.WHITE);
        cmbDay.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbDay.setForeground(Color.BLACK);
        UIManager.put("cmbDay.buttonBackground", Color.WHITE);
        UIManager.put("cmbDay.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbDay.buttonHighlight", Color.WHITE);
        UIManager.put("cmbDay.buttonShadow", Color.WHITE);
        
        //cmbType
        cmbType.setBackground(Color.WHITE);
        cmbType.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbType.setForeground(Color.BLACK);
        UIManager.put("cmbType.buttonBackground", Color.WHITE);
        UIManager.put("cmbType.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbType.buttonHighlight", Color.WHITE);
        UIManager.put("cmbType.buttonShadow", Color.WHITE);
        
        //cmbSequenceNo
        cmbSequenceNo.setBackground(Color.WHITE);
        cmbSequenceNo.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbSequenceNo.setForeground(Color.BLACK);
        UIManager.put("cmbSequenceNo.buttonBackground", Color.WHITE);
        UIManager.put("cmbSequenceNo.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbSequenceNo.buttonHighlight", Color.WHITE);
        UIManager.put("cmbSequenceNo.buttonShadow", Color.WHITE);
        
        //cmbEmployeeID
        cmbEmployeeId.setBackground(Color.WHITE);
        cmbEmployeeId.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbEmployeeId.setForeground(Color.BLACK);
        UIManager.put("cmbEmployeeId.buttonBackground", Color.WHITE);
        UIManager.put("cmbEmployeeId.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbEmployeeId.buttonHighlight", Color.WHITE);
        UIManager.put("cmbEmployeeId.buttonShadow", Color.WHITE);
        
        
        
        
    }
    private void toggleFilter(boolean status){
        applyDocumentFilter(txtBlock, 15, status);
        applyDocumentValueFilter(txtTimeStart1, 24,status);
        applyDocumentValueFilter(txtTimeStart2, 60,status);
        applyDocumentValueFilter(txtTimeEnd1, 24,status);
        applyDocumentValueFilter(txtTimeEnd2, 60,status);
        applyDocumentFilter(txtRoom, 10, status);
   }
    private void applyDocumentFilter(JTextField textField, int characterCount, boolean apply) {
        if (apply) {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new CharacterCountFilter(characterCount));
        } else {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(null);
        }
    }
    private void applyDocumentValueFilter(JTextField textField, int value, boolean apply) {
        if (apply) {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new IntValueFilter(value));
        } else {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(null);
        }
    }
    
    public void refresh() {
        try {
            isConnectionOpen = true; // Set flag to true when starting the refresh process
            conn = ConnectPLMDB.Connect();

            // Populate cmbSchoolYear with school years
            ps = conn.prepareStatement("SELECT syear FROM schoolyear ORDER BY syear DESC");
            rs = ps.executeQuery();
            cmbSchoolYear.removeAllItems();
            System.out.println("Populating cmbSchoolYear:");
            while (rs.next()) {
                String syear = rs.getString("syear");
                System.out.println(" - " + syear);
                cmbSchoolYear.addItem(syear);
            }
            rs.close();
            ps.close();

            // Populate cmbSemester with semesters
            ps = conn.prepareStatement("SELECT semester FROM semester ORDER BY semester ASC");
            rs = ps.executeQuery();
            cmbSemester.removeAllItems();
            System.out.println("Populating cmbSemester:");
            while (rs.next()) {
                String semester = rs.getString("semester");
                System.out.println(" - " + semester);
                cmbSemester.addItem(semester);
            }
            rs.close();
            ps.close();

            // Populate cmbCollege with college codes and descriptions
            ps = conn.prepareStatement("SELECT college_code, description FROM college");
            rs = ps.executeQuery();
            cmbCollege.removeAllItems();
            System.out.println("Populating cmbCollege:");
            while (rs.next()) {
                String collegeCode = rs.getString("college_code");
                String description = rs.getString("description");
                String item = collegeCode + " - " + description;
                System.out.println(" - " + item);
                cmbCollege.addItem(item);
            }
            rs.close();
            ps.close();

            // Populate cmbSubjectCode with subject codes and descriptions
            ps = conn.prepareStatement("SELECT subject_code, description FROM subject ORDER BY subject_code ASC");
            rs = ps.executeQuery();
            cmbSubjectCode.removeAllItems();
            System.out.println("Populating cmbSubjectCode:");
            while (rs.next()) {
                String subjectCode = rs.getString("subject_code");
                String description = rs.getString("description");
                String item = subjectCode + " - " + description;
                System.out.println(" - " + item);
                cmbSubjectCode.addItem(item);
            }
            rs.close();
            ps.close();

            
            // Populate cmbEmployeeId with employee IDs
            ps = conn.prepareStatement("SELECT employee_id FROM employee WHERE status = 'A'");
            rs = ps.executeQuery();
            cmbEmployeeId.removeAllItems();
            System.out.println("Populating cmbEmployeeId:");
            while (rs.next()) {
                String employeeId = rs.getString("employee_id");
                System.out.println(" - " + employeeId);
                cmbEmployeeId.addItem(employeeId);
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
                txtSubjectDescription.setText(rs.getString("description"));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateEmployeeName(String subjectCode) {
        if (isConnectionOpen) {
            return; // Do not proceed if a connection is currently open
        }

        try {
            conn = ConnectPLMDB.Connect();
            ps = conn.prepareStatement("SELECT lastname, firstname FROM employee WHERE employee_id = ?");
            ps.setString(1, subjectCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                txtEmployeeName.setText(rs.getString("lastname")+", "+rs.getString("firstname"));
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

        roundPanel1 = new finalproject.Components.RoundPanel();
        dashboard1 = new finalproject.Menu.Dashboard();
        btnSave = new finalproject.Components.Button();
        cmbEmployeeId = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cmbSequenceNo = new javax.swing.JComboBox<>();
        cmbType = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        cmbDay = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        cmbSubjectCode = new javax.swing.JComboBox<>();
        txtSubjectDescription = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cmbSchoolYear = new javax.swing.JComboBox<>();
        cmbCollege = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        header1 = new finalproject.Menu.Header();
        txtBlock = new javax.swing.JTextField();
        txtTimeStart1 = new javax.swing.JTextField();
        txtRoom = new javax.swing.JTextField();
        txtTimeStart2 = new javax.swing.JTextField();
        txtTimeEnd2 = new javax.swing.JTextField();
        txtTimeEnd1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        btnCancel = new finalproject.Components.Button();
        jLabel1 = new javax.swing.JLabel();
        txtEmployeeName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        roundPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnSave.setBackground(new java.awt.Color(91, 142, 225));
        btnSave.setBorder(null);
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Create");
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

        cmbEmployeeId.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        cmbEmployeeId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEmployeeIdItemStateChanged(evt);
            }
        });
        cmbEmployeeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEmployeeIdActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel19.setText("Employee ID");

        jLabel13.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel13.setText("Room");

        jLabel6.setText("to");

        jLabel18.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel18.setText("Time (hh:mm)");

        jLabel16.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel16.setText("Sequence No.");

        cmbSequenceNo.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        cmbSequenceNo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2"}));
        cmbSequenceNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSequenceNoActionPerformed(evt);
            }
        });

        cmbType.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        cmbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "F2F", "OL"}));

        jLabel15.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel15.setText("Type");

        cmbDay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M", "T", "W", "Th", "F", "S", "Su"}));
        cmbDay.setMinimumSize(new java.awt.Dimension(72, 23));

        jLabel17.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel17.setText("Day");

        jPanel4.setBackground(new java.awt.Color(224, 234, 255));

        jLabel12.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(43, 58, 103));
        jLabel12.setText("Class Schedule");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        cmbSubjectCode.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        cmbSubjectCode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSubjectCodeItemStateChanged(evt);
            }
        });
        cmbSubjectCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbSubjectCodeMouseClicked(evt);
            }
        });
        cmbSubjectCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSubjectCodeActionPerformed(evt);
            }
        });

        txtSubjectDescription.setEditable(false);
        txtSubjectDescription.setBackground(new java.awt.Color(255, 255, 255));
        txtSubjectDescription.setBorder(null);
        txtSubjectDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubjectDescriptionActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel11.setText("Subject Description");

        jLabel10.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel10.setText("Subject Code");

        jPanel3.setBackground(new java.awt.Color(224, 234, 255));

        jLabel9.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(43, 58, 103));
        jLabel9.setText("Subject Details");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        cmbSchoolYear.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N

        cmbCollege.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel5.setText("College");

        jLabel4.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel4.setText("School Year");

        jPanel2.setBackground(new java.awt.Color(224, 234, 255));

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(43, 58, 103));
        jLabel3.setText("Academic Information");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        cmbSemester.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel8.setText("Semester");

        jLabel7.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel7.setText("Block");

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(43, 58, 103));
        jLabel2.setText("Add Subject Schedule");

        jLabel14.setText(":");

        jLabel20.setText(":");

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

        jLabel1.setText("Employee Name");

        txtEmployeeName.setEditable(false);
        txtEmployeeName.setBackground(new java.awt.Color(255, 255, 255));
        txtEmployeeName.setBorder(null);
        txtEmployeeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeeNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(dashboard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(roundPanel1Layout.createSequentialGroup()
                            .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(roundPanel1Layout.createSequentialGroup()
                                    .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(cmbDay, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGap(18, 18, 18)
                                    .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(roundPanel1Layout.createSequentialGroup()
                                            .addGap(1, 1, 1)
                                            .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(roundPanel1Layout.createSequentialGroup()
                                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(82, 82, 82))
                                                .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(cmbSequenceNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(roundPanel1Layout.createSequentialGroup()
                                            .addComponent(txtTimeStart1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(1, 1, 1)
                                            .addComponent(jLabel14)
                                            .addGap(2, 2, 2)
                                            .addComponent(txtTimeStart2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(5, 5, 5)
                                            .addComponent(txtTimeEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(1, 1, 1)
                                            .addComponent(jLabel20)
                                            .addGap(2, 2, 2)
                                            .addComponent(txtTimeEnd2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(92, 92, 92)
                                    .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(roundPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel1))
                                        .addGroup(roundPanel1Layout.createSequentialGroup()
                                            .addComponent(cmbEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(27, 27, 27)
                                            .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(roundPanel1Layout.createSequentialGroup()
                                    .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbSubjectCode, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(72, 72, 72)
                                    .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtSubjectDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel2))
                            .addGap(55, 55, 55))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, roundPanel1Layout.createSequentialGroup()
                            .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbSchoolYear, 0, 135, Short.MAX_VALUE))
                            .addGap(27, 27, 27)
                            .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbCollege, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtBlock, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(30, 30, 30)
                            .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(38, 38, 38)))
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(46, Short.MAX_VALUE))
            .addComponent(header1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel1Layout.createSequentialGroup()
                .addComponent(header1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cmbCollege, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5))
                                        .addGap(12, 12, 12)
                                        .addComponent(cmbSchoolYear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(roundPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(roundPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addGap(12, 12, 12)
                                    .addComponent(txtBlock, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(30, 30, 30)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSubjectDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbSubjectCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(txtRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbDay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel19)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(cmbEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtTimeEnd2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTimeEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(jLabel14))
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(jLabel6))))
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtTimeStart1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTimeStart2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(jLabel20)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbSequenceNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72))
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(dashboard1, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSubjectDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubjectDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubjectDescriptionActionPerformed

    private void cmbSubjectCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubjectCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSubjectCodeActionPerformed

    private void cmbSequenceNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSequenceNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSequenceNoActionPerformed

    private void cmbEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEmployeeIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEmployeeIdActionPerformed

    private void cmbSubjectCodeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSubjectCodeItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                    String selectedItem = (String) cmbSubjectCode.getSelectedItem();
                    if (selectedItem != null) {
                        String subjectCode = selectedItem.split(" - ")[0];
                        updateSubjectDescription(subjectCode);
                    }
                }
    }//GEN-LAST:event_cmbSubjectCodeItemStateChanged

    private void cmbSubjectCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbSubjectCodeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSubjectCodeMouseClicked

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        // TODO add your handling code here:
        String syear = (String) cmbSchoolYear.getSelectedItem();
        String semester = (String) cmbSemester.getSelectedItem();
        String collegeCode = ((String) cmbCollege.getSelectedItem()).split(" - ")[0];
        String blockNo = txtBlock.getText();
        String subjectCode = ((String) cmbSubjectCode.getSelectedItem()).split(" - ")[0];
        String day = (String) cmbDay.getSelectedItem();
        String startTime = txtTimeStart1.getText()+":"+txtTimeStart2.getText();
        String endTime = txtTimeEnd1.getText()+":"+txtTimeEnd2.getText();
        String time = startTime + " - " + endTime;
        String room = txtRoom.getText();
        String type = (String) cmbType.getSelectedItem();
        String sequenceNo = (String) cmbSequenceNo.getSelectedItem();
        String employeeId = (String) cmbEmployeeId.getSelectedItem();

        if (syear.isEmpty() || semester.isEmpty() || collegeCode.isEmpty() || blockNo.isEmpty() || subjectCode.isEmpty() || day.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || room.isEmpty() || type.isEmpty() || sequenceNo.isEmpty() || employeeId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return;
        }

        try {
            conn = ConnectPLMDB.Connect();
            ps = conn.prepareStatement("INSERT INTO SUBJECT_SCHEDULE (syear, semester, college_code, block_no, subject_code, day, time, room, type, sequence_no, employee_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, syear);
            ps.setString(2, semester);
            ps.setString(3, collegeCode);
            ps.setString(4, blockNo);
            ps.setString(5, subjectCode);
            ps.setString(6, day);
            ps.setString(7, time);
            ps.setString(8, room);
            ps.setString(9, type);
            ps.setString(10, sequenceNo);
            ps.setString(11, employeeId);
            ps.executeUpdate();
            conn.close();
            JOptionPane.showMessageDialog(null, "Record added successfully!");
            refresh();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding record: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnSaveMouseClicked

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

    private void cmbEmployeeIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEmployeeIdItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                    String selectedItem = (String) cmbEmployeeId.getSelectedItem();
                    if (selectedItem != null) {
                        String employeeName = selectedItem;
                        updateEmployeeName(employeeName);
                    }
                }
    }//GEN-LAST:event_cmbEmployeeIdItemStateChanged

    private void txtEmployeeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeeNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmployeeNameActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddSubjectSchedule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddSubjectSchedule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddSubjectSchedule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddSubjectSchedule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddSubjectSchedule().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private finalproject.Components.Button btnCancel;
    private finalproject.Components.Button btnSave;
    private javax.swing.JComboBox<String> cmbCollege;
    private javax.swing.JComboBox<String> cmbDay;
    private javax.swing.JComboBox<String> cmbEmployeeId;
    private javax.swing.JComboBox<String> cmbSchoolYear;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JComboBox<String> cmbSequenceNo;
    private javax.swing.JComboBox<String> cmbSubjectCode;
    private javax.swing.JComboBox<String> cmbType;
    private finalproject.Menu.Dashboard dashboard1;
    private finalproject.Menu.Header header1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private finalproject.Components.RoundPanel roundPanel1;
    private javax.swing.JTextField txtBlock;
    private javax.swing.JTextField txtEmployeeName;
    private javax.swing.JTextField txtRoom;
    private javax.swing.JTextField txtSubjectDescription;
    private javax.swing.JTextField txtTimeEnd1;
    private javax.swing.JTextField txtTimeEnd2;
    private javax.swing.JTextField txtTimeStart1;
    private javax.swing.JTextField txtTimeStart2;
    // End of variables declaration//GEN-END:variables
}
