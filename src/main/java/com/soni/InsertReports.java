package com.soni;

import java.util.*;
import java.util.concurrent.*;

import com.soni.util.MongoDBConnection;
import org.bson.Document;

public class InsertReports {
    //Default values for threads and reports
    private static final int DEFAULT_NUM_THREADS = 20;
    private static final int DEFAULT_NUM_REPORTS = 10000;

    public /**static*/ void main(String[] args) {
        System.out.println("InsertReports");
        new Settings();
        MongoDBConnection.startConnection();

        //Get client UUIDs from MongoDB
        List<UUID> clients = MongoDBConnection.getInstance().getClients().stream()
                .map(client -> UUID.fromString(client.getString("UUID")))
                .toList();
        int clientCount = clients.size();

        //Read number of threads and reports from arguments if provided
        int numThreads = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_NUM_THREADS;
        int numReports = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_NUM_REPORTS;

        //Create an ExecutorService with a fixed number of threads
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        //Submit tasks to executor
        for (int i = 0; i < numReports; i++) {
            final int reportId = 2000 + i;
            executor.submit(() -> {
                int randomClientIndex = (int) (Math.random() * clientCount);
                Document document = new Document();
                document.append("Title", "Report " + reportId);
                document.append("Report", "This is a report");
                document.append("Date", new Date());
                document.append("Client-UUID", clients.get(randomClientIndex).toString());
                MongoDBConnection.getInstance().insertReportDocument(UUID.randomUUID(), document);
            });
        }

        //Shutdown the executor and wait for termination
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("All reports have been inserted successfully.");
    }
}

