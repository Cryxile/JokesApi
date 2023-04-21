package com.example.demo.service.impl;

import com.example.demo.model.OriginalAndTranslatedJoke;
import com.example.demo.service.FileOperationsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.utils.Constants.JOKES_FILE_PATH;
import static com.example.demo.utils.Constants.META_FILE_PATH;

@Service
public class FileOperationsServiceImpl implements FileOperationsService {

    private final File jokeFile = new File(JOKES_FILE_PATH);
    private final File metaFile = new File(META_FILE_PATH);

    @Override
    public void writeToFile(List<OriginalAndTranslatedJoke> origAndTranslJokes, Integer prevListLength) {
        try (RandomAccessFile toJokesFile = new RandomAccessFile(jokeFile, "rw");
             RandomAccessFile toMetaFile = new RandomAccessFile(metaFile, "rw")
        ) {
            Long jokePosition;
            Long jokeMetaPosition;
            String jokeStr;
            Integer jokeStrLength;
            if (toJokesFile.length() < 1) {
                ObjectMapper objectMapper = new ObjectMapper();
                String newJokes = objectMapper.writeValueAsString(origAndTranslJokes);
                jokePosition = toJokesFile.length() + 1;
                toJokesFile.writeBytes(
                        new String(
                                newJokes.getBytes(StandardCharsets.UTF_8),
                                StandardCharsets.ISO_8859_1
                        )
                );
                jokeStr = origAndTranslJokes.get(0).toString();
                jokeStrLength = jokeStr.length();
                jokeMetaPosition = toMetaFile.length();
                toMetaFile.writeBytes(
                        new String(
                                (origAndTranslJokes.get(0).getId().toString()).getBytes(StandardCharsets.UTF_8),
                                StandardCharsets.ISO_8859_1
                        )
                                + ";" + jokePosition + ";" + jokeStrLength + "-" + jokeMetaPosition + "\n"
                );
            } else {
                Integer newListLength = origAndTranslJokes.size();
                toJokesFile.seek(toJokesFile.length() - 1);
                for (Integer i = prevListLength; i < newListLength; i++) {
                    if (!isJokeExistInFile(origAndTranslJokes.get(i).getId())) {
                        toJokesFile.writeBytes("," + "\n");
                        jokePosition = toJokesFile.length();
                        jokeStr = origAndTranslJokes.get(i).toString();
                        toJokesFile.writeBytes(
                                new String(
                                        jokeStr.getBytes(StandardCharsets.UTF_8),
                                        StandardCharsets.ISO_8859_1
                                )
                        );
                        jokeStrLength = jokeStr.length();
                        toJokesFile.seek(toJokesFile.length());
                        toMetaFile.seek(toMetaFile.length());
                        jokeMetaPosition = toMetaFile.length();
                        toMetaFile.writeBytes(
                                new String(
                                        (origAndTranslJokes.get(i).getId().toString())
                                                .getBytes(StandardCharsets.UTF_8),
                                        StandardCharsets.ISO_8859_1
                                )
                                        + ";" + jokePosition + ";" + jokeStrLength + "-" + jokeMetaPosition + "\n"
                        );
                    }
                }
                toJokesFile.writeBytes("]");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void removeFromFile(Integer id) {
        if (metaFile.length() > 0 && jokeFile.length() > 0) {
            if (isJokeExistInFile(id)) {
//                try {
                    //File tempFile = File.createTempFile("temp_data", ".tmp", new File("src/main/java/com/example/demo/db/"));
                    removeFromJokesFile(id);
                    removeFromMetaFile(id);
//                    if (!tempFile.delete()) {
//                        throw new IOException();
//                    }
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
            }
        }
    }

    private void removeFromMetaFile(Integer id) {
        try (RandomAccessFile toMetaFile = new RandomAccessFile(metaFile, "rw")) {
            String[] lineElements;
            Long jokeMetaPosition;
            File tempFile = File.createTempFile("temp_meta", ".tmp", new File("src/main/java/com/example/demo/db/"));

            //TODO Write and read temp string from file (create .tmp file with specific class??)
            StringBuilder tempStringBuilder = new StringBuilder();
            String randAccFileReadLine = toMetaFile.readLine();

            while (randAccFileReadLine != null) {
                lineElements = randAccFileReadLine.split(";");
                if (lineElements[0].equals(id.toString())) {
                    jokeMetaPosition = Long.parseLong(lineElements[2].split("-")[1]);
                    if (jokeMetaPosition + randAccFileReadLine.length() + 1 != toMetaFile.length()) {
                        Integer removingStringLength = randAccFileReadLine.length();
                        Integer removingJokeStringLength = Integer.parseInt(lineElements[2].split("-")[0]);
                        Long newMetaPosition = 0L;
                        Long newJokePosition;
                        String[] splittedLineForMetaPosition;
                        String[] splittedLineForJokePosition;

                        randAccFileReadLine = toMetaFile.readLine();
                        while (randAccFileReadLine != null) {
                            splittedLineForMetaPosition = randAccFileReadLine.split("-");
                            splittedLineForJokePosition = splittedLineForMetaPosition[0].split(";");
                            newMetaPosition = Long.parseLong(splittedLineForMetaPosition[1]) - removingStringLength - 1;
                            newJokePosition = Long.parseLong(splittedLineForJokePosition[1]) - removingJokeStringLength - 2;
                            tempStringBuilder.append(splittedLineForJokePosition[0]).append(";")
                                    .append(newJokePosition).append(";")
                                    .append(splittedLineForJokePosition[2]).append("-")
                                    .append(newMetaPosition).append("\n");
                            putInTempFile(tempStringBuilder.toString(), tempFile);
                            tempStringBuilder.delete(0, tempStringBuilder.length());
                            randAccFileReadLine = toMetaFile.readLine();
                        }
                        toMetaFile.setLength(newMetaPosition);
                        toMetaFile.seek(jokeMetaPosition);
                        Boolean isEndOfFile = false;
                        Integer targetStringNumber = 1;
                        String tempString;
                        while (!isEndOfFile) {
                            tempString = getFromTempFile(tempFile, targetStringNumber);
                            if (!tempString.equals("")) {
                                toMetaFile.writeBytes(tempString + "\n");
                                targetStringNumber += 1;
                            } else {
                                isEndOfFile = true;
                            }
                        }
                    if (!tempFile.delete()) {
                        throw new IOException();
                    }
                        //toMetaFile.writeBytes(tempStringBuilder.toString());
                    } else {
                        toMetaFile.setLength(jokeMetaPosition);
                    }
                    break;
                }
                randAccFileReadLine = toMetaFile.readLine();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void removeFromJokesFile(Integer id) {
        try (RandomAccessFile toJokesFile = new RandomAccessFile(jokeFile, "rw")) {

            List<Long> fileMeta = getFileMeta(id).stream().toList();
            Long jokePosition = fileMeta.get(0);
            Long jokeStringLength = fileMeta.get(1);
            File tempFile = File.createTempFile("temp_jokes", ".tmp", new File("src/main/java/com/example/demo/db/"));

            if (jokePosition + jokeStringLength + 1 != jokeFile.length()) {

                Long saveFrom = jokePosition + jokeStringLength + 2;
                toJokesFile.seek(saveFrom);

                //TODO Write and read temp string from file (create .tmp file with specific class??)
                StringBuilder tempStringBuilder = new StringBuilder();
                String randAccFileReadLine = toJokesFile.readLine();
                while (randAccFileReadLine != null) {
                    tempStringBuilder.append(randAccFileReadLine);
                    tempStringBuilder.append("\n");
                    putInTempFile(tempStringBuilder.toString(), tempFile);
                    tempStringBuilder.delete(0, tempStringBuilder.length());
                    randAccFileReadLine = toJokesFile.readLine();
                }
                toJokesFile.setLength(jokePosition);
                toJokesFile.seek(jokePosition);

                Boolean isEndOfFile = false;
                Integer targetStringNumber = 1;
                String tempString;
                while (!isEndOfFile) {
                    tempString = getFromTempFile(tempFile, targetStringNumber);
                    if (!tempString.equals("")) {
                        toJokesFile.writeBytes(tempString + "\n");
                        targetStringNumber += 1;
                    } else {
                        isEndOfFile = true;
                    }
                }
                    if (!tempFile.delete()) {
                        throw new IOException();
                    }
                //toMetaFile.writeBytes(getFromTempFile());
                //toJokesFile.writeBytes(tempString.toString());
                toJokesFile.setLength(toJokesFile.length() - 1);
            } else {
                toJokesFile.setLength(jokePosition);
                toJokesFile.seek(jokePosition - 2);
                toJokesFile.writeBytes("]");
                toJokesFile.setLength(toJokesFile.length() - 1);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void putInTempFile(String tempString, File tempFile) {
        //try (RandomAccessFile toTempFile = new RandomAccessFile(tempFile, "rw")) {
        try (BufferedWriter oneWroteLine = new BufferedWriter(new FileWriter(tempFile, true))) {
            oneWroteLine.write(tempString);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getFromTempFile(File tempFile, Integer targetStringNumber) {
        try (BufferedReader oneReadLine = new BufferedReader(new FileReader(tempFile))) {
            String currentString = "";
            Integer currentStringNumber = 0;
            while (!currentStringNumber.equals(targetStringNumber)) {
                currentString = oneReadLine.readLine();
                currentStringNumber += 1;
            }
            return currentString;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    private List<Long> getFileMeta(Integer id) {
        List<Long> fileMeta = new ArrayList<>();

        if (metaFile.length() > 0) {
            if (isJokeExistInFile(id)) {
                String currentLine;
                String[] lineElements;

                try (BufferedReader oneReadLine = new BufferedReader(new FileReader(metaFile))) {
                    currentLine = oneReadLine.readLine();
                    while (currentLine != null) {
                        lineElements = currentLine.split(";");
                        if (lineElements[0].equals(id.toString())) {
                            fileMeta.add(Long.parseLong(lineElements[1]));
                            fileMeta.add(Long.parseLong(lineElements[2].split("-")[0]));
                            oneReadLine.close();
                            break;
                        }
                        currentLine = oneReadLine.readLine();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return fileMeta;
    }

    private Boolean isJokeExistInFile(Integer id) {
        String currentLine;
        String[] lineElements;

        try (BufferedReader oneReadLine = new BufferedReader(new FileReader(metaFile))) {
            currentLine = oneReadLine.readLine();
            while (currentLine != null) {
                lineElements = currentLine.split(";");
                if (lineElements[0].equals(id.toString())) {
                    return true;
                }
                currentLine = oneReadLine.readLine();
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }
}
