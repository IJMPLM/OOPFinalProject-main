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
import finalproject.FieldValidation.IntDateFilter;
import javax.swing.table.TableColumnModel;

public class Employee extends javax.swing.JFrame {
    Connection conn = null; 
    PreparedStatement ps = null;
    ResultSet rs = null; 

    public Employee() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        //setExtendedState(JFrame.MAXIMIZED_BOTH);

        //cmbStatus
        cmbStatus.setBackground(Color.WHITE);
        cmbStatus.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbStatus.setForeground(Color.BLACK);
        UIManager.put("cmbStatus.buttonBackground", Color.WHITE);
        UIManager.put("cmbStatus.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbStatus.buttonHighlight", Color.WHITE);
        UIManager.put("cmbStatus.buttonShadow", Color.WHITE);
        
        //cmbGender
        cmbGender.setBackground(Color.WHITE);
        cmbGender.setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14));
        cmbGender.setForeground(Color.BLACK);
        UIManager.put("cmbGender.buttonBackground", Color.WHITE);
        UIManager.put("cmbGender.buttonDarkShadow", Color.WHITE);
        UIManager.put("cmbGender.buttonHighlight", Color.WHITE);
        UIManager.put("cmbGender.buttonShadow", Color.WHITE);
        
        // Set custom renderer for the action column
        tblEmployees.getColumnModel().getColumn(0).setCellRenderer(new TableActionCellRender());
        tblEmployees.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblEmployees.getTableHeader().setFont(new Font("Microsoft Jheng Hei UI", Font.PLAIN, 14)); 
        tblEmployees.getTableHeader().setOpaque(false);
        tblEmployees.getTableHeader().setBackground(new Color(212, 228, 255));
        tblEmployees.getTableHeader().setForeground(new Color(0, 0, 0));
        tblEmployees.setRowHeight(40); 
        toggleFilter(true);
    }
   private void toggleFilter(boolean status){
        applyDocumentFilter(txtEmployeeId, 10, status); 
        applyDocumentFilter(txtLastName, 32, status); 
        applyDocumentFilter(txtFirstName, 32, status);   
        applyDocumentFilter(txtEmail, 32, status); 
        applyDocumentDigitFilter(txtMobileNo, 11, status); 
        applyDocumentFilter(txtAddress, 32, status);   
        applyDocumentDigitFilter(txtBDYear, 4, status); 
        applyDocumentMonthFilter(txtBDMonth, status);
        applyDocumentDayFilter(txtBDDay, txtBDMonth.getText(), status);   
        applyDocumentDigitFilter(txtDSYear, 4, status); 
        applyDocumentMonthFilter(txtDSMonth, status);
        applyDocumentDayFilter(txtDSDay, txtDSMonth.getText(), status);   
        applyDocumentDigitFilter(txtDGYear, 4, status); 
        applyDocumentMonthFilter(txtDGMonth, status);
        applyDocumentDayFilter(txtDGDay, txtDGMonth.getText(), status);
   }
    
   private void applyDocumentFilter(JTextField textField, int characterCount, boolean apply) {
        if (apply) {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new CharacterCountFilter(characterCount));
        } else {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(null);
        }
    }
   private void applyDocumentDigitFilter(JTextField textField, int digitCount, boolean apply) {
        if (apply) {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new IntDigitFilter(digitCount));
        } else {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(null);
        }
    }
   private void applyDocumentMonthFilter(JTextField textField, boolean apply) {
        if (apply) {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new IntDateFilter(null, true));
        } else {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(null);
        }
    }
   private void applyDocumentDayFilter(JTextField textField, String month, boolean apply) {
        if (apply) {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new IntDateFilter(month, false));
        } else {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(null);
        }
    }
    public void refresh() {
        try {
            conn = ConnectPLMDB.Connect();
            ps = conn.prepareStatement("SELECT employee_id, lastname, firstname, email, gender, cp_num, address, bday, status, date_started, date_resigned FROM employee WHERE status = 'A' ORDER BY lastname ASC ");
            rs = ps.executeQuery();
            DefaultTableModel model = new DefaultTableModel(new Object[]{"Employee ID", "Name", "Email", "Gender", "Mobile No", "Address", "Birthdate", "Status", "Date Started", "Date Resigned"}, 0);
            while (rs.next()) {
                String employeeId = rs.getString("employee_id");
                String name = rs.getString("lastname") + ", " + rs.getString("firstname");
                String email = rs.getString("email");
                String gender = rs.getString("gender");
                String mobileNo = rs.getString("cp_num");
                String address = rs.getString("address");
                Date birthdate = rs.getDate("bday");
                String status = rs.getString("status");
                Date dateStarted = rs.getDate("date_started");
                Date dateResigned = rs.getDate("date_resigned");

                model.addRow(new Object[]{employeeId, name, email, gender, mobileNo, address, birthdate, status, dateStarted, dateResigned});
            }
            tblEmployees.setModel(model); 
            toggleFilter(true);
            TableColumnModel columnModel = tblEmployees.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(100); // Employee ID
            columnModel.getColumn(1).setPreferredWidth(200); // Name
            columnModel.getColumn(2).setPreferredWidth(200); // Email
            columnModel.getColumn(3).setPreferredWidth(50);  // Gender
            columnModel.getColumn(4).setPreferredWidth(100); // Mobile No
            columnModel.getColumn(5).setPreferredWidth(100); // Address
            columnModel.getColumn(6).setPreferredWidth(130); // Birthdate
            columnModel.getColumn(7).setPreferredWidth(50);  // Status
            columnModel.getColumn(8).setPreferredWidth(130); // Date Started
            columnModel.getColumn(9).setPreferredWidth(130); // Date Resigned

            conn.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header1 = new finalproject.Menu.Header();
        roundPanel1 = new com.raven.swing.RoundPanel();
        header2 = new finalproject.Menu.Header();
        dashboard1 = new finalproject.Menu.Dashboard();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmployees = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtEmployeeId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtBDYear = new javax.swing.JTextField();
        txtBDMonth = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtBDDay = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        btnAdd = new finalproject.Components.Button();
        btnDelete = new finalproject.Components.Button();
        btnUpdate = new finalproject.Components.Button();
        jLabel1 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cmbGender = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        txtMobileNo = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtDSYear = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtDSMonth = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtDSDay = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtDGYear = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtDGMonth = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtDGDay = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();

        javax.swing.GroupLayout header1Layout = new javax.swing.GroupLayout(header1);
        header1.setLayout(header1Layout);
        header1Layout.setHorizontalGroup(
            header1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        header1Layout.setVerticalGroup(
            header1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        roundPanel1.setBackground(new java.awt.Color(255, 255, 255));
        roundPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        roundPanel1.add(header2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, -1));
        roundPanel1.add(dashboard1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, 650));

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(43, 58, 103));
        jLabel2.setText("Employees");
        roundPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 360, -1, -1));

        tblEmployees.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        tblEmployees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Student No", "Name", "Email", "Gender", "Mobile No", "Address", "Birthdate", "Status", "Date Started", "Date Resigned"}
        ));
        tblEmployees.setFocusable(false);
        tblEmployees.setGridColor(new java.awt.Color(204, 204, 204));
        tblEmployees.setRowHeight(40);
        tblEmployees.setSelectionBackground(new java.awt.Color(224, 234, 255));
        tblEmployees.setShowGrid(true);
        tblEmployees.setShowVerticalLines(false);
        tblEmployees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblEmployees);

        roundPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(278, 395, 975, 310));

        jLabel5.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel5.setText("Employee ID");
        roundPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 115, 111, -1));

        txtEmployeeId.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        txtEmployeeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeeIdActionPerformed(evt);
            }
        });
        roundPanel1.add(txtEmployeeId, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 140, 196, 30));

        jLabel7.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel7.setText("Last Name");
        roundPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(489, 114, 111, -1));

        txtLastName.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        txtLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLastNameActionPerformed(evt);
            }
        });
        roundPanel1.add(txtLastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 140, 220, 30));

        jLabel8.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel8.setText("Birthdate (YYYY-MM-DD)");
        roundPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 190, -1, -1));

        txtBDYear.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        txtBDYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBDYearActionPerformed(evt);
            }
        });
        roundPanel1.add(txtBDYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 210, 58, 30));

        txtBDMonth.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        roundPanel1.add(txtBDMonth, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 210, 34, 30));

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel3.setText("-");
        roundPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 220, -1, -1));

        txtBDDay.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        roundPanel1.add(txtBDDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 210, 34, 30));

        jLabel11.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel11.setText("First Name");
        roundPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 120, 84, 16));

        txtFirstName.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        roundPanel1.add(txtFirstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 140, 235, 30));

        btnAdd.setBackground(new java.awt.Color(91, 142, 225));
        btnAdd.setBorder(null);
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add");
        btnAdd.setBorderColor(new java.awt.Color(91, 142, 225));
        btnAdd.setBorderPainted(false);
        btnAdd.setColor(new java.awt.Color(91, 142, 225));
        btnAdd.setColorClick(new java.awt.Color(73, 114, 180));
        btnAdd.setColorOver(new java.awt.Color(82, 128, 203));
        btnAdd.setFocusPainted(false);
        btnAdd.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        btnAdd.setRadius(20);
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
        });
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        roundPanel1.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 300, 80, 30));

        btnDelete.setBackground(new java.awt.Color(91, 142, 225));
        btnDelete.setBorder(null);
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");
        btnDelete.setBorderColor(new java.awt.Color(91, 142, 225));
        btnDelete.setBorderPainted(false);
        btnDelete.setColor(new java.awt.Color(91, 142, 225));
        btnDelete.setColorClick(new java.awt.Color(73, 114, 180));
        btnDelete.setColorOver(new java.awt.Color(82, 128, 203));
        btnDelete.setFocusPainted(false);
        btnDelete.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        btnDelete.setRadius(20);
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteMouseClicked(evt);
            }
        });
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        roundPanel1.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 300, 80, 30));

        btnUpdate.setBackground(new java.awt.Color(91, 142, 225));
        btnUpdate.setBorder(null);
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("Update");
        btnUpdate.setBorderColor(new java.awt.Color(91, 142, 225));
        btnUpdate.setBorderPainted(false);
        btnUpdate.setColor(new java.awt.Color(91, 142, 225));
        btnUpdate.setColorClick(new java.awt.Color(73, 114, 180));
        btnUpdate.setColorOver(new java.awt.Color(82, 128, 203));
        btnUpdate.setFocusPainted(false);
        btnUpdate.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        btnUpdate.setRadius(20);
        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateMouseClicked(evt);
            }
        });
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        roundPanel1.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 300, 80, 30));

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        jLabel1.setText("E-mail");
        roundPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 120, -1, -1));

        txtEmail.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        roundPanel1.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 140, 260, 30));

        jLabel14.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel14.setText("Gender");
        roundPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, 90, -1));

        cmbGender.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        cmbGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M", "F"}));
        roundPanel1.add(cmbGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, 120, 30));

        jLabel15.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel15.setText("Mobile Number");
        roundPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 190, 130, -1));

        txtMobileNo.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        roundPanel1.add(txtMobileNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 210, 210, 30));

        jLabel16.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel16.setText("Address");
        roundPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 190, -1, -1));

        txtAddress.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        roundPanel1.add(txtAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 210, 165, 30));

        jLabel9.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel9.setText("Date Started (YYYY-MM-DD)");
        roundPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 280, -1, -1));

        txtDSYear.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        txtDSYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDSYearActionPerformed(evt);
            }
        });
        roundPanel1.add(txtDSYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 300, 58, 30));

        jLabel12.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel12.setText("-");
        roundPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 310, -1, -1));

        txtDSMonth.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        roundPanel1.add(txtDSMonth, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 300, 34, 30));

        jLabel13.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel13.setText("-");
        roundPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 310, -1, -1));

        txtDSDay.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        roundPanel1.add(txtDSDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 300, 34, 30));

        jLabel17.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel17.setText("Date Resigned (YYYY-MM-DD)");
        roundPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 280, -1, -1));

        txtDGYear.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        txtDGYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDGYearActionPerformed(evt);
            }
        });
        roundPanel1.add(txtDGYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 300, 58, 30));

        jLabel18.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel18.setText("-");
        roundPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 310, 10, -1));

        txtDGMonth.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        roundPanel1.add(txtDGMonth, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 300, 34, 30));

        jLabel19.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel19.setText("-");
        roundPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 220, -1, -1));

        txtDGDay.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        roundPanel1.add(txtDGDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 300, 34, 30));

        jLabel10.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel10.setText("Status");
        roundPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 280, -1, -1));

        cmbStatus.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A", "I"}));
        roundPanel1.add(cmbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 300, -1, 30));

        jPanel2.setBackground(new java.awt.Color(224, 234, 255));

        jLabel6.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(43, 58, 103));
        jLabel6.setText("Employee Information");
        jLabel6.setPreferredSize(new java.awt.Dimension(157, 50));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(689, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        roundPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(276, 70, 980, -1));

        jLabel21.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 12)); // NOI18N
        jLabel21.setText("-");
        roundPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 310, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_formWindowOpened

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        // TODO add your handling code here:
        String employeeId = txtEmployeeId.getText();
        String lastName = txtLastName.getText();
        String firstName = txtFirstName.getText();
        String email = txtEmail.getText();
        String gender = cmbGender.getSelectedItem().toString();
        String mobileNo = txtMobileNo.getText();
        String address = txtAddress.getText();
        String birthdateStr = txtBDYear.getText() + "-" + txtBDMonth.getText() + "-" + txtBDDay.getText();
        String status = cmbStatus.getSelectedItem().toString();
        String dateStartedStr = txtDSYear.getText() + "-" + txtDSMonth.getText() + "-" + txtDSDay.getText();
        String dateResignedStr = txtDGYear.getText() + "-" + txtDGMonth.getText() + "-" + txtDGDay.getText();

        if (employeeId.isEmpty() || lastName.isEmpty() || firstName.isEmpty() || email.isEmpty() || gender.isEmpty() || mobileNo.isEmpty() || address.isEmpty() || birthdateStr.isEmpty() || status.isEmpty() || dateStartedStr.isEmpty() || dateResignedStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return;
        }

        try {
            // Parse the dates
            java.sql.Date birthdate = java.sql.Date.valueOf(birthdateStr);
            java.sql.Date dateStarted = java.sql.Date.valueOf(dateStartedStr);
            java.sql.Date dateResigned = java.sql.Date.valueOf(dateResignedStr);

            conn = ConnectPLMDB.Connect();
            ps = conn.prepareStatement("SELECT COUNT(*) FROM employee WHERE employee_id = ?");
            ps.setString(1, employeeId);
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                JOptionPane.showMessageDialog(null, "Employee ID not found.");
                conn.close();
                return;
            }

            ps = conn.prepareStatement("UPDATE employee SET lastname = ?, firstname = ?, email = ?, gender = ?, cp_num = ?, address = ?, bday = ?, status = ?, date_started = ?, date_resigned = ? WHERE employee_id = ?");
            ps.setString(1, lastName);
            ps.setString(2, firstName);
            ps.setString(3, email);
            ps.setString(4, gender);
            ps.setString(5, mobileNo);
            ps.setString(6, address);
            ps.setDate(7, birthdate);
            ps.setString(8, status);
            ps.setDate(9, dateStarted);
            ps.setDate(10, dateResigned);
            ps.setString(11, employeeId);
            ps.executeUpdate();
            conn.close();
            JOptionPane.showMessageDialog(null, "Record updated successfully!");
            refresh();
            toggleFilter(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating record: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid date format. Please use YYYY-MM-DD.");
        }
    }//GEN-LAST:event_btnUpdateMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseClicked
        // TODO add your handling code here:
        String employeeId = txtEmployeeId.getText();

        if (employeeId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter the Employee ID to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            conn = ConnectPLMDB.Connect();
            ps = conn.prepareStatement("SELECT COUNT(*) FROM employee WHERE employee_id = ?");
            ps.setString(1, employeeId);
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                JOptionPane.showMessageDialog(null, "Employee ID not found.");
                conn.close();
                return;
            }

            ps = conn.prepareStatement("DELETE FROM employee WHERE employee_id = ?");
            ps.setString(1, employeeId);
            int rowsDeleted = ps.executeUpdate();
            conn.close();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Record deleted successfully!");
                refresh();
                toggleFilter(true);
            } else {
                JOptionPane.showMessageDialog(null, "Employee ID not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting record: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnDeleteMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        String employeeId = txtEmployeeId.getText();
        String lastName = txtLastName.getText();
        String firstName = txtFirstName.getText();
        String email = txtEmail.getText();
        String gender = cmbGender.getSelectedItem().toString();
        String mobileNo = txtMobileNo.getText();
        String address = txtAddress.getText();
        String birthdateStr = txtBDYear.getText() + "-" + txtBDMonth.getText() + "-" + txtBDDay.getText();
        String status = cmbStatus.getSelectedItem().toString();
        String dateStartedStr = txtDSYear.getText() + "-" + txtDSMonth.getText() + "-" + txtDSDay.getText();
        String dateResignedStr = txtDGYear.getText() + "-" + txtDGMonth.getText() + "-" + txtDGDay.getText();

        if (employeeId.isEmpty() || lastName.isEmpty() || firstName.isEmpty() || email.isEmpty() || gender.isEmpty() || mobileNo.isEmpty() || address.isEmpty() || birthdateStr.isEmpty() || status.isEmpty() || dateStartedStr.isEmpty() || dateResignedStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return;
        }

        try {
            // Parse the dates
            java.sql.Date birthdate = java.sql.Date.valueOf(birthdateStr);
            java.sql.Date dateStarted = java.sql.Date.valueOf(dateStartedStr);
            java.sql.Date dateResigned = java.sql.Date.valueOf(dateResignedStr);

            conn = ConnectPLMDB.Connect();
            ps = conn.prepareStatement("SELECT COUNT(*) FROM employee WHERE employee_id = ?"); 
            ps.setString(1, employeeId);
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Employee ID already exists.");
                conn.close();
                return;
            }

            ps = conn.prepareStatement("INSERT INTO employee (employee_id, lastname, firstname, email, gender, cp_num, address, bday, status, date_started, date_resigned) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, employeeId);
            ps.setString(2, lastName);
            ps.setString(3, firstName);
            ps.setString(4, email);
            ps.setString(5, gender);
            ps.setString(6, mobileNo);
            ps.setString(7, address);
            ps.setDate(8, birthdate);
            ps.setString(9, status);
            ps.setDate(10, dateStarted);
            ps.setDate(11, dateResigned);
            ps.executeUpdate();
            conn.close();
            JOptionPane.showMessageDialog(null, "Record added successfully!");
            refresh();
            toggleFilter(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding record: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid date format. Please use YYYY-MM-DD.");
        }
    }//GEN-LAST:event_btnAddMouseClicked

    private void txtBDYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBDYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBDYearActionPerformed

    private void txtEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeeIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmployeeIdActionPerformed

    private void tblEmployeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeesMouseClicked
        int row = tblEmployees.getSelectedRow();
        toggleFilter(false);
        if (row >= 0) {
            txtEmployeeId.setText(tblEmployees.getModel().getValueAt(row, 0).toString());
            String[] name = tblEmployees.getModel().getValueAt(row, 1).toString().split(", ");
            txtLastName.setText(name[0]);
            txtFirstName.setText(name[1]);
            txtEmail.setText(tblEmployees.getModel().getValueAt(row, 2).toString());
            cmbGender.setSelectedItem(tblEmployees.getModel().getValueAt(row, 3).toString());
            txtMobileNo.setText(tblEmployees.getModel().getValueAt(row, 4).toString());
            txtAddress.setText(tblEmployees.getModel().getValueAt(row, 5).toString());
            String[] birthdate = tblEmployees.getModel().getValueAt(row, 6).toString().split("-");
            txtBDYear.setText(birthdate[0]);
            txtBDMonth.setText(birthdate[1]);
            txtBDDay.setText(birthdate[2]);
            cmbStatus.setSelectedItem(tblEmployees.getModel().getValueAt(row, 7).toString());
            String[] dateStarted = tblEmployees.getModel().getValueAt(row, 8).toString().split("-");
            txtDSYear.setText(dateStarted[0]);
            txtDSMonth.setText(dateStarted[1]);
            txtDSDay.setText(dateStarted[2]);
            String[] dateResigned = tblEmployees.getModel().getValueAt(row, 9).toString().split("-");
            txtDGYear.setText(dateResigned[0]);
            txtDGMonth.setText(dateResigned[1]);
            txtDGDay.setText(dateResigned[2]);
        }
        toggleFilter(true);
    }//GEN-LAST:event_tblEmployeesMouseClicked

    private void txtDSYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDSYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDSYearActionPerformed

    private void txtDGYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDGYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDGYearActionPerformed

    private void txtLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLastNameActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        refresh();
    }//GEN-LAST:event_formWindowActivated

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Employee().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private finalproject.Components.Button btnAdd;
    private finalproject.Components.Button btnDelete;
    private finalproject.Components.Button btnUpdate;
    private javax.swing.JComboBox<String> cmbGender;
    private javax.swing.JComboBox<String> cmbStatus;
    private finalproject.Menu.Dashboard dashboard1;
    private finalproject.Menu.Header header1;
    private finalproject.Menu.Header header2;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.raven.swing.RoundPanel roundPanel1;
    private javax.swing.JTable tblEmployees;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBDDay;
    private javax.swing.JTextField txtBDMonth;
    private javax.swing.JTextField txtBDYear;
    private javax.swing.JTextField txtDGDay;
    private javax.swing.JTextField txtDGMonth;
    private javax.swing.JTextField txtDGYear;
    private javax.swing.JTextField txtDSDay;
    private javax.swing.JTextField txtDSMonth;
    private javax.swing.JTextField txtDSYear;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmployeeId;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtMobileNo;
    // End of variables declaration//GEN-END:variables
}
    

