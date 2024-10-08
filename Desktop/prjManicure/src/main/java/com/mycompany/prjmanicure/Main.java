package com.mycompany.prjmanicure;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        SistemaAgendamento sistema = new SistemaAgendamento();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("1. Agendar");
            System.out.println("2. Exibir Agendamentos");
            System.out.println("3. Localizar Agendamento");
            System.out.println("4. Excluir Agendamento");
            System.out.println("5. Atualizar Agendamento");
            System.out.println("6. Sair");
            int opcao = scanner.nextInt();
            scanner.nextLine();  // Consumir a nova linha

            switch (opcao) {
                case 1:
                    // Código para agendar
                    System.out.print("Nome do Cliente: ");
                    String nomeCliente = scanner.nextLine();
                    System.out.print("Telefone do Cliente: ");
                    String telefoneCliente = scanner.nextLine();
                    System.out.print("Nome da Manicure: ");
                    String nomeManicure = scanner.nextLine();
                    System.out.print("Data e Hora (HH:mm dd/MM/yyyy): ");
                    String dataHoraTexto = scanner.nextLine();
                    
                    try {
                        LocalDateTime dataHora = LocalDateTime.parse(dataHoraTexto, DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
                        Cliente cliente = new Cliente(nomeCliente, telefoneCliente);
                        Manicure manicure = new Manicure(nomeManicure);
                        sistema.agendar(cliente, manicure, dataHora);
                    } catch (Exception e) {
                        System.out.println("Erro ao agendar: " + e.getMessage());
                    }
                    break;

                case 2:
                    sistema.exibirAgendamentos();
                    break;

                case 3:
                    // Localizar agendamento
                    System.out.print("Nome do Cliente: ");
                    String nomeParaLocalizar = scanner.nextLine();
                    Agendamento agendamento = sistema.localizarAgendamento(nomeParaLocalizar);
                    if (agendamento != null) {
                        System.out.println(agendamento);
                    } else {
                        System.out.println("Agendamento não encontrado.");
                    }
                    break;

                case 4:
                    // Excluir agendamento
                    System.out.print("Data e Hora do Agendamento para excluir (HH:mm dd/MM/yyyy): ");
                    String dataHoraExcluirTexto = scanner.nextLine();
                    try {
                        LocalDateTime dataHoraExcluir = LocalDateTime.parse(dataHoraExcluirTexto, DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
                        sistema.excluirAgendamento(dataHoraExcluir);
                        System.out.println("Agendamento excluído com sucesso.");
                    } catch (Exception e) {
                        System.out.println("Erro ao excluir agendamento: " + e.getMessage());
                    }
                    break;

                case 5:
                    // Atualizar agendamento
                    System.out.print("Nome do Cliente para atualizar: ");
                    String nomeParaAtualizar = scanner.nextLine();
                    System.out.print("Novo nome da Manicure: ");
                    String novaManicureNome = scanner.nextLine();
                    System.out.print("Nova data e hora (HH:mm dd/MM/yyyy): ");
                    String novaDataHoraTexto = scanner.nextLine();
                    
                    try {
                        LocalDateTime novaDataHora = LocalDateTime.parse(novaDataHoraTexto, DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
                        Manicure novaManicure = new Manicure(novaManicureNome);
                        sistema.atualizarAgendamento(nomeParaAtualizar, novaManicure, novaDataHora);
                        System.out.println("Agendamento atualizado com sucesso.");
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 6:
                    scanner.close();
                    return;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
