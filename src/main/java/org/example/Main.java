package org.example;

import org.example.Client;
import org.example.DAO.ClientDAO;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        ClientDAO clientDAO = new ClientDAO();

        // CREATE - zapis nowego klienta
        Client client = new Client(1, "John", "Doe");
        clientDAO.saveClient(client);

        // READ - pobranie klienta po ID
        Client retrievedClient = clientDAO.getClient(client.getPersonalId());
        System.out.println("Retrieved client: " + retrievedClient.getFirstName() + " " + retrievedClient.getLastName());

        // UPDATE - aktualizacja danych klienta
        retrievedClient.setFirstName("Jane");
        clientDAO.updateClient(retrievedClient);

        // DELETE - usunięcie klienta
        clientDAO.deleteClient(retrievedClient.getPersonalId());
    }
}

/*
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }
 */