package AplicacionBackend;

import clases.Game;
import clases.Player;
import clases.Videogame;
import implementacion.Factory;
import daos.RemoteDAO;
import implementacion.XmlImpl;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import pojo.ConfigXML;

public class Backend extends javax.swing.JFrame {

    private TableDataHandler currentStrategy;
    public static RemoteDAO controller;
    private XmlImpl config;
    private ConfigXML datosConfig;

    public Backend() {
        initComponents();
        config = new XmlImpl();
        String[] datos = config.getConfig();
        datosConfig = new ConfigXML(datos[0], Integer.parseInt(datos[1]), datos[2], datos[3], datos[4]);

        try {
            // Intentar conectarse con la configuración inicial
            if (datos[1].equals("5432")) {
                controller = new Factory("Postgres").createRemoteDAO();
            } else {
                controller = new Factory("MySQL").createRemoteDAO();
            }

            // Verificar conexión inicial
            if (controller.getAllVideogames() == null) {
                config();
                throw new Exception("No se puede conectar con la base de datos.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo establecer conexión con la base de datos.\nPor favor, verifica la configuración.",
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);

           
            config(); // Llamar al método que abre el cuadro de configuración

            // Intentar nuevamente la conexión después de la configuración
            try {
                if (datosConfig.getPort() == 5432) {
                    controller = new Factory("Postgres").createRemoteDAO();
                } else {
                    controller = new Factory("MySQL").createRemoteDAO();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error crítico: No se pudo establecer conexión después de configurar.\nEl programa se cerrará.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(jtbGames);
        buttonGroup.add(jtbVideogames);
        buttonGroup.add(jtbPlayers);
        jbTopPlayers.setVisible(false);
        toggleButtons(false);

    }

    private void config() {
        // Crear campos para los datos de configuración
        JTextField hostField = new JTextField(datosConfig.getHost());
        JTextField portField = new JTextField(String.valueOf(datosConfig.getPort()));
        JTextField userField = new JTextField(datosConfig.getUser());
        JTextField passwordField = new JPasswordField(datosConfig.getPass());

        Object[] message = {
            "Host:", hostField,
            "Port:", portField,
            "User:", userField,
            "Password:", passwordField,};

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
                System.out.println("Entra aqui: "+datosConfig.getHost());
                // Guardar en el archivo XML
                config.saveConfig(datosConfig);

                // Reiniciar la conexión con la base de datos
                if (datosConfig.getPort() == 5432) {
                    controller = new Factory("Postgres").createRemoteDAO();
                } else {
                    controller = new Factory("MySQL").createRemoteDAO();
                }

                JOptionPane.showMessageDialog(this, "Configuración actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar la configuración: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void updateTableContent() {
        switch (getToggledButton()) {

            case 1:
                changeTableStructure(new String[]{"session_id", "game_id", "player_id", "experience", "life_level", "coins", "session_date"}, updateGameData(controller.getAllGames()));
                break;
            case 2:
                //changeTableStructure(new String[] {"player_id", "nick_name", "experience", "life_level", "coins", "session_count", "last_login"}, new String[][] {{}});
                changeTableStructure(new String[]{"player_id", "nick_name", "experience", "life_level", "coins", "session_count", "last_login"}, updatePlayerData(controller.getAllPlayers()));
                break;
            case 3:
                changeTableStructure(new String[]{"game_id", "isbn", "title", "player_count", "total_sessions", "last_session"}, updateVideoGameData(controller.getAllVideogames()));
                break;
            default:
                break;
        }
    }

    public int getToggledButton() {
        if (jtbGames.isSelected()) {
            jbUpdate.setEnabled(false);
            jbCreate.setEnabled(false);
            jbTopPlayers.setVisible(false);
            return 1;
        } else if (jtbPlayers.isSelected()) {
            jbUpdate.setEnabled(true);
            jbCreate.setEnabled(true);
            jbTopPlayers.setVisible(true);
            return 2;
        } else if (jtbVideogames.isSelected()) {
            jbUpdate.setEnabled(true);
            jbCreate.setEnabled(true);
            jbTopPlayers.setVisible(false);
            return 3;
        } else {
            return 0;
        }
    }

    public void toggleButtons(boolean state) {
        jbCreate.setVisible(state);
        jbDestroy.setVisible(state);
        jbUpdate.setVisible(state);
        jbVisualize.setVisible(state);
        jScrollPane1.setVisible(state);
    }

    public void changeTableStructure(String[] columnNames, String[][] initialData) {
        mainTable.setModel(new DefaultTableModel(initialData, columnNames));
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
        jbTopPlayers = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jtbGames = new javax.swing.JToggleButton();
        jtbPlayers = new javax.swing.JToggleButton();
        jtbVideogames = new javax.swing.JToggleButton();
        jbDestroy = new javax.swing.JButton();
        jbVisualize = new javax.swing.JButton();
        jbCreate = new javax.swing.JButton();
        jbUpdate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setLocation(new java.awt.Point(660, 305));
        setMaximumSize(new java.awt.Dimension(580, 370));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Backend Controller");
        jLabel1.setName(""); // NOI18N

        jbTopPlayers.setBackground(new java.awt.Color(102, 0, 0));
        jbTopPlayers.setForeground(new java.awt.Color(255, 255, 255));
        jbTopPlayers.setText("Top 10 players");
        jbTopPlayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbTopPlayersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 211, Short.MAX_VALUE)
                .addComponent(jbTopPlayers)
                .addGap(53, 53, 53))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(jbTopPlayers))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, -1));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jtbGames.setBackground(new java.awt.Color(102, 0, 0));
        jtbGames.setForeground(new java.awt.Color(255, 255, 255));
        jtbGames.setText("Games");
        jtbGames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbGamesActionPerformed(evt);
            }
        });

        jtbPlayers.setBackground(new java.awt.Color(102, 0, 0));
        jtbPlayers.setForeground(new java.awt.Color(255, 255, 255));
        jtbPlayers.setText("Players");
        jtbPlayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbPlayersActionPerformed(evt);
            }
        });

        jtbVideogames.setBackground(new java.awt.Color(102, 0, 0));
        jtbVideogames.setForeground(new java.awt.Color(255, 255, 255));
        jtbVideogames.setText("Videogames");
        jtbVideogames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbVideogamesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtbGames, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jtbPlayers, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
            .addComponent(jtbVideogames, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jtbGames, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtbPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtbVideogames, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(159, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 120, 320));

        jbDestroy.setBackground(new java.awt.Color(0, 0, 0));
        jbDestroy.setForeground(new java.awt.Color(255, 255, 255));
        jbDestroy.setText("Destroy");
        jbDestroy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDestroyActionPerformed(evt);
            }
        });
        getContentPane().add(jbDestroy, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 70, 100, 40));

        jbVisualize.setBackground(new java.awt.Color(0, 0, 0));
        jbVisualize.setForeground(new java.awt.Color(255, 255, 255));
        jbVisualize.setText("Visualize");
        jbVisualize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbVisualizeActionPerformed(evt);
            }
        });
        getContentPane().add(jbVisualize, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 100, 40));

        jbCreate.setBackground(new java.awt.Color(0, 0, 0));
        jbCreate.setForeground(new java.awt.Color(255, 255, 255));
        jbCreate.setText("Create");
        jbCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCreateActionPerformed(evt);
            }
        });
        getContentPane().add(jbCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, 100, 40));

        jbUpdate.setBackground(new java.awt.Color(0, 0, 0));
        jbUpdate.setForeground(new java.awt.Color(255, 255, 255));
        jbUpdate.setText("Update");
        jbUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbUpdateActionPerformed(evt);
            }
        });
        getContentPane().add(jbUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, 100, 40));

        mainTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(mainTable);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 430, 230));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtbGamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbGamesActionPerformed
        toggleButtons(true);
        currentStrategy = new GameTableStrategy();
        updateTableContent();
    }//GEN-LAST:event_jtbGamesActionPerformed

    private void jtbPlayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbPlayersActionPerformed
        toggleButtons(true);
        currentStrategy = new PlayerTableStrategy();
        updateTableContent();
    }//GEN-LAST:event_jtbPlayersActionPerformed

    private void jtbVideogamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtbVideogamesActionPerformed
        toggleButtons(true);
        currentStrategy = new VideogameTableStrategy();
        updateTableContent();
    }//GEN-LAST:event_jtbVideogamesActionPerformed

    private void jbVisualizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbVisualizeActionPerformed
        updateTableContent();
    }//GEN-LAST:event_jbVisualizeActionPerformed

    private void jbCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCreateActionPerformed
        switch (getToggledButton()) {
            case 2: // Jugadores
                createPlayerDialog();
                break;
            case 1: // Juegos
                createGameDialog();
                break;
            case 3: // Videojuegos
                createVideogameDialog();
                break;
            default:
                JOptionPane.showMessageDialog(this, "No ha seleccionado una tabla válida");
                break;
        }
    }//GEN-LAST:event_jbCreateActionPerformed
    private void createPlayerDialog() {
        JTextField tfNickName = new JTextField();
        JTextField tfExperience = new JTextField();
        JTextField tfLifeLevel = new JTextField();
        JTextField tfCoins = new JTextField();
        JTextField tfSessionCount = new JTextField();

        Object[] message = {
            "Nick Name:", tfNickName,
            "Experience:", tfExperience,
            "Life Level:", tfLifeLevel,
            "Coins:", tfCoins,
            "Session Count:", tfSessionCount
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Create Player", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String nickName = tfNickName.getText();
                int experience = Integer.parseInt(tfExperience.getText());
                int lifeLevel = Integer.parseInt(tfLifeLevel.getText());
                int coins = Integer.parseInt(tfCoins.getText());
                int sessionCount = Integer.parseInt(tfSessionCount.getText());

                Player newPlayer = new Player(nickName, experience, lifeLevel, coins, sessionCount, null);
                controller.savePlayer(newPlayer); 

                updateTableContent(); 
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese valores válidos.");
            }
        }
    }

    private void jbUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbUpdateActionPerformed
        int selectedRow = mainTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona alguna linea para actualizar");
            return;
        }

        switch (getToggledButton()) {
            case 2: // Jugadores

                try {
                    String playerId = (String) mainTable.getValueAt(selectedRow, 0); 
                    String nickName = (String) mainTable.getValueAt(selectedRow, 1); 
                    int experience = Integer.parseInt((String) mainTable.getValueAt(selectedRow, 2)); 
                    int lifeLevel = Integer.parseInt((String) mainTable.getValueAt(selectedRow, 3)); 
                    int coins = Integer.parseInt((String) mainTable.getValueAt(selectedRow, 4)); 
                    int sessionCount = Integer.parseInt((String) mainTable.getValueAt(selectedRow, 5));
                    String lastLogin = (String) mainTable.getValueAt(selectedRow, 6); 

                    Player updatedPlayer = new Player(Integer.parseInt(playerId), nickName, experience, lifeLevel, coins, sessionCount);

                    controller.updatePlayer(updatedPlayer);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error actualizando jugador");
                }

                break;

            case 1: // Progreso

                try {
                    String session_id = (String) mainTable.getValueAt(selectedRow, 0);
                    String gameId = (String) mainTable.getValueAt(selectedRow, 1);
                    String playerIdGame = (String) mainTable.getValueAt(selectedRow, 2);
                    int gameExperience = Integer.parseInt((String) mainTable.getValueAt(selectedRow, 3));
                    int gameLifeLevel = Integer.parseInt((String) mainTable.getValueAt(selectedRow, 4));
                    int gameCoins = Integer.parseInt((String) mainTable.getValueAt(selectedRow, 5));
                    String sessionDate = (String) mainTable.getValueAt(selectedRow, 6);

                    controller.updateGame(new Game(Integer.parseInt(session_id), Integer.parseInt(gameId), Integer.parseInt(playerIdGame), gameExperience, gameLifeLevel, gameCoins, LocalDateTime.parse(sessionDate)));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error actualizando juego");
                }
                break;

            case 3: // Videojuegos
                try {
                    int gameIdVG = Integer.parseInt((String) mainTable.getValueAt(selectedRow, 0)); // game_id
                    String isbn = (String) mainTable.getValueAt(selectedRow, 1); // isbn
                    String title = (String) mainTable.getValueAt(selectedRow, 2); // title
                    int playerCount = Integer.parseInt((String) mainTable.getValueAt(selectedRow, 3)); // player_count
                    int totalSessions = Integer.parseInt((String) mainTable.getValueAt(selectedRow, 4)); // total_sessions
                    String lastSession = (String) mainTable.getValueAt(selectedRow, 5); // last_session

                    
                    LocalDateTime lastSessionDateTime = null;
                    if (lastSession != null && !lastSession.isEmpty()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Ajusta el patrón según el formato de tu String
                        lastSessionDateTime = LocalDateTime.parse(lastSession, formatter);
                    }

                    
                    controller.updateVideogame(new Videogame(gameIdVG, isbn, title, playerCount, totalSessions, lastSessionDateTime));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                JOptionPane.showMessageDialog(this, "No ha seleccionado una tabla válida");
                break;
        }

        updateTableContent();
    }//GEN-LAST:event_jbUpdateActionPerformed

    private void jbDestroyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDestroyActionPerformed
        int selectedRow = mainTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona alguna línea para eliminar");
            return;
        }

        switch (getToggledButton()) {
            case 2: // Jugadores
                try {
                    String playerId = (String) mainTable.getValueAt(selectedRow, 0); // player_id
                    controller.deletePlayerById(Integer.parseInt(playerId));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error eliminando jugador");
                }
                break;

            case 1: // Juegos
                try {
                    String sessionId = (String) mainTable.getValueAt(selectedRow, 0); // session_id
                    controller.deleteGameById(Integer.parseInt(sessionId));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error eliminando juego");
                }
                break;

            case 3: // Videojuegos
                try {
                    String gameId = (String) mainTable.getValueAt(selectedRow, 0); // game_id
                    controller.deleteVideogameById(Integer.parseInt(gameId));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error eliminando videojuego");
                }
                break;

            default:
                JOptionPane.showMessageDialog(this, "No ha seleccionado una tabla válida");
                break;
        }

        updateTableContent();
    }//GEN-LAST:event_jbDestroyActionPerformed

    private void jbTopPlayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbTopPlayersActionPerformed
        changeTableStructure(new String[]{"player_id", "nick_name", "experience", "life_level", "coins", "session_count", "last_login"}, updatePlayerData(controller.getTopPlayers()));
        createTop10File(controller.getTopPlayers());
        JOptionPane.showConfirmDialog(this, "Se ha creado la lista del top 10 en ./top10.txt", "Confirmacion", JOptionPane.CLOSED_OPTION);
    }//GEN-LAST:event_jbTopPlayersActionPerformed
    private void createTop10File(ArrayList<Player> topPlayers) {
        String filePath = "top10.txt"; 
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentDate = LocalDateTime.now().format(dateFormatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) { 
            writer.write("=====================================\n");
            writer.write("Top 10 Players - Generated on: " + currentDate + "\n");
            writer.write("=====================================\n");

            int rank = 1;
            for (Player player : topPlayers) {
                writer.write(String.format("%2d. %-15s %5d points\n", rank, player.getNick_name(), player.getExperience()));
                rank++;
            }

            writer.write("\n");
            System.out.println("Top 10 file updated successfully.");
        } catch (IOException e) {
            System.err.println("Error while updating the Top 10 file: " + e.getMessage());
        }
    }

    private void createGameDialog() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void createVideogameDialog() {
        JTextField tfIsbn = new JTextField();
        JTextField tfTitle = new JTextField();

        Object[] message = {
            "ISBN:", tfIsbn,
            "Title:", tfTitle,};

        int option = JOptionPane.showConfirmDialog(this, message, "Create Videogame", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String isbn = tfIsbn.getText();
                String title = tfTitle.getText();
                int playerCount = 0;
                int totalSessions = 0;
                LocalDateTime lastSession = null;

                Videogame newVideogame = new Videogame(isbn, title, playerCount, totalSessions, lastSession);
                controller.saveGame(newVideogame); // Implementar este método
                updateTableContent(); // Actualiza la tabla con el nuevo videojuego
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese valores válidos.");
            }
        }
    }

    public class PlayerTableStrategy implements TableDataHandler {

        @Override
        public String[] getColumnNames() {
            return new String[]{"player_id", "nick_name", "experience", "life_level", "coins", "session_count", "last_login"};
        }

        @Override
        public String[][] getData(RemoteDAO controller) {
            return updatePlayerData(controller.getAllPlayers());
        }
    }

    public class GameTableStrategy implements TableDataHandler {

        @Override
        public String[] getColumnNames() {
            return new String[]{"session_id", "game_id", "player_id", "experience", "life_level", "coins", "session_date"};
        }

        @Override
        public String[][] getData(RemoteDAO controller) {
            return updateGameData(controller.getAllGames());
        }
    }

    public class VideogameTableStrategy implements TableDataHandler {

        @Override
        public String[] getColumnNames() {
            return new String[]{"game_id", "isbn", "title", "player_count", "total_sessions", "last_session"};
        }

        @Override
        public String[][] getData(RemoteDAO controller) {
            
            return new String[][]{{}};  
        }
    }

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
            java.util.logging.Logger.getLogger(Backend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Backend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Backend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Backend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Backend().setVisible(true);
            }
        });
    }

    public String[][] updatePlayerData(ArrayList<Player> list) {
        String[][] finalList = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            finalList[i] = list.get(i).getPlayerDataArray();
        }
        return finalList;
    }

    public String[][] updateVideoGameData(ArrayList<Videogame> list) {
        String[][] finalList = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            finalList[i] = list.get(i).getVideogameDataArray();
        }
        return finalList;
    }

    public String[][] updateGameData(ArrayList<Game> list) {
        String[][] finalList = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            finalList[i] = list.get(i).getGameDataArray();
        }
        return finalList;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbCreate;
    private javax.swing.JButton jbDestroy;
    private javax.swing.JButton jbTopPlayers;
    private javax.swing.JButton jbUpdate;
    private javax.swing.JButton jbVisualize;
    private javax.swing.JToggleButton jtbGames;
    private javax.swing.JToggleButton jtbPlayers;
    private javax.swing.JToggleButton jtbVideogames;
    private javax.swing.JTable mainTable;
    // End of variables declaration//GEN-END:variables
}
