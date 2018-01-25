/*
 *
 *  * Copyright (c) 2015 Ivan Hristov <hristov[DOT]iv[AT]gmail[DOT]com>
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * 	http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.mycompany.cloud.controller.test.spark;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class WordsCounterSparkTest implements Serializable {

    public static void main(String[] args) throws IOException {
        WordsCounterSparkTest test = new WordsCounterSparkTest();
        test.count();





        args = new String[] { "D:/tmp/spark/test.txt" };

        if (args.length < 1) {
            System.err.println("Usage: JavaWordCount <file>");
            System.exit(1);
        }

        SparkSession spark = SparkSession.builder().appName("JavaWordCount").master("local").getOrCreate();

        // SparkConf conf = new
        // SparkConf().setAppName("ingini-spark-java8").setMaster("local");

        JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();

        JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());

        JavaPairRDD<String, Integer> counts = words.mapToPair(w -> new Tuple2<String, Integer>(w, 1))
                .reduceByKey((x, y) -> x + y);
        // counts.collect();

        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?, ?> tuple : output) {
            System.out.println(tuple._1() + ":== " + tuple._2());
        }

        spark.stop();
    }
    public void count() throws IOException {
        //GIVEN
        String source = "src/test/resources/gutenberg/4300.txt";
        WordsCounterSpark wordsCounterSpark = new WordsCounterSpark();

        //WHEN
        //List<Tuple2<String, Integer>> result = wordsCounterSpark.count(source);

        //THEN
        //assertThat(Files.hash(new File(Utils.writeToFile("target/spark_output.txt", result)), Hashing.md5()).toString()).isEqualTo("18be35505e1799b1f49af1b99c2b40c5");

    }
}
