package com.example.demo.service.impl;

import com.example.demo.model.Joke;
import com.example.demo.service.FileOperationsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.demo.utils.Constants.*;

@Service
public class FileOperationsServiceImpl implements FileOperationsService {

    private final File jokeFile = new File(JOKES_FILE_PATH);
    private final File metaFile = new File(META_FILE_PATH);

    public FileOperationsServiceImpl() {
        try {
            if (jokeFile.createNewFile()) {
                System.out.println("Joke file has been created!");
            }
            if (metaFile.createNewFile()) {
                System.out.println("Meta file has been created!");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void writeJoke(Joke joke) {
        try (RandomAccessFile toJokesFile = new RandomAccessFile(jokeFile, FILE_ACCESS_MODE);
             RandomAccessFile toMetaFile = new RandomAccessFile(metaFile, FILE_ACCESS_MODE)
        ) {
            long jokePosition;
            String jokeStr = joke.toString();
            if (toJokesFile.length() < 1) {
                jokePosition = 1L;
                writeToJokesFile(toJokesFile, jokeStr, false);
                writeToMetaFile(toMetaFile, joke, jokePosition, false);
            } else {
                if (!isJokeExistInFile(joke.getId())) {
                    jokePosition = toJokesFile.length() + 1;
                    writeToJokesFile(toJokesFile, jokeStr, true);
                    writeToMetaFile(toMetaFile, joke, jokePosition, true);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void writeToJokesFile(RandomAccessFile toJokesFile, String jokeStr, boolean isNextWrite) {
        try {
            if (isNextWrite) {
                toJokesFile.seek(toJokesFile.length() - 1);
                toJokesFile.writeBytes("," + "\n");
            } else {
                toJokesFile.writeBytes("[");
            }
            toJokesFile.writeBytes(
                            new String(
                                    jokeStr.getBytes(StandardCharsets.UTF_8),
                                    StandardCharsets.ISO_8859_1
                            )
                            + "]"
            );
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void writeToMetaFile(RandomAccessFile toMetaFile,
                                 Joke joke,
                                 long jokePosition,
                                 boolean isNextWrite) {
        try {
            if (isNextWrite) {
                toMetaFile.seek(toMetaFile.length());
            }
            int jokeStrLength = joke.toString().length();
            long jokeMetaPosition = toMetaFile.length();
            toMetaFile.writeBytes(
                    new String(
                            (joke.getId().toString()).getBytes(StandardCharsets.UTF_8),
                            StandardCharsets.ISO_8859_1
                    )
                            + ";" + jokePosition + ";" + jokeStrLength + "-" + jokeMetaPosition + "\n"
            );
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void removeJoke(int id) {
        if (metaFile.length() > 0 && jokeFile.length() > 0) {
            if (isJokeExistInFile(id)) {
                removeFromJokesFile(id);
                removeFromMetaFile(id);
            }
        }
    }

    private void removeFromJokesFile(int id) {
        try (RandomAccessFile toJokesFile = new RandomAccessFile(jokeFile, FILE_ACCESS_MODE)) {
            List<Long> fileMeta = Objects.requireNonNull(getJokeMeta(id)).stream().toList();
            Long jokePosition = fileMeta.get(0);
            Long jokeStringLength = fileMeta.get(1);
            File tempFile = File.createTempFile("temp_jokes", FILE_EXTENSION_TYPE, new File(FILE_CREATION_PATH));
            if (jokePosition + jokeStringLength + 1 != jokeFile.length()) {
                long saveFrom = jokePosition + jokeStringLength + 2;
                toJokesFile.seek(saveFrom);
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
                writeTempData(tempFile, toJokesFile);
                toJokesFile.setLength(toJokesFile.length() - 1);
            } else {
                toJokesFile.setLength(jokePosition);
                toJokesFile.seek(jokePosition - 2);
                toJokesFile.writeBytes("]");
                toJokesFile.setLength(toJokesFile.length() - 1);
            }
            if (!tempFile.delete()) {
                throw new FileNotFoundException();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private List<Long> getJokeMeta(Integer id) {
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
                            return fileMeta;
                        }
                        currentLine = oneReadLine.readLine();
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return null;
    }

    private void removeFromMetaFile(Integer id) {
        try (RandomAccessFile toMetaFile = new RandomAccessFile(metaFile, FILE_ACCESS_MODE)) {
            File tempFile = File.createTempFile("temp_meta", FILE_EXTENSION_TYPE, new File(FILE_CREATION_PATH));
            StringBuilder tempStringBuilder = new StringBuilder();
            String randAccFileReadLine = toMetaFile.readLine();
            while (randAccFileReadLine != null) {
                String[] lineElements = randAccFileReadLine.split(";");
                if (lineElements[0].equals(id.toString())) {
                    long jokeMetaPosition = Long.parseLong(lineElements[2].split("-")[1]);
                    if (jokeMetaPosition + randAccFileReadLine.length() + 1 != toMetaFile.length()) {
                        int removingStringLength;
                        int removingJokeStringLength = Integer.parseInt(lineElements[2].split("-")[0]);
                        Long newMetaPosition = jokeMetaPosition;
                        Long newJokePosition;
                        String[] splittedLineForJokePosition;
                        randAccFileReadLine = toMetaFile.readLine();
                        while (randAccFileReadLine != null) {
                            splittedLineForJokePosition = randAccFileReadLine.split("-")[0].split(";");
                            newJokePosition = Long.parseLong(splittedLineForJokePosition[1])
                                    - removingJokeStringLength - 2;
                            tempStringBuilder.append(splittedLineForJokePosition[0]).append(";")
                                    .append(newJokePosition).append(";")
                                    .append(splittedLineForJokePosition[2]).append("-")
                                    .append(newMetaPosition).append("\n");
                            putInTempFile(tempStringBuilder.toString(), tempFile);
                            removingStringLength = tempStringBuilder.length() - 1;
                            tempStringBuilder.delete(0, tempStringBuilder.length());
                            newMetaPosition = newMetaPosition + removingStringLength + 1;
                            randAccFileReadLine = toMetaFile.readLine();
                        }
                        toMetaFile.setLength(newMetaPosition);
                        toMetaFile.seek(jokeMetaPosition);
                        writeTempData(tempFile, toMetaFile);
                    } else {
                        toMetaFile.setLength(jokeMetaPosition);
                    }
                    if (!tempFile.delete()) {
                        throw new FileNotFoundException();
                    }
                    break;
                }
                randAccFileReadLine = toMetaFile.readLine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void writeTempData(File tempFile, RandomAccessFile toFile) {
        try {
            boolean isEndOfFile = false;
            int targetStringNumber = 1;
            String tempString;
            while (!isEndOfFile) {
                tempString = getFromTempFile(tempFile, targetStringNumber);
                if (tempString != null) {
                    toFile.writeBytes(tempString + "\n");
                    targetStringNumber++;
                } else {
                    isEndOfFile = true;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void putInTempFile(String tempString, File tempFile) {
        try (BufferedWriter oneWroteLine = new BufferedWriter(new FileWriter(tempFile, true))) {
            oneWroteLine.write(tempString);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private String getFromTempFile(File tempFile, int targetStringNumber) {
        try (BufferedReader oneReadLine = new BufferedReader(new FileReader(tempFile))) {
            String currentString = "";
            Integer currentStringNumber = 0;
            while (!currentStringNumber.equals(targetStringNumber)) {
                currentString = oneReadLine.readLine();
                currentStringNumber += 1;
            }
            return currentString;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
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
            System.out.println(ex.getMessage());
        }
        return false;
    }

    @Override
    public Joke getJoke(Integer id) {
        List<Long> jokeMeta = getJokeMeta(id);
        Joke askedJoke = new Joke();
        if (jokeMeta != null) {
            askedJoke = convertJoke(jokeMeta);
        }
        return askedJoke;
    }

    private Joke convertJoke(List<Long> jokeMeta) {
        ObjectMapper objectMapper = new ObjectMapper();
        long jokePosition = jokeMeta.get(0);
        String jokeString = getJokeFromFile(jokePosition);
        Joke askedJoke = new Joke();
        try {
            askedJoke = objectMapper.readValue(jokeString, Joke.class);
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }
        return askedJoke;
    }

    private String getJokeFromFile(long jokePosition) {
        String askedJoke = "";
        try (RandomAccessFile toJokesFile = new RandomAccessFile(jokeFile, FILE_ACCESS_MODE)) {
            toJokesFile.seek(jokePosition);
            askedJoke = toJokesFile.readLine();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return askedJoke;
    }
}
