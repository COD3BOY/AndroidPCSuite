
package pcsuiteserver;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PcSuiteHome extends javax.swing.JFrame {
    
            DataInputStream is;
            OutputStream out;
            Connection mConn;
            Statement mStmt;
            String mData="";
            Vector dataVect, tempVect, colVect;

         public PcSuiteHome() {
        initComponents();

         
         try
        {
        Class.forName("com.mysql.jdbc.Driver");
        mConn=DriverManager.getConnection("jdbc:mysql://localhost:3306/pcsuite", "root", "root");
        mStmt = mConn.createStatement();
        }
        catch(Exception ex){
        JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }

       public void refreshtable()
    {
         try {

                String sqlquery = "SELECT * FROM contacts";
                PreparedStatement pstshow = (PreparedStatement) mConn.prepareStatement(sqlquery);
                ResultSet resultset = (ResultSet) pstshow.executeQuery();

                dataVect = new Vector();
                while (resultset.next()) {
                    tempVect = new Vector();
                    tempVect.add(resultset.getString("ContactName"));
                    tempVect.add(resultset.getString("PhoneNo"));
                    dataVect.add(tempVect);
                     }
               colVect=new Vector();
               colVect.add("  Contact Name  ");
               colVect.add("  Phone No  ");
               jTable1.setModel(new DefaultTableModel(dataVect, colVect));
               ((DefaultTableCellRenderer)jTable1.getTableHeader().getDefaultRenderer())
               .setHorizontalAlignment(JLabel.CENTER);
               resultset.close();
               pstshow.close();
         }
               catch(Exception e)
               { 
                System.out.println("Exception#RefreshTable : "+e.getMessage());
               }
         } 
    public void refreshtableForMessage()
    {
         try {
            
  //              Class.forName("com.mysql.jdbc.Driver");
  //              mConn=DriverManager.getConnection("jdbc:mysql://localhost:3306/pcsuite", "root", "root");
                String sqlquery = "SELECT * FROM Messages";
                PreparedStatement pstshow = (PreparedStatement) mConn.prepareStatement(sqlquery);
                ResultSet resultset = (ResultSet) pstshow.executeQuery();

                dataVect = new Vector();
                while (resultset.next()) {
                    tempVect = new Vector();
                    tempVect.add(resultset.getString("Name"));
                    tempVect.add(resultset.getString("Message"));
                    dataVect.add(tempVect);
                     }
               colVect=new Vector();
               colVect.add("  Sender Name  ");
               colVect.add("  Message  ");
               jTable2.setModel(new DefaultTableModel(dataVect, colVect));
               ((DefaultTableCellRenderer)jTable2.getTableHeader().getDefaultRenderer())
               .setHorizontalAlignment(JLabel.CENTER);
               resultset.close();
               pstshow.close();
         }
               catch(Exception e)
               { 
               System.out.println("Exception#RefreshTableMessage : "+e.getMessage());
               
               }
         }

class DatePicker {
        int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);;
        JLabel l = new JLabel("", JLabel.CENTER);
        String day = "";
        JDialog d;
        JButton[] button = new JButton[49];

        public DatePicker(JFrame parent) {
                d = new JDialog();
                d.setModal(true);
                String[] header = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
                JPanel p1 = new JPanel(new GridLayout(7, 7));
                p1.setPreferredSize(new Dimension(430, 120));

                for (int x = 0; x < button.length; x++) {
                        final int selection = x;
                        button[x] = new JButton();
                        button[x].setFocusPainted(false);
                        button[x].setBackground(Color.white);
                        if (x > 6)
                                button[x].addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent ae) {
                                                day = button[selection].getActionCommand();
                                                d.dispose();
                                        }
                                });
                        if (x < 7) {
                                button[x].setText(header[x]);
                                button[x].setForeground(Color.red);
                        }
                        p1.add(button[x]);
                }
                JPanel p2 = new JPanel(new GridLayout(1, 3));
                JButton previous = new JButton("<< Previous");
                previous.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                                month--;
                                displayDate();
                        }
                });
                p2.add(previous);
                p2.add(l);
                JButton next = new JButton("Next >>");
                next.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                                month++;
                                displayDate();
                        }
                });
                p2.add(next);
                d.add(p1, BorderLayout.CENTER);
                d.add(p2, BorderLayout.SOUTH);
                d.pack();
                d.setLocationRelativeTo(parent);
                displayDate();
                d.setVisible(true);
        }

        public void displayDate() {
                for (int x = 7; x < button.length; x++)
                        button[x].setText("");
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                                "MMMM yyyy");
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(year, month, 1);
                int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
                int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
                for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
                        button[x].setText("" + day);
                l.setText(sdf.format(cal.getTime()));
                d.setTitle("Date Picker");
        }
        public String setPickedDate() {
                if (day.equals(""))
                        return day;
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                                "dd-MM-yyyy");
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(year, month, Integer.parseInt(day));
                return sdf.format(cal.getTime());
        }
}


    
public class WaitThread implements Runnable{

