package com.lxz.mahout.practice;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.common.RandomUtils;

public class RecommenderEvaluator {
	public static void main(String[] args) throws IOException{
		RandomUtils.useTestSeed();
		DataModel model = new FileDataModel(new File(""));
//		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
	}
}
