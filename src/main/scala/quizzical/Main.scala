package quizzical

import org.scalajs.dom
import quizzical.Question

import scala.scalajs.js
import scala.scalajs.js.JSON

object Main {
  def main(args: Array[String]): Unit = {
    val url = "https://the-trivia-api.com/api/questions?limit=1"
    val xhr = new dom.XMLHttpRequest()
    xhr.open("GET", url, false)
    xhr.send()

    val json = JSON.parse(xhr.responseText).asInstanceOf[js.Array[js.Dynamic]]
    val q = json(0)

    Question.component(
      Question.Props(
        q.question.toString,
        List(
          q.correctAnswer.toString,
          "incorrect answer 1",
          "incorrect answer 2",
          "incorrect answer 3"
        )
      )
    ).renderIntoDOM(dom.document.getElementById("app"))
  }
}