	public WaitThread() {
	}
	@Override
	public void run() {
		waitForConnection();
	}
	private void waitForConnection() {
            JFrame abc = new WaitFrame();
            abc.setVisible(true);
            LocalDevice local = null;
		StreamConnectionNotifier notifier;
		StreamConnection connection = null;
		try {
			local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);
			UUID uuid = new UUID(80087355); // "04c6093b-0000-1000-8000-00805f9b34fb"
			String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
			notifier = (StreamConnectionNotifier)Connector.open(url);
		} catch (Exception e) {
                        JOptionPane.showMessageDialog(rootPane, e.getMessage());
			return;
		}
		while(true) {
			try {
				System.out.println("waiting for connection...");
	                  	connection = notifier.acceptAndOpen();
                                System.out.println("Connected ..");
                                abc.setVisible(false);
                                Thread processThread = new Thread(new ProcessConnectionThread(connection));
				processThread.start();
			} catch (Exception e) {
                            JOptionPane.showMessageDialog(rootPane, e.getMessage());
				return;
			}
		}
	}
}

public class ContactDetails implements Runnable{
        @Override
public void run(){
        try
        {     
        byte [] mBytes = "contacts".getBytes();
        out.write(mBytes);
        out.flush();
        boolean mFlag = false, LoopFlag=true;
        while(LoopFlag)
        {
        while(is.available() > 0 && mFlag == false)
        {
        byte [] mBytes1 = new byte[65535];
        is.read(mBytes1, 0, mBytes1.length);
        mData = new String(mBytes1);
        System.out.println(mData);
        mFlag=true;
        LoopFlag=false;
        }
            System.out.println("out of the loop");
        }
        mStmt.executeUpdate("delete from contacts");
        String [] mValues = mData.split(";");
        for(int i=0; i<mValues.length-1; i++)
        {
            int pos = mValues[i].indexOf("=");
            pos+=1;
            String SqlStmt = String.format("insert into contacts values ('%s', '%s')", mValues[i].substring(0, pos-1), mValues[i].substring(pos) );
            System.out.println(SqlStmt);
            mStmt.executeUpdate(SqlStmt);
        }
          jDialog1.setTitle("Contact List - Android Pc Suite");
          jDialog1.setVisible(true);
         refreshtable();
        }
          
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
}
}
public class MessageDetails implements Runnable{
        @Override
        public void run(){
        try
        {     
        byte [] mBytes = "messages".getBytes();
        out.write(mBytes);
        out.flush();
        boolean mFlag = false, LoopFlag=true;
        while(LoopFlag)
        {
        while(is.available() > 0 && mFlag == false)
        {
        byte [] mBytes1 = new byte[65535];
        is.read(mBytes1, 0, mBytes1.length);
        mData = new String(mBytes1);
        System.out.println(mData);
        mFlag=true;
        LoopFlag=false;
        }
            System.out.println("out of the loop");
        }
        mStmt.executeUpdate("delete from Messages");
        String [] mValues = mData.split(";");
        for(int i=0; i<mValues.length-1; i++)
        {
            int pos = mValues[i].indexOf("=");
            pos+=1;
            String SqlStmt = String.format("insert into Messages values ('%s', '%s')", mValues[i].substring(0, pos-1), mValues[i].substring(pos) );
            System.out.println(SqlStmt);
            mStmt.executeUpdate(SqlStmt);
        }
          
        }
          
        catch(Exception ex)
        {
            System.out.println("Exception#MessageDetails Thread : "+ex.getMessage());
        }
}
}
public class ProcessConnectionThread implements Runnable {

