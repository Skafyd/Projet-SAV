import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.table.DefaultTableModel;


public class projetsav extends JFrame {
    private final JTextField nomField;
    private final JTextField prenomField;
    private final JTextField TicketField;
    private final JComboBox typeField;
    private final JTextArea reclamationArea;
    private final DefaultTableModel tableModel;
    private String result;

    public projetsav() {
        setTitle("Formulaire de Service après-vente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] optionsToChoose = {"réparation", "retour", "maintenant"};

        JPanel formulairePanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);

        // Label et champ pour le nom
        constraints.gridx = 0;
        constraints.gridy = 0;
        formulairePanel.add(new JLabel("Nom :"), constraints);

        constraints.gridx = 1;
        nomField = new JTextField(20);
        formulairePanel.add(nomField, constraints);

        // Label et champ pour le prénom
        constraints.gridx = 0;
        constraints.gridy = 1;
        formulairePanel.add(new JLabel("Prénom :"), constraints);

        constraints.gridx = 1;
        prenomField = new JTextField(20);
        formulairePanel.add(prenomField, constraints);


        // Label et champ pour le problème
        constraints.gridx = 0;
        constraints.gridy = 3;
        formulairePanel.add(new JLabel("problème :"), constraints);

        constraints.gridx = 1;
        typeField = new JComboBox<>(optionsToChoose);
        formulairePanel.add(typeField, constraints);

        // Label et champ pour la réclamation
        constraints.gridx = 0;
        constraints.gridy = 4;
        formulairePanel.add(new JLabel("Réclamation :"), constraints);

        constraints.gridx = 1;
        constraints.gridwidth = 2;
        reclamationArea = new JTextArea(5, 20);
        reclamationArea.setWrapStyleWord(true);
        reclamationArea.setLineWrap(true);
        formulairePanel.add(new JScrollPane(reclamationArea), constraints);


        TicketField = new JTextField(20); // Ajoutez cette ligne pour initialiser TicketField
        formulairePanel.add(TicketField, constraints);



// Bouton Envoyer
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 3;
        JButton envoyerButton = new JButton("Envoyer");
        envoyerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String Ticket = TicketField.getText();
                String reclamation = reclamationArea.getText();

                // Établir la connexion à la base de données et exécuter la requête d'insertion
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/projetsav", "root", "")) {
                    String insertQuery = "INSERT INTO connectionsav (Nom, Prenom, Reclamation, Ticket) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                        pstmt.setString(1, nom);
                        pstmt.setString(2, prenom);
                        pstmt.setString(3, reclamation);
                        pstmt.setString(4, Ticket);

                        // Exécuter la requête d'insertion
                        pstmt.executeUpdate();

                        // Ajouter les données au modèle de tableau
                        String[] rowData = {"Nom", "Prenom", "Reclamation", "Ticket"};
                        tableModel.addRow(rowData);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("Erreur lors de l'ajout des données dans la base de données.");
                }
            }
        });
        formulairePanel.add(envoyerButton, constraints);


        // Créer le modèle de tableau
        String[] columnNames = {"Nom", "Prenom", "Réclamation", "Ticket"};
        tableModel = new DefaultTableModel(columnNames, 0);

        // Créer le tableau
        JTable tableau = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableau);

        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 3;
        formulairePanel.add(scrollPane, constraints);

        try
        {
            //étape 1: charger la classe de driver
            Class.forName("com.mysql.jdbc.Driver");

            //étape 2: créer l'objet de connexion
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/projetsav?characterEncoding=UTF-8", "root", "");


            //étape 3: créer l'objet statement
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM connectionsav");

            //étape 4: exécuter la requête
            while(res.next()){
                String Nom = res.getString(1);
                String Prenom = res.getString(2);

                String Reclamation = res.getString(3);

                int Ticket = res.getInt(4);
                // Ajouter les données au modèle de tableau
                tableModel.addRow(new Object[]{Nom, Prenom, Reclamation, Ticket});
            }

            System.out.println("tes ok");

        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("erreur");
        }

        add(formulairePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new projetsav();

            }
        });
    }
}