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
import daos.SQLiteDAO;
import implementacion.Factory;
import implementacion.XmlImpl;
import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import pojo.ConfigXML;

/**
 *
 * @author Vespertino
 */
public class Frontend extends javax.swing.JFrame {

    private String nickname;
    private int currentGameID;
    private daos.SQLiteDAO localController;
    private daos.RemoteDAO remoteController;
    private XmlImpl config;
    private ConfigXML datosConfig;
    private String[] datosLocales;

    /**
     * Creates new form Frontend
     */
    public Frontend() {
        localController = new Factory("SQLite").createSQLiteDAO();
        datosLocales = localController.getConfig();
        config = new XmlImpl();
        String[] datos = config.getConfig();
        datosConfig = new ConfigXML(datos[0], Integer.parseInt(datos[1]), datos[2], datos[3], datos[4]);

        try {
            // Intentar conectarse con la configuración inicial
            if (datos[1].equals("5432")) {
                remoteController = new Factory("Postgres").createRemoteDAO();
            } else {
                remoteController = new Factory("MySQL").createRemoteDAO();
            }

            // Verificar conexión inicial
            if (remoteController.getAllVideogames() == null) {
                jButton1ActionPerformed(null);
                throw new Exception("No se puede conectar con la base de datos.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo establecer conexión con la base de datos.\nPor favor, verifica la configuración.",
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);

            jButton1ActionPerformed(null); // Llamar al método que abre el cuadro de configuración

            try {
                if (datosConfig.getPort() == 5432) {
                    remoteController = new Factory("Postgres").createRemoteDAO();
                } else {
                    remoteController = new Factory("MySQL").createRemoteDAO();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error crítico: No se pudo establecer conexión después de configurar.\nEl programa se cerrará.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }

        nickname = choosePlayer();
        initComponents();
        loadGameButtons();
    }

    private void loadGameButtons() {
        ArrayList<Videogame> videogamesList = remoteController.getAllVideogames();
        jPanel2.removeAll();

        if (videogamesList == null || videogamesList.isEmpty()) {
            JOptionPane.showConfirmDialog(this, "No se cargó ningún juego", "Error", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("no hay na");
        }

        jPanel2.setLayout(new java.awt.FlowLayout());

        for (Videogame currentVideogame : videogamesList) {
            JButton gameButton = new JButton(currentVideogame.getTitle());
            gameButton.setName(currentVideogame.getId() + "");
            gameButton.setVisible(true);
            gameButton.setBackground(new Color(102, 0, 0));
            gameButton.setForeground(Color.WHITE);
            gameButton.setSize(64, 18);
            gameButton.setPreferredSize(new Dimension(124, 68));
            gameButton.setMaximumSize(new Dimension(124, 68));
            gameButton.setMinimumSize(new Dimension(124, 68));

            gameButton.addActionListener(evt -> {
                currentGameID = currentVideogame.getId();
                if ("pitufobros".equalsIgnoreCase(currentVideogame.getTitle())) {

                    int response = JOptionPane.showConfirmDialog(this, "AVISO: Este juego esta en fase pruebas. No se almacenarán los datos de la partida", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        new GameThreadClass().startGame();
                    }
                } else {
                    runTestGame();
                }
            });

            jPanel2.add(gameButton);
        }

        // Actualizar la interfaz para que los botones se pongan correctamente
        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private String choosePlayer() {
        String nickName = null;

        while (nickName == null || nickName.trim().isEmpty()) {
            JTextField tfNickName = new JTextField();

            Object[] message = {
                "Nick Name:", tfNickName
            };

            int option = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "Select Player",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            // Si el usuario cierra con la "X" o selecciona "Cancelar"
            if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                int confirmExit = JOptionPane.showConfirmDialog(
                        this,
                        "El nickname es obligatorio. ¿Deseas cerrar el programa?",
                        "Confirmar salida",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirmExit == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }

                continue;
            }

            // Procesar el nickname ingresado
            nickName = tfNickName.getText().trim();

            if (nickName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "El nickname no puede estar vacío. Por favor, ingrésalo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {
                // Comprobar si el jugador existe
                if (remoteController.getPlayerByNickname(nickName) == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "El jugador no existe. Por favor, ingresa un nickname válido.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    nickName = null; // Reiniciar el ciclo
                }
            }
        }

        return nickName;
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jbPitufoBros = new javax.swing.JButton();
        jbTestGame = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        setForeground(new java.awt.Color(204, 204, 204));
        setLocation(new java.awt.Point(600, 300));
        setMaximumSize(new java.awt.Dimension(645, 438));
        setMinimumSize(new java.awt.Dimension(645, 438));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Game App");

        jLabel2.setBackground(new java.awt.Color(204, 0, 0));
        jLabel2.setForeground(new java.awt.Color(153, 0, 0));
        jLabel2.setIconTextGap(0);
        jLabel2.setPreferredSize(new java.awt.Dimension(32, 32));

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setLabel("Config");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setText("Exit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addGap(115, 115, 115)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jbPitufoBros.setBackground(new java.awt.Color(102, 0, 0));
        jbPitufoBros.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbPitufoBros.setForeground(new java.awt.Color(255, 255, 255));
        jbPitufoBros.setText("Pitufo-Bros");
        jbPitufoBros.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jbPitufoBros.setName("jbPitufoGame"); // NOI18N
        jbPitufoBros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPitufoBrosActionPerformed(evt);
            }
        });

        jbTestGame.setBackground(new java.awt.Color(102, 0, 0));
        jbTestGame.setForeground(new java.awt.Color(255, 255, 255));
        jbTestGame.setText("TEST_GAME");
        jbTestGame.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
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
                .addComponent(jbPitufoBros, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbTestGame, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbPitufoBros, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbTestGame, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(290, Short.MAX_VALUE))
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
            while (a) {
                nickname = choosePlayer();
                if (nickname != null) {
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
        datosConfig.setNick_name(nickname);
        config.saveConfig(datosConfig); // se guarda en el xml el ultimo jugador 
        remoteController.savePlayerProgress(juego, jugador);

    }

    private void runTestGame() {
        connectedData();
        Random random = new Random();
        ArrayList<Object> elements = new ArrayList<>();
        elements.add(random.nextInt(30) + 1);
        elements.add(random.nextInt(300) + 50);
        elements.add(LocalDateTime.now());
        elements.add(nickname);

        disconnectedData(elements);
    }

    private void jbTestGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbTestGameActionPerformed
        runTestGame();
    }//GEN-LAST:event_jbTestGameActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        // Crear campos para los datos de configuración
        JTextField hostField = new JTextField(datosConfig.getHost());
        JTextField portField = new JTextField(String.valueOf(datosConfig.getPort()));
        JTextField userField = new JTextField(datosConfig.getUser());
        JTextField passwordField = new JPasswordField(datosConfig.getPass());
        JTextField resolutionField = new JTextField(datosLocales[1]);
        JTextField languageField = new JTextField(datosLocales[2]);
        JTextField soundEnabledField = new JTextField(datosLocales[0]);

        Object[] message = {
            "Host:", hostField,
            "Port:", portField,
            "User:", userField,
            "Password:", passwordField,
            "Resolution", resolutionField,
            "Language", languageField,
            "Sound Enabled", soundEnabledField};

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Configuración de la Base de Datos",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                // Actualizar los datos de configuración
                datosConfig.setHost(hostField.getText());
                datosConfig.setPort(Integer.parseInt(portField.getText()));
                datosConfig.setUser(userField.getText());
                datosConfig.setPass(passwordField.getText());

                // Guardar en el archivo XML
                config.saveConfig(datosConfig);
                String[] dato = new String[3];
                dato[1] = soundEnabledField.getText();
                dato[0] = resolutionField.getText();
                dato[2] = languageField.getText();

                localController.updateConfig(dato);
                // Reiniciar la conexión con la base de datos
                if (datosConfig.getPort() == 5432) {
                    remoteController = new Factory("Postgres").createRemoteDAO();
                } else {
                    remoteController = new Factory("MySQL").createRemoteDAO();
                }

                // Volver a pedir el nickname
                nickname = choosePlayer();

                // Recargar los botones de los juegos
                loadGameButtons();

                JOptionPane.showMessageDialog(this, "Configuración actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar la configuración: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        // Confirmar cierre de sesión
        int confirmLogout = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmLogout == JOptionPane.YES_OPTION) {
            // Cerrar la ventana actual
            this.dispose();

            // Crear una nueva instancia de la interfaz gráfica
            Frontend newFrontend = new Frontend();
            newFrontend.setVisible(true);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbPitufoBros;
    private javax.swing.JButton jbTestGame;
    // End of variables declaration//GEN-END:variables
}