    private StreamConnection mConnection;
    public ProcessConnectionThread(StreamConnection connection) {
    mConnection = connection;
    }
    @Override
    public void run() {
        try {
             is = mConnection.openDataInputStream();
             out = mConnection.openOutputStream();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jDialog2 = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jDialog3 = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jDialog4 = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jDialog5 = new javax.swing.JDialog();
        jTextField3 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jSpinner1 = new JSpinner( new SpinnerDateModel() );
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();

        jDialog1.setMinimumSize(new java.awt.Dimension(560, 280));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Contact Name", "Contact No"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Add Contact");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Delete Contact");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Close");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jDialog2.setMinimumSize(new java.awt.Dimension(400, 160));

        jLabel1.setText("Contact Name");

        jLabel2.setText("Phone No");

        jButton4.setMnemonic('o');
        jButton4.setText("Ok");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setMnemonic('n');
        jButton5.setText("Cancel");

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                .addGap(46, 46, 46)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                .addGap(23, 23, 23))
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton4))
                .addGap(29, 29, 29)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jButton5))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jDialog3.setMinimumSize(new java.awt.Dimension(537, 286));

        jTable2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Message"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jButton6.setText("Remove Message");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Send Message");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Close");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog3Layout.createSequentialGroup()
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jButton6)
                        .addGap(35, 35, 35)
                        .addComponent(jButton7)
                        .addGap(39, 39, 39)
                        .addComponent(jButton8)))
                .addContainerGap())
        );

        jDialog4.setMinimumSize(new java.awt.Dimension(402, 260));

        jLabel3.setText("Contact Name");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Message");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jButton9.setText("Send");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Cancel");

        javax.swing.GroupLayout jDialog4Layout = new javax.swing.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(59, 59, 59)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38))
            .addGroup(jDialog4Layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(54, 54, 54)
                .addComponent(jButton10)
                .addGap(129, 129, 129))
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9)
                    .addComponent(jButton10))
                .addContainerGap())
        );

        jDialog5.setMinimumSize(new java.awt.Dimension(450, 200));

        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField3FocusGained(evt);
            }
        });

        jButton11.setText("Date Picker");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel5.setText("Remainder");

        jButton12.setText("Ok");
        jButton12.setMinimumSize(new java.awt.Dimension(65, 23));
        jButton12.setPreferredSize(new java.awt.Dimension(65, 23));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("Cancel");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel6.setText("Date");

        jLabel7.setText("Time");

        javax.swing.GroupLayout jDialog5Layout = new javax.swing.GroupLayout(jDialog5.getContentPane());
        jDialog5.getContentPane().setLayout(jDialog5Layout);
        jDialog5Layout.setHorizontalGroup(
            jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog5Layout.createSequentialGroup()
                .addGroup(jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog5Layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addComponent(jButton13))
                    .addGroup(jDialog5Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                        .addGroup(jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton11)
                .addGap(30, 30, 30))
        );
        jDialog5Layout.setVerticalGroup(
            jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addGroup(jDialog5Layout.createSequentialGroup()
                        .addGroup(jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jButton11))
                        .addGap(18, 18, 18)
                        .addGroup(jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("P C Suite for Android Mobile ");

        jMenu3.setText("Admin");

        jMenuItem1.setMnemonic('v');
        jMenuItem1.setText("Start Service");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem2.setText("Exit Application");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar2.add(jMenu3);

        jMenu4.setText("Options");
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });

        jMenuItem3.setMnemonic('g');
        jMenuItem3.setText("Contacts");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem5.setMnemonic('s');
        jMenuItem5.setText("Messages");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuItem6.setMnemonic('e');
        jMenuItem6.setText("Calendar");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuItem7.setMnemonic('u');
        jMenuItem7.setText("Upload File");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuBar2.add(jMenu4);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 425, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Thread waitThread = new Thread(new WaitThread());
	waitThread.start();      
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        int choice = JOptionPane.showConfirmDialog(this, "This will End Your Application.\r\nAre You Sure?", "End Application", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if(choice==JOptionPane.YES_OPTION)
        {
                System.exit(0);
        }      
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
      
        Thread PhoneContacts = new Thread(new ContactDetails());
        PhoneContacts.start();
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed
    }//GEN-LAST:event_jMenu4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         jDialog2.setTitle("Add New Contact - Android Pc Suite ");
         jDialog2.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        try {
            mStmt.executeUpdate(String.format("insert into contacts values('%s', '%s')", jTextField1.getText(), jTextField2.getText()));
            refreshtable();
            byte [] mBytes = ("contacts##"+jTextField1.getText()+"##"+jTextField2.getText()).getBytes();
            out.write(mBytes);
            out.flush();
        } catch (Exception ex) {
            Logger.getLogger(PcSuiteHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        jDialog2.setTitle("Add New Contact - Android Pc Suite ");
        jDialog2.setVisible(false);
        JOptionPane.showMessageDialog(rootPane, "Contact Successfully Added to the Contact ");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        String name = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
        String number = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
         try {
            mStmt.executeUpdate("delete from contacts where ContactName='" + name + "' and PhoneNo='" + number + "'");
            refreshtable();
            byte [] mBytes = ("delete##" + name + "##" + number).getBytes();
            out.write(mBytes);
            out.flush();
            System.out.println(name +" = " + number);
            refreshtable();
            JOptionPane.showMessageDialog(rootPane, "Contact Successfully Removed From Smartphone");
       } catch (Exception ex) {
            Logger.getLogger(PcSuiteHome.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
         jDialog1.setTitle("Contact Details - Android Pc Suite");
         jDialog1.setVisible(false);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        Thread PhoneMessages = new Thread(new MessageDetails());
        PhoneMessages.start();
        
        jDialog3.setTitle("Message List - Android Pc Suite");
        jDialog3.setVisible(true);
        refreshtableForMessage();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jDialog3.setTitle("Message List - Android PC Suite");
        jDialog3.setVisible(false);
    }//GEN-LAST:event_jButton8ActionPerformed
       String [] ContactNames= null;
        String [] ContactNumbers=null;
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        this.jComboBox1.removeAllItems();
        jDialog4.setTitle("Autmomated SMS Redirector - Android Pc Suite ");
        jDialog4.setVisible(true);
        int count=0;
         try
        {
        ResultSet mRst = mStmt.executeQuery("Select * from contacts");
        while(mRst.next())
        {
        count++;
        }
        System.out.println(count);
        mRst.first();

        ContactNames = new String[count];
        ContactNumbers = new String[count];
        int i=0;
        while(mRst.next())
        {
            ContactNames[i] = mRst.getString(1);
            ContactNumbers[i]= mRst.getString(2);
            i++;
        }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(rootPane, "No Data could be retrieved from Contacts.  Error Returned : " + ex.getMessage());
        }
        for(int i=0; i<count; i++)
        {
            this.jComboBox1.addItem(ContactNames[i]);
        }
        this.jComboBox1.setSelectedIndex(0);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        String number = (String) ContactNumbers[jComboBox1.getSelectedIndex()];
        String name = (String) jComboBox1.getSelectedItem();
        String message = jTextArea1.getText();
         try {
            mStmt.executeUpdate(String.format("insert into messages values('%s', '%s')", number, message));
            refreshtableForMessage();
            byte [] mBytes = ("messageinsert##" + number + "##" + message).getBytes();
            out.write(mBytes);
            out.flush();
            System.out.println(number +" = " + message);
            refreshtableForMessage();
            JOptionPane.showMessageDialog(rootPane, "Message Successfully Sent To contact  << " + name + " >>");
       } catch (Exception ex) 
       {
            Logger.getLogger(PcSuiteHome.class.getName()).log(Level.SEVERE, null, ex);
       }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String name = (String) jTable2.getValueAt(jTable2.getSelectedRow(), 0);
        String body = (String) jTable2.getValueAt(jTable2.getSelectedRow(), 1);
        try
        {
        System.out.println(name + " = " + body);
//        ResultSet mRst = mStmt.executeQuery("Select PhoneNo from contacts where ContactName = '" + name + "'");
//        System.out.println("after resultset");
//        if(mRst.next())
//        {
//            System.out.println("in the ResultSet before delete");
//            String mMessage = mRst.getString(1);
        
            mStmt.execute("delete from messages where Name = '" + name + "' and Message = '" + body + "'" );
            System.out.println("delete from messages where Name = '" + name + "' and Message = '" + body + "'");
            
//            byte [] mBytes = ("messagedelete##" + mMessage + "##" + body).getBytes();
            
            byte [] mBytes = ("messagedelete##" + "tt" + "##" + body).getBytes();
            out.write(mBytes);
            out.flush();
            System.out.println(name + " = " + body);
            JOptionPane.showMessageDialog(rootPane, "Message Successfully Deleted!!!");
//        }
//        else
//        {
//            System.out.println("ELse!");
//        }
//        mRst.close();
        refreshtableForMessage();
        }
        catch(Exception ex)
        {
            System.out.println("Exception#ButtonClick : "+ex.getMessage());

        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
JFileChooser chooser = new JFileChooser();
File mFile = new File(".");
chooser.setCurrentDirectory(new File(mFile.getAbsolutePath()));
chooser.showOpenDialog(null);
File myFile = chooser.getSelectedFile();

   
try{
    byte [] mBytes = ("incomingfile##" + "tt" + "##" ).getBytes();
    out.write(mBytes);
    out.flush();
    
    sendFile(myFile);
    
}

catch(Exception re)
{
    System.out.println("Exception#SendFile : "+re.getMessage());

}
//8888888888888888888

//8888888888888888888888
//String path= myFile.getAbsolutePath();
//path = path.replaceAll("\\\\","\\\\\\\\");

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(jSpinner1, "HH:mm:ss");
jSpinner1.setEditor(timeEditor);
jSpinner1.setValue(new Date());
        jDialog5.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        jTextField3.setText(new DatePicker(this).setPickedDate());
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        try
        {
            Date date = (Date) jSpinner1.getValue();
            String hours = "" + date.getHours();
            String mins  = "" + date.getMinutes();
            String time = hours+"::"+mins;
            byte [] mBytes = ("calendar##" + jTextField3.getText() + "##" + time + "##" + jTextField4.getText()).getBytes();
            System.out.println("calendar##" + jTextField3.getText() + "##" + time + "##" + jTextField4.getText());
            out.write(mBytes);
            out.flush();
        }
        catch(Exception ex)
        {}
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusGained
       jSpinner1.requestFocus();
    }//GEN-LAST:event_jTextField3FocusGained

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        jDialog5.setVisible(false);
    }//GEN-LAST:event_jButton13ActionPerformed

    public void sendFile(File file) throws IOException, ClassNotFoundException {
    byte[] buf = new byte[1024];
    BufferedOutputStream mout = new BufferedOutputStream(out, 1024);
    FileInputStream inn = new FileInputStream(file);
    int i = 0;
    int bytecount = 1024;
    while ((i = inn.read(buf, 0, 1024)) != -1) {
      bytecount = bytecount + 1024;
      mout.write(buf, 0, i);
      mout.flush();
    }
    //out.shutdownOutput(); /* important */
    System.out.println("Bytes Sent :" + bytecount);
    mout.close();

    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PcSuiteHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PcSuiteHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PcSuiteHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PcSuiteHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new PcSuiteHome().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JDialog jDialog5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
