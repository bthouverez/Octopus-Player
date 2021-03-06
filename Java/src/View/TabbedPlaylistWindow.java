/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Playlist;
import Model.Song;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Babs
 */
public class TabbedPlaylistWindow extends javax.swing.JFrame {

    /**
     * Creates new form MainWindow
     */
    public TabbedPlaylistWindow() {
        initComponents();
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
        jTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        playlistNameLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "dirname", "basename", "title", "artist", "album", "comment", "track", "year", "genre", "genre str"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        jLabel1.setText("Playlist");

        playlistNameLabel.setText("azertyuiop");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1078, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playlistNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(playlistNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void setPlaylistName(String s) {
        this.playlistNameLabel.setText(s);
    }

    public void fill(Playlist p) {
        for (int ii = 0; ii < p.size(); ii++) {
            Song s = p.get(ii);
            DefaultTableModel model = (DefaultTableModel) this.jTable.getModel();
            model.addRow(new Object[]{
                s.getId(),
                s.getDirname(),
                s.getBasename(),
                s.getTitle(),
                s.getArtist(),
                s.getAlbum(),
                s.getComment(),
                s.getTrack(),
                s.getYear(),
                s.getGenre(),
                s.getGenreStr()
            });
        }
        this.jTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        this.jTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        this.jTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        this.jTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        this.jTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        this.jTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        this.jTable.getColumnModel().getColumn(6).setPreferredWidth(500);
        this.jTable.getColumnModel().getColumn(7).setPreferredWidth(60);
        this.jTable.getColumnModel().getColumn(8).setPreferredWidth(75);
        this.jTable.getColumnModel().getColumn(9).setPreferredWidth(100);
        this.jTable.getColumnModel().getColumn(10).setPreferredWidth(200);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JLabel playlistNameLabel;
    // End of variables declaration//GEN-END:variables
}
