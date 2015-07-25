package com.lxz.mahout.practice;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class Kmeans {
	public static final double[][] points = {{1, 1},{2, 1},{1, 2},
											{2, 2},{3, 3},{8, 8},
											{9, 8},{8, 9},{9, 9}};
	public static void writePointsToFile(List<Vector> points, String filename,
										FileSystem fs, Configuration conf) throws IOException{
		Path path = new Path(filename);
		SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, 
									path, LongWritable.class, VectorWritable.class);
		long recNum = 0;
	}
}
