package com.haikuMasterTrainingDataPreprocessorStanford.factory;



import com.haikuMasterTrainingDataPreprocessorStanford.data.SentencesTokenTagData;
import com.haikuMasterTrainingDataPreprocessorStanford.tagger.PosTagger;
import com.haikuMasterTrainingDataPreprocessorStanford.writer.DataWriter;
import com.haikuMasterTrainingDataPreprocessorStanford.writer.DataWriterImpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 7/13/2017.
 */
public class SentencesTokenTagDataFactoryImpl implements SentencesTokenTagDataFactory {

    private static final String TOKEN_TAG_DATA_PATH = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\TokenTagData.txt";

    private static final String FILTERED_SENTENCES_DATA_PATH = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\FilteredHaikuMasterTextData.txt";

    private PosTagger posTagger = new PosTagger();

    @Override
    public SentencesTokenTagData create() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<String> filteredSentences = new ArrayList<>();
        List<String> tokenTagDataStringRows = new ArrayList<>();
        int numberOfTaggedWords = 0;
        int numberOfSentences = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\MergedWikiGuthenbergWord2VecData.txt"));
//            br = new BufferedReader(new FileReader("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\DummyData.txt"));
//            br = new BufferedReader(new FileReader("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\WikiWord2VecFile.txt"));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String sentence = br.readLine();
            while (sentence != null) {
                if (!"".equals(sentence)) {
                    numberOfSentences++;
                    StringBuilder stringBuilder = new StringBuilder();
                    String[] tokensArray = sentence.split("\\ ");
                    if (tokensArray.length <= 30) {
                        try {
                            String tagsAsString = posTagger.tag(sentence);
                            String[] tagsArray = tagsAsString.split("\\ ");
                            for (int i = 0; i < tagsArray.length; i++) {
                                String tag = tagsArray[i];
                                String token = tokensArray[i];
                                if ("N".equals(tag) || "AJ".equals(tag) || "V".equals(tag) || "AV".equals(tag)) {
                                    String tokenTagDataStringRow = token + "#" + tag;
                                    tokenTagDataStringRows.add(tokenTagDataStringRow);
                                    numberOfTaggedWords++;
                                    stringBuilder.append(token);
                                    stringBuilder.append(" ");
                                }
                            }
                            String filteredSentence = stringBuilder.toString();
                            System.out.println("Sentence number: " + numberOfSentences);
                            System.out.println("Filtered sentence: " + filteredSentence);
                            filteredSentences.add(filteredSentence);
                        } catch (Exception e) {
                            sentence = br.readLine();
                        }
                    }
                }
                if (filteredSentences.size() % 20000 == 0) {
                    DataWriter filteredSentencesDataWriter = new DataWriterImpl(filteredSentences, FILTERED_SENTENCES_DATA_PATH);
                    filteredSentencesDataWriter.write();
                    filteredSentences.clear();
                }
                if (tokenTagDataStringRows.size() % 20000 == 0) {
                    DataWriter tokenTagDataStringRowsDataWriter = new DataWriterImpl(tokenTagDataStringRows, TOKEN_TAG_DATA_PATH);
                    tokenTagDataStringRowsDataWriter.write();
                    tokenTagDataStringRows.clear();
                }
//                if (numberOfSentences % 100000 == 0) {
//                    posTagger = null;
//                    posTagger = new PosTagger();
//                }
                sentence = br.readLine();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(filteredSentences.size() + " sentences processed in " + (elapsedTime / 1000) / 60 + " minutes and "
                + +(elapsedTime / 1000) % 60 + " seconds");
        System.out.println(numberOfTaggedWords + " token tags were added into TokenTags model");
        return new SentencesTokenTagData(filteredSentences, tokenTagDataStringRows);
    }
}