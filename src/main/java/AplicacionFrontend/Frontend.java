/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package AplicacionFrontend;

import static AplicacionBackend.Backend.controller;
import PitufoBros.GameThreadClass;
import clases.Game;
import clases.Player;
import clases.Videogame;
import daos.Factory;
import implementacion.XmlImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Vespertino
 */
public class Frontend extends javax.swing.JFrame {

    private String nickname = "kevin";
    private int currentGameID;
    private daos.SQLiteDAO localController;
    private daos.RemoteDAO remoteController;

    /**
     * Creates new form Frontend
     */
    public Frontend() {
        nickname= choosePlayer();
        initComponents();
        localController = new Factory("SQLite").createSQLiteDAO();

        XmlImpl config = new XmlImpl();
        String puerto = config.getConfig()[1];
        if (puerto.equals("5432")) {
            remoteController = new Factory("Postgres").createRemoteDAO();
        } else {
            remoteController = new Factory("MySQL").createRemoteDAO();
        }
        
    }

    private String choosePlayer() {
        JTextField tfNickName = new JTextField();

        Object[] message = {
            "Nick Name:", tfNickName,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Create Player", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String nickName = tfNickName.getText();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El jugador no existe");
                 String nickName = "";
            }

        }
                    return nickname;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jbPitufoBros = new javax.swing.JButton();
        jbTestGame = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 0, 153));
        jLabel1.setText("Game App");

        jLabel2.setBackground(new java.awt.Color(204, 0, 0));
        jLabel2.setForeground(new java.awt.Color(153, 0, 0));
        jLabel2.setIconTextGap(0);
        jLabel2.setPreferredSize(new java.awt.Dimension(32, 32));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jbPitufoBros.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbPitufoBros.setText("Pitufo-Bros");
        jbPitufoBros.setName("jbPitufoGame"); // NOI18N
        jbPitufoBros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPitufoBrosActionPerformed(evt);
            }
        });

        jbTestGame.setText("TEST_GAME");
        jbTestGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbTestGameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbPitufoBros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbTestGame, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbTestGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbPitufoBros, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                .addContainerGap(270, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbPitufoBrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPitufoBrosActionPerformed
        GameThreadClass game = new GameThreadClass();
        game.startGame();

    }//GEN-LAST:event_jbPitufoBrosActionPerformed

    private void connectedData() {
        Videogame juego = remoteController.getGameById(currentGameID);
        juego.setPlayer_count(juego.getPlayer_count() + 1);
        remoteController.updateVideogame(juego);
    }

    private void disconnectedData(ArrayList<Object> elements) {
        boolean a = true;
        int coins = (int) elements.get(0);
        int experience = (int) elements.get(1);
        LocalDateTime last_session = (LocalDateTime) elements.get(2);
        String plrNickname = (String) elements.get(3);

        Player jugador = remoteController.getPlayerByNickname(plrNickname);

        if (jugador == null) {
            while(a){
                nickname=choosePlayer();
                if(nickname != null){
                    a = false;
                }
            }
        }

        jugador.setCoins(jugador.getCoins() + coins);
        jugador.setExperience(jugador.getExperience() + experience);
        jugador.setLast_login(last_session);
        jugador.setSession_count(jugador.getSession_count() + 1);

        remoteController.updatePlayer(jugador);
        localController.updatePlayerState(jugador);

        Videogame juego = remoteController.getGameById(currentGameID);
        juego.setPlayer_count(juego.getPlayer_count() - 1);
        juego.setLast_session(LocalDateTime.now());
        juego.setTotal_sessions(juego.getTotal_sessions() + 1);
        remoteController.updateVideogame(juego);
    }

    private void jbTestGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbTestGameActionPerformed
        connectedData();
        Random random = new Random();
        ArrayList<Object> elements = new ArrayList<>();
        elements.add(random.nextInt(30) + 1);
        elements.add(random.nextInt(300) + 50);
        elements.add(LocalDateTime.now());
        elements.add(nickname);
        disconnectedData(elements);
    }//GEN-LAST:event_jbTestGameActionPerformed

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
            java.util.logging.Logger.getLogger(Frontend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frontend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frontend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frontend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frontend().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbPitufoBros;
    private javax.swing.JButton jbTestGame;
    // End of variables declaration//GEN-END:variables
}
