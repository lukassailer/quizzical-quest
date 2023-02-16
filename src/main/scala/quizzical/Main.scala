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
          q.correctAnswer.asInstanceOf[String],
          q.incorrectAnswers.asInstanceOf[js.Array[String]](0),
          q.incorrectAnswers.asInstanceOf[js.Array[String]](1),
          q.incorrectAnswers.asInstanceOf[js.Array[String]](2)
        )
      )
    ).renderIntoDOM(dom.document.getElementById("app"))
  }
}
