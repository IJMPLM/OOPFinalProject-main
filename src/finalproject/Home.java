package finalproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import finalproject.Connection.ConnectPLMDB;


public class Home extends javax.swing.JFrame {


     public Home() {
        initComponents();
        
        setBackground(new Color(0, 0, 0, 0));
       
        statPanel1.setLayout(new BorderLayout());
        statPanel1.setPreferredSize(new Dimension(420, 200));
        statPanel2.setLayout(new BorderLayout());
        statPanel2.setPreferredSize(new Dimension(420, 200));
        
        showGenderPieChart();
        showCoursePieChart();
    }
    
    public void showGenderPieChart() {

    DefaultPieDataset barDataset = new DefaultPieDataset();

    try {
        Connection conn = ConnectPLMDB.Connect();

        String sql = "SELECT gender, COUNT(*) AS value FROM plm.student GROUP BY gender"; 
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String gender = resultSet.getString("gender"); 
            double value = resultSet.getDouble("value");  
            barDataset.setValue(gender, value);     
        }

        resultSet.close();
        statement.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    JFreeChart piechart = ChartFactory.createPieChart(
        "Student Gender Chart",   
        barDataset,                     
        false,                        
        true,                        
        false                          
    );

    PiePlot piePlot = (PiePlot) piechart.getPlot();
    piePlot.setBackgroundPaint(new Color(212,228,255));
    

    piePlot.setLabelGenerator(new org.jfree.chart.labels.StandardPieSectionLabelGenerator(
        "{0}: {1} ({2})" 
    ));

    piePlot.setLabelBackgroundPaint(Color.WHITE);
    piePlot.setLabelFont(new java.awt.Font("Inter", java.awt.Font.PLAIN, 15));
    piePlot.setLabelOutlinePaint(Color.BLACK);
    piePlot.setLabelShadowPaint(null);
    
    piePlot.setSectionPaint("F", new Color(91,142,225));   // Set color for "F"
    piePlot.setSectionPaint("M", new Color(222,77,77));

    ChartPanel barChartPanel = new ChartPanel(piechart);
    statPanel1.removeAll();
    statPanel1.add(barChartPanel, BorderLayout.CENTER);
    statPanel1.validate();
}

public void showCoursePieChart() {

    DefaultPieDataset courseDataset = new DefaultPieDataset();

    try {
        Connection conn = ConnectPLMDB.Connect();

        String sql = "SELECT course_code, COUNT(*) AS value FROM plm.student GROUP BY course_code"; 
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String course = resultSet.getString("course_code"); 
            double value = resultSet.getDouble("value");     
            courseDataset.setValue(course, value);        
        }

        resultSet.close();
        statement.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }


    JFreeChart coursePieChart = ChartFactory.createPieChart(
        "Student Course Distribution", 
        courseDataset,            
        false,                      
        true,                         
        false                      
    );

 
    PiePlot piePlot = (PiePlot) coursePieChart.getPlot();
    piePlot.setBackgroundPaint(new Color(212,228,255));
    
    piePlot.setLabelGenerator(new org.jfree.chart.labels.StandardPieSectionLabelGenerator(
        "{0}: {1} ({2})" 
    ));

    piePlot.setLabelBackgroundPaint(Color.WHITE);
    piePlot.setLabelFont(new java.awt.Font("Inter", java.awt.Font.PLAIN, 15));
    piePlot.setLabelOutlinePaint(Color.BLACK);
    piePlot.setLabelShadowPaint(null);
    
    piePlot.setSectionPaint("F", new Color(91,142,225));   // Set color for "F"
    piePlot.setSectionPaint("M", new Color(222,77,77));

    ChartPanel courseChartPanel = new ChartPanel(coursePieChart);
    statPanel2.removeAll();
    statPanel2.add(courseChartPanel, BorderLayout.CENTER);
    statPanel2.validate();
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header1 = new finalproject.Menu.Header();
        roundPanel1 = new com.raven.swing.RoundPanel();
        header2 = new finalproject.Menu.Header();
        dashboard1 = new finalproject.Menu.Dashboard();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        statPanel1 = new finalproject.Components.RoundPanel();
        statPanel2 = new finalproject.Components.RoundPanel();

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
        });

        roundPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/finalproject/images/home-banner.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("____________________________________________");

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("explore the statistics");

        jLabel4.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText(" ___________________________________________");

        statPanel1.setBackground(new java.awt.Color(212, 228, 255));

        javax.swing.GroupLayout statPanel1Layout = new javax.swing.GroupLayout(statPanel1);
        statPanel1.setLayout(statPanel1Layout);
        statPanel1Layout.setHorizontalGroup(
            statPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
        );
        statPanel1Layout.setVerticalGroup(
            statPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 147, Short.MAX_VALUE)
        );

        statPanel2.setBackground(new java.awt.Color(212, 228, 255));

        javax.swing.GroupLayout statPanel2Layout = new javax.swing.GroupLayout(statPanel2);
        statPanel2.setLayout(statPanel2Layout);
        statPanel2Layout.setHorizontalGroup(
            statPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
        );
        statPanel2Layout.setVerticalGroup(
            statPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 147, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(dashboard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(44, 44, 44)
                                .addComponent(jLabel3)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel4))
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addComponent(statPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(statPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(48, Short.MAX_VALUE))
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addComponent(header2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel2)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)))
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jLabel3)))
                        .addGap(44, 44, 44)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(statPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(dashboard1, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        header2.getAccessibleContext().setAccessibleDescription("");

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

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private finalproject.Menu.Dashboard dashboard1;
    private finalproject.Menu.Header header1;
    private finalproject.Menu.Header header2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private com.raven.swing.RoundPanel roundPanel1;
    private finalproject.Components.RoundPanel statPanel1;
    private finalproject.Components.RoundPanel statPanel2;
    // End of variables declaration//GEN-END:variables

}
