package com.utils

import java.util.Properties

object Mysql {

  def getProperties() = {
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "123456")
    val url = "jdbc:mysql://localhost:3306/project?useUnicode=true&characterEncoding=utf8"
    (prop, url)
  }

}
