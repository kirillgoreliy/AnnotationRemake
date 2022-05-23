package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class Test @Inject() (){
  private val lazyList = "test1" :: "test2" :: "test3" :: Nil

  def returnList(): List[String] = this.lazyList

}
