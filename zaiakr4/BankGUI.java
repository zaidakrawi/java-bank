package zaiakr4;

/* GUI klass för banksystem
 * Bygger ett GUI för att interaktivt
 * skapa kund, konton, samt visa information
* @author Zaid Akrawi, zaikr-4
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BankGUI extends JFrame{
	private BankLogic bank;
	private JTextArea outputArea; // visar info till användaren
	
	public BankGUI() {
		bank = new BankLogic();  // objekt för logiken
        setTitle("Bank System - zaikr4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // skapar meny
        JMenuBar menuBar = new JMenuBar();

        // arkiv meny
        JMenu menuArkiv = new JMenu("Arkiv");
        JMenuItem avslutaItem = new JMenuItem("Avsluta");
        avslutaItem.addActionListener(e -> System.exit(0));
        menuArkiv.add(avslutaItem);
        menuBar.add(menuArkiv);

        //Kund meny
        JMenu menuKund = new JMenu("Kund");

        JMenuItem skapaKundItem = new JMenuItem("Skapa kund");
        skapaKundItem.addActionListener(e -> skapaKundDialog());

        JMenuItem visaAllaKunderItem = new JMenuItem("Visa alla kunder");
        visaAllaKunderItem.addActionListener(e -> visaAllaKunder());
        
        JMenuItem visaKundItem = new JMenuItem("Visa kundinfo");
        visaKundItem.addActionListener(e -> visaKundInfo());
        menuKund.add(visaKundItem);

        menuKund.add(skapaKundItem);
        menuKund.add(visaAllaKunderItem);
        
        // ta bort lkund
        JMenuItem taBortKundItem = new JMenuItem("Ta bort kund");
        taBortKundItem.addActionListener(e -> taBortKundDialog());
        menuKund.add(taBortKundItem);
        
        menuBar.add(menuKund);
        	
        
        //Konto
        JMenu menuKonto = new JMenu("Konto");

        JMenuItem skapaKontoItem = new JMenuItem("Skapa konto");
        skapaKontoItem.addActionListener(e -> skapaKontoDialog());

        menuKonto.add(skapaKontoItem);
        
        JMenuItem transaktionItem = new JMenuItem("Insättning / Uttag");
        transaktionItem.addActionListener(e -> transaktionDialog());

        menuKonto.add(transaktionItem);
        
        menuBar.add(menuKonto);
        
        JMenuItem visaTransaktionerItem = new JMenuItem("Visa transaktioner");
        visaTransaktionerItem.addActionListener(e -> visaTransaktionerDialog());
        menuKonto.add(visaTransaktionerItem);
        // ta bort konto
        JMenuItem taBortKontoItem = new JMenuItem("Ta bort konto");
        taBortKontoItem.addActionListener(e -> taBortKontoDialog());
        menuKonto.add(taBortKontoItem);

        setJMenuBar(menuBar);

        // Textområde för att visa info
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
	}
	
	// för att visa alla kunder
	private void visaAllaKunder() {
		outputArea.setText("");
		for (String info : bank.getAllCustomers()) {
			outputArea.append(info + "\n");
		}
	}
	
	// Dialogruta för att skapa kund
    private void skapaKundDialog() {
        JTextField förnamnFält = new JTextField();
        JTextField efternamnFält = new JTextField();
        JTextField personnummerFält = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Förnamn:"));
        panel.add(förnamnFält);
        panel.add(new JLabel("Efternamn:"));
        panel.add(efternamnFält);
        panel.add(new JLabel("Personnummer:"));
        panel.add(personnummerFält);

        int resultat = JOptionPane.showConfirmDialog(
                this, panel, "Skapa ny kund", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultat == JOptionPane.OK_OPTION) {
            String fnamn = förnamnFält.getText();
            String enamn = efternamnFält.getText();
            String pnr = personnummerFält.getText();

            boolean skapad = bank.createCustomer(fnamn, enamn, pnr);
            if (skapad) {
                outputArea.setText("Kund skapad: " + pnr + " " + fnamn + " " + enamn);
            } else {
                outputArea.setText("Kund med personnummer " + pnr + " finns redan.");
            }
        }
    }
    
    
    // Starta programmet
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankGUI());
    }
    
    
    
    private void visaKundInfo() {
        String pnr = JOptionPane.showInputDialog(this, "Ange personnummer:");

        if (pnr != null && !pnr.isEmpty()) {
            List<String> kundInfo = bank.getCustomer(pnr);

            if (kundInfo != null) {
                outputArea.setText("");
                for (String rad : kundInfo) {
                    outputArea.append(rad + "\n");
                }
            } else {
                outputArea.setText("Ingen kund hittades med personnummer: " + pnr);
            }
        }
    }
    
    
    // användaren skriver pnummer, väljer kontotyp, GUI anropar rätt metod i banklogic
    private void skapaKontoDialog() {
        JTextField pnrFält = new JTextField();

        String[] kontotyper = {"Sparkonto", "Kreditkonto"};
        JComboBox<String> kontoVal = new JComboBox<>(kontotyper);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Personnummer:"));
        panel.add(pnrFält);
        panel.add(new JLabel("Välj kontotyp:"));
        panel.add(kontoVal);

        int resultat = JOptionPane.showConfirmDialog(
                this, panel, "Skapa konto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultat == JOptionPane.OK_OPTION) {
            String pnr = pnrFält.getText();
            String typ = (String) kontoVal.getSelectedItem();

            int kontoNummer = -1;

            if (typ.equals("Sparkonto")) {
                kontoNummer = bank.createSavingsAccount(pnr);
            } else if (typ.equals("Kreditkonto")) {
                kontoNummer = bank.createCreditAccount(pnr);
            }

            if (kontoNummer != -1) {
                outputArea.setText("Skapade " + typ + " med kontonummer: " + kontoNummer);
            } else {
                outputArea.setText("Kunde inte skapa konto. Kontrollera personnummer.");
            }
        }
    }
    
    
    
    private void transaktionDialog() {
        JTextField pnrFält = new JTextField();
        JTextField kontoFält = new JTextField();
        JTextField beloppFält = new JTextField();

        String[] val = {"Insättning", "Uttag"};
        JComboBox<String> transaktionVal = new JComboBox<>(val);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Personnummer:"));
        panel.add(pnrFält);
        panel.add(new JLabel("Kontonummer:"));
        panel.add(kontoFält);
        panel.add(new JLabel("Belopp (kr):"));
        panel.add(beloppFält);
        panel.add(new JLabel("Typ av transaktion:"));
        panel.add(transaktionVal);

        int resultat = JOptionPane.showConfirmDialog(this, panel, "Insättning/Uttag", JOptionPane.OK_CANCEL_OPTION);

        if (resultat == JOptionPane.OK_OPTION) {
            try {
                String pnr = pnrFält.getText();
                int kontoNr = Integer.parseInt(kontoFält.getText());
                int belopp = Integer.parseInt(beloppFält.getText());

                boolean lyckades = false;
                String typ = (String) transaktionVal.getSelectedItem();

                if (typ.equals("Insättning")) {
                    lyckades = bank.deposit(pnr, kontoNr, belopp);
                } else {
                    lyckades = bank.withdraw(pnr, kontoNr, belopp);
                }

                if (lyckades) {
                    outputArea.setText(typ + " på " + belopp + " kr lyckades.");
                } else {
                    outputArea.setText("Transaktionen misslyckades. Kontrollera saldo eller personnummer/konto.");
                }

            } catch (NumberFormatException e) {
                outputArea.setText("Felaktigt belopp eller kontonummer.");
            }
        }
    }
    
    
    private void visaTransaktionerDialog() {
        JTextField pnrFält = new JTextField();
        JTextField kontoFält = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Personnummer:"));
        panel.add(pnrFält);
        panel.add(new JLabel("Kontonummer:"));
        panel.add(kontoFält);

        int resultat = JOptionPane.showConfirmDialog(this, panel, "Visa transaktioner", JOptionPane.OK_CANCEL_OPTION);

        if (resultat == JOptionPane.OK_OPTION) {
            try {
                String pnr = pnrFält.getText();
                int kontoNr = Integer.parseInt(kontoFält.getText());

                List<String> transaktioner = bank.getTransactions(pnr, kontoNr);

                outputArea.setText(""); // Töm fältet först

                if (transaktioner == null) {
                    outputArea.setText("Kund eller konto hittades inte.");
                } else if (transaktioner.isEmpty()) {
                    outputArea.setText("Inga transaktioner har gjorts på kontot.");
                } else {
                    outputArea.append("Transaktioner för konto " + kontoNr + ":\n");
                    for (String t : transaktioner) {
                        outputArea.append(t + "\n");
                    }
                }

            } catch (NumberFormatException e) {
                outputArea.setText("Kontonummer måste vara ett heltal.");
            }
        }
    }
    
    
    private void taBortKontoDialog() {
        JTextField pnrFält = new JTextField();
        JTextField kontoFält = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Personnummer:"));
        panel.add(pnrFält);
        panel.add(new JLabel("Kontonummer:"));
        panel.add(kontoFält);

        int resultat = JOptionPane.showConfirmDialog(this, panel, "Ta bort konto", JOptionPane.OK_CANCEL_OPTION);

        if (resultat == JOptionPane.OK_OPTION) {
            try {
                String pnr = pnrFält.getText();
                int kontoNr = Integer.parseInt(kontoFält.getText());

                String info = bank.closeAccount(pnr, kontoNr);

                if (info != null) {
                    outputArea.setText("Konto avslutat:\n" + info);
                } else {
                    outputArea.setText("Konto kunde inte avslutas. Kontrollera personnummer och kontonummer.");
                }

            } catch (NumberFormatException e) {
                outputArea.setText("Kontonummer måste vara ett heltal.");
            }
        }
    }
    
    
    private void taBortKundDialog() {
        String pnr = JOptionPane.showInputDialog(this, "Ange personnummer på kunden som ska tas bort:");

        if (pnr != null && !pnr.isEmpty()) {
            List<String> borttagen = bank.deleteCustomer(pnr);

            if (borttagen != null && !borttagen.isEmpty()) {
                outputArea.setText("Kund borttagen:\n");
                for (String rad : borttagen) {
                    outputArea.append(rad + "\n");
                }
            } else {
                outputArea.setText("Ingen kund hittades med personnummer: " + pnr);
            }
        }
    }
 

}


