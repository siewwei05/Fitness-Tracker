package DAO;

import model.ActivityRecord;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityRecordDAO {
    private static final String ONGOING_FILE_PATH = "src/main/data/ongoing_exercise";
    private static final String COMPLETED_FILE_PATH = "src/main/data/completed_exercise";

    public void saveOngoing(ActivityRecord record) {
        saveToFile(record, ONGOING_FILE_PATH);
    }

    public void saveCompleted(ActivityRecord record) {
        saveToFile(record, COMPLETED_FILE_PATH);
    }

    private void saveToFile(ActivityRecord record, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(toCsvString(record) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toCsvString(ActivityRecord record) {
        return record.getCategory() + "," + record.getName() + "," +
                record.getDistanceReps() + "," + record.getDuration() + "," +
                record.getCaloriesBurned() + "," + record.isCompleted() + "," +
                (record.getCompletedDateTime() != null ? record.getCompletedDateTime().toString() : "");
    }

    public List<ActivityRecord> loadOngoing() {
        return loadFromFile(ONGOING_FILE_PATH);
    }

    public List<ActivityRecord> loadCompleted() {
        return loadFromFile(COMPLETED_FILE_PATH);
    }

    private List<ActivityRecord> loadFromFile(String filePath) {
        List<ActivityRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    System.out.println("Invalid exercise entry: " + line);
                    continue; // Skip invalid entries
                }
                ActivityRecord record = new ActivityRecord();
                record.setCategory(parts[0]);
                record.setName(parts[1]);
                record.setDistanceReps(parts[2]);
                record.setDuration(Integer.parseInt(parts[3]));
                record.setCaloriesBurned(Integer.parseInt(parts[4]));
                record.setCompleted(Boolean.parseBoolean(parts[5]));
                if (parts.length > 6 && !parts[6].isEmpty()) {
                    record.setCompletedDateTime(LocalDateTime.parse(parts[6]));
                }
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    public void deleteOngoing(ActivityRecord record) {
        deleteFromFile(record, ONGOING_FILE_PATH);
    }

    public void deleteCompleted(ActivityRecord record) {
        deleteFromFile(record, COMPLETED_FILE_PATH);
    }

    private void deleteFromFile(ActivityRecord record, String filePath) {
        List<ActivityRecord> records = loadFromFile(filePath);
        List<ActivityRecord> updatedRecords = new ArrayList<>();

        for (ActivityRecord r : records) {
            if (!r.equals(record)) {
                updatedRecords.add(r);
            } else {
                System.out.println("Deleting record: " + r.getName());
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (ActivityRecord r : updatedRecords) {
                writer.write(toCsvString(r) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveToCompleted(ActivityRecord record) {
        saveCompleted(record);
        deleteOngoing(record);
        System.out.println("Record moved to completed: " + record.getName());
    }
}
