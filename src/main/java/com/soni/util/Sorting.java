package com.soni.util;

import com.soni.panels.data.ClientPanel;
import com.soni.panels.data.ReportPanel;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Sorting {

    public static List<ClientPanel> sortClients(List<ClientPanel> clients, ClientSortBy clientSortBy) {
        ArrayList<ClientPanel> sortedClients = new ArrayList<>(clients);
        Comparator<ClientPanel> comparator;

        switch (clientSortBy) {
            case AGE:
                comparator = Comparator.comparing(ClientPanel::getAge);
                break;
            case NAME:
                comparator = Comparator.comparing(client -> client.getFirstName() + client.getLastName());
                break;
            case NONE:
            default:
                return clients;
        }

        return mergeSort(sortedClients, comparator);
    }

    public static List<ReportPanel> sortReports(List<ReportPanel> reports, ReportSortBy reportSortBy) {
        ArrayList<ReportPanel> sortedReports = new ArrayList<>(reports);
        Comparator<ReportPanel> comparator;

        switch (reportSortBy) {
            case NEWESTFIRST,OLDESTFIRST:
                comparator = Comparator.comparing(ReportPanel::getDate);
                if(reportSortBy == ReportSortBy.NEWESTFIRST)
                {
                    comparator = comparator.reversed();
                }
                break;
            case NONE:
            default:
                return reports;
        }

        return mergeSort(sortedReports, comparator);
    }

    public static <T> ArrayList<T> mergeSort(ArrayList<T> items, Comparator<T> comparator) {
        if (items.size() <= 1) {
            return items;
        }

        int middle = items.size() / 2;
        ArrayList<T> left = new ArrayList<>(items.subList(0, middle));
        ArrayList<T> right = new ArrayList<>(items.subList(middle, items.size()));

        left = mergeSort(left, comparator);
        right = mergeSort(right, comparator);

        return merge(left, right, comparator);
    }

    private static <T> ArrayList<T> merge(ArrayList<T> left, ArrayList<T> right, Comparator<T> comparator) {
        int leftIndex = 0, rightIndex = 0;
        ArrayList<T> merged = new ArrayList<>();

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (comparator.compare(left.get(leftIndex), right.get(rightIndex)) <= 0) {
                merged.add(left.get(leftIndex++));
            } else {
                merged.add(right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            merged.add(left.get(leftIndex++));
        }
        while (rightIndex < right.size()) {
            merged.add(right.get(rightIndex++));
        }

        return merged;
    }
}
