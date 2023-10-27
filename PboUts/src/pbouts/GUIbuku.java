/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbouts;

//import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.TypedQuery;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class GUIbuku extends javax.swing.JFrame {

    public void peringatan(String pesan) {
        JOptionPane.showMessageDialog(rootPane, pesan);
    }
    ArrayList<Buku> dataBuku;

    private Timer refresTimer;

    public GUIbuku(Timer refreshTimer) {
        try {
            dataBuku = new ArrayList<>();
            initComponents();
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Db_Buku", "postgres", "12345");
            tampil(conn);

            // Membuat dan mengatur timer untuk auto-refresh setiap 5 detik (5000 milidetik)
            refreshTimer = new Timer(1000, (ActionEvent e) -> {
                tampil(conn);
            });
            refreshTimer.start();
        } catch (SQLException ex) {
            Logger.getLogger(GUIbuku.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int masukkanData(Connection conn, String ISBN, String JudulBuku, String TahunTerbit, String Penerbit) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("INSERT INTO buku (isbn, judul_buku, tahun_terbit, penerbit) VALUES(?,?,?,?)");
        pst.setString(1, ISBN);
        pst.setString(2, JudulBuku);
        pst.setString(3, TahunTerbit);
        pst.setString(4, Penerbit);
        return pst.executeUpdate();
    }

    private int updateData(Connection conn, String ISBN, String JudulBuku, String TahunTerbit, String Penerbit) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("UPDATE buku SET isbn = ?, judul_buku = ?, tahun_terbit = ?, penerbit = ? WHERE isbn = ?");
        pst.setString(1, ISBN);
        pst.setString(2, JudulBuku);
        pst.setString(3, TahunTerbit);
        pst.setString(4, Penerbit);
        pst.setString(5, ISBN);
        return pst.executeUpdate();
    }

    private int hapusData(Connection conn, String ISBN) throws SQLException {
        String query = "DELETE FROM buku WHERE isbn = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, ISBN);

        return preparedStatement.executeUpdate();
    }

//    private void cetakDataTabel(JTable tabel) {
//        try {
//            // Membuat instance dari TableModel
//            TableModel model = tabel.getModel();
//
//            // Mencetak data tabel
//            boolean berhasil = tabel.print(JTable.PrintMode.FIT_WIDTH,
//                    null, null, true, null, true, null);
//
//            if (berhasil) {
//                JOptionPane.showMessageDialog(this, "Data telah dicetak.");
//            } else {
//                JOptionPane.showMessageDialog(this, "Gagal mencetak data.");
//            }
//        } catch (PrinterException e) {
//            JOptionPane.showMessageDialog(this, "Terjadi kesalahan mencetak: " + e.getMessage());
//        }
//    }
    private void tampil(Connection conn) {
//        dataBuku.clear();
//        try {
//            String sql = "select * from buku";
//            Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery(sql);
//            while (rs.next()) {
//                Buku data = new Buku();
//                data.setISBN(String.valueOf(rs.getObject(1)));
//                data.setJudulBuku(String.valueOf(rs.getObject(2)));
//                data.setTahunTerbit(String.valueOf(rs.getObject(3)));
//                data.setPenerbit(String.valueOf(rs.getObject(4)));
//                dataBuku.add(data);
//            }
//            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
//            model.setRowCount(0);
//            for (Buku data : dataBuku) {
//
//                Object[] baris = new Object[4];
//                baris[0] = data.getISBN();
//                baris[1] = data.getJudulBuku();
//                baris[2] = data.getTahunTerbit();
//                baris[3] = data.getPenerbit();
//                model.addRow(baris);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(GUIbuku.class.getName()).log(Level.SEVERE, null, ex);
//        }
        EntityManager entityManager = Persistence.createEntityManagerFactory("PboUtsPU").createEntityManager();
        entityManager.getTransaction().begin();
        TypedQuery<Buku_1> querySelectAll = entityManager.createNamedQuery("Buku_1.findAll", Buku_1.class);
        List<Buku_1> results = querySelectAll.getResultList();

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (Buku_1 data : results) {
            Object[] baris = new Object[4];
            baris[0] = data.getIsbn();
            baris[1] = data.getJudulBuku();
            baris[2] = data.getTahunTerbit();
            baris[3] = data.getPenerbit();
            model.addRow(baris);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private void tampil() {
//        dataBuku.clear();
//        try {
//            String sql = "select * from buku";
//            Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery(sql);
//            while (rs.next()) {
//                Buku data = new Buku();
//                data.setISBN(String.valueOf(rs.getObject(1)));
//                data.setJudulBuku(String.valueOf(rs.getObject(2)));
//                data.setTahunTerbit(String.valueOf(rs.getObject(3)));
//                data.setPenerbit(String.valueOf(rs.getObject(4)));
//                dataBuku.add(data);
//            }
//            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
//            model.setRowCount(0);
//            for (Buku data : dataBuku) {
//
//                Object[] baris = new Object[4];
//                baris[0] = data.getISBN();
//                baris[1] = data.getJudulBuku();
//                baris[2] = data.getTahunTerbit();
//                baris[3] = data.getPenerbit();
//                model.addRow(baris);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(GUIbuku.class.getName()).log(Level.SEVERE, null, ex);
//        }
        EntityManager entityManager = Persistence.createEntityManagerFactory("PboUtsPU").createEntityManager();
        entityManager.getTransaction().begin();
        TypedQuery<Buku_1> querySelectAll = entityManager.createNamedQuery("Buku_1.findAll", Buku_1.class);
        List<Buku_1> results = querySelectAll.getResultList();

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (Buku_1 data : results) {
            Object[] baris = new Object[4];
            baris[0] = data.getIsbn();
            baris[1] = data.getJudulBuku();
            baris[2] = data.getTahunTerbit();
            baris[3] = data.getPenerbit();
            model.addRow(baris);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public GUIbuku() {
        try {
            dataBuku = new ArrayList<>();
            initComponents();
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Db_Buku", "postgres", "12345");
            tampil(conn);

        } catch (SQLException ex) {
            Logger.getLogger(GUIbuku.class
                    .getName()).log(Level.SEVERE, null, ex);
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfisbn = new javax.swing.JTextField();
        tfjudul = new javax.swing.JTextField();
        tftahun = new javax.swing.JTextField();
        tfpenerbit = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1Simpan = new javax.swing.JButton();
        jButton2Update = new javax.swing.JButton();
        jButton3Delete = new javax.swing.JButton();
        jButton4Cetak = new javax.swing.JButton();
        jButtonImport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("INPUT DATA BUKU");

        jLabel2.setText("ISBN");

        jLabel3.setText("Judul Buku");

        jLabel4.setText("Tahun Terbit");

        jLabel5.setText("Penerbit");

        tfisbn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfisbnActionPerformed(evt);
            }
        });

        tfjudul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfjudulActionPerformed(evt);
            }
        });

        tftahun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tftahunActionPerformed(evt);
            }
        });

        tfpenerbit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfpenerbitActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ISBN", "Judul Buku", "Tahun Terbit", "Penerbit"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jScrollPane1.setViewportView(jScrollPane2);

        jButton1Simpan.setText("Simpan");
        jButton1Simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1SimpanActionPerformed(evt);
            }
        });

        jButton2Update.setText("Update");
        jButton2Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2UpdateActionPerformed(evt);
            }
        });

        jButton3Delete.setText("Delete");
        jButton3Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3DeleteActionPerformed(evt);
            }
        });

        jButton4Cetak.setText("Cetak");
        jButton4Cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4CetakActionPerformed(evt);
            }
        });

        jButtonImport.setText("Import");
        jButtonImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1Simpan)
                                .addGap(59, 59, 59)
                                .addComponent(jButton2Update)
                                .addGap(51, 51, 51)
                                .addComponent(jButton3Delete))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tfjudul, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfisbn, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tftahun, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfpenerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(233, 233, 233)
                            .addComponent(jLabel1))
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jButtonImport)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton4Cetak)
                            .addGap(21, 21, 21))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfisbn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfjudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tftahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tfpenerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1Simpan)
                    .addComponent(jButton2Update)
                    .addComponent(jButton3Delete))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4Cetak)
                    .addComponent(jButtonImport))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tftahunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tftahunActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tftahunActionPerformed

    private void tfisbnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfisbnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfisbnActionPerformed

    private void jButton1SimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1SimpanActionPerformed
        // TODO add your handling code here:
        String ISBN = tfisbn.getText().trim();
        String JudulBuku = tfjudul.getText();
        String TahunTerbit = tftahun.getText();
        String Penerbit = tfpenerbit.getText();

        /*if (!ISBN.isEmpty() && !JudulBuku.isEmpty() && !TahunTerbit.isEmpty() && !Penerbit.isEmpty()) {

            try {
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Db_Buku", "postgres", "12345");
                int k = masukkanData(conn, ISBN, JudulBuku, TahunTerbit, Penerbit);
                if (k > 0) {
                    tampil(conn);
                    this.peringatan("Simpan Berhasil");
                } else {
                    this.peringatan("Simpan Gagal");
                }
            } catch (SQLException ex) {
                Logger.getLogger(GUIbuku.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.peringatan("Input Data MataKuliah Gagal");
        }*/
        // awal persistence
        EntityManager entityManager = Persistence.createEntityManagerFactory("PboUtsPU").createEntityManager();
        entityManager.getTransaction().begin();
        Buku_1 b = new Buku_1();
        b.setIsbn(ISBN);
        b.setJudulBuku(JudulBuku);
        b.setTahunTerbit(TahunTerbit);
        b.setPenerbit(Penerbit);
        entityManager.persist(b);
        entityManager.getTransaction().commit();
        // akhir persistence

        tfisbn.setText("");
        tfjudul.setText("");
        tftahun.setText("");
        tfpenerbit.setText("");
    }//GEN-LAST:event_jButton1SimpanActionPerformed

    private void jButton2UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2UpdateActionPerformed

        String ISBN = tfisbn.getText().trim();
        String JudulBuku = tfjudul.getText();
        String TahunTerbit = tftahun.getText();
        String Penerbit = tfpenerbit.getText();

        /*if (!ISBN.isEmpty() && !JudulBuku.isEmpty() && !TahunTerbit.isEmpty() && !Penerbit.isEmpty()) {

            try {
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Db_Buku", "postgres", "12345");
                  int k = updateData(conn, ISBN, JudulBuku, TahunTerbit, Penerbit);
                if (k > 0) {
                    tampil(conn);
                    this.peringatan("Update Berhasil");
                } else {
                    this.peringatan("Update Gagal");
                }
            } catch (SQLException ex) {
                Logger.getLogger(GUIbuku.class.getName()).log(Level.SEVERE, null, ex);
            } 
        } else {
            this.peringatan("Input Data MataKuliah Gagal");
        }*/
        // awal persistence
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PboUtsPU");
        EntityManager em = emf.createEntityManager();
        Buku_1 b = new Buku_1();
        b.setIsbn(ISBN);
        b.setJudulBuku(JudulBuku);
        b.setTahunTerbit(TahunTerbit);
        b.setPenerbit(Penerbit);
        em.getTransaction().begin();
        em.merge(b);
        em.getTransaction().commit();
        // akhir persistence
        this.tampil();
        tfisbn.setText("");
        tfjudul.setText("");
        tftahun.setText("");
        tfpenerbit.setText("");
    }//GEN-LAST:event_jButton2UpdateActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        /*try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Db_Buku", "postgres", "12345");
            int row = jTable1.getSelectedRow();
            String tabel_klik = (jTable1.getModel().getValueAt(row, 0).toString());
            java.sql.Statement stm = conn.createStatement();
            java.sql.ResultSet sql = stm.executeQuery("select * from buku where isbn ='" + tabel_klik + "'");
            if (sql.next()) {
                String ISBN = sql.getString("isbn");
                tfisbn.setText(ISBN);
                String JudulBuku = sql.getString("judul_buku");
                tfjudul.setText(JudulBuku);
                String TahunTerbit = sql.getString("tahun_terbit");
                tftahun.setText(TahunTerbit);
                String Penerbit = sql.getString("penerbit");
                tfpenerbit.setText(Penerbit);
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jTable1MouseClicked
*/
        int baris = jTable1.rowAtPoint(evt.getPoint());
        String ISBN = jTable1.getValueAt(baris, 0).toString();
        tfisbn.setText(ISBN);

        String judul = jTable1.getValueAt(baris, 1).toString();
        tfjudul.setText(judul);

        String tahun = jTable1.getValueAt(baris, 2).toString();
        tftahun.setText(tahun);

        String penerbit = jTable1.getValueAt(baris, 3).toString();
        tfpenerbit.setText(penerbit);
    }


    private void jButton3DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3DeleteActionPerformed

        String ISBN = tfisbn.getText().trim();

        /*if (!ISBN.isEmpty()) {
            try {
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Db_Buku", "postgres", "12345");

                // Lakukan penghapusan data
                int k = hapusData(conn, ISBN);

                if (k > 0) {
                    tampil(conn);
                    this.peringatan("Hapus Berhasil");
                } else {
                    this.peringatan("Hapus Gagal");
                }
            } catch (SQLException ex) {
                Logger.getLogger(GUIbuku.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.peringatan("Input Kode Mata Kuliah yang akan dihapus");
        }*/
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PboUtsPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Buku_1 buku = em.find(Buku_1.class,
                ISBN);
        em.remove(buku);
        em.getTransaction().commit();

        em.close();
        emf.close();

        tfisbn.setText("");
    }//GEN-LAST:event_jButton3DeleteActionPerformed

    private void jButton4CetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4CetakActionPerformed

        /*try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Db_Buku", "postgres", "12345");
            //} catch (SQLException ex) {
            // Logger.getLogger(GUIMatkul.class.getName()).log(Level.SEVERE, null, ex);

            String jrxmlFile = new String("src/pbouts/reportBuku.jrxml");

            JasperReport jr = JasperCompileManager.compileReport(jrxmlFile);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
            JasperViewer.viewReport(jp, false);

        } catch (SQLException ex) {
            Logger.getLogger(GUIbuku.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(GUIbuku.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4CetakActionPerformed
*/
        try {
            EntityManager entityManager = Persistence.createEntityManagerFactory("PboUtsPU").createEntityManager();
            entityManager.getTransaction().begin();
            TypedQuery<Buku_1> querySelectAll = entityManager.createNamedQuery("Buku_1.findAll", Buku_1.class
            );
            List<Buku_1> results = querySelectAll.getResultList();
            entityManager.getTransaction().commit();
            entityManager.close();

            String jrxmlFile = "src/pbouts/reportBuku.jrxml";
            JasperReport jr = JasperCompileManager.compileReport(jrxmlFile);
//            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, new JRBeanCollectionDataSource(results));
            JasperViewer.viewReport(jp, false);

        } catch (JRException ex) {
            Logger.getLogger(GUIbuku.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (Exception e) {
            Logger.getLogger(GUIbuku.class
                    .getName()).log(Level.SEVERE, null, e);
        }
    }

    private void tfjudulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfjudulActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfjudulActionPerformed

    private void tfpenerbitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfpenerbitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfpenerbitActionPerformed

    private void jButtonImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportActionPerformed
        // TODO add your handling code here:
        JFileChooser filechooser = new JFileChooser();

        int i = filechooser.showOpenDialog(null);
        if (i == JFileChooser.APPROVE_OPTION) {

            EntityManager entityManager = Persistence.createEntityManagerFactory("PboUtsPU").createEntityManager();
            entityManager.getTransaction().begin();

            File f = filechooser.getSelectedFile();
            String filepath = f.getPath();
            String fi = f.getName();
            //Parsing CSV Data
            System.out.print(filepath);
            DefaultTableModel csv_data = new DefaultTableModel();

            try {

                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filepath));
                org.apache.commons.csv.CSVParser csvParser = CSVFormat.DEFAULT.parse(inputStreamReader);
                for (CSVRecord csvRecord : csvParser) {

                    Buku_1 z = new Buku_1();
                    z.setIsbn(csvRecord.get(0));
                    z.setJudulBuku(csvRecord.get(1));
                    z.setTahunTerbit(csvRecord.get(2));
                    z.setPenerbit(csvRecord.get(3));
                    entityManager.persist(z);

                }

            } catch (Exception ex) {
                System.out.println("Error in Parsing CSV File");
            }

            entityManager.getTransaction().commit();
        }
    }//GEN-LAST:event_jButtonImportActionPerformed

    
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
            java.util.logging.Logger.getLogger(GUIbuku.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIbuku.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIbuku.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIbuku.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIbuku().setVisible(true);
            }
        });
    }

    //public void refreshData() {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1Simpan;
    private javax.swing.JButton jButton2Update;
    private javax.swing.JButton jButton3Delete;
    private javax.swing.JButton jButton4Cetak;
    private javax.swing.JButton jButtonImport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tfisbn;
    private javax.swing.JTextField tfjudul;
    private javax.swing.JTextField tfpenerbit;
    private javax.swing.JTextField tftahun;
    // End of variables declaration//GEN-END:variables
}
