package com.haikuMasterTrainingDataPreprocessorStanford.main;


import com.haikuMasterTrainingDataPreprocessorStanford.data.SentencesTokenTagData;
import com.haikuMasterTrainingDataPreprocessorStanford.factory.SentencesTokenTagDataFactory;
import com.haikuMasterTrainingDataPreprocessorStanford.factory.SentencesTokenTagDataFactoryImpl;

/**
 * Created by Oliver on 7/14/2017.
 */
public class HaikuMasterTrainingDataPreprocessor {


    public static void main(String[] args) throws InterruptedException {
        SentencesTokenTagDataFactory sentencesTokenTagDataFactory = new SentencesTokenTagDataFactoryImpl();
        SentencesTokenTagData sentencesTokenTagData = sentencesTokenTagDataFactory.create();
    }


}