package quizzical

import org.scalajs.dom
import quizzical.Question

import scala.scalajs.js

object Main {
  def main(args: Array[String]): Unit = {
    val url = "https://the-trivia-api.com/api/questions?limit=1"

    dom.fetch(url).`then` { response =>
      response.json().`then` { json =>
        val onlyQuestionObject = json.asInstanceOf[js.Array[js.Dynamic]](0)

        val questionText = onlyQuestionObject.question.toString
        val correctAnswer = onlyQuestionObject.correctAnswer.asInstanceOf[String]
        val incorrectAnswers = onlyQuestionObject.incorrectAnswers.asInstanceOf[js.Array[String]]
        Question.component(
          Question.Props(
            questionText,
            List(
              correctAnswer,
              incorrectAnswers(0),
              incorrectAnswers(1),
              incorrectAnswers(2)
            )
          )
        ).renderIntoDOM(dom.document.getElementById("app"))
      }
    }
  }
}
