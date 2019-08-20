package com.ETL

import java.io.FileInputStream
import java.util.Properties

import com.utils.Mysql
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row, SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}

object GetProCityNum {

  def main(args: Array[String]): Unit = {

    //判断路径是否正确
    if(args.length != 2){
      println("目录参数不正确，退出")
      sys.exit()
    }
    val Array(inputPath,outputPath) = args

    val conf = new SparkConf().setAppName(this.getClass.getName).setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext=new SQLContext(sc)

    val files: DataFrame = sqlContext.read.parquet(inputPath)

    val lines: RDD[Row] = files.map(x=>x)
    val schema = SchemaUtils.structtype

    val lineRDD: DataFrame = sqlContext.createDataFrame(lines,schema)
    val province_city: DataFrame = lineRDD.toDF
    province_city.registerTempTable("t_province_city")

    val pro_cityNum: DataFrame = sqlContext.sql("select count(*) ct, provincename,cityname from t_province_city group by provincename,cityname ")

    pro_cityNum.write.mode(SaveMode.Overwrite).jdbc(Mysql.getProperties()._2,"province_city",Mysql.getProperties()._1)


  }

}
