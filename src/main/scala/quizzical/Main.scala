package quizzical

import japgolly.scalajs.react.vdom.html_<^.*
import japgolly.scalajs.react.{Callback, ScalaComponent}
import org.scalajs.dom.document
import quizzical.Question.*

object Main {
  def main(args: Array[String]): Unit = {
    component(Props("What is the capital of France?", List("Paris", "London", "Berlin", "Rome"))).renderIntoDOM(document.getElementById("app"))
  }
}
