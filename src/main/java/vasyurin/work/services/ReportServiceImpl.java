package vasyurin.work.services;

import vasyurin.work.entities.Transaction;
import vasyurin.work.services.interfaces.ReportService;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReportServiceImpl implements ReportService {
    @Override
    public void printTransactionsTable(List<Transaction> transactions) {

        System.out.println("-----------------------------------------------------------");
        System.out.printf("%-20s %-12s %-10s %10s%n", "Дата", "Категория", "Тип", "Сумма");
        System.out.println("-----------------------------------------------------------");

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        for (Transaction t : transactions) {
            System.out.printf("%-20s %-12s %-10s %10.2f%n",
                    sdf.format(t.getCreated()),
                    t.getCategory(),
                    t.getType(),
                    t.getAmount());
        }
        System.out.println("-----------------------------------------------------------");
    }
}